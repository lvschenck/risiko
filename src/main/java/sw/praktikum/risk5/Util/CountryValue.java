package sw.praktikum.risk5.Util;

public enum CountryValue {

  /**
   * An Enum to determine which country has which color.
   *
   * @author lschenck
   *
   */
  Alaska("010101"),
  NorthWestTerritory("020202"),
  Alberta("030303"),
  WesternUnitedStates("040404"),
  CentralAmerica("050505"),
  Greenland("060606"),
  Ontario("070707"),
  Quebec("080808"),
  EasternUnitedStates("090909"),

  Venezuela("0a0a0a"),
  Peru("0b0b0b"),
  Brazil("0c0c0c"),
  Argentinia("0d0d0d"),

  Iceland("0e0e0e"),
  Scandinavia("0f0f0f"),
  Ukraine("101010"),
  GreatBritain("111111"),
  NorthernEurope("121212"),
  WesternEurope("131313"),
  SouthernEurope("141414"),

  NorthAfrica("151515"),
  Egypt("161616"),
  Congo("171717"),
  EastAfrica("181818"),
  SouthAfrica("191919"),
  Madagascar("1a1a1a"),

  Siberia("1b1b1b"),
  Ural("1c1c1c"),
  China("1d1d1d"),
  Afghanistan("1e1e1e"),
  MiddleEast("1f1f1f"),
  India("202020"),
  Siam("212121"),
  Yakutsk("222222"),
  Irkutsk("232323"),
  Mongolia("242424"),
  Japan("252525"),
  Kamchatka("262626"),

  Indonesia("272727"),
  NewGuinea("282828"),
  WesternAustralia("292929"),
  EasternAustralia("2a2a2a");

  public String getColorCode() {
    return colorCode;
  }

  private CountryValue(String colorCode) {
    this.colorCode = colorCode;
  }

  private final String colorCode;

}
