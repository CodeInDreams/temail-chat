package com.syswin.temail.usermail.core;


import com.syswin.temail.usermail.core.dto.ResultDto;

public interface ICmdAdapter<T> {

  String getCommand();

  Class getParamClass();

  ResultDto cmdHandler(String cdtpHeader, T params);

}


