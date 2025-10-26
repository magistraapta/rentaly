package main.app.rental_app.car.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.car.mapper.CarMapper;
import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.model.dto.AddCarRequest;
import main.app.rental_app.car.model.dto.CarDto;
import main.app.rental_app.car.model.enums.CarType;
import main.app.rental_app.car.repository.CarRepository;
import main.app.rental_app.exc.CarNotFoundException;
import main.app.rental_app.shared.BaseResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarServiceImpl implements CarService {

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";
    
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public BaseResponse<List<CarDto>> getAllCars() {
        List<Car> cars = carRepository.findAll();
        if (cars.isEmpty()) {
            throw new CarNotFoundException("No cars found");
        }
        List<CarDto> carDtos = cars.stream()
            .map(carMapper::toDto)
            .collect(Collectors.toList());
        return BaseResponse.success(HttpStatus.OK, "Cars found", carDtos);
    }

    @Override
    public BaseResponse<CarDto> getCarById(Long carId) {
        Car car = carRepository.findById(carId)
            .orElseThrow(() -> new CarNotFoundException("Car not found"));

        if (car == null) {
            throw new CarNotFoundException("Car not found");
        }
        return BaseResponse.success(HttpStatus.OK, "Car found", carMapper.toDto(car));
    }

    @Override
    public BaseResponse<CarDto> getCarByName(String carName) {
        Car car = carRepository.findByName(carName);
        if (car == null) {
            throw new CarNotFoundException("Car not found");
        }   

        return BaseResponse.success(HttpStatus.OK, "Car found", carMapper.toDto(car));
    }

    @Override
    public BaseResponse<List<CarDto>> getCarsByType(CarType carType) {

        List<Car> cars = carRepository.findByCarType(carType);
        if (cars.isEmpty()) {
            throw new CarNotFoundException("No cars found for type: " + carType);
        }
        return BaseResponse.success(HttpStatus.OK, "Cars found", cars.stream().map(carMapper::toDto).collect(Collectors.toList()));
    }

    @Override
    public BaseResponse<CarDto> addCar(AddCarRequest addCarRequest) throws IOException {
        // Validate required fields
        if (addCarRequest.getName() == null || addCarRequest.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Car name is required");
        }
        if (addCarRequest.getPrice() == null || addCarRequest.getPrice() <= 0) {
            throw new IllegalArgumentException("Valid car price is required");
        }
        if (addCarRequest.getStock() == null || addCarRequest.getStock() < 0) {
            throw new IllegalArgumentException("Valid stock quantity is required");
        }
        if (addCarRequest.getImage() == null || addCarRequest.getImage().isEmpty()) {
            throw new IllegalArgumentException("Car image is required");
        }

        String imageUrl = null;
        String uploadedFileName = null;
        
        try {
            // Validate and process image
            MultipartFile image = addCarRequest.getImage();
            
            // Validate file type
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed");
            }
            
            // Validate file size (5MB limit)
            long maxSize = 5 * 1024 * 1024; // 5MB in bytes
            if (image.getSize() > maxSize) {
                throw new IllegalArgumentException("Image size must be less than 5MB");
            }
            
            // Generate unique filename
            String originalFilename = image.getOriginalFilename();
            if (originalFilename == null || originalFilename.trim().isEmpty()) {
                throw new IllegalArgumentException("Image filename is required");
            }
            
            // Sanitize filename
            String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");
            uploadedFileName = System.currentTimeMillis() + "_" + sanitizedFilename;
            
            // Create upload directory if it doesn't exist
            Path uploadDir = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // Save file to filesystem
            Path filePath = uploadDir.resolve(uploadedFileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Set image URL for database
            imageUrl = "/images/" + uploadedFileName;
            
            log.info("Image uploaded successfully: {}", uploadedFileName);
            
        } catch (IOException e) {
            log.error("Failed to upload image: {}", e.getMessage());
            throw new IOException("Failed to upload image: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.error("Image validation failed: {}", e.getMessage());
            throw e;
        }
        
        try {
            // Create and save car entity
            Car car = Car.builder()
                .name(addCarRequest.getName().trim())
                .description(addCarRequest.getDescription() != null ? addCarRequest.getDescription().trim() : null)
                .price(addCarRequest.getPrice())
                .carType(addCarRequest.getCarType())
                .stock(addCarRequest.getStock())
                .imageUrl(imageUrl)
                .build();

            Car savedCar = carRepository.save(car);
            log.info("Car created successfully with ID: {}", savedCar.getId());

            return BaseResponse.success(HttpStatus.OK, "Car added successfully", carMapper.toDto(savedCar));
            
        } catch (Exception e) {
            // Cleanup uploaded file if car creation fails
            if (uploadedFileName != null) {
                try {
                    Path fileToDelete = Paths.get(UPLOAD_DIR, uploadedFileName);
                    Files.deleteIfExists(fileToDelete);
                    log.info("Cleaned up uploaded file after car creation failure: {}", uploadedFileName);
                } catch (IOException cleanupException) {
                    log.error("Failed to cleanup uploaded file: {}", cleanupException.getMessage());
                }
            }
            
            log.error("Failed to create car: {}", e.getMessage());
            throw new IOException("Failed to create car: " + e.getMessage(), e);
        }
    }


    @Override
    public BaseResponse<CarDto> updateCar(Long carId, CarDto carDto) {
        Car car = carRepository.findById(carId)
            .orElseThrow(() -> new CarNotFoundException("Car not found"));

        if (carDto.getName() != null) {
            car.setName(carDto.getName());
        }
        if (carDto.getDescription() != null) {
            car.setDescription(carDto.getDescription());
        }
        if (carDto.getPrice() != null) {
            car.setPrice(carDto.getPrice());
        }
        if (carDto.getCarType() != null) {
            car.setCarType(carDto.getCarType());
        }
        if (carDto.getStock() != null) {
            car.setStock(carDto.getStock());
        }
        if (carDto.getImageUrl() != null) {
            car.setImageUrl(carDto.getImageUrl());
        }

        car.setUpdatedAt(Instant.now());
        carRepository.save(car);
        return BaseResponse.success(HttpStatus.OK, "Car updated", carMapper.toDto(car));
    }

    @Override
    public BaseResponse<CarDto> deleteCar(Long carId) {
        Car car = carRepository.findById(carId)
            .orElseThrow(() -> new CarNotFoundException("Car not found"));
        carRepository.delete(car);
        return BaseResponse.success(HttpStatus.OK, "Car deleted", carMapper.toDto(car));
    }
}
