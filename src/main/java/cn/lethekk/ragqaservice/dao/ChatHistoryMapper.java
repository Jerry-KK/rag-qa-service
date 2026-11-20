package cn.lethekk.ragqaservice.dao;

import cn.lethekk.ragqaservice.model.po.ChatHistoryPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Lethekk
 * @Date 2026/11/20
 * 历史消息表 Mapper
 */
@Mapper
public interface ChatHistoryMapper extends BaseMapper<ChatHistoryPO> {
}
