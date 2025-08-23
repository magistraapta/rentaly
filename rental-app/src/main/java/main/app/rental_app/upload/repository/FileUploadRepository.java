package main.app.rental_app.upload.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import main.app.rental_app.upload.model.CarImage;

public interface FileUploadRepository extends JpaRepository<CarImage, Long> {
    Optional<CarImage> findByCarId(Long carId);
    List<CarImage> findAllByCarId(Long carId);
}
