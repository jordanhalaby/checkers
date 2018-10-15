package referee;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.*;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.rmi.*;
import java.net.*;
import java.util.Arrays;

/**
 * <p>
 * Title: Player</p>
 * <p>
 * Description: Testing representation of a checkers player</p>
 * <p>
 * Copyright: Copyright (c) 2004</p>
 * <p>
 * Company: </p>
 *
 * @author Matthew Evett
 * @version 1.0
 */
public class ArtificialPlayer extends java.rmi.server.UnicastRemoteObject implements referee.Player {

    private static String name;
    public static final int NO_PIECE = 0;
    public static final int WHITE_PAWN = 1000;
    public static final int WHITE_KING = 2000;
    public static final int BLACK_PAWN = -1000;
    public static final int BLACK_KING = -2000;

    public static int wb;
    public static int MAX_PLY = 2;//max ply
    public static int PLAYER_MAX;
    public static ArtificialBoard best = new ArtificialBoard(new ArrayList());
    private ArrayList<Square> board;
   
    public ArtificialPlayer(String name) throws java.rmi.RemoteException {
        this.name = name;
    }

    /**
     * This method returns a move for the given board, board.
     *
     * @author Matthew Evett
     */
    public int[] getMove(int[] board, boolean isWhite, int movesRemaining) throws java.rmi.RemoteException {

        
        for (int i = 0; i < board.length; i++) {
            board[i] = board[i] * -100;
        }
//        ArrayList result = new ArrayList();
        
        System.out.println(boardToString(board));
        /***********************************************************/
        /***********************************************************/
        System.out.println("There are " + movesRemaining + " moves remaining. Please enter move for " + name + " ("
                + (isWhite ? "White" : "Black") + ") : ");
//        String s = "";
//        try {
//            InputStreamReader isr = new InputStreamReader(System.in);
//            BufferedReader br = new BufferedReader(isr);
//            s = br.readLine();
//        } catch (IOException ex) {
//            System.err.println("IOException" + ex);
//        }
        //System.out.println("s = " + s);
//        StringTokenizer tokens = new StringTokenizer(s);
//        int nextMove = -1;
//        while (tokens.hasMoreTokens()) {
//            try {
//                nextMove = Integer.parseInt(tokens.nextToken());
//            } catch (NumberFormatException ex1) {
//                System.err.println("Bad position number: " + nextMove + " in input: " + s);
//                return null;
//            }
//            result.add(new Integer(nextMove));
//        }
//        int[] result2 = new int[result.size()];
//        for (int i = 0; i < result.size(); i++) {
//            result2[i] = ((Integer) result.get(i)).intValue();
//        }
        /**********************************************************************/
        //System.out.println(board.length);
        ArtificialBoard startBoard = new ArtificialBoard(board);
//                //wb = 1;
                wb = PLAYER_MAX;
//        
                int alphaBetaResult = BoardValue (startBoard, wb, 0, -999999999, 999999999);
                
                
                //System.out.println(alphaBetaResult);
                while(ArtificialPlayer.best != null) {
                    if(ArtificialPlayer.best.parent.equals(startBoard)) 
                        break;
                    ArtificialPlayer.best = ArtificialPlayer.best.parent;
                }
                //printBoard(ArtificialPlayer.best.board);
                //System.out.println("best board = " + Arrays.toString(display(ArtificialPlayer.best.board)));
                int [] temp = display(ArtificialPlayer.best.board);
                //System.out.println("processed board = " + Arrays.toString(temp));
                for(int i = 0; i <= 32; i++){
                    if(temp[i] == (PLAYER_MAX == 1? -100: 100)){
                        ;
                    } else if(temp[i] == (PLAYER_MAX == 1? 100: -100)){
                        ;
                    } else
                        temp[i] = 0;
                }
                System.out.println("AI board " + Arrays.toString(temp));
                int from = 0;
                int to = 0;
                for (int i = 0; i <= 32; i++){
                    if(board[i] != temp[i]) {
                        
                        if(board[i] == (PLAYER_MAX == 1? -100: 100) && temp[i] == 0){
                            from = i;
                        } else if(board[i] == 0 && temp[i] == (PLAYER_MAX == 1? -100: 100)){
                            to = i;
                        }
                    }
                }
                System.out.println("( From, To ) ( " +from +", "+ to + " )");
               int []result2 = new int [2];
                result2[0] = from;
                result2[1] = to;
        /**********************************************************************/
        
        //System.out.println("startBoard " + Arrays.toString(startBoard.display()));
        System.out.println("   board " + Arrays.toString(board));
        //System.out.println("result2 " + Arrays.toString(result2));
        return result2;
    }

    /**
     *
     * @author Matthew Evett
     * @return String that is a printable representation of the board position.
     */
    private static String boardToString(int[] pieces) {
        String result = "  ";
        for (int pos = 1; pos <= 32; pos++) {
            if (pos % 4 == 1 && pos != 1) {
                result += "\n";
                if (!isLeftRow(pos)) {
                    result += "  ";
                }
            }
            String piece;
            switch (pieces[pos]) {
                case BLACK_KING:
                    piece = "B ";
                    break;
                case 100:
                    piece = "b ";
                    break;
                case NO_PIECE:
                    piece = Integer.toString(pos);
                    if (pos < 10) {
                        piece += " ";  // All spaces should be two characters
                    }
                    break;
                case -100:
                    piece = "w ";
                    break;
                case WHITE_KING:
                    piece = "W ";
                    break;
                default:
                    piece = Integer.toString(pieces[pos]);
                    break;
            }
            result += piece + "  ";
        }
        return result;
    }

    /**
     *
     * @author Matthew Evett
     * @return true if row containing the indicated position is left-justfied
     */
    private static boolean isLeftRow(int pos) {
        return (pos - 1) % 8 > 3;
    }

    //simple BoardEval to test BoardValue function. Not intended as final 
    //prodcut
    private static int BoardEval(ArtificialBoard b)
    {
        ArrayList<Square> squares = b.board;
        int black = 0;
        int white = 0;
        int eval = 0;

        //generate value of each piece on the board
        for (int i=0; i < squares.size(); i++)
        {
            //piece is black pawn
            if (squares.get(i).piece == 100)
            {
                int row = b.getRow(squares.get(i).index) * 8;

                //value of piece is innately 8
                //pieces that are further up the board have a higher value
                black = black + 8 + (row/8);
                //System.out.println("Black row " + row);
            }

            //piece is black king
            if (squares.get(i).piece == 200)
            {
                //value of king is always 16
                black = black + 16;
            }

            //piece is white pawn
            if (squares.get(i).piece == -100)
            {
                int row = b.getRow(squares.get(i).index) * 8;

                //value of piece is innately 8
                //pieces that are further up the board have a higher value
                white = white + 8 + (row/8);
                //System.out.println("White row " + row);
            }

            //piece is white king
            if (squares.get(i).piece == -200)
            {
                //value of king is always 16
                white = white + 16;
            }
        }

        //player is white
        if (wb == 1)
        {
            
            eval = white - black;
            //System.out.println("white - black =  " + eval);
        }

        //player is black
        if (wb == 2)
        {
            eval = black - white;
            //System.out.println("black - white = " + eval);
        }
        
        //System.out.println("(black, white) = (" + black +"," + white +")");
        return eval;
    }

    /**
     * Given board initBrd, return the alphabeta value of it, and place the move
     * that renders this value in <best>.
     */
    public static int BoardValue(ArtificialBoard initBrd, int p, int ply,
            int alpha, int beta) {
        wb = p;
        //System.out.println("(Alpha, Beta, ply) " + alpha + ", " + beta+ ", " + ply);
        //printBoard(initBrd.board);
        if (ply >= MAX_PLY) // At bottom of search tree
        {
            return BoardEval(initBrd);
        }

        // Else we've got to look at the descendants
        ArrayList<ArtificialBoard> boards = ReachableBoards(initBrd.getBoard(), initBrd, p);
        System.out.println("Parent Board (ply) " + ply);
        printBoard(initBrd.board);
        System.out.println("Child Boards (ply) " + ply);
        for(int i = 0; i < boards.size(); i++){
            printBoard(boards.get(i).board);
            System.out.println();
        }
        if (boards.isEmpty()) { //no reachable moves you lose.
            //best = boards[0];// ???What to do if boards is empty???
            best = boards.get(0);
            //ArtificialPlayer.best = best;
            return best.value;
        }
        
        for (int b = 0; b < boards.size(); b++) {
            //System.out.println("(P, !P) => ( " + p + ", " + (p == 1 ? 2 : 1) + ") ");
            int val = BoardValue(boards.get(b), (p == 1 ? 2 : 1), ply + 1, alpha, beta);

            if (p == PLAYER_MAX) {
                if (val > alpha) {
                    alpha = val;
                    best = boards.get(b);
                    //System.out.println("(Alpha, Beta, ply) " + alpha + ", " + beta+ ", " + ply);
                    //ArtificialPlayer.best = best;
                }
                if (alpha >= beta) {
                    //System.out.println("(Alpha, Beta, ply) " + alpha + ", " + beta+ ", " + ply);
                    return alpha;	// Prune!  Alpha-cutoff
                }
            } else {			// MIN's turn
                if (val < beta) {
                    beta = val;
                    best = boards.get(b);
                    //ArtificialPlayer.best = best;
                    //System.out.println("(Alpha, Beta, ply) " + alpha + ", " + beta+ ", " + ply);
                }
                if (alpha >= beta) {
                    //printBoard(best.board);
                    //System.out.println("(Alpha, Beta, ply) " + alpha + ", " + beta+ ", " + ply);
                    return beta;	// Prune!  Beta-cutoff
                }
            }
        }
        return (p == PLAYER_MAX ? alpha : beta);
    }

    public static void printBoard(ArrayList<Square> board) {
        System.out.println(boardToString(display(board)));
    }

    public static int[] display(ArrayList<Square> board) {
        int[] places = new int[33];

        for (int i = 0; i < board.size(); i++) {
            places[i] = board.get(i).piece;
        }
        return places;
    }

    /**
     * Here's your chance to be imaginative.
     *
     * @return the name of the agent (hopefully something memorable!)
     */
    public String getName() throws java.rmi.RemoteException {
        return name;
    }

    public static ArrayList<ArtificialBoard> ReachableBoards(ArrayList<Square> state, ArtificialBoard mainBoard, int wb) {
        ArrayList<String> children = generate(state, mainBoard);
        ArrayList<ArtificialBoard> reachable = new ArrayList<ArtificialBoard>();

        for (int c = 0; c < children.size(); c++) {
            String child = children.get(c);
            String[] pieces = child.split("\\W+");

            ArrayList<Square> BoardSquares = new ArrayList<Square>();
            Square dummy = new Square();
            dummy.index = 0;
            dummy.piece = 0;
            BoardSquares.add(dummy);
            for (int p = 1; p < pieces.length; p++) {
                String curPiece = pieces[p];
                int temp;
                if (curPiece.equals("w")) {
                    temp = -100;
                } else if (curPiece.equals("b")) {
                    temp = 100;
                } else {
                    temp = p;
                }
                //int tok = Integer.parseInt(curPiece);

                Square sq = new Square();
                sq.index = p;
                sq.piece = temp;
                BoardSquares.add(sq);
            }

            ArtificialBoard tempBoard = new ArtificialBoard(BoardSquares);
            tempBoard.parent = mainBoard;
            reachable.add(tempBoard);

        }

        return reachable;
    }

    public static ArrayList<String> generate(ArrayList<Square> state,
            ArtificialBoard board) {
        int size = state.size();
        ArrayList<String> ret = new ArrayList<String>();

        int[] myState = new int[size + 1];
        int[] copy = new int[size + 1];

        for (int i = 0; i < size; i++) {
            myState[i] = state.get(i).piece;
            copy[i] = state.get(i).piece;
        }

        int counter = 0;

        for (int i = 0; i < state.size(); i++) {
            int curSpot = state.get(i).index;
            String loc = board.getOpenSquares(wb, curSpot);
            if (!loc.equals("")) {

                String pairs[] = loc.split(";;");

                for (int j = 0; j < pairs.length; j++) {

                    int piece = (wb == 1 ? -100 : 100);
//                    if (wb == 1) {
//                        piece = -100;
//                    } else {
//                        piece = 100;
//                    }

                    String currentPair = pairs[j];

                    String[] comps = currentPair.split("--");

                    int start = Integer.parseInt(comps[0]);
                    int end = Integer.parseInt(comps[1]);

                    myState[start] = start - 1;
                    myState[end] = piece;

                    String layout = boardToString(myState);
                    //System.out.println(layout);

                    ret.add(layout);
                    counter++;
                    for (int k = 0; k < size; k++) {
                        myState[k] = copy[k];
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Debugging: creates two players named Black and White and registers them
     * with RMI.
     *
     * @author Matthew Evett
     * @param args Two command-line arguments: the first should be either '1' or
     * '2', indicating whether the created Player should be rmi-registered as
     * 'first' or 'second'. The second argument should be the name of the
     * Player.
     * <p>
     * Example usage: <br>
     * <code>java -Djava.security.policy=permit.txt referee.HumanPlayer 1 "Fraser"</code>
     */
    public static void main(String[] args) {
        /*
       */
        if (args.length != 2 || (!args[0].equals("1") && !args[0].equals("2"))) {
            System.err.println("Usage: java HumanPlayer X FOO, where X is 1 for registering the agent as 'first',\n"
                    + "  2 for registering it as 'second'.  The second argument (FOO)is the name of the agent.\n");
            System.exit(-1);
        }

        String playerName = args[1];
        String playerRegistration = (args[0].equals("1") ? "first" : "second");
        PLAYER_MAX = wb = (args[0].equals("1") ? 1 : 2);
        System.setSecurityManager(new RMISecurityManager());

        try {
            ArtificialPlayer p = new ArtificialPlayer(playerName);
            Naming.rebind(playerRegistration, p);
            System.out.println("Player " + playerRegistration + "(named " + playerName + ") is waiting for the referee");
        } catch (MalformedURLException ex) {
            System.err.println("Bad URL for RMI server");
            System.err.println(ex);
        } catch (RemoteException ex) {
            System.err.println(ex);
        } 
        
//    ArrayList<Square> StartList = new ArrayList();
//    
//     for(int i=0; i<=32; i++){
//         Square sq = new Square();
//         if(i < 13){
//            sq.piece = 100; // black pawn
//         }
//         else if(i > 20){
//            sq.piece = -100; // white pawn
//         }
//         else{
//            sq.piece = i;
//         }
//         sq.index= i;
//         StartList.add(sq);
//      }
        // player 2 is black;
        // player 1 is white;
//    name = "jordan";
//    wb = 1;
//    ArtificialBoard startBoard = new ArtificialBoard(StartList);
//    int[] brdElements = startBoard.display();
        //ourBoard.getOpenSquares(wb, index);
        //ArrayList<Square> state = startBoard.getBoard();    
        //ArrayList<ArtificialBoard> reachable = ReachableBoards(state, startBoard, wb);
//    for(int i=0; i<reachable.size(); i++){
//       ArtificialBoard curBoard = reachable.get(i);
//       brdElements = curBoard.display();
//       System.out.println(boardToString(brdElements));
//       System.out.println();
//       System.out.println();
//    }

        // Return an integer that is larger for boards that are better for player MAX
/******************************************************************************    
        int [] temp = {0, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 0, 0, 0, 0, 100, 0, 0, 0, -100, -100, -100, -100, 0, -100, -100, -100, -100, -100, -100, -100, -100};
        ArtificialBoard startBoard = new ArtificialBoard(temp);
        //wb = 1;
        PLAYER_MAX = wb = 2;

        int result = BoardValue (startBoard, wb, 0, -999999999, 999999999);
        //System.out.println(" ( wb, PLAYER_MAX ) ( " + wb + ", " + PLAYER_MAX + " )");
        wb = PLAYER_MAX;
        
        System.out.println(result);
        while(ArtificialPlayer.best != null) {
            if(ArtificialPlayer.best.parent.equals(startBoard)) 
                break;
            ArtificialPlayer.best = ArtificialPlayer.best.parent;
        }
        printBoard(ArtificialPlayer.best.board);
/******************************************************************************/
        
        //result = BoardValue (best, 1, 0, result, 999999999);
        //System.out.println(result);
        //printBoard(best.board);
        /*
    int index = 7;
    System.out.println(ourBoard.getRow(index) + " " + ourBoard.getCol(index));
    
    int row = ourBoard.getRow(index);
    int col = ourBoard.getCol(index);
    System.out.println(ourBoard.getIndex(row, col));*/
        //ourBoard.getChildren(playerRegistration, index);
    }
}
