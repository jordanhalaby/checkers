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
 * <p>Title: Player</p>
 * <p>Description: Testing representation of a checkers player</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
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
  
  public ArtificialPlayer(String name) throws java.rmi.RemoteException {
    this.name = name;
  }

  /**
   * This method returns a move for the given board, board.
   *
   * @author Matthew Evett
   */
  public int[] getMove(int[] board, boolean isWhite, int movesRemaining) 
          throws java.rmi.RemoteException {
   for(int i=0; i<board.length; i++){
      board[i] = board[i]*-100;
   }
    ArrayList result = new ArrayList();
    System.out.println(boardToString(board));
    System.out.print("There are "+movesRemaining+" moves remaining. Please enter move for " + name + " ("+
                     (isWhite ? "White" : "Black") + ") : ");
    String s = "";
    try {
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);
      s = br.readLine();
    }
    catch (IOException ex) {
      System.err.println("IOException" + ex);
    }

    StringTokenizer tokens = new StringTokenizer(s);
    int nextMove = -1;
    while (tokens.hasMoreTokens()) {
      try {
        nextMove = Integer.parseInt(tokens.nextToken());
      }
      catch (NumberFormatException ex1) {
        System.err.println("Bad position number: "+nextMove+ " in input: "+s);
        return null;
      }
      result.add(new Integer(nextMove));
    }

    int[] result2 = new int[result.size()];
    for (int i = 0; i<result.size(); i++) {
      result2[i]=((Integer)result.get(i)).intValue();
    }
    return result2;
  }

  /**
   *
   * @author Matthew Evett
   * @return String that is a printable representation of the board position.
   */
  private static String boardToString (int[] pieces) {
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
          if (pos < 10) piece +=" ";  // All spaces should be two characters
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
      result += piece+"  ";
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

  /**
   * Debugging: creates two players named Black and White and registers them with RMI.
   *
   * @author Matthew Evett
   * @param args Two command-line arguments: the first should be either '1' or '2', indicating
   * whether the created Player should be rmi-registered as 'first' or 'second'.  The second argument
   * should be the name of the Player.
   * <p>Example usage: <br>
   * <code>java -Djava.security.policy=permit.txt referee.HumanPlayer 1 "Fraser"</code>
   */
  public static void main (String[] args) {
    
    
    
    // player 2 is black;
    // player 1 is white;
    name = "jordan";
    String playerName = "jordan Halaby";
    wb = 1;
    String playerRegistration = "first";
    
    ArtificialBoard ourBoard = new ArtificialBoard();
   
    //int[] brdElements = ourBoard.display();
    //System.out.println(Arrays.toString(brdElements));
    //System.out.println(boardToString(brdElements));
    System.out.println("---------------");
    System.out.println("Initial state");
    printBoard(ourBoard.getBoard());
    System.out.println("---------------");
    
    System.setSecurityManager(new RMISecurityManager());

    
    ArrayList<Square> state = ourBoard.getBoard();    
    
    ArrayList<ArrayList<Square>> outer = generate(state, ourBoard);
    for(int i = 0; i < outer.size(); i++){
        ArrayList<Square> inner = outer.get(i);
        System.out.println("---------------");
        System.out.println("Child State");
        printBoard(inner);
        System.out.println("---------------");
    }
    //System.out.println(outer.get(0));
    
    }

  /**
   * Here's your chance to be imaginative.
   *
   * @return the name of the agent (hopefully something memorable!)
   */
  public String getName() throws java.rmi.RemoteException {
    return name;
  }

   public static ArrayList<ArrayList<Square>> generate(ArrayList<Square> state, ArtificialBoard board){
       
        ArrayList<ArrayList<Square>> outer = new ArrayList<ArrayList<Square>>();
        //ArrayList<Square> inner = new ArrayList<Square>();
        
        ArrayList<String[]> test = new ArrayList<String[]>();
        //System.out.println(state.size());
        for(int i=1; i < state.size(); i++) {
            //System.out.println(state.get(i).index);
            int curSpot = state.get(i).index;
            String loc = board.getOpenSquares(1, curSpot);
            if(!loc.equals("")){
                String SetDelimit = ";;";
                String[] setMoves = loc.split(SetDelimit);
                test.add(setMoves);
//                System.out.println("Moves for each piece: " + Arrays.toString(setMoves)); 
//                System.out.println(curSpot + ": " + loc);
//                System.out.println("Number of moves: " + setMoves.length);
//                System.out.println("-----------------------------------------");    
                //ArrayList<Square> inner = move(setMoves[0],state);
                //for(int j=0; j<setMoves.length; j++){
                   //ArrayList<Square> inner = move(setMoves[j],state);
//                   
//                   System.out.println(inner.get(j).index); 
//                   System.out.println(inner.get(j).piece);
                   //temp.add(moveList);
                   //moveList = null;

                   //printArrayList(temp);
                //}

            }
        }
        ArrayList<Square> initialState = ArrayListCopy(state); 
        
//        System.out.println("---------------");
//        System.out.println("Initial state");
//        printBoard(initialState);
//        System.out.println("---------------");
        
        for(int i = 0; i < test.size();i++){
            for(int j = 0; j < test.get(i).length; j++){
                //System.out.println(test.get(i)[j]);

                ArrayList<Square> inner = move(test.get(i)[j],state);
                //System.out.println(inner.get(i).piece);
                outer.add(inner);
            }
        }
//        System.out.println("---------------");
//        System.out.println("Initial state");
//        printBoard(initialState);
//        System.out.println("---------------");
        
        //System.out.println(outer.size());
        
//        for(int i=0; i < outer.size(); i++){
//            System.out.println();
//            System.out.println();
//            ArrayList<Square> child = outer.get(i);
////            for(int k=0; k<child.size(); k++){
////              System.out.println(child.get(k).piece);
////            }
//            ArtificialBoard brd = new ArtificialBoard(child);
//            int[] tointboard = brd.display();
//            System.out.println(boardToString(tointboard));
//        }
      return outer;
   }
    private static ArrayList<Square> ArrayListCopy(ArrayList<Square> source) {
        ArrayList<Square> destination = new ArrayList<Square>();
        
        for(int i = 0; i < source.size(); i++){
            Square temp = new Square();
            temp.index = source.get(i).index;
            temp.piece = source.get(i).piece;
            destination.add(temp);
        }
        
        return destination;
    }
   public static ArrayList<Square> move(String pair, ArrayList<Square> board){
       //ret = board;
       ArrayList<Square> ret = ArrayListCopy(board);
      
        
       
        String PairDelimit = "--";
        //System.out.println(pair);
        String[] comps = pair.split(PairDelimit);
        //System.out.println(Arrays.toString(comps));

        int piece;
        //White = -100
        //Black = 100;
        if(wb == 1){
           piece = -100;
        }
        else{
           piece = 100;
        }

        int []pairArray = new int[2];

        for(int i = 0; i < comps.length; i++){
           pairArray[i] = Integer.parseInt(comps[i]);
           //System.out.println(pairArray[i]);
        }
      //System.out.println(Arrays.toString(pairArray));
      /*
      Setting square on board to white.
      Then setting  old square to index.
      */
      
//      System.out.println("( from, to )");
//      System.out.println("( " + pairArray[0] + ", " + pairArray[1] + " )");
      //System.out.println(ret.size());
//      for(int i = 0; i < ret.size(); i++){
//          System.out.println("Index: " + ret.get(i).index);
//          System.out.println("Piece: " + ret.get(i).piece);
//      }
      //System.out.println("Index: " + ret.get(pairArray[1]).index);
      //System.out.println("Piece: " + ret.get(pairArray[1]).piece);
      //System.out.println(Arrays.toString(display(ret)));
//      System.out.println("---------------");
//      System.out.println("Current state");
//      printBoard(ret);
//      System.out.println("---------------");
      
      /*Go to next state and set it to White*/
      Square sq;
      //Go to index of what ever pairArray[1] is and set sq to it
      int nextIndex = pairArray[1];
      sq = ret.get(nextIndex);
      sq.piece = piece;
      //System.out.println(sq.piece);
      //System.out.println(sq.index);
      ret.set(nextIndex, sq);
      //printBoard(ret);
      
      int currentIndex = pairArray[0];
      sq = ret.get(currentIndex);
      sq.piece = currentIndex;
      ret.set(currentIndex, sq);
      
//      System.out.println("---------------");
//      System.out.println("Next state");
//      printBoard(ret);
//      System.out.println("---------------");
      //System.out.println(arrOfStr[arrOfStr.length-1]);    
      return ret;
    }
   
   public static void printBoard(ArrayList<Square> board){
       System.out.println(boardToString(display(board)));
   }
   public static int[] display(ArrayList<Square> board){
      int[] places = new int[33];

      for(int i =0; i < board.size(); i++){
         places[i] = board.get(i).piece;
      }
      return places;
   }
}
