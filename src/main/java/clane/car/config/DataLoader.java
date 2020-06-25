package clane.car.config;

import clane.car.model.Car;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import clane.car.model.Category;
import clane.car.model.Tag;
import clane.car.service.CarService;
import clane.car.service.CategoryService;
import clane.car.service.TagService;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Festus Jejelowo
 * @author mail.festus@gmail.com
 *
 * This class to set some initial data in the database at the launch of the
 * application.
 */
@Component
public class DataLoader implements ApplicationRunner {

    private final CarService cars;
    private final CategoryService categories;
    private final TagService tags;

    static final Logger LOG = Logger.getLogger(DataLoader.class.getName());

    @Autowired
    public DataLoader(
            CarService carsService,
            CategoryService categoryService,
            TagService tagService) {
        this.cars = carsService;
        this.categories = categoryService;
        this.tags = tagService;
    }

    @Override
    public void run(ApplicationArguments aa) throws Exception {
        if (cars.isNotPopulated()) {
            LOG.info("data loading started ..");
            List<String> categoryList = new ArrayList();
            List<String> tagList = new ArrayList();
            Arrays.asList("sedan", "truck", "tanker", "bus").forEach(c -> {
                LOG.log(Level.INFO, "\tcreating category: {0}", c);
                categoryList.add(categories.create(new Category(c)).getData().toString());
            });

            Arrays.asList("electric", "v4", "v6", "v8", "petrol", "luxury", "sports", "off-road").forEach(t -> {
                LOG.log(Level.INFO, "\tcreating tag: {0}", t);
                tagList.add(tags.create(new Tag(t)).getData().toString());
            });
            String[] descriptions = {"UK used", "New Arrival from Canada", "2nd hand used low milleage"};
            Arrays.asList("UK used car", "Toyata Camury", "Lexus Jeep").forEach(c -> {
                LOG.log(Level.INFO, "\tcreating car: {0}", c);
                Car car = new Car(c);
                car.setDescription(descriptions[(int) (Math.random() * 3)]);
                Category cat = new Category();
                int selected = (int) (Math.random() * categoryList.size());
                cat.setId(categoryList.get(selected));
                car.setCategory(cat);
                for (int x = 0; x < 2; x++) {
                    int option = (int) (Math.random() * tagList.size());
                    Tag t = new Tag();
                    t.setId(tagList.get(option));
                    try {
                        car.addTag(t);
                    } catch (Exception ex) {
                        LOG.log(Level.INFO, "\tcannot add tag to car: {0}", c);
                    }
                }
                cars.create(car);
            });
            System.out.println("data loading completed.");
        }
    }
}
