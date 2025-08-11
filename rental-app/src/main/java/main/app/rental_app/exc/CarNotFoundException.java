package main.app.rental_app.exc;

public class CarNotFoundException extends ResourceNotFoundException {
    public CarNotFoundException(String message) {
        super(message);
    }
}
