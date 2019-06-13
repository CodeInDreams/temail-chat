package com.syswin.temail.usermail.configuration;

import com.google.gson.Gson;
import com.syswin.temail.usermail.common.Constants.HttpHeaderKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class UsermailSwaggers2Config {

  @Bean
  public Docket docket() {
    ParameterBuilder cdtpHeader = new ParameterBuilder();
    ParameterBuilder xPacketId = new ParameterBuilder();
    List<Parameter> headerList = new ArrayList<>();
    String header = "header";
    String defaultPacketId = "temail-usermailagent_2019-01-01_00-00-00";
    cdtpHeader.name(HttpHeaderKey.CDTP_HEADER).description("请求头信息CDTP-header")
        .modelRef(new ModelRef("String"))
        .parameterType(header).required(true).defaultValue(generateExampleCdtpHeader()).build();
    xPacketId.name(HttpHeaderKey.X_PACKET_ID).description("请求头信息X-PACKET-ID")
        .modelRef(new ModelRef("String"))
        .parameterType(header).required(true).defaultValue(defaultPacketId).build();
    headerList.add(cdtpHeader.build());
    headerList.add(xPacketId.build());
    return new Docket(DocumentationType.SWAGGER_2).groupName("temail-usermail")
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.syswin"))
        .paths(PathSelectors.any())
        .build()
        .globalOperationParameters(headerList);
  }

  private String generateExampleCdtpHeader() {
    Map<String, String> cdtpHeaderMap = new HashMap<>(2);
    cdtpHeaderMap.put("header", "temail-usermailagent_example-header");
    return new Gson().toJson(cdtpHeaderMap);
  }

}
