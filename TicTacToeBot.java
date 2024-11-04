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
    int player;
    
    public TicTacToeBot() {
        start = new MoveTree(0, 0, null);
    }
    //Given a set of moves, goes to that MoveTree
    //which has the current move and the set of possible moves
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
        int result;
        int place;
        int rating;
        int[] moves;

        public MoveTree(int p, int r, MoveTree pr) {
            place = p;
            result = r;
            previous = pr;
            possible = new HashMap<>();
            ttt = new TicTacToe();
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
                        MoveTree temp = new MoveTree(place, result, this);
                        temp.ttt = ttt.copy();
                        temp.ttt.move(i);
                        temp.moves[temp.moves.length - 1] = i;
                        possible.put(i, temp);
                        temp.place = i;
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
         * Most moves will be rated 0 because they will lead to
         * a draw with optimal play.
         * 
         * @return rating of this move
         */
        public int rateMove() {
            //Checks whether this move ends the game
            if (result == 3) return 0;
            if (result == 2) return 2;
            if (result == 1) return 1;
            //Checks possible moves
            if (result == 0) {
                for (int i = 1; i <= 9; i++) {
                    if (possible.containsKey(i)) {
                        //If the current player can win by force,
                        //that's the rating of the move
                        int a = possible.get(i).rateMove();
                        if (a == player) return a;
                    }
                }
            }
            return 0;
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
                    if (possible.containsKey(i)) s += "["+possible.get(i).place+"]";
                }
            }
            return s;
        }   
    }
    //Testing
    public static void main(String[] args) {
        TicTacToeBot test = new TicTacToeBot();
        test.start.addPaths();
        int[] path = {2, 4, 5};
        System.out.println(test.goToTree(path).toString());
    }
}