package com.ichack.scancode.model.storage;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.NoSuchElementException;

public class DbStorageGuard implements StorageGuard {

  private static final String DATABASENAME = "DatabaseCodeStorage";
  private static final String HOST = "localhost";
  private static final int PORT = 27017;
  private static final String COLLECTION = "dataCollection";

  private MongoClient mongo;
  private MongoCredential credentials;
  private MongoDatabase db;
  private MongoCollection<Document> collection;

  public DbStorageGuard() {
    connectToDatabase();
  }

  private void connectToDatabase() {
    //creating the mongo client
    mongo = new MongoClient(HOST, PORT);

    //creating credentials
    credentials = MongoCredential.createCredential("admin", DATABASENAME,
        "password".toCharArray());

    //accessing the database
    db = mongo.getDatabase(DATABASENAME);

    setCollection();
  }

  private void setCollection() {
    //check if collection already exists
    if (db.listCollectionNames().iterator().hasNext()) {
      //exists
      collection = db.getCollection(COLLECTION);
    } else {
      //collection doesn't exist, create it
      db.createCollection(COLLECTION);
      collection = db.getCollection(COLLECTION);
    }
  }

  @Override
  public boolean containsData(long code) {
    return collection.find(Filters.eq("code", code)).iterator().hasNext();
  }

  @Override
  public void add(long code, String data) {
    //simulate map behaviour, delete entry if it already exists
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

    return (String) entry.get("data");
  }
}
