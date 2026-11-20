CREATE TABLE `chat_memory`
(
    `conversation_id` varchar(256) NOT NULL COMMENT '会话ID',
    `content`         text         NOT NULL COMMENT '消息内容',
    `type`            enum('USER','ASSISTANT','SYSTEM','TOOL') NOT NULL COMMENT '消息角色/类型',
    `timestamp`       timestamp    NOT NULL COMMENT '消息时间戳',
    `sequence_id`     bigint       NOT NULL COMMENT '消息序号',
    `meta_data`       varchar(256) NOT NULL COMMENT '元数据',
    KEY               `idx_conversation_id_timestamp` (`conversation_id`,`timestamp`),
    KEY               `idx_conversation_id_sequence_id` (`conversation_id`,`sequence_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `chat_conversation`
(
    `conversation_id` varchar(256) NOT NULL COMMENT '会话ID',
    `user_id`         int          NOT NULL COMMENT '用户ID',
    `title`           varchar(64)  NOT NULL COMMENT '标题',
    `create_time`     timestamp    NOT NULL COMMENT '创建时间',
    primary key (`conversation_id`),
    key               idx_user_id_create_time( `user_id`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `chat_history`
(
    `history_id`    varchar(256) NOT NULL COMMENT '历史消息ID',
    `conversation_id` varchar(256) NOT NULL COMMENT '会话ID',
    `sequence_id`     bigint       NOT NULL COMMENT '消息会话序号',
    `user_id`         int          NOT NULL COMMENT '用户ID',
    `content`         text         NOT NULL COMMENT '消息内容',
    `type`            enum('USER','ASSISTANT','SYSTEM','TOOL') NOT NULL COMMENT '消息角色/类型',
    `timestamp`       timestamp    NOT NULL COMMENT '消息时间戳'
    primary key (`history_id`),
    unique key `uk_conversation_id_sequence_id`(`conversation_id`, `sequence_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;