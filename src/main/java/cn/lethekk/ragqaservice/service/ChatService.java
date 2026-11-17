package cn.lethekk.ragqaservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * @Author Lethekk
 * @Date 2026/11/18 0:15
 */
@AllArgsConstructor
@Slf4j
@Service
public class ChatService {

    private final ChatClient chatClient;

    public String ask(String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }

}
