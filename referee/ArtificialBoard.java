package referee;

import java.util.*;

public class ArtificialBoard {

    public int numRow = 8;
    public int numCol = 8;
    public int value = 0;
    public boolean isJump;
    ArtificialBoard parent;

    ArrayList<Square> board = new ArrayList();
    //Generate initial board

    public ArtificialBoard() {
        parent = null;
        for (int i = 0; i <= 32; i++) {
            Square sq = new Square();
            if (i < 13) {
                sq.piece = 100; // black pawn
            } else if (i > 20) {
                sq.piece = -100; // white pawn
            } else {
                sq.piece = i;
            }
            sq.index = i;
            board.add(sq);
        }
    }
    public ArtificialBoard(int [] boardToArrayList){
    /* the purpose of this constructor is to convert boardToArrayList to 
        ArrayList<Square> board which our AI uses to function
        */
        //System.out.println("boardToArrayList " + Arrays.toString(boardToArrayList));
        parent = null;
        for (int i = 0; i <= 32; i++) {
            Square sq = new Square();
            sq.piece = boardToArrayList[i];
            sq.index = i;
            board.add(sq);
        }
        
    }
    public ArtificialBoard(ArrayList<Square> list) {
        parent = null;
        board = list;
    }

    public int getIndex(int row, int col) {
        int temp = 0;
        if (row % 2 == 0) {
            temp = (7 - col) / 2;
        } else {
            temp = (6 - col) / 2;
        }
        return (row + 1) * 4 - temp;
    }

    public int getRow(int index) {
        return (int) (Math.ceil(index / 4.0)) - 1;
    }

    public int getCol(int index) {
        int row = getRow(index);
        int ret = 0;
        int temp = index * 2 - 1;

        if (row % 2 == 0) {
            ret = (temp % 8);
        } else {
            ret = (((index - 4) * 2 - 1) % 8) - 1;
        }

        return ret;
    }

    public int[] display() {
        int[] places = new int[64];
        for (int i = 0; i < board.size(); i++) {
            places[i] = board.get(i).piece;
        }

        return places;
    }

    // wb is 1 if player is white
    // wb is 2 if player is black
    // white for testing
    public String getOpenSquares(int wb, int index) {
        // check if index space is white
        //System.out.println("wb, index ---> " + wb + ", " + index );
        isJump = false;
        int currPiece = board.get(index).piece;

        int row = getRow(index);
        int col = getCol(index);

        int newPiece;

        String PairDelimit = "--";
        String SetDelimit = ";;";
        String ret = "";
        String retjump ="";

        if (currPiece == -100) {
            if (wb == 1) {
                // piece is white
                int newRow = row - 1;
                int colLeft = col - 1;
                int colRight = col + 1;

                if (newRow >= 0) {
                    if (colLeft >= 0) {
                        int newIndexLeft = getIndex(newRow, colLeft);
                        newPiece = board.get(newIndexLeft).piece;

                        if (newPiece == 100) {
                            int newRowJump = newRow - 1;
                            int newColLeftJump = colLeft - 1;

                            if (newRowJump >= 0 && newColLeftJump >= 0) {
                                int newIndexJump = getIndex(newRowJump, newColLeftJump);
                                int newPieceJump = board.get(newIndexJump).piece;

                                if (newPieceJump != -100 && newPieceJump != 100) {
                                    retjump = retjump + index + PairDelimit + newIndexJump
                                           + PairDelimit + newIndexLeft + SetDelimit;
                                    isJump = true;
                                }
                            }
                        }
                        if (newPiece != -100 && newPiece != 100) {
                            ret = ret + index + PairDelimit + newIndexLeft
                                    + SetDelimit;
                            //System.out.println(newIndexLeft);
                        }
                    } else {
                        //System.out.println("No left moves");
                    }

                    if (colRight <= 7) {
                        int newIndexRight = getIndex(newRow, colRight);
                        newPiece = board.get(newIndexRight).piece;

                        if (newPiece == 100) {
                            int newRowJump = newRow - 1;
                            int newColRightJump = colRight + 1;

                            if (newRowJump >= 0 && newColRightJump <= 7) {
                                int newIndexJump = getIndex(newRowJump,
                                        newColRightJump);
                                int newPieceJump = board.get(newIndexJump).piece;

                                if (newPieceJump != -100 && newPieceJump != 100) {
                                	retjump = retjump + index + PairDelimit + newIndexJump
                                            + PairDelimit + newIndexRight + SetDelimit;
                                        
                                        isJump = true;
//                                        System.out.println(retjump+ "," + isJump);
                                }
                            }
                        }
                        if (newPiece != -100 && newPiece != 100) {
                            //System.out.println(newIndexRight);
                            ret = ret + index + PairDelimit + newIndexRight
                                    + SetDelimit;
                        }
                    } else {
                        //System.out.println("No right moves");
                    }
                } else {
                    //System.out.println("No new moves");
                }
                //System.out.println(row + " " + col);
            }
        } else if (currPiece == 100) {
            // piece is black
            if (wb == 2) {
                int newRow = row + 1;
                int colLeft = col - 1;
                int colRight = col + 1;

                if (newRow <= 7) {
                    if (colLeft >= 0) {
                        int newIndexLeft = getIndex(newRow, colLeft);
                        newPiece = board.get(newIndexLeft).piece;

                        if (newPiece == -100) {
                            int newRowJump = newRow + 1;
                            int newColLeftJump = colLeft - 1;

                            if (newColLeftJump >= 0) {
                                int newIndexJump = getIndex(newRowJump, newColLeftJump);
                                int newPieceJump = board.get(newIndexJump).piece;

                                if (newPieceJump != -100 && newPieceJump != 100) {
                                	retjump = retjump + index + PairDelimit + newIndexJump
                                            + PairDelimit + newIndexLeft + SetDelimit;
                                        isJump = true;
                                }
                                if (newPiece != -100 && newPiece != 100) {
                                    ret = ret + index + PairDelimit + newIndexLeft
                                            + SetDelimit;
                                    //System.out.println(newIndexLeft);
                                }
                            }
                        }
                        if (newPiece != -100 && newPiece != 100) {
                            ret = ret + index + PairDelimit + newIndexLeft
                                    + SetDelimit;
                            //System.out.println(newIndexLeft);
                        }
                    } else {
                        //System.out.println("No left moves");
                    }

                    if (colRight <= 7) {
                        int newIndexRight = getIndex(newRow, colRight);
                        newPiece = board.get(newIndexRight).piece;

                        if (newPiece == -100) {
                            int newRowJump = newRow + 1;
                            int newColRightJump = colRight + 1;

                            if (newColRightJump <= 7) {
                                int newIndexJump = getIndex(newRowJump,
                                        newColRightJump);
                                 //System.out.println("jordan: " + newIndexJump);
                                int newPieceJump = board.get(newIndexJump).piece;

                                if (newPieceJump != -100 && newPieceJump != 100) {
                                	retjump = retjump + index + PairDelimit + newIndexJump
                                            + PairDelimit + newIndexRight + SetDelimit;
                                        isJump = true;
                                }

                                if (newPiece != -100 && newPiece != 100) {
                                    ret = ret + index + PairDelimit + newIndexRight
                                            + SetDelimit;
                                    //System.out.println(newIndexLeft);
                                }
                            }
                        }

                        if (newPiece != -100 && newPiece != 100) {
                            //System.out.println(newIndexRight);
                            ret = ret + index + PairDelimit + newIndexRight
                                    + SetDelimit;
                        }
                    } else {
                        //System.out.println("No right moves");
                    }
                } else {
                    //System.out.println("No new moves");
                }
                //System.out.println(row + " " + col);
            }
        } else {
            // piece is neither
            //System.out.println("none");
        }
         if(!retjump.equals("")){
            return retjump;
         }
         else{
            return ret;
         }
    }

    public ArrayList<Square> getBoard() {
        return this.board;
    }
}