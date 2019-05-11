package sw.praktikum.risk5.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sw.praktikum.risk5.Database.Database;
import sw.praktikum.risk5.Domain.Domain;
import javafx.fxml.FXML;
import sw.praktikum.risk5.GUI.*;


/**
 * Launches the Application.
 *
 * @author lschenck, esali
 */

public class RiskMain extends Application {


  @Override
  public void start(Stage primaryStage) throws Exception {


    primaryStage.setTitle("Risiko");

    // all screens are loaded in order to be able to switch seamlessly between them later on
    FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/view/LoginPanel.fxml"));
    Parent rootLogin = loginLoader.load();
    Scene loginScene = new Scene(rootLogin, 1366, 768);


    // the application is started with the login screen
    primaryStage.setScene(loginScene);

    Screen screen = Screen.getPrimary();
    Rectangle2D bounds = screen.getVisualBounds();

    primaryStage.setX(bounds.getMinX());
    primaryStage.setY(bounds.getMinY());
    primaryStage.setWidth(bounds.getWidth());
    primaryStage.setHeight(bounds.getHeight());
    primaryStage.setMaximized(true);
    primaryStage.setFullScreen(true);
    primaryStage.setFullScreenExitHint("");

    primaryStage.show();
  }


  public static void main(String[] args) {
    Application.launch(RiskMain.class, args);
  }

  private static RiskMain instance;
  private Domain domain = new Domain();

  public Domain getDomain() {
    return this.domain;
  }

  public static RiskMain getInstance() {
    if (RiskMain.instance == null) {
      RiskMain.instance = new RiskMain();
    }

    return RiskMain.instance;
  }

}
