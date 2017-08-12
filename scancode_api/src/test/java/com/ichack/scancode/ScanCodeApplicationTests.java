package com.ichack.scancode;

import java.util.HashMap;
import org.junit.Test;
import org.junit.Assert;

import com.ichack.scancode.controller.ScanCodeController;
import com.ichack.scancode.responses.ScanResult;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ScanCodeApplicationTests {

	private final ScanCodeController controller = new ScanCodeController();

	@Test
  public void invalidAPIKey() {
    HashMap<String, Object> input_map = new HashMap<>();
    input_map.put("image", "my_image");
    input_map.put("apiKey", "invalid");

    Assert.assertEquals(
        new ResponseEntity<ScanResult>(HttpStatus.UNAUTHORIZED),
        controller.scanCode(input_map));
  }
}
