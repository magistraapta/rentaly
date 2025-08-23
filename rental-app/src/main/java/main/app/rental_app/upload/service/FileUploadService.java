package main.app.rental_app.upload.service;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import main.app.rental_app.shared.BaseResponse;
import main.app.rental_app.upload.model.CarImage;

public interface FileUploadService {
    ResponseEntity<BaseResponse<String>> uploadCarImage(MultipartFile file, Long carId);
    List<CarImage> getCarImagesByCarId(Long carId);
}
