package sw.praktikum.risk5.GUI;

import java.io.File;
import sw.praktikum.risk5.Network.Server;
import sw.praktikum.risk5.Network.ServerInterface;

public interface GuiInterface {
  boolean runningGame();

  void receiveData(File file);

  void receiveMessageChat(String author, String chatMessage, boolean single);

  void receiveMessageError(String errorType);

  void receiveMessageLoginFail();
}
