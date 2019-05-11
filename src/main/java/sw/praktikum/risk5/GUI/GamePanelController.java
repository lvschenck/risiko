package sw.praktikum.risk5.GUI;

import static java.awt.Color.white;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sw.praktikum.risk5.App.RiskMain;
import sw.praktikum.risk5.Database.Database;
import sw.praktikum.risk5.Json.ReadJson;
import sw.praktikum.risk5.Json.WriteJson;
import sw.praktikum.risk5.Network.ClientInterface;
import sw.praktikum.risk5.Network.ServerInterface;
import sw.praktikum.risk5.Util.CountryValue;

/**
 * Handles the input on the Game Screen.
 *
 * @author lschenck, esali
 */

public class GamePanelController implements Initializable, GuiInterface {

  @FXML
  private Text headerText;
  @FXML
  private Button cardsButton;
  @FXML
  private Button phaseButton;
  @FXML
  private Button roundButton;

  @FXML
  private List<BorderPane> countryPanes;
  @FXML
  private List<BorderPane> playerPanes;
  @FXML
  private StackPane popUpPane;
  @FXML
  private GridPane actionPopup;
  @FXML
  private Text actionPopUpText;
  @FXML
  private GridPane errorPopup;
  @FXML
  private Text errorPopUpText;
  @FXML
  private Spinner troupCount;

  @FXML
  private GridPane cardsPane;
  // @FXML
  // private List<Button> cardButtons;
  @FXML
  private ToggleButton card1;
  @FXML
  private ToggleButton card2;
  @FXML
  private ToggleButton card3;
  @FXML
  private ToggleButton card4;
  @FXML
  private ToggleButton card5;


  @FXML
  private TextField messageInput;
  @FXML
  private TextArea messageOutput;
  @FXML
  private ChoiceBox<String> recipientList;

  private int turnPhase = 0;
  private boolean clickCountFirst = true;
  private boolean currentPlayer;
  private String popText = "";
  private int countryId1;
  private int countryId2;
  private SpinnerValueFactory valueFactory;
  private String[] playerColors =
      {"#6666ff", "#ff6666", "#ffff66", "#b2ff66", "#66666f", "#c0c0c0"};
  private boolean start = true;
  private int ownId;
  private int currentPlayerId;
  private int playerIds[];
  private int[] troops = new int[42]; // Test
  private int[] ownerIds = new int[42]; // Test
  private ReadJson jsonReader = new ReadJson();
  private WriteJson jsonWriter = new WriteJson();
  private int[][] cards;

  private ServerInterface serverInterface;
  private ClientInterface clientInterface;
  private Database db;

  private ObservableList<String> participantList = FXCollections.observableArrayList("all");

  /**
   * When receiving a GameState-json this method updates all values.
   *
   * @param json file containing game information.
   * @author lschenck
   */
  @Override
  public void receiveData(File json) {
    this.jsonReader.readJson(json);
    String jsonName = this.jsonReader.getJsonName();
    this.jsonReader.readRiskGetGameStateJSON(json);
    if (jsonName.equals("gameEnded")) {
      popUpPane.toFront();
      errorPopup.toFront();
      errorPopUpText.setText("Game ended");
    } else {
      if (this.start) {
        initializeOnStart(this.jsonReader.getGameStatePlayerNames(),
            this.jsonReader.getGameStatePlayerIds(), this.jsonReader.getGameStatePlayerUnits(),
            this.jsonReader.getGameStateAiPlayer());
        this.playerIds = this.jsonReader.getGameStatePlayerIds();
        this.ownId = RiskMain.getInstance().getDomain().getData()
            .getUserId(RiskMain.getInstance().getDomain().getPlayerName());
        this.start = false;
        this.currentPlayerId = this.jsonReader.getGameStateCurrentPlayer();
        updateHighlightedPlayer(this.jsonReader.getGameStateCurrentPlayer(), this.jsonReader.getGameStateCurrentPlayer(), this.jsonReader.getGameStatePlayerUnits());
      }
      if (this.currentPlayerId != this.jsonReader.getGameStateCurrentPlayer()) {
        updateHighlightedPlayer(currentPlayerId, this.jsonReader.getGameStateCurrentPlayer(),
            this.jsonReader.getGameStatePlayerUnits());
      }
      this.currentPlayerId = this.jsonReader.getGameStateCurrentPlayer();
      if (this.ownId == this.jsonReader.getGameStateCurrentPlayer()) {
        this.currentPlayer = true;
      } else {
        this.currentPlayer = false;
      }
      this.ownerIds = this.jsonReader.getGameStateCountriesOwner();
      this.troops = this.jsonReader.getGameStateCountriesTroops();
      updateCountries(troops, ownerIds);
      updateTexts(this.jsonReader.getGameStateCurrentTurnPhase(),
          this.jsonReader.getGameStatePlayerUnits());
      this.cards = this.jsonReader.getGameStatePlayerCards();
      this.ownerIds = this.jsonReader.getGameStateCountriesOwner();
      this.troops = this.jsonReader.getGameStateCountriesTroops();
        if (this.jsonReader.getGameStateCurrentGamePhase() == 0) {
          this.valueFactory = new IntegerSpinnerValueFactory(1, 1, 1);
          this.troupCount.setValueFactory(this.valueFactory);
        } else {
          this.valueFactory = new IntegerSpinnerValueFactory(1, 250, 3);
          troupCount.setValueFactory(this.valueFactory);
      } if (this.turnPhase != this.jsonReader.getGameStateCurrentTurnPhase()) {
        this.turnPhase = this.jsonReader.getGameStateCurrentTurnPhase();
        updateCountries(troops, ownerIds);
      }
    }

    System.out.println("back, backe kuchen " + this.jsonReader.getGameStateCurrentPlayer());
  }

  @Override
  public void receiveMessageError(String errorType) {
    popUpPane.toFront();
    errorPopup.toFront();
    String attack = "attack Error";
    if (errorType.equals("attack")) {
      errorPopUpText.setText("attack error");
    } else if (errorType.equals("fortify")) {
      errorPopUpText.setText("fortify error");
    } else if (errorType.equals("place")) {
      errorPopUpText.setText("place error.");
    } else if (errorType.equals("redeemCards")) {
      errorPopUpText.setText("redeem cards error");
    }
  }

  @Override
  public void receiveMessageLoginFail() {

  }

  /**
   * For the currently active player his pane is highlighted
   *
   * @author lschenck
   */
  private void updateHighlightedPlayer(int currentPlayerId, int gameStateCurrentPlayer,
      int[] gameStatePlayerUnits) {
    int playercount = 0;
    for (int i = 0; i < playerIds.length; i++) {
      playercount++;
      if (playerIds[i] == currentPlayerId) {
        playerPanes.get(i).setBlendMode(BlendMode.SRC_OVER);
      }
    }
    for (int i = 0; i < playerIds.length; i++) {
      if (playerIds[i] == gameStateCurrentPlayer) {
        playerPanes.get(i).setBlendMode(BlendMode.MULTIPLY);
      }
    }
  }

  /**
   * For the currently active turnphase the buttons and header text is adjusted
   *
   * @author lschenck
   */
  private void updateTexts(int turnphase, int[] troopsToPlace) {

    int ownTroopsToPlace = 0;
    for (int i = 0; i < troopsToPlace.length; i++) {
      if (playerIds[i] == ownId) {
        ownTroopsToPlace = troopsToPlace[i];
      }
      if (playerIds[i] != 0) {
        Platform.runLater(
            () -> ((Label) this.playerPanes.get(0).getTop()).setText("" + troopsToPlace[0]));
        Platform.runLater(
            () -> ((Label) this.playerPanes.get(1).getTop()).setText("" + troopsToPlace[1]));
        Platform.runLater(
            () -> ((Label) this.playerPanes.get(2).getTop()).setText("" + troopsToPlace[2]));
        Platform.runLater(
            () -> ((Label) this.playerPanes.get(3).getTop()).setText("" + troopsToPlace[3]));
        Platform.runLater(
            () -> ((Label) this.playerPanes.get(4).getTop()).setText("" + troopsToPlace[4]));
        Platform.runLater(
            () -> ((Label) this.playerPanes.get(5).getTop()).setText("" + troopsToPlace[5]));

      }
    }
    if (turnphase == 0) {
      phaseButton.setText("finish fortifying");
      headerText.setText("Place " + ownTroopsToPlace + " troops wisely!");
    } else if (turnphase == 1) {
      phaseButton.setText("finish attacking");
      headerText.setText("Conquer!");
    } else if (turnphase == 2) {
      phaseButton.setText("finish moving");
      headerText.setText("Move your troops wisely");
    }
    if (this.currentPlayer) {
      phaseButton.setDisable(false);
      cardsButton.setDisable(false);
    } else {
      phaseButton.setDisable(true);
      cardsButton.setDisable(true);
    }
  }

  /**
   * When receiving a GameState-json at the Start of the game, some values are set.
   *
   * @param playernames names of all players(<=6) in the game
   * @param playerids ids of all players(<=6) in the game
   * @author lschenck
   */
  private void initializeOnStart(String[] playernames, int[] playerids, int[] troopstoPlace,
      boolean[] isAi) {
    db = RiskMain.getInstance().getDomain().getData();
    for (BorderPane player : playerPanes) {
      player.setOpacity(0);
    }
    for (int i = 0; i < playerids.length; i++) {
      if (playerids[i] != 0) {
        String avatar = db.getPlayerData(String.valueOf(playerids[i]), "avatar");
        playerPanes.get(i).getCenter().getStyleClass().clear();
        playerPanes.get(i).getCenter().getStyleClass().add(avatar);
        playerPanes.get(i).setStyle("-fx-background-color: " + playerColors[i]);
        ((Label) playerPanes.get(i).getBottom()).setText(playernames[i]);
        ((Label) playerPanes.get(i).getTop()).setText(String.valueOf(troopstoPlace[i]));
        playerPanes.get(i).setOpacity(1.0);
      }
      if (isAi[i]) {
        System.out.println("ai erkannt");
        playerPanes.get(i).getCenter().getStyleClass().clear();
        playerPanes.get(i).getCenter().getStyleClass().add("player-ki");
      }
    }

    recipientList.setItems(participantList);
    recipientList.setValue("all");
  }

  @Override
  public boolean runningGame() {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * Creates a Json File and sends it to the Client. You can send attack-, move-, place-json.
   *
   * @param json: the first letter of the json you want to send
   * @param countryId1: id of the first selected country
   * @param player1Id: the corresponding playerId for the first country
   * @param troops: the amount of troops affected
   * @param countryId2: id of the second selected country
   * @param player2Id the corresponding playerId for the second country
   * @author lschenck
   */
  protected void sendJson(char json, int countryId1, int player1Id, int troops, int countryId2,
      int player2Id) {
    int artillry;
    int cavallry;
    int infantry;
    File jsonSend = null;
    switch (json) {
      // Ihr braucht Getter in euren Attack/Move/Place Klassen um an die Parameter zu
      // kommen, die in
      // den
      // verschiedenen write Methoden stehen (auskommentiert)

      case 'a':
        artillry = troops / 10;
        troops = troops % 10;
        cavallry = troops / 5;
        troops = troops % 5;
        infantry = troops;
        jsonSend = this.jsonWriter.writeAttackJson(countryId1, player1Id, cavallry, infantry,
            artillry, countryId2, player2Id);
        // Client interface benutzen um JSON zu schicken, wenn fertig
        break;
      case 'm':
        // File jsonMove = this.jsonWriter.writeMoveJson(sourceCountryID,
        // sourcePlayerID, cavalry,
        // infantry, artillery, targetCountryID, targetPlayerID)
        artillry = troops / 10;
        troops = troops % 10;
        cavallry = troops / 5;
        troops = troops % 5;
        infantry = troops;
        jsonSend = this.jsonWriter.writeMoveJson(countryId1, player1Id, cavallry, infantry,
            artillry, countryId2, player2Id);
        // Client interface benutzen um JSON zu schicken, wenn fertig
        break;
      case 'p':
        jsonSend = this.jsonWriter.writePlaceJson(troops, countryId1, player1Id);
        // Client interface benutzen um JSON zu schicken, wenn fertig
        break;
      case 's':
        break;
      // case 'r':
      // this.jsonWriter.writeCardRedemption(cardGroup.getSelected());
      // break;
      default:
    }
    if (RiskMain.getInstance().getDomain().getIsServer()) {
      RiskMain.getInstance().getDomain().getGame().receiveData(jsonSend);
    } else {
      RiskMain.getInstance().getDomain().getClient().sendJSON(jsonSend);
    }
  }

  /*
   * Sends a message from one player to another possible to either send to one player or to all
   *
   * @author esali
   */
  @FXML
  private void sendMessage() {
    String playerName = RiskMain.getInstance().getDomain().getPlayerName();
    System.out.println(messageInput.getText());
    String text = playerName + ":     " + messageInput.getText();
    if (RiskMain.getInstance().getDomain().getIsServer()) {
      this.serverInterface = RiskMain.getInstance().getDomain().getServer();
      this.serverInterface.sendMessageChat(messageInput.getText(), playerName,
          recipientList.getValue());
    } else {
      this.clientInterface = RiskMain.getInstance().getDomain().getClient();
    }
    messageInput.setText("");
    messageOutput.appendText(text + "\n");
  }

  @Override
  public void receiveMessageChat(String author, String chatMessage, boolean single) {
    if (single) {
      popUpPane.toFront();
      errorPopup.toFront();
      errorPopUpText.setText("server is closed");
    } else {
      messageOutput.appendText(author + ":   ");
      messageOutput.appendText(chatMessage + "\n");
    }
  }

  /**
   * opens Pane with Cards of the User
   *
   * @author esali
   */
  @FXML
  private void showCards(ActionEvent actionEvent) {
    popUpPane.toFront();
    cardsPane.setOpacity(1.0);
    cardsPane.toFront();
  }

  @FXML
  private void useCards(ActionEvent actionEvent) {

  }

  @FXML
  private void cancelCards(ActionEvent actionEvent) {
    cardsPane.setOpacity(0.0);
    cardsPane.toBack();
    popUpPane.toBack();
  }

  @FXML
  private void selectCard1(ActionEvent event) {}

  @FXML
  private void selectCard2() {

  }

  @FXML
  private void selectCard3() {

  }

  @FXML
  private void selectCard4() {

  }

  @FXML
  private void selectCard5() {

  }

  /**
   * Action upon finishing a turnphase.
   *
   * @author lschenck
   */
  @FXML
  private void finishPhase() {
    if (turnPhase == 0) {
      phaseButton.setText("finish attacking");
      turnPhase++;
    } else if (turnPhase == 1) {
      phaseButton.setText("finish moving");
      this.jsonWriter.writeSkipTurnJson();
      turnPhase++;
    } else if (turnPhase == 2) {
      phaseButton.setText("finish fortifying");
      this.jsonWriter.writeSkipTurnJson();
      turnPhase++;
    }
    phaseButton.setDisable(true);
  }

  /**
   * Action upon finishing a round.
   *
   * @author lschenck
   */
  @FXML
  private void finishRound() {
    phaseButton.setDisable(true);
    roundButton.setDisable(true);

    currentPlayer = false;
  }

  /**
   * Opens the menu.
   *
   * @author lschenck
   */

  @FXML
  private void leaveGame(ActionEvent event) {
    Parent newContent = null;

    try {
      newContent = FXMLLoader.load(RiskMain.class.getResource("/view/LobbyPanel.fxml"));

    } catch (IOException e) {
      e.printStackTrace();
    }
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    primaryStage.getScene().setRoot(newContent);
  }

  /**
   * This method values which at which coordinates a mouse click occurred. Values are calculated
   * *1.3 to match the stretched image later.
   *
   * @param mouse the position of the mouse at the time it was clicked
   * @author lschenck
   */
  @FXML
  private void getClickPosition(MouseEvent mouse) {
    int posX = (int) (mouse.getX() / 1.3);
    int posY = (int) (mouse.getY() / 1.3);
    getColorFromPosition(posX, posY);
  }

  /**
   * This method takes the coordinates and determines which color it responds to on the countries
   * Map. Finally from the color it can be determined which country is at given location.
   *
   * @param posX the x coordinate from the mouse click
   * @param posY the y coordinate from the mouse click
   * @author lschenck
   */
  private void getColorFromPosition(int posX, int posY) {
    Image countries = new Image("images/risk_map.gif");
    PixelReader pxr = countries.getPixelReader();
    Color countryColor = pxr.getColor(posX, posY);
    String colorCode = countryColor.toString();
    colorCode = colorCode.substring(2, 8);
    getCountryFromColor(colorCode);
  }

  /**
   * Finally from the calculated color, it becomes clear, which country is at click position.
   * Depending on the GameState action can be taken.
   *
   * @param colorCode color of the chosen country
   * @author lschenck
   */
  private void getCountryFromColor(String colorCode) {
    for (CountryValue value : CountryValue.values()) {
      if (colorCode.equals(value.getColorCode())) {
        if (currentPlayer && turnPhase == 0) {
          openFortifyPopUp(value.toString(), value.ordinal() + 1);
        } else if (currentPlayer && turnPhase == 1) {
          openAtackPopUp(value.toString(), value.ordinal() + 1);
        } else if (currentPlayer && turnPhase == 2) {
          openMovePopUp(value.toString(), value.ordinal() + 1);
        }
      }
    }
  }

  /**
   * Opens the popUp for selecting number of troops to attack/move/place in a country. Sets the
   * input parameters to the selected country and turn fortifysystem.
   *
   * @author lschenck
   */
  private void openFortifyPopUp(String countryName, int countryId) {
    if (this.currentPlayer) {
      this.popText = "How many troops do you want to station in " + countryName + "?";
      this.countryId1 = countryId - 1;
      popUpPane.toFront();
      actionPopup.toFront();
      actionPopup.setOpacity(1.0);
      actionPopUpText.setText(popText);
    }
  }

  /**
   * Opens the popUp for selecting number of troops to attack/move/place in a country. Sets the
   * input parameters to the selected country and turn attack.
   *
   * @author lschenck
   */
  private void openAtackPopUp(String countryName, int countryId) {
    if (this.currentPlayer) {
      if (clickCountFirst) {
        clickCountFirst = false;
        this.popText = "Choose troops to attack from " + countryName;
        this.countryId1 = countryId - 1;
      } else {
        this.popText += " to " + countryName;
        this.countryId2 = countryId - 1;
        popUpPane.toFront();
        actionPopup.toFront();
        actionPopup.setOpacity(1.0);
        actionPopUpText.setText(popText);
        this.clickCountFirst = true;
      }
    }
  }

  /**
   * Opens the popUp for selecting number of troops to attack/move/place in a country. Sets the
   * input parameters to the selected country and turn move.
   *
   * @author lschenck
   */
  private void openMovePopUp(String countryName, int countryId) {
    if (this.currentPlayer) {
      if (clickCountFirst) {
        this.clickCountFirst = false;
        this.popText = "Choose troops to move from " + countryName;
        this.countryId1 = countryId - 1;
      } else {
        this.popText += " to " + countryName;
        this.countryId2 = countryId - 1;
        popUpPane.toFront();
        actionPopup.toFront();
        actionPopup.setOpacity(1.0);
        actionPopUpText.setText(popText);
        clickCountFirst = true;
      }
    }
  }

  /**
   * Closes the popUp for selecting number of troops to attack/move/place in a country.
   *
   * @author lschenck
   */
  @FXML
  private void closeActionPopUp() {
    actionPopup.setOpacity(0.0);
    actionPopup.toBack();
    popUpPane.toBack();
  }

  /**
   * Closes the popUp, which displays error messages.
   *
   * @author lschenck
   */
  @FXML
  private void closeErrorPopUp() {
    errorPopup.setOpacity(0.0);
    errorPopup.toBack();
    popUpPane.toBack();
  }

  /**
   * Based on the given Inputs the parameters for the Json file are confirmed.
   *
   * @author lschenck
   */
  @FXML
  private void confirmPopUpSelection() {
    if (turnPhase == 0) {
      // updateCountryStatus(countryId1, troops, ownId); //ONLY FOR TEST
      sendJson('p', countryId1 + 1, ownId, (Integer) valueFactory.getValue(), 0, 0);

    } else if (turnPhase == 1) {
      this.valueFactory = troupCount.getValueFactory();
      ((Text) countryPanes.get(countryId2).getCenter()).setText(valueFactory.getValue() + "");

      // owner id nicht von 1-42
      sendJson('a', countryId1 + 1, ownerIds[countryId1], (Integer) valueFactory.getValue(),
          countryId2 + 1, ownerIds[countryId2]);

    } else if (turnPhase == 2) {
      sendJson('m', countryId1 + 1, ownerIds[countryId1], (Integer) valueFactory.getValue(),
          countryId2 + 1, ownerIds[countryId2]);
    }
    closeActionPopUp();
  }

  private void updateCountries(int[] troops, int[] ownerIds) {
    for (int i = 0; i < troops.length; i++) {
      updateCountryStatus(i, troops[i], ownerIds[i]);
    }
  }

  /**
   * Changes number of troops and player color for the specified BorderPane of a country.
   *
   * @author lschenck
   */
  private void updateCountryStatus(int countryId, int troops, int player) {
    BorderPane country = countryPanes.get(countryId);

    ((Text) country.getCenter()).setText(String.valueOf(troops));

    for (int i = 0; i < playerIds.length; i++) {
      if (playerIds[i] == player && playerIds[i] != 0) {
        country.setStyle("-fx-background-color: " + playerColors[i]);
      }
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    RiskMain.getInstance().getDomain().setGui(this);
    if (RiskMain.getInstance().getDomain().getIsServer()) {
      RiskMain.getInstance().getDomain().getServer().startGame();
    }

    // int playerSize = 6;

    // the pane for each player is painted in his respective color

    roundButton.setDisable(true);

    recipientList.setItems(participantList);
    recipientList.setValue("all");

  }

  public void setOwnId(int id) {
    this.ownId = id;
  }
}
