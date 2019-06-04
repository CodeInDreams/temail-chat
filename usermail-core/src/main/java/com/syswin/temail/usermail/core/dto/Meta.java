package com.syswin.temail.usermail.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

@ApiModel(value = "元信息")
public class Meta implements java.io.Serializable{

  @ApiModelProperty("at列表")
  private String at = "";
  @ApiModelProperty("主题")
  private String topic = "";
  @ApiModelProperty("扩展字段")
  private String extraData = "";

  public Meta() {
  }

  public Meta(String at, String topic, String extraData) {
    this.at = at;
    this.topic = topic;
    this.extraData = extraData;
  }

  public String getAt() {
    return at;
  }

  public void setAt(String at) {
    this.at = at;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getExtraData() {
    return extraData;
  }

  public void setExtraData(String extraData) {
    this.extraData = extraData;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Meta{");
    sb.append("at='").append(at).append('\'');
    sb.append(", topic='").append(topic).append('\'');
    sb.append(", extraData='").append(extraData).append('\'');
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Meta meta = (Meta) o;
    return Objects.equals(at, meta.at) &&
        Objects.equals(topic, meta.topic) &&
        Objects.equals(extraData, meta.extraData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(at, topic, extraData);
  }
}
