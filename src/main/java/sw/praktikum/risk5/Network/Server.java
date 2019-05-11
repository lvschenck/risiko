package sw.praktikum.risk5.Network;



import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import sw.praktikum.risk5.App.RiskMain;
import sw.praktikum.risk5.Database.Database;
import sw.praktikum.risk5.GUI.GuiInterface;
import sw.praktikum.risk5.GUI.LobbyPanelInterface;
import sw.praktikum.risk5.Gamelogic.*;
import sw.praktikum.risk5.Message.*;


/**
 * Class starts Server
 *
 * @author mgass
 */

public class Server extends Thread implements ServerInterface {
  private final static int port = 34567;
  private final static String localhost = "localhost";
  protected static int amount = 6;
  private static HashMap<Integer, ObjectOutputStream> outputStreams =
      new HashMap<Integer, ObjectOutputStream>();
  private static HashMap<Integer, String> users = new HashMap<Integer, String>();
  private GameControllerInterface gameController;
  private GuiInterface gui;
  private LobbyPanelInterface lobby = RiskMain.getInstance().getDomain().getLobby();
  private int id;
  private Database db = RiskMain.getInstance().getDomain().getData();



  public Server(String username) {
    this.start();
    id = this.db.getUserId(username);
    this.users.put(this.id, username);
    RiskMain.getInstance().getDomain().setIsServer(true);
  }

  public void run() {

    try {
      ServerSocket server = new ServerSocket(port);
      Socket s;
      while (true) {
        s = server.accept();
        new ServerProtocol(s).start();
      }

    } catch (IOException e) {


    }

  }

  protected static boolean checkUsernameAvaible(String username) {

    return !(RiskMain.getInstance().getDomain().getData().checkUser(username));

  }

  protected static boolean checkAmount() {
    return amount > users.size();
  }

  protected static int getID(String username) {
    Database db = RiskMain.getInstance().getDomain().getData();
    return db.getUserId(username);

  }

  protected static boolean checkPassword(String username, String password) {
    return true;
  }


  protected static void addUser(int ID, ObjectOutputStream toClient, String username) {
    Database db = RiskMain.getInstance().getDomain().getData();
    db.createTempUser(username);
    int dbId = db.getUserId(username);
    outputStreams.put(dbId, toClient);
    users.put(dbId, username);
    System.out.println("verbunden");

  }

  protected static void RemoveUser(int ID) {
    users.remove(ID);
    outputStreams.remove(ID);
    // mögliche Chat Nachricht dass User spiel verlassen hat
  }



  /*
   * Method to send a chat message to all players
   * 
   * @mgass
   */

  public void broadcast(String author, String receiver, String chatMessage) {
    int idAuthor;

    idAuthor = getIDWithUser(author);

    if (receiver.equals("all")) {
      
      Iterator iterator = outputStreams.entrySet().iterator();
      while (iterator.hasNext()) {
        HashMap.Entry hM = (HashMap.Entry) iterator.next();
        Message message;
        message = new MessageChat(chatMessage, author, receiver, false);

        try {
          outputStreams.get(hM.getKey()).writeObject(message);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      this.gui.receiveMessageChat(author, chatMessage, false);
    } else {
      int idReceiver = getIDWithUser(receiver);
      Message message;
      message = new MessageChat(chatMessage, author, receiver, true);
      if(receiver.equals(this.id)) {
        this.gui.receiveMessageChat(author, chatMessage, true);
      }
      try {
        outputStreams.get(idReceiver).writeObject(message);
      } catch (IOException e) {

        e.printStackTrace();
      }

    }

  }

  protected static int getIDWithUser(String username) {
    Iterator iterator = users.entrySet().iterator();
    while (iterator.hasNext()) {
      HashMap.Entry hM = (HashMap.Entry) iterator.next();
      if (hM.getValue().equals(username)) {
        return (int) hM.getKey();
      }
    }

    return 0;
  }

  public void startGame() {
    int length = users.size();
    int[] ids = new int[length];
    String[] usernames = new String[length];
    int i = 0;
    Iterator iterator = users.entrySet().iterator();
    while (iterator.hasNext()) {
      HashMap.Entry hM = (HashMap.Entry) iterator.next();
      usernames[i] = (String) hM.getValue();
      ids[i] = (int) hM.getKey();
      System.out.println(usernames[i] + "  " + ids[i]);
      System.out.println(usernames.length);
      i++;
    }
    this.gui = RiskMain.getInstance().getDomain().getGui();
    this.gameController = new GameController();
    RiskMain.getInstance().getDomain().setGame(gameController);
    
    this.gameController.startGame(ids, usernames);
    Message m = new MessageStart();
    this.sendMessage(m);
  }


  public void sendJSON(File f) {

    Iterator iterator = outputStreams.entrySet().iterator();
    gui.receiveData(f);
    while (iterator.hasNext()) {
      HashMap.Entry hM = (HashMap.Entry) iterator.next();
      try {
        outputStreams.get(hM.getKey()).writeObject(f);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void sendMessage(Message m) {
    Iterator iterator = outputStreams.entrySet().iterator();
    while (iterator.hasNext()) {
      HashMap.Entry hM = (HashMap.Entry) iterator.next();
      try {
        outputStreams.get(hM.getKey()).writeObject(m);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void sendMessage(Message m, int id) {
    try {
      outputStreams.get(id).writeObject(m);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void shutDown() {
    MessageShutdown mS = new MessageShutdown();
    this.sendMessage(mS);
  }

  @Override
  public void sendMessageData() {

  }

  @Override
  public void sendMessageChat(String chatMessage, String author, String receiver) {
    broadcast(author, receiver, chatMessage);
  }

  @Override
  public void sendMessageError(String errorType, int id) {
    if (id == this.id) {
      this.gui.receiveMessageError(errorType);
    } else {
      MessageError m = new MessageError(errorType);
      this.sendMessage(m, id);
    }
  }

  /**
   * sends a message to all players when a new player joins. All players get the information that
   * the player joint and the new player gets the information about how the current lobby looks
   * like.
   * 
   * @author lroell
   * @param gameName name of the current game
   * @param amountAiWithDifficulty the amount of AIs the hoster wants to play with and the
   *        difficulty
   * @param allPlayerNames the names of all players in the current lobby
   * @param pictureOfPlayers the picture of all players in the current lobby
   */
  public void sendMessageLobby(String gameName, int[] amountAiWithDifficulty,
      String[] allPlayerNames, String[] pictureOfPlayers) {
    // muss aufgerufen werden wenn neuer Spieler joint
    MessageLobby ml =
        new MessageLobby(gameName, amountAiWithDifficulty, allPlayerNames, pictureOfPlayers);
    this.sendMessage(ml);
    this.lobby.receiveMessageLobby(gameName, amountAiWithDifficulty, allPlayerNames,
        pictureOfPlayers);
  }

  /**
   * send a message to all players when the host presses on the start game button. All players can
   * now switch to the gamepanel
   * 
   * @author lroell
   */
  public void sendMessageStart() {
    // muss aufgerufen werden wenn Host auf start drückt
    MessageStart ms = new MessageStart();
    this.sendMessage(ms);
  }

  public void sendMessageAssignId(int id) {
    MessageAssignId maa = new MessageAssignId(id);
    this.sendMessage(maa, id);
  }

}

