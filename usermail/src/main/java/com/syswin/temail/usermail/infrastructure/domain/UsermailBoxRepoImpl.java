package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.UsermailBox;
import com.syswin.temail.usermail.domains.UsermailBoxRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsermailBoxRepoImpl implements UsermailBoxRepo {

  private final UsermailBoxMapper usermailBoxMapper;

  @Autowired
  public UsermailBoxRepoImpl(UsermailBoxMapper usermailBoxMapper) {
    this.usermailBoxMapper = usermailBoxMapper;
  }

  @Override
  public void saveUsermailBox(UsermailBox usermailBox) {
    usermailBoxMapper.saveUsermailBox(usermailBox);
  }

  @Override
  public List<UsermailBox> getUsermailBoxByOwner(String mail, int archiveStatus) {
    return usermailBoxMapper.getUsermailBoxByOwner(mail, archiveStatus);
  }

  @Override
  public int deleteByOwnerAndTo(String from, String to) {
    return usermailBoxMapper.deleteByOwnerAndTo(from, to);
  }

  @Override
  public List<UsermailBox> selectByOwnerAndTo(String from, String to) {
    return usermailBoxMapper.selectByOwnerAndTo(from, to);
  }

  @Override
  public int updateArchiveStatus(String from, String to, int archiveStatus) {
    return usermailBoxMapper.updateArchiveStatus(from, to, archiveStatus);
  }

  @Override
  public UsermailBox selectUsermailBox(String owner, String to) {
    return usermailBoxMapper.selectUsermailBox(owner, to);
  }

}