package ntu.hung.coffeemystar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection
{

    // Constants for DB connection
    private static final String URL = "jdbc:mysql://localhost:3306/coffeemystar";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    // ======= Login ========

    // Phương thức kiểm tra thông tin đăng nhập của người dùng
    public static boolean validateCredentials(String username, String password)
    {
        String hashedPassword = MD5.hashMD5(password); // Mã hóa mật khẩu người dùng bằng thuật toán MD5
        String query = "SELECT * FROM users WHERE username = ? AND password = ?"; // Câu lệnh truy vấn SQL

        // Thử kết nối với cơ sở dữ liệu và thực thi truy vấn
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query))
        {
            // Thiết lập tham số cho câu lệnh SQL
            stmt.setString(1, username); // Gán tên người dùng vào tham số đầu tiên
            stmt.setString(2, hashedPassword); // Gán mật khẩu đã mã hóa vào tham số thứ hai

            // Thực thi câu lệnh truy vấn và kiểm tra kết quả
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có dòng dữ liệu trả về, nghĩa là thông tin đăng nhập hợp lệ
        }
        catch (Exception e)
        {
            e.printStackTrace(); // In ra thông tin lỗi nếu có
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

    // ======= Register Methods =======

    // Phương thức đăng ký người dùng mới
    public static boolean registerUser(String username, String password)
    {
        String hashedPassword = MD5.hashMD5(password); // Mã hóa mật khẩu người dùng bằng thuật toán MD5
        String query = "INSERT INTO users (username, password) VALUES (?, ?)"; // Câu lệnh truy vấn SQL để chèn dữ liệu vào bảng

        // Thử kết nối với cơ sở dữ liệu và thực thi câu lệnh chèn dữ liệu
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query))
        {
            // Thiết lập tham số cho câu lệnh SQL
            stmt.setString(1, username); // Gán tên người dùng vào tham số đầu tiên
            stmt.setString(2, hashedPassword); // Gán mật khẩu đã mã hóa vào tham số thứ hai

            // Thực thi câu lệnh chèn dữ liệu
            stmt.executeUpdate();
            return true; // Trả về true nếu người dùng được đăng ký thành công
        }
        catch (Exception e)
        {
            e.printStackTrace(); // In ra thông tin lỗi nếu có
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }


    // ======= Inventory Methods =======

    // Check for item exists
    public static boolean checkItemExists(String itemName) {
        String sql = "SELECT * FROM inventory WHERE item_name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemName);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If there's a result, the item exists
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Insert item into inventory
    public static boolean addItem(String itemName, int price, int quantity, String category) {
        String sql = "INSERT INTO inventory (item_name, price, quantity, category) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemName);
            stmt.setInt(2, price);
            stmt.setInt(3, quantity);
            stmt.setString(4, category);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update item in inventory
    public static boolean updateItem(int id, String itemName, int price, int quantity, String category) {
        String sql = "UPDATE inventory SET item_name = ?, price = ?, quantity = ?, category = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemName);
            stmt.setInt(2, price);
            stmt.setInt(3, quantity);
            stmt.setString(4, category);
            stmt.setInt(5, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete item from inventory
    public static boolean deleteItem(int id) {
        String sql = "DELETE FROM inventory WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fetch all items from inventory
    public static ResultSet getAllItems() {
        String sql = "SELECT * FROM inventory";
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ======= Orders Methods =======
    public static int createOrder(int totalPrice)
    {
        String query = "INSERT INTO orders (total_price) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {

            stmt.setInt(1, totalPrice);
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next())
            {
                return keys.getInt(1); // Return the generated order ID
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return -1;
    }

    public static boolean addOrderItem(int orderId, int inventoryId, int quantity, int itemPrice)
    {
        String query = "INSERT INTO order_items (order_id, inventory_id, quantity, item_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query))
        {

            stmt.setInt(1, orderId);
            stmt.setInt(2, inventoryId);
            stmt.setInt(3, quantity);
            stmt.setInt(4, itemPrice);

            stmt.executeUpdate();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean completeOrder(int orderId)
    {
        // Placeholder for further logic such as marking the order as complete in the DB
        return true;
    }

    // ======= Revenue Report Methods =======
    public static List<Revenue_Report> getRevenueReports()
    {
        String query = "SELECT * FROM revenue_report";
        List<Revenue_Report> reports = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery())
        {

            while (rs.next())
            {
                reports.add(new Revenue_Report(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getInt("total_revenue")
                ));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return reports;
    }

    public static boolean addRevenueReport(Date date, int totalRevenue)
    {
        String query = "INSERT INTO revenue_report (date, total_revenue) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query))
        {

            stmt.setDate(1, date);
            stmt.setInt(2, totalRevenue);

            stmt.executeUpdate();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
