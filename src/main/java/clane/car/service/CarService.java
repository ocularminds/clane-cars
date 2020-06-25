package clane.car.service;

import clane.car.model.Car;
import clane.car.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;
import clane.car.Fault;
import clane.car.common.IdGenerator;
import clane.car.common.Identifier;
import clane.car.error.InvalidParamsError;
import clane.car.error.NotFoundError;
import clane.car.model.Category;
import clane.car.model.Tag;
import clane.car.repository.CategoryRepository;
import clane.car.repository.TagRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

/**
 * Cars data service provider. This class manages the Creation,
 * Read,Update,Delete operation for all car related entities. it also provides
 * helper methods for card operations
 *
 * @author Jejelowo B. Festus
 */
@Service
public class CarService {

    private final CarRepository repository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final EntityManager em;
    static final Logger LOG = Logger.getLogger(CarService.class.getName());

    @Autowired
    public CarService(CarRepository repo, CategoryRepository categoryRepository,
            TagRepository tagRepository, EntityManager entityManager) {
        this.repository = repo;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.em = entityManager;
    }

    public boolean isNotPopulated() {
        return this.repository.count() == 0;
    }

    /**
     * Update or create a new Car record.
     *
     * @param car
     * @return Car newly created
     * @throws InvalidParamsError Error raised when processing fails
     */
    public Fault create(Car car) throws InvalidParamsError {
        if (car.getName() == null || car.getName().trim().isEmpty()) {
            throw new InvalidParamsError("Car name field is required");
        }
        fillTheBlanks(car, false);
        Car record;

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        StringBuilder sb = new StringBuilder();
        violations.forEach((violation) -> {
            LOG.log(Level.INFO, "{0}: {1}",
                    new Object[]{violation.getPropertyPath(), violation.getMessage()}
            );
            sb.append(violation.getMessage()).append(", ");
        });
        if (violations.size() > 0) {
            throw new InvalidParamsError("Car validation failed. Reason:" + sb.toString());
        }
        record = repository.save(car);
        LOG.log(Level.INFO, "Car {0} created.", car.getName());
        return new Fault(
                Integer.toString(HttpStatus.CREATED.value()),
                "Completed Successfully", record.getId()
        );
    }

    public Fault update(String id, Car input) {
        Car car = repository.findById(id)
                .orElseThrow(() -> new NotFoundError("car with " + id + " not exist"));

        if (input.getDescription() != null && !input.getDescription().isEmpty()) {
            car.setDescription(input.getDescription());
        }
        if (input.getCategory() != null) {
            Category c = categoryRepository.findById(input.getCategory().getId())
                    .orElseThrow(() -> new NotFoundError("Invalid category id"));
            car.setCategory(c);
        }
        if (input.getTags() != null && input.getTags().size() > 0) {
            input.getTags().forEach(tag -> {
                tag = tagRepository.findById(tag.getId()).orElseThrow(
                        () -> new InvalidParamsError("Invalid tag.")
                );
                car.addTag(tag);
            });
        }
        repository.save(car);
        return new Fault(Integer.toString(HttpStatus.OK.value()), "Completed Successfully", car.getId());
    }

    /**
     * Retrieves all Cars, mostly the General Ledger Cars.
     *
     * @return Car records
     */
    @SuppressWarnings("unchecked")
    public List<Car> cars() {
        List<Car> cars = new ArrayList<>();
        repository.findAll().forEach(car -> {
            cars.add(car);
        });
        return cars;
    }

    /**
     * Retrieves an Car record having this id.
     *
     * @param id String Car id as stored in the database
     * @return Car data records
     * @throws Exception Error raised when query fails
     */
    public Car car(String id) throws Exception {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundError("car id: " + id + " not found.")
        );
    }

    /**
     * Retrieves Car details for the Car number
     *
     * @param number Reference Car engine number
     * @return Car details records
     * @throws Exception Error raised when query fails
     */
    public Car of(String number) throws Exception {
        if (number == null) {
            return null;
        }
        return repository.findDistinctByNumber(number).orElse(null);
    }

    /**
     * Retrieves and filter Car by search query term. This allows searching for
     * cars in the catalogue using a single search term that can either match
     * the name, description, category or tags of a car
     *
     * @param term
     * @return Car records
     * @throws Exception Error raised when query fails
     *
     * @Todo
     */
    @SuppressWarnings("unchecked")
    public List<Car> search(final String term) throws Exception {
        if (term == null || term.trim().length() < 2) {
            return Collections.EMPTY_LIST;
        }
        String q = term.toLowerCase();
        Specification<Car> spec = (root, query, cb) -> {
            Root<Car> car = root;
            From join = car.join("category");
            List<Predicate> predicates = Arrays.asList(
                    cb.equal(cb.lower(join.get("name")), q),
                    cb.equal(car.get("number"), q),
                    cb.like(cb.lower(car.get("name")), "%" + q + "%"),
                    cb.like(cb.lower(car.get("description")), "%" + q + "%")
            );

            Expression<Set<Tag>> tags = car.get("tags");
            Subquery<Tag> tagSubQuery = query.subquery(Tag.class);
            Root<Tag> tag = tagSubQuery.from(Tag.class);
            tagSubQuery.select(tag).where(cb.equal(cb.lower(tag.get("name")), q));

            List<Predicate> filter = Arrays.asList(
                    cb.isNotNull(tag),
                    cb.isMember(tag, tags)
            );
            //predicates.add(cb.and(filter.toArray(new Predicate[0])));
            return cb.or(predicates.toArray(new Predicate[0]));
        };
        return repository.findAll(spec);
    }

    /**
     * Fill in other required properties of the car.
     *
     * @param input
     * @param updating
     */
    private void fillTheBlanks(Car input, boolean updating) {
        if (null == input.getDescription()) {
            input.setDescription("New " + input.getName().toLowerCase());
        }
        if (input.getRecord() == null) {
            input.setRecord(new java.util.Date());
        }
        Category category;
        if (input.getCategory() == null || input.getCategory().getId() == null) {
            Optional<Category> result = categoryRepository.findDistinctByName("Unknown");
            category = result.orElse(categoryRepository.save(new Category("Unknown")));
        } else {
            category = categoryRepository.findById(input.getCategory().getId()).get();
        }
        input.setCategory(category);
        if (!updating) {
            input.setId(IdGenerator.getInstance().generate(Identifier.Type.LONG));
            String num = String.format("%05d", repository.count() + 1);
            input.setNumber(num);
        }
    }
}
