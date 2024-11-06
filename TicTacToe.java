import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class TicTacToe {
    
    public Map<Integer, Square> grid;
    public ArrayList<Integer> moves;
    public int nextPlayer;
    public int winner;
    public int player;


    public TicTacToe() {
        reset();
    }

    public void reset() {
        winner = 0;
        player = 1;
        nextPlayer = 2;
        moves = new ArrayList<>();
        grid = new HashMap<>();
        for (int i = 1; i <= 9; i++) {
            grid.put(i, new Square());
            grid.get(i).type = 0;
        }
    }

    public String toString() {
        String str = new String();
        for (int i = 1; i <= 9; i++) {
            str += (grid.get(i));
            if (i==3 || i==6) str+="\n";
        }
        return str;
    }
    /*
     * If the Square that corresponds to the
     * given number exists and is empty, that Square is now
     * taken by the current player, adds to move list, 
     * and it's the next player's turn.
     * 
     * @return whether the method succeeded
     */
    public boolean move(int n) {
        Square s = grid.get(n);
        if (s == null) return false;
        else if (s.type == 0) {
            s.type = player;
            checkIfFinished();
            int temp = player;
            player = nextPlayer;
            nextPlayer = temp;
            moves.add(n);
        } 
        else return false;
        return true; 
    }

    public void playUserInput() {
        Scanner scan = new Scanner(System.in);
        System.out.println(
"""
|TIC-TAC-TOE|
Reference:
[1][2][3]
[4][5][6]
[7][8][9] """);

        while(winner == 0) {           
            System.out.println("Player " + player + ": ");
            try {
                if(move(scan.nextInt()))
                    System.out.println(toString());
                else System.out.println("Invalid input");
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input");
                scan.nextLine();
            }
        }
        
        if (winner == 3) {
            System.out.println("Draw!");
        }

        else {
            System.out.println("Player " + winner + " wins!");
        }

        scan.close();
    }

    public void playAgainstBot() {
        System.out.println("Generating tree...");
        Scanner scan = new Scanner(System.in);
        TicTacToeBot bot = new TicTacToeBot(this);

        boolean check = false;
        System.out.println("Player 1 or 2?");
        while (check == false) {
            String q = scan.nextLine();
            if (q.equals("1")) {
                check = true;
                System.out.println(
"""
|TIC-TAC-TOE|
Reference:
[1][2][3]
[4][5][6]
[7][8][9] """);
                System.out.println("Player 1: ");
                try {
                    if(move(scan.nextInt()))
                        System.out.println(toString());
                    else System.out.println("Invalid input");
                }
                catch (InputMismatchException e) {
                    System.out.println("Invalid input");
                    scan.nextLine();
                }
            }
            else if (q.equals("2")) {
                check = true;
                System.out.println(toString());
            }
            else {
                System.out.println("Type '1' or '2':");
            }
        }
        while (winner == 0) {
            System.out.println("Bot's move:");
            move(bot.goToTree(moves).chooseMove());
            System.out.println(toString());

            check = false;
            while (winner == 0 && check == false) {
                System.out.println("Player " + player + ": ");
                try {
                    if(move(scan.nextInt())) {
                        System.out.println(toString());
                        check = true;
                    }
                    else {
                        System.out.println("Invalid input");
                    }
                    
                }
                catch (InputMismatchException e) {
                    System.out.println("Invalid input");
                    scan.nextLine();
                }
            }
        }

        if (winner == 3) System.out.println("Draw!");
        else System.out.println("Player " + winner + " wins!");

        scan.close();
    }

    public void checkIfFinished() {
        Square a; Square b; Square c;
        for (int i = 0; i < 3; i++) {
            a = grid.get(1+3*i); b = grid.get(2+3*i); c = grid.get(3+3*i);
            child(a, b, c);
            a = grid.get(1+i); b = grid.get(4+i); c = grid.get(7+i);
            child(a, b, c);
        }
            a = grid.get(1); b = grid.get(5); c = grid.get(9);
            child(a, b, c);
            a = grid.get(3); b = grid.get(5); c = grid.get(7);
            child(a, b, c);

        if (winner == 0) {
            winner = 3;
            for (int i = 1; i <= 9; i++) {
                if (grid.get(i).type == 0) winner = 0;
            }
        }
    }
    //weird format but I don't know what the standard would be
        public void child(Square a, Square b, Square c) {
            if (a.type == b.type && b.type == c.type && a.type != 0) {
                winner = player;
                a.isPartOfLine = true; b.isPartOfLine = true; c.isPartOfLine = true;
            }
        }

    public TicTacToe copy() {
        TicTacToe d = new TicTacToe();
        for (int i = 1; i <= 9; i++) {
            d.grid.put(i, new Square());
            d.grid.get(i).type = grid.get(i).type;
        }
        d.player = player;
        d.nextPlayer = nextPlayer;
        d.winner = winner;
        return d;
    }

    public class Square {
        public int type;
        public String letter;
        public boolean isPartOfLine;

        public String toString() {
            if (type == 0) letter = " ";
            if (type == 1) letter = "x";
            if (type == 2) letter = "o";
            if (isPartOfLine) letter = letter.toUpperCase();
            return "["+letter+"]";
        }

    }

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        // game.playUserInput();
        game.playAgainstBot();
    }
}