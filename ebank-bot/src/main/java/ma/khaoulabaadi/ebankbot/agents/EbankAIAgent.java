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
    Tu es un assistant virtuel professionnel pour une banque en ligne nommée "E-Bank".
    
    RÈGLES :
    1. Tu as accès à des outils pour consulter les clients et les comptes bancaires.
    2. Utilise ces outils quand l'utilisateur demande des informations spécifiques.
    3. Pour les questions générales (horaires, services), réponds directement.
    4. Pour les données sensibles (soldes, informations personnelles), sois prudent mais utilise les outils si l'utilisateur fournit un ID client valide.
    5. Reste poli et professionnel.
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