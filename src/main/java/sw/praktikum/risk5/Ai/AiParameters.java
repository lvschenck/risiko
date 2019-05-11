package sw.praktikum.risk5.Ai;

/**
 * Parameters for the AI Algorithms.
 * 
 * @author fahaerte
 *
 */
class AiParameters {
  /**
   * default value of a continent (Australia, South-America, North-America, Africa, Europe, Asia).
   */
  protected final static double[] continentsValue = {1.2, 2.3, 0.6, 1.0, 0.2, 3.3};

  /**
   * Weight that is used in the continent priority method in AiPlacementHandler.
   */
  protected final static double weightForContinentPriority = 0.5;
  /**
   * Weight for border countries for country priority method in AiPlacementHandler because border
   * countries are very important to hold.
   */
  protected final static double weightForBorderCountryA = 1.2;
  /**
   * Weight for border countries for country priority method in AiPlacementHandler because border
   * countries are very important to hold.
   */
  protected final static double weightForBorderCountryD = 3;
  /**
   * Weight for the amount of neighbor countries of an AI country used in AiPlacementHandler.
   */
  protected final static double weightForAmountOfNeighbourCountries = 0.2;
  /**
   * This value shows how many troops the AI should have more than all enemies in a continent to
   * have a high probability to conquer the whole continent.
   */
  protected final static double ratioForConqueringWholeContinent = 1.75;
  /**
   * This ratio shows how many troops are needed to conquer a country with a high probability.
   */
  protected final static double ratioToConquerCountry = 1.5;

  /**
   * This ratio shows how many troops are required to conquer an other country.
   */
  protected final static double ratioForRequiredTroopsToConquerCountry = 1.5;
}
