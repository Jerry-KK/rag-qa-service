package cn.lethekk.ragqaservice.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageVO {

    private String historyId;

    private String conversationId;

    private Long sequenceId;

    private String content;

    private String role;

    private LocalDateTime timestamp;

}
