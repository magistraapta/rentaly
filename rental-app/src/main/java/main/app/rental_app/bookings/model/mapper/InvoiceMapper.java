package main.app.rental_app.bookings.model.mapper;

import java.time.Instant;

import org.mapstruct.Mapper;

import main.app.rental_app.bookings.model.Invoices;
import main.app.rental_app.bookings.model.dto.CreateInvoicesDto;
import main.app.rental_app.bookings.model.dto.ResponseInvoiceDto;
import main.app.rental_app.bookings.model.dto.UserDto;
import main.app.rental_app.bookings.model.dto.CarDto;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    
    public static ResponseInvoiceDto toResponseInvoiceDto(Invoices invoice) {
        return ResponseInvoiceDto.builder()
            .id(invoice.getId())
            .user(UserDto.builder()
                .id(invoice.getUser().getId())
                .username(invoice.getUser().getUsername())
                .email(invoice.getUser().getEmail())
                .build())
            .car(CarDto.builder()
                .id(invoice.getCar().getId())
                .name(invoice.getCar().getName())
                .description(invoice.getCar().getDescription())
                .price(invoice.getCar().getPrice())
                .carType(invoice.getCar().getCarType())
                .imageUrl(invoice.getCar().getImageUrl())
                .build())
            .status(invoice.getStatus())
            .rentStatus(invoice.getRentStatus())
            .startTime(invoice.getStartDate())
            .endTime(invoice.getEndDate())
            .totalPrice(invoice.getTotalPrice())
            .createdTime(invoice.getCreatedAt())
            .updatedTime(invoice.getUpdatedAt())
            .build();
    }

    public static Invoices toEntity(CreateInvoicesDto invoiceDto) {
        return Invoices.builder()
            .startDate(invoiceDto.getStartDate().atStartOfDay())
            .endDate(invoiceDto.getEndDate().atTime(23, 59, 59))
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }
}
