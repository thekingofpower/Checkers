import java.util.ArrayList;

//A class for a King on a checkerboard
public class King extends Piece{

    public King(char letter, int number, boolean black){
      
        super(letter, number, black);

        this.setSymbol(black ? 'B' : 'R');
    }

    @Override
    public ArrayList<Position> getCaptureMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces){
        
        ArrayList<Position> possibleMoves = new ArrayList<Position>();
        
        int pieceX = this.getPosition().getX();
        int pieceY = this.getPosition().getY();

        //A King can move forward or backward
                        
        if (new Position(pieceX + 1, pieceY + 1).isOccupied(oppositePieces) && this.canMakeMove(new Position(pieceX + 2, pieceY + 2), samePieces, oppositePieces)){
            possibleMoves.add(new Position(pieceX + 2, pieceY + 2));
        }

        if (new Position(pieceX - 1, pieceY + 1).isOccupied(oppositePieces) && this.canMakeMove(new Position(pieceX - 2, pieceY + 2), samePieces, oppositePieces)){
            possibleMoves.add(new Position(pieceX - 2, pieceY + 2));
        }

        if (new Position(pieceX + 1, pieceY - 1).isOccupied(oppositePieces) && this.canMakeMove(new Position(pieceX + 2, pieceY - 2), samePieces, oppositePieces)){
            possibleMoves.add(new Position(pieceX + 2, pieceY - 2));
        }

        if (new Position(pieceX - 1, pieceY - 1).isOccupied(oppositePieces) && this.canMakeMove(new Position(pieceX - 2, pieceY - 2), samePieces, oppositePieces)){
            possibleMoves.add(new Position(pieceX - 2, pieceY - 2));
        }
    
        return possibleMoves;             
    }

    @Override
    public ArrayList<Position> getNormalMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces){
        
        ArrayList<Position> possibleMoves = new ArrayList<Position>();
        
        int pieceX = this.getPosition().getX();
        int pieceY = this.getPosition().getY();
                
        if (this.canMakeMove(new Position(pieceX + 1, pieceY + 1), samePieces, oppositePieces)){
            possibleMoves.add(new Position(pieceX + 1, pieceY + 1));
        }

        if (this.canMakeMove(new Position(pieceX - 1, pieceY + 1), samePieces, oppositePieces)){
            possibleMoves.add(new Position(pieceX - 1, pieceY + 1));
        }

        if (this.canMakeMove(new Position(pieceX + 1, pieceY - 1), samePieces, oppositePieces)){
            possibleMoves.add(new Position(pieceX + 1, pieceY - 1));
        }

        if (this.canMakeMove(new Position(pieceX - 1, pieceY - 1), samePieces, oppositePieces)){
            possibleMoves.add(new Position(pieceX - 1, pieceY - 1));
        }
    
        return possibleMoves;             
    }

    @Override
    public boolean canCapture(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces){
        int pieceX = this.getPosition().getX();
        int pieceY = this.getPosition().getY();
                        
        if (new Position(pieceX + 1, pieceY + 1).isOccupied(oppositePieces) && this.canMakeMove(new Position(pieceX + 2, pieceY + 2), samePieces, oppositePieces)){
            return true;
        }

        if (new Position(pieceX - 1, pieceY + 1).isOccupied(oppositePieces) && this.canMakeMove(new Position(pieceX - 2, pieceY + 2), samePieces, oppositePieces)){
            return true;
        }

        if (new Position(pieceX + 1, pieceY - 1).isOccupied(oppositePieces) && this.canMakeMove(new Position(pieceX + 2, pieceY - 2), samePieces, oppositePieces)){
            return true;
        }

        if (new Position(pieceX - 1, pieceY - 1).isOccupied(oppositePieces) && this.canMakeMove(new Position(pieceX - 2, pieceY - 2), samePieces, oppositePieces)){
            return true;
        }
    
        return false;
    }

    @Override
    public boolean hasNormalMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces){
        
        int pieceX = this.getPosition().getX();
        int pieceY = this.getPosition().getY();
                
        if (this.canMakeMove(new Position(pieceX + 1, pieceY + 1), samePieces, oppositePieces)){
            return true;
        }

        if (this.canMakeMove(new Position(pieceX - 1, pieceY + 1), samePieces, oppositePieces)){
            return true;
        }

        if (this.canMakeMove(new Position(pieceX + 1, pieceY - 1), samePieces, oppositePieces)){
            return true;
        }

        if (this.canMakeMove(new Position(pieceX - 1, pieceY - 1), samePieces, oppositePieces)){
            return true;
        }
    
        return false;
    }                
    
    @Override
    void move(Position move, ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, boolean capture){
        if (capture){
            Piece capturedPiece = new Position(((this.getPosition().getX() + move.getX()) / 2), ((this.getPosition().getY() + move.getY()) / 2)).getPieceOnSpace(oppositePieces);

            capturedPiece.setAlive(false);

            oppositePieces.remove(capturedPiece);

        }
        
        this.getPosition().setPosition(move);
    }



 
    
}
