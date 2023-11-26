package cn.jxust.supermarket.domain;

import cn.jxust.supermarket.domain.exception.FoodExpiredException;

import java.time.LocalDate;

public class FoodItem extends ProductItem implements Food {
    private final LocalDate shelfLife;

    public FoodItem(int id, String name, String type, String details, double price, int stock, LocalDate ExpirationDate) {
        super(id, name, type, details, price, stock);
        this.shelfLife = ExpirationDate;
    }

    @Override
    public void showDetails() {
        super.showDetails();
        System.out.println("过期时间：" + shelfLife);
    }

    @Override
    public void checkExpired() throws FoodExpiredException {
        LocalDate expired = this.getExpiredDate();
        if (expired.compareTo(LocalDate.now()) < 0) {
            throw new FoodExpiredException(this.getName() + " ID:" + this.getId(), getExpiredDate());
        }
    }


    public LocalDate getExpiredDate() {
        return shelfLife;
    }
}
