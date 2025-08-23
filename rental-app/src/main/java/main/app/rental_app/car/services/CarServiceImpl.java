package main.app.rental_app.car.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import main.app.rental_app.car.mapper.CarMapper;
import main.app.rental_app.car.model.Car;
import main.app.rental_app.car.model.dto.CarDto;
import main.app.rental_app.car.model.dto.CreateCarRequest;
import main.app.rental_app.car.model.enums.CarType;
import main.app.rental_app.car.repository.CarRepository;
import main.app.rental_app.exc.CarNotFoundException;
import main.app.rental_app.shared.BaseResponse;
import main.app.rental_app.upload.service.FileUploadService;
import main.app.rental_app.upload.model.CarImage;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final FileUploadService fileUploadService;

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
    public BaseResponse<CarDto> addCar(CarDto carDto) {
        Car car = carMapper.toEntity(carDto);
        carRepository.save(car);
        return BaseResponse.success(HttpStatus.OK, "Car added", carMapper.toDto(car));
    }

    @Override
    public BaseResponse<CarDto> addCarWithImages(CreateCarRequest createCarRequest) {
        log.info("Processing CreateCarRequest: {}", createCarRequest);
        log.info("Images in request: {}", createCarRequest.getImages() != null ? createCarRequest.getImages().length : "null");
        
        // First, create the car
        Car car = Car.builder()
            .name(createCarRequest.getName())
            .description(createCarRequest.getDescription())
            .price(createCarRequest.getPrice())
            .carType(createCarRequest.getCarType())
            .build();
        
        log.info("Car built: {}", car);
        
        // Save the car to get the ID
        Car savedCar = carRepository.save(car);
        log.info("Car saved with ID: {}", savedCar.getId());
        
        // Upload images if provided
        if (createCarRequest.getImages() != null && createCarRequest.getImages().length > 0) {
            log.info("Processing {} images", createCarRequest.getImages().length);
            for (int i = 0; i < createCarRequest.getImages().length; i++) {
                MultipartFile image = createCarRequest.getImages()[i];
                log.info("Processing image {}: name={}, size={}, empty={}", i, 
                    image != null ? image.getOriginalFilename() : "null",
                    image != null ? image.getSize() : "null",
                    image != null ? image.isEmpty() : "null");
                
                if (image != null && !image.isEmpty()) {
                    log.info("Uploading image {} for car ID {}", i, savedCar.getId());
                    fileUploadService.uploadCarImage(image, savedCar.getId());
                    log.info("Image {} uploaded successfully", i);
                } else {
                    log.warn("Image {} is null or empty, skipping", i);
                }
            }
        } else {
            log.warn("No images provided in request");
        }
        
        // Fetch the car again and manually load its images
        Car carWithImages = carRepository.findById(savedCar.getId())
            .orElseThrow(() -> new CarNotFoundException("Car not found after image upload"));
        
        log.info("Car fetched: {}", carWithImages.getName());
        
        // Since we know images were uploaded, let's manually fetch them
        // This bypasses the JOIN FETCH issues
        List<CarImage> carImages = fileUploadService.getCarImagesByCarId(savedCar.getId());
        if (carImages != null && !carImages.isEmpty()) {
            log.info("Found {} images for car", carImages.size());
            // Manually set the images on the car object
            carWithImages.setCarImages(carImages);
        } else {
            log.warn("No images found for car ID: {}", savedCar.getId());
        }
        
        return BaseResponse.success(HttpStatus.OK, "Car added with images", carMapper.toDto(carWithImages));
    }

    @Override
    public BaseResponse<CarDto> deleteCar(Long carId) {
        Car car = carRepository.findById(carId)
            .orElseThrow(() -> new CarNotFoundException("Car not found"));
        carRepository.delete(car);
        return BaseResponse.success(HttpStatus.OK, "Car deleted", carMapper.toDto(car));
    }
}
