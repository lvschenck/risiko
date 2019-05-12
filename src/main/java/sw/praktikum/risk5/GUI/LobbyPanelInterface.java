package sw.praktikum.risk5.GUI;

import java.util.ArrayList;

public interface LobbyPanelInterface {
  
  void receiveMessageChat(String author, String chatMessage, boolean single);
  
  void receiveMessageLobby(String gameName, ArrayList<Integer> amountAiWithDifficulty,
      String[] allPlayerNames, String[] pictureOfPlayers);
  
  void receiveMessageStart();

  void sendMessageLobby();
}
