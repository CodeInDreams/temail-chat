package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.domains.UsermailBlacklistDO;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBlacklistRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsermailBlacklistService {

  private final UsermailBlacklistRepo usermailBlacklistRepo;

  private final IUsermailAdapter iUsermailAdapter;

  @Autowired
  public UsermailBlacklistService(UsermailBlacklistRepo usermailBlacklistRepo, IUsermailAdapter iUsermailAdapter) {
    this.usermailBlacklistRepo = usermailBlacklistRepo;
    this.iUsermailAdapter = iUsermailAdapter;
  }

  /**
   * @param usermailBlacklist 黑名单参数（temailAddress:发起人 blackedAddress:被拉黑人）
   * @return 新增行数
   * @description 保存单聊黑名单
   */
  @Transactional
  public int save(UsermailBlacklistDO usermailBlacklist) {
    usermailBlacklist.setId(iUsermailAdapter.getUsermailBlacklistPkID());
    return usermailBlacklistRepo.insert(usermailBlacklist);
  }

  /**
   * @param usermailBlacklist 移除黑名单参数（temailAddress:发起人 blackedAddress:被移除人）
   * @return 删除行数
   * @description 移除单聊黑名单
   */
  @Transactional
  public int remove(UsermailBlacklistDO usermailBlacklist) {
    return usermailBlacklistRepo.deleteByAddresses(usermailBlacklist);
  }

  /**
   * @param temailAddress 发起人
   * @param blackedAddress 被拉黑人
   * @return 黑名单数据（temailAddress:发起人 blackedAddress:被拉黑人）
   * @description 查询单条黑名单数据
   */
  @Transactional(readOnly = true)
  public UsermailBlacklistDO findByAddresses(String temailAddress, String blackedAddress) {
    return usermailBlacklistRepo.selectByAddresses(temailAddress, blackedAddress);
  }

  /**
   * @param temailAddress 发送人
   * @return 发送人黑名单列表（temailAddress:发起人 blackedAddress:被拉黑人）
   * @description 查询黑名单列表
   */
  @Transactional(readOnly = true)
  public List<UsermailBlacklistDO> findByTemailAddress(String temailAddress) {
    return usermailBlacklistRepo.selectByTemailAddress(temailAddress);
  }

  /**
   * @param from 发件人
   * @param to 收件人
   * @return int  是否在黑名单中（1:在，0:不在）
   * @description 检查发件人是否在收件人的黑名单中
   */
  @Transactional(readOnly = true)
  public int isInBlacklist(String from, String to) {
    // from to 对应黑名单要反过来查询，确认自己是否被[to]加入黑名单
    return usermailBlacklistRepo.countByAddresses(to, from);
  }
}
