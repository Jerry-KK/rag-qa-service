package cn.lethekk.ragqaservice.adapter.outbound.repository;

import cn.lethekk.ragqaservice.dao.ChatConversationMapper;
import cn.lethekk.ragqaservice.dao.ChatHistoryMapper;
import cn.lethekk.ragqaservice.model.domain.ChatMessage;
import cn.lethekk.ragqaservice.model.domain.Conversation;
import cn.lethekk.ragqaservice.model.po.ChatConversationPO;
import cn.lethekk.ragqaservice.model.po.ChatHistoryPO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ChatHistoryRepositoryImpl implements ChatHistoryRepository {

    private final ChatConversationMapper conversationMapper;
    private final ChatHistoryMapper historyMapper;

    @Override
    public Optional<Conversation> findConversationById(String conversationId) {
        return Optional.ofNullable(conversationMapper.selectById(conversationId)).map(this::toDomain);
    }

    @Override
    public List<Conversation> findConversationsByUserId(Integer userId) {
        return conversationMapper.selectList(new LambdaQueryWrapper<ChatConversationPO>()
                        .eq(ChatConversationPO::getUserId, userId)
                        .orderByDesc(ChatConversationPO::getCreateTime))
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ChatMessage> findMessagesByConversationId(String conversationId) {
        return historyMapper.selectList(new LambdaQueryWrapper<ChatHistoryPO>()
                        .eq(ChatHistoryPO::getConversationId, conversationId)
                        .orderByAsc(ChatHistoryPO::getSequenceId))
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public long findMaxSequenceId(String conversationId) {
        ChatHistoryPO last = historyMapper.selectOne(new LambdaQueryWrapper<ChatHistoryPO>()
                .eq(ChatHistoryPO::getConversationId, conversationId)
                .orderByDesc(ChatHistoryPO::getSequenceId)
                .last("LIMIT 1"));
        return last == null ? 0L : last.getSequenceId();
    }

    @Override
    public void saveConversation(Conversation conversation) {
        conversationMapper.insert(toPO(conversation));
    }

    @Override
    public void saveMessages(List<ChatMessage> messages) {
        messages.forEach(message -> historyMapper.insert(toPO(message)));
    }

    @Override
    public boolean updateConversationTitle(String conversationId, String title) {
        return conversationMapper.update(null, new LambdaUpdateWrapper<ChatConversationPO>()
                .eq(ChatConversationPO::getConversationId, conversationId)
                .set(ChatConversationPO::getTitle, title)) > 0;
    }

    @Override
    public void deleteMessagesByConversationId(String conversationId) {
        historyMapper.delete(new LambdaQueryWrapper<ChatHistoryPO>()
                .eq(ChatHistoryPO::getConversationId, conversationId));
    }

    @Override
    public boolean deleteConversationById(String conversationId) {
        return conversationMapper.deleteById(conversationId) > 0;
    }

    private Conversation toDomain(ChatConversationPO po) {
        Conversation conversation = new Conversation();
        conversation.setConversationId(po.getConversationId());
        conversation.setUserId(po.getUserId());
        conversation.setTitle(po.getTitle());
        conversation.setCreateTime(po.getCreateTime());
        return conversation;
    }

    private ChatMessage toDomain(ChatHistoryPO po) {
        ChatMessage message = new ChatMessage();
        message.setHistoryId(po.getHistoryId());
        message.setConversationId(po.getConversationId());
        message.setSequenceId(po.getSequenceId());
        message.setUserId(po.getUserId());
        message.setContent(po.getContent());
        message.setRole(po.getType());
        message.setTimestamp(po.getTimestamp());
        return message;
    }

    private ChatConversationPO toPO(Conversation conversation) {
        ChatConversationPO po = new ChatConversationPO();
        po.setConversationId(conversation.getConversationId());
        po.setUserId(conversation.getUserId());
        po.setTitle(conversation.getTitle());
        po.setCreateTime(conversation.getCreateTime());
        return po;
    }

    private ChatHistoryPO toPO(ChatMessage message) {
        ChatHistoryPO po = new ChatHistoryPO();
        po.setHistoryId(message.getHistoryId());
        po.setConversationId(message.getConversationId());
        po.setSequenceId(message.getSequenceId());
        po.setUserId(message.getUserId());
        po.setContent(message.getContent());
        po.setType(message.getRole());
        po.setTimestamp(message.getTimestamp());
        return po;
    }

}
