package ntu.hung.coffeemystar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

    // Constants for DB connection
    private static final String URL = "jdbc:mysql://localhost:3306/coffeemystar"; // Địa chỉ kết nối cơ sở dữ liệu MySQL
    private static final String USER = "root"; // Tên người dùng để kết nối CSDL
    private static final String PASSWORD = ""; // Mật khẩu kết nối CSDL

    // ======= Login ========

    // Phương thức kiểm tra thông tin đăng nhập của người dùng
    public static boolean validateCredentials(String username, String password) {
        String hashedPassword = MD5.hashMD5(password); // Mã hóa mật khẩu người dùng bằng thuật toán MD5
        String query = "SELECT * FROM users WHERE username = ? AND password = ?"; // Câu lệnh SQL kiểm tra đăng nhập

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Thiết lập tham số cho câu lệnh SQL
            stmt.setString(1, username); // Gán tên người dùng
            stmt.setString(2, hashedPassword); // Gán mật khẩu đã mã hóa

            // Thực thi câu lệnh truy vấn và kiểm tra kết quả
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có dòng dữ liệu trả về, nghĩa là thông tin đăng nhập hợp lệ
        } catch (Exception e) {
            e.printStackTrace(); // In ra thông tin lỗi nếu có
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

    // ======= Register Methods =======

    // Phương thức đăng ký người dùng mới
    public static boolean registerUser(String username, String password) {
        String hashedPassword = MD5.hashMD5(password); // Mã hóa mật khẩu người dùng
        String query = "INSERT INTO users (username, password) VALUES (?, ?)"; // Câu lệnh SQL để thêm người dùng mới vào bảng

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Thiết lập tham số cho câu lệnh SQL
            stmt.setString(1, username); // Gán tên người dùng
            stmt.setString(2, hashedPassword); // Gán mật khẩu đã mã hóa

            // Thực thi câu lệnh chèn dữ liệu
            stmt.executeUpdate();
            return true; // Trả về true nếu người dùng được đăng ký thành công
        } catch (Exception e) {
            e.printStackTrace(); // In ra thông tin lỗi nếu có
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

    // ======= Inventory Methods =======

    // Kiểm tra xem mặt hàng có tồn tại trong kho không
    public static boolean checkItemExists(String itemName) {
        String sql = "SELECT * FROM inventory WHERE item_name = ?"; // Câu lệnh SQL để kiểm tra sự tồn tại của mặt hàng
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemName); // Gán tên mặt hàng vào tham số
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có dòng dữ liệu trả về, mặt hàng tồn tại
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Thêm mặt hàng vào kho
    public static boolean addItem(String itemName, int price, int quantity, String category) {
        String sql = "INSERT INTO inventory (item_name, price, quantity, category) VALUES (?, ?, ?, ?)"; // Câu lệnh SQL để thêm mặt hàng vào kho
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemName);
            stmt.setInt(2, price);
            stmt.setInt(3, quantity);
            stmt.setString(4, category);
            return stmt.executeUpdate() > 0; // Trả về true nếu chèn dữ liệu thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật thông tin mặt hàng trong kho
    public static boolean updateItem(int id, String itemName, int price, int quantity, String category) {
        String sql = "UPDATE inventory SET item_name = ?, price = ?, quantity = ?, category = ? WHERE id = ?"; // Câu lệnh SQL để cập nhật mặt hàng
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemName);
            stmt.setInt(2, price);
            stmt.setInt(3, quantity);
            stmt.setString(4, category);
            stmt.setInt(5, id);
            return stmt.executeUpdate() > 0; // Trả về true nếu cập nhật thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa mặt hàng khỏi kho
    public static boolean deleteItem(int id) {
        String sql = "DELETE FROM inventory WHERE id = ?"; // Câu lệnh SQL để xóa mặt hàng khỏi kho
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id); // Gán id của mặt hàng cần xóa vào tham số
            return stmt.executeUpdate() > 0; // Trả về true nếu xóa thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy tất cả các mặt hàng từ kho
    public static ResultSet getAllItems() {
        String sql = "SELECT * FROM inventory"; // Câu lệnh SQL để lấy tất cả mặt hàng trong kho
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql); // Trả về kết quả truy vấn
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ======= Orders Methods =======

    // Thêm đơn hàng mới và trả về ID của đơn hàng
    public static int addOrder(double totalPrice) {
        String sql = "INSERT INTO orders (total_price) VALUES (?)"; // Câu lệnh SQL để thêm đơn hàng
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, totalPrice); // Gán giá trị tổng tiền vào tham số
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Trả về ID của đơn hàng
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không thể tạo đơn hàng
    }

    // Thêm mặt hàng vào bảng order_items
    public static boolean addOrderItems(int orderId, int inventoryId, int quantity, double itemPrice) {
        String sql = "INSERT INTO order_items (order_id, inventory_id, quantity, item_price) VALUES (?, ?, ?, ?)"; // Câu lệnh SQL để thêm mặt hàng vào đơn hàng
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, inventoryId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, itemPrice);
            return stmt.executeUpdate() > 0; // Trả về true nếu thêm thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật số lượng kho sau khi đặt hàng
    public static boolean reduceInventoryQuantity(int inventoryId, int quantity) {
        String sql = "UPDATE inventory SET quantity = quantity - ? WHERE id = ? AND quantity >= ?"; // Câu lệnh SQL để giảm số lượng mặt hàng trong kho
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity); // Gán số lượng mặt hàng cần giảm
            stmt.setInt(2, inventoryId); // Gán id mặt hàng
            stmt.setInt(3, quantity); // Kiểm tra xem số lượng trong kho có đủ không
            return stmt.executeUpdate() > 0; // Trả về true nếu giảm thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy các mặt hàng trong kho để chọn cho đơn hàng
    public static ResultSet getInventoryForOrder() {
        String sql = "SELECT id, item_name, price, quantity FROM inventory WHERE quantity > 0"; // Câu lệnh SQL lấy mặt hàng còn trong kho
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql); // Trả về kết quả truy vấn
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ======= Dashboard Methods =======

    // Lấy tổng doanh thu từ bảng orders và order_items
    public static int getTotalSales() {
        String sql = "SELECT SUM(oi.quantity * oi.item_price) AS total_sales " +
                "FROM order_items oi " +
                "JOIN orders o ON oi.order_id = o.id " +
                "WHERE o.order_date <= NOW()"; // Câu lệnh SQL tính tổng doanh thu
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt("total_sales"); // Trả về tổng doanh thu
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Lấy các mặt hàng có số lượng thấp trong kho
    public static ResultSet getLowStockItems() {
        String sql = "SELECT item_name, quantity FROM inventory WHERE quantity < 10"; // Câu lệnh SQL lấy mặt hàng có số lượng thấp
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql); // Trả về kết quả truy vấn
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy doanh thu theo từng loại sản phẩm cho biểu đồ PieChart
    public static ResultSet getSalesByCategory() {
        String sql = "SELECT i.category, SUM(oi.quantity * oi.item_price) AS category_sales " +
                "FROM order_items oi " +
                "JOIN inventory i ON oi.inventory_id = i.id " +
                "JOIN orders o ON oi.order_id = o.id " +
                "WHERE o.order_date <= NOW() " +
                "GROUP BY i.category"; // Câu lệnh SQL lấy doanh thu theo từng loại sản phẩm
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql); // Trả về kết quả truy vấn
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ======= Revenue Report Methods =======

    // Lấy tất cả các đơn hàng từ cơ sở dữ liệu
    public static ResultSet getOrders() {
        String sql = "SELECT * FROM orders ORDER BY order_date DESC"; // Câu lệnh SQL lấy danh sách đơn hàng mới nhất
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql); // Trả về kết quả truy vấn
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Lấy các mặt hàng trong một đơn hàng theo ID đơn hàng
    public static ResultSet getOrderItems(int orderId) {
        String sql = "SELECT oi.id, i.item_name, oi.quantity, oi.item_price " +
                "FROM order_items oi " +
                "JOIN inventory i ON oi.inventory_id = i.id " +
                "WHERE oi.order_id = ?"; // Câu lệnh SQL lấy các mặt hàng trong đơn hàng
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId); // Gán ID đơn hàng vào tham số
            return stmt.executeQuery(); // Trả về kết quả truy vấn
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
