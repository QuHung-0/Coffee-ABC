package ntu.hung.coffeemystar;

public class Inventory_Item {
    private int id;
    private String itemName;
    private int price;
    private int quantity;
    private String category;

    public Inventory_Item(int id, String itemName, int price, int quantity, String category) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }
}
