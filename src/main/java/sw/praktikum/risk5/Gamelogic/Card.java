package sw.praktikum.risk5.Gamelogic;

/**
 * Class simulates a Card in risk
 *
 * @author fahaerte
 */

class Card {

  private Country country;
  private SymbolEnum symbol;
  private int id;

  /**
   * Constructor for a card-object
   * 
   * @author fahaerte
   * @param country The country that is displayed on a card
   * @param symbol Type from SymbolEnum
   * @param id Card-ID equals Country-ID
   */
  protected Card(Country country, SymbolEnum symbol, int id) {
    this.country = country;
    this.symbol = symbol;
    this.id = id;
  }

  protected Country getCountry() {
    return this.country;
  }

  protected SymbolEnum getSymbol() {
    return this.symbol;
  }

  protected int getId() {
    return this.id;
  }
}
