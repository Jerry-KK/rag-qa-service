package cn.lethekk.ragqaservice.dao;

import cn.lethekk.ragqaservice.model.po.ChatConversationPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Lethekk
 * @Date 2026/11/20
 * 会话表 Mapper
 */
@Mapper
public interface ChatConversationMapper extends BaseMapper<ChatConversationPO> {
}
