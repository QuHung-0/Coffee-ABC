package ntu.hung.coffeemystar;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class Dashboard_Controller {

    @FXML
    private PieChart salesChart; // Biểu đồ PieChart hiển thị phân bổ doanh thu theo các loại sản phẩm

    @FXML
    private Label totalSalesLabel; // Nhãn hiển thị tổng doanh thu

    @FXML
    private Label lowStockLabel; // Nhãn hiển thị các mặt hàng còn ít trong kho

    // Phương thức khởi tạo, sẽ được gọi khi giao diện được nạp
    public void initialize() {
        // Tải dữ liệu doanh thu vào biểu đồ PieChart
        loadSalesChart();

        // Tải tổng doanh thu và hiển thị lên giao diện
        totalSalesLabel.setText("Tổng doanh thu: " + getTotalSales() + " VND");

        // Hiển thị các mặt hàng còn ít trong kho
        displayLowStockItems();
    }

    // Phương thức để tải dữ liệu doanh thu vào biểu đồ PieChart
    private void loadSalesChart() {
        ResultSet resultSet = DatabaseConnection.getSalesByCategory(); // Lấy dữ liệu doanh thu theo từng loại sản phẩm
        try {
            // Duyệt qua từng dòng dữ liệu từ kết quả truy vấn
            while (resultSet.next()) {
                String category = resultSet.getString("category"); // Lấy tên danh mục sản phẩm
                int sales = resultSet.getInt("category_sales"); // Lấy doanh thu của danh mục sản phẩm
                // Thêm dữ liệu vào biểu đồ PieChart
                salesChart.getData().add(new PieChart.Data(category, sales));
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có sự cố khi truy vấn cơ sở dữ liệu
        }
    }

    // Phương thức để lấy tổng doanh thu từ cơ sở dữ liệu
    private int getTotalSales() {
        return DatabaseConnection.getTotalSales(); // Truy vấn tổng doanh thu từ cơ sở dữ liệu
    }

    // Phương thức để hiển thị các mặt hàng có số lượng thấp trong kho
    private void displayLowStockItems() {
        ResultSet resultSet = DatabaseConnection.getLowStockItems(); // Lấy dữ liệu các mặt hàng có số lượng ít
        StringBuilder lowStockItems = new StringBuilder(); // Dùng StringBuilder để tạo chuỗi thông tin các mặt hàng

        try {
            // Duyệt qua từng mặt hàng có số lượng thấp
            while (resultSet.next()) {
                String itemName = resultSet.getString("item_name"); // Lấy tên mặt hàng
                int quantity = resultSet.getInt("quantity"); // Lấy số lượng còn lại của mặt hàng
                // Thêm thông tin mặt hàng vào chuỗi
                lowStockItems.append(itemName).append(" (").append(quantity).append(" còn lại)\n");
            }

            // Kiểm tra xem có mặt hàng nào có số lượng thấp không
            if (lowStockItems.length() == 0) {
                lowStockLabel.setText("Không có mặt hàng nào còn ít trong kho.");
            } else {
                lowStockLabel.setText(lowStockItems.toString()); // Hiển thị các mặt hàng còn ít trong kho
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có sự cố khi truy vấn cơ sở dữ liệu
        }
    }
}
