package sw.praktikum.risk5.Network;

import java.io.File;
import sw.praktikum.risk5.Message.Message;

public interface ClientInterface {

  void sendJSON(File f);

  void sendMessage(Message m);

  void sendMessageChat(String chatmessage, String author, String receiver);

}
