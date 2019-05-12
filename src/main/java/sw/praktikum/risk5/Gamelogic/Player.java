package sw.praktikum.risk5.Gamelogic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Class that simulates a player in risk.
 * 
 * @author lroell
 */
class Player {

  private int id;
  private String name;
  private Match match;
  private HashSet<Country> conqueredCountries;
  private ArrayList<Card> holdingCards;
  private boolean conquered;
  private boolean ai;
  private int amountOfUnitsToPlace;
  private int amountRedeems;

  /**
   * Constructor for a player object.
   * 
   * @author lroell
   * @param playerId The player's ID
   * @param playerName The player's name
   */
  protected Player(int playerId, String playerName) {
    this.id = playerId;
    this.name = playerName;
    this.conqueredCountries = new HashSet<Country>();
    this.holdingCards = new ArrayList<Card>();
    this.conquered = false;
    this.ai = false;
    this.amountOfUnitsToPlace = 0;
    this.amountRedeems = 0;
  }

  /**
   * Gives a player a defined amount of troops depending on his conquered countries at the beginning
   * of every round.
   * 
   * @author lroell
   */
  protected void receiveTroops() {
    int amount = 0;
    amount += (int) this.conqueredCountries.size() / 3;
    Iterator<Country> it = this.conqueredCountries.iterator();
    int northAmerica = 0;
    int southAmerica = 0;
    int europe = 0;
    int africa = 0;
    int asia = 0;
    int oceania = 0;

    while (it.hasNext()) {
      Country c = it.next();
      switch (c.getId()) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
          northAmerica++;
          break;
        case 10:
        case 11:
        case 12:
        case 13:
          southAmerica++;
          break;
        case 14:
        case 15:
        case 16:
        case 17:
        case 18:
        case 19:
        case 20:
          europe++;
          break;
        case 21:
        case 22:
        case 23:
        case 24:
        case 25:
        case 26:
          africa++;
          break;
        case 27:
        case 28:
        case 29:
        case 30:
        case 31:
        case 32:
        case 33:
        case 34:
        case 35:
        case 36:
        case 37:
        case 38:
          asia++;
          break;
        case 39:
        case 40:
        case 41:
        case 42:
          oceania++;
          break;
        default:
          break;

      }
    }
    if (northAmerica == 9) {
      amount += 5;
    }
    if (southAmerica == 4) {
      amount += 2;
    }
    if (europe == 7) {
      amount += 5;
    }
    if (africa == 6) {
      amount += 3;
    }
    if (asia == 12) {
      amount += 7;
    }
    if (oceania == 4) {
      amount += 2;
    }
    if (amount < 3) {
      amount = 3;
    }
    this.amountOfUnitsToPlace += amount;
  }

  /**
   * Lets a player draw a card if he conquered a country in this round and if the card is available.
   * 
   * @author lroell
   * @param c The received card
   * @return True if the card is available and if the player conquered a country in his turn. False
   *         if not.
   */
  protected boolean drawCard(Card c) {
    ArrayList<Card> availableCards = this.match.getAvailableCards();
    if (this.conquered) {
      availableCards.remove(c);
      this.holdingCards.add(c);
      return true;
    }
    return false;
  }

  /**
   * Redeems cards for a player.
   * 
   * @author fahaerte
   * @param cards Array with cards that should be redeemed
   * @return 0 if the redemption was not valid, else the amount of troops the player receives Method
   *         that redeems 3 Cards of a player and adds the cards to the available Cards of the
   *         match. Calculates also the amount of troops, that a player receives by redemption
   */
  protected boolean redeemCards(Card[] cards) {
    int amount = 0;
    int[] symbols = new int[4];
    for (int i = 0; i < cards.length; i++) {
      switch (cards[i].getSymbol()) {
        case INFANTRY:
          symbols[0]++;
          break;
        case CAVALRY:
          symbols[1]++;
          break;
        case ARTILLERY:
          symbols[2]++;
          break;
        default:
          symbols[3]++;
      }
    }

    boolean validRedemption = false;

    if (symbols[0] == 3 || symbols[1] == 3 || symbols[2] == 3) {
      validRedemption = true;
    } else if (symbols[0] == 1 && symbols[1] == 1 && symbols[2] == 1) {
      validRedemption = true;
    } else if (symbols[3] == 1 && (symbols[0] == 2 || symbols[1] == 2 || symbols[2] == 2)) {
      validRedemption = true;
    } else if (symbols[3] == 1 && ((symbols[0] == 1 && symbols[1] == 1)
        || (symbols[1] == 1 && symbols[2] == 1) || (symbols[0] == 1 && symbols[2] == 1))) {
      validRedemption = true;
    }

    if (validRedemption) {
      this.holdingCards.remove(cards[0]);
      this.holdingCards.remove(cards[1]);
      this.holdingCards.remove(cards[2]);
      this.match.addAvailableCard(cards[0]);
      this.match.addAvailableCard(cards[1]);
      this.match.addAvailableCard(cards[2]);

      if (this.amountRedeems < 5) {
        amount = 4 + this.amountRedeems * 2;
      } else if (this.amountRedeems == 5) {
        amount = 15;
      } else {
        amount = 15 + (this.amountRedeems - 5) * 5;
      }

      if (this.conqueredCountries.contains(cards[1].getCountry())) {
        amount += 2;
      }
      if (this.conqueredCountries.contains(cards[2].getCountry())) {
        amount += 2;
      }
      if (this.conqueredCountries.contains(cards[4].getCountry())) {
        amount += 2;
      }

      this.amountRedeems++;
    }
    this.amountOfUnitsToPlace += amount;
    return validRedemption;
  }

  protected void setMatch(Match m) {
    this.match = m;
  }

  protected int getId() {
    return this.id;
  }

  protected String getName() {
    return this.name;
  }

  protected boolean getAi() {
    return this.ai;
  }

  protected int getAmountOfUnitsToPlace() {
    return this.amountOfUnitsToPlace;
  }

  protected void setAmountOfUnitsToPlace(int amount) {
    this.amountOfUnitsToPlace += amount;
  }

  protected HashSet<Country> getConqueredCountries() {
    return this.conqueredCountries;
  }

  protected ArrayList<Card> getHoldingCards() {
    return this.holdingCards;
  }

  protected void addCountry(Country c) {
    this.conqueredCountries.add(c);
  }

  protected void addCard(Card c) {
    this.holdingCards.add(c);
  }

  protected void incRedeem() {
    this.amountRedeems++;
  }

  protected void setConquered(boolean b) {
    this.conquered = b;
  }

  protected void setAi() {
    this.ai = true;
  }
}
