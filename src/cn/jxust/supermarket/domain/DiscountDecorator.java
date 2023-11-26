package cn.jxust.supermarket.domain;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static cn.jxust.supermarket.server.util.ProductFactory.createDiscountProduct;
import static cn.jxust.supermarket.server.util.ProductFactory.createPhone;

public class DiscountDecorator<T extends Product> implements InvocationHandler, Serializable {
    private final T product;
    private final double discount;

    public DiscountDecorator(T product, double discount) {
        this.product = product;
        this.discount = discount;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        double price = product.getPrice();
        switch (method.getName()) {
            case "getPrice":
                result = price * discount;
                break;
            case "getDiscount":
                return discount;
            case "showDetails":
                System.out.printf("|========%.2f折=========|\n", discount * 10);
                method.invoke(product, args);
                System.out.printf("折后%.2f￥%n", price * discount);
                break;
            case "toString":
                return String.format("%s (%.2f折)", product, discount * 10);
            default:
                result = method.invoke(product, args);
                break;
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    public T decorator() {
        return (T)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), product.getClass().getInterfaces(), this);
    }
}
