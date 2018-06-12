package org.telegram;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SaveFile {


    public static HashMap<Integer, ArrayList<Long>> hashMap = new HashMap<Integer, ArrayList<Long>>();

    private static final String COMMA_DELIMITER = ";";

    private static final int ID_IDX = 1;

    private int removeCount = 0;

    private static final String Member_SAVE_FILE = "C:\\Users\\EPB\\IdeaProjects\\WGGames\\Member.csv";
    private static final String AVTION_CARDS_SAVE_FILE = "C:\\Users\\EPB\\IdeaProjects\\TelegramBotsExample-master\\ActionCards.csv";

    public static void readCSV(long chat_id) throws FileNotFoundException {


        BufferedReader fileReader = null;
        try {




            String line = "";
            //return array

            Member[] members = new Member[4];


            //create file reader
            fileReader = new BufferedReader(new FileReader(Member_SAVE_FILE));

            //skip first row
            fileReader.readLine();
            while ((line = fileReader.readLine()) != null) {

                //Get all tokens available in line

                String[] tokens = line.split(COMMA_DELIMITER);

                if(Long.parseLong(tokens[1]) == chat_id){
                    String[] res = new String[tokens.length];
                    for(int i = 0; i < tokens.length; i++){
                        res[i] = tokens[i];
                    }
                     Basis.current = res;
                }


            }


        }catch(Exception e){
            System.out.println("Error in SAVEFILE");
            e.printStackTrace();
        }finally{
            try {
                fileReader.close();
            }catch(Exception e){
                System.out.println("Error when closing");
                e.printStackTrace();
            }
        }
    }

    public static void AvtionCardsRead() throws FileNotFoundException{

        String csvFile = AVTION_CARDS_SAVE_FILE;
        String line = "";
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] card = line.split(cvsSplitBy);



                if(!(card[0].equals("X"))){


                    ActionCards.Actions.put(card[0],card[1]);
                }



            }

            System.out.println(ActionCards.Actions);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }







    public static void saveToCSV(long id){

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(Member_SAVE_FILE,true);
            fileWriter.append(String.valueOf(id));
            //fileWriter.append(COMMA_DELIMITER);
            fileWriter.append("\n");

        }catch(IOException e){
            System.out.println("Error when appending");
            e.printStackTrace();
        }finally{
            try {
                fileWriter.flush();
                fileWriter.close();
            }catch(IOException e) {
                System.out.println("Error when closing");
                e.printStackTrace();
            }
        }


    }

    public static void saveToCSV_Stats(String time, int lengtID, int whereCount, int menuCount, int shareCount, int updateCount, int exceptionCount, int removeCount){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(Member_SAVE_FILE,true);



        }catch(IOException e){
            System.out.println("Error when appending");
            e.printStackTrace();
        }finally{
            try {
                fileWriter.flush();
                fileWriter.close();
            }catch(IOException e) {
                System.out.println("Error when closing");
                e.printStackTrace();
            }
        }
    }

    public static void removeLine(long chatID){
        try {
            CSVReader reader2 = new CSVReader(new FileReader(Member_SAVE_FILE));
            List<String[]> allElements = reader2.readAll();

            for(int i = 1; i<allElements.size(); i++){

                if(chatID== Long.valueOf(allElements.get(i)[0])){
                    allElements.remove(i);
                    //System.out.println("removeLine: " +i);

                }
            }
            FileWriter sw = new FileWriter(Member_SAVE_FILE);
            CSVWriter writer = new CSVWriter(sw);

            writer.writeAll(allElements,false);
            writer.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e1){
            e1.printStackTrace();
        }


        System.out.println("Removed: " + chatID);

    }


}
