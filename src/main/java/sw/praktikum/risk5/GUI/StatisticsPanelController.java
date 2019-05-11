package sw.praktikum.risk5.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sw.praktikum.risk5.GUI.*;
import sw.praktikum.risk5.App.RiskMain;
import sw.praktikum.risk5.Database.Database;


public class StatisticsPanelController implements Initializable{
	
	Database db;
	
	@FXML
	private Label playedGames;
	@FXML
	private Label totalAmountTroops;
	@FXML
	private Label winRate;
	@FXML
	private Label averagePeakEmpire;
	@FXML
	private Label successfullAttacks;
	@FXML 
	private Label successfullDefenses;
	@FXML
	private TableView<String> table;

	@FXML
  private void openMenu(ActionEvent actionEvent) {
    Parent newContent = null;
    try {
      newContent = FXMLLoader.load(RiskMain.class.getResource("/view/MainMenu.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    primaryStage.getScene().setRoot(newContent);
  }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<org.jdom2.Attribute> attribute = db.getStatisticsLobby(Integer.toString(db.getUserId(RiskMain.getInstance().getDomain().getPlayerName())));
		for(int i=0; i<attribute.size(); i++) {
			switch(attribute.get(i).getName()) {
			case "placement": 
			}
		}
		
	}



}
