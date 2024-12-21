package ntu.hung.coffeemystar;

public class Report_Item {
    private int id;
    private String orderDate;
    private int totalPrice;

    public Report_Item(int id, String orderDate, int totalPrice) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
