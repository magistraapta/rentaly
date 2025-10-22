package main.app.rental_app.bookings.model.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import main.app.rental_app.bookings.model.enums.PaymentStatus;
import main.app.rental_app.bookings.model.enums.RentStatus;

@Data
@Builder
public class ResponseInvoiceDto {
    private Long id;
    private UserDto user;
    private CarDto car;
    private PaymentStatus status;
    private RentStatus rentStatus;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalPrice;
    private Instant createdTime;
    private Instant updatedTime;
}
