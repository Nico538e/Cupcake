package app.entities;

public class Orders {
    private int orderId;
     private User user;

    public Orders(int orderId, User user) {
        this.orderId = orderId;
        this.user = user;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return "Orders{" +
                "orderId=" + orderId +
                ", User=" + user +
                '}';
    }
}
