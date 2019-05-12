package sw.praktikum.risk5.Message;

import org.jdom2.Element;

public class MessageData extends Message {

  private static final long serialVersionUID = 1L;

  private Element game;
  private String[] playerData;
  
  public MessageData(Element game, String[] playerData) {
    super(MessageType.DATA);
    this.game = game;
    this.playerData = playerData;

  }

  public Element getGame() {
    return game;
  }

  public void setGame(Element game) {
    this.game = game;
  }

  public String[] getPlayerData() {
    return playerData;
  }

  public void setPlayerData(String[] playerData) {
    this.playerData = playerData;
  }

}
