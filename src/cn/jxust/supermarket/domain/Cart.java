package cn.jxust.supermarket.domain;


import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Cart implements Serializable{
    private final int userId;
    private final Map<Integer, Integer> itemsMap = new TreeMap<>(new SerializableComparator());
    private double totPrise = 0;

    public Cart(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Cart cart)) {
            return false;
        }
        return this.userId == cart.userId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(userId);
    }

    public int getUserId() {
        return userId;
    }

    public Map<Integer, Integer> getItemsMap() {
        return itemsMap;
    }

    public double getTotPrise() {
        return totPrise;
    }

    public void setTotPrise(double totPrise) {
        this.totPrise = totPrise;
    }

    // Add a static inner class that implements both Comparator<Integer> and Serializable
    private static class SerializableComparator implements Comparator<Integer>, Serializable {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }
}
