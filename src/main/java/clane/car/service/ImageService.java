package clane.car.service;

import clane.car.error.NotFoundError;
import clane.car.model.Car;
import clane.car.model.Image;
import clane.car.repository.ImageRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Babatope Festus
 */
@Service
public class ImageService {

    private final ImageRepository repository;
    private final CarService service;
    private final File uploadDirRoot;

    @Autowired
    public ImageService(@Value("${app.upload.dir}") String uploadDir,
            ImageRepository imageRepository,
            CarService carService) {
        this.repository = imageRepository;
        this.service = carService;
        this.uploadDirRoot = new File(uploadDir);
    }

    public List<Image> find(String carId) throws Exception {
        Car car = service.car(carId);
        return repository.findByCar(car);
    }

    public Image find(String carId, String id) throws Exception {
        Car car = service.car(carId);
        return repository.findByCarAndId(car, id).orElseThrow(
                () -> new NotFoundError("Card image not found")
        );
    }

    public void delete(String carId, String id) throws Exception {
        Car car = service.car(carId);
        Image image = find(carId, id);
        car.removeImage(image);
        repository.delete(image);
    }

    public void upload(String carId, MultipartFile[] files) throws Exception {
        Car car = service.car(carId);
        Arrays.asList(files).stream().forEach(file -> {
            File carFile;
            try {
                carFile = uploadPath(car, file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            boolean stored = false;
            try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(carFile)) {
                FileCopyUtils.copy(in, out);
                stored = true;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } finally {
                if (stored) {
                    Image image = new Image(carFile.getName());
                    car.addImage(image);
                    repository.save(image);
                    service.update(carId, car);
                }
            }
        });
    }

    private File uploadPath(Car car, MultipartFile file) throws IOException {
        File uploadPath = Paths.get(this.uploadDirRoot.getPath(), car.getId()).toFile();
        if (uploadPath.exists() == false) {
            uploadPath.mkdirs();
        }
        return new File(uploadPath.getAbsolutePath(), file.getOriginalFilename());
    }
}
