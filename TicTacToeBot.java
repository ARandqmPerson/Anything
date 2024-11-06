import java.util.HashMap;
import java.util.ArrayList;
import java.lang.Math;
/*
 * A TicTacToeBot that can calculate every possible
 * game of TicTacToe and save them into a MoveTree
 * (hopefully in the future you won't have to create this
 * tree every time) and will eventually be able to
 * play from any position.
 */
public class TicTacToeBot {
    MoveTree start;
    
    public TicTacToeBot(TicTacToe t) {
        start = new MoveTree(null, t);
        start.addPaths();
        start.rateMove();
    }
    
    public MoveTree goToTree(ArrayList<Integer> z) {
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
        int result;
        int rating;

        public MoveTree(MoveTree pr, TicTacToe t) {
            ttt = t;
            result = t.winner;
            previous = pr;
            possible = new HashMap<>();
            if (pr != null) {
                for (Integer i : pr.ttt.moves) {
                    ttt.moves.add(i);
                }
            }
            else ttt.moves = new ArrayList<>();
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
                        MoveTree temp = new MoveTree(this, ttt.copy());
                        temp.ttt.move(i);
                        temp.ttt.moves.add(i);
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
        /*
         * Will pick the best-rated move, and
         * picks randomly if there's a tie to
         * prevent the same game over and over
         * 
         * @return the chosen move
         */
        public int chooseMove() {
            ArrayList<Integer> temp = new ArrayList<>();
            int max = ttt.nextPlayer;
            
            for (int i = 1; i <= 9; i++) {
                if (possible.containsKey(i)) {
                    temp.add(i);
                    if (possible.get(i).rating == ttt.player) max = ttt.player;
                    if (max != ttt.player && possible.get(i).rating == 0) max = 0;
                }
            }
            
            for (int i = temp.size() - 1; i >= 0; i--) {
                if (possible.get(temp.get(i)).rating != max) temp.remove(i);
            }

            return temp.get((int)(Math.random() * temp.size()));
        }
        
        public String toString() {
            String s = new String("Moves so far:\n[");
            s += ttt.moves.get(0);
            for (int i = 1; i < ttt.moves.size(); i++) {
                s += ", "+ttt.moves.get(i);
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
        TicTacToeBot test = new TicTacToeBot(new TicTacToe());
        ArrayList<Integer> path = new ArrayList<>();
        path.add(2); path.add(4);
        MoveTree testTree = test.goToTree(path);
        System.out.println(testTree.rating+"\n"+testTree.ttt+"\n"+testTree.toString());
    }
}