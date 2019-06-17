package com.syswin.temail.usermail.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.syswin.temail.usermail.infrastructure.domain.mapper",
    "com.syswin.temail.data.consistency.infrastructure"})
public class UsermailMybatisConfiguration {


}
