package com.ichack.scancode;

import com.ichack.scancode.model.StorageGuard;
import com.ichack.scancode.controller.ScanCodeController;
import com.ichack.scancode.responses.GeneratedCode;
import com.ichack.scancode.responses.ScanResult;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.junit.Test;
import org.junit.Assert;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ScanCodeApplicationTests {

  private static final String API_KEY = "7D8s2DJK23iD92jdDJksqEQewscxnr24j2Dsncsksddsjejdmnds2";
  private static final int SCAN_TESTS_NUMBER = 7;
  private static final int SCAN_GENERATED_TESTS_NUMBER = 50;

  private class MockStorageGuard implements StorageGuard {

    private HashMap<Long, String> map;

    MockStorageGuard() {
      map = new HashMap<>();

      map.put(593217406L, "test0");
      map.put(5563998139L, "test1");
      map.put(841833976L, "test2");
      map.put(7744917784L, "test3");
      map.put(5115949022L, "test4");
      map.put(7695937394L, "test5");
      map.put(6510494296L, "test6");
    }

    @Override
    public boolean containsData(long code) {
      return map.containsKey(code);
    }

    @Override
    public void add(long code, String data) {
      map.put(code, data);
    }

    @Override
    public String getData(long code) {
      if (containsData(code)) {
        return map.get(code);
      }
      throw new NoSuchElementException("Code does not exist in storage");
    }
  }

	private ScanCodeController controller = new ScanCodeController(new MockStorageGuard());

	@Test
  public void invalidAPIKey() {
    HashMap<String, Object> inputScan = new HashMap<>();
    inputScan.put("image", "my_image");
    inputScan.put("apiKey", "invalid_key");

    Assert.assertEquals(
        new ResponseEntity<ScanResult>(HttpStatus.UNAUTHORIZED),
        controller.scanCode(inputScan));
  }

  @Test
  public void scanCodes() {
	  HashMap<String, Object> inputScan = new HashMap<>();
    inputScan.put("apiKey", API_KEY);

    try {
      for (int i = 0; i < SCAN_TESTS_NUMBER; i++) {
        BufferedImage originalImage =
            ImageIO.read(new File("src/test/resources/test" + i + "_input.JPG"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "jpg", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        String base64 = Base64.getEncoder().encodeToString(imageInByte);
        inputScan.put("image", base64);

        Assert.assertEquals(
            new ResponseEntity<>(new ScanResult("test" + i), HttpStatus.OK),
            controller.scanCode(inputScan));
      }
    } catch(IOException e){
      Logger.getGlobal().log(Level.SEVERE, String.valueOf(e));
    }
  }

  @Test
  public void scanGeneratedCodes() {
	  for (int i = 0; i < SCAN_GENERATED_TESTS_NUMBER; i++) {
      ResponseEntity<GeneratedCode> result = controller.generateCode("generated" + i, API_KEY);
      Assert.assertEquals(result.getStatusCode(), HttpStatus.OK);

      HashMap<String, Object> inputScan = new HashMap<>();
      inputScan.put("apiKey", API_KEY);
      inputScan.put("image", result.getBody().getImage());

      Assert.assertEquals(
          new ResponseEntity<>(new ScanResult("generated" + i), HttpStatus.OK),
          controller.scanCode(inputScan));
    }
  }
}
