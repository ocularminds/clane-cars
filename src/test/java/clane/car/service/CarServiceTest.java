package clane.car.service;

import clane.car.Fault;
import clane.car.error.InvalidParamsError;
import clane.car.error.NotFoundError;
import clane.car.model.Car;
import clane.car.model.Category;
import clane.car.model.Tag;
import clane.car.repository.CarRepository;
import clane.car.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;

/**
 * Test cases for the CarService public methods
 *
 * @author Babatope Festus
 */
@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

    @Mock
    CarRepository mockRepository;
    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CarService service;

    @Test
    public void givenCountMethodMockedWhenCountInvokedThenMockValueReturned() {
        when(mockRepository.count()).thenReturn(123L);
        long userCount = mockRepository.count();
        Assert.assertEquals(123L, userCount);
        Mockito.verify(mockRepository).count();
    }

    @Test
    public void testReturnListOfCarsWhenFindAllIsCalled() {
        List<Car> records = new ArrayList<>();
        Category category = new Category("Sedan");
        category.setId(Integer.toString((int) (Math.random() * 123456)));
        Set<Tag> tags = new HashSet<>();
        Car car = new Car("German Machine");
        car.setDescription("Selling black sedan");
        car.setId(Long.toString((long) (Math.random() * 1234567890)));
        car.setNumber(Long.toString((long) (Math.random() * 98654)));
        car.setCategory(category);
        car.setCategory(category);
        car.setTags(tags);
        records.add(car);

        car = new Car("German Machine");
        car.setDescription("Selling black sedan");
        car.setId(Long.toString((long) (Math.random() * 1234567890)));
        car.setNumber(Long.toString((long) (Math.random() * 98654)));
        car.setCategory(category);
        car.setCategory(category);
        car.setTags(tags);
        records.add(car);
        when(mockRepository.findAll()).thenReturn(records);
        List<Car> result = mockRepository.findAll();
        Assert.assertEquals(result.size(), records.size());
        Mockito.verify(mockRepository).findAll();
    }

    @Test
    public void testReturnFaultWithError201WhenCreated() throws Exception {
        String catId = Integer.toString((int) (Math.random() * 123456));
        final String EXPECTED_ERROR = "201";
        Category category = new Category("Sedan");
        category.setId(catId);
        Set<Tag> tags = new HashSet<>();
        Car car = new Car("German Machine");
        car.setDescription("Selling black sedan");
        car.setNumber(Long.toString((long) (Math.random() * 98654)));
        car.setCategory(category);
        car.setCategory(category);
        car.setTags(tags);

        when(categoryRepository.findById(catId)).thenReturn(Optional.ofNullable(category));
        when(mockRepository.count()).thenReturn(7L);
        when(mockRepository.save(car)).thenReturn(car);

        Fault fault = service.create(car);
        Assert.assertEquals(EXPECTED_ERROR, fault.getError());
        Mockito.verify(mockRepository).save(car);
    }

    @Test
    public void testReturnFaultWithError200WhenUpdated() {
        final String EXPECTED_ERROR = "201";
        Car car = new Car("German Machine");
        car.setDescription("Selling black sedan");

        when(categoryRepository.findDistinctByName("Unknown")).thenReturn(Optional.ofNullable(null));
        when(mockRepository.count()).thenReturn(7L);
        when(mockRepository.save(car)).thenReturn(car);

        Fault fault = service.create(car);
        Assert.assertEquals(EXPECTED_ERROR, fault.getError());
        Mockito.verify(mockRepository).save(car);
    }

    @Test
    public void testReturnFaultWithError201WithUnknownWhenCatePropertyIsMissing() {
        String catId = Integer.toString((int) (Math.random() * 123456));
        final String EXPECTED_ERROR = "201";
        Category category = new Category("Unknown");
        category.setId(catId);
        Car car = new Car("Abstract Venza");
        car.setDescription("Selling black sedan");

        when(categoryRepository.findDistinctByName("Unknown")).thenReturn(Optional.ofNullable(null));
        when(categoryRepository.save(any())).thenReturn(category);
        when(mockRepository.count()).thenReturn(7L);
        when(mockRepository.save(car)).thenReturn(car);

        Fault fault = service.create(car);
        Assert.assertEquals(EXPECTED_ERROR, fault.getError());
        Mockito.verify(mockRepository).save(car);

    }

    @Test
    public void testReturnFaultWithError201AndAutomaticallyAddMissingProperties() {
        String catId = Integer.toString((int) (Math.random() * 123456));
        final String EXPECTED_ERROR = "201";
        Category category = new Category("Unknown");
        category.setId(catId);
        Car car = new Car("Toyota Camury");

        when(categoryRepository.findDistinctByName("Unknown")).thenReturn(Optional.ofNullable(null));
        when(categoryRepository.save(any())).thenReturn(category);
        when(mockRepository.count()).thenReturn(7L);
        when(mockRepository.save(car)).thenReturn(car);

        Fault fault = service.create(car);
        Assert.assertEquals(EXPECTED_ERROR, fault.getError());
        Mockito.verify(mockRepository).save(car);

    }

    @Test
    public void testThrowsInvalidParamsErrorWhenNameIsNotSupplied() {
        Exception exception = assertThrows(InvalidParamsError.class, () -> {
            service.create(new Car());
        });
        String expectedMessage = "Car name field is required";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testThrowsNotFoundErrorWhenUpdatingWWithNoneExistingId() {
        String id = "001";
        Exception exception = assertThrows(NotFoundError.class, () -> {
            service.update("001", new Car("Abstract Venza"));
        });
        String expectedMessage = "car with " + id + " not exist";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testReturnsEmptyListWhenSearchTermIsLessThan2Characters() throws Exception {
        List<Car> result = service.search("A");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSearchTermReturnListOfCars() throws Exception {
        List<Car> records = new ArrayList<>();
        Category category = new Category("Sedan");
        category.setId(Integer.toString((int) (Math.random() * 123456)));
        Set<Tag> tags = new HashSet<>();
        Car car = new Car("German Machine");
        car.setDescription("Selling black sedan");
        car.setId(Long.toString((long) (Math.random() * 1234567890)));
        car.setNumber(Long.toString((long) (Math.random() * 98654)));
        car.setCategory(category);
        car.setCategory(category);
        car.setTags(tags);
        records.add(car);

        car = new Car("German Machine");
        car.setDescription("Selling black sedan");
        car.setId(Long.toString((long) (Math.random() * 1234567890)));
        car.setNumber(Long.toString((long) (Math.random() * 98654)));
        car.setCategory(category);
        car.setCategory(category);
        car.setTags(tags);
        records.add(car);

        Specification<Car> spec = (root, query, cb) -> cb.isNull(root);
        when(mockRepository.findAll(any(Specification.class))).thenReturn(records);

        List<Car> result = service.search("luxury");
        Assert.assertEquals(result.size(), records.size());
    }

    @Test
    public void givenCountMethodMockedWhenCountInvokedThenMockedValueReturned() {
        CarRepository localMockRepository = Mockito.mock(CarRepository.class);
        when(localMockRepository.count()).thenReturn(111L);
        long carCount = localMockRepository.count();
        Assert.assertEquals(111L, carCount);
        Mockito.verify(localMockRepository).count();
    }
}
