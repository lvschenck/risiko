package sw.praktikum.risk5.Network;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import sw.praktikum.risk5.Ai.*;
import sw.praktikum.risk5.App.*;
import sw.praktikum.risk5.Database.Database;
import sw.praktikum.risk5.GUI.GuiInterface;
import sw.praktikum.risk5.GUI.LobbyPanelInterface;
import sw.praktikum.risk5.GUI.GamePanelController;
import sw.praktikum.risk5.Json.ReadJson;
import sw.praktikum.risk5.Message.*;



public class Client implements Runnable, ClientInterface {

  /**
   * Class to handle the network game , when you join a game
   * 
   * @author mgass
   */

  Socket c = null;
  private ObjectOutputStream toServer;
  private ObjectInputStream fromServer;
  private boolean activeP = true;
  private InetAddress IP;
  private Thread t;
  private String username;
  private String password;
  private String IPString;
  private Message message;
  private ReadJson reader;
  private GuiInterface gui = RiskMain.getInstance().getDomain().getGui();
  private LobbyPanelInterface lobby ;
  private boolean aiBool = false;
  private AiInterface ai;

  public Client(String IP, String username, String type, boolean ai, String picture) {
    this.IPString = IP;
    if (this.connect(IP)) {
      try {
        toServer.writeObject(new MessageLogin(username, picture));
      } catch (IOException e) {
        e.printStackTrace();
      }

      if (ai) {
        Database db = RiskMain.getInstance().getDomain().getData();
        if (type.equals("Easy")) {
          this.ai = new AiMain(AiType.EASY, db.getUserId(username), this, username);
        } else if (type.equals("Medium")) {
          this.ai = new AiMain(AiType.MEDIUM, db.getUserId(username), this, username);
        } else if (type.equals("Hard")) {
          this.ai = new AiMain(AiType.HARD, db.getUserId(username), this, username);
        }
        this.aiBool = true;
      } else {
        RiskMain.getInstance().getDomain().setClient(this);
        RiskMain.getInstance().getDomain().setClient(true);
        RiskMain.getInstance().getDomain().getMenu().connect(true);
      }
    } else {
      RiskMain.getInstance().getDomain().getMenu().connect(false);
    }

  }

  /**
   * Method tto check if you can connect to the server
   * 
   * @param IP , the IP of the Host
   * @author mgass
   */

  private boolean connect(String ip) {
    if (c == null || c.isClosed()) {
      try {
        this.c = new Socket(this.IP.getByName(ip), 34567);
        this.toServer = new ObjectOutputStream(c.getOutputStream());
        this.fromServer = new ObjectInputStream(c.getInputStream());
        this.activeP = true;
        this.startThread();
      } catch (UnknownHostException ue) {
        return false;
      } catch (IOException e) {
        return false;
      }
    }
    return true;
  }

  private void startThread() {
    this.t = new Thread(this);
    t.start();
  }

  /**
   * Method to handel incoming jsons and messages from the server
   * 
   * @author mgass
   *
   */
  public void run() {

    while (activeP && !(this.c.isClosed()) && !(this.c.isInputShutdown())) {

      try {

        Object o = this.fromServer.readObject();
        System.out.println(o.getClass().getSimpleName());
        if (!aiBool || o.getClass().getSimpleName().equals("MessageAssignId")
            || o.getClass().getSimpleName().equals("MessageStart")) {
          if (!(o.getClass().getSimpleName().equals("File"))) {
            this.message = (Message) o;
            switch (this.message.getType()) {
              case CHAT:
                String chatMessage = ((MessageChat) this.message).getChatMessage();
                String author = ((MessageChat) this.message).getAuthor();
                boolean single = ((MessageChat) this.message).getSingle();
                if (RiskMain.getInstance().getDomain().getInLobby()) {
                  this.lobby.receiveMessageChat(author, chatMessage, single);
                } else {
                  this.gui.receiveMessageChat(author, chatMessage, single);
                }
                System.out.println("Chat angekommen");
                System.out.println(chatMessage);
                System.out.println(author);
                break;

              case SHUTDOWN:
                if(RiskMain.getInstance().getDomain().getInLobby()) {
                  this.lobby.receiveMessageShutdown();
                }else {
                  this.gui.receiveMessageShutdown();
                }
                this.fromServer.close();
                this.toServer.close();
                this.activeP = false;
                break;

              case ERROR:
                String errorType = ((MessageError) this.message).getErrorType();
                this.gui.receiveMessageError(errorType);

                break;

              case LOGINFAIL:

                this.gui.receiveMessageLoginFail();
                break;
              case START:
                if (!this.aiBool) {
                  this.lobby.receiveMessageStart();
                  RiskMain.getInstance().getDomain().setInLobby(false);
                }
                MessageError me = new MessageError("");
                this.toServer.writeObject(me);
                break;
              case LOBBY:
                MessageLobby ml = (MessageLobby) this.message;
                String gameName = ml.getGameName();
                ArrayList<Integer> amountAiWithDifficulty = ml.getAmountAiwithDifficulty();
                String[] otherPlayerNames = ml.getOtherPlayerNames();
                String[] pictureOfPlayers = ml.getPictureOfPlayers();
                System.out.println("jjjj");
                System.out.println(gameName);
                System.out.println(amountAiWithDifficulty);
                System.out.println(otherPlayerNames[0]);
                System.out.println(pictureOfPlayers[0]);
                this.lobby.receiveMessageLobby(gameName, amountAiWithDifficulty, otherPlayerNames,
                    pictureOfPlayers);
                break;
              case DATA:
                MessageData md = (MessageData) this.message;
                RiskMain.getInstance().getDomain().getData().implementGame(username,md.getPlayerData() ,md.getGame());
                break;
              case ID:
                MessageAssignId maa = (MessageAssignId) this.message;
                if (aiBool) {
                  this.ai.setId(maa.getId());
                } else {
                  RiskMain.getInstance().getDomain().setId(maa.getId());
                }
                
                RiskMain.getInstance().getDomain().getMenu().connect(true);
              default:

                break;

            }
          } else {
            File f = (File) o;
            this.gui.receiveData(f);
            System.out.println("Json");

          }
        } else {
          if (o.getClass().getSimpleName().equals("File")) {
            File f = (File) o;
            ai.performAction(f);
          }
        }
      } catch (ClassNotFoundException e) {

        e.printStackTrace();
      } catch (IOException e) {

        e.printStackTrace();
      }


    }
  }

  /**
   * Method to send a JSON to the server
   * 
   * @mgass
   */

  @Override
  public void sendJSON(File f) {
    try {
      this.toServer.writeObject(f);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Method to send a message to the server
   * 
   * @mgass
   */

  @Override
  public void sendMessage(Message m) {
    try {
      this.toServer.writeObject(m);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void hostDetection() {


  }

  /**
   * Method to send a messageChat to the server
   * 
   * @mgass
   */

  @Override
  public void sendMessageChat(String author, String chatmessage, String receiver) {
    MessageChat message;
    if (receiver.equals("all")) {
      message = new MessageChat(chatmessage, author, receiver, false);

    } else {
      message = new MessageChat(chatmessage, author, receiver, true);
    }

    try {
      this.toServer.writeObject(message);
    } catch (IOException e) {

      e.printStackTrace();
    }


  }

  @Override
  public void setLobby() {
   this.lobby = RiskMain.getInstance().getDomain().getLobby();
    
  }
}


