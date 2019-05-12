package sw.praktikum.risk5.Message;

import java.util.ArrayList;

public class MessageLobby extends Message {
  private static final long serialVersionUID = 1L;
  
  private String gameName;
  private ArrayList<Integer> amountAiwithDifficulty;
  private String[] otherPlayerNames;
  private String[] pictureOfPlayers;
  
  public MessageLobby(String gameName, ArrayList<Integer> amountAiwithDifficulty, String[] otherPlayerNames, String[] pictureOfPlayers) {
    super(MessageType.LOBBY);
    this.setGameName(gameName);
    this.setAmountAiwithDifficulty(amountAiwithDifficulty);
    this.setOtherPlayerNames(otherPlayerNames);
    this.setPictureOfPlayers(pictureOfPlayers);
  }

  public String getGameName() {
    return this.gameName;
  }

  public void setGameName(String gameName) {
    this.gameName = gameName;
  }

  public ArrayList<Integer> getAmountAiwithDifficulty() {
    return this.amountAiwithDifficulty;
  }

  public void setAmountAiwithDifficulty(ArrayList<Integer> amountAiwithDifficulty) {
    this.amountAiwithDifficulty = amountAiwithDifficulty;
  }

  public String[] getOtherPlayerNames() {
    return this.otherPlayerNames;
  }

  public void setOtherPlayerNames(String[] otherPlayerNames) {
    this.otherPlayerNames = otherPlayerNames;
  }

  public String[] getPictureOfPlayers() {
    return this.pictureOfPlayers;
  }

  public void setPictureOfPlayers(String[] pictureOfPlayers) {
    this.pictureOfPlayers = pictureOfPlayers;
  }
}
