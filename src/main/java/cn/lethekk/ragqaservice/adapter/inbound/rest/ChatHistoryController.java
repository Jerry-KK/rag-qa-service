package cn.lethekk.ragqaservice.adapter.inbound.rest;

import cn.lethekk.ragqaservice.model.po.ChatConversationPO;
import cn.lethekk.ragqaservice.model.po.ChatHistoryPO;
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
    public List<ChatConversationPO> getConversationList(@RequestParam Integer userId) {
        return service.listConversations(userId);
    }

    //获取指定会话的全部消息
    @GetMapping("/conversations/{conversationId}/messages")
    public List<ChatHistoryPO> getMessages(@PathVariable String conversationId, @RequestParam Integer userId) {
        return service.listMessages(conversationId, userId);
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

}
