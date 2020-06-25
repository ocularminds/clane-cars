package clane.car.routes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * The Categories class create various RESTful endpoints for data entry,
 * retrieval and update for application configurations and other configurations
 * that drive the application life cycle
 *
 * @author Festus B. Jejelowo
 * @author mail.festus@gmail.com
 */
@RestController
public class Index {

    /**
     * New instance with configuration parameters
     */
    LocalDateTime start;

    public Index() {
        start = LocalDateTime.now();
    }

    @GetMapping("/")
    public Map<String, Object> index() throws Exception {
        long timediff = ChronoUnit.SECONDS.between(start, LocalDateTime.now());
        Map<String, Object> object = new HashMap<>();
        object.put("app", "Car Service API");
        object.put("status", "Running. Healthy.");
        object.put("uptime", timediff + "secs");
        return object;
    }

    @GetMapping(value = "/ping")
    public String ping() {
        return "pong";
    }
}
