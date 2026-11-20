package cn.lethekk.ragqaservice.model.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessage {

    private String historyId;

    private String conversationId;

    private Long sequenceId;

    private Integer userId;

    private String content;

    private String role;

    private LocalDateTime timestamp;

}
