import java.lang.Math;

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
    public double Iterate(double wager, double balance, double chance, int iterations) {
        double totalBalance = balance;
        while (iterations > 0 && totalBalance - wager > 0) {
            Martingale a = new Martingale();
            totalBalance = a.letsGoGambling(wager, totalBalance, chance);
            iterations--;
        }
        return totalBalance;
    }

    public static void main(String[] args) {
        Martingale a = new Martingale();
        //Chance is 0.49 because, at a casino, the house is always benefitted
        double b = a.Iterate(1, 1000, 0.49, 10000);
        System.out.println("Final balance: " + b + "$");
    }
}