package main.app.rental_app.upload.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import main.app.rental_app.shared.BaseResponse;
import main.app.rental_app.upload.repository.FileUploadRepository;
import main.app.rental_app.upload.model.CarImage;
import main.app.rental_app.car.repository.CarRepository;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    // Use absolute path to rentaly parent folder with images directory
    private static final String UPLOAD_DIR = "/Users/magistraapta/Desktop/Code/java/rentaly/images/";

    private final FileUploadRepository fileUploadRepository;
    private final CarRepository carRepository;

    @Override
    public ResponseEntity<BaseResponse<String>> uploadCarImage(MultipartFile file, Long carId) {
        try {
            createUploadDirectoryIfNotExists();
            
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                return ResponseEntity.ok(BaseResponse.error(HttpStatus.BAD_REQUEST, "Invalid file name"));
            }
            
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            String newFileName = carId + "." + fileExtension;
            String filePath = UPLOAD_DIR + newFileName;

            // Create the file and transfer the content
            File destinationFile = new File(filePath);
            file.transferTo(destinationFile);

            CarImage carImage = CarImage.builder()
                .car(carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found")))
                .imageUrl(filePath)
                .build();

            fileUploadRepository.save(carImage);

            return ResponseEntity.ok(BaseResponse.success(HttpStatus.OK, "Image uploaded successfully", filePath));
        } catch (Exception e) {
            return ResponseEntity.ok(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image: " + e.getMessage()));
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
