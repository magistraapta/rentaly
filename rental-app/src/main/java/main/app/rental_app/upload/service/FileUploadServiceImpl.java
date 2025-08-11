package main.app.rental_app.upload.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.repository.CarRepository;
import main.app.rental_app.exc.BadRequestException;
import main.app.rental_app.exc.CarNotFoundException;
import main.app.rental_app.exc.ResourceNotFoundException;
import main.app.rental_app.shared.BaseResponse;
import main.app.rental_app.upload.model.CarImage;
import main.app.rental_app.upload.repository.FileUploadRepository;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    // Use absolute path to rentaly parent folder with images directory
    @Value("${file.upload.directory}")
    private String UPLOAD_DIR;

    private final FileUploadRepository fileUploadRepository;
    private final CarRepository carRepository;

    @Override
    public ResponseEntity<BaseResponse<String>> uploadCarImage(MultipartFile file, Long carId) {
        try {
            Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car not found with id: " + carId));
            
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                throw new BadRequestException("Invalid file name");
            }
            
            if (file.isEmpty()) {
                throw new BadRequestException("File is empty");
            }
            
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            String newFileName = carId + "." + fileExtension;
            String filePath = UPLOAD_DIR + newFileName;
            
            createUploadDirectoryIfNotExists();
            
            File destinationFile = new File(filePath);
            file.transferTo(destinationFile);
            
            CarImage carImage = CarImage.builder()
                .car(car)  // Use the car we already validated
                .imageUrl(filePath)
                .build();
            
            fileUploadRepository.save(carImage);
            
            return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Image uploaded successfully", filePath));
            
        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    /**
     * Creates the upload directory if it doesn't exist
     */
    private void createUploadDirectoryIfNotExists() throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

}
