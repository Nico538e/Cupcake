package app.entities;

public class ShowUserOrders {
    private String userName;
    private int orderId;
    private int orderLineId;
    private int toppingId;
    private int bottomId;
    private String toppingName;
    private String bottomName;
    private double orderLinePrice;




    public ShowUserOrders(String userName, int orderId, int orderLineId, int toppingId, int bottomId, String toppingName, String bottomName, double orderLinePrice) {
        this.userName = userName;
        this.orderId = orderId;
        this.orderLineId = orderLineId;
        this.toppingId = toppingId;
        this.bottomId = bottomId;
        this.toppingName = toppingName;
        this.bottomName = bottomName;
        this.orderLinePrice = orderLinePrice;
    }

    @Override
    public String toString() {
        return "ShowUserOrders{" +
                "userName='" + userName + '\'' +
                ", orderId=" + orderId +
                ", orderLineId=" + orderLineId +
                ", toppingId=" + toppingId +
                ", bottomId=" + bottomId +
                ", toppingName='" + toppingName + '\'' +
                ", bottomName='" + bottomName + '\'' +
                ", orderLinePrice=" + orderLinePrice +
                '}';
    }
}
