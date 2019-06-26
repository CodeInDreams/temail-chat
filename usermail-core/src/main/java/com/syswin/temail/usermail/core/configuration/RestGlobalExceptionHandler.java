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

package com.syswin.temail.usermail.core.configuration;

import com.syswin.temail.usermail.common.ResultCodeEnum;
import com.syswin.temail.usermail.core.dto.ResultDTO;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
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
  public ResultDTO handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
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

    ResultDTO resultDto = new ResultDTO();
    resultDto.setCode(ResultCodeEnum.ERROR_SERVER.getCode());
    resultDto.setMessage(ResultCodeEnum.ERROR_SERVER.getMessage() + ",异常信息为:" + e.getMessage());
    return resultDto;
  }

  @ExceptionHandler(IllegalGmArgsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResultDTO handleException(HttpServletRequest request, HttpServletResponse response, IllegalGmArgsException e) {
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
    ResultDTO resultDto = new ResultDTO();
    resultDto.setCode(e.getResultCode().getCode());
    resultDto.setMessage(e.getResultCode().getMessage() + (e.getMessage() == null ? "" :  ": " + e.getMessage()));
    return resultDto;
  }

  @ExceptionHandler(DuplicateKeyException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResultDTO handleException(HttpServletRequest request, HttpServletResponse response, DuplicateKeyException e) {
    LOGGER.warn("Database key conflict", e);
    ResultDTO resultDto = new ResultDTO();
    resultDto.setCode(ResultCodeEnum.ERROR_DATABASE_KEY_ID.getCode());
    resultDto.setMessage(ResultCodeEnum.ERROR_DATABASE_KEY_ID.getMessage() + e.getMessage());
    return resultDto;
  }

  @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResultDTO handleBindException(HttpServletRequest request, HttpServletResponse response, Exception e) {
    LOGGER.warn("parameter binding exception", e);

    ResultDTO resultDto = new ResultDTO();
    resultDto.setCode(ResultCodeEnum.ERROR_REQUEST_PARAM.getCode());
    resultDto.setMessage(ResultCodeEnum.ERROR_REQUEST_PARAM.getMessage());

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