package sw.praktikum.risk5.Util;

public enum CountryValue {

  /**
   * An Enum to determine which country has which color.
   *
   * @author lschenck
   *
   */
  Alaska("010101", 1),
  NorthWestTerritory("020202", 2),
  Alberta("030303", 1),
  WesternUnitedStates("040404", 1),
  CentralAmerica("050505", 3),
  Greenland("060606", 3),
  Ontario("070707", 3),
  Quebec("080808", 2),
  EasternUnitedStates("090909", 2),

  Venezuela("0a0a0a", 2),
  Peru("0b0b0b", 3),
  Brazil("0c0c0c", 2),
  Argentinia("0d0d0d", 1),

  Iceland("0e0e0e", 1),
  Scandinavia("0f0f0f", 2),
  Ukraine("101010", 2),
  GreatBritain("111111", 3),
  NorthernEurope("121212", 1),
  WesternEurope("131313", 1),
  SouthernEurope("141414", 3),

  NorthAfrica("151515", 1),
  Egypt("161616", 1),
  Congo("171717", 3),
  EastAfrica("181818", 2),
  SouthAfrica("191919", 2),
  Madagascar("1a1a1a", 1),

  Siberia("1b1b1b", 2),
  Ural("1c1c1c", 3),
  China("1d1d1d", 3),
  Afghanistan("1e1e1e", 1),
  MiddleEast("1f1f1f", 2),
  India("202020", 1),
  Siam("212121", 2),
  Yakutsk("222222", 3),
  Irkutsk("232323", 1),
  Mongolia("242424", 2),
  Japan("252525", 1),
  Kamchatka("262626", 3),

  Indonesia("272727", 3),
  NewGuinea("282828", 3),
  WesternAustralia("292929", 2),
  EasternAustralia("2a2a2a", 1);

  public String getColorCode() {
    return colorCode;
  }
public int getCardCode() {
	return cardCode;
}
  private CountryValue(String colorCode, int cardCode) {
    this.colorCode = colorCode;
    this.cardCode = cardCode;
  }

  private final String colorCode;
  private final int cardCode;

}

