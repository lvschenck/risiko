package sw.praktikum.risk5.Network;

import java.io.File;
import java.util.ArrayList;
import sw.praktikum.risk5.Message.*;

public interface ServerInterface {

  void sendJSON(File f);

  void shutDown();

  void sendMessageData();

  void sendMessageChat(String author, String chatMessage, String receiver);

  void sendMessageError(String errorType, int id);

  void sendMessageLobby(String gameName, ArrayList<Integer> amountAiWithDifficulty, String[] otherPlayerNames,
      String[] pictureOfPlayers);

  void sendMessageStart();
  
  void startGame();
  
  void sendMessageAssignId(int id);
  
  void broadcast(String author, String receiver, String chatMessage);

}
