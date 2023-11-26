package cn.jxust.supermarket.server.util;

import cn.jxust.supermarket.domain.*;

import java.time.LocalDate;

public class ProductFactory {
    private static int nextId = 1000;
    public static Product createProduct(String name, String type, String details, double price, int stock) {
        return new ProductItem(nextId++, name, type, details, price, stock);
    }

    public static Phone createPhone(String name, String type, String details, double price, int stock, String brand, String theCPU, int ram, int storage, String theOS) {
        return new PhoneItem(nextId++, name, type, details, price, stock, brand, theCPU, ram, storage, theOS);
    }

    public static Food creatBread(String name, String type, String details, double price, int stock, LocalDate shelfLife) {
        return new FoodItem(nextId++, name, type, details, price, stock, shelfLife);
    }

    public static <T extends Product> T createDiscountProduct(double discount, T product) {
        try {
            return new DiscountDecorator<>(product, discount).decorator();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
