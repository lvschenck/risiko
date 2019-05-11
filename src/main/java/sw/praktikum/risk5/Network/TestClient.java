package sw.praktikum.risk5.Network;

import java.io.File;
import sw.praktikum.risk5.Message.MessageChat;
import sw.praktikum.risk5.Message.MessageLogin;

public class TestClient {
  public static void main(String[] args) {
    ClientInterface client = new Client("134.155.194.144", "Luca","",false);
  }
}
