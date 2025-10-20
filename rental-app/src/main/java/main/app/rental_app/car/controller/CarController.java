package main.app.rental_app.car.controller;

import java.util.List;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

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
@Tag(name = "Car Management", description = "Endpoints for managing cars in the rental system")
public class CarController {
    
    private final CarService carService;

    @Operation(
        summary = "Get all cars",
        description = "Retrieve a list of all available cars in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cars retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No cars found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        )
    })
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

    @Operation(
        summary = "Get car by ID",
        description = "Retrieve a specific car by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Car found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Car not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<CarDto>> getCarById(
        @Parameter(description = "Car ID", required = true, example = "1")
        @PathVariable Long id) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        
        
        try {
            log.info("Fetching car with id: {}", id);
            return ResponseEntity.ok(carService.getCarById(id));
        } catch (ResourceNotFoundException e) {
            log.error("Car not found with id: {}", id);
            throw new ResourceNotFoundException("Car not found: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Get car by name",
        description = "Retrieve a specific car by its name"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Car found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Car not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        )
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<BaseResponse<CarDto>> getCarByName(
        @Parameter(description = "Car name", required = true, example = "Toyota Camry")
        @PathVariable String name) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        
        
        try {
            log.info("Fetching car with name: {}", name);
            return ResponseEntity.ok(carService.getCarByName(name));
        } catch (ResourceNotFoundException e) {
            log.error("Car not found with name: {}", name);
            throw new ResourceNotFoundException("Car not found: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Get cars by type",
        description = "Retrieve all cars of a specific type (SEDAN, SUV, TRUCK)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cars retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid car type",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No cars found for the specified type",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        )
    })
    @GetMapping("/type/{type}")
    public ResponseEntity<BaseResponse<List<CarDto>>> getCarsByType(
        @Parameter(description = "Car type", required = true, example = "SEDAN", schema = @Schema(allowableValues = {"SEDAN", "SUV", "TRUCK"}))
        @PathVariable String type) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        
        
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

    @Operation(
        summary = "Add a new car",
        description = "Create a new car in the system (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Car added successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Admin access required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<BaseResponse<CarDto>> addCar(
        @Parameter(description = "Car details", required = true)
        @RequestBody CarDto carDto) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
            return ResponseEntity.ok(carService.addCar(carDto));
    }

    @Operation(
        summary = "Add a new car with images",
        description = "Create a new car in the system with uploaded images (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Car added successfully with images",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid input data or car type",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Admin access required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/add-with-images")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<BaseResponse<CarDto>> addCarWithImages(
            @Parameter(description = "Car name", required = true, example = "Toyota Camry")
            @RequestParam("name") String name,
            @Parameter(description = "Car description", required = true, example = "A reliable sedan perfect for city driving")
            @RequestParam("description") String description,
            @Parameter(description = "Daily rental price", required = true, example = "50")
            @RequestParam("price") Integer price,
            @Parameter(description = "Car type", required = true, example = "SEDAN", schema = @Schema(allowableValues = {"SEDAN", "SUV", "TRUCK"}))
            @RequestParam("carType") String carType,
            @Parameter(description = "Car images (optional)", required = false)
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

    @Operation(
        summary = "Delete a car",
        description = "Remove a car from the system by its ID (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Car deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Car not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Admin access required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<BaseResponse<CarDto>> deleteCar(
        @Parameter(description = "Car ID", required = true, example = "1")
        @PathVariable Long id) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        return ResponseEntity.ok(carService.deleteCar(id));
    }
}
