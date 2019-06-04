package com.syswin.temail.usermail.core;

public interface IRedisDispatcherAdapter {

  boolean checkPacketIdUnique(String key, Integer eventType);

}
