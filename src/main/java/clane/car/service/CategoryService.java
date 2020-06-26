package clane.car.service;

import clane.car.Fault;
import clane.car.common.JsonParser;
import clane.car.error.InvalidParamsError;
import clane.car.error.NotFoundError;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import clane.car.model.Category;
import clane.car.repository.CategoryRepository;
import java.util.logging.Logger;

/**
 *
 * @author Jejelowo B. Festus
 */
@Service
public class CategoryService {

    private final CategoryRepository repository;
    static final Logger LOG = Logger.getLogger(CategoryService.class.getName());

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        repository = categoryRepository;
    }

    /**
     * Persists a category data to storage. 
     *
     * @param input Category data
     * @return New category data
     */
    public Fault create(Category input) throws InvalidParamsError {        
        Fault fault = new Fault("00", "Success.");
        if (null == input.getName() || input.getName().trim().isEmpty()) {
            throw new InvalidParamsError("Invalid category name");
        }
        LOG.info(JsonParser.toJson(input));
        Category output = repository.save(input);
        fault.setData(output.getId());

        return fault;
    }

    public Fault update(Category input) {
        Category output = repository.findById(input.getId()).orElseThrow(
                () -> new NotFoundError("category " + input.getId())
        );
        if (input.getName() != null) {
            output.setName(input.getName());
        }
        repository.save(output);
        return new Fault("00", "Completed Successfully.");
    }

    public Fault delete(String id) {
        Category output = repository.findById(id).orElseThrow(
                () -> new NotFoundError("category " + id)
        );
        repository.delete(output);
        return new Fault("00", "Completed Successfully.");
    }

    /**
     * Retrieves all category data
     *
     * @return Category records
     */
    @SuppressWarnings("unchecked")
    public List<Category> findAll() {
        return repository.findAll();
    }

    /**
     * Retrieves single category from storage.
     *
     * @param id Category Id
     * @return Category details
     */
    public Category findById(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundError("category " + id));
    }
}
