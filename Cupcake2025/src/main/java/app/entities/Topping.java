package app.entities;

public class Topping {
private int toppingId;
private String toppingName;
private double toppingPrice;

    public Topping(int toppingId, String toppingName, double toppingPrice) {
        this.toppingId = toppingId;
        this.toppingName = toppingName;
        this.toppingPrice = toppingPrice;
    }

    public Topping(String toppingName, double toppingPrice) {
        this.toppingName = toppingName;
        this.toppingPrice = toppingPrice;
    }

    public int getToppingId() {
        return toppingId;
    }

    public void setToppingId(int toppingId) {
        this.toppingId = toppingId;
    }

    public String getToppingName() {
        return toppingName;
    }

    public void setToppingName(String toppingName) {
        this.toppingName = toppingName;
    }

    public double getToppingPrice() {
        return toppingPrice;
    }

    public void setToppingPrice(double toppingPrice) {
        this.toppingPrice = toppingPrice;
    }

    @Override
    public String toString() {
        return "Topping{" +
                "toppingId=" + toppingId +
                ", toppingName='" + toppingName + '\'' +
                ", toppingPrice=" + toppingPrice +
                '}';
    }
}
