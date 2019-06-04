package com.syswin.temail.usermail.core.dto;

import com.syswin.temail.ps.client.Message;
import java.io.Serializable;

public class MqClientMsgDto implements Serializable {

  private Message groupchatMessage;
  private int type;
  private String groupTemail;

  public Message getGroupchatMessage() {
    return groupchatMessage;
  }

  public void setGroupchatMessage(Message groupchatMessage) {
    this.groupchatMessage = groupchatMessage;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getGroupTemail() {
    return groupTemail;
  }

  public void setGroupTemail(String groupTemail) {
    this.groupTemail = groupTemail;
  }
}
