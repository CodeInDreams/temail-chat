package com.syswin.temail.usermail.core.configuration;

import com.syswin.temail.usermail.common.Contants.RESULT_CODE;
import com.syswin.temail.usermail.core.dto.ResultDto;
import com.syswin.temail.usermail.core.exception.IllegalGMArgsException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class RestGlobalExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestGlobalExceptionHandler.class);


  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResultDto handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
    Enumeration<String> headerNames = request.getHeaderNames();
    StringBuilder bufHeader = new StringBuilder();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      String headerValue = request.getHeader(headerName);
      bufHeader.append(headerName).append("=").append(headerValue).append(";");
    }
    Enumeration<String> parameterNames = request.getParameterNames();
    StringBuilder bufParam = new StringBuilder();
    while (parameterNames.hasMoreElements()) {
      String paramKey = parameterNames.nextElement();
      String paramValue = request.getParameter(paramKey);
      bufParam.append(paramKey).append("=").append(paramValue).append(";");
    }
    LOGGER.error("500-Parameter=[{}],Header=[{}]:", bufParam.toString(), bufHeader.toString(), e);

    ResultDto resultDto = new ResultDto();
    resultDto.setCode(RESULT_CODE.ERROR_SERVER.getCode());
    resultDto.setMessage(RESULT_CODE.ERROR_SERVER.getMessage() + ",异常信息为:" + e.getMessage());
    return resultDto;
  }

  @ExceptionHandler(IllegalGMArgsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResultDto handleException(HttpServletRequest request, HttpServletResponse response, IllegalGMArgsException e) {
    Enumeration<String> headerNames = request.getHeaderNames();
    StringBuilder bufHeader = new StringBuilder();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      String headerValue = request.getHeader(headerName);
      bufHeader.append(headerName).append("=").append(headerValue).append(";");
    }
    Enumeration<String> parameterNames = request.getParameterNames();
    StringBuilder bufParam = new StringBuilder();
    while (parameterNames.hasMoreElements()) {
      String paramKey = parameterNames.nextElement();
      String paramValue = request.getParameter(paramKey);
      bufParam.append(paramKey).append("=").append(paramValue).append(";");
    }
    LOGGER.warn("400-Parameter=[{}],Header=[{}]:", bufParam.toString(), bufHeader.toString(), e);
    ResultDto resultDto = new ResultDto();
    resultDto.setCode(e.getResultCode().getCode());
    resultDto.setMessage(e.getResultCode().getMessage() + (e.getMessage() == null ? "" :  ": " + e.getMessage()));
    return resultDto;
  }

  @ExceptionHandler(DuplicateKeyException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResultDto handleException(HttpServletRequest request, HttpServletResponse response, DuplicateKeyException e) {
    LOGGER.warn("Database key conflict", e);
    ResultDto resultDto = new ResultDto();
    resultDto.setCode(RESULT_CODE.ERROR_DATABASE_KEY_ID.getCode());
    resultDto.setMessage(RESULT_CODE.ERROR_DATABASE_KEY_ID.getMessage() + e.getMessage());
    return resultDto;
  }

  @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResultDto handleBindException(HttpServletRequest request, HttpServletResponse response, Exception e) {
    LOGGER.warn("parameter binding exception", e);

    ResultDto resultDto = new ResultDto();
    resultDto.setCode(RESULT_CODE.ERROR_REQUEST_PARAM.getCode());
    resultDto.setMessage(RESULT_CODE.ERROR_REQUEST_PARAM.getMessage());

    FieldError fieldError = null;
    if (e instanceof BindException) {
      fieldError = ((BindException) e).getBindingResult().getFieldError();
    } else if (e instanceof MethodArgumentNotValidException) {
      fieldError = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError();
    }

    if (null != fieldError) {
      resultDto.setMessage(fieldError.toString());
    }

    return resultDto;
  }


}