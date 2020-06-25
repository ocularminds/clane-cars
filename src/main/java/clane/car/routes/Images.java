package clane.car.routes;

import clane.car.Fault;
import clane.car.error.DuplicateError;
import clane.car.error.InvalidParamsError;
import clane.car.error.NotFoundError;
import clane.car.error.ValidationError;
import clane.car.error.ValidationErrors;
import clane.car.model.Image;
import clane.car.service.ImageService;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Images class creates REST API which will responsible for handling multiple
 * uploads posted to serve with API path /api/cars/3/images/multiple
 *
 * @author Babatope Festus
 */
@RestController
@RequestMapping(value = "/api/cars/{carId}/images")
public class Images implements Route {

    private final ImageService service;
    private final MessageSource messageSource;

    @Autowired
    Images(ImageService imageService, MessageSource src) {
        this.service = imageService;
        this.messageSource = src;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.ACCEPTED)
    Callable<ResponseEntity<Fault>> create(
            @PathVariable String id, @RequestParam("files") MultipartFile[] files
    ) throws Exception {
        Fault fault = new Fault("00", "File submit. Processing.");
        return () -> {
            service.upload(id, files);
            return ResponseEntity.of(Optional.of(fault));
        };
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Image> findAll(@PathVariable(name = "carId", required = true) String carId) throws Exception {
        return service.find(carId);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Image read(@PathVariable(name = "carId", required = true) String carId,
            @PathVariable(name = "id", required = true) String id) throws Exception {
        return service.find(carId, id);
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
