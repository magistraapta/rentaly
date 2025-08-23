package main.app.rental_app.upload.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    // Use absolute path to rentaly parent folder with images directory
    @Value("${file.upload.directory}")
    private String UPLOAD_DIR;

    private final FileUploadRepository fileUploadRepository;
    private final CarRepository carRepository;

    @Override
    public ResponseEntity<BaseResponse<String>> uploadCarImage(MultipartFile file, Long carId) {
        log.info("Starting image upload for car ID: {}", carId);
        log.info("File details: name={}, size={}, empty={}", 
            file != null ? file.getOriginalFilename() : "null",
            file != null ? file.getSize() : "null",
            file != null ? file.isEmpty() : "null");
        
        try {
            Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car not found with id: " + carId));
            
            log.info("Car found: {}", car.getName());
            
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                throw new BadRequestException("Invalid file name");
            }
            
            if (file.isEmpty()) {
                throw new BadRequestException("File is empty");
            }
            
            log.info("Processing file: {}", fileName);
            
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            String newFileName = carId + "_" + System.currentTimeMillis() + "." + fileExtension;
            String filePath = UPLOAD_DIR + newFileName;
            
            log.info("New filename: {}, Full path: {}", newFileName, filePath);
            
            createUploadDirectoryIfNotExists();
            
            File destinationFile = new File(filePath);
            file.transferTo(destinationFile);
            
            log.info("File saved to: {}", destinationFile.getAbsolutePath());
            
            // Store relative URL for web access instead of absolute file path
            String relativeUrl = "/images/" + newFileName;
            
            log.info("Relative URL: {}", relativeUrl);
            
            CarImage carImage = CarImage.builder()
                .car(car)  // Use the car we already validated
                .imageUrl(relativeUrl)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
            
            log.info("CarImage built: {}", carImage);
            
            fileUploadRepository.save(carImage);
            
            log.info("CarImage saved to database with ID: {}", carImage.getId());
            
            return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Image uploaded successfully", filePath));
            
        } catch (ResourceNotFoundException | BadRequestException e) {
            log.error("Error during upload: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during upload: {}", e.getMessage(), e);
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

    @Override
    public List<CarImage> getCarImagesByCarId(Long carId) {
        log.info("Fetching car images for car ID: {}", carId);
        List<CarImage> images = fileUploadRepository.findAllByCarId(carId);
        log.info("Found {} images for car ID: {}", images != null ? images.size() : 0, carId);
        return images;
    }

}

