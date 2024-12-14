package ntu.hung.coffeemystar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Login_Controller
{
    // Khai báo các trường giao diện (UI components)
    @FXML
    private TextField usernameField; // Trường nhập tên người dùng
    @FXML
    private PasswordField passwordField; // Trường nhập mật khẩu
    @FXML
    private Button loginButton; // Nút đăng nhập
    @FXML
    private Button RegisterButton; // Nút đăng ký

    // Phương thức khởi tạo khi màn hình được tải
    @FXML
    public void initialize()
    {
        // Gán hành động cho nút đăng nhập
        loginButton.setOnAction(event -> handleLogin());
    }

    // Phương thức xử lý đăng nhập khi người dùng nhấn nút đăng nhập
    private void handleLogin()
    {
        // Lấy thông tin tên người dùng và mật khẩu từ giao diện
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Kiểm tra nếu tên người dùng hoặc mật khẩu trống
        if (username.isEmpty() || password.isEmpty())
        {
            showAlert("Error", "Username and password cannot be empty!", Alert.AlertType.ERROR); // Hiển thị thông báo lỗi nếu trống
            return;
        }

        // Kiểm tra thông tin đăng nhập với cơ sở dữ liệu
        if (DatabaseConnection.validateCredentials(username, password))
        {
            showAlert("Success", "Login successful!", Alert.AlertType.INFORMATION); // Hiển thị thông báo thành công nếu đăng nhập hợp lệ
            navigateToDashboard(); // Chuyển đến màn hình chính (Dashboard)
        }
        else
        {
            showAlert("Error", "Invalid username or password.", Alert.AlertType.ERROR); // Hiển thị thông báo lỗi nếu thông tin không hợp lệ
        }
    }

    // Phương thức xử lý khi người dùng nhấn nút đăng ký
    @FXML
    private void navigateToRegister()
    {
        try
        {
            // Lấy cửa sổ hiện tại và thay đổi cảnh (Scene) thành màn hình đăng ký
            Stage stage = (Stage) loginButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register-view.fxml")); // Tải màn hình đăng ký
            stage.setScene(new Scene(loader.load())); // Thiết lập cảnh đăng ký mới
            stage.setTitle("Register"); // Đặt tiêu đề cho cửa sổ
        }
        catch (Exception e)
        {
            e.printStackTrace(); // In ra thông tin lỗi nếu có lỗi xảy ra
            showAlert("Error", "Failed to navigate to Register: " + e.getMessage(), Alert.AlertType.ERROR); // Hiển thị lỗi nếu không thể chuyển màn hình
        }
    }

    // Phương thức chuyển hướng người dùng tới màn hình chính (Dashboard) sau khi đăng nhập thành công
    private void navigateToDashboard()
    {
        try
        {
            // Tải cảnh (Scene) của màn hình chính
            Parent root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root)); // Thiết lập cảnh mới cho cửa sổ
            stage.centerOnScreen();
        }
        catch (Exception e)
        {
            e.printStackTrace(); // In ra thông tin lỗi nếu không thể tải màn hình chính
            showAlert("Error", "Cannot load dashboard: " + e.getMessage(), Alert.AlertType.ERROR); // Hiển thị lỗi nếu không thể tải màn hình
        }
    }

    // Phương thức hiển thị hộp thoại thông báo với các thông tin như tiêu đề, thông điệp và loại thông báo
    private void showAlert(String title, String message, Alert.AlertType type)
    {
        Alert alert = new Alert(type); // Tạo một hộp thoại cảnh báo với loại thông báo đã chọn
        alert.setTitle(title); // Đặt tiêu đề cho hộp thoại
        alert.setContentText(message); // Đặt nội dung cho hộp thoại
        alert.showAndWait(); // Hiển thị hộp thoại và chờ người dùng đóng
    }
}

