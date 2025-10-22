package main.app.rental_app.bookings.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.app.rental_app.bookings.model.Invoices;
import main.app.rental_app.bookings.model.dto.CreateInvoicesDto;
import main.app.rental_app.bookings.model.dto.ResponseInvoiceDto;
import main.app.rental_app.bookings.service.BookingService;
import main.app.rental_app.shared.BaseResponse;

@RestController
@RequestMapping("/v1/bookings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Booking Management", description = "Endpoints for managing car bookings and rentals")
public class BookingController {
    
    private final BookingService invoiceService;

    @Operation(
        summary = "Book a car",
        description = "Create a new booking/invoice for a specific car"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Car booked successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/book/{carId}")
    public ResponseEntity<BaseResponse<Invoices>> bookCar(
        @Parameter(description = "Booking details", required = true)
        @RequestBody CreateInvoicesDto invoiceDto, 
        @Parameter(description = "Car ID to book", required = true, example = "1")
        @PathVariable long carId) {
        try {
            log.info("Controller:Booking car: {}", invoiceDto);
            return invoiceService.createInvoice(invoiceDto, carId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @Operation(
        summary = "Get booking by ID",
        description = "Retrieve a specific booking/invoice by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Booking found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Booking not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ResponseInvoiceDto>> getInvoiceById(
        @Parameter(description = "Booking ID", required = true, example = "1")
        @PathVariable Long id) {
        try {
            log.info("Controller: Getting invoice by id: {}", id);
            return invoiceService.getInvoiceById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @Operation(
        summary = "Get all bookings",
        description = "Retrieve all bookings/invoices (Admin only)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "All bookings retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Forbidden - Admin access required",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<BaseResponse<List<ResponseInvoiceDto>>> getAllInvoices() {
        try {
            log.info("Controller: Getting all invoices");
            return invoiceService.getAllInvoices();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @Operation(
        summary = "Get bookings by user ID",
        description = "Retrieve all bookings/invoices for a specific user"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User bookings retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<List<ResponseInvoiceDto>>> getInvoicesByUserId(
        @Parameter(description = "User ID", required = true, example = "1")
        @PathVariable Long userId) {
        try {
            log.info("Controller: Getting invoices by user id: {}", userId);
            return invoiceService.getInvoicesByUserId(userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @Operation(
        summary = "Return a car",
        description = "Mark a booking as returned and update the rental status"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Car returned successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Booking not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request - Car already returned or invalid status",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}/return")
    public ResponseEntity<BaseResponse<Invoices>> returnCar(
        @Parameter(description = "Booking ID", required = true, example = "1")
        @PathVariable Long id) {
        try {
            log.info("Controller: Returning car for invoice: {}", id);
            return invoiceService.returnCar(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @Operation(
        summary = "Cancel a rental",
        description = "Cancel an existing booking/rental and update the status"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Rental cancelled successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Booking not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request - Cannot cancel already returned or cancelled booking",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = BaseResponse.class)
            )
        )
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<BaseResponse<Invoices>> cancelRental(
        @Parameter(description = "Booking ID", required = true, example = "1")
        @PathVariable Long id) {
        try {
            log.info("Controller: Cancelling rental for invoice: {}", id);
            return invoiceService.cancelRental(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }
}
