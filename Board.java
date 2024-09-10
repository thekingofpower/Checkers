import java.util.ArrayList;

//A class representing a checkerboard
public class Board{
    
    //The board's layout (including Pieces as well as blank spaces)
    char[][] layout = new char[8][8];

    //Contains all active black pieces
    ArrayList<Piece> blackPieces = new ArrayList<>();

    //Contains all active red pieces
    ArrayList<Piece> redPieces = new ArrayList<>();

    /**
     * Creates a new blank Board, adds all the Pieces to the game, then adds all the Pieces to the Board
     */
    public Board(){

        //Sets all spaces to the right color
        this.resetBoard();

        //Add all pieces to the ArrayLists at the correct starting positions
        this.resetPieces();
        
        //Add all the pieces to the board
        this.addPieces();
    }

    /**
     * Clears all pieces off the board, generating only blank spaces
    */
    public void resetBoard(){
        
        //Goes through every position and sets it to be alternating black and red spaces
        for (int i = 0; i < layout.length; i++){
            for (int j = 0; j < layout[0].length; j++){
                layout[i][j] = i % 2 == j % 2 ? '▮' : '▯';
            }
        }
    }

    /**
     * Adds all Pieces to blackPieces and redPieces in their default starting positions
    */
    public void resetPieces(){

        blackPieces.clear();
        redPieces.clear();

        //These for loops will add all the pieces in the right places
        for (char i = 'A'; i <= 'G'; i += 2){
            blackPieces.add(new Piece(i, 1, true));
            blackPieces.add(new Piece(i, 3, true));
            redPieces.add(new Piece(i, 7, false));
        }

        for (char i = 'B'; i <= 'H'; i+=2){
            blackPieces.add(new Piece(i, 2, true));
            redPieces.add(new Piece(i, 6, false));
            redPieces.add(new Piece(i, 8, false));
        }
    }

    /**
     * Adds all pieces from blackPieces and redPieces to the layout
    */
    public void addPieces(){

        //Add the black pieces to the board
        for (int i = 0; i < blackPieces.size(); i++){
            layout[blackPieces.get(i).getPosition().getY()][blackPieces.get(i).getPosition().getX()] = blackPieces.get(i).getSymbol();
        }

        //Add the red pieces to the board
        for (int i = 0; i < redPieces.size(); i++){
            layout[redPieces.get(i).getPosition().getY()][redPieces.get(i).getPosition().getX()] = redPieces.get(i).getSymbol();
        }
    }

    /**
     * Marks the possible moves for a Piece on the Board's layout
     * @param moves The list of possible moves for the Piece
     */
    public void addMoves(ArrayList<Position> moves){

        //Any possible move will be marked with an 'O'
        for (int i = 0; i < moves.size(); i++){
            layout[moves.get(i).getY()][moves.get(i).getX()] = 'O';
        }
    }

    /**
     * Generates the Board's layout in text form
     * @return A string representing the Board's layout
     */
    @Override
    public String toString(){
        
        //Start with the letters at the top
        String result = " A B C D E F G H\n";

        //Go through every row in layout
        for (int i = 0; i < layout.length; i++){

            //Add the numbers to the left side
            result += (8 - i);

            //Add one full row to the String
            for (int j = 0; j < layout[0].length; j++){
                result += layout[i][j] + " ";
            }
            
            //Add the numbers to the right side
            result += (8 - i) + "\n";
        }

        //End with the letters at the bottom
        result += " A B C D E F G H          ";

        return result;
    }

    /**
     * Prints out the Board's layout in a format easy to understand
    */
    public void drawBoard(){

        //Clear all Pieces off the Board
        this.resetBoard();

        //Add the Pieces back to the board in their current positions
        this.addPieces();

        //Print the Board
        System.out.println(this.toString());
    }

    /**
     * Prints out the Board's layout and marks any possible moves for a Piece
     * @param possibleMoves The list of possible moves 
     */
    public void drawBoardWithMoves(ArrayList<Position> possibleMoves){

        //Clear all Pieces off the Board
        this.resetBoard();

        //Add the possible moves to the Board
        this.addMoves(possibleMoves);

        //Add the Pieces back to the Board in their current positions
        this.addPieces();

        //Print the Board
        System.out.println(this.toString());
    }
}
 
