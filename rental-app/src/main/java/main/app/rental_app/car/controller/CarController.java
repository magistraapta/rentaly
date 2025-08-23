package main.app.rental_app.car.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.car.model.dto.CarDto;
import main.app.rental_app.car.model.dto.CreateCarRequest;
import main.app.rental_app.car.model.enums.CarType;
import main.app.rental_app.car.services.CarService;
import main.app.rental_app.exc.BadRequestException;
import main.app.rental_app.exc.ForbiddenException;
import main.app.rental_app.exc.ResourceNotFoundException;
import main.app.rental_app.exc.UnauthorizedException;
import main.app.rental_app.shared.BaseResponse;

@RestController
@RequestMapping("/v1/cars")
@RequiredArgsConstructor
@Slf4j
public class CarController {
    
    private final CarService carService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<CarDto>>> getAllCars() throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        try {
            log.info("Fetching all cars");
            return ResponseEntity.ok(carService.getAllCars());
        } catch (ResourceNotFoundException e) {
            log.error("No cars found");
            throw new ResourceNotFoundException("No cars found: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<CarDto>> getCarById(@PathVariable Long id) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        
        
        try {
            log.info("Fetching car with id: {}", id);
            return ResponseEntity.ok(carService.getCarById(id));
        } catch (ResourceNotFoundException e) {
            log.error("Car not found with id: {}", id);
            throw new ResourceNotFoundException("Car not found: " + e.getMessage());
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<BaseResponse<CarDto>> getCarByName(@PathVariable String name) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        
        
        try {
            log.info("Fetching car with name: {}", name);
            return ResponseEntity.ok(carService.getCarByName(name));
        } catch (ResourceNotFoundException e) {
            log.error("Car not found with name: {}", name);
            throw new ResourceNotFoundException("Car not found: " + e.getMessage());
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<BaseResponse<List<CarDto>>> getCarsByType(@PathVariable String type) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        
        
        try {
            log.info("Fetching cars with type: {}", type);
            CarType carType = CarType.valueOf(type.toLowerCase());
            return ResponseEntity.ok(carService.getCarsByType(carType));
        } catch (IllegalArgumentException e) {
            log.error("Invalid car type: {}", type);
            throw new BadRequestException("Invalid car type: " + type);
        } catch (ResourceNotFoundException e) {
            log.error("No cars found for type: {}", type);
            throw new ResourceNotFoundException("No cars found for type: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<BaseResponse<CarDto>> addCar(@RequestBody CarDto carDto) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
            return ResponseEntity.ok(carService.addCar(carDto));
    }

    @PostMapping("/add-with-images")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<BaseResponse<CarDto>> addCarWithImages(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Integer price,
            @RequestParam("carType") String carType,
            @RequestParam(value = "images", required = false) MultipartFile[] images) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        
        try {
            log.info("Adding car with images: {}", name);
            log.info("Images received: {}", images != null ? images.length : "null");
            if (images != null) {
                for (int i = 0; i < images.length; i++) {
                    MultipartFile image = images[i];
                    log.info("Image {}: name={}, size={}, empty={}", i, 
                        image != null ? image.getOriginalFilename() : "null",
                        image != null ? image.getSize() : "null",
                        image != null ? image.isEmpty() : "null");
                }
            }
            
            // Parse car type - handle both uppercase and lowercase input
            CarType parsedCarType;
            try {
                // First try exact match
                parsedCarType = CarType.valueOf(carType);
            } catch (IllegalArgumentException e) {
                // If exact match fails, try case-insensitive match
                try {
                    parsedCarType = CarType.valueOf(carType.toUpperCase());
                } catch (IllegalArgumentException e2) {
                    throw new BadRequestException("Invalid car type: " + carType + ". Valid types are: SEDAN, SUV, TRUCK");
                }
            }
            
            // Create request object
            CreateCarRequest createCarRequest = CreateCarRequest.builder()
                .name(name)
                .description(description)
                .price(price)
                .carType(parsedCarType)
                .images(images)
                .build();
            
            log.info("CreateCarRequest built: {}", createCarRequest);
            
            return ResponseEntity.ok(carService.addCarWithImages(createCarRequest));
        } catch (IllegalArgumentException e) {
            log.error("Invalid car type: {}", carType);
            throw new BadRequestException("Invalid car type: " + carType + ". Valid types are: SEDAN, SUV, TRUCK");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<BaseResponse<CarDto>> deleteCar(@PathVariable Long id) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        return ResponseEntity.ok(carService.deleteCar(id));
    }
}
