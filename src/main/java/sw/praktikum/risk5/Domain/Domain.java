package sw.praktikum.risk5.Domain;

import sw.praktikum.risk5.Database.Database;
import sw.praktikum.risk5.GUI.GamePanelController;
import sw.praktikum.risk5.GUI.GuiInterface;
import sw.praktikum.risk5.GUI.LobbyPanelController;
import sw.praktikum.risk5.GUI.LobbyPanelInterface;
import sw.praktikum.risk5.GUI.MenuPanelController;
import sw.praktikum.risk5.GUI.MenuPanelInterface;
import sw.praktikum.risk5.Gamelogic.GameController;
import sw.praktikum.risk5.Gamelogic.GameControllerInterface;
import sw.praktikum.risk5.Network.Client;
import sw.praktikum.risk5.Network.ClientInterface;
import sw.praktikum.risk5.Network.Server;
import sw.praktikum.risk5.Network.ServerInterface;

/**
 * This class stores all unique instances. It guarantees that instances only exists once and all
 * packages can get and work with these instances. 
 * Furthermore this class stores some variables for javafx so other panels can work with them.
 * 
 * @see server, gui, game, client unique instances
 * @see playerName, gameName parameters to store
 * @author lroell
 */

public class Domain {
  private ServerInterface server;
  private String gameName;
  private GuiInterface gui;
  private GameControllerInterface game;
  private MenuPanelInterface menu;
  private ClientInterface client;
  private LobbyPanelInterface lobby;
  private String playerName;
  private boolean isServer= false;
  private boolean isClient = false;
  private String[] countryNames;
  private Database data = new Database();
  private boolean inLobby = true;
  private int id;
  private int gameType; // 1: single, 2: multiplayer_host, 3: multiplayer_join 4: tutorial

  public ServerInterface getServer() {
    return this.server;
  }

  public void setServer(ServerInterface server) {
    this.server = server;
  }

  public String getGameName() {
    return this.gameName;
  }

  public void setGameName(String gameName) {
    this.gameName = gameName;
  }

  public GuiInterface getGui() {
    return this.gui;
  }

  public void setGui(GamePanelController gui) {
    this.gui = gui;
  }

  public GameControllerInterface getGame() {
    return this.game;
  }

  public void setGame(GameControllerInterface game) {
    this.game = game;
  }
  
  public MenuPanelInterface getMenu() {
	  return this.menu;
  }
  
  public void setMenu(MenuPanelController menu) {
	  this.menu = menu;
  }

  public ClientInterface getClient() {
    return this.client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public String getPlayerName() {
    return this.playerName;
  }

  public void setPlayerName(String name) {
    this.playerName = name;
  }

  public Boolean isServer() {
    return isServer;
  }

  public void setIsServer(boolean isServer) {
    this.isServer = isServer;
  }

  public int getGameType() {
    return this.gameType;
  }

  public void setGameType(int gameType) {
    this.gameType = gameType;
  }

  public LobbyPanelInterface getLobby() {
    return lobby;
  }

  public void setLobby(LobbyPanelController lobby) {
    this.lobby = lobby;
  }

  public Database getData() {
    return data;
  }

  public void setData(Database data) {
    this.data = data;
  }

  public String[] getCountryNames() {
    return countryNames;
  }

  public void setCountryNames(String[] countryNames) {
    this.countryNames = countryNames;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean getInLobby() {
    return inLobby;
  }

  public void setInLobby(boolean inLobby) {
    this.inLobby = inLobby;
  }

  public boolean isClient() {
    return isClient;
  }

  public void setClient(boolean isClient) {
    this.isClient = isClient;
  }
}
