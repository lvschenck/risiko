package sw.praktikum.risk5.Message;


import sw.praktikum.risk5.Message.Message;
import sw.praktikum.risk5.Message.MessageType;

public class MessageLogin extends Message {


  private static final long serialVersionUID = 1L;
  private String username;
  private String picture;

  public MessageLogin(String username, String picture) {
    super(MessageType.LOGIN);
    this.username = username;
    this.picture = picture;

  }

  public void setUsername(String password) {
    this.username = password;
  }

  public String getUsername() {
    return this.username;
  }


  public String getPicture() {
    return picture;
  }
}
