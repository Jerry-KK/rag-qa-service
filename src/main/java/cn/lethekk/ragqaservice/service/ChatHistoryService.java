package cn.lethekk.ragqaservice.service;

import cn.lethekk.ragqaservice.adapter.outbound.repository.ChatHistoryRepository;
import cn.lethekk.ragqaservice.model.domain.ChatMessage;
import cn.lethekk.ragqaservice.model.domain.Conversation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Lethekk
 * @Date 2026/11/20 12:46
 */
@AllArgsConstructor
@Slf4j
@Service
public class ChatHistoryService {

    private static final int TITLE_MAX_LENGTH = 64;

    private final ChatHistoryRepository chatHistoryRepository;

    //获取全部会话消息
    public List<ChatMessage> listMessages(String conversationId, Integer userId) {
        if (!isOwner(conversationId, userId)) {
            return List.of();
        }
        return chatHistoryRepository.findMessagesByConversationId(conversationId);
    }

    //删除会话
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConversation(String conversationId, Integer userId) {
        if (!isOwner(conversationId, userId)) {
            return false;
        }
        chatHistoryRepository.deleteMessagesByConversationId(conversationId);
        return chatHistoryRepository.deleteConversationById(conversationId);
    }

    //获取会话列表
    public List<Conversation> listConversations(Integer userId) {
        return chatHistoryRepository.findConversationsByUserId(userId);
    }

    //修改会话标题
    public boolean updateTitle(String conversationId, Integer userId, String title) {
        if (!isOwner(conversationId, userId)) {
            return false;
        }
        return chatHistoryRepository.updateConversationTitle(conversationId, truncateTitle(title));
    }

    private boolean isOwner(String conversationId, Integer userId) {
        return chatHistoryRepository.findConversationById(conversationId)
                .map(conversation -> conversation.isOwnedBy(userId))
                .orElse(false);
    }

    private String truncateTitle(String text) {
        return text.length() <= TITLE_MAX_LENGTH ? text : text.substring(0, TITLE_MAX_LENGTH);
    }

}
