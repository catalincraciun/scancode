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
  private static final int numTests = 1000;
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

    for (int i = 0; i < numTests; i++) {
      testStorageGuard.add(i, "test" + i);
    }

    for (int i = 0; i < numTests; i++) {
      Assert.assertEquals(testStorageGuard.getData(i), "test" + i);
    }

    for (int i = 1; i <= numTests; i++) {
      testStorageGuard.add(i, "test" + (i + 1));
    }

    Assert.assertEquals(testStorageGuard.getData(0), "test0");

    for (int i = 1; i <= numTests; i++) {
      Assert.assertEquals(testStorageGuard.getData(i), "test" + (i + 1));
    }

    destroyTestStorageGuard();
  }

  @Test(expected = NoSuchElementException.class)
  public void testGetDataException() {
    createTestStorageGuard();
    testStorageGuard.getData(numTests);
    destroyTestStorageGuard();
  }

  private void createTestStorageGuard() {
    testStorageGuard = new DBStorageGuard(TEST_DATABASE_NAME,
        TEST_HOST, TEST_PORT);
  }

  private void destroyTestStorageGuard() {
    MongoClient testMongoClient = testStorageGuard.getMongo();
    testMongoClient.dropDatabase(TEST_DATABASE_NAME);
    testMongoClient.close();
  }

  private void randomizeTestCodes(int numberOfCodes) {
    Random generator = new Random();

    for (int i = 0; i < numberOfCodes; i++) {
      testCodes.add(Math.abs(generator.nextLong()));
    }
  }
}
