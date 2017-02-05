package hello;

import hello.corners.CornerAnalyzer;
import hello.corners.PictureUtils;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScancodeController {

    private static final long upperBound = 8589934591L;
    private StorageGuard storage;
    private static final String apiKey =
        "7D8s2DJK23iD92jdDJksqEQewscxnr24j2Dsncsksddsjejdmnds2";
    private static final String template = "Alright, %s!";
    private final AtomicLong counter = new AtomicLong();

    public ScancodeController() {
        storage = new StorageGuard();
    }

    private long getUniqueCode() {
        long rand = ThreadLocalRandom.current().nextLong(upperBound);
        while (storage.contains(rand)) {
            rand = ThreadLocalRandom.current().nextLong(upperBound);
        }
        return rand;
    }

    @RequestMapping("/generateCode")
    public Greeting generateCode(
        @RequestParam(value="data", defaultValue="null") String data,
        @RequestParam(value="apiKey", defaultValue="null") String clientKey) {
        if (clientKey.equals(apiKey)) {
            // Authorised access
            try {
                long rand = getUniqueCode();
                Image generatedImage = new Image(rand);
                storage.add(rand, data);
                return new Greeting(true, generatedImage.getBase64());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new Greeting(false, "null");
        }
        return new Greeting(false, "Unauthorised access");
    }

    @RequestMapping(value="/scanCode", method=RequestMethod.POST)
    public Greeting scanCode(@RequestBody Map<String, Object> map) {
        if (!map.containsKey("apiKey") || !map.containsKey("image")) {
            return new Greeting(false, "Unauthorised access");
        }

        String base64 = (String)map.get("image");
        String apiKey = (String)map.get("apiKey");
        if (apiKey.equals(apiKey) && !base64.equals("null")) {
            // Authorised access
            try {
                Image myImage = new Image(base64);
                CornerAnalyzer analyzer = new CornerAnalyzer(new PictureUtils(myImage.getImage()));
                analyzer.calculateCorners();
                return new Greeting(true, "TopLeft: " + analyzer.getTopLeft() + " TopRight: " +
                    analyzer.getTopRight() + " BottomRight: " + analyzer.getBottomRight() +
                    analyzer.getBottomRight() + " BottomLeft: " + analyzer.getBottomLeft());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Greeting(false, "Unauthorised access");
    }

}
