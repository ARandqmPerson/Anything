import java.lang.Math;
import java.util.Scanner;

/*
 * Model of the Martingale algorithm, a gambling method
 * which sounds intuitively successful but is no better
 * than keeping your wager consistent.
 */
public class Martingale {
    //Uses recursion to gamble until you lose all your money or you win back your initial wager
    public double letsGoGambling(double w, double b, double chance) {  
        double wager = w;
        double balance = b;
        if(balance - wager >= 0) {    
            if (Math.random() > chance) {
                balance = letsGoGambling(wager * 2, balance - wager, chance);
            }
            else {
                balance = balance + wager;
            }
        }
        //fix floating point error
        balance = Math.round(balance * 100.0) / 100.0;
        
        return balance;
    }
    //Runs the simulation multiple times and returns the average profit/loss
    public double Iterate(double wager, double balance, double chance, int iterations) {
        double[] sum = new double[iterations];
        double average = 0;
        for (int i = 0; i < iterations; i++) {
            Martingale a = new Martingale();
            sum[i] = a.letsGoGambling(wager, balance, chance) - balance;
            iterations--;
        }
        for (double i : sum) {
            average += i;
        }
        average = average / iterations;
        return average;
    }

    public static void main(String[] args) {
        Martingale a = new Martingale();
        Scanner b = new Scanner(System.in);
        System.out.println("Enter the amount of times you want to try (higher is more accurate): ");
        int c = b.nextInt();
        b.close();
        //Chance is 0.49 because, at a casino, the house is always benefitted
        System.out.println("Average return: " + a.Iterate(1, 100, 0.49, c) + "$");
    }
}