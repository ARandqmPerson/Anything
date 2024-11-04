import java.util.HashMap;
/*
 * A TicTacToeBot that can calculate every possible
 * game of TicTacToe and save them into a MoveTree
 * (hopefully in the future you won't have to create this
 * tree every time) and will eventually be able to
 * play from any position.
 */
public class TicTacToeBot {
    MoveTree start;
    int givenPlayer;
    
    public TicTacToeBot() {
        start = new MoveTree(0, null, new TicTacToe());
    }
    
    public MoveTree goToTree(int[] z) {
        MoveTree current = start;
        for (int i : z) {
            if (!current.possible.containsKey(i)) return null;
            current = current.possible.get(i);
        }
        return current;
    }

    public class MoveTree {

        HashMap<Integer, MoveTree> possible;
        MoveTree previous;
        TicTacToe ttt;
        int[] moves;
        int result;
        int rating;

        public MoveTree(int r, MoveTree pr, TicTacToe t) {
            ttt = t;
            result = r;
            previous = pr;
            possible = new HashMap<>();
            /* 
             * If there was a previous move, copies the moves
             * of the previous MoveTree and leaves a spot open
             * for the current move (which should be filled after
             * the constructor is called),
             * otherwise makes an empty array
            */
            if (pr != null) {
                moves = new int[pr.moves.length + 1];
                for (int i = 0; i < pr.moves.length; i++) {
                    moves[i] = pr.moves[i];
                }
            }
            else {
                moves = new int[0];
            }
        }
        /*
        * Recursively records every possible outcome
        * At each move, attempts to move on every square
        * If a move finishes the game, that tree is closed
        * and the result is recorded
        */
        public void addPaths() {
            //Checks whether the game is already over
            if (ttt.winner != 0) {
                result = ttt.winner;
            }
            else {
                //Tries every move
                for (int i = 1; i <= 9; i++) {
                    //Checks whether the move is possible
                    if (ttt.grid.get(i).type == 0) {
                        /*
                         * Creates a temporary MoveTree which has the same place,
                         * has the same result (0), has this MoveTree as its previous,
                         * and has an array of moves that's the same as this but with an
                         * extra space.
                         * Then creates an identical TicTacToe game, makes a move,
                         * records the move, gets added to the possible list, and
                         * the method iterates again.
                         */
                        MoveTree temp = new MoveTree(result, this, ttt.copy());
                        temp.ttt.move(i);
                        temp.moves[temp.moves.length - 1] = i;
                        possible.put(i, temp);
                        temp.addPaths();
                    }
                }
            }            
        }
        /*
         * Recursively checks every outcome after this move to
         * determine how "good" it is.
         * If this move wins the game for either side, it gets
         * a rating, 1 or 2, depending on the outcome --
         * If either player can win by force after this move,
         * it will receive the same rating --
         * Otherwise, the move will be rated 0.
         * 
         * @return rating of this move
         */
        public int rateMove() {
            
            if (result == 3) {rating = 0; return rating;}
            if (result == 2) {rating = 2; return rating;}
            if (result == 1) {rating = 1; return rating;}
            
            rating = 0;
            boolean trapped = true; 
            for (int i = 1; i <= 9; i++) {
                if (possible.containsKey(i)) {
                    int a = possible.get(i).rateMove();
                    if (a == ttt.player) {
                        rating = ttt.player;
                        trapped = false;
                    }
                    if (a == 0) trapped = false;
                }
            }
            if (trapped) rating = ttt.nextPlayer;
        
            return rating;
        }
        
        public String toString() {
            String s = new String("Moves so far:\n[");
            s += moves[0];
            for (int i = 1; i < moves.length; i++) {
                s += ", "+moves[i];
            }
            s += "]\n";
            if (possible.isEmpty()) s += "No possible moves, game is over";
            else {
                s += "Possible moves:\n";
                for (int i = 1; i <= 9; i++) {
                    if (possible.containsKey(i)) s += "["+i+"]";
                }
            }
            return s;
        }   
    }

    //Testing
    public static void main(String[] args) {
        TicTacToeBot test = new TicTacToeBot();
        test.start.addPaths();
        int[] path = {1, 4};
        MoveTree testTree = test.goToTree(path);
        System.out.println(testTree.rateMove()+"\n"+testTree.ttt+"\n"+testTree.toString());
    }
}