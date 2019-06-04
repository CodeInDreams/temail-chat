package com.syswin.temail.usermail.core;


import com.syswin.temail.usermail.core.dto.MqPacketDto;
import java.util.function.Consumer;

public interface IPackageDispatcher {

  void dispatch(String packet, Consumer<MqPacketDto> consumer);

}
