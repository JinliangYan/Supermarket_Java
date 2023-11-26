package cn.jxust.supermarket.domain;

import java.io.Serializable;

public interface Product extends Serializable {
    int getId();
    String getName();
    String getType();
    String getDetails();
    double getPrice();
    int getStock();
    void showDetails();
    void setStock(int stock);
    double getDiscount();
    double getPriceBefore();
}

