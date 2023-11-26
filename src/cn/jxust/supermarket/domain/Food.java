package cn.jxust.supermarket.domain;

import cn.jxust.supermarket.domain.exception.FoodExpiredException;

import java.time.LocalDate;

public interface Food extends Product{
    LocalDate getExpiredDate();
    void checkExpired() throws FoodExpiredException;
}
