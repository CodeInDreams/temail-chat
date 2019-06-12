package com.syswin.temail.usermail.core;

@Deprecated
public interface IRedisDispatcherAdapter {

  boolean checkPacketIdUnique(String key, Integer eventType);

}
