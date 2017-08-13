package com.ichack.scancode;

import com.ichack.scancode.model.StorageGuard;
import com.ichack.scancode.controller.ScanCodeController;
import com.ichack.scancode.responses.ScanResult;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.junit.Test;
import org.junit.Assert;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ScanCodeApplicationTests {

  private static final String API_KEY = "7D8s2DJK23iD92jdDJksqEQewscxnr24j2Dsncsksddsjejdmnds2";
  private static final int TESTS_NUMBER = 7;

  private class MockStorageGuard implements StorageGuard {

    @Override
    public boolean containsData(long code) {
      return code == 593217406L ||
          code == 841833976L ||
          code == 5563998139L ||
          code == 7744917784L ||
          code == 5115949022L ||
          code == 7695937394L ||
          code == 6510494296L;
    }

    @Override
    public void add(long code, String data) {
      throw new NotImplementedException();
    }

    @Override
    public String getData(long code) {
      if (code == 593217406L) {
        return "test0";
      } else if (code == 5563998139L) {
        return "test1";
      } else if (code == 841833976L) {
        return "test2";
      } else if (code == 7744917784L) {
        return "test3";
      } else if (code == 5115949022L) {
        return "test4";
      } else if (code == 7695937394L) {
        return "test5";
      } else if (code == 6510494296L) {
        return "test6";
      }
      throw new ValueException("Invalid code!");
    }
  }

	private final ScanCodeController controller = new ScanCodeController(new MockStorageGuard());

	@Test
  public void invalidAPIKey() {
    HashMap<String, Object> input_map = new HashMap<>();
    input_map.put("image", "my_image");
    input_map.put("apiKey", "invalid_key");

    Assert.assertEquals(
        new ResponseEntity<ScanResult>(HttpStatus.UNAUTHORIZED),
        controller.scanCode(input_map));
  }

  @Test
  public void scanCode() {
	  HashMap<String, Object> input_map = new HashMap<>();
    input_map.put("apiKey", API_KEY);

    try {
      for (int i = 0; i < TESTS_NUMBER; i++) {
        BufferedImage originalImage =
            ImageIO.read(new File("src/test/resources/test" + i + "_input.JPG"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "jpg", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        String base64 = Base64.getEncoder().encodeToString(imageInByte);
        input_map.put("image", base64);

        Assert.assertEquals(
            new ResponseEntity<>(new ScanResult("test" + i), HttpStatus.OK),
            controller.scanCode(input_map));
      }
    } catch(IOException e){
      Logger.getGlobal().log(Level.SEVERE, String.valueOf(e));
    }
  }
}
