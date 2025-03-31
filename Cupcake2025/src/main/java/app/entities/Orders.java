package app.entities;

public class Orders {
    private int orderId;
    private int userId;
    private User user;

    public Orders(int orderId, int userId) {
        this.orderId = orderId;
        this.userId = userId;
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
