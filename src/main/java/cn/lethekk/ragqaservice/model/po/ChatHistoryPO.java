package cn.lethekk.ragqaservice.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @Author Lethekk
 * @Date 2026/11/20 13:31
 * 历史消息表实体
 * 对应表：chat_history
 */
@Data
@TableName("chat_history")
public class ChatHistoryPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 历史消息ID
     */
    @TableId(type = IdType.INPUT)
    private String historyId;

    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 消息会话序号
     */
    private Long sequenceId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息角色/类型
     * 对应枚举：USER / ASSISTANT / SYSTEM / TOOL
     */
    private String type;

    /**
     * 消息时间戳
     */
    private LocalDateTime timestamp;

}
