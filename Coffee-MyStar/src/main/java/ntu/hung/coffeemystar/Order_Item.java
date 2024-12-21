package ntu.hung.coffeemystar;

public class Order_Item {
    private int Id;
    private String itemName;
    private int quantity;
    private int price;

    public Order_Item(int inventoryId, String itemName, int quantity, int price) {
        this.Id = inventoryId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return Id;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
