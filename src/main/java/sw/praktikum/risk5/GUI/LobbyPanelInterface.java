package sw.praktikum.risk5.GUI;

public interface LobbyPanelInterface {
  
  void receiveMessageChat(String author, String chatMessage, boolean single);
  
  void receiveMessageLobby(String gameName, int[] amountAiWithDifficulty,
      String[] allPlayerNames, String[] pictureOfPlayers);
  
  void receiveMessageStart();
}
