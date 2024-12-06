package ntu.hung.coffeemystar;


import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController
{
    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize()
    {
        welcomeLabel.setText("Welcome to My Star Coffee Dashboard!");
    }
}

