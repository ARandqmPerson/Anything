import java.util.*;

/*
 * A simulation of the 100 prisoners problem:
 * 100 prisoners are faced with a challenge. Inside a room are 100 numbered boxes, and inside each box
 * is a random number 1-100, with every number appearing once. Each prisoner has their own number,
 * and if they find their number inside a box, they're free. However, each one can only check 50 boxes,
 * and if even one prisoner does not find their number, they are all executed.
 * 
 * It seems impossible, but there is a fascinating solution which gives all prisoners a 31% chance to
 * escape, and I have modeled it here.
 */
public class Prisoners 
{
    public Map<Integer, Integer> boxes;
    public Set<Integer> loops;
    public boolean success;

    public Prisoners() 
    {
        boxes = new HashMap<>();
        loops = new HashSet<Integer>();
        success = true;
    }
    //Shuffle the boxes
    public void Randomize() 
    {
        ArrayList<Integer> a = new ArrayList<>();
        
        for (int i = 1; i < 101; i++) 
        {
            a.add(i);
        }

        for (int i = 1; i < 101; i++)
        {
            int b = (int) (Math.random() * a.size());
            boxes.put(i, a.get(b));
            a.remove(b);
        }
    }

    //Prisoners search for their number, 1-by-1

    public void Search()
    {
        int prisoner = 1;
        
        while (prisoner <= 100)
        {
            int box = 0;
            boolean done = false;
            int num = prisoner;

            while (done == false)
            {
                box++;
                if (boxes.get(num) == prisoner) done = true;
                if (box == 51) success = false;
                
                num = boxes.get(num);
                
            }
            if (box > 100) System.out.println(box);
            else loops.add(box);
            prisoner++;
        }
    }

    //Runs the simulation numerous times
    //Records the success rate and frequency of loop lengths
    public void Iterate(int iterations) {
        int totalSuccess = 0;
        double successRate;

        Map<Integer, Double> freq = new HashMap<>();
        for (int i = 1; i <= 100; i++) 
        {
            freq.put(i, 0.0);
        }

        for (int i = 0; i < iterations; i++) 
        {
            Prisoners a = new Prisoners();
            a.Randomize();
            a.Search();
            for (int j : a.loops) {
                freq.put(j, freq.get(j) + 1.0);
            }
            if (a.success) totalSuccess++;
        }

        //Calculate and print sucess rate
        successRate = 100.0 * totalSuccess / iterations;
        System.out.println("Success rate: " + successRate + "%");

        //Calculate and print frequency of loop lengths
        for (int i = 1; i <= 100; i++) {
            freq.replace(i, freq.get(i) / iterations);
        }
        System.out.println(freq.toString());
    }
    public static void main(String[] args) {
        
        Prisoners h = new Prisoners();
        h.Iterate(10000);
    }
}