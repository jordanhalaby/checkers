package referee;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.*;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.rmi.*;
import java.net.*;

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
    public static int PLAYER_MAX = 1;
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
        ArrayList result = new ArrayList();
        
        System.out.println(boardToString(board));
        /***********************************************************/
        //Our code here
        //ArtificialBoard startBoard = new ArtificialBoard();
        
        //BoardValue (startBoard, 1, 0, -999999999, 999999999);
        /***********************************************************/
        System.out.print("There are " + movesRemaining + " moves remaining. Please enter move for " + name + " ("
                + (isWhite ? "White" : "Black") + ") : ");
        String s = "";
        try {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            s = br.readLine();
        } catch (IOException ex) {
            System.err.println("IOException" + ex);
        }
        StringTokenizer tokens = new StringTokenizer(s);
        int nextMove = -1;
        while (tokens.hasMoreTokens()) {
            try {
                nextMove = Integer.parseInt(tokens.nextToken());
            } catch (NumberFormatException ex1) {
                System.err.println("Bad position number: " + nextMove + " in input: " + s);
                return null;
            }
            result.add(new Integer(nextMove));
        }
        int[] result2 = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            result2[i] = ((Integer) result.get(i)).intValue();
        }
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
    public static int BoardEval(ArtificialBoard b) {
        //System.out.println("************************");
        //printBoard(b.board);

        for (int i = 0; i < b.board.size(); i++) {
            if (b.board.get(i).piece == -100) {
                b.value++;
            }
            if (b.board.get(i).piece == 100) {
                b.value--;
            }
        }
        //System.out.println(b.value);
        //System.out.println("************************");
        return b.value;
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

        if (boards.isEmpty()) { //no reachable moves you lose.
            //best = boards[0];// ???What to do if boards is empty???
            best = boards.get(0);
            //ArtificialPlayer.best = best;
            return best.value;
        }
        
        for (int b = 0; b < boards.size(); b++) {
            int val = BoardValue(boards.get(b), (p == 1 ? 2 : p), ply + 1, alpha, beta);

            if (p == PLAYER_MAX) {
                if (val > alpha) {
                    alpha = val;
                    best = boards.get(b);
                    //ArtificialPlayer.best = best;
                }
                if (alpha >= beta) {
                    
                    return alpha;	// Prune!  Alpha-cutoff
                }
            } else {			// MIN's turn
                if (val < beta) {
                    beta = val;
                    best = boards.get(b);
                    //ArtificialPlayer.best = best;
                }
                if (alpha >= beta) {
                    //printBoard(best.board);
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

                    int piece;
                    if (wb == 1) {
                        piece = -100;
                    } else {
                        piece = 100;
                    }

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
        if (args.length != 2 || (!args[0].equals("1") && !args[0].equals("2"))) {
            System.err.println("Usage: java HumanPlayer X FOO, where X is 1 for registering the agent as 'first',\n"
                    + "  2 for registering it as 'second'.  The second argument (FOO)is the name of the agent.\n");
            System.exit(-1);
        }

        String playerName = args[1];
        String playerRegistration = (args[0].equals("1") ? "first" : "second");
        wb = (args[0].equals("1") ? 1 : 2);
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
        */
        
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
        ArtificialBoard startBoard = new ArtificialBoard();
        wb = 1;
//        ArtificialBoard best = new ArtificialBoard();
//        ArtificialPlayer.best = best;
        //printBoard(startBoard.board);
        int result = BoardValue (startBoard, 1, 0, -999999999, 999999999);
        System.out.println(result);
        while(ArtificialPlayer.best != null) {
            if(ArtificialPlayer.best.parent.equals(startBoard)) 
                break;
            ArtificialPlayer.best = ArtificialPlayer.best.parent;
        }
        printBoard(ArtificialPlayer.best.board);
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
