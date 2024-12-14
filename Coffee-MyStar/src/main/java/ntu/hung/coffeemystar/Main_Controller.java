package ntu.hung.coffeemystar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
        loadView("reports-view.fxml");
    }

    public void logout()
    {
        try
        {
            Stage stage = (Stage) contentArea.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Login");
            stage.centerOnScreen();
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