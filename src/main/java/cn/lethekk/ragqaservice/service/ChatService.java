package cn.lethekk.ragqaservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
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
    private final ChatHistoryService chatHistoryService;

    public String ask(String question, Integer userId, String sessionId) {

        String conversationId = userId + sessionId;  //会话ID
        String answer = chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text("请回答：{question}").param("question", question))
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
        chatHistoryService.saveRound(conversationId, userId, question, answer);
        return answer;
    }

}
