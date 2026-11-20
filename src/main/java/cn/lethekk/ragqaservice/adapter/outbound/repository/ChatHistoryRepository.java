package cn.lethekk.ragqaservice.adapter.outbound.repository;

import cn.lethekk.ragqaservice.model.domain.ChatMessage;
import cn.lethekk.ragqaservice.model.domain.Conversation;

import java.util.List;
import java.util.Optional;

public interface ChatHistoryRepository {

    Optional<Conversation> findConversationById(String conversationId);

    List<Conversation> findConversationsByUserId(Integer userId);

    List<ChatMessage> findMessagesByConversationId(String conversationId);

    long findMaxSequenceId(String conversationId);

    void saveConversation(Conversation conversation);

    void saveMessages(List<ChatMessage> messages);

    boolean updateConversationTitle(String conversationId, String title);

    void deleteMessagesByConversationId(String conversationId);

    boolean deleteConversationById(String conversationId);

}
