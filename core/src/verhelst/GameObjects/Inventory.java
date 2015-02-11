package verhelst.GameObjects;

import java.security.Key;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Orion on 2/10/2015.
 */
public class Inventory {

    private static HashMap<Item, Integer> items = new HashMap<Item, Integer>();

    private static HashSet<Equipment> equipmentHashSet = new HashSet<Equipment>();

    public static void addItem(Item item){
        if(item instanceof  Equipment){
            equipmentHashSet.add((Equipment)item);
        }else{
            if(items.containsKey(item)){
                int a = items.get(item).intValue() + 1;
                items.put(item, new Integer(a));
            }
        }
    }






}
