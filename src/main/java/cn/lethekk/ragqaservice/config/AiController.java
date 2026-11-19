package cn.lethekk.ragqaservice.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Lethekk
 * @Date 2026/11/18 0:10
 */
@Configuration
public class AiController {

    @Bean(name = "chatMemorySummarizer")
    public ChatMemory chatMemorySummarizer(ChatMemoryRepository repository, ChatModel chatModel) {
        ChatClient summarizerChatClient = ChatClient.builder(chatModel).build();
        //return new SummaryChatMemory(repository, summarizerChatClient, 40, 12);
        return new SummaryChatMemory(repository, summarizerChatClient, 8, 4);
    }

    @Bean(name = "chatMemoryClient")
    public ChatClient chatMemoryClient(ChatClient.Builder builder, @Qualifier("chatMemorySummarizer") ChatMemory chatMemory) {
        return builder
                .defaultSystem("你是公司的差旅制度助手，只依据制度原文回答。")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

}
