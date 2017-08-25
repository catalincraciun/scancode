package com.ichack.scancode.controllers;

import com.ichack.scancode.model.storage.FileStorageGuard;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ThreadLocalRandom;
import java.io.IOException;

import com.ichack.scancode.model.scanning.Code;
import com.ichack.scancode.model.scanning.CornerAnalyzer;
import com.ichack.scancode.model.scanning.PictureUtils;
import com.ichack.scancode.model.storage.StorageGuard;
import com.ichack.scancode.model.scanning.Image;
import com.ichack.scancode.model.geometry.Point;
import com.ichack.scancode.responses.GeneratedCode;
import com.ichack.scancode.responses.ScanResult;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScanCodeController {

  /* The number of codes we can generate */
  private static final long UPPER_BOUND = 8589934590L;
  /* API Key for simple usage authorisation */
  private static final String API_KEY =
      "7D8s2DJK23iD92jdDJksqEQewscxnr24j2Dsncsksddsjejdmnds2";

  private final StorageGuard storage;

  public ScanCodeController(StorageGuard storage) {
    this.storage = storage;
  }

  public ScanCodeController() {
    this.storage = new FileStorageGuard();
  }

  private long getUniqueCode() {
    long rand = ThreadLocalRandom.current().nextLong(UPPER_BOUND) + 1;
    while (storage.containsData(rand)) {
      rand = ThreadLocalRandom.current().nextLong(UPPER_BOUND);
    }
    return rand;
  }

  @RequestMapping("/generateCode")
  public ResponseEntity<GeneratedCode> generateCode(
      @RequestParam(value = "data", defaultValue = "null") String data,
      @RequestParam(value = "apiKey", defaultValue = "null") String clientKey) {
    if (clientKey.equals(API_KEY)) {
      // Authorised access
      try {
        long rand = getUniqueCode();
        Image generatedImage = new Image(rand);
        storage.add(rand, data);
        return new ResponseEntity<>(
            new GeneratedCode(rand, generatedImage.getBase64()),
            HttpStatus.OK);
      } catch (Exception e) {
        Logger.getGlobal().log(Level.SEVERE, String.valueOf(e));
      }
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

  @RequestMapping(value = "/scanCode", method = RequestMethod.POST)
  public ResponseEntity<ScanResult> scanCode(@RequestBody Map<String, Object> map) {
    if (!map.containsKey("apiKey") || !map.containsKey("image")) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    String base64 = (String) map.get("image");
    String userApiKey = (String) map.get("apiKey");
    if (API_KEY.equals(userApiKey)) {
      // Authorised access
      try {
        Image myImage = new Image(base64);
        CornerAnalyzer analyzer = new CornerAnalyzer(new PictureUtils(myImage.getImage()));
        analyzer.scanCorners();
        Code code = new Code(myImage, new Point[]{
            new Point(analyzer.getTopLeft().getY(), analyzer.getTopLeft().getX()),
            new Point(analyzer.getTopRight().getY(), analyzer.getTopRight().getX()),
            new Point(analyzer.getBottomLeft().getY(), analyzer.getBottomLeft().getX()),
            new Point(analyzer.getBottomRight().getY(), analyzer.getBottomRight().getX())});
        return new ResponseEntity<>(
            new ScanResult(storage.getData(code.getCode())),
            HttpStatus.OK);
      } catch (IOException e) {
        Logger.getGlobal().log(Level.SEVERE, String.valueOf(e));
      }
    }
    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }
}
