package referee;
 import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.*;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.rmi.*;
import java.net.*;

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
  public int[] getMove(int[] board, boolean isWhite, int movesRemaining) throws java.rmi.RemoteException {
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
    
    
    ArrayList<Square> StartList = new ArrayList();
    
     for(int i=0; i<=32; i++){
         Square sq = new Square();
         if(i < 13){
            sq.piece = 100; // black pawn
         }
         else if(i > 20){
            sq.piece = -100; // white pawn
         }
         else{
            sq.piece = i;
         }
         sq.index= i;
         StartList.add(sq);
      }
      
      
    // player 2 is black;
    // player 1 is white;
    name = "jordan";
    String playerName = "jordan Halaby";
    wb = 1;
    String playerRegistration = "first";
    
    ArtificialBoard startBoard = new ArtificialBoard(StartList);
    int[] brdElements = startBoard.display();
    
    System.setSecurityManager(new RMISecurityManager());
    
    int index = 21;
    
    //ourBoard.getOpenSquares(wb, index);
    
    ArrayList<Square> state = startBoard.getBoard();    
    
    ArrayList<String> children = generate(state, startBoard);
   
    
    for(int c=0; c <children.size(); c++){
      String child = children.get(c);
         System.out.println(child);
         System.out.println();
         System.out.println();
         }
    /*
    try {
      ArtificialPlayer p = new ArtificialPlayer(playerName);
      Naming.rebind(playerRegistration, p);
      System.out.println("Player "+playerRegistration+"(named "+playerName+") is waiting for the referee");
    }
    catch (MalformedURLException ex) {
      System.err.println("Bad URL for RMI server");
      System.err.println(ex);
    }
    catch (RemoteException ex) {
      System.err.println(ex);
    }*/
    
    /*
    int index = 7;
    System.out.println(ourBoard.getRow(index) + " " + ourBoard.getCol(index));
    
    int row = ourBoard.getRow(index);
    int col = ourBoard.getCol(index);
    System.out.println(ourBoard.getIndex(row, col));*/
    
    //ourBoard.getChildren(playerRegistration, index);
  }
   /**
   * Here's your chance to be imaginative.
   *
   * @return the name of the agent (hopefully something memorable!)
   */
  public String getName() throws java.rmi.RemoteException {
    return name;
  }
    public static ArrayList<String> generate(ArrayList<Square> state, ArtificialBoard board){
      int size = state.size();
      ArrayList<String> ret = new ArrayList<String>();
            
      int[] myState = new int[size+1];
      int[] copy = new int[size+1];
      
      for(int i=0; i< size; i++){
         myState[i] = state.get(i).piece;
         copy[i] = state.get(i).piece;
      }
      
      int counter = 0;
      
      for(int i=0; i<state.size(); i++){
         int curSpot = state.get(i).index;
         String loc = board.getOpenSquares(wb, curSpot);
         if(!loc.equals("")){
            System.out.println(curSpot + ": " + loc);
            
            String pairs[] = loc.split(";;");
            
            for(int j=0; j<pairs.length; j++){
               
               int piece;
               if(wb == 1){
                  piece = -100;
               }
               else{
                  piece = 100;
               }
               
              String currentPair = pairs[j];
              
              String[] comps = currentPair.split("--");
              
              int start = Integer.parseInt(comps[0]);
              int end = Integer.parseInt(comps[1]);
              
               myState[start] = start-1;
               myState[end] = piece;
               
               String layout = boardToString(myState);
               //System.out.println(layout);
               System.out.println();
               System.out.println();
               
               ret.add(layout);
               counter++;
               for(int k =0; k<size; k++){
                  myState[k] = copy[k];
               }
            }
         }
      }
      return ret;
   }
}