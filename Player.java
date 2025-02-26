public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the game
    }

    /*
     * TODO: removes and returns the tile in given index
     */
    public Tile getAndRemoveTile(int index) {
        Tile removedTile = playerTiles[index];
        Tile[] newPlayerTiles = new Tile[numberOfTiles - 1];
        for (int i = 0; i < numberOfTiles; i++) {
            if (i < index) {
                newPlayerTiles[i] = playerTiles[i];
            } 
            else if (i > index) {
                newPlayerTiles[i - 1] = playerTiles[i];
            }
        }
        playerTiles = newPlayerTiles;
        numberOfTiles--;
        return removedTile;
    }

    /*
     * TODO: adds the given tile to the playerTiles in order
     * should also update numberOfTiles accordingly.
     * make sure playerTiles are not more than 15 at any time
     */
    // You should make the add tile method of the player to insert new tiles in order so that each player’s hand is always sorted without using a sorting algorithm.
    public void addTile(Tile t) {
        throw new UnsupportedOperationException("Task assigned to: esra");
    }

    /*
     * TODO: checks if this player's hand satisfies the winning condition
     * to win this player should have 3 chains of length 4, extra tiles
     * does not disturb the winning condition
     * @return
     */
    public boolean isWinningHand() {
        int winingCondition = 0;
        int count = 0;
        for(int i = 0; i + 1 < this.numberOfTiles; i++){
            if(this.playerTiles[i].getValue() == this.playerTiles[i+1].getValue()){
                if(this.playerTiles[i].getColor() != this.playerTiles[i+1].getColor()){
                    count++;
                }
            }
            else{
                if(count == 3){
                    winingCondition++;
                }
                count = 0;
            }
        }
        
        return winingCondition == 3;
    }

    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if(playerTiles[i].compareTo(t) == 0) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
