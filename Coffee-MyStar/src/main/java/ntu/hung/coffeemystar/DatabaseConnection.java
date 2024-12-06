package ntu.hung.coffeemystar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseConnection
{
    // Khai báo các hằng số để kết nối với cơ sở dữ liệu
    private static final String URL = "jdbc:mysql://localhost:3306/coffeemystar"; // Địa chỉ kết nối cơ sở dữ liệu
    private static final String USER = "root"; // Tên người dùng cho kết nối cơ sở dữ liệu
    private static final String PASSWORD = ""; // Mật khẩu cho người dùng

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

}


