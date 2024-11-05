/**
 * Compile using: javac FindMax.java
 * Run using: java FindMax n1 n2 n3 
 * For example: java FindMax 3 5 4
 * It should print 5
 * For help options:
 * Use: java FindMax -h
 * Use: java FinxMax --help
 */

public class FindMax
{

    public static void main (String[] args)
    {
        //System.out.println("args[0]" + args[0]);
        if (args == null || args.length == 0) {
            System.out.println("Usage: java FindMax n1 n2 n3");
            System.exit(1);
        }  
        if (args[0].equals("") ||
            args[0].equalsIgnoreCase("-h") ||
            args[0].equalsIgnoreCase("--help"))
        {
            System.out.println("Usage: java FindMax n1 n2 n3");
            System.exit(1);
        }

        int[] arrIntegers = new int[args.length];
        for (int index = 0; index < args.length; index++)
        {
            arrIntegers[index] = Integer.valueOf(args[index]);
        }

        FindMax maxFinder = new FindMax();
        try
        {
            int max = maxFinder.findMax(arrIntegers);
            System.out.println("Max: " + max);
            System.exit(0);
        }
        catch (IllegalArgumentException iArgumentException)
        {
            System.out.println(iArgumentException.getMessage());
            System.exit(1);
        }
    }

    private int findMax(int[] items) throws IllegalArgumentException
    {
        if (items == null)
        {
            throw new IllegalArgumentException("Items cannot be null.");
        }
        if (items.length == 0) {
            throw new IllegalArgumentException("Items must have at least one entry.");     
        }

        int max = Integer.MIN_VALUE;
        for (int index = 0; index < items.length; index++)
        {
            if (items[index] > max)
            {
                max = items[index];
            }
        }
        return max;
    }
}