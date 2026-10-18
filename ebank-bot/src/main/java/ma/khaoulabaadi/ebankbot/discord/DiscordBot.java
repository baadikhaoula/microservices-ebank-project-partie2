package ma.khaoulabaadi.ebankbot.discord;

import com.zgamelogic.discord.annotations.DiscordController;
import com.zgamelogic.discord.annotations.DiscordMapping;
import ma.khaoulabaadi.ebankbot.agents.EbankAIAgent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.ai.chat.prompt.Prompt;

@DiscordController
public class DiscordBot {
    private EbankAIAgent ebankAiAgent;

    public DiscordBot(EbankAIAgent ebankAiAgent) {
        this.ebankAiAgent = ebankAiAgent;
    }
}
    @DiscordMapping
    private void perform(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String query = event.getMessage().getContentRaw();
        String response = ebankAIAgent.chat(query);
        event.getChannel().sendMessage(response).queue();
    }
}