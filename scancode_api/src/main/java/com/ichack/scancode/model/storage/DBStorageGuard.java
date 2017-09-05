package com.ichack.scancode.model.storage;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.NoSuchElementException;

/**
 * This implementation of StorageGuard uses a MongoDB database
 * to store objects.
 */
public class DBStorageGuard implements StorageGuard {

  /**
   * The default name of the collection holding the data.
   */
  private static final String COLLECTION = "DataCollection";

  /**
   * The default username used to connect to the database.
   */
  private static final String USERNAME = "user";

  /**
   * The name of the database where the data is to be kept.
   */
  private final String databaseName; //"DatabaseCodeStorage"

  /**
   * The name of the domain hosting the database.
   */
  private final String host; // "localhost";

  /**
   * The name of the port hosting the database.
   */
  private final int port; //27017;

  /**
   * The Mongo client used to connect to the database.
   */
  private MongoClient mongo;

  /**
   * The Mongo credentials used to connect to the database.
   */
  private MongoCredential credentials;

  /**
   * The database.
   */
  private MongoDatabase db;

  /**
   * The collection holding the data.
   */
  private MongoCollection<Document> collection;

  /**
   * Create a new DbStorageGuard object which connects
   * to the given database, on the given host and port.
   *
   * @param databaseName The name of the database to connect to.
   * @param host The name of the host.
   * @param port The name of the port.
   */
  public DBStorageGuard(String databaseName, String host, int port) {
    this.databaseName = databaseName;
    this.host = host;
    this.port = port;
    connectToDatabase();
  }

  /**
   * Initialise all the necessary fields to connect to
   * the database and retrieve the collection.
   */
  private void connectToDatabase() {
    //creating the Mongo client with the given host and port
    mongo = new MongoClient(host, port);

    //creating the Mongo credentials for the database
    credentials = MongoCredential.createCredential(USERNAME, databaseName,
        "password".toCharArray());

    //accessing the database
    db = mongo.getDatabase(databaseName);

    //retrieve the collection
    setCollection();
  }

  /**
   * A method which retrieves the collection if it already exists
   * from a previous instance, otherwise it sets the collection
   * field to a newly created one.
   */
  private void setCollection() {
    if (!db.listCollectionNames().iterator().hasNext()) {
      db.createCollection(COLLECTION);
    }

    collection = db.getCollection(COLLECTION);
  }

  /**
   * Getter method which returns the Mongo client used for this DBStorageGuard.
   * @return A MongoClient object representing the Mongo client.
   */
  public MongoClient getMongo() {
    return mongo;
  }

  @Override
  public boolean containsData(long code) {
    return collection.find(Filters.eq("code", code)).iterator().hasNext();
  }

  @Override
  public void add(long code, String data) {
    if (containsData(code)) {
      collection.deleteOne(collection.find(Filters.eq("code", code))
          .iterator().next());
    }

    Document toAdd = new Document("code", code)
        .append("data", data);
    collection.insertOne(toAdd);
  }

  @Override
  public String getData(long code) {
    if (!containsData(code)) {
      throw new NoSuchElementException("There is no data for that code");
    }

    Document entry = collection.find(Filters.eq("code", code))
        .iterator().next();

    return (String)entry.get("data");
  }
}
