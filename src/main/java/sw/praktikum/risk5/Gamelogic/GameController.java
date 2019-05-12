package sw.praktikum.risk5.Gamelogic;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import sw.praktikum.risk5.App.RiskMain;
import sw.praktikum.risk5.Database.Database;
import sw.praktikum.risk5.Json.ReadJson;
import sw.praktikum.risk5.Json.WriteJson;
import sw.praktikum.risk5.Network.Client;
import sw.praktikum.risk5.Network.ClientInterface;
import sw.praktikum.risk5.Network.ServerInterface;

/**
 * Class in which the game risk is controlled.
 *
 * @author lroell, fahaerte
 */
public class GameController implements GameControllerInterface {

  private Dice dice;
  private Match match;
  private BattleHandler battleHandler;
  private ArrayList<Country> allCountries = new ArrayList<Country>();
  private ArrayList<Card> allCards;
  private WriteJson jsonWriter = new WriteJson();
  private ReadJson jsonReader = new ReadJson();
  private Database database = RiskMain.getInstance().getDomain().getData();
  private ServerInterface server;
  private Statistics stats;

  /**
   * When the host presses start this method creates a gamController with the players that want to
   * play the game. This method also initializes all needed instances for the game.
   *
   * @param ids all players playing the game (the host and the players in the lobby)
   * @param usernames the usernames from all players playing the game
   * @author lroell
   */
  public void startGame(int[] ids, String[] usernames) {
    this.server = RiskMain.getInstance().getDomain().getServer();
    this.initCountrys();
    this.initializeCards(this.allCountries);
    this.stats = new Statistics();
    String gameName = RiskMain.getInstance().getDomain().getGameName();
    this.match = new Match(this.allCards, this.allCountries, gameName, this.stats);
    for (int i = 0; i < ids.length; i++) {
      this.createPlayer(usernames[i], ids[i]);
    }
    this.stats.setPlayerLength(this.match.getPlayers().size());
    this.dice = new Dice();
    this.battleHandler = new BattleHandler(this.dice, this.match, this.stats);
    this.match.establishOrder();
    this.match.receiveTroopsBeginning();
    this.sendJson('r');
  }

  /**
   * Sends Json to server.
   *
   * @param json first letter of json File
   * @author lroell
   */
  protected void sendJson(char json) {
    switch (json) {
      case 'r':
        File jsonGameState = this.jsonWriter.writeGetGameStateJson(this.match.getCountriesName(),
            this.match.getCountriesIds(), this.match.getCountriesOwners(),
            this.match.getCountriesTroops(), this.match.getCountriesNeighbors(),
            this.match.getName(), this.match.getMatchId(), this.match.getPlayersId(),
            this.match.getPlayerAi(), this.match.getPlayerNames(),
            this.match.getPlayersUnitsToPlace(), this.match.getPlayerCards(),
            this.match.getCurrentPlayerId(), this.match.getCurrentTurnPhase(),
            this.match.getCurrentGamePhase());
        this.server.sendJSON(jsonGameState);
        break;
      case 'e':
        Iterator<Player> it = this.match.getPlayers().iterator();
        Player first = null;
        Player second = null;
        while (it.hasNext()) {
          Player tmp = it.next();
          int placement = Integer.parseInt(
              this.database.getGameData(tmp.getName(), "" + this.match.getMatchId(), "placement"));
          if (placement == 1) {
            first = tmp;
          }
          if (placement == 2) {
            second = tmp;
          }
        }
        File jsonEndGame = this.jsonWriter.writeEndGameJson(first.getId(), second.getId());
        this.server.sendJSON(jsonEndGame);
        break;
      // wenn host auch weiter drückt dann ruft ui server.shutDown()
      default:

    }
  }

  /**
   * When receiving a json file this method will on the first place check which file was received.
   * Furthermore this method handles the json files depending on his type if they were send
   * correctly. If it was send correctly, the method changes the current GameSate depending on the
   * received json and sends a updated GameState-json to the Server. If the json file includes
   * illegal movements, it will send an Error to the Server.
   *
   * @param json can be different json files
   * @author lroell
   */
  public void receiveData(File json) {
    this.jsonReader.readJson(json);
    String name = this.jsonReader.getJsonName();
    System.out.println(name);
    if (name.equals("redeemCards")) {
      Player player = this.match.getCurrentPlayer();
      int[] cardsId = this.jsonReader.getCardRedemptionCardsId();
      Card[] cards = new Card[cardsId.length];
      for (int i = 0; i < cards.length; i++) {
        cards[i] = this.allCards.get(cardsId[i]);
      }
      if (this.redeemCards(player, cards)) {
        this.sendJson('r');
      } else {
        this.server.sendMessageError("redeemCards", this.match.getCurrentPlayerId());
      }
    } else if (name.equals("skip_turn")) {
      this.match.endTurnPhase();
    } else if (name.equals("attack")) {
      this.jsonReader.readAttackJSON(json);
      int amountAttack = this.jsonReader.getAttackInfantry()
          + this.jsonReader.getAttackCavalry() * 5 + this.jsonReader.getAttackAtillery() * 10;
      Country defendingCountry =
          this.allCountries.get(this.jsonReader.getAttackTargetCountry() - 1);
      int amountDefend = defendingCountry.getTroops();
      System.out
          .println(amountAttack + "   " + amountDefend + "defenderID" + defendingCountry.getId());
      //Test
      int[] countrieOwner = this.match.getCountriesOwners();
      int[] countrieTroops = this.match.getCountriesTroops();
      System.out.println("CountrieOwner");
      for (int i = 0; i< 42; i++) {
        System.out.println(countrieOwner[i]);
      }
      System.out.println("CountrieTroops");
      for (int i = 0; i< 42; i++) {
        System.out.println(countrieTroops[i]);
      }
      
      //Test
      boolean b = this.attack(this.jsonReader.getAttackSourcePlayer(),
          this.jsonReader.getAttackSourceCountry(), amountAttack, amountDefend,
          this.jsonReader.getAttackTargetPlayer(), this.jsonReader.getAttackTargetCountry());
      
      int[] countrieOwner2 = this.match.getCountriesOwners();
      int[] countrieTroops2 = this.match.getCountriesTroops();
      
      System.out.println("---------------------------------------");
      System.out.println("CountrieOwner");
      for (int i = 0; i< 42; i++) {
        System.out.println(countrieOwner2[i]);
      }
      System.out.println("CountrieTroops");
      for (int i = 0; i< 42; i++) {
        System.out.println(countrieTroops2[i]);
      }
      try {
        Thread.sleep(3000000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      if (b) {
        this.sendJson('r');
      } else {
        this.server.sendMessageError("attack", this.match.getCurrentPlayerId());
      }
    } else if (name.equals("gameEnded")) {
      this.jsonReader.readEndGameJSON(json);
      // Davor Statistiken in XML eintragen
      this.endMatch();
    } else if (name.equals("getStatistics")) {
      this.jsonReader.readGetStatisticsJSON(json);
      // Statistiken aus XML wählen
    } else if (name.equals("move")) {
      this.jsonReader.readMoveJSON(json);
      int amount =
          this.jsonReader.getMoveUnitsInfantry() + this.jsonReader.getMoveUnitsCavalry() * 5
              + this.jsonReader.getMoveUnitsArtillery() * 10;
      Country source = this.allCountries.get(this.jsonReader.getMoveSourceCountry() - 1);
      Country target = this.allCountries.get(this.jsonReader.getMoveTargetCountry() - 1);
      boolean b = this.fortifyPosition(this.jsonReader.getMoveSourcePlayer(), source, amount,
          this.jsonReader.getMoveTargetPlayer(), target);
      this.match.endTurnPhase();
      if (b) {
        this.sendJson('r');
      } else {
        this.server.sendMessageError("fortify", this.match.getCurrentPlayerId());
      }
    } else if (name.equals("placeArmy")) {
      this.jsonReader.readPlaceJSON(json);
      boolean b = this.chooseCountry(this.jsonReader.getPlaceCountry(),
          this.jsonReader.getPlacePlayer(), this.jsonReader.getPlaceNumberOfUnits());
      Player currentPlayer = this.match.getCurrentPlayer();
      if (this.match.getCurrentGamePhase() == 2) {
        if (currentPlayer.getAmountOfUnitsToPlace() == 0) {
          this.match.updateStatistics();
          this.match.endTurnPhase();
        }
      }
      if (b) {
        this.sendJson('r');
      } else {
        this.server.sendMessageError("place", this.match.getCurrentPlayerId());
      }
    }
  }

  /**
   * When a player presses the leave-button this method replaces the player with a medium-AI.
   *
   * @param id of the player
   * @author lroell
   */
  public void receiveMessageLogoff(int id) {
    Player player = this.match.getPlayers().get(id);
    player.setAi();
    // Simon methode um namem zu ersetzen
    ClientInterface clientAi = new Client("localhost", "AIHorst", "Medium", true, null);
  }

  /**
   * Places troops on a country the player chooses. Used to first own a country or to place troops
   * on a country you already own.
   *
   * @param amount the amount of troops the player want to place in the country
   * @param playerId the Player who wants to take a country
   * @param targetCountryId the country that is chosen
   * @return true if all countries have an owner, else: false
   * @author lroell
   */
  protected boolean chooseCountry(int targetCountryId, int playerId, int amount) {
    boolean b = this.match.chooseCountry(targetCountryId, playerId, amount);
    return b;
  }

  /**
   * Fortifying of your position.
   *
   * @param sourcePlayerId the player'id
   * @param sourceCountry the id of the source country
   * @param amount the amount of troops
   * @param targetPlayer the target player id
   * @param targetCountry the target country id
   * @return boolean whether everything was valid or not
   * @author fahaerte
   */
  protected boolean fortifyPosition(int sourcePlayerId, Country sourceCountry, int amount,
      int targetPlayer, Country targetCountry) {
    return this.match.fortifyPosition(sourcePlayerId, sourceCountry, amount, targetPlayer,
        targetCountry);
  }

  /**
   * gives a player a defined amount of troops depending on his conquered countries at the beginning
   * of every round.
   *
   * @param player the player who receives troops
   * @author lroell
   */
  protected void receiveTroops(Player player) {
    player.receiveTroops();
  }

  /**
   * Invokes the method redeemCards of the class Player Redeem Cards of a player.
   *
   * @param player Player who wants to redeem cards
   * @param cards 3 cards that should be turned in
   * @author fahaerte
   */
  protected boolean redeemCards(Player player, Card[] cards) {
    return player.redeemCards(cards);
  }

  /**
   * Calculates the attack.
   *
   * @param attacker The attacking country
   * @param diceAtt the dice of the attacker
   * @param target the attacked country,
   * @param dieDef the dice of the defender
   * @return The winner of the battle
   * @author fahaerte
   */
  protected boolean attack(int attackerId, int attackerCountry, int attackingTroops,
      int defendingTroops, int targetId, int targetCountry) {
    return this.battleHandler.attack(attackerId, attackerCountry, attackingTroops, defendingTroops,
        targetId, targetCountry);
  }

  /**
   * Ends a match.
   *
   * @author lroell
   */
  public void endMatch() {
    this.match.endMatch();
    this.match = null;
  }

  /**
   * Creates a player with an unique ID.
   *
   * @param name the player's name
   * @param id the player's id
   * @return the id of the player
   * @author lroell
   */
  public int createPlayer(String name, int id) {
    Player newP = new Player(id, name);
    return this.match.createPlayer(newP);
  }

  /**
   * Deletes a player with a specified ID.
   *
   * @param id the player's id
   * @author lroell
   */
  public void deletePlayer(int id) {
    this.match.deletePlayer(id);
  }

  /**
   * Returns the amount of player that are currently involved in a match.
   * 
   * @author lroell
   */
  public int getPlayerLength() {
    return this.match.getPlayers().size();
  }

  /**
   * This method initializes all countries and safes them in a class' instance.
   *
   * @author gschakar
   */
  protected void initCountrys() {
    String[] countries = {"Alaska", "Northwest-Territory", "Alberta", "Western-United-States",
        "Central-America", "Greenland", "Ontario", "Quebec", "Eastern-United-States", "Venezuela",
        "Peru", "Brazil", "Argentina", "Iceland", "Scandinavia", "Ukraine", "Great-Britain",
        "Northern-Europe", "Western-Europe", "Southern-Europe", "North-Africa", "Egypt", "Congo",
        "East-Africa", "South-Africa", "Madagascar", "Siberia", "Ural", "China", "Afghanistan",
        "Middle-East", "India", "Siam", "Yakutsk", "Irkutsk", "Mongolia", "Japan", "Kamchatka",
        "Indonesia", "New-Guinea", "Western-Australia", "Eastern-Australia"};
    for (int i = 0; i < countries.length; i++) {
      Country memory = new Country(countries[i], i + 1);
      this.allCountries.add(memory);
    }
    RiskMain.getInstance().getDomain().setCountryNames(countries);
    Country alaska = getCountryFromList("Alaska");
    Country[] alaskaNeigh = {getCountryFromList("Kamchatka"), getCountryFromList("Alberta"),
        getCountryFromList("Northwest-Territory")};
    alaska.setNeighbors(alaskaNeigh);

    Country northwest = getCountryFromList("Northwest-Territory");
    Country[] northwestNeigh = {getCountryFromList("Alaska"), getCountryFromList("Alberta"),
        getCountryFromList("Ontario"), getCountryFromList("Greenland")};
    northwest.setNeighbors(northwestNeigh);

    Country alberta = getCountryFromList("Alberta");
    Country[] albertaNeigh = {getCountryFromList("Northwest-Territory"),
        getCountryFromList("Quebec"), getCountryFromList("Iceland")};
    alberta.setNeighbors(albertaNeigh);

    Country westUnited = getCountryFromList("Western-United-States");
    Country[] westUnitedNeigh = {getCountryFromList("Alberta"), getCountryFromList("Ontario"),
        getCountryFromList("Eastern-United-States"), getCountryFromList("Central-America")};
    westUnited.setNeighbors(westUnitedNeigh);

    Country centralA = getCountryFromList("Central-America");
    Country[] centralANeigh = {getCountryFromList("Western-United-States"),
        getCountryFromList("Eastern-United-States"), getCountryFromList("Venezuela")};
    centralA.setNeighbors(centralANeigh);

    Country greenland = getCountryFromList("Greenland");
    Country[] greenlandNeigh = {getCountryFromList("Northwest-Territory"),
        getCountryFromList("Quebec"), getCountryFromList("Iceland")};
    greenland.setNeighbors(greenlandNeigh);

    Country ontario = getCountryFromList("Ontario");
    Country[] ontarioNeigh = {getCountryFromList("Northwest-Territory"),
        getCountryFromList("Alberta"), getCountryFromList("Quebec"),
        getCountryFromList("Western-United-States"), getCountryFromList("Eastern-United-States")};
    ontario.setNeighbors(ontarioNeigh);

    Country quebec = getCountryFromList("Quebec");
    Country[] quebecNeigh = {getCountryFromList("Greenland"), getCountryFromList("Ontario"),
        getCountryFromList("Eastern-United-States")};
    quebec.setNeighbors(quebecNeigh);

    Country easternUnited = getCountryFromList("Eastern-United-States");
    Country[] easternUnitedNeigh =
        {getCountryFromList("Central-America"), getCountryFromList("Western-United-States"),
            getCountryFromList("Ontario"), getCountryFromList("Quebec")};
    easternUnited.setNeighbors(easternUnitedNeigh);

    Country venezuela = getCountryFromList("Venezuela");
    Country[] venezuelaNeigh = {getCountryFromList("Central-America"), getCountryFromList("Peru"),
        getCountryFromList("Brazil")};
    venezuela.setNeighbors(venezuelaNeigh);

    Country peru = getCountryFromList("Peru");
    Country[] peruNeigh = {getCountryFromList("Venezuela"), getCountryFromList("Brazil"),
        getCountryFromList("Argentina")};
    peru.setNeighbors(peruNeigh);

    Country brazil = getCountryFromList("Brazil");
    Country[] brazilNeigh = {getCountryFromList("Venezuela"), getCountryFromList("Peru"),
        getCountryFromList("Argentina"), getCountryFromList("North-Africa")};
    brazil.setNeighbors(brazilNeigh);

    Country argentina = getCountryFromList("Argentina");
    Country[] argentinaNeigh = {getCountryFromList("Peru"), getCountryFromList("Brazil")};
    argentina.setNeighbors(argentinaNeigh);

    Country iceland = getCountryFromList("Iceland");
    Country[] icelandNeigh = {getCountryFromList("Greenland"), getCountryFromList("Great-Britain")};
    iceland.setNeighbors(icelandNeigh);

    Country scandinavia = getCountryFromList("Scandinavia");
    Country[] scandinaviaNeigh =
        {getCountryFromList("Northern-Europe"), getCountryFromList("Ukraine")};
    scandinavia.setNeighbors(scandinaviaNeigh);

    Country ukraine = getCountryFromList("Ukraine");
    Country[] ukraineNeigh = {getCountryFromList("Scandinavia"),
        getCountryFromList("Northern-Europe"), getCountryFromList("Southern-Europe"),
        getCountryFromList("Ural"), getCountryFromList("Afghanistan")};
    ukraine.setNeighbors(ukraineNeigh);

    Country gb = getCountryFromList("Great-Britain");
    Country[] gbNeigh = {getCountryFromList("Iceland"), getCountryFromList("Western-Europe"),
        getCountryFromList("Northern-Europe")};
    gb.setNeighbors(gbNeigh);

    Country northernEu = getCountryFromList("Northern-Europe");
    Country[] northernEuNeigh = {getCountryFromList("Great-Britain"),
        getCountryFromList("Scandinavia"), getCountryFromList("Ukraine"),
        getCountryFromList("Southern-Europe"), getCountryFromList("Western-Europe")};
    northernEu.setNeighbors(northernEuNeigh);

    Country westEu = getCountryFromList("Western-Europe");
    Country[] westEuNeigh =
        {getCountryFromList("Great-Britain"), getCountryFromList("North-Africa"),
            getCountryFromList("Northern-Europe"), getCountryFromList("Southern-Europe")};
    westEu.setNeighbors(westEuNeigh);

    Country southernEu = getCountryFromList("Southern-Europe");
    Country[] southernEuNeigh =
        {getCountryFromList("Western-Europe"), getCountryFromList("North-Africa"),
            getCountryFromList("Egypt"), getCountryFromList("Middle-East"),
            getCountryFromList("Ukraine"), getCountryFromList("Northern-Europe")};
    southernEu.setNeighbors(southernEuNeigh);

    Country northAfrica = getCountryFromList("North-Africa");
    Country[] northAfricaNeigh =
        {getCountryFromList("Western-Europe"), getCountryFromList("Brazil"),
            getCountryFromList("Egypt"), getCountryFromList("Southern-Europe"),
            getCountryFromList("East-Africa"), getCountryFromList("Congo")};
    northAfrica.setNeighbors(northAfricaNeigh);

    Country egypt = getCountryFromList("Egypt");
    Country[] egyptNeigh =
        {getCountryFromList("North-Africa"), getCountryFromList("Southern-Europe"),
            getCountryFromList("Middle-East"), getCountryFromList("East-Africa")};
    egypt.setNeighbors(egyptNeigh);

    Country congo = getCountryFromList("Congo");
    Country[] congoNeigh = {getCountryFromList("North-Africa"), getCountryFromList("East-Africa"),
        getCountryFromList("South-Africa")};
    congo.setNeighbors(congoNeigh);

    Country eastA = getCountryFromList("East-Africa");
    Country[] eastANeigh = {getCountryFromList("Egypt"), getCountryFromList("North-Africa"),
        getCountryFromList("Congo"), getCountryFromList("South-Africa"),
        getCountryFromList("Madagascar"), getCountryFromList("Middle-East")};
    eastA.setNeighbors(eastANeigh);

    Country southA = getCountryFromList("South-Africa");
    Country[] southANeigh = {getCountryFromList("Congo"), getCountryFromList("East-Africa"),
        getCountryFromList("Madagascar")};
    southA.setNeighbors(southANeigh);

    Country madagascar = getCountryFromList("Madagascar");
    Country[] madagascarNeigh =
        {getCountryFromList("South-Africa"), getCountryFromList("East-Africa")};
    madagascar.setNeighbors(madagascarNeigh);

    Country siberia = getCountryFromList("Siberia");
    Country[] siberiaNeigh = {getCountryFromList("Ural"), getCountryFromList("Yakutsk"),
        getCountryFromList("Irkutsk"), getCountryFromList("Mongolia"), getCountryFromList("China")};
    siberia.setNeighbors(siberiaNeigh);

    Country ural = getCountryFromList("Ural");
    Country[] uralNeigh = {getCountryFromList("Ukraine"), getCountryFromList("Afghanistan"),
        getCountryFromList("China"), getCountryFromList("Siberia")};
    ural.setNeighbors(uralNeigh);

    Country china = getCountryFromList("China");
    Country[] chinaNeigh = {getCountryFromList("Siam"), getCountryFromList("India"),
        getCountryFromList("Afghanistan"), getCountryFromList("Ural"),
        getCountryFromList("Siberia"), getCountryFromList("Mongolia")};
    china.setNeighbors(chinaNeigh);

    Country afghanistan = getCountryFromList("Afghanistan");
    Country[] afghanistanNeigh = {getCountryFromList("Ukraine"), getCountryFromList("Ural"),
        getCountryFromList("Middle-East"), getCountryFromList("India"),
        getCountryFromList("China")};
    afghanistan.setNeighbors(afghanistanNeigh);

    Country middleEast = getCountryFromList("Middle-East");
    Country[] middleEastNeigh =
        {getCountryFromList("Southern-Europe"), getCountryFromList("Ukraine"),
            getCountryFromList("Egypt"), getCountryFromList("East-Africa"),
            getCountryFromList("Afghanistan"), getCountryFromList("India")};
    middleEast.setNeighbors(middleEastNeigh);

    Country india = getCountryFromList("India");
    Country[] indiaNeigh = {getCountryFromList("Middle-East"), getCountryFromList("Afghanistan"),
        getCountryFromList("China"), getCountryFromList("Siam")};
    india.setNeighbors(indiaNeigh);

    Country siam = getCountryFromList("Siam");
    Country[] siamNeigh =
        {getCountryFromList("India"), getCountryFromList("China"), getCountryFromList("Indonesia")};
    siam.setNeighbors(siamNeigh);

    Country yakutsk = getCountryFromList("Yakutsk");
    Country[] yakutskNeigh = {getCountryFromList("Siberia"), getCountryFromList("Irkutsk"),
        getCountryFromList("Kamchatka")};
    yakutsk.setNeighbors(yakutskNeigh);

    Country irkutsk = getCountryFromList("Irkutsk");
    Country[] irkutskNeigh = {getCountryFromList("Siberia"), getCountryFromList("Yakutsk"),
        getCountryFromList("Kamchatka"), getCountryFromList("Mongolia")};
    irkutsk.setNeighbors(irkutskNeigh);

    Country mongolia = getCountryFromList("Mongolia");
    Country[] mogoliaNeigh =
        {getCountryFromList("China"), getCountryFromList("Siberia"), getCountryFromList("Irkutsk"),
            getCountryFromList("Kamchatka"), getCountryFromList("Japan")};
    mongolia.setNeighbors(mogoliaNeigh);

    Country japan = getCountryFromList("Japan");
    Country[] japanNeigh = {getCountryFromList("Mongolia"), getCountryFromList("Kamchatka")};
    japan.setNeighbors(japanNeigh);

    Country kamchatka = getCountryFromList("Kamchatka");
    Country[] kamchatkaNeigh = {getCountryFromList("Yakutsk"), getCountryFromList("Irkutsk"),
        getCountryFromList("Mongolia"), getCountryFromList("Japan"), getCountryFromList("Alaska")};
    kamchatka.setNeighbors(kamchatkaNeigh);

    Country indonesia = getCountryFromList("Indonesia");
    Country[] indonesiaNeigh = {getCountryFromList("Siam"), getCountryFromList("New-Guinea"),
        getCountryFromList("Western-Australia")};
    indonesia.setNeighbors(indonesiaNeigh);

    Country newGuinea = getCountryFromList("New-Guinea");
    Country[] newGuineaNeigh =
        {getCountryFromList("Indonesia"), getCountryFromList("Eastern-Australia")};
    newGuinea.setNeighbors(newGuineaNeigh);

    Country westAus = getCountryFromList("Western-Australia");
    Country[] wetsAusNeigh = {getCountryFromList("Indonesia"), getCountryFromList("New-Guinea"),
        getCountryFromList("Eastern-Australia")};
    westAus.setNeighbors(wetsAusNeigh);

    Country eastAus = getCountryFromList("Eastern-Australia");
    Country[] eastAusNeigh =
        {getCountryFromList("Western-Australia"), getCountryFromList("New-Guinea")};
    eastAus.setNeighbors(eastAusNeigh);

    Iterator<Country> it = this.allCountries.iterator();
    int i = 0;
    while (it.hasNext()) {
      Country c = it.next();
      if (i < 9) {
        c.setContinent(ContinentEnum.NORTH_AMERICA);
      }
      if (i >= 9 && i < 13) {
        c.setContinent(ContinentEnum.SOUTH_AMERICA);
      }
      if (i >= 13 && i < 20) {
        c.setContinent(ContinentEnum.EUROPE);
      }
      if (i >= 20 && i < 26) {
        c.setContinent(ContinentEnum.AFRICA);
      }
      if (i >= 26 && i < 38) {
        c.setContinent(ContinentEnum.ASIA);
      }
      if (i >= 38 && i < 41) {
        c.setContinent(ContinentEnum.OCEANIA);
      }
      i++;
    }
  }

  /**
   * This method return a county from the country list by using a String for the search.
   *
   * @param s String value to search for the country
   * @return the requested country
   * @author gschakar
   */
  protected Country getCountryFromList(String s) {
    Country rueckgabe = null;
    for (Country c : this.allCountries) {
      if (c.getName().equals(s)) {
        rueckgabe = c;
      }
    }
    return rueckgabe;
  }

  /**
   * Initializes all 42 cards and wildcards.
   *
   * @param country ArrayList with all countries
   * @return ArrayList<Card> all initialized cards
   * @author lroell
   */
  protected ArrayList<Card> initializeCards(ArrayList<Country> country) {
    ArrayList<Card> cards = new ArrayList<Card>();
    ArrayList<Country> countries = country;

    Iterator<Country> it = countries.iterator();
    SymbolEnum[] symbols = {SymbolEnum.INFANTRY, SymbolEnum.ARTILLERY, SymbolEnum.INFANTRY,
        SymbolEnum.INFANTRY, SymbolEnum.CAVALRY, SymbolEnum.CAVALRY, SymbolEnum.CAVALRY,
        SymbolEnum.ARTILLERY, SymbolEnum.ARTILLERY, SymbolEnum.ARTILLERY, SymbolEnum.CAVALRY,
        SymbolEnum.ARTILLERY, SymbolEnum.INFANTRY, SymbolEnum.INFANTRY, SymbolEnum.ARTILLERY,
        SymbolEnum.ARTILLERY, SymbolEnum.CAVALRY, SymbolEnum.CAVALRY, SymbolEnum.INFANTRY,
        SymbolEnum.CAVALRY, SymbolEnum.INFANTRY, SymbolEnum.INFANTRY, SymbolEnum.CAVALRY,
        SymbolEnum.ARTILLERY, SymbolEnum.ARTILLERY, SymbolEnum.INFANTRY, SymbolEnum.ARTILLERY,
        SymbolEnum.CAVALRY, SymbolEnum.CAVALRY, SymbolEnum.INFANTRY, SymbolEnum.ARTILLERY,
        SymbolEnum.INFANTRY, SymbolEnum.ARTILLERY, SymbolEnum.CAVALRY, SymbolEnum.INFANTRY,
        SymbolEnum.ARTILLERY, SymbolEnum.INFANTRY, SymbolEnum.CAVALRY, SymbolEnum.CAVALRY,
        SymbolEnum.CAVALRY, SymbolEnum.ARTILLERY, SymbolEnum.INFANTRY};
    int i = 0;
    while (it.hasNext()) {
      Country c = it.next();
      Card card = new Card(c, symbols[i], i + 1);
      cards.add(card);
      i++;
    }
    Card wildcard1 = new Card(null, null, 0);
    Card wildcard2 = new Card(null, null, 0);
    cards.add(wildcard1);
    cards.add(wildcard2);

    return cards;
  }

  public static void main(String[] args) {
    // Domain d = RiskMain.getInstance().getDomain();
    // d.setPlayerName("Peter");
    // System.out.println(d.getPlayerName());
    // // WriteJSON writer = new WriteJSON();
    // // ReadJSON reader = new ReadJSON();
    // // GameController game = new GameController();
    // //
    // // game.startGame(, usernames);
    // // game.createPlayer("muh1", 1);
    // // game.createPlayer("muh2", 2);
    // // game.createPlayer("muh3", 3);
    // // game.createPlayer("muh4", 4);
    // // game.match.establishOrder();
    // // File jsonGameState = game.jsonWriter.writeGetGameStateJson(game.match.getCountriesName(),
    // // game.match.getCountriesIds(), game.match.getCountriesOwners(),
    // // game.match.getCountriesTroops(), game.match.getCountriesNeighbors(), game.match.getName(),
    // // game.match.getMatchId(), game.match.getPlayersId(), game.match.getPlayerAi(),
    // // game.match.getPlayerNames(), game.match.getPlayersUnitsToPlace(),
    // // game.match.getPlayerCards(), game.match.getCurrentPlayerId(),
    // // game.match.getCurrentTurnPhase(), game.match.getCurrentGamePhase());
    // // reader.readRiskGetGameStateJSON(jsonGameState);
    // //
    // // int[][] test = reader.getGameStateNeighbours();
    // // int count = 0;
    // // for (int i = 0; i < test.length; i++) {
    // //
    // // for (int j = 0; j < test[i].length; j++) {
    // // System.out.println(test[i][j]);
    // // count++;
    // // }
    // // }
    // // System.out.println(count);
    // File f = new File("src/main/resources/JSON/attack.json");
    // Message m = new MessageError("attack");
    // test(f);
    GameController game = new GameController();
    int[] ids = {1, 2, 3};
    String[] s = {"Leon", "Em", "Simon"};
    game.startGame(ids, s);
  }

  public static void test(Object o) {
    // System.out.println(o.getClass().getSimpleName());
    // Message m = (Message) o;
    // System.out.println(m.getType());
    // MessageError me = (MessageError) m;
    // System.out.println(me.getErrorType());
  }
}
