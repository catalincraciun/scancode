package com.ichack.scancode.model.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This implementation of StorageGuard uses a HashMap to store objects
 * and a file called codes.snc to persist the HashMap
 *
 * Note: This implementation is not thread-safe
 */
public class FileStorageGuard implements StorageGuard {

  private static final String FILENAME = "src/main/resources/codes.snc";
  private HashMap<Long, String> map;

  public FileStorageGuard() {
    loadStorage();
  }

  @Override
  public boolean containsData(long code) {
    return map != null && map.containsKey(code);
  }

  @Override
  public String getData(long code) {
    if (map != null && map.containsKey(code)) {
      return map.get(code);
    }
    throw new NoSuchElementException("There is no data for that code");
  }

  @Override
  public void add(long code, String data) {
    if (map != null) {
      map.put(code, data);
      saveData();
    }
  }

  private void saveData() {
    try {
      PrintWriter writer = new PrintWriter(FILENAME, "UTF-8");
      for (Map.Entry<Long, String> entry : map.entrySet()) {
        writer.println(entry.getKey() + " " + entry.getValue());
      }
      writer.close();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, String.valueOf(e));
    }
  }

  private void loadStorage() {
    File file = new File(FILENAME);
    if (file.exists() && file.isFile()) {
      try (Scanner scanner = new Scanner(new File(FILENAME))) {
        map = new HashMap<>();
        while (scanner.hasNextInt()) {
          map.put(scanner.nextLong(), scanner.next());
        }
      } catch (FileNotFoundException e) {
        Logger.getGlobal().log(Level.SEVERE, String.valueOf(e));
      }
    } else {
      try {
        if (!file.createNewFile()) {
          Logger.getGlobal().log(Level.SEVERE, "Could not create file for storage!!");
        }
        map = new HashMap<>();
      } catch (Exception fileError) {
        Logger.getGlobal().log(Level.SEVERE, String.valueOf(fileError));
      }
    }
  }
}
