package ntu.hung.coffeemystar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Locale;

public class Order_Controller {

    // Các thành phần giao diện người dùng (UI)
    @FXML
    private TableView<Inventory_Item> inventoryTable; // Bảng hiển thị các mặt hàng trong kho
    @FXML
    private TableView<Order_Item> orderTable; // Bảng hiển thị các món trong đơn hàng

    // Các cột trong bảng kho
    @FXML
    private TableColumn<Inventory_Item, Integer> inventoryIdCol; // Cột ID món trong kho
    @FXML
    private TableColumn<Inventory_Item, String> inventoryNameCol; // Cột tên món trong kho
    @FXML
    private TableColumn<Inventory_Item, Integer> inventoryQuantityCol; // Cột số lượng món trong kho
    @FXML
    private TableColumn<Inventory_Item, Integer> inventoryPriceCol; // Cột giá món trong kho

    // Các cột trong bảng đơn hàng
    @FXML
    private TableColumn<Order_Item, Integer> orderIdCol; // Cột ID món trong đơn hàng
    @FXML
    private TableColumn<Order_Item, String> orderItemNameCol; // Cột tên món trong đơn hàng
    @FXML
    private TableColumn<Order_Item, Integer> orderQuantityCol; // Cột số lượng món trong đơn hàng
    @FXML
    private TableColumn<Order_Item, Integer> orderPriceCol; // Cột giá món trong đơn hàng

    // Trường nhập liệu để nhập số lượng đơn hàng
    @FXML
    private TextField orderQuantityField;

    // Nhãn hiển thị tổng giá trị đơn hàng
    @FXML
    private Label totalPriceLabel;

    // Danh sách kho và đơn hàng dưới dạng ObservableList
    private final ObservableList<Inventory_Item> inventoryList = FXCollections.observableArrayList();
    private final ObservableList<Order_Item> orderList = FXCollections.observableArrayList();

    // Phương thức khởi tạo để thiết lập giao diện
    @FXML
    public void initialize() {
        // Cấu hình các cột cho bảng kho
        inventoryIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        inventoryNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        inventoryQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        inventoryPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Cấu hình các cột cho bảng đơn hàng
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderItemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        orderQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Tải dữ liệu kho vào bảng
        loadInventory();
    }

    // Phương thức tải dữ liệu kho vào bảng
    private void loadInventory() {
        inventoryList.clear(); // Xóa danh sách kho hiện tại
        ResultSet rs = DatabaseConnection.getInventoryForOrder(); // Lấy dữ liệu kho từ cơ sở dữ liệu
        try {
            // Thêm từng món từ kết quả truy vấn vào danh sách kho
            while (rs.next()) {
                inventoryList.add(new Inventory_Item(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getInt("price"),
                        rs.getInt("quantity"),
                        null // Không cần thiết lập loại món ở đây
                ));
            }
            inventoryTable.setItems(inventoryList); // Cập nhật bảng kho với danh sách món
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có sự cố
        }
    }

    // Phương thức thêm món vào đơn hàng
    @FXML
    private void addItemToOrder() {
        Inventory_Item selectedItem = inventoryTable.getSelectionModel().getSelectedItem(); // Lấy món được chọn trong bảng kho
        if (selectedItem == null) {
            showAlert("Lỗi", "Hãy thêm một món.", Alert.AlertType.ERROR); // Nếu không có món được chọn, hiển thị lỗi
            return;
        }

        int quantity;
        try {
            // Lấy số lượng từ trường nhập liệu
            quantity = Integer.parseInt(orderQuantityField.getText());
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Số lượng phải là số.", Alert.AlertType.ERROR); // Kiểm tra nếu số lượng không phải là số hợp lệ
            return;
        }

        // Kiểm tra nếu số lượng hợp lệ
        if (quantity <= 0 || quantity > selectedItem.getQuantity()) {
            showAlert("Lỗi", "Số lượng không hợp lệ.", Alert.AlertType.ERROR);
            return;
        }

        int itemTotalPrice = quantity * selectedItem.getPrice(); // Tính tổng giá của món theo số lượng
        orderList.add(new Order_Item(selectedItem.getId(), selectedItem.getItemName(), quantity, itemTotalPrice)); // Thêm món vào danh sách đơn hàng

        // Cập nhật lại tổng giá đơn hàng
        updateTotalPrice();
    }

    // Phương thức xóa món khỏi đơn hàng
    @FXML
    private void removeItemFromOrder() {
        Order_Item selectedOrderItem = orderTable.getSelectionModel().getSelectedItem(); // Lấy món được chọn trong bảng đơn hàng
        if (selectedOrderItem != null) {
            orderList.remove(selectedOrderItem); // Xóa món khỏi đơn hàng
            updateTotalPrice(); // Cập nhật lại tổng giá đơn hàng
        } else {
            showAlert("Lỗi", "Món chưa được chọn để hoàn tác.", Alert.AlertType.ERROR); // Nếu không có món nào được chọn, hiển thị lỗi
        }
    }

    // Phương thức đặt đơn hàng
    @FXML
    private void placeOrder() {
        if (orderList.isEmpty()) {
            showAlert("Lỗi", "Không có món trong đơn.", Alert.AlertType.ERROR); // Kiểm tra nếu đơn hàng không có món
            return;
        }

        // Tính tổng giá của đơn hàng
        int totalPrice = orderList.stream().mapToInt(Order_Item::getPrice).sum();

        // Thêm đơn hàng vào cơ sở dữ liệu
        int orderId = DatabaseConnection.addOrder(totalPrice);
        if (orderId == -1) {
            showAlert("Lỗi", "Thanh toán thất bại.", Alert.AlertType.ERROR); // Kiểm tra nếu không thể thanh toán
            return;
        }

        // Thêm các món vào đơn hàng và giảm số lượng trong kho
        for (Order_Item orderItem : orderList) {
            DatabaseConnection.addOrderItems(orderId, orderItem.getId(), orderItem.getQuantity(), orderItem.getPrice());
            DatabaseConnection.reduceInventoryQuantity(orderItem.getId(), orderItem.getQuantity());
        }

        showAlert("Thành Công", "Thanh toán thành công!", Alert.AlertType.INFORMATION); // Hiển thị thông báo thành công
        orderList.clear(); // Xóa đơn hàng
        updateTotalPrice(); // Cập nhật lại tổng giá
        loadInventory(); // Tải lại dữ liệu kho
    }

    // Phương thức cập nhật tổng giá của đơn hàng
    private void updateTotalPrice() {
        int total = orderList.stream().mapToInt(Order_Item::getPrice).sum(); // Tính tổng giá đơn hàng
        totalPriceLabel.setText("Tổng: " + total + " VND"); // Hiển thị tổng giá lên nhãn
        orderTable.setItems(orderList); // Cập nhật bảng đơn hàng
    }

    // Phương thức hiển thị thông báo lỗi hoặc thành công
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type); // Chọn loại thông báo
        alert.setTitle(title); // Tiêu đề thông báo
        alert.setContentText(message); // Nội dung thông báo
        alert.showAndWait(); // Hiển thị thông báo và đợi người dùng đóng
    }
}
