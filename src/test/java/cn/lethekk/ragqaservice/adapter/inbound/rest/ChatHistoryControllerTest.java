package cn.lethekk.ragqaservice.adapter.inbound.rest;

import cn.lethekk.ragqaservice.dao.ChatConversationMapper;
import cn.lethekk.ragqaservice.dao.ChatHistoryMapper;
import cn.lethekk.ragqaservice.model.po.ChatConversationPO;
import cn.lethekk.ragqaservice.model.po.ChatHistoryPO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class ChatHistoryControllerTest {

    private static final int OWNER_ID = 990001;
    private static final int OTHER_ID = 990002;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ChatConversationMapper conversationMapper;

    @Autowired
    private ChatHistoryMapper historyMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void listConversationsReturnsOnlyOwnConversations() throws Exception {
        String ownConversationId = seedConversation(OWNER_ID, "我的会话");
        seedConversation(OTHER_ID, "别人的会话");

        mockMvc.perform(get("/chat/history/conversations").param("userId", String.valueOf(OWNER_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].conversationId").value(ownConversationId))
                .andExpect(jsonPath("$[0].title").value("我的会话"));
    }

    @Test
    void listMessagesReturnsMessagesInSequenceOrderForOwner() throws Exception {
        String conversationId = seedConversation(OWNER_ID, "会话");
        seedMessage(conversationId, OWNER_ID, 2L, "ASSISTANT", "回复");
        seedMessage(conversationId, OWNER_ID, 1L, "USER", "提问");

        mockMvc.perform(get("/chat/history/conversations/{id}/messages", conversationId)
                        .param("userId", String.valueOf(OWNER_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].content").value("提问"))
                .andExpect(jsonPath("$[1].content").value("回复"));
    }

    @Test
    void listMessagesReturnsEmptyForNonOwner() throws Exception {
        String conversationId = seedConversation(OWNER_ID, "会话");
        seedMessage(conversationId, OWNER_ID, 1L, "USER", "提问");

        mockMvc.perform(get("/chat/history/conversations/{id}/messages", conversationId)
                        .param("userId", String.valueOf(OTHER_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void updateTitleSucceedsForOwner() throws Exception {
        String conversationId = seedConversation(OWNER_ID, "旧标题");

        mockMvc.perform(put("/chat/history/conversations/{id}/title", conversationId)
                        .param("userId", String.valueOf(OWNER_ID))
                        .param("title", "新标题"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        ChatConversationPO updated = conversationMapper.selectById(conversationId);
        org.assertj.core.api.Assertions.assertThat(updated.getTitle()).isEqualTo("新标题");
    }

    @Test
    void updateTitleFailsForNonOwner() throws Exception {
        String conversationId = seedConversation(OWNER_ID, "旧标题");

        mockMvc.perform(put("/chat/history/conversations/{id}/title", conversationId)
                        .param("userId", String.valueOf(OTHER_ID))
                        .param("title", "新标题"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));

        ChatConversationPO unchanged = conversationMapper.selectById(conversationId);
        org.assertj.core.api.Assertions.assertThat(unchanged.getTitle()).isEqualTo("旧标题");
    }

    @Test
    void deleteConversationRemovesConversationAndMessagesForOwner() throws Exception {
        String conversationId = seedConversation(OWNER_ID, "会话");
        seedMessage(conversationId, OWNER_ID, 1L, "USER", "提问");

        mockMvc.perform(delete("/chat/history/conversations/{id}", conversationId)
                        .param("userId", String.valueOf(OWNER_ID))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        org.assertj.core.api.Assertions.assertThat(conversationMapper.selectById(conversationId)).isNull();
        org.assertj.core.api.Assertions.assertThat(historyMapper.selectList(null)
                .stream().filter(h -> conversationId.equals(h.getConversationId())).count()).isZero();
    }

    @Test
    void deleteConversationFailsForNonOwner() throws Exception {
        String conversationId = seedConversation(OWNER_ID, "会话");

        mockMvc.perform(delete("/chat/history/conversations/{id}", conversationId)
                        .param("userId", String.valueOf(OTHER_ID)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));

        org.assertj.core.api.Assertions.assertThat(conversationMapper.selectById(conversationId)).isNotNull();
    }

    private String seedConversation(int userId, String title) {
        ChatConversationPO conversation = new ChatConversationPO();
        conversation.setConversationId(UUID.randomUUID().toString());
        conversation.setUserId(userId);
        conversation.setTitle(title);
        conversation.setCreateTime(LocalDateTime.now());
        conversationMapper.insert(conversation);
        return conversation.getConversationId();
    }

    private void seedMessage(String conversationId, int userId, long sequenceId, String type, String content) {
        ChatHistoryPO history = new ChatHistoryPO();
        history.setHistoryId(UUID.randomUUID().toString());
        history.setConversationId(conversationId);
        history.setSequenceId(sequenceId);
        history.setUserId(userId);
        history.setContent(content);
        history.setType(type);
        history.setTimestamp(LocalDateTime.now());
        historyMapper.insert(history);
    }

}
