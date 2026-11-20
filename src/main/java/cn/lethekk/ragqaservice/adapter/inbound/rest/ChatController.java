package cn.lethekk.ragqaservice.adapter.inbound.rest;

import cn.lethekk.ragqaservice.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Lethekk
 * @Date 2026/11/18 0:02
 */
@RequestMapping("/chat")
@RestController
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/ask")
    public String ask(@RequestParam String question, @RequestParam Integer userId, @RequestParam String sessionId) {
        return chatService.ask(question, userId, sessionId);
    }

}
