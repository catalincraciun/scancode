package com.ichack.scancode;

import com.ichack.scancode.model.StorageGuard;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.junit.Test;
import org.junit.Assert;

import com.ichack.scancode.controller.ScanCodeController;
import com.ichack.scancode.responses.ScanResult;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ScanCodeApplicationTests {

  private class MockStorageGuard implements StorageGuard {

    @Override
    public boolean containsData(long code) {
      return code == 593217406L ||
          code == 841833976L ||
          code == 5563998139L;
    }

    @Override
    public void add(long code, String data) {
      throw new NotImplementedException();
    }

    @Override
    public String getData(long code) {
      if (code == 593217406L) {
        return "test1";
      } else if (code == 841833976L) {
        return "test3";
      } else if (code == 5563998139L) {
        return "test2";
      }
      return null;
    }
  }

	private final ScanCodeController controller = new ScanCodeController(new MockStorageGuard());

	@Test
  public void invalidAPIKey() {
    HashMap<String, Object> input_map = new HashMap<>();
    input_map.put("image", "my_image");
    input_map.put("apiKey", "invalid");

    Assert.assertEquals(
        new ResponseEntity<ScanResult>(HttpStatus.UNAUTHORIZED),
        controller.scanCode(input_map));
  }

  @Test
  public void scanCode() {
	  HashMap<String, Object> input_map = new HashMap<>();
    input_map.put("apiKey", "7D8s2DJK23iD92jdDJksqEQewscxnr24j2Dsncsksddsjejdmnds2");

    try {
      for (int i = 1; i <= 3; i++) {
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
        Thread.sleep(1000);
      }
    } catch(IOException e){
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
