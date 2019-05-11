package sw.praktikum.risk5.Ai;

/**
 * Class that simulates a card of the AI.
 * 
 * @author fahaerte
 */
class AiCard {
  private int id;
  private String symbol;

  /**
   * Constructor for a Card. Due to the card's id the method initalizes the card's symbol.
   * @author fahaerte
   * @param cardId The card's id equals to the country's id.
   */
  protected AiCard(int cardId) {
    this.id = cardId;
    switch (cardId) {
      case 1:
      case 3:
      case 4:
      case 13:
      case 14:
      case 19:
      case 21:
      case 22:
      case 26:
      case 30:
      case 32:
      case 35:
      case 37:
      case 42:
        this.symbol = "Infantry";
        break;
      case 2:
      case 8:
      case 9:
      case 10:
      case 12:
      case 15:
      case 16:
      case 24:
      case 25:
      case 27:
      case 31:
      case 33:
      case 36:
      case 41:
        this.symbol = "Attilery";
        break;
      case 5:
      case 6:
      case 7:
      case 11:
      case 17:
      case 18:
      case 20:
      case 23:
      case 28:
      case 29:
      case 34:
      case 38:
      case 39:
      case 40:
        this.symbol = "Cavalry";
        break;
      case 43:
      case 44:
        this.symbol = "Wildcard";
        break;
        default: 
          this.symbol = "null";
    }
  }

  protected String getCardSymbol() {
    return this.symbol;
  }

  protected int getCardId() {
    return this.id;
  }
}
