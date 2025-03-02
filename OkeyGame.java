import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class OkeyGame {

    Player[] players;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[112];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 7; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i,'Y');
                tiles[currentTile++] = new Tile(i,'B');
                tiles[currentTile++] = new Tile(i,'R');
                tiles[currentTile++] = new Tile(i,'K');
            }
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already shuffled
     */
    public void distributeTilesToPlayers() {
        for(int j=0; j<14; j++)
        {
            for(int i =0; i <4 ; i++)
            {
                players[i].addTile(tiles[tiles.length -1]);
                tiles = Arrays.copyOf(tiles, tiles.length - 1);
            }
        }
        players[0].addTile(tiles[tiles.length-1]);
        tiles = Arrays.copyOf(tiles, tiles.length - 1);
    }

    /* 
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() {
        players[currentPlayerIndex].addTile(lastDiscardedTile);
        return lastDiscardedTile.toString();
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {
        Tile topTile = tiles[tiles.length - 1];
        players[currentPlayerIndex].addTile(topTile);
        tiles = Arrays.copyOf(tiles, tiles.length - 1);
        return topTile.toString();
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    public void shuffleTiles() {
        Random random = new Random();
        for (int i = 111; i > 0; i--) {  
            int index = random.nextInt(i + 1); 
            Tile temp = tiles[i]; 
            tiles[i] = tiles[index];
            tiles[index] = temp;
        }
    }
    

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game, use isWinningHand() method of Player to decide
     */
    public boolean didGameFinish() {
        return this.players[getCurrentPlayerIndex()].isWinningHand();
    }

    /*
     * TODO: Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You should consider if the discarded tile is useful for the computer in
     * the current status. Print whether computer picks from tiles or discarded ones.
     */
    public void pickTileForComputer() {
        int triosBeforeTheNewTile = this.players[currentPlayerIndex].findTheNumberOfTrios();
        this.players[currentPlayerIndex].addTile(lastDiscardedTile);
        int triosAfterTheNewTile = this.players[currentPlayerIndex].findTheNumberOfTrios();
        if(triosBeforeTheNewTile >= triosAfterTheNewTile)
        {
            int indexToRemove= this.players[currentPlayerIndex].findPositionOfTile(lastDiscardedTile);
            this.players[currentPlayerIndex].getAndRemoveTile(indexToRemove);
            getTopTile();
            System.out.println("A tile is picked from tiles");
        }
        else
        {
            System.out.println("Last discarded tile is picked.");
        }
    }

    /* 
     * TODO: Current computer player will discard the least useful tile.
     * this method should print what tile is discarded since it should be
     * known by other players. You may first discard duplicates and then
     * the single tiles and tiles that contribute to the smallest chains.
     */
    public void discardTileForComputer() {
        int discardedTileIndex = -1;
        Tile discardedTile = null;
        Tile[] playerTiles = players[currentPlayerIndex].getTiles();
        outerLoop:
        for(int i = 0; i<players[currentPlayerIndex].getTiles().length; i++){
            for(int j = 0; j<players[currentPlayerIndex].getTiles().length; j++){
                if(i != j && players[currentPlayerIndex].getTiles()[i].compareTo(players[currentPlayerIndex].getTiles()[j]) == 0){
                    discardedTileIndex = j;
                    break outerLoop;
                }
            }
        }

        if(discardedTileIndex == -1){
            if(!(playerTiles[0].canFormChainWith(playerTiles[1]))){
                discardedTileIndex = 0;
                break;
            }
            else if(!(playerTiles[playerTiles.length-1].canFormChainWith(playerTiles[playerTiles.length-2]))){
                discardedTileIndex = playerTiles.length-1;
                break;
            }
            else{
                for(int i = 1; i<playerTiles.length; i++){
                    if(!(playerTiles[i].canFormChainWith(playerTiles[i-1])&& playerTiles[i].canFormChainWith(playerTiles[i+1]))){
                        discardedTileIndex = i;
                    }
                }
            }
        }

        if(discardedTileIndex == -1){
            int minChainLength = 15;
            int chainLength = 0;
            for(int i = 0; i<playerTiles.length-1; i++){
                if(playerTiles[i].canFormChainWith(playerTiles[i+1])){
                    chainLength++;
                }
                else{
                    if(chainLength < minChainLength){
                        minChainLength = chainLength;
                        discardedTileIndex = i;
                        chainLength = 0;
                    }
                }
            }
        }

        discardedTile = players[currentPlayerIndex].getAndRemoveTile(discardedTileIndex);
        lastDiscardedTile = discardedTile;
        displayDiscardInformation();
        
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        Tile discardedTile = players[currentPlayerIndex].getAndRemoveTile(tileIndex);
        lastDiscardedTile = discardedTile;
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
