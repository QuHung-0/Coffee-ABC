package ntu.hung.coffeemystar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController
{

    // Khai báo các trường giao diện (UI components)
    @FXML
    private TextField usernameField; // Trường nhập tên người dùng
    @FXML
    private PasswordField passwordField; // Trường nhập mật khẩu
    @FXML
    private PasswordField repasswordField; // Trường nhập lại mật khẩu
    @FXML
    private Button register; // Nút đăng ký
    @FXML
    private Button loginPage; // Nút quay lại trang đăng nhập

    // Phương thức khởi tạo khi màn hình được tải
    @FXML
    public void initialize()
    {
        // Gán hành động cho nút đăng ký
        register.setOnAction(event -> handleRegister());

        // Gán hành động cho nút quay lại trang đăng nhập
        loginPage.setOnAction(event -> navigateToLogin());
    }

    // Phương thức xử lý đăng ký người dùng mới
    private void handleRegister()
    {
        String username = usernameField.getText(); // Lấy tên người dùng từ trường nhập
        String password = passwordField.getText(); // Lấy mật khẩu từ trường nhập
        String rePassword = repasswordField.getText(); // Lấy mật khẩu nhập lại từ trường nhập

        // Kiểm tra nếu các trường bị bỏ trống
        if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty())
        {
            showAlert("Error", "All fields must be filled.", Alert.AlertType.ERROR); // Hiển thị thông báo lỗi nếu có trường trống
            return;
        }

        // Kiểm tra nếu mật khẩu và mật khẩu nhập lại không khớp
        if (!password.equals(rePassword))
        {
            showAlert("Error", "Passwords do not match.", Alert.AlertType.ERROR); // Hiển thị thông báo lỗi nếu mật khẩu không khớp
            return;
        }

        // Đăng ký người dùng mới
        boolean isRegistered = DatabaseConnection.registerUser(username, password);
        if (isRegistered)
        {
            showAlert("Success", "User registered successfully!", Alert.AlertType.INFORMATION); // Hiển thị thông báo thành công nếu đăng ký thành công
            navigateToLogin(); // Chuyển đến trang đăng nhập
        }
        else
        {
            showAlert("Error", "Registration failed. Username might already exist.", Alert.AlertType.ERROR); // Hiển thị thông báo lỗi nếu đăng ký thất bại
        }
    }

    // Phương thức chuyển hướng người dùng quay lại trang đăng nhập
    @FXML
    private void navigateToLogin()
    {
        try
        {
            // Lấy cửa sổ hiện tại và thay đổi cảnh (Scene) thành màn hình đăng nhập
            Stage stage = (Stage) loginPage.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml")); // Tải màn hình đăng nhập
            stage.setScene(new Scene(loader.load())); // Thiết lập cảnh đăng nhập mới
            stage.setTitle("Login"); // Đặt tiêu đề cho cửa sổ
        }
        catch (Exception e)
        {
            e.printStackTrace(); // In ra thông tin lỗi nếu có lỗi xảy ra
            showAlert("Error", "Failed to navigate to Login: " + e.getMessage(), Alert.AlertType.ERROR); // Hiển thị lỗi nếu không thể chuyển màn hình
        }
    }

    // Phương thức hiển thị hộp thoại thông báo với các thông tin như tiêu đề, thông điệp và loại thông báo
    private void showAlert(String title, String message, Alert.AlertType alertType)
    {
        Alert alert = new Alert(alertType); // Tạo một hộp thoại cảnh báo với loại thông báo đã chọn
        alert.setTitle(title); // Đặt tiêu đề cho hộp thoại
        alert.setContentText(message); // Đặt nội dung cho hộp thoại
        alert.showAndWait(); // Hiển thị hộp thoại và chờ người dùng đóng
    }
}
