package ntu.hung.coffeemystar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;

public class Inventory_Controller {

    // Các thành phần giao diện người dùng (UI)
    @FXML
    private TableView<Inventory_Item> inventoryTable; // Bảng hiển thị các mặt hàng trong kho

    @FXML
    private TableColumn<Inventory_Item, Integer> CotID; // Cột ID mặt hàng
    @FXML
    private TableColumn<Inventory_Item, String> CotTenMon; // Cột tên món
    @FXML
    private TableColumn<Inventory_Item, Integer> CotGia; // Cột giá
    @FXML
    private TableColumn<Inventory_Item, Integer> CotSoLuong; // Cột số lượng
    @FXML
    private TableColumn<Inventory_Item, String> CotLoai; // Cột loại món

    // Các trường văn bản để nhập liệu
    @FXML
    private TextField IDTextField, TenMonTextField, GiaTextField, SoluongTextField, LoaiTextField;

    // Các nút điều khiển cho việc thêm, xóa, cập nhật món
    @FXML
    private Button btn_ThemMon, btn_XoaMon, btn_CapNhat;

    // Danh sách các món trong kho dưới dạng ObservableList
    private final ObservableList<Inventory_Item> itemList = FXCollections.observableArrayList();

    // Phương thức khởi tạo để thiết lập giao diện
    @FXML
    public void initialize() {
        // Thiết lập các cột trong bảng
        CotID.setCellValueFactory(new PropertyValueFactory<>("id"));
        CotTenMon.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        CotGia.setCellValueFactory(new PropertyValueFactory<>("price"));
        CotSoLuong.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        CotLoai.setCellValueFactory(new PropertyValueFactory<>("category"));

        loadInventory(); // Tải danh sách mặt hàng vào bảng khi ứng dụng bắt đầu
    }

    // Phương thức thêm món mới vào kho
    @FXML
    private void addItem() {
        try {
            // Lấy dữ liệu từ các trường nhập liệu
            String name = TenMonTextField.getText();
            int price = Integer.parseInt(GiaTextField.getText());
            int quantity = Integer.parseInt(SoluongTextField.getText());
            String category = LoaiTextField.getText();

            // Kiểm tra xem món này đã tồn tại trong kho chưa
            if (DatabaseConnection.checkItemExists(name)) {
                showAlert("Error", "Món với tên này đã tồn tại", Alert.AlertType.ERROR);
                return;
            }

            // Thêm món vào kho
            if (DatabaseConnection.addItem(name, price, quantity, category)) {
                showAlert("Success", "Thêm món thành công.", Alert.AlertType.INFORMATION);
                loadInventory(); // Tải lại danh sách món sau khi thêm thành công
            } else {
                showAlert("Error", "Lỗi không thêm được món.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Giá và số lượng phải là số hợp lệ.", Alert.AlertType.ERROR); // Xử lý lỗi nếu giá và số lượng không phải là số
        }
    }

    // Phương thức cập nhật thông tin món
    @FXML
    private void updateItem() {
        try {
            // Lấy dữ liệu từ các trường nhập liệu
            int id = Integer.parseInt(IDTextField.getText());
            String name = TenMonTextField.getText();
            int price = Integer.parseInt(GiaTextField.getText());
            int quantity = Integer.parseInt(SoluongTextField.getText());
            String category = LoaiTextField.getText();

            // Cập nhật thông tin món trong kho
            if (DatabaseConnection.updateItem(id, name, price, quantity, category)) {
                showAlert("Success", "Món cập nhật thành công!", Alert.AlertType.INFORMATION);
                loadInventory(); // Tải lại danh sách món sau khi cập nhật
            } else {
                showAlert("Error", "Thất bại cập nhật món.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "ID, Giá, Số lượng phải là số hợp lệ", Alert.AlertType.ERROR); // Xử lý lỗi nếu ID, giá và số lượng không phải là số
        }
    }

    // Phương thức xóa món khỏi kho
    @FXML
    private void deleteItem() {
        try {
            // Lấy ID của món cần xóa
            int id = Integer.parseInt(IDTextField.getText());

            // Hiển thị hộp thoại xác nhận trước khi xóa
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xóa");
            alert.setHeaderText("Bạn có chắc là xóa món với ID này: " + id + "?");
            alert.setContentText("Hành động này không thể hoàn tác");

            // Nếu người dùng xác nhận xóa
            if (alert.showAndWait().get() == ButtonType.OK) {
                // Thực hiện xóa món
                if (DatabaseConnection.deleteItem(id)) {
                    showAlert("Success", "Món đã xóa thành công!", Alert.AlertType.INFORMATION);
                    loadInventory(); // Tải lại danh sách món sau khi xóa
                } else {
                    showAlert("Error", "Thất bại! Không thể xóa món với ID: " + id, Alert.AlertType.ERROR);
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "ID phải là số hợp lệ.", Alert.AlertType.ERROR); // Xử lý lỗi nếu ID không phải là số hợp lệ
        }
    }

    // Phương thức tải danh sách các mặt hàng từ cơ sở dữ liệu và hiển thị trên bảng
    private void loadInventory() {
        itemList.clear(); // Xóa danh sách hiện tại
        ResultSet rs = DatabaseConnection.getAllItems(); // Lấy tất cả các món từ cơ sở dữ liệu
        try {
            while (rs.next()) {
                // Thêm mỗi món vào danh sách hiển thị
                itemList.add(new Inventory_Item(rs.getInt("id"), rs.getString("item_name"), rs.getInt("price"),
                        rs.getInt("quantity"), rs.getString("category")));
            }
            inventoryTable.setItems(itemList); // Cập nhật bảng với danh sách mới
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có sự cố trong quá trình lấy dữ liệu
        }
    }

    // Phương thức hiển thị thông báo lỗi hoặc thành công
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type); // Chọn loại thông báo
        alert.setTitle(title); // Tiêu đề của thông báo
        alert.setContentText(message); // Nội dung thông báo
        alert.showAndWait(); // Hiển thị thông báo và chờ người dùng đóng
    }
}
