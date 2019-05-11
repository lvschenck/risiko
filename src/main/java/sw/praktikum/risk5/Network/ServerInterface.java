package sw.praktikum.risk5.Network;

import java.io.File;
import sw.praktikum.risk5.Message.*;

public interface ServerInterface {

  void sendJSON(File f);

  void shutDown();

  void sendMessageData();

  void sendMessageChat(String chatmessage, String author, String receiver);

  void sendMessageError(String errorType, int id);

  void sendMessageLobby(String gameName, int[] amountAiWithDifficulty, String[] otherPlayerNames,
      String[] pictureOfPlayers);

  void sendMessageStart();
  
  void startGame();

}
