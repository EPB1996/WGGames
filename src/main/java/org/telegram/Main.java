package org.telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;
import org.telegram.telegrambots.logging.BotsFileHandler;
import org.telegram.updateshandlers.ChannelHandlers;
import org.telegram.updateshandlers.CommandsHandler;
import org.telegram.updateshandlers.DirectionsHandlers;
import org.telegram.updateshandlers.ElektrollArtFanHandler;
import org.telegram.updateshandlers.FilesHandlers;
import org.telegram.updateshandlers.RaeHandlers;
import org.telegram.updateshandlers.TransifexHandlers;
import org.telegram.updateshandlers.WeatherHandlers;
import org.telegram.updateshandlers.WebHookExampleHandlers;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief Main class to create all bots
 * @date 20 of June of 2015
 */
public class Main {
    private static final String LOGTAG = "MAIN";

    public static void main(String[] args) {

        try {
            ApiContextInitializer.init();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            try {
                // Register long polling bots. They work regardless type of TelegramBotsApi we are creating
                telegramBotsApi.registerBot(new Basis());


            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }


}
