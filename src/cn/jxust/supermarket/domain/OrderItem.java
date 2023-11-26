package cn.jxust.supermarket.domain;

import java.time.LocalDate;

public class OrderItem implements Order{
    private final boolean isVipUser;
    private static int nextHash = 0;
    private final int hash;
    private final Product product;
    private final LocalDate creatTime;
    private final int quantity;

    private final int pointsCost;
    private final double priceBeforeDiscount;
    private final double priceAfterDiscount;

    public OrderItem(boolean isVipUser, Product product, LocalDate creatTime, int quantity, int pointsCost, double priceBeforeDiscount, double priceAfterDiscount) {
        this.isVipUser = isVipUser;
        this.product = product;
        this.creatTime = creatTime;
        this.quantity = quantity;
        this.pointsCost = pointsCost;
        this.priceBeforeDiscount = priceBeforeDiscount;
        this.priceAfterDiscount = priceAfterDiscount;
        this.hash = nextHash++;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(hash);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OrderItem other = (OrderItem) obj;
        return other.hashCode() == this.hashCode();
    }


    public Product getProduct() {
        return product;
    }

    public LocalDate getCreatTime() {
        return creatTime;
    }


    public int getQuantity() {
        return quantity;
    }

    public boolean isVipUser() {
        return isVipUser;
    }

    public int getPointsCost() {
        return pointsCost;
    }

    public double getPriceBeforeDiscount() {
        return priceBeforeDiscount;
    }

    public double getPriceAfterDiscount() {
        return priceAfterDiscount;
    }
}
