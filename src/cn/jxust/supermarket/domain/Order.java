package cn.jxust.supermarket.domain;

import java.io.Serializable;
import java.time.LocalDate;

public interface Order extends Serializable {
     Product getProduct();
     LocalDate getCreatTime();
     int getQuantity();
     boolean isVipUser();

     int getPointsCost();

     double getPriceBeforeDiscount();

     double getPriceAfterDiscount();
}
