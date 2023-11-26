package cn.jxust.supermarket.domain;

import cn.jxust.supermarket.server.dao.exception.ProductException;

public class ProductItem implements Product {
    private int id;
    private String name;
    private String type;
    private String details;
    private double price;
    private int stock;

    public ProductItem(int id, String name, String type, String details, double price, int stock) {
        this.id = id;
        this.type = type;
        this.details = details;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ID:" + id + "\t" + name + "\t" + price + "￥\t库存：" + stock;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProductItem product)) {
            return false;
        }
        return this.id == product.id;
    }

    public void showDetails() {
        System.out.println(this);
        System.out.println(this.details);
    }


    // getter 和 setter 方法

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public final double getDiscount() throws ProductException{
        return 1.0;
    }

    @Override
    public final double getPriceBefore() throws ProductException{
        return this.price;
    }
}
