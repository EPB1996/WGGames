package org.telegram;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class ActionCards {
    public static HashMap<String, String> Actions = new HashMap<>();


    public static HashMap<String, String> getCards(){

        //GET them from csv
        try {
            SaveFile.AvtionCardsRead();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return Actions;

    }

    //transform Into usable.

}
