package main.app.rental_app.car.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.exc.BadRequestException;
import main.app.rental_app.exc.UnauthorizedException;
import main.app.rental_app.exc.ForbiddenException;
import main.app.rental_app.car.services.CarService;
import main.app.rental_app.car.model.dto.CarDto;
import main.app.rental_app.car.model.enums.CarType;
import main.app.rental_app.exc.ResourceNotFoundException;
import main.app.rental_app.auth.jwt.JwtUtil;

@RestController
@RequestMapping("/v1/cars")
@RequiredArgsConstructor
@Slf4j
public class CarController {
    
    private final CarService carService;
    private final JwtUtil jwtUtil;

    @GetMapping("/{id}")
    public CarDto getCarById(@PathVariable Long id) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        // Check authentication (401 Unauthorized)
        jwtUtil.checkAuthentication();
        
        // Check authorization (403 Forbidden) - example: only users with 'admin' role can access
        // jwtUtil.checkAuthorization("admin");
        
        try {
            log.info("Fetching car with id: {}", id);
            return carService.getCarById(id);
        } catch (ResourceNotFoundException e) {
            log.error("Car not found with id: {}", id);
            throw new ResourceNotFoundException("Car not found: " + e.getMessage());
        }
    }

    @GetMapping("/name/{name}")
    public CarDto getCarByName(@PathVariable String name) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        // Check authentication (401 Unauthorized)
        jwtUtil.checkAuthentication();
        
        try {
            log.info("Fetching car with name: {}", name);
            return carService.getCarByName(name);
        } catch (ResourceNotFoundException e) {
            log.error("Car not found with name: {}", name);
            throw new ResourceNotFoundException("Car not found: " + e.getMessage());
        }
    }

    @GetMapping("/type/{type}")
    public List<CarDto> getCarsByType(@PathVariable String type) throws ResourceNotFoundException, UnauthorizedException, ForbiddenException {
        // Check authentication (401 Unauthorized)
        jwtUtil.checkAuthentication();
        
        try {
            log.info("Fetching cars with type: {}", type);
            CarType carType = CarType.valueOf(type.toLowerCase());
            return carService.getCarsByType(carType);
        } catch (IllegalArgumentException e) {
            log.error("Invalid car type: {}", type);
            throw new BadRequestException("Invalid car type: " + type);
        } catch (ResourceNotFoundException e) {
            log.error("No cars found for type: {}", type);
            throw new ResourceNotFoundException("No cars found for type: " + e.getMessage());
        }
    }

}
