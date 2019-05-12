package sw.praktikum.risk5.Ai;

import java.io.File;
import sw.praktikum.risk5.Json.ReadJson;
import sw.praktikum.risk5.Json.WriteJson;

class AiTest {

  public static void main(String[] args) {
    WriteJson writer = new WriteJson();
    ReadJson reader = new ReadJson();
    reader.readRiskGetGameStateJSON(new File("src/main/resources/JSON/risk_getGameState.json"));
    int[][] neigh = reader.getGameStateNeighbours();
    String[] names = new String[42];
    int[] countriesId = new int[42];
    int[] countriesOwner = new int[42];
    int[] countriesUnits = new int[42];
    int[] pIds = {1, 2};
    int[] playerTroops = {40, 40};
    int[][] playerCards = {{1, 3, 0, 0, 0}, {0, 0, 0, 0, 0}};
    String[] pNames = {"a", "b"};
    boolean[] pAi = {true, true};
    for (int i = 0; i < 42; i++) {
      names[i] = "a";
      countriesId[i] = i + 1;
      countriesOwner[i] = 0;
      countriesUnits[i] = 0;
    }

    int currentPlayer = 1;
    int currentTurnPhase = 0;
    int currentGamePhase = 0;
    AiMain ai = new AiMain(AiType.EASY, 1, null, "");
    AiMain ai2 = new AiMain(AiType.EASY, 2, null, "");

    System.out.println("start");
    System.out.println();
    
    //Choosing countries
//    for (int i = 0; i < 21; i++) {
//      File result = ai.performAction(writer.writeGetGameStateJson(names, countriesId,
//          countriesOwner, countriesUnits, neigh, "", 0, pIds, pAi, pNames, playerTroops,
//          playerCards, currentPlayer, currentTurnPhase, currentGamePhase));
//      reader.readPlaceJSON(result);
//
//      countriesUnits[reader.getPlaceCountry() - 1] += reader.getPlaceNumberOfUnits();
//      countriesOwner[reader.getPlaceCountry() - 1] = reader.getPlacePlayer();
//      System.out.println(reader.getPlaceCountry());
//      
//      currentPlayer = 2;
//      result = ai2.performAction(writer.writeGetGameStateJson(names, countriesId, countriesOwner,
//          countriesUnits, neigh, "", 0, pIds, pAi, pNames, playerTroops, playerCards, currentPlayer,
//          currentTurnPhase, currentGamePhase));
//      reader.readPlaceJSON(result);
//      
//      countriesUnits[reader.getPlaceCountry() - 1] += reader.getPlaceNumberOfUnits();
//      countriesOwner[reader.getPlaceCountry() - 1] = reader.getPlacePlayer();
//      currentPlayer = 1;
//      playerTroops[0] -= 1;
//      playerTroops[1] -= 1;
//      System.out.println(reader.getPlaceCountry());
//      System.out.println("performed: " + i);
    }
//    
//    //Troops Placement
//    currentTurnPhase = 0;
//    currentGamePhase = 1;
//    currentPlayer = 1;
//    while (playerTroops[0] != 0 && playerTroops[1] != 0) {
//      
//      File result = ai.performAction(writer.writeGetGameStateJson(names, countriesId,
//          countriesOwner, countriesUnits, neigh, "", 0, pIds, pAi, pNames, playerTroops,
//          playerCards, currentPlayer, currentTurnPhase, currentGamePhase));
//      reader.readPlaceJSON(result);
//
//      countriesUnits[reader.getPlaceCountry() - 1] += reader.getPlaceNumberOfUnits();
//      countriesOwner[reader.getPlaceCountry() - 1] = reader.getPlacePlayer();
//      playerTroops[0] -= reader.getPlaceNumberOfUnits();
//      
//      currentPlayer = 2;
//      result = ai2.performAction(writer.writeGetGameStateJson(names, countriesId, countriesOwner,
//          countriesUnits, neigh, "", 0, pIds, pAi, pNames, playerTroops, playerCards, currentPlayer,
//          currentTurnPhase, currentGamePhase));
//      reader.readPlaceJSON(result);
//      
//      countriesUnits[reader.getPlaceCountry() - 1] += reader.getPlaceNumberOfUnits();
//      countriesOwner[reader.getPlaceCountry() - 1] = reader.getPlacePlayer();
//      currentPlayer = 1;
//      playerTroops[1] -= reader.getPlaceNumberOfUnits();
//    }
//    
//    System.out.println();
//    System.out.println("After place Rest Troops");
//    for (int i = 0; i < 42; i++) {
//      System.out.println("ID: " + countriesId[i]);
//      System.out.println("Owner: " + countriesOwner[i]);
//      System.out.println("Units: " + countriesUnits[i]);
//    }
   
}
