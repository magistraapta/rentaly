# Car Images Directory

This directory stores uploaded car images for the rental application.

## Directory Structure
- Images are stored directly in this directory
- Filename format: `{carId}_{timestamp}.{extension}`
- Example: `1_1234567890.jpg`

## Access URLs
- Images are accessible via: `http://localhost:8080/images/{filename}`
- Example: `http://localhost:8080/images/1_1234567890.jpg`

## Configuration
- Upload directory: `src/main/resources/static/images/`
- Max file size: 10MB
- Allowed formats: jpg, jpeg, png, gif, webp

## Notes
- Spring Boot automatically serves files from this directory
- Images are bundled with the application JAR
- For production, consider using cloud storage (AWS S3, etc.)

