package org.telegram;

import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendLocation;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
//import org.telegram.abilitybots.api.bot.AbilityBot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MyAmazingBot extends TelegramLongPollingBot   {

    @Override
    public void onUpdateReceived(Update update) {
        // TODO: ADD DB

        long[] id = {198057550,-307410721};

        //GET ID
       long chat_id = update.getMessage().getChatId();

        //TODO: ADMIN STUFF
        if(update.getMessage().getFrom().getId() == 198057550 && update.hasMessage() && update.getMessage().hasLocation()) {

            //check if message is location

                // Set variables
                float lat = update.getMessage().getLocation().getLatitude();
                float lon = update.getMessage().getLocation().getLongitude();



                try {
                    sendLocationOnUpdate(id[0], lat, lon);
                } catch (TelegramApiException e) {
                    e.printStackTrace();

                }

                try {
                    sendLocationOnUpdate(id[1], lat, lon);
                } catch (TelegramApiException e) {
                    e.printStackTrace();

                }



        }

        //TODO: NORMAL USER STUFF
        else {
            //COMMANDS
            if(update.getMessage().getText().equals("/commands")){
                //TODO: Add other commands
                String[] comd = {"/where - Get Location", "/menu - Get Menu"};
                for(int i = 0; i < comd.length; i++) {
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id)
                            .setText(comd[i]);
                    try {
                        sendMessage(message); // Sending our message object to user
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }


            }
            //WHERE
            if (update.getMessage().getText().equals("/where")) {
                float la = (float) 47.498308;
                float lo = (float) 8.728309;



                try {
                    sendLocationOnUpdate(chat_id, la, lo);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            //MENU
            if (update.getMessage().getText().equals("/menu")){

                File file = new File("C:\\Users\\EPB\\IdeaProjects\\FrauHund\\menu.pdf");
                // System.out.println("Chatid:" +ctx.chatId().toString());
                try {
                    sendDocUploadingAFile(chat_id,file,"En Guetä");
                }catch(TelegramApiException e){
                    e.printStackTrace();
                }
            }


        }

    }

    @Override
    public String getBotUsername() {
        // TODO
        return "FrauHund_bot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "562085965:AAEZaF28tQBe4uuknzUHZCVRwdLSroAFQDc" ;
    }

    public void sendDocUploadingAFile(Long chatId, File save, String caption) throws TelegramApiException {

        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(chatId);
        sendDocumentRequest.setNewDocument(save);
        sendDocumentRequest.setCaption(caption);
        sendDocument(sendDocumentRequest);
    }

    //helper to send location
    public void sendLocationOnUpdate(Long chatId, float latitude,float longitude) throws TelegramApiException{

        SendLocation sendLocationRequest = new SendLocation();
        sendLocationRequest.setChatId(chatId);
        sendLocationRequest.setLatitude(latitude);
        sendLocationRequest.setLongitude(longitude);
        sendLocation(sendLocationRequest);

    }

    //keyboardlayout
    /*
    private static ReplyKeyboardMarkup getSettingsKeyboard(String language) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);


        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(getLanguagesCommand(language));
        keyboardFirstRow.add(getUnitsCommand(language));
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(getAlertsCommand(language));
        keyboardSecondRow.add(getBackCommand(language));
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        keyboardFirstRow.add(L)
        return replyKeyboardMarkup;
    }
    */
}