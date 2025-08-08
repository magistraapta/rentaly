package main.app.rental_app.upload.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import main.app.rental_app.shared.BaseResponse;
import main.app.rental_app.upload.service.FileUploadService;

@RestController
@RequestMapping("/v1/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping("/car-image")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<BaseResponse<String>> uploadCarImage(@RequestParam("file") MultipartFile file, @RequestParam("carId") Long carId) {
        return fileUploadService.uploadCarImage(file, carId);
    }

}
