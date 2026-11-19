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

    public String ask(String question, String userId, String sessionId) {

        //会话ID由服务端拼出来，不接受客户端直接传
        //String conversationId = user.getId() + session.getId();
        String conversationId = userId + sessionId;  //这里演示不规范，直接从参数取了。

        return chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text("请回答：{question}").param("question", question))
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

}
