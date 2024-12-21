package ntu.hung.coffeemystar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;

public class Report_Controller {

    // Các bảng hiển thị đơn hàng và các món trong đơn hàng
    @FXML
    private TableView<Report_Item> ordersTable; // Bảng hiển thị đơn hàng
    @FXML
    private TableView<Order_Item> orderItemsTable; // Bảng hiển thị các món trong đơn hàng

    // Các cột trong bảng đơn hàng
    @FXML
    private TableColumn<Report_Item, Integer> orderIdCol; // Cột ID đơn hàng
    @FXML
    private TableColumn<Report_Item, String> orderDateCol; // Cột ngày của đơn hàng
    @FXML
    private TableColumn<Report_Item, Integer> totalPriceCol; // Cột tổng giá đơn hàng

    // Các cột trong bảng món trong đơn hàng
    @FXML
    private TableColumn<Order_Item, Integer> itemIdCol; // Cột ID món trong đơn hàng
    @FXML
    private TableColumn<Order_Item, String> itemNameCol; // Cột tên món trong đơn hàng
    @FXML
    private TableColumn<Order_Item, Integer> itemQuantityCol; // Cột số lượng món trong đơn hàng
    @FXML
    private TableColumn<Order_Item, Integer> itemPriceCol; // Cột giá món trong đơn hàng

    // Danh sách các đơn hàng và các món trong đơn hàng
    private final ObservableList<Report_Item> ordersList = FXCollections.observableArrayList();
    private final ObservableList<Order_Item> orderItemsList = FXCollections.observableArrayList();

    // Phương thức khởi tạo để thiết lập giao diện và các bảng
    @FXML
    public void initialize() {
        // Cấu hình bảng đơn hàng
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderDateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // Cấu hình bảng các món trong đơn hàng
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        itemQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Tải danh sách đơn hàng
        loadOrders();

        // Thêm listener để khi người dùng chọn đơn hàng, tải các món trong đơn hàng đó
        ordersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadOrderItems(newSelection.getId());
            }
        });
    }

    // Phương thức tải danh sách các đơn hàng từ cơ sở dữ liệu và hiển thị trong bảng
    private void loadOrders() {
        ordersList.clear(); // Xóa danh sách đơn hàng cũ
        ResultSet rs = DatabaseConnection.getOrders(); // Lấy dữ liệu đơn hàng từ cơ sở dữ liệu
        try {
            // Duyệt qua các dòng dữ liệu và thêm vào danh sách đơn hàng
            while (rs.next()) {
                ordersList.add(new Report_Item(
                        rs.getInt("id"),
                        rs.getString("order_date"),
                        rs.getInt("total_price")
                ));
            }
            ordersTable.setItems(ordersList); // Cập nhật bảng đơn hàng
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có sự cố
        }
    }

    // Phương thức tải các món trong một đơn hàng cụ thể từ cơ sở dữ liệu và hiển thị trong bảng
    private void loadOrderItems(int orderId) {
        orderItemsList.clear(); // Xóa danh sách các món trong đơn hàng cũ
        ResultSet rs = DatabaseConnection.getOrderItems(orderId); // Lấy các món trong đơn hàng từ cơ sở dữ liệu
        try {
            // Duyệt qua các dòng dữ liệu và thêm vào danh sách các món trong đơn hàng
            while (rs.next()) {
                orderItemsList.add(new Order_Item(
                        rs.getInt("id"),
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        rs.getInt("item_price")
                ));
            }
            orderItemsTable.setItems(orderItemsList); // Cập nhật bảng các món trong đơn hàng
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có sự cố
        }
    }
}
