package clane.car.service;

import clane.car.Fault;
import clane.car.error.InvalidParamsError;
import clane.car.error.NotFoundError;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import clane.car.model.Tag;
import clane.car.repository.TagRepository;

/**
 *
 * @author Jejelowo B. Festus
 */
@Service
public class TagService {

    private final TagRepository repository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        repository = tagRepository;
    }

    /**
     * Persists a terminal data to storage. It connects to the HSM and generate
     * the terminal key as well.
     *
     * @param input Tag data
     * @return New terminal data
     * @throws InvalidParamsError Error raised when query fails
     */
    public Fault create(Tag input) throws InvalidParamsError {
        Fault fault = new Fault("00", "Success. car tag saved.");
        Tag output;
        if (null == input.getName() || input.getName().trim().isEmpty()) {
            throw new InvalidParamsError("Invalid tag name");
        }
        output = repository.save(input);
        fault.setData(output.getId());

        return fault;
    }

    public Fault update(Tag input) {
        Tag output = repository.findById(input.getId()).orElseThrow(
                () -> new NotFoundError("tag " + input.getId())
        );
        if (input.getName() != null) {
            output.setName(input.getName());
        }
        repository.save(output);
        return new Fault("00", "Completed Successfully.");
    }

    public Fault delete(String id) {
        Tag output = repository.findById(id).orElseThrow(
                () -> new NotFoundError("tag " + id)
        );
        repository.delete(output);
        return new Fault("00", "Completed Successfully.");
    }

    /**
     * Retrieves all terminal data
     *
     * @return Tag records
     */
    @SuppressWarnings("unchecked")
    public List<Tag> findAll() {
        return repository.findAll();
    }

    /**
     * Retrieves single terminal from storage.
     *
     * @param id Tag Id
     * @return Beneficiary records
     */
    public Tag findById(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundError("tag " + id));
    }
}
