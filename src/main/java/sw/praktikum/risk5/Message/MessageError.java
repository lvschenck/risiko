package sw.praktikum.risk5.Message;

import sw.praktikum.risk5.Message.Message;
import sw.praktikum.risk5.Message.MessageType;

/**
 * Message that is send when a JSON file was not valid. Furthermore the in-game inputs of a player
 * were not valid.
 * 
 * @author lroell
 */

public class MessageError extends Message {

  private static final long serialVersionUID = 1L;
  private String errorType;

  /**
   * @param errorType: either attack or ...
   * 
   * @author lroell
   */
  public MessageError(String errorType) {
    super(MessageType.ERROR);
    this.errorType = errorType;
  }

  public String getErrorType() {
    return errorType;
  }
}
