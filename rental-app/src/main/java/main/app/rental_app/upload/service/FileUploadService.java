package main.app.rental_app.upload.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import main.app.rental_app.shared.BaseResponse;

public interface FileUploadService {
    ResponseEntity<BaseResponse<String>> uploadCarImage(MultipartFile file, Long carId);
}
