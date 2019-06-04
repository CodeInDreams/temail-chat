package com.syswin.temail.usermail.core.configuration;

import com.syswin.temail.usermail.core.filter.ParamFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ServletComponentScan
@Configuration
public class WebConfiguration {

  @Bean
  public FilterRegistrationBean indexFilterRegistration() {
    FilterRegistrationBean registration = new FilterRegistrationBean(new ParamFilter());
    registration.addUrlPatterns("/*");
    return registration;
  }


}
