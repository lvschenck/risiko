package sw.praktikum.risk5.Message;


import sw.praktikum.risk5.Message.Message;
import sw.praktikum.risk5.Message.MessageType;

public class MessageLogin extends Message {


  private static final long serialVersionUID = 1L;
  private String username;

  public MessageLogin(String username) {
    super(MessageType.LOGIN);
    this.username = username;

  }

  public void setUsername(String password) {
    this.username = password;
  }

  public String getUsername() {
    return this.username;
  }


}
