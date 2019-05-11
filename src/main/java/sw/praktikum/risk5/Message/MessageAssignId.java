package sw.praktikum.risk5.Message;

public class MessageAssignId extends Message {
  private static final long serialVersionUID = 1L;
  private int id;

  public MessageAssignId(int id) {
    super(MessageType.ID);
    this.id = id;
  }

  public int getId() {
    return this.id;
  }
}
