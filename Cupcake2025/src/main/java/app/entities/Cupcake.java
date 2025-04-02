package app.entities;

public class Cupcake {
    private Bottom bottom;
    private Topping topping;
    private int quantity;


    public Cupcake(Bottom bottom, Topping topping, int quantity) {
        this.bottom = bottom;
        this.topping = topping;
        this.quantity = quantity;

    }


    public Bottom getBottom() {
        return bottom;
    }

    public void setBottom(Bottom bottom) {
        this.bottom = bottom;
    }

    public Topping getTopping() {
        return topping;
    }

    public void setTopping(Topping topping) {
        this.topping = topping;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
