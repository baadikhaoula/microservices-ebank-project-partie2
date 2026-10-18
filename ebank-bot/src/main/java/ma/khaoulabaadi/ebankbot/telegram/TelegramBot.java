package ma.khaoulabaadi.ebankbot.telegram;

import jakarta.annotation.PostConstruct;
import ma.khaoulabaadi.ebankbot.agents.EbankAIAgent;
import org.springframework.beans.factory.annotation.Value;  // ✅ Correction 1
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private String telegramBotToken;
    private final EbankAIAgent aiAgent;

    public TelegramBot(@Value("${telegram.token}") String telegramBotToken, EbankAIAgent aiAgent) {
        super(telegramBotToken);
        this.telegramBotToken = telegramBotToken;
        this.aiAgent = aiAgent;
    }

    @Override
    public String getBotUsername() {
        return "EbankBot";
    }

    @PostConstruct
    public void registerTelegramBot() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (!update.hasMessage() || !update.getMessage().hasText()) return;

            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            sendTyping(chatId);

            String answer = aiAgent.chat(messageText);

            sendText(chatId, answer);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendTyping(Long chatId) {
        SendChatAction action = new SendChatAction();
        action.setChatId(chatId.toString());
        action.setAction(ActionType.TYPING);
        try {
            execute(action);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendText(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}