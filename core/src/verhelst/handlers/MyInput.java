package verhelst.handlers;

/**
 * Created by Orion on 2/8/2015.
 */
public class MyInput {

    private static int x, y;

    protected static void setTouch(int x1, int y1){
        x = x1;
        y = y1;
    }

    public static boolean hasTouch(){
        return x != 0 && y != 0;
    }

    public static int[] consumeXY(){
        int[] ret = {x,y};
        x= 0;
        y = 0;
        return ret;
    }


}
