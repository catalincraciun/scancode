package hello;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class StorageGuard {

  private HashMap<Long, String> map;
  private static final String fileName = "codes.snc";

  public StorageGuard() {
    loadStorage();
  }

  public void saveData() {
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

  public boolean contains(long code) {
    return map.containsKey(code);
  }

  public String getData(long code) {
    if (map != null && map.containsKey(code)) {
      return map.get(code);
    }
    return "null";
  }

  public void add(long code, String data) {
    if (map != null) {
      if (!map.containsKey(code)) {
        map.put(code, data);
        saveData();
      }
    }
  }

  public void loadStorage() {
    try {
      Scanner scanner = new Scanner(new File(fileName));
      map = new HashMap<>();
      while (scanner.hasNextInt()) {
        long code = scanner.nextInt();
        String input = scanner.next();

        map.put(code, input);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

}
