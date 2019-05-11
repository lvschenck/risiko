package sw.praktikum.risk5.Network;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import sw.praktikum.risk5.App.RiskMain;
import sw.praktikum.risk5.GUI.GuiInterface;
import sw.praktikum.risk5.GUI.GamePanelController;
import sw.praktikum.risk5.Gamelogic.*;
import sw.praktikum.risk5.Message.*;

/**
 * class to handle Server Client Connection
 * 
 * @author mgass
 *
 */
public class ServerProtocol extends Thread {

  Socket s; // Socket in Verbindung mit dem Client
  private ObjectOutputStream toClient; // Ausgabe-Strom zum Client
  private ObjectInputStream fromClient; // Eingabe-Strom vom Client
  private boolean activeP = true;
  private String username;
  private String password;
  private int ID=0;
  private GameControllerInterface gameController;
  private boolean runningGame = true;
  private GuiInterface gui = RiskMain.getInstance().getDomain().getGui();
  private ServerInterface server = RiskMain.getInstance().getDomain().getServer();
  private Message message;



  public ServerProtocol(Socket s) {
    this.s = s;
    try {
      this.toClient = new ObjectOutputStream(s.getOutputStream());
      this.fromClient = new ObjectInputStream(s.getInputStream());
      
    } catch (IOException e) {
      System.out.println("IO-Error");
      e.printStackTrace();
    }
  }

  public void run() {
    while (activeP) {
     
      try {
        Object o = this.fromClient.readObject();
        System.out.println(o.getClass().getSimpleName());


        if (!(o.getClass().getSimpleName().equals("File"))) {
          message = (Message) o;

          System.out.println(message.getType());
          switch (message.getType()) {

            case LOGIN:
              if (Server.checkAmount()) {
                this.username = ((MessageLogin) message).getUsername();
                int i = 2;
                do {
                  if (!Server.checkUsernameAvaible(username)) {
                    this.username = username + i;
                    i++;
                  }
                } while (!(Server.checkUsernameAvaible(this.username)));
                
                Server.addUser(ID, toClient, username);
                this.ID = Server.getID(this.username);
                this.server.sendMessageAssignId(this.ID);
              } else {
                MessageLoginFail m = new MessageLoginFail();
                this.toClient.writeObject(m);
              }

              break;

            case LOGOFF:
              runningGame = gui.runningGame();

              if (runningGame) {
                gameController.receiveMessageLogoff(ID);
                Server.RemoveUser(ID);
                // Niederlage an Datenbank ( Aufgabe)
                activeP = false;

              } else {
                Server.RemoveUser(ID);
                activeP = false;
              }

              break;

            case CHAT:
              String chatMessage;
              String author;
              String receiver;

              chatMessage = ((MessageChat) message).getChatMessage();
              author = ((MessageChat) message).getAuthor();
              receiver = ((MessageChat) message).getReceiver();
              Server.broadcast(author, receiver, chatMessage);

              break;
            case ERROR:
              
              this.gameController = RiskMain.getInstance().getDomain().getGame();
            default:

              break;

          }
        } else {
          File f = (File) o;
          this.gameController.receiveData(f);
          System.out.println("JSON");
        }
      } catch (IOException e) {
        System.out.println(e.getClass().getName());
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}
