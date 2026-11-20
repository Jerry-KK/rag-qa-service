package cn.lethekk.ragqaservice.service;

import cn.lethekk.ragqaservice.dao.ChatConversationMapper;
import cn.lethekk.ragqaservice.dao.ChatHistoryMapper;
import cn.lethekk.ragqaservice.model.po.ChatConversationPO;
import cn.lethekk.ragqaservice.model.po.ChatHistoryPO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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

    private final ChatConversationMapper conversationMapper;
    private final ChatHistoryMapper historyMapper;

    //获取全部会话消息
    public List<ChatHistoryPO> listMessages(String conversationId, Integer userId) {
        if (!isOwner(conversationId, userId)) {
            return List.of();
        }
        return historyMapper.selectList(new LambdaQueryWrapper<ChatHistoryPO>()
                .eq(ChatHistoryPO::getConversationId, conversationId)
                .orderByAsc(ChatHistoryPO::getSequenceId));
    }

    //删除会话
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConversation(String conversationId, Integer userId) {
        if (!isOwner(conversationId, userId)) {
            return false;
        }
        historyMapper.delete(new LambdaQueryWrapper<ChatHistoryPO>()
                .eq(ChatHistoryPO::getConversationId, conversationId));
        return conversationMapper.deleteById(conversationId) > 0;
    }

    //获取会话列表
    public List<ChatConversationPO> listConversations(Integer userId) {
        return conversationMapper.selectList(new LambdaQueryWrapper<ChatConversationPO>()
                .eq(ChatConversationPO::getUserId, userId)
                .orderByDesc(ChatConversationPO::getCreateTime));
    }

    //修改会话标题
    public boolean updateTitle(String conversationId, Integer userId, String title) {
        return conversationMapper.update(null, new LambdaUpdateWrapper<ChatConversationPO>()
                .eq(ChatConversationPO::getConversationId, conversationId)
                .eq(ChatConversationPO::getUserId, userId)
                .set(ChatConversationPO::getTitle, truncateTitle(title))) > 0;
    }

    private boolean isOwner(String conversationId, Integer userId) {
        ChatConversationPO conversation = conversationMapper.selectById(conversationId);
        return conversation != null && userId.equals(conversation.getUserId());
    }

    private String truncateTitle(String text) {
        return text.length() <= TITLE_MAX_LENGTH ? text : text.substring(0, TITLE_MAX_LENGTH);
    }

}
