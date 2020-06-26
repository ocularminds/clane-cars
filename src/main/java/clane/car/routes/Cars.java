package clane.car.routes;

import clane.car.Fault;
import clane.car.error.DuplicateError;
import clane.car.error.InvalidParamsError;
import clane.car.error.NotFoundError;
import clane.car.error.ValidationError;
import clane.car.error.ValidationErrors;
import clane.car.model.Car;
import clane.car.model.Tag;
import clane.car.service.CarService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import javax.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Cars is responsible for creating RESTful endpoints for Cars
 *
 * @author Festus B. Jejelowo
 */
@RestController
@RequestMapping(value = {"/api/cars"})
public class Cars implements Route {

    final CarService service;
    private final MessageSource messageSource;

    @Autowired
    public Cars(CarService carService, MessageSource src) {
        this.service = carService;
        this.messageSource = src;
    }

    /**
     * Returns available cars with code 200
     *
     * @return
     */
    @GetMapping
    public List<Car> findAll() {
        return service.cars();
    }

    /**
     * Returns 201 and new entity if operation successful or 400 if invalid data
     * supplied.
     *
     * @param car
     * @return
     * @throws java.lang.Exception
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Fault create(@RequestBody Car car) throws Exception {
        return service.create(car);
    }

    @GetMapping(value = "/{id}")
    public Car read(@PathVariable(name = "id", required = true) String id) throws Exception {
        return service.car(id);
    }

    @PutMapping(value = "/{id}")
    public Fault update(@PathVariable(name = "id", required = true) String id, @RequestBody Car car) throws Exception {
        return service.update(id, car);
    }

    /**
     * Deletes given car if car and tags is valid. Return 404 if ether list or
     * entry id is incorrect. Return 400 if specified ID does not match a car
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        //service.delete(id);
    }

    @GetMapping(value = "/search/query")
    @ResponseStatus(HttpStatus.OK)
    public List<Car> search(@RequestParam String term) throws Exception {
        return service.search(term);
    }

    /**
     * Returns 201 and new entity if operation successful or 400 if invalid data
     * supplied.
     *
     * @param carId
     * @param tag
     * @return Tag
     */
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Tag addTag(@PathVariable String carId, @RequestBody @Valid Tag tag) {
        return new Tag(null);//service.addTag(carId, tag);
    }

    /**
     * Deletes given entry if list and entry is valid.Return 404 if ether list
     * or entry id is incorrect.Return 400 if specified entry ID does not belong
     * to the list.
     *
     * @param carId
     * @param tagId
     */
    @DeleteMapping("/{tagId}/{cardId}")
    public void deleteEntry(@PathVariable String carId, @PathVariable String tagId) {
        //service.deleteTag(carId, tagId);
    }

    /**
     * Lists all tags in the specified car, 404 if list not found
     *
     * @param id Id of the Car of which tags are to be retrieved
     * @return List of Tag
     */
    @GetMapping("/{id}/tags")
    public List<Tag> getTags(@PathVariable String id) {
        return new ArrayList<>();
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notFound() {
        // No-op, return empty 404
    }

    @ExceptionHandler(NotFoundError.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notFoundException() {
        // No-op, return empty 404
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @Override
    public ValidationErrors processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return validate(fieldErrors, messageSource);
    }

    /**
     *
     * @param e Duplicate Exception
     * @return
     */
    @ExceptionHandler(DuplicateError.class)
    @Override
    public ResponseEntity<ValidationError> duplicateException(final DuplicateError e) {
        return error(e, HttpStatus.TOO_MANY_REQUESTS, e.getMessage());
    }

    /**
     *
     * @param e InvalidParamsError
     * @return
     */
    @ExceptionHandler(InvalidParamsError.class)
    public ResponseEntity<ValidationError> invalidParamsException(final InvalidParamsError e) {
        return error(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
