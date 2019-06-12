package com.syswin.temail.usermail.core.filter;


import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebFilter(urlPatterns = "/*", filterName = "paramFilter")
public class ParamFilter implements Filter {

  private static final Logger LOGGER = LoggerFactory.getLogger(ParamFilter.class);


  @Override
  public void destroy() {
    // Nothing to do
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    if (LOGGER.isDebugEnabled()) {
      HttpServletRequest request = (HttpServletRequest) servletRequest;
      String uri = request.getRequestURI();
      String method = request.getMethod();
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
      ResettableStreamHttpServletRequest wrappedRequest = new ResettableStreamHttpServletRequest(request);
      String body = IOUtils.toString(wrappedRequest.getReader());
      wrappedRequest.resetInputStream();
      LOGGER
          .debug("uri=[{}],method=[{}],Header=[{}],Parameter=[{}],Body=[{}]:", uri, method, bufHeader, bufParam, body);
      filterChain.doFilter(wrappedRequest, servletResponse);
    } else {

      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) {
    LOGGER.info("init ParamFilter");
  }


}
