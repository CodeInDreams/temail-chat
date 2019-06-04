package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.UsermailBlacklist;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailBlacklistMapper {

  int insert(UsermailBlacklist usermailBlacklist);

  int deleteByAddresses(UsermailBlacklist usermailBlacklist);

  UsermailBlacklist selectByAddresses(@Param("temailAddress") String temailAddress,
      @Param("blackedAddress") String blackedAddress);

  List<UsermailBlacklist> selectByTemailAddress(String temailAddress);

  int countByAddresses(@Param("temailAddress") String temailAddress, @Param("blackedAddress") String blackedAddress);

}
