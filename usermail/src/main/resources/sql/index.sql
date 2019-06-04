
CREATE  INDEX `i_se_owner` ON `usermail`  (`sessionid`,`owner`,`seq_no`);
ALTER TABLE `usermail` ADD UNIQUE KEY `UK_msg` (`msgid`,`owner`);


--CREATE  INDEX `i_owner_mail2` ON usermail_box  (`owner`,`mail2`) USING BTREE;
--普通索引改成唯一索引
ALTER TABLE `usermail_box`
ADD UNIQUE INDEX `i_owner_mail2` (`owner`, `mail2`) USING BTREE ;


-- 防止前端重发，加入唯一索引
ALTER TABLE `usermail_msg_reply` ADD UNIQUE KEY `UK_rep_msg` (`msgid`,`owner`);
-- 查询优化
ALTER TABLE `usermail_msg_reply` ADD INDEX `i_msgre_utemail` (`parent_msgid`,`owner`,`seq_no`);

CREATE UNIQUE INDEX `index-blacked_address` ON usermail_blacklist (`blacked_address`,`temail_address`) USING BTREE;







