package org.telegram;



import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.FileNotFoundException;
import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class Basis extends TelegramLongPollingBot {

    public static String[] current;
    public static Boolean Request_FLAG = FALSE;
    public static Boolean VOTE_OK = FALSE;
    public static Boolean POINTS_FLAG = FALSE;
    private static Boolean DENIED = FALSE;


    private static int ACC_COUNT = 0;
    private static int DE_COUNT = 0;

    private static int C1 = 0;
    private static int C2 = 0;
    private static int C3 = 0;

    private static final long GROUP_ID = -282214764;
    private static final long OP = 198057550;

    private  long[] AL_VOTED = {OP,208639675};
    private  long[] AL_VOTED_S = {OP,208639675};

    private HashMap<Integer, Long> CHECKER = new HashMap<>();
    private HashMap<Long, String> REQUEST = new HashMap<>();

    private Integer z = 0;



    @Override
    public void onUpdateReceived(Update update) {


        //Update fields:

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {


            String Update = update.getMessage().getText();
            Integer ID = update.getMessage().getFrom().getId();
            long chat_id = update.getMessage().getChatId();


            System.out.println(update.getMessage().getFrom().getId());
            System.out.println(chat_id);
            //TODO: Admin stuff
            if (validateID(ID)) {
                if (Update.equals("/op")) {
                    String message_text = "Welcome OP!";


                    SendMessage message = new SendMessage()
                            .setChatId(chat_id)
                            .setText(message_text);
                    try {
                        sendMessage(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }

            //TODO: Other stuff
            //Menu
            if (Update.equals("/menu")) {

                if(chat_id == GROUP_ID){

                    String[] items = {"Now", "Whats coming", "Gamble"};

                    SendMessage message = new SendMessage()
                            .setChatId(update.getMessage().getChatId());

                    message.enableMarkdown(true);
                    message.setReplyMarkup(getSettingsKeyboard(items));
                    message.setReplyToMessageId(message.getReplyToMessageId());
                    message.setChatId(chat_id);
                    message.setText("SAY WHAT: ");

                    try {
                        sendMessage(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                }else {


                    String[] items = {"Now", "Points", "Request", "Whats coming","Gamble"};

                    SendMessage message = new SendMessage()
                            .setChatId(update.getMessage().getChatId());

                    message.enableMarkdown(true);
                    message.setReplyMarkup(getSettingsKeyboard(items));
                    message.setReplyToMessageId(message.getReplyToMessageId());
                    message.setChatId(chat_id);
                    message.setText("Now - To be collected \n" +
                            "Points - Get your points \n" +
                            "Request - Request voting \n" +
                            "Whats coming - Stuff that can happen in the near future \n"+
                            "Gamble - Only if Request gets denied");

                    try {
                        sendMessage(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }


            if(Update.equals("test")){
                //Get the ActionCards
                getReward(3);


            }


            if(Update.equals("Gamble")){
                if(DENIED && chat_id == Long.parseLong( String.valueOf(REQUEST.keySet().toArray()[0]))){
                    deniedVoting();
                    DENIED = FALSE;
                }else if (!(chat_id == Long.parseLong( String.valueOf(REQUEST.keySet().toArray()[0])))){
                    SendMessage msg = new SendMessage();
                    msg.setChatId(chat_id);
                    msg.setText("You have no rights to invoke the Gamble Game!");
                    try {
                        sendMessage(msg);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }


            //Points
            if (Update.equals("Points") && chat_id != GROUP_ID) {

                //TODO: Check for UserID in csv and give current points.

                getPointsWithID(chat_id);

            }

            if (Update.equals("Request")) {

                //set WHO
                REQUEST.put(chat_id,update.getMessage().getFrom().getFirstName());


                //TODO: Check for UserID in csv and give current points.
                if(Request_FLAG || POINTS_FLAG){
                    SendMessage msg = new SendMessage(); // Create a message object object
                    msg.setText("Theres already a Request/Voting in progress. Please be patient...");
                    msg.setChatId(chat_id);
                    try {
                        sendMessage(msg); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else{
                    Request_FLAG = TRUE;
                    sendRequest(chat_id);
                }



            }
        }
        //start of photo
        else if (update.hasMessage() && update.getMessage().hasPhoto()) {

        }
        //callback
        else if (update.hasCallbackQuery()) {
            //requests
            if (Request_FLAG) {
                //validation for no double votes!

                if(CHECKER.containsValue(update.getCallbackQuery().getMessage().getChatId())){
                    VOTE_OK = FALSE;
                }else{
                    z++;
                    CHECKER.put(z,update.getCallbackQuery().getMessage().getChatId());
                    VOTE_OK =TRUE;
                }
                System.out.println(CHECKER);
                //IF not Voted befor: VOTEPROCESS


                if (VOTE_OK) {
                    if (update.getCallbackQuery().getData().equals("Request accepted")) {
                        ACC_COUNT++;
                        System.out.println(ACC_COUNT);

                        SendMessage msg = new SendMessage();
                        msg.setChatId(update.getCallbackQuery().getMessage().getChatId());
                        msg.setText("Thanks for the vote!");
                        try {
                            sendMessage(msg);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }

                        checkFlag();
                    } else {
                        DE_COUNT++;
                        System.out.println(DE_COUNT);
                        checkFlag();


                    }
                }
                //If voted befor send message
                else {
                    SendMessage msg = new SendMessage();
                    msg.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    msg.setText("YOU BAD BOY! You've already voted! ");
                    try {
                        sendMessage(msg);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

            }

            //React to Pointsvote
            if(POINTS_FLAG){

                if(CHECKER.containsValue(update.getCallbackQuery().getMessage().getChatId())){
                    VOTE_OK = FALSE;
                }else{
                    z++;
                    CHECKER.put(z,update.getCallbackQuery().getMessage().getChatId());
                    VOTE_OK =TRUE;
                }
                System.out.println(CHECKER);

                if (VOTE_OK) {
                    if (update.getCallbackQuery().getData().equals("1")) {

                        C1++;

                        SendMessage msg = new SendMessage();
                        msg.setChatId(update.getCallbackQuery().getMessage().getChatId());
                        msg.setText("Thanks for the vote!");
                        try {
                            sendMessage(msg);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        checkPoints();



                    }
                    if (update.getCallbackQuery().getData().equals("2")) {

                        C2++;

                        SendMessage msg = new SendMessage();
                        msg.setChatId(update.getCallbackQuery().getMessage().getChatId());
                        msg.setText("Thanks for the vote!");
                        try {
                            sendMessage(msg);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        checkPoints();

                    }
                    if (update.getCallbackQuery().getData().equals("3")) {

                        C3++;

                        SendMessage msg = new SendMessage();
                        msg.setChatId(update.getCallbackQuery().getMessage().getChatId());
                        msg.setText("Thanks for the vote!");
                        try {
                            sendMessage(msg);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }

                        checkPoints();

                    }
                    //If not all voted:

                }else{
                    SendMessage msg = new SendMessage();
                    msg.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    msg.setText("NO DOUBLE VOTING! \n"+
                            "Still " + (2 - C1 - C2 - C3) + " votes needed!");
                    try {
                        sendMessage(msg);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

            }




        }

    }




    //Requeststeller gets this
    //TODO: Implement Account!
    private void getReward(int max){

        SendMessage msg = new SendMessage();
        msg.setChatId(GROUP_ID);
        msg.setText("MAX: "+ max);
        try {
            sendMessage(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        //Get ActionCards:
        HashMap<String,String> cards = ActionCards.getCards();
        //Make elements out of the cards

        Object[] set_X = cards.keySet().toArray();

        Object[] set_Avtion =cards.values().toArray();


        //Put them all together in a string[]
        String[] set_complete = new String[5];
        for(int i = 0; i < set_Avtion.length; i++){
            set_complete[i] = String.valueOf(set_X[i]) + String.valueOf(set_Avtion[i]);
        }

        System.out.println(set_complete);
        //TODO: Select random 3 cards and then one "max"- out of this!!
        int rnd = new Random().nextInt(set_complete.length);
        getRnd();

    }

    private String[] getRnd(){

        return null;
    }

    //Check logic behind voting
    private void checkPoints(){
        int gesamt = C1+C2+C3;

        System.out.println("CHECKPOINTS: " + AL_VOTED_S.length);
        System.out.println("C1: " + C1);
        System.out.println("C2: " + C2);
        System.out.println("C3: " +C3);

        if(AL_VOTED_S.length < gesamt) {

            SendMessage msg = new SendMessage();
            msg.setChatId(GROUP_ID);
            msg.setText("Still " + (2 - C1 - C2 - C3) + " votes needed!");
            try {
                sendMessage(msg);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }else if(gesamt == AL_VOTED_S.length){
            //Get the one random spot with most votes
            int p=0;
            if(C1 > C2 && C1 > C3){p = 1;}
            if(C2 > C1 && C2 > C3){p = 2;}
            if(C3 > C2 && C3 > C1){p = 3;}

            resetCounter();
            getReward(p);

        }
    }

    //FlagValidation
    private void checkFlag(){
        int gesamt = ACC_COUNT + DE_COUNT;
        SendMessage msg = new SendMessage();
        //TODO: Set that result is postet in group
        msg.setChatId(GROUP_ID);

        msg.setText("We need more votes!");
        if(ACC_COUNT == 2){

            msg.setText("Voting over: \n " +
                    "Accepted: " + ACC_COUNT + "\n" +
                    "Denied: " + DE_COUNT);
            try{
                sendMessage(msg);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }
            //reset ACC and DE COUNTER.
            resetCounter();
        }else if(DE_COUNT ==2){

            msg.setText("Voting over: \n " +
                    "Accepted: " + ACC_COUNT + "\n" +
                    "Denied: " + DE_COUNT + "\n \n" +
                    String.valueOf(REQUEST.values().toArray()[0]) + " can invoke the Gamble Game");
            //reset ACC and DE COUNTER.




            try{
                sendMessage(msg);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }

            //TODO: SEND TO REQUESTGUY, CHECK IF GAMBLE UPDATE IS FROM REQZEST GUY, AFTER GAMBLE DELET REQUEST GUY
            SendMessage message = new SendMessage();


            String ID = String.valueOf(REQUEST.keySet().toArray()[0]);
            message.setChatId(Long.parseLong(ID));
            message.setText("You can now invoke the Gamble Game!");

            try{
                sendMessage(message);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }

            resetCounter();
            //Invoke Denied Process

            DENIED = TRUE;


        }else if(gesamt == 3){

            msg.setText("Voting over: \n " +
                    "Accepted: " + ACC_COUNT + "\n" +
                    "Denied: " + DE_COUNT);
            //reset ACC and DE COUNTER.

            try{
                sendMessage(msg);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }
            resetCounter();
        }



    }

    //Denied Request procedure
    private void deniedVoting(){
        //Set points_flag
        POINTS_FLAG = TRUE;
        System.out.println("PROBLEM HIER?"+CHECKER);

        long[] sendToOthers = {198057550,208639675};

        SendMessage message = new SendMessage(); // Create a message object object
        message.setText("Choose wisely");

        //inline answer
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("1").setCallbackData("1"));
        rowInline.add(new InlineKeyboardButton().setText("2").setCallbackData("2"));
        rowInline.add(new InlineKeyboardButton().setText("3").setCallbackData("3"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        for(int i= 0; i< sendToOthers.length; i++) {
            message.setChatId(sendToOthers[i]);
            try {
                sendMessage(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        SendMessage msg = new SendMessage(); // Create a message object object
        msg.setText("RandomPoint System Invoked. Please give your vote!");
        msg.setChatId(GROUP_ID);
        try {
            sendMessage(msg); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //reset all the globals
    private void resetCounter(){

        Request_FLAG = FALSE;
        POINTS_FLAG = FALSE;
        //Reset the counter
        ACC_COUNT = 0;
        DE_COUNT = 0;
        //Reset the allowed voters
        AL_VOTED[0] = OP;
        AL_VOTED[1] = 208639675;

        AL_VOTED_S[0] = OP;
        AL_VOTED_S[1] = 208639675;

        C1 = 0;
        C2 = 0;
        C3 = 0;
        z=0;
        CHECKER = new HashMap<>();

        VOTE_OK = FALSE;

    }

    private void getPointsWithID(long id) {

        try {
            SaveFile.readCSV(id);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

                SendMessage msg = new SendMessage();
                msg.setChatId(id);
                msg.setText(current[0]  + "'s Punkte: \n"
                                        + "Gesamt: " + current[2] + "\n"
                                        + "Positiv: " + current[3] + "\n"
                                        + "Negativ: " + current[4] + "\n");
                try {
                    sendMessage(msg);
                }catch (TelegramApiException e){
                    e.printStackTrace();
                }

            //System.out.println(set[i]);


    }

    //Send a Request
    private void sendRequest(long chat_id) {
        //add other chat_IDs
        //TODO: get other chatids!
        long[] sendToOthers = {198057550,208639675};
        //invoke

        SendMessage message = new SendMessage() // Create a message object object
                .setText("You got a request");
        //inline answer
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Accept").setCallbackData("Request accepted"));
        rowInline.add(new InlineKeyboardButton().setText("Deny").setCallbackData("Request denied"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        for(int i= 0; i< sendToOthers.length; i++) {
             message.setChatId(sendToOthers[i]);
            try {
                sendMessage(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        SendMessage msg = new SendMessage(); // Create a message object object
        msg.setText("Request has been made. Please give your vote!");
        msg.setChatId(GROUP_ID);
        try {
            sendMessage(msg); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }




    //check for admin
    private boolean validateID(long chatID){

        if (chatID == OP) {
            return true;
        }else {
            return false;
        }
    }

    private static ReplyKeyboardMarkup getSettingsKeyboard(String[] items) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(false);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);


        List<KeyboardRow> keyboardRow = new ArrayList<>();
        KeyboardRow keyboard = new KeyboardRow();
        for (int i = 0; i < items.length; i++){
            keyboard.add(items[i]);
        }


        keyboardRow.add(keyboard);
        replyKeyboardMarkup.setKeyboard(keyboardRow);



        return replyKeyboardMarkup;
    }

    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "WGGames_bot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "602984031:AAFmNGNZFfzZ_Zc503XfdCTKIyDJEgxE_FA";
    }
}