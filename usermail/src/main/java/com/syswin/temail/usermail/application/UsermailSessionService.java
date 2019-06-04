package com.syswin.temail.usermail.application;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class UsermailSessionService {

  public String getSessionID(String from, String to) {
    if (null == from || "".equals(from)) {
      throw new IllegalArgumentException("param [from] is illegal");
    }
    if (null == to || "".equals(to)) {
      throw new IllegalArgumentException("param [to] is illegal");
    }

    String value;
    if (from.compareTo(to) > 0) {
      value = from + to;
    } else {
      value = to + from;
    }
    return DigestUtils.md5Hex(value);
  }

}
