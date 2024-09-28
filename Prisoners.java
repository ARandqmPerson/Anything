import java.util.Map;
import java.util.HashMap;
import java.lang.Math;
import java.util.ArrayList;

public class Prisoners 
{
    public Map<Integer, Integer> boxes;

    public Prisoners() 
    {
        boxes = new HashMap<>();
    }
    
    public void Randomize() 
    {
        //There might be a better way but I'm tired
        ArrayList<Integer> a = new ArrayList<>();
        
        for (int i = 1; i < 101; i++) 
        {
            a.add(i);
        }
        System.out.println(a);

        for (int i = 1; i < 101; i++)
        {
            int b = (int) (Math.random() * a.size());
            boxes.put(i, a.get(b));
            a.remove(b);
        }
        System.out.println(boxes);
    }

    public boolean Search()
    {
        int prisoner = 1;
        
        boolean success = true;
    
        while (prisoner <= 100)
        {
            int box = 1;
            boolean done = false;
            int num = prisoner;
            while (done == false)
            {
                if (boxes.get(num) == prisoner) done = true;
                if (box == 50) success = false;
                
                num = boxes.get(num);
                box++;
            }
            System.out.println("Prisoner "+prisoner+": "+box);
            prisoner++;
        }

        return success;
    }
    public static void main(String args[]) {
        Prisoners a = new Prisoners();
        a.Randomize();
        System.out.println(a.Search());
    }
}
