package sw.praktikum.risk5.GUI;

import javafx.scene.control.Alert;
import javafx.stage.Window;

public class AlertHelper {

  public static void showAlert(Alert.AlertType alertType, Window window, String title,
      String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.initOwner(window);
    alert.show();
  }
}
