import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

//A class for a game of checkers
public class Game {
    
    //Used for user input
    static Scanner sc = new Scanner(System.in);

    //Used for bot input
    static Random rand = new Random();

    //The checkerboard
    static Board gameBoard = new Board();

    //The number of moves made without any captures
    static int numMovesWithNoCaptures = 0;

    //All board layouts that have occurred once throughout the game
    static ArrayList<String> layoutsOnce = new ArrayList<String>();

    //All board layouts that have occurred twice throughout the game
    static ArrayList<String> layoutsTwice = new ArrayList<String>();

    //chcp 65001
    public static void main(String[] args){

        //Whether or not the game has ended
        boolean gameActive = true;

        //Draw the initial board before the game starts
        gameBoard.drawBoard();

        while (gameActive){

            //Black moves first
            boolean didBlackCapture = blackTurn();

            gameBoard.drawBoard();

            //If an end condition has been reached, end the game
            if (gameOver(true, didBlackCapture)){
                gameActive = false;
                break;
            }

            System.out.println("Bot's Turn...");

            //Wait for a second before the bot moves
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Red moves second
            boolean didRedCapture = redTurn();

            gameBoard.drawBoard();

            //If an end condition has been reached, end the game
            if (gameOver(false, didRedCapture)){
                gameActive = false;
            }
        }
    }

    /**
     * Executes the black team's turn
     * @return true if the black team captures an opposing Piece and false otherwise
     */
    public static boolean blackTurn(){
        
        //Assume the player does not capture
        boolean capture = false;

        //If it is found that the black team is able to capture, set capture to true
        for (Piece blackPiece: gameBoard.blackPieces){
            if (blackPiece.canCapture(gameBoard.blackPieces, gameBoard.redPieces)){
                capture = true;
                break;
            }
        }

        //Ask the player what Piece they want to move. If at least one Piece can capture, they have to choose a Piece that can capture
        Piece blackMover = selectMoverPlayer(capture);

        //If the Piece can capture, it has to use a capturing move
        ArrayList<Position> blackMoves = capture ? blackMover.getCaptureMoves(gameBoard.blackPieces, gameBoard.redPieces) : blackMover.getNormalMoves(gameBoard.blackPieces, gameBoard.redPieces);

        //Show the player a preview of the moves they can make
        gameBoard.drawBoardWithMoves(blackMoves);

        //Get the player to pick a move
        Position blackMove = askForMove(blackMoves);

        //Make at least one move, but potentially more
        do{
            blackMover.move(blackMove, gameBoard.blackPieces, gameBoard.redPieces, capture);  

            //If the move captured a Piece and the Piece's new position allows it to capture another Piece, move again
            if (capture){
                blackMoves = blackMover.getCaptureMoves(gameBoard.blackPieces, gameBoard.redPieces);

                if (blackMoves.size() > 0){
                    gameBoard.drawBoardWithMoves(blackMoves);   

                    blackMove = askForMove(blackMoves);
                }
            }
            
        }
        while (blackMoves.size() > 0 && capture);

        //Return whether or not the black team captured
        return capture;
    }

    /**
     * Executes the red team's turn
     * @return true if the red team captures an opposing Piece and false otherwise
     */
    public static boolean redTurn(){
        
        //Assume the red team does not capture
        boolean capture = false;

        //If any of the red pieces can capture an opposing Piece, set capture to true
        for (Piece redPiece: gameBoard.redPieces){
            if (redPiece.canCapture(gameBoard.redPieces, gameBoard.blackPieces)){
                capture = true;
                break;
            }
        }

        Piece redMover = selectMoverBot(capture);

        //If a red Piece can capture, it has to use a capturing move
        ArrayList<Position> redMoves = capture ? redMover.getCaptureMoves(gameBoard.redPieces, gameBoard.blackPieces) : redMover.getNormalMoves(gameBoard.redPieces, gameBoard.blackPieces);

        //Randomly select the red move out of all of the Piece's legal moves
        Position redMove = redMoves.get(rand.nextInt(redMoves.size()));

        do{
            redMover.move(redMove, gameBoard.redPieces, gameBoard.blackPieces, capture);  
            
            if (capture){
                redMoves = redMover.getCaptureMoves(gameBoard.redPieces, gameBoard.blackPieces);

                //If the move captured a Piece and now the moving Piece can capture another Piece, pause then move again
                if (redMoves.size() > 0){

                    gameBoard.drawBoard();  
                    
                    System.out.println("Bot moving again...");

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
    
                    redMove = redMoves.get(rand.nextInt(redMoves.size()));
                }
            }
        }
        while (redMoves.size() > 0 && capture);

        //Return whether or not the red team captured
        return capture;
    }

    /**
     * Gets the player to select a Piece to make a move with
     * @param capture whether or not the moving Piece has to be able to capture an opposing Piece
     * @return the Piece the user selected (which must have a legal move)
     */
    public static Piece selectMoverPlayer(boolean capture){

        //Notify the player if they have to capture
        if (capture){
            System.out.println("You have to capture!");
        }
        
        System.out.println("What's the position of the piece you want to move?");
        
        String answer = sc.nextLine();

        Position moverPosition = new Position(Character.toUpperCase(answer.charAt(0)), Character.getNumericValue(answer.charAt(1)));

        //Go through all of the black Pieces and check if they have the same position that the player entered
        for (int i = 0; i < gameBoard.blackPieces.size(); i++){
            if (gameBoard.blackPieces.get(i).getPosition().samePosition(moverPosition)){
                //If the player can capture, check if the Piece they selected can capture. Otherwise, check if they have non-capturing moves.
                if (capture){
                    if (gameBoard.blackPieces.get(i).canCapture(gameBoard.blackPieces, gameBoard.redPieces)){
                        return gameBoard.blackPieces.get(i);
                    }
                    else{
                        System.out.println("You have to select a Piece that can capture. Try again.");   
                        return selectMoverPlayer(true);
                    }
                }
                else{
                    if (gameBoard.blackPieces.get(i).hasNormalMoves(gameBoard.blackPieces, gameBoard.redPieces)){
                        return gameBoard.blackPieces.get(i);
                    }
                    else{
                        System.out.println("That piece doesn't have any legal moves. Try again.");   
                        return selectMoverPlayer(false);
                    }
                }
            }   
        }
        
        //If no matches were found, tell the player to try again
        System.out.println("That's not one of your pieces.");
        return selectMoverPlayer(capture);
    }

    /**
     * Gets the bot to select a Piece to make a move with
     * @param capture whether or not the bot is required to capture
     * @return the Piece the bot selected
     */
    public static Piece selectMoverBot(boolean capture){
        
        ArrayList<Piece> potentialMovers = new ArrayList<Piece>();

        //If the bot has to capture, only select Pieces that can capture. Otherwise, select Pieces with non-capturing moves.
        for (int i = 0; i < gameBoard.redPieces.size(); i++){
            if (capture){
                if (gameBoard.redPieces.get(i).canCapture(gameBoard.redPieces, gameBoard.blackPieces)){
                    potentialMovers.add(gameBoard.redPieces.get(i));
                }
            }
            else{
                if (gameBoard.redPieces.get(i).hasNormalMoves(gameBoard.redPieces, gameBoard.blackPieces)){
                    potentialMovers.add(gameBoard.redPieces.get(i));
                }
            }
        }

        return potentialMovers.get(rand.nextInt(potentialMovers.size()));
    }

    /**
     * Asks the player for the move they want to make for the Piece they previously selected
     * @param possibleMoves
     * @return
     */
    public static Position askForMove(ArrayList<Position> possibleMoves){

        System.out.print("What move do you want to make? ");

        String answer = sc.nextLine();

        char letter = Character.toUpperCase(answer.charAt(0));

        int number = Character.getNumericValue(answer.charAt(1));

        Position selectedMove = new Position(letter, number);

        //Check if the position they selected is in the list of possible moves
        for (int i = 0; i < possibleMoves.size(); i++){
            if (selectedMove.samePosition(possibleMoves.get(i))){
                return new Position(letter, number);
            }
        } 
        
        System.out.println("That move is invalid. Try again.");
        return askForMove(possibleMoves);
    }

    /**
     * Checks if the game should end
     * @param blackJustMoved whether or not the black team just moved
     * @param captured whether or not the previous move was a capture
     * @return true if the game should end and false otherwise
     */
    public static boolean gameOver(boolean blackJustMoved, boolean captured){

        //Check if the team that just moved defeated all the opposing pieces or if it put the opposite team into a position where it has no moves
        if (blackJustMoved){
            if (gameBoard.redPieces.size() == 0){
                System.out.println("Black defeated all opposing Pieces! Black wins!");
                return true;
            }
            else if (!teamHasMoves(gameBoard.redPieces, gameBoard.blackPieces)){
                System.out.println("Red has no moves! Black wins!");
                return true;
            }
        }
        else{
            if (gameBoard.blackPieces.size() == 0){
                System.out.println("Red defeated all opposing Pieces! Red wins!");
                return true;
            }
            else if (!teamHasMoves(gameBoard.blackPieces, gameBoard.redPieces)){
                System.out.println("Black has no moves! Red wins!");
                return true;
            }
        }

        //If 40 moves have gone by with no captures, the game ends ina draw
        if (check40Moves(captured)){
            System.out.println("50 turns with no captures! Draw!");
            return true;
        }
        
        //If the same board layout occurs 3 times in a game, end the game in a draw
        if (checkPreviousLayouts(gameBoard.toString())){
            System.out.println("Same position 3 times! Draw!");
            return true;
        }

        //If none of these conditiosn are met, don't end the game
        return false;
    }

    /**
     * Checks if 40 moves have gone by without any captures
     * @param captured whether or not the previous move was a capture
     * @return true if 40 moves have gone by with no captures and false otherwise
     */
    public static boolean check40Moves(boolean captured){

        //If the previous move was a capture, reset the counter and clear all previous layouts
        if (captured){
            
            numMovesWithNoCaptures = 0;

            clearPreviousPositions();
        }
        else{

            numMovesWithNoCaptures++;

            if (numMovesWithNoCaptures >= 40){
                return true;
            }
        }

        return false;
    }
    
    /**
     * Checks if the same board layout has occurred 3 times within a single game
     * @param layout the layout from the previous move
     * @return true if the same layout has occurred 3 times and false otherwise
     */
    public static boolean checkPreviousLayouts(String layout){

        //If the previous layout has occurred twice already, end the game
        for (int i = 0; i < layoutsTwice.size(); i++){
            if (layout.equals(layoutsTwice.get(i))){
                return true;
            }
        }

        //If the previous layout has occurred once already, add it to the list of layouts that have occurred twice
        for (int i = 0; i < layoutsOnce.size(); i++){
            if (layout.equals(layoutsOnce.get(i))){
                layoutsTwice.add(layoutsOnce.get(i));
                layoutsOnce.remove(i);
                return false;
            }
        }

        //If the previous layout has not appeared yet, add it to the list of layouts that have occurred once
        layoutsOnce.add(layout);
        return false;
    }

    /**
     * Clears all previously occurring positions from the records
     */
    public static void clearPreviousPositions(){
        layoutsOnce.clear();
        layoutsTwice.clear();
    }

    /**
     * Checks if a team has any legal moves for any of its Pieces
     * @param samePieces The team's Pieces
     * @param oppositePieces The opposing team's Pieces
     * @return true if the team has moves and false otherwise
     */
    public static boolean teamHasMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces){
        for (Piece piece: samePieces){
            if (piece.hasNormalMoves(samePieces, oppositePieces) || piece.canCapture(samePieces, oppositePieces)){
                return true;
            }
        }
        return false;
    }
}