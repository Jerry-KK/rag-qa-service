package cn.lethekk.ragqaservice.adapter.inbound.rest;

import cn.lethekk.ragqaservice.model.domain.ChatMessage;
import cn.lethekk.ragqaservice.model.domain.Conversation;
import cn.lethekk.ragqaservice.model.vo.ChatMessageVO;
import cn.lethekk.ragqaservice.model.vo.ConversationVO;
import cn.lethekk.ragqaservice.service.ChatHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Lethekk
 * @Date 2026/11/19 14:09
 */
//聊天历史
@RequestMapping("/chat/history")
@RestController
@AllArgsConstructor
public class ChatHistoryController {

    private final ChatHistoryService service;

    //获取用户对话列表
    @GetMapping("/conversations")
    public List<ConversationVO> getConversationList(@RequestParam Integer userId) {
        return service.listConversations(userId).stream().map(this::toVO).toList();
    }

    //获取指定会话的全部消息
    @GetMapping("/conversations/{conversationId}/messages")
    public List<ChatMessageVO> getMessages(@PathVariable String conversationId, @RequestParam Integer userId) {
        return service.listMessages(conversationId, userId).stream().map(this::toVO).toList();
    }

    //修改会话标题
    @PutMapping("/conversations/{conversationId}/title")
    public boolean updateTitle(@PathVariable String conversationId, @RequestParam Integer userId,
                               @RequestParam String title) {
        return service.updateTitle(conversationId, userId, title);
    }

    //删除指定会话
    @DeleteMapping("/conversations/{conversationId}")
    public boolean delConversation(@PathVariable String conversationId, @RequestParam Integer userId) {
        return service.deleteConversation(conversationId, userId);
    }

    private ConversationVO toVO(Conversation conversation) {
        ConversationVO vo = new ConversationVO();
        vo.setConversationId(conversation.getConversationId());
        vo.setTitle(conversation.getTitle());
        vo.setCreateTime(conversation.getCreateTime());
        return vo;
    }

    private ChatMessageVO toVO(ChatMessage message) {
        ChatMessageVO vo = new ChatMessageVO();
        vo.setHistoryId(message.getHistoryId());
        vo.setConversationId(message.getConversationId());
        vo.setSequenceId(message.getSequenceId());
        vo.setContent(message.getContent());
        vo.setRole(message.getRole());
        vo.setTimestamp(message.getTimestamp());
        return vo;
    }

}
