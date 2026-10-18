package ma.khaoulabaadi.ebankbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class EbankAIAgent {

    private final ChatClient chatClient;

    public EbankAIAgent(ChatClient.Builder chatClientBuilder,
                        ToolCallbackProvider toolCallbackProvider) {

        // ✅ Prompt système naturel et équilibré
        String systemPrompt = """
                Tu es un assistant virtuel amical et professionnel pour une banque en ligne appelée "E-Bank".
                
                Ton rôle :
                - Accueillir les clients chaleureusement
                - Répondre à leurs questions sur les services bancaires
                - Les aider à consulter leurs informations (comptes, transactions, etc.)
                
                Quand un client te salue (Bonjour, Salut, etc.), réponds simplement de manière courtoise.
                Quand il pose une question, utilise les outils disponibles si nécessaire pour trouver l'information.
                Si tu ne sais pas, dis-le honnêtement et propose de l'aider autrement.
                
                Sois toujours poli, utile et professionnel.
                """;

        this.chatClient = chatClientBuilder
                .defaultSystem(systemPrompt)
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .defaultTools(toolCallbackProvider)
                .build();
    }

    public String chat(String query) {
        return chatClient.prompt()
                .user(query)
                .call()
                .content();
    }
}