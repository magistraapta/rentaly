package main.app.rental_app.transactions.model;

import lombok.Builder;
import lombok.Data;
import main.app.rental_app.car.model.Car;
import main.app.rental_app.user.model.User;
import java.time.LocalDateTime;

@Data
@Builder
public class Invoices {
    private Long id;
    private User user;
    private Car car;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer totalPrice;
}
