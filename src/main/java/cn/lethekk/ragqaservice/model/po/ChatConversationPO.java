package cn.lethekk.ragqaservice.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author Lethekk
 * @Date 2026/11/20 13:30
 * 会话表实体
 * 对应表：chat_conversation
 */
@Data
@TableName("chat_conversation")
public class ChatConversationPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    @TableId(type = IdType.INPUT)
    private String conversationId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
