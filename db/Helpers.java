package db;

/**
 * Created by jessicahsiao on 3/6/17.
 */

public class Helpers {
    public static boolean isLowerCase(String s)
    {
        for (int i=0; i<s.length(); i++)
        {
            if (!Character.isLowerCase(s.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }
}
