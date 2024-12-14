package ntu.hung.coffeemystar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;

public class Inventory_Controller {

    @FXML
    private TableView<Inventory_Item> inventoryTable;

    @FXML
    private TableColumn<Inventory_Item, Integer> CotID;
    @FXML
    private TableColumn<Inventory_Item, String> CotTenMon;
    @FXML
    private TableColumn<Inventory_Item, Integer> CotGia;
    @FXML
    private TableColumn<Inventory_Item, Integer> CotSoLuong;
    @FXML
    private TableColumn<Inventory_Item, String> CotLoai;

    @FXML
    private TextField IDTextField, TenMonTextField, GiaTextField, SoluongTextField, LoaiTextField;

    @FXML
    private Button btn_ThemMon, btn_XoaMon, btn_CapNhat;

    private final ObservableList<Inventory_Item> itemList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize TableView columns
        CotID.setCellValueFactory(new PropertyValueFactory<>("id"));
        CotTenMon.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        CotGia.setCellValueFactory(new PropertyValueFactory<>("price"));
        CotSoLuong.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        CotLoai.setCellValueFactory(new PropertyValueFactory<>("category"));

        loadInventory();
    }

    @FXML
    private void addItem() {
        try {
            String name = TenMonTextField.getText();
            int price = Integer.parseInt(GiaTextField.getText());
            int quantity = Integer.parseInt(SoluongTextField.getText());
            String category = LoaiTextField.getText();

            // Check if the item with the same name exists (by ID or name)
            if (DatabaseConnection.checkItemExists(name)) {
                showAlert("Error", "Món với tên này đã tồn tại", Alert.AlertType.ERROR);
                return;
            }

            // Add the new item
            if (DatabaseConnection.addItem(name, price, quantity, category)) {
                showAlert("Success", "Thêm món thành công.", Alert.AlertType.INFORMATION);
                loadInventory(); // Reload the inventory to show the new item
            } else {
                showAlert("Error", "lỗi không thêm được món.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Giá và số lượng phải là số hợp lệ.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void updateItem() {
        try {
            int id = Integer.parseInt(IDTextField.getText());
            String name = TenMonTextField.getText();
            int price = Integer.parseInt(GiaTextField.getText());
            int quantity = Integer.parseInt(SoluongTextField.getText());
            String category = LoaiTextField.getText();

            if (DatabaseConnection.updateItem(id, name, price, quantity, category)) {
                showAlert("Success", "Món cập nhật thành công!", Alert.AlertType.INFORMATION);
                loadInventory();
            } else {
                showAlert("Error", "Thất bại cập nhật món.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "ID, Giá, Số lượng phải là số hợp lệ", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void deleteItem() {
        try {
            int id = Integer.parseInt(IDTextField.getText());

            // Show a confirmation dialog before deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xóa");
            alert.setHeaderText("Bạn có chắc là xóa món với ID này: " + id + "?");
            alert.setContentText("Hành động này không thể hoàn tác");

            if (alert.showAndWait().get() == ButtonType.OK) {
                // Proceed with deletion
                if (DatabaseConnection.deleteItem(id)) {
                    showAlert("Success", "Món đã xóa thành công!", Alert.AlertType.INFORMATION);
                    loadInventory(); // Reload the inventory to remove the deleted item
                } else {
                    showAlert("Error", "Thất bại! Không thể xóa món với ID: " + id, Alert.AlertType.ERROR);
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "ID phải là số hợp lệ.", Alert.AlertType.ERROR);
        }
    }


    private void loadInventory() {
        itemList.clear();
        ResultSet rs = DatabaseConnection.getAllItems();
        try {
            while (rs.next()) {
                itemList.add(new Inventory_Item(rs.getInt("id"), rs.getString("item_name"), rs.getInt("price"),
                        rs.getInt("quantity"), rs.getString("category")));
            }
            inventoryTable.setItems(itemList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
