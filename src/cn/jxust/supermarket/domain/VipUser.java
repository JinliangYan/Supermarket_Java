package cn.jxust.supermarket.domain;

public class VipUser extends User {
    private int vipPoints;

    public VipUser(String username, String password, int vipPoints) {
        super(username, password);
        this.vipPoints = vipPoints;
    }

    public VipUser(int id, String username, String password, String salt, int errorCount, double balance, int vipPoints) {
        super(id, username, password, salt, errorCount, balance);
        this.vipPoints = vipPoints;
    }

    public double getDiscountAmount(double amount) {
        double discountAmount = vipPoints;
        return Math.min(discountAmount, amount);
    }

    public int getVipPoints() {
        return vipPoints;
    }

    public void setVipPoints(int vipPoints) {
        this.vipPoints = vipPoints;
    }
}
