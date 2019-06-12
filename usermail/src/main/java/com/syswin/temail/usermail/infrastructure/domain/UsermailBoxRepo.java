package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.UsermailBox;
import java.util.List;

public interface UsermailBoxRepo {

  void saveUsermailBox(UsermailBox usermailBox);

  List<UsermailBox> getUsermailBoxByOwner(String mail, int archiveStatus);

  int deleteByOwnerAndTo(String from, String to);

  List<UsermailBox> selectByOwnerAndTo(String from, String to);

  int updateArchiveStatus(String from, String to,
      int archiveStatus);

  UsermailBox selectUsermailBox(String owner, String to);

}
