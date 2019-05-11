package sw.praktikum.risk5.Network;

import java.io.File;
import sw.praktikum.risk5.Message.MessageChat;

public class TestServer {
  private boolean test = true;
  static ServerInterface server = new Server("Mo");

  public static void change() {
    File f = new File("src/main/resources/JSON/attack.json");
    server.sendJSON(f);

    MessageChat m = new MessageChat("Hallo Test", "Moritz", "all", true);
//    server.sendMessage(m);
    System.out.println("change");
  }

  public static void main(String[] args) {


    ServerInterface server = new Server("Mo");

  


  }

}
