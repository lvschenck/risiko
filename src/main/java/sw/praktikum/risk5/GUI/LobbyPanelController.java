package sw.praktikum.risk5.GUI;

import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sw.praktikum.risk5.App.RiskMain;
import sw.praktikum.risk5.Database.Database;
import sw.praktikum.risk5.Message.MessageAssignId;
import sw.praktikum.risk5.Message.MessageLobby;
import sw.praktikum.risk5.Message.MessageUpdateLobby;
import sw.praktikum.risk5.Network.Client;
import sw.praktikum.risk5.Network.ClientInterface;
import sw.praktikum.risk5.Network.Server;
import sw.praktikum.risk5.Network.ServerInterface;

/**
 * Handles the input on the Settings Screen.
 *
 * @author lschenck
 */

public class LobbyPanelController implements LobbyPanelInterface {


  @FXML
  private Button startGame;
  @FXML
  private Text lobbyHeader;
  @FXML
  private ChoiceBox<String> kiStatus1;
  @FXML
  private ChoiceBox<String> kiStatus2;
  @FXML
  private ChoiceBox<String> kiStatus3;
  @FXML
  private ChoiceBox<String> kiStatus4;
  @FXML
  private ChoiceBox<String> kiStatus5;

  @FXML
  private BorderPane player1Pane;
  @FXML
  private BorderPane player2Pane;
  @FXML
  private BorderPane player3Pane;
  @FXML
  private BorderPane player4Pane;
  @FXML
  private BorderPane player5Pane;
  @FXML
  private BorderPane player6Pane;

  @FXML
  private AnchorPane avatarPane1;

  @FXML
  private AnchorPane avatarPane2;

  @FXML
  private StackPane playerPane;

  @FXML
  private StackPane kiPane;

  @FXML
  private GridPane kiPlayerGrid;
  @FXML
  private Button addKiButton;
  @FXML
  private Button removeKiButton;

  @FXML
  private GridPane mainGridPane;

  @FXML
  private TextField messageInput;
  @FXML
  private TextArea messageOutput;
  @FXML
  private ChoiceBox<String> recipientList;
  @FXML
  private Button sendMessageButton;

  private boolean kiSelected;
  private boolean directStart;
  private ServerInterface serverInterface;
  private ClientInterface clientPlayerInterface;
  private ClientInterface clientInterface1;
  private ClientInterface clientInterface2;
  private ClientInterface clientInterface3;
  private ClientInterface clientInterface4;
  private ClientInterface clientInterface5;
  private Database db;
  private ActionEvent actionEvent;

  private ObservableList<String> kiStatusList1 = FXCollections
      .observableArrayList("Easy", "Medium", "Hard");
  private ObservableList<String> kiStatusList2 = kiStatusList1;
  private ObservableList<String> kiStatusList3 = kiStatusList1;
  private ObservableList<String> kiStatusList4 = kiStatusList1;
  private ObservableList<String> kiStatusList5 = kiStatusList1;

  private Integer kiPlayerSize;
  private String[] playerNames;
  private String[] pictures;
  private ArrayList<Integer> amountAiWithDifficulty;

  private ObservableList<String> participantList = FXCollections.observableArrayList("all");

  /**
   * Sends a message from one player to another possible to either send to one player or to all
   *
   * @author esali
   */
  @FXML
  private void sendMessage(ActionEvent actionEvent) {
    String playerName = RiskMain.getInstance().getDomain().getPlayerName();
    System.out.println(messageInput.getText());
    String text = playerName + ":     " + messageInput.getText();
    if (RiskMain.getInstance().getDomain().isServer()) {
      this.serverInterface = RiskMain.getInstance().getDomain().getServer();
      this.serverInterface
          .sendMessageChat(RiskMain.getInstance().getDomain().getPlayerName(),
              messageInput.getText(), recipientList.getValue());
    } else {
      this.clientPlayerInterface = RiskMain.getInstance().getDomain().getClient();
      this.clientPlayerInterface.sendMessageChat(RiskMain.getInstance().getDomain().getPlayerName(),
          messageInput.getText(), recipientList.getValue());
    }
  }

  /**
   * gets Message and displays it in the TextArea
   *
   * @author esali
   */
  @Override
  public void receiveMessageChat(String author, String chatMessage, boolean single) {
    if (single) {
      messageOutput.appendText(
          author + "(" + RiskMain.getInstance().getDomain().getPlayerName() + ")" + ":   ");
      messageOutput.appendText(chatMessage + "\n");
    } else {
      messageOutput.appendText(author + "(all):   ");
      messageOutput.appendText(chatMessage + "\n");
    }
  }

  @Override
  public void receiveMessageLobby(String gameName, ArrayList<Integer> amountAiWithDifficulty,
      String[] allPlayerNames, String[] pictureOfPlayers) {
    System.out.println("receive message lobby gui");
    switch (allPlayerNames.length) {
      case 6:
        ((Label) player6Pane.getCenter()).setText(allPlayerNames[5]);
        ((HBox) player6Pane.getLeft()).getStyleClass().clear();
        ((HBox) player6Pane.getLeft()).getStyleClass().add(pictureOfPlayers[5]);
      case 5:
        ((Label) player5Pane.getCenter()).setText(allPlayerNames[4]);
        ((HBox) player5Pane.getLeft()).getStyleClass().clear();
        ((HBox) player5Pane.getLeft()).getStyleClass().add(pictureOfPlayers[4]);
      case 4:
        ((Label) player4Pane.getCenter()).setText(allPlayerNames[3]);
        ((HBox) player4Pane.getLeft()).getStyleClass().clear();
        ((HBox) player4Pane.getLeft()).getStyleClass().add(pictureOfPlayers[3]);
      case 3:
        ((Label) player3Pane.getCenter()).setText(allPlayerNames[2]);
        ((HBox) player3Pane.getLeft()).getStyleClass().clear();
        ((HBox) player3Pane.getLeft()).getStyleClass().add(pictureOfPlayers[2]);
      case 2:
        System.out.println("lobby player 2" + allPlayerNames[1]);
        player2Pane.setOpacity(1.0);
        ((Label) player2Pane.getCenter()).setText(allPlayerNames[1]);
        ((HBox) player2Pane.getLeft()).getStyleClass().clear();
        ((HBox) player2Pane.getLeft()).getStyleClass().add(pictureOfPlayers[1]);
      case 1:
        ((Label) player1Pane.getCenter()).setText(allPlayerNames[0]);
        ((HBox) player1Pane.getLeft()).getStyleClass().clear();
        ((HBox) player1Pane.getLeft()).getStyleClass().add(pictureOfPlayers[0]);
        break;
    }
    this.playerNames = allPlayerNames;
    this.pictures = pictureOfPlayers;
    this.amountAiWithDifficulty = amountAiWithDifficulty;
    RiskMain.getInstance().getDomain().setGameName(gameName);
    this.lobbyHeader.setText("Gamelobby " + gameName);
    int aiCount = 0;
    for (int i = 0; i < allPlayerNames.length; i++) {
      if ((db.getPlayerData(String.valueOf(db.getUserId(allPlayerNames[i])), "avatar")
          .equals("player-ki"))) {
        switch (aiCount) {
          case 1:
            kiStatus1.setValue(String.valueOf(amountAiWithDifficulty.get(aiCount)));
            break;
          case 2:
            kiStatus2.setValue(String.valueOf(amountAiWithDifficulty.get(aiCount)));
            break;
          case 3:
            kiStatus3.setValue(String.valueOf(amountAiWithDifficulty.get(aiCount)));
            break;
          case 4:
            kiStatus4.setValue(String.valueOf(amountAiWithDifficulty.get(aiCount)));
            break;
          case 5:
            kiStatus5.setValue(String.valueOf(amountAiWithDifficulty.get(aiCount)));
            break;
        }
        aiCount++;
      }
    }

  }

  @Override
  public void receiveMessageStart() {

    System.out.println("spielstart nanan");
    Parent newContent = null;

    Parent root = null;
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GamePanel.fxml"));
      root = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Stage primaryStage = (Stage) kiPane.getScene().getWindow();
    primaryStage.getScene().setRoot(root);
  }

  @Override
  public void sendMessageLobby() {
    if(this.amountAiWithDifficulty == null || this.amountAiWithDifficulty.get(0) == null){
      this.amountAiWithDifficulty = new ArrayList<Integer>();
      amountAiWithDifficulty.add(9);
    }
    if(this.playerNames == null){
      this.playerNames = new String[]{RiskMain.getInstance().getDomain().getPlayerName()};
    }
    if(this.pictures == null){
      this.pictures = new String[]{RiskMain.getInstance().getDomain().getData().getPlayerData(String.valueOf(RiskMain.getInstance().getDomain().getData().getUserId(RiskMain.getInstance().getDomain().getPlayerName())),"avatar")};
    }
  this.serverInterface.sendMessageLobby(RiskMain.getInstance().getDomain().getGameName(), this.amountAiWithDifficulty, this.playerNames, this.pictures);
  }

  @FXML
  private void addKiPlayer() {
    if (kiPlayerSize < 5 && kiSelected == false) {
      Platform.runLater(() -> {
        kiPlayerGrid.getChildren().get(kiPlayerSize - 1).setOpacity(1.0);
        ((BorderPane) kiPlayerGrid.getChildren().get(kiPlayerSize - 1)).getRight()
            .setDisable(false);
      });
      kiPlayerSize += 1;
    }
  }

  @FXML
  private void removeKiPlayer() {
    if (kiPlayerSize > 0 && kiSelected == false) {
      Platform.runLater(() -> {
        kiPlayerGrid.getChildren().get(kiPlayerSize).setOpacity(0.0);
        ((BorderPane) kiPlayerGrid.getChildren().get(kiPlayerSize)).getRight().setDisable(true);
      });
      kiPlayerSize -= 1;
    }
  }

  @FXML
  private void chooseAvatarPlayer(MouseEvent event) {
    avatarPane1.setOpacity(1.0);
    double posY = event.getScreenY() - 225.0;
    if (posY > 180) {
      posY = 180;
    }
    avatarPane1.getChildren().get(0).relocate(88.0, posY);
    playerPane.getChildren().get(0).toFront();
  }

  @FXML
  private void selectAvatarPlayer(MouseEvent event) {
    avatarPane1.setOpacity(0.0);
    playerPane.getChildren().get(1).toBack();
  }

  @FXML
  private void chooseAvatarKi(MouseEvent event) {
    avatarPane2.setOpacity(1.0);
    double posY = event.getScreenY() - 225.0;
    if (posY > 180) {
      posY = 180;
    }
    avatarPane2.getChildren().get(0).relocate(88.0, posY);
    kiPane.getChildren().get(0).toFront();
  }

  @FXML
  private void selectAvatarKi(MouseEvent event) {
    avatarPane2.setOpacity(0.0);
    kiPane.getChildren().get(1).toBack();
  }

  @FXML
  private void openGame(ActionEvent actionEvent) {
    if (!(kiSelected && directStart)) {
    this.serverInterface = new Server(RiskMain.getInstance().getDomain().getPlayerName());
      RiskMain.getInstance().getDomain().setServer(serverInterface);
      int countKi = 0;
      for (int i = 0; i < 5; i++) {
        System.out.println(!((BorderPane) kiPlayerGrid.getChildren().get(i)).getRight()
            .disableProperty()
            .getValue());
        if (!((BorderPane) kiPlayerGrid.getChildren().get(i)).getRight()
            .disableProperty()
            .getValue()) {
          countKi++;
          switch (i) {
            case 0:
              this.clientInterface1 = new Client("localhost", "Gott",
                  (String) kiStatus1.getValue(), true, "player-ki");
              System.out.println("client" + countKi);
              if (kiStatus1.getValue() == "Easy") {
                amountAiWithDifficulty.add(0);
              } else if (kiStatus1.getValue() == "Medium") {
                amountAiWithDifficulty.add(1);
              } else if (kiStatus1.getValue() == "Hard") {
                amountAiWithDifficulty.add(2);
              }
              break;
            case 1:
              this.clientInterface2 = new Client("localhost", "E.T.", (String) kiStatus2.getValue(),
                  true, "player-ki");
              System.out.println("client" + countKi);
              if (kiStatus2.getValue() == "Easy") {
                amountAiWithDifficulty.add(0);
              } else if (kiStatus2.getValue() == "Medium") {
                amountAiWithDifficulty.add(1);
              } else if (kiStatus2.getValue() == "Hard") {
                amountAiWithDifficulty.add(2);
              }
              break;
            case 2:
              this.clientInterface3 = new Client("localhost", "R2D2", (String) kiStatus3.getValue(),
                  true, "player-ki");
              System.out.println("client" + countKi);
              if (kiStatus3.getValue() == "Easy") {
                amountAiWithDifficulty.add(0);
              } else if (kiStatus3.getValue() == "Medium") {
                amountAiWithDifficulty.add(1);
              } else if (kiStatus3.getValue() == "Hard") {
                amountAiWithDifficulty.add(2);
              }
              break;
            case 3:
              this.clientInterface4 = new Client("localhost", "Terminator",
                  (String) kiStatus4.getValue(),
                  true, "player-ki");
              System.out.println("client" + countKi);
              if (kiStatus4.getValue() == "Easy") {
                amountAiWithDifficulty.add(0);
              } else if (kiStatus4.getValue() == "Medium") {
                amountAiWithDifficulty.add(1);
              } else if (kiStatus4.getValue() == "Hard") {
                amountAiWithDifficulty.add(2);
              }
              break;
            case 4:
              this.clientInterface5 = new Client("localhost", "Ultron",
                  (String) kiStatus5.getValue(),
                  true, "player-ki");
              System.out.println("client" + countKi);
              if (kiStatus5.getValue() == "Easy") {
                amountAiWithDifficulty.add(0);
              } else if (kiStatus5.getValue() == "Medium") {
                amountAiWithDifficulty.add(1);
              } else if (kiStatus5.getValue() == "Hard") {
                amountAiWithDifficulty.add(2);
              }
              break;
          }
          try {
            Thread.sleep(50);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }

      if (!directStart) {
        startGame.setText("Start Game");
        directStart = true;
      } else {
        Parent newContent = null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GamePanel.fxml"));

        Parent root = null;
        try {
          root = loader.load();
        } catch (IOException e) {
          e.printStackTrace();
        }
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.getScene().setRoot(root);
      }
      enableMultiPlayerCommunication();
    } else {
      // In Lobby ohne KI Spieler - nutze gameType Variable in Domain für Spiel (Sinle, Multi oder Tutorial)

      Parent newContent = null;

      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GamePanel.fxml"));

      // GamePanelController controller = new GamePanelController(userName,
      // serverInterface);
      // loader.setController(controller);
      Parent root = null;
      try {
        root = loader.load();
      } catch (IOException e) {
        e.printStackTrace();
      }
      Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
      primaryStage.getScene().setRoot(root);
    }
  }

  private void enableMultiPlayerCommunication() {
    addKiButton.setDisable(true);
    removeKiButton.setDisable(true);
    kiStatus1.setDisable(true);
    kiStatus2.setDisable(true);
    kiStatus3.setDisable(true);
    kiStatus4.setDisable(true);
    kiStatus5.setDisable(true);
    startGame.setText("start Game");
    kiSelected = true;

    sendMessageButton.setDisable(false);
    recipientList.setDisable(false);
    messageInput.setDisable(false);
    messageOutput.setDisable(false);
  }

  /**
   * opens the Menu
   *
   * @author esali
   */
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

  /**
   * initialize values
   *
   * @author lvschenck
   */
  public void initialize() {
    this.db = RiskMain.getInstance().getDomain().getData();
    this.amountAiWithDifficulty = new ArrayList<Integer>();
    ((Label) player1Pane.getCenter()).setText(RiskMain.getInstance().getDomain().getPlayerName());
    String avatar = db.getPlayerData(
        String.valueOf(db.getUserId(RiskMain.getInstance().getDomain().getPlayerName())), "avatar");
    ((HBox) player1Pane.getLeft()).getStyleClass().clear();
    ((HBox) player1Pane.getLeft()).getStyleClass().add(avatar);
    kiPlayerSize = 1;

    kiStatus1.setValue("Easy");
    kiStatus1.setItems(kiStatusList1);

    kiStatus2.setValue("Easy");
    kiStatus2.setItems(kiStatusList2);
    kiStatus2.setDisable(true);

    kiStatus3.setValue("Easy");
    kiStatus3.setItems(kiStatusList3);
    kiStatus3.setDisable(true);

    kiStatus4.setValue("Easy");
    kiStatus4.setItems(kiStatusList4);
    kiStatus4.setDisable(true);

    kiStatus5.setValue("Easy");
    kiStatus5.setItems(kiStatusList5);
    kiStatus5.setDisable(true);

    recipientList.setValue("all");
    recipientList.setItems(participantList);
    switch (RiskMain.getInstance().getDomain().getGameType()) {
      default:
        kiSelected = false;
        directStart = false;
        break;
      case 1:
        directStart = true;
        kiSelected = false;
        startGame.setText("Start Game");
        lobbyHeader.setText("Gamelobby " + "Single Player");
        RiskMain.getInstance().getDomain().setGameName("SinglePlayer");
        break;
      case 2:
        kiSelected = false;
        directStart = false;
        lobbyHeader.setText("Gamelobby " + RiskMain.getInstance().getDomain().getGameName());
        break;
      case 3:
        kiSelected = true;
        directStart = true;
        lobbyHeader.setText("Gamelobby " + RiskMain.getInstance().getDomain().getGameName());
        startGame.setDisable(true);
        startGame.setText("Start Game");
        enableMultiPlayerCommunication();
        kiPlayerGrid.getChildren().get(0).setOpacity(0.0);
        ((BorderPane) kiPlayerGrid.getChildren().get(0)).getRight().setDisable(true);
        break;
      case 4:
        startGame.setText("Start Game");
        directStart = true;
        kiSelected = false;
        break;
    }
    RiskMain.getInstance().getDomain().setLobby(this);

    if(RiskMain.getInstance().getDomain().isClient()){
      System.out.println(RiskMain.getInstance().getDomain().isClient()+"    clieeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeent");
      RiskMain.getInstance().getDomain().getClient().setLobby();
      MessageUpdateLobby lobby = new MessageUpdateLobby();
      RiskMain.getInstance().getDomain().getClient().sendMessage(lobby);
    }
  }

  @Override
  public void receiveMessageShutdown() {
    // TODO Auto-generated method stub
    
  }

}
