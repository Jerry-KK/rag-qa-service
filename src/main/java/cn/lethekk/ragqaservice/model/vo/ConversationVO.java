package cn.lethekk.ragqaservice.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConversationVO {

    private String conversationId;

    private String title;

    private LocalDateTime createTime;

}
