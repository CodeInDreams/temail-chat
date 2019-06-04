package com.syswin.temail.usermail.core;

import com.syswin.temail.usermail.core.dto.MqPacketParamDto;

public interface IPacketSdkProxy {

  String assembleCdtpPacket(MqPacketParamDto mqPacketParamDto);

  byte[] pack(MqPacketParamDto mqPacketParamDto);
}
