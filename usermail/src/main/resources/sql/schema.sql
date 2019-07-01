
DROP TABLE IF EXISTS `usermail`;
CREATE TABLE `usermail` (
  `id` bigint(20) NOT NULL COMMENT 'PKID',
  `msgid` varchar(128)  NOT NULL DEFAULT '' COMMENT '消息ID',
  `sessionid` varchar(128) NOT NULL DEFAULT '' COMMENT 'sessionID',
  `from` varchar(320) NOT NULL DEFAULT '' COMMENT '发送者',
  `to` varchar(320) NOT NULL DEFAULT '' COMMENT '接收者',
  `type` tinyint NOT NULL COMMENT '消息类型:0=普通消息',
  `status` tinyint NOT NULL COMMENT '消息状态:0=正常',
  `seq_no` bigint(20) NOT NULL COMMENT '会话序号',
  `owner` varchar(320) NOT NULL DEFAULT '' COMMENT '消息所属人(from/to)',
  `message` mediumtext NOT NULL DEFAULT '' COMMENT '加密消息内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `at` varchar(512) DEFAULT '' COMMENT 'at联系人',
  `topic` varchar(512) DEFAULT '' COMMENT '主题',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `reply_count` int(10) NOT NULL DEFAULT '0' COMMENT '回复消息数',
  `last_reply_msgid` varchar(128) NOT NULL DEFAULT '' COMMENT '最新回复消息msgid',
  `zip_msg` mediumblob COMMENT '消息加密消息内容(Gzip压缩)',
  `author` varchar(320) NOT NULL DEFAULT '' COMMENT '群聊消息发起者',
  `filter` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `usermail_box`;
CREATE TABLE `usermail_box` (
  `id` bigint(20) NOT NULL COMMENT 'PKID',
  `sessionid` varchar(128) NOT NULL DEFAULT '' COMMENT 'sessionID',
  `mail1` varchar(320) DEFAULT '',
  `mail2` varchar(320) NOT NULL DEFAULT '' COMMENT '另一位聊天者',
  `create_time` timestamp NOT NULL  DEFAULT CURRENT_TIMESTAMP ,
  `owner` varchar(128) NOT NULL DEFAULT '' COMMENT '会话拥有者',
  `archive_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '归档状态',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `session_ext_data` varchar(256) DEFAULT '' COMMENT '成员信息扩展字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `usermail_msg_reply`;
CREATE TABLE `usermail_msg_reply` (
  `id` bigint(20) NOT NULL,
  `parent_msgid` varchar(128) NOT NULL COMMENT '父消息ID，关联usermail表',
  `msgid` varchar(128) NOT NULL COMMENT '消息ID',
  `from` varchar(128) NOT NULL COMMENT '发件人邮件',
  `to` varchar(128) NOT NULL COMMENT '接收人邮件',
  `seq_no` bigint(20) NOT NULL COMMENT '回复消息序列号',
  `msg` mediumtext ,
  `status` tinyint(1) NOT NULL COMMENT '消息状态',
  `type` tinyint(1) NOT NULL COMMENT '消息类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `owner` varchar(320) NOT NULL DEFAULT '' COMMENT '消息所属人(from/to)',
  `sessionid` varchar(128) NOT NULL DEFAULT '' COMMENT 'sessionID',
  `zip_msg` mediumblob COMMENT '消息加密消息内容(Gzip压缩)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `usermail_blacklist`;
CREATE TABLE `usermail_blacklist` (
  `id` bigint(20) NOT NULL COMMENT 'PKID',
  `temail_address` varchar(320) NOT NULL DEFAULT '' COMMENT '黑名单发起者temail地址',
  `blacked_address` varchar(320) NOT NULL DEFAULT '' COMMENT '被拉黑者的temail地址',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态，0-生效状态，1-失效状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

