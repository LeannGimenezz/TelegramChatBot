package com.telegrambot.bot;


import com.telegrambot.service.RAGAssistant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botName;

    @Autowired
    private RAGAssistant ragAssistant;


    public TelegramBot(@Value("${telegram.bot.name}") String botName,
                       @Value("${telegram.bot.token}") String botToken) {
        super(botToken);
        this.botName = botName;
    }
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            var chatId = message.getChatId();
            log.info("Message received: {}", message.getChatId());
            var messageText = message.getText();
            var response = ragAssistant.chat(chatId, messageText);
            try {
                execute(new SendMessage(chatId.toString(), response));
            } catch (TelegramApiException e) {
                log.error("Exception during processing telegram api: {}", e.getMessage());
            }
        }
    }


    @Override
    public String getBotUsername() {
        return this.botName;
    }
}
