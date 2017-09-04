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

public class DbStorageGuard implements StorageGuard {

  /**
   * The name of the database where the data is kept.
   */
  private static final String DATABASENAME = "DatabaseCodeStorage";

  /**
   * The name of the domain hosting the database.
   */
  private static final String HOST = "localhost";

  /**
   * The name of the port hosting the database.
   */
  private static final int PORT = 27017;

  /**
   * The name of the collection holding the data.
   */
  private static final String COLLECTION = "DataCollection";

  /**
   * The default username used to connect to the database.
   */
  private static final String USERNAME = "user";

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
   * Create a new DbStorageGuard object, which always
   * connects to the same database and uses the same collection.
   */
  public DbStorageGuard() {
    connectToDatabase();
  }

  /**
   * Initialise all the necessary fields to connect to
   * the database and retrieve the collection.
   */
  private void connectToDatabase() {
    //creating the Mongo client with the given host and port
    mongo = new MongoClient(HOST, PORT);

    //creating the Mongo credentials for the database
    credentials = MongoCredential.createCredential(USERNAME, DATABASENAME,
        "password".toCharArray());

    //accessing the database
    db = mongo.getDatabase(DATABASENAME);

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
   * A method which checks if a code is contained in the database.
   *
   * @param code The code you want to check for.
   * @return A boolean representing whether the code exists in the database.
   */
  @Override
  public boolean containsData(long code) {
    return collection.find(Filters.eq("code", code)).iterator().hasNext();
  }

  /**
   * A method which stores data for a given code, simulating
   * the behaviour of a map (replacing a previous entry if one
   * already exists).
   *
   * @param code The code to which the data coresponds.
   * @param data The data you want to store.
   */
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

  /**
   * Retrieves the data associated with a stored code.
   *
   * @param code The code you want to query data for.
   * @return A String representing the data.
   */
  @Override
  public String getData(long code) {
    if (!containsData(code)) {
      throw new NoSuchElementException("There is no data for that code");
    }

    Document entry = collection.find(Filters.eq("code", code))
        .iterator().next();

    return (String) entry.get("data");
  }
}
