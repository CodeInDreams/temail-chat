/*
 * MIT License
 *
 * Copyright (c) 2019 Syswin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.syswin.temail.usermail.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UsermailConfig {

  /**
   * 发送给通知服务的topic
   */
  @Value("${spring.rocketmq.topic.notify}")
  public String mqTopic = "temail-usermail";

  /**
   * 自消费的topic
   */
  @Value("${spring.rocketmq.topic.usermail}")
  public String mqUserMailAgentTopic = "temail-usermailagent";

  @Value("${spring.rocketmq.consumer-group}")
  public String mqTrashConsumer = "temail-usermail-consumer";

  @Value("${spring.rocketmq.host}")
  public String namesrvAddr;

  @Value("${spring.rocketmq.temailmgt.topic}")
  public String mqMgtTopic = "topic_oss_notice";

  @Value("${spring.rocketmq.temailmgt.delete-domain.topic}")
  public String mqMgtDeleteDomainTopic = "temail-oss-topic-deletedomain";

  @Value("${spring.rocketmq.temailmgt.groupname}")
  public String mqMgtGroup = "temail_oss_usermailagent";


  /**
   * libraryMessage配置
   */
  @Value("${spring.rocketmq.senderType:REDIS}")
  public String senderMqType;

  @Value("${spring.rocketmq.receiverType:REDIS}")
  public String receiverMqType;
}
