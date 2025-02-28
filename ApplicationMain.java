import java.util.Scanner;

public class ApplicationMain {

    public static void main(String[] args) {

        boolean isRealPlayer = true; // to silence compiler
        Scanner sc = new Scanner(System.in);
        OkeyGame game = new OkeyGame();

        System.out.print("Do you want to play game? Otherwise, only computers will play.\n1. Yes \n2. No \nChoice: ");
        int choice = sc.nextInt(); sc.nextLine();
        switch(choice){
            case 1:
            isRealPlayer = true;
                break;
            case 2:
            isRealPlayer = false;
                break;
        }

        String playerName;

        if(isRealPlayer){
            System.out.print("Please enter your name: ");
            playerName = sc.next();
        }
        else{
            playerName = "Jack";
        }


        game.setPlayerName(0, playerName);
        game.setPlayerName(1, "John");
        game.setPlayerName(2, "Jane");
        game.setPlayerName(3, "Ted");

        game.createTiles();
        game.shuffleTiles();
        game.distributeTilesToPlayers();

        // developer mode is used for seeing the computer players hands, to be used for debugging
        boolean devModeOn;
        if(isRealPlayer){
            System.out.print("Play in developer's mode with other player's tiles visible? (Y/N): ");
            char devMode = sc.next().charAt(0);
            devModeOn = devMode == 'Y';
        }
        else{
            devModeOn = true;
        }
        
        boolean firstTurn = true;
        boolean gameContinues = true;
        int playerChoice = -1;

        while(gameContinues) {
            
            int currentPlayer = game.getCurrentPlayerIndex();
            System.out.println(game.getCurrentPlayerName() + "'s turn.");
            
            if(isRealPlayer && currentPlayer == 0) {
                // this is the human player's turn
                game.displayCurrentPlayersTiles();
                game.displayDiscardInformation();

                System.out.println("What will you do?");

                if(!firstTurn) {
                    // after the first turn, player may pick from tile stack or last player's discard
                    System.out.println("1. Pick From Tiles");
                    System.out.println("2. Pick From Discard");
                }
                else{
                    // on first turn the starting player does not pick up new tile
                    System.out.println("1. Discard Tile");
                }

                System.out.print("Your choice: ");
                playerChoice = sc.nextInt();

                // after the first turn we can pick up
                if(!firstTurn) {
                    if(playerChoice == 1) {
                        System.out.println("You picked up: " + game.getTopTile());
                        firstTurn = false;
                    }
                    else if(playerChoice == 2) {
                        System.out.println("You picked up: " + game.getLastDiscardedTile()); 
                    }

                    // display the hand after picking up new tile
                    game.displayCurrentPlayersTiles();
                }
                else{
                    // after first turn it is no longer the first turn
                    firstTurn = false;
                }

                gameContinues = !game.didGameFinish();

                if(gameContinues) {
                    // if game continues we need to discard a tile using the given index by the player
                    System.out.println("Which tile you will discard?");
                    System.out.print("Discard the tile in index: ");
                    playerChoice = sc.nextInt();

                    // TODO: make sure the given index is correct, should be 0 <= index <= 14

                    while(playerChoice<0 ||playerChoice>14){
                        System.out.print("The given index is not valid, enter a different index: ");
                        playerChoice = sc.nextInt();
                    }
                    
                    game.discardTile(playerChoice);
                    game.passTurnToNextPlayer();
                }
                else{
                    // if we finish the hand we win
                    System.out.println("Congratulations, you win!");
                }
            }
            else{
                // this is the computer player's turn
                if(devModeOn) {
                    game.displayCurrentPlayersTiles();
                }

                if(!firstTurn){
                    // computer picks a tile from tile stack or other player's discard
                    game.pickTileForComputer();
                }
                else{
                    firstTurn = false;
                }

                gameContinues = !game.didGameFinish();

                if(gameContinues) {
                    // if game did not end computer should discard
                    game.discardTileForComputer();
                    game.passTurnToNextPlayer();
                }
                else{
                    // current computer character wins
                    System.out.println(game.getCurrentPlayerName() + " wins.");
                }
            }
        }
    }
}