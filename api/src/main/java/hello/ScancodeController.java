package hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScancodeController {

    private StorageGuard storage;
    private static final String apiKey =
        "7D8s2DJK23iD92jdDJksqEQewscxnr24j2Dsncsksddsjejdmnds2";
    private static final String template = "Alright, %s!";
    private final AtomicLong counter = new AtomicLong();

    public ScancodeController() {
        storage = new StorageGuard();
    }

    @RequestMapping("/generateCode")
    public Greeting generateCode(
        @RequestParam(value="name", defaultValue="Scancode") String name,
        @RequestParam(value="apiKey", defaultValue="null") String clientKey) {
        if (clientKey.equals(apiKey)) {
            // Authorised access
            storage.add((int)counter.get(), name);
            return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
        }
        return null;
    }

    @RequestMapping("/scanCode")
    public Greeting scanCode(
        @RequestParam(value="key", defaultValue="-1") String base64,
        @RequestParam(value="apiKey", defaultValue="null") String clientKey) {
        if (clientKey.equals(apiKey)) {
            // Authorised access
            return new Greeting(counter.incrementAndGet(),
                                storage.getData(new Integer(base64)));
        }
        return null;
    }

}
