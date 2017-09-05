package com.ichack.scancode;

import com.ichack.scancode.model.storage.DBStorageGuard;
import com.mongodb.MongoClient;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class ScancodeStorageTests {

  private static final String TEST_DATABASE_NAME = "TestDatabase";
  private static final String TEST_HOST = "localhost";
  private static final int TEST_PORT = 27017;
  private static final List<Long> testCodes = new ArrayList<>();
  private static final int numTests = 100;
  private DBStorageGuard testStorageGuard;

  @Test
  public void testConnection() {
    createTestStorageGuard();
    try {
      testStorageGuard.getMongo().getAddress();
    } catch (Exception e) {
      Assert.assertTrue("Connection failed!", false);
    } finally {
      destroyTestStorageGuard();
    }

    Assert.assertTrue("Connection successful!", true);
  }

  @Test
  public void testAdd() {
    createTestStorageGuard();
    randomizeTestCodes(numTests);

    for (int i = 0; i < numTests; i++) {
      testStorageGuard.add(testCodes.get(i), "test" + i);
    }

    destroyTestStorageGuard();
    testCodes.clear();
    Assert.assertTrue("Added codes succesfully!", true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDuplicateCodeException() {
    createTestStorageGuard();
    testStorageGuard.add(0, "first code");
    testStorageGuard.add(0, "duplicate not allowed!");
    destroyTestStorageGuard();
  }

  @Test
  public void testContainsData() {
    createTestStorageGuard();
    randomizeTestCodes(numTests);

    for (int i = 0; i < numTests; i++) {
      if (testCodes.get(i) % 2 == 0) {
        testStorageGuard.add(testCodes.get(i), "test" + i);
      }
    }

    for (int i = 0; i < numTests; i++) {
      Assert.assertTrue((testCodes.get(i) % 2 == 1)
          ^ testStorageGuard.containsData(testCodes.get(i)));
    }

    destroyTestStorageGuard();
    testCodes.clear();
  }

  @Test
  public void testGetData() {
    createTestStorageGuard();
    randomizeTestCodes(numTests);

    for (int i = 0; i < numTests; i++) {
      testStorageGuard.add(testCodes.get(i), "test" + i);
    }

    for (int i = 0; i < numTests; i++) {
      Assert.assertEquals(testStorageGuard.getData(testCodes.get(i)),
          "test" + i);
    }

    destroyTestStorageGuard();
    testCodes.clear();
  }

  @Test(expected = NoSuchElementException.class)
  public void testGetDataException() {
    createTestStorageGuard();
    testStorageGuard.getData(numTests);
    destroyTestStorageGuard();
  }

  /**
   * A method that creates the test storage guard.
   */
  private void createTestStorageGuard() {
    testStorageGuard = new DBStorageGuard(TEST_DATABASE_NAME,
        TEST_HOST, TEST_PORT);
  }

  /**
   * A method that destroys the test storage guard.
   */
  private void destroyTestStorageGuard() {
    MongoClient testMongoClient = testStorageGuard.getMongo();
    testMongoClient.dropDatabase(TEST_DATABASE_NAME);
    testMongoClient.close();
  }

  /**
   * This method populates the testCodes with a given number of
   * random distinct long codes.
   *
   * @param numberOfCodes The number of codes to populate the list with.
   */
  private void randomizeTestCodes(int numberOfCodes) {
    Random generator = new Random();
    while(testCodes.size() < numberOfCodes) {
      long codeToAdd = Math.abs(generator.nextLong());
      if (testCodes.contains(codeToAdd)) {
        continue;
      }
      testCodes.add(codeToAdd);
    }
  }
}
