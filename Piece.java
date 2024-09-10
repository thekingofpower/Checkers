import java.util.ArrayList;

//A class for a Piece on a checkerboard
public class Piece {

    //The Position of the Piece
    private Position piecePosition;

    //Whether or not the Piece has been captured
    private boolean alive;

    //The color of the Piece (black or red)
    private boolean black;

    //The text symbol used to represent the Piece
    private char symbol;

    /**
     * Creates a new Piece for a game of checkers
     * @param letter The letter of the Position of the Piece
     * @param number The number of the Position of the Piece
     * @param black Whether or not the Piece is on the black team
     */
    public Piece(char letter, int number, boolean black){
      
        piecePosition = new Position(letter, number);
        
        alive = true;
        
        this.black = black;

        symbol = black ? 'b' : 'r';
    }

    //Getter methods

    public Position getPosition(){
        return piecePosition;
    }

    public boolean getAlive(){
        return alive;
    }

    public boolean getBlack(){
        return black;
    }

    public char getSymbol(){
        return symbol;
    }

    //Setter methods
   
    public void setSymbol(char newSymbol){
        this.symbol = newSymbol;
    }

    public void setAlive(boolean newAlive){
        this.alive = newAlive;
    }

    /**
     * Gets all of the capturing moves for a Piece
     * @param samePieces The Pieces on the same team as the moving Piece
     * @param oppositePieces The Pieces on the opposite team as the moving Piece
     * @return A list of the possible capturing moves for the Piece
     */
    public ArrayList<Position> getCaptureMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces){
        
        ArrayList<Position> possibleMoves = new ArrayList<Position>();
        
        int pieceX = this.getPosition().getX();
        int pieceY = this.getPosition().getY();

        //The direction the Piece moves in depends on the team it is on
        int yMod = this.black ? -1 : 1;
                        
        //Check if the positions diagonal to the moving Piece's position are occupied by an opposing Piece, and if the diagonal positions after that are free
        if (new Position(pieceX + 1, pieceY + yMod).isOccupied(oppositePieces) && this.canMakeMove(new Position(pieceX + 2, pieceY + 2 * yMod), samePieces, oppositePieces)){
            possibleMoves.add(new Position(pieceX + 2, pieceY + 2 * yMod));
        }

        if (new Position(pieceX - 1, pieceY + yMod).isOccupied(oppositePieces) && this.canMakeMove(new Position(pieceX - 2, pieceY + 2 * yMod), samePieces, oppositePieces)){
            possibleMoves.add(new Position(pieceX - 2, pieceY + 2 * yMod));
        }
    
        return possibleMoves;             
    }

    /**
     * Gets all the non-capturing moves for a Piece
     * @param samePieces The Pieces on the same team as the moving Piece
     * @param oppositePieces The Pieces on the opposite team as the moving Piece
     * @return A list of the possible non-capturing moves for the Piece
     */
    public ArrayList<Position> getNormalMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces){
        
        ArrayList<Position> possibleMoves = new ArrayList<Position>();
        
        int pieceX = this.getPosition().getX();
        int pieceY = this.getPosition().getY();

        //The direction the Piece moves in depends on the team it is on
        int yMod = this.black ? -1 : 1;
                
        //Check if the spaces diagonal to the Piece are unoccupied
        if (this.canMakeMove(new Position(pieceX + 1, pieceY + yMod), samePieces, oppositePieces)){
            possibleMoves.add(new Position(pieceX + 1, pieceY + yMod));
        }

        if (this.canMakeMove(new Position(pieceX - 1, pieceY + yMod), samePieces, oppositePieces)){
            possibleMoves.add(new Position(pieceX - 1, pieceY + yMod));
        }
    
        return possibleMoves;             
    }

    /**
     * Checks if a Piece can capture one of the opposing Pieces
     * @param samePieces The Pieces on the same team as the moving Piece
     * @param oppositePieces The Pieces on the opposite team as the moving Piece
     * @return true if the Piece can capture and false otherwise
     */
    public boolean canCapture(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces){
        int pieceX = this.getPosition().getX();
        int pieceY = this.getPosition().getY();

        int yMod = this.black ? -1 : 1;                
        
        if (new Position(pieceX + 1, pieceY + yMod).isOccupied(oppositePieces) && this.canMakeMove(new Position(pieceX + 2, pieceY + 2 * yMod), samePieces, oppositePieces)){
            return true;
        }
        
        if (new Position(pieceX - 1, pieceY + yMod).isOccupied(oppositePieces) && this.canMakeMove(new Position(pieceX - 2, pieceY + 2 * yMod), samePieces, oppositePieces)){
            return true;
        }
    
        return false; 
    }

    /**
     * Checks if a Piece has any legal non-capturing moves
     * @param samePieces The Pieces on the same team as the moving Piece
     * @param oppositePieces The Pieces on the opposite team as the moving Piece
     * @return true if the Piece has any non-capturing moves and false otherwise
     */
    public boolean hasNormalMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces){
        
        int pieceX = this.getPosition().getX();
        int pieceY = this.getPosition().getY();

        int yMod = this.black ? -1 : 1;
                
        if (this.canMakeMove(new Position(pieceX + 1, pieceY + yMod), samePieces, oppositePieces)){
            return true;
        }
        
        if (this.canMakeMove(new Position(pieceX - 1, pieceY + yMod), samePieces, oppositePieces)){
            return true;
        }
        
        return false; 
    }                
  
    /**
     * Kings a Piece
     * @param samePieces The Pieces on the same team as the Piece being made into a King
     */
    void king(ArrayList<Piece> samePieces){

        Piece promoter = this;

        //Remove the Piece and replace it with a King with the same Position as the Piece
        samePieces.remove(this);
        samePieces.add(new King(promoter.getPosition().getLetter(), promoter.getPosition().getNumber(), this.black));
        System.out.println("King!");
    }   
    
    /**
     * Moves a Piece to a new Position
     * @param move the new Position of the move
     * @param samePieces the Pieces on the same team as the moving Piece
     * @param oppositePieces the Pieces on the opposite team as the moving Piece
     * @param capture whether or not the move will capture a Piece
     */
    void move(Position move, ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, boolean capture){
        
        //If the move is a capturing move, capture the Piece between the moving Piece's current Position and its Position after the move
        if (capture){
            Piece capturedPiece = new Position(((this.getPosition().getX() + move.getX()) / 2), ((this.getPosition().getY() + move.getY()) / 2)).getPieceOnSpace(oppositePieces);

            capturedPiece.alive = false;

            oppositePieces.remove(capturedPiece);

        }
        
        this.piecePosition.setPosition(move);

        //If a Piece reaches the edge of the board, King it
        if ((black && this.piecePosition.getNumber() == 8) || (!black && this.piecePosition.getNumber() == 1)){
            this.king(samePieces);
        }
    }

    /**
     * Checks if a Piece can make a specific move
     * @param newPosition the Position of the move
     * @param samePieces The Pieces on the same team as the moving Piece
     * @param oppositePieces The Pieces on the opposite team as the moving Piece
     * @return true if the Piece can make the move and false otherwise
     */
    public boolean canMakeMove(Position newPosition, ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces){

        //The move must be inbounds and not currently occupied by any pieces
        if (!newPosition.isInBounds() || newPosition.isOccupied(samePieces) || newPosition.isOccupied(oppositePieces)){
            return false;
        }
        else{
            return true;
        }
    }
}
