package cn.jxust.supermarket.domain.exception;

import java.time.LocalDate;

public class FoodExpiredException extends RuntimeException {
    private LocalDate expiryDate;

    public FoodExpiredException(String message, LocalDate expiryDate) {
        super(message);
        this.expiryDate = expiryDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}
