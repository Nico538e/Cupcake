package app.entities;

public class OrderLine {
private int orderLineId;
private int bottomId;
private int toppingId;
private int orderId;
private int amount;
private double orderLinePrice;


// publuc OrderLine (int orderLineID, CakeTop top, CakeBottom bottom, int orderID)
    public OrderLine(int orderLineId, int bottomId, int toppingId, int orderId, double orderLinePrice, double linePrice) {
        this.orderLineId = orderLineId;
        this.bottomId = bottomId;
        this.toppingId = toppingId;
        this.orderId = orderId;
        this.orderLinePrice = orderLinePrice;
    }


    public int getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(int orderLineId) {
        this.orderLineId = orderLineId;
    }

    public int getBottomId() {
        return bottomId;
    }

    public void setBottomId(int bottomId) {
        this.bottomId = bottomId;
    }

    public int getToppingId() {
        return toppingId;
    }

    public void setTopping(int toppingId) {
        this.toppingId = toppingId;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getOrderLinePrice() {
        return orderLinePrice;
    }

    public void setOrderLinePrice(double orderLinePrice) {
        this.orderLinePrice = orderLinePrice;
    }

    @Override
    public String toString() {
        return "OrderLine{" +
                "orderLineId=" + orderLineId +
                ", bottomId=" + bottomId +
                ", toppingId=" + toppingId +
                ", orderId=" + orderId +
                ", amount=" + amount +
                ", orderLinePrice=" + orderLinePrice +
                '}';
    }
}
