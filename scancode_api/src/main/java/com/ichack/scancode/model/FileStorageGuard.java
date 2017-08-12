package com.ichack.scancode.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileStorageGuard implements StorageGuard {

  private HashMap<Long, String> map;
  private static final String fileName = "src/main/resources/codes.snc";

  public FileStorageGuard() {
    loadStorage();
  }

  @Override
  public boolean containsData(long code) {
    return map.containsKey(code);
  }

  @Override
  public String getData(long code) {
    if (map != null && map.containsKey(code)) {
      return map.get(code);
    }
    return "null";
  }

  @Override
  public void add(long code, String data) {
    if (map != null) {
      if (!map.containsKey(code)) {
        map.put(code, data);
        saveData();
      }
    }
  }

  private void saveData() {
    try {
      PrintWriter writer = new PrintWriter(fileName, "UTF-8");
      for (Long key : map.keySet()) {
        writer.println(key + " " + map.get(key));
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void loadStorage() {
    Scanner scanner = null;
    try {
      scanner = new Scanner(new File(fileName));
      map = new HashMap<>();
      while (scanner.hasNextInt()) {
        long code = scanner.nextInt();
        String input = scanner.next();

        map.put(code, input);
      }
    } catch (FileNotFoundException e) {
      File file = new File(fileName);
      try {
        file.createNewFile();
        map = new HashMap<>();
      } catch (Exception fileError) {
        Logger.getGlobal().log(Level.SEVERE, String.valueOf(fileError));
      }
    } finally {
      if (scanner != null) {
        scanner.close();
      }
    }
  }

}
