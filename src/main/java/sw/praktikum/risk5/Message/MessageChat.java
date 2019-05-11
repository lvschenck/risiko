package sw.praktikum.risk5.Message;



public class MessageChat extends Message {

  private static final long serialVersionUID = 1L;
  private String chatMessage;
  private String author;
  private String receiver;
  private boolean single;



  public MessageChat(String chatMessage, String author, String receiver, boolean single) {
    super(MessageType.CHAT);
    this.setChatMessage(chatMessage);
    this.setAuthor(author);
    this.setReceiver(receiver);
    this.single = single;

  }


  public String getAuthor() {
    return author;
  }


  public void setAuthor(String author) {
    this.author = author;
  }


  public String getChatMessage() {
    return chatMessage;
  }


  public void setChatMessage(String chatMessage) {
    this.chatMessage = chatMessage;
  }


  public String getReceiver() {
    return receiver;
  }


  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }
  
  public boolean getSingle() {
    return this.single;
  }



}
