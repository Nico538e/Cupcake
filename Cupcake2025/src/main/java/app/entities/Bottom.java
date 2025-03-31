package app.entities;

public class Bottom {
private int bottomId;
private String bottomsName;
private double bottomPrice;

    public Bottom(int bottomId, String bottomsName, double bottomPrice) {
        this.bottomId = bottomId;
        this.bottomsName = bottomsName;
        this.bottomPrice = bottomPrice;
    }

    public Bottom(String bottomsName, double bottomPrice) {
        this.bottomsName = bottomsName;
        this.bottomPrice = bottomPrice;
    }

    public Bottom(String bottomsName) {
        this.bottomsName = bottomsName;
    }

    public int getBottomId() {
        return bottomId;
    }

    public void setBottomId(int bottomId) {
        this.bottomId = bottomId;
    }

    public String getBottomsName() {
        return bottomsName;
    }

    public void setBottomsName(String bottomsName) {
        this.bottomsName = bottomsName;
    }

    public double getBottomPrice() {
        return bottomPrice;
    }

    public void setBottomPrice(double bottomPrice) {
        this.bottomPrice = bottomPrice;
    }

    @Override
    public String toString() {
        return "Bottom{" +
                "bottomId=" + bottomId +
                ", bottomsName='" + bottomsName + '\'' +
                ", bottomPrice=" + bottomPrice +
                '}';
    }
}
