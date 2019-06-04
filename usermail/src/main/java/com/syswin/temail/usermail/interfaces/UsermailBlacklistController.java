package com.syswin.temail.usermail.interfaces;

import com.syswin.temail.usermail.application.UsermailBlacklistService;
import com.syswin.temail.usermail.core.dto.ResultDto;
import com.syswin.temail.usermail.domains.UsermailBlacklist;
import com.syswin.temail.usermail.dto.BlacklistDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsermailBlacklistController {

  private final UsermailBlacklistService usermailBlacklistService;

  @Autowired
  public UsermailBlacklistController(UsermailBlacklistService usermailBlacklistService) {
    this.usermailBlacklistService = usermailBlacklistService;
  }

  @ApiOperation(value = "添加黑名单(1000)")
  @PostMapping(value = "/blacklist")
  public ResultDto createBlacklist(HttpServletRequest request,
      @ApiParam(value = "黑名单关系双方的temail地址", required = true) @RequestBody @Valid BlacklistDto blacklistDto) {
    usermailBlacklistService.save(new UsermailBlacklist(blacklistDto.getTemailAddress(), blacklistDto.getBlackedAddress()));
    return new ResultDto();
  }

  @ApiOperation(value = "删除黑名单(1001)")
  @DeleteMapping(value = "/blacklist")
  public ResultDto removeBlacklist(HttpServletRequest request,
      @ApiParam(value = "黑名单关系双方的temail地址", required = true) @RequestBody @Valid BlacklistDto blacklistDto) {
    usermailBlacklistService.remove(new UsermailBlacklist(blacklistDto.getTemailAddress(), blacklistDto.getBlackedAddress()));
    return new ResultDto();
  }

  @ApiOperation(value = "查询发起者拉黑关系列表")
  @GetMapping(value = "/blacklist")
  public ResultDto findBlacklists(
      @ApiParam(value = "发起者temail地址", required = true) @RequestParam(value = "temailAddress", defaultValue = "") String temailAddress) {
    List<UsermailBlacklist> usermailBlacklists = usermailBlacklistService.findByTemailAddress(temailAddress);
    List<String> blackedAddresses = usermailBlacklists.stream()
        .map(UsermailBlacklist::getBlackedAddress).collect(Collectors.toList());
    ResultDto resultDto = new ResultDto();
    resultDto.setData(blackedAddresses);
    return resultDto;
  }

  @ApiOperation(value = "[from]给[to]时，判断是否被[to]加在黑名单中")
  @GetMapping(value = "/inblacklist")
  public ResultDto isInBlacklists(
      @ApiParam(value = "发起人", required = true) @RequestParam(value = "from", defaultValue = "") String from,
      @ApiParam(value = "收件人", required = true) @RequestParam(value = "to", defaultValue = "") String to) {
    int inBlacklist = usermailBlacklistService.isInBlacklist(from, to);
    ResultDto resultDto = new ResultDto();
    resultDto.setData(inBlacklist);
    return resultDto;
  }

}
