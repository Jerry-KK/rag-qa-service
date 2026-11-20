package cn.lethekk.ragqaservice.model.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Conversation {

    private String conversationId;

    private Integer userId;

    private String title;

    private LocalDateTime createTime;

    public boolean isOwnedBy(Integer userId) {
        return this.userId.equals(userId);
    }

}
