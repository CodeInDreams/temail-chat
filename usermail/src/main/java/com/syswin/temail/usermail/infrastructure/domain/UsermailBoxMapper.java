package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.UsermailBox;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailBoxMapper {

  void saveUsermailBox(UsermailBox usermailBox);

  List<UsermailBox> getUsermailBoxByOwner(@Param("owner") String mail, @Param("archiveStatus") int archiveStatus);

  int deleteByOwnerAndTo(@Param("owner") String from, @Param("mail2") String to);

  List<UsermailBox> selectByOwnerAndTo(@Param("owner") String from, @Param("mail2") String to);

  int updateArchiveStatus(@Param("owner") String from, @Param("mail2") String to,
      @Param("archiveStatus") int archiveStatus);

  UsermailBox selectUsermailBox(@Param("owner") String owner, @Param("mail2") String to);
}
