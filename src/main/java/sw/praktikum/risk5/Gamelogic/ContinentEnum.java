package sw.praktikum.risk5.Gamelogic;

/**
 * Class contains all available continents in risk.
 *
 * @author lroell
 */
enum ContinentEnum {
  AFRICA(1), ASIA(2), OCEANIA(3), EUROPE(4), NORTH_AMERICA(5), SOUTH_AMERICA(6);

  private final int id;

  /**
   * Constructor for a continent.
   * 
   * @author lroell
   * @param id ID of the ccontinent
   */
  private ContinentEnum(int id) {
    this.id = id;
  }

  protected int getId() {
    return this.id;
  }
}
