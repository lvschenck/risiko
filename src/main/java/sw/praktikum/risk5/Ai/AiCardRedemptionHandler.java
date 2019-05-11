package sw.praktikum.risk5.Ai;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class that calculates the action of turning in cards.
 * 
 * @author fahaerte
 */
class AiCardRedemptionHandler {

  private ArrayList<AiCard> holdingCards;

  /**
   * Constructor for the RedemptionHandler.
   * 
   * @author fahaerte
   */
  protected AiCardRedemptionHandler() {
    this.holdingCards = new ArrayList<AiCard>();
  }

  /**
   * Checks if the AI has the ability to turn in cards.
   * 
   * @author fahaerte
   * @return True if the AI can turn in cards
   */
  protected boolean checkPossibilityToTurnIn() {
    Iterator<AiCard> it = this.holdingCards.iterator();
    
    int c1, c2, c3, c4;
    c1 = c2 = c3 = c4 = 0;

    while (it.hasNext()) {
      AiCard c = it.next();
      switch (c.getCardSymbol().charAt(0)) {
        case 'I':
          c1++;
          break;
        case 'A':
          c2++;
          break;
        case 'C':
          c3++;
          break;
        case 'W':
          c4++;
      }
    }
    
    if (c1 >= 3 || c2 >= 3 || c3 >= 3 || (c1 >= 1 && c2 >= 1 && c3 >= 1)) {
      return true;
    } else if (c4 > 0 && this.holdingCards.size() > 2) {
      return true;
    }
    return false;
  }

  /**
   * Method that turns in the cards of the AI.
   * 
   * @return The id of the three cards or null if no redemption is possible
   * @author fahaerte
   */
  protected int[] redeemCards() {

    Iterator<AiCard> it = this.holdingCards.iterator();
    int c1, c2, c3, c4;
    c1 = c2 = c3 = c4 = 0;

    while (it.hasNext()) { // Zaehlt wie viele Karten von welcher Art verfuegbar sind
      AiCard c = it.next();
      switch (c.getCardSymbol().charAt(0)) {
        case 'I':
          c1++;
          break;
        case 'A':
          c2++;
          break;
        case 'C':
          c3++;
          break;
        default:
          c4++;
      }
    }

    int[] redeemedCards = new int[3];

    // Karten loeschen
    if (c1 >= 3) {

      redeemedCards[0] = this.holdingCards.remove(0).getCardId();
      redeemedCards[1] = this.holdingCards.remove(0).getCardId();
      redeemedCards[2] = this.holdingCards.remove(0).getCardId();

    } else if (c2 >= 3) {

      redeemedCards[0] = this.holdingCards.remove(c1).getCardId();
      redeemedCards[1] = this.holdingCards.remove(c1).getCardId();
      redeemedCards[2] = this.holdingCards.remove(c1).getCardId();

    } else if (c3 >= 3) {

      redeemedCards[0] = this.holdingCards.remove(c1 + c2).getCardId();
      redeemedCards[1] = this.holdingCards.remove(c1 + c2).getCardId();
      redeemedCards[2] = this.holdingCards.remove(c1 + c2).getCardId();

    } else if (c1 >= 1 && c2 >= 1 && c3 >= 1) {

      redeemedCards[0] = this.holdingCards.remove(c1 + c2).getCardId();
      redeemedCards[1] = this.holdingCards.remove(c2).getCardId();
      redeemedCards[2] = this.holdingCards.remove(0).getCardId();

    } else if (c4 > 0) {

      if (c1 >= 2) {

        redeemedCards[0] = this.holdingCards.remove(0).getCardId();
        redeemedCards[1] = this.holdingCards.remove(0).getCardId();
        redeemedCards[2] = this.holdingCards.remove(this.holdingCards.size() - 1).getCardId();

      } else if (c2 >= 2) {

        redeemedCards[0] = this.holdingCards.remove(c1).getCardId();
        redeemedCards[1] = this.holdingCards.remove(c1).getCardId();
        redeemedCards[2] = this.holdingCards.remove(this.holdingCards.size() - 1).getCardId();

      } else if (c3 >= 2) {

        redeemedCards[0] = this.holdingCards.remove(c1 + c2).getCardId();
        redeemedCards[1] = this.holdingCards.remove(c1 + c2).getCardId();
        redeemedCards[2] = this.holdingCards.remove(this.holdingCards.size() - 1).getCardId();

      } else if (c1 == 1 && c2 == 1) {

        redeemedCards[0] = this.holdingCards.remove(c1).getCardId();
        redeemedCards[1] = this.holdingCards.remove(0).getCardId();
        redeemedCards[2] = this.holdingCards.remove(this.holdingCards.size() - 1).getCardId();

      } else if (c2 == 1 && c3 == 1) {

        redeemedCards[0] = this.holdingCards.remove(c1 + c2).getCardId();
        redeemedCards[1] = this.holdingCards.remove(c1).getCardId();
        redeemedCards[2] = this.holdingCards.remove(this.holdingCards.size() - 1).getCardId();

      } else if (c1 == 1 && c3 == 1) {

        redeemedCards[0] = this.holdingCards.remove(c1 + c2).getCardId();
        redeemedCards[1] = this.holdingCards.remove(0).getCardId();
        redeemedCards[2] = this.holdingCards.remove(this.holdingCards.size() - 1).getCardId();

      }
    }
    return redeemedCards;
  }

  protected void addCard(AiCard c) {
    this.holdingCards.add(c);
  }

  protected void clearCards() {
    this.holdingCards.clear();
  }
}
