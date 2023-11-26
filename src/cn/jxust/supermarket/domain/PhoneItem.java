package cn.jxust.supermarket.domain;

public class PhoneItem extends ProductItem implements Phone {
    private final String brand;
    private final String theCPU;
    private final int ram;
    private final int storage;
    private final String theOS;

    public PhoneItem(int id, String name, String type, String details, double price, int stock, String brand, String theCPU, int ram, int storage, String theOS) {
        super(id, name, type, details, price, stock);
        this.brand = brand;
        this.theCPU = theCPU;
        this.ram = ram;
        this.storage = storage;
        this.theOS = theOS;
    }

    @Override
    public void showDetails()
    {
        super.showDetails();
        System.out.println("品牌：" + this.brand + "™");
        System.out.println("操作系统：" + this.theOS);
        System.out.println("CPU:" + this.theCPU);
        System.out.println("内存：" + this.ram + "G");
        System.out.println("储存：" + this.storage + "G");
    }

    public String getBrand() {
        return brand;
    }

    public String getTheCPU() {
        return theCPU;
    }

    public int getRam() {
        return ram;
    }

    public int getStorage() {
        return storage;
    }

    public String getTheOS() {
        return theOS;
    }
}
