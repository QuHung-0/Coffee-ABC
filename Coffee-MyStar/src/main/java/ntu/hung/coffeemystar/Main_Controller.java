package ntu.hung.coffeemystar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main_Controller
{
    @FXML
    private Label welcomeText;

    @FXML
    private AnchorPane contentArea;

    public void navigateToDashboard()
    {
        loadView("dashboard-view.fxml");
    }

    public void navigateToOrders()
    {
        loadView("orders-view.fxml");
    }

    public void navigateToMenu()
    {
        loadView("inventory-view.fxml");
    }

    public void navigateToReports()
    {
        loadView("report-view.fxml");
    }

    /*
        public void logout()
        {
            try
            {
                Stage stage = (Stage) contentArea.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
                stage.setScene(new Scene(loader.load()));
                stage.setTitle("Đăng Nhập");
                stage.centerOnScreen();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }*/
    public void logout()
    {
        try
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("");
            alert.setHeaderText("Đăng xuất");
            alert.setContentText("Bạn có chắc bạn muốn đăng xuất");

            if (alert.showAndWait().get() == ButtonType.OK)
            {
                Stage stage = (Stage) contentArea.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
                stage.setScene(new Scene(loader.load()));
                stage.setTitle("Đăng Nhập");
                stage.centerOnScreen();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void loadView(String fxml)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            AnchorPane pane = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(pane);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}