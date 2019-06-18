package com.syswin.temail.usermail.interfaces;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syswin.temail.usermail.application.UmBlacklistProxy;
import com.syswin.temail.usermail.application.UsermailMsgReplyService;
import com.syswin.temail.usermail.common.ParamsKey;
import com.syswin.temail.usermail.common.ResultCodeEnum;
import com.syswin.temail.usermail.common.Constants.TemailStoreType;
import com.syswin.temail.usermail.common.Constants.TemailType;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.core.dto.ResultDTO;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import com.syswin.temail.usermail.domains.UsermailMsgReplyDO;
import com.syswin.temail.usermail.dto.ReplyDestroyDTO;
import com.syswin.temail.usermail.dto.UsermailMsgReplyDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsermailMsgReplyControllerTest {

  private CdtpHeaderDTO headerInfo = new CdtpHeaderDTO("{CDTP-header:value}", "xPacketId:value");

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @MockBean
  private UmBlacklistProxy umBlacklistProxy;

  @MockBean
  private UsermailMsgReplyService usermailMsgReplyService;

  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  public void shouldSendUsermailMsgReplyWhenNotInBlackList() throws Exception {
    List<String> msgIds = new ArrayList<>();
    msgIds.add("12344321");
    UsermailMsgReplyDTO msgReply = new UsermailMsgReplyDTO("12344321", "from", "to", TemailType.TYPE_NORMAL_0, "msg000",
        "syswin-11111111", 100, msgIds,
        TemailStoreType.STORE_TYPE_FROM_2);
    Mockito.doNothing().when(umBlacklistProxy).checkInBlacklist(msgReply.getFrom(), msgReply.getTo());
    Map<String, Object> map = new HashMap<>();
    map.put("msgId", "12344321");
    map.put("seqId", 5);
    Mockito.doReturn(map).when(usermailMsgReplyService)
        .createMsgReply(headerInfo, msgReply.getFrom(), msgReply.getTo(), msgReply.getMsgData(), msgReply.getMsgId(),
            msgReply.getParentMsgId(), msgReply.getType(), msgReply.getAttachmentSize(), "from");
    ObjectMapper objectMapper = new ObjectMapper();
    mockMvc.perform(
        post("/usermail/msg/reply").contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(objectMapper.writeValueAsString(msgReply)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));

  }

  @Test
  public void shouldNotSendMsgReplyWhenInBlackList() throws Exception {
    List<String> msgIds = new ArrayList<>();
    msgIds.add("173849");
    UsermailMsgReplyDTO msgReply = new UsermailMsgReplyDTO("1234321", "from@temail", "to@temail", 0, "sdkji", "1233",
        100, msgIds, 1);
    Mockito.doThrow(new IllegalGmArgsException(ResultCodeEnum.ERROR_IN_BLACKLIST)).when(umBlacklistProxy)
        .checkInBlacklist(msgReply.getFrom(), msgReply.getTo());
    Map<String, Object> map = new HashMap<>();
    map.put("msgId", "12344321");
    map.put("seqNo", 5);
    Mockito.doReturn(map).when(usermailMsgReplyService)
        .createMsgReply(headerInfo, "from@temail", "to@temail", "sdkji", "123444321", "1233", 0, 100, "from@temail");
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        post("/usermail/msg/reply")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(msgReply)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(461));
  }

  @Test
  public void shouldSuccessWhenRevertMsgReply() throws Exception {
    List<String> msgIds = new ArrayList<>();
    msgIds.add("173849");
    UsermailMsgReplyDTO msgReply = new UsermailMsgReplyDTO("1234321", "from@temail", "to@temail", 0, "sdkji", "1233",
        100, msgIds, 1);
    Mockito.doNothing().when(usermailMsgReplyService)
        .removeMsgReplys(headerInfo, "1233", msgIds, "from@temail", "to@temail");
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        put("/usermail/msg/reply")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(msgReply)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldSuccessWhenRemoveMsgReply() throws Exception {
    List<String> msgIds = new ArrayList<>();
    msgIds.add("173849");
    UsermailMsgReplyDTO msgReply = new UsermailMsgReplyDTO("1234321", "from@temail", "to@temail", 0, "sdkji", "1233",
        100, msgIds, 1);
    Mockito.doNothing().when(usermailMsgReplyService)
        .removeMsgReplys(headerInfo, "1233", msgIds, "from@temail", "to@temail");
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        delete("/usermail/msg/reply")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(msgReply)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldGetUsermailMsgReply() throws Exception {
    String parentMsgid = "1233";
    int pageSize = 20;
    long seqId = 0;
    String owner = "from@temail";
    String signal = "before";
    String filterSeqIds = "";
    List<UsermailMsgReplyDO> usermailMsgReplies = Arrays.asList(
        new UsermailMsgReplyDO(123, parentMsgid, "123444321", "from@temail", "to@temail", seqId, "msgs", 0, 0, owner, ""),
        new UsermailMsgReplyDO(1234, parentMsgid, "12344432", "from@temail", "to@temail", 2, "msgs2", 0, 0, owner, ""));
    Mockito.doReturn(usermailMsgReplies).when(usermailMsgReplyService)
        .getMsgReplys(headerInfo, parentMsgid, pageSize, seqId, signal, owner, filterSeqIds);
    ObjectMapper mapper = new ObjectMapper();
    MvcResult result = mockMvc.perform(
        get("/usermail/msg/reply")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .param("parentMsgId", parentMsgid)
            .param("pageSize", pageSize + "")
            .param("seqId", seqId + "")
            .param("signal", signal)
            .param("from", owner)).andReturn();
    ResultDTO resultDto = new ResultDTO();
    resultDto.setData(usermailMsgReplies);
    Assert.assertEquals(mapper.writeValueAsString(resultDto), result.getResponse().getContentAsString());
  }

  @Test
  public void shouldDestroyAfterRead() throws Exception {
    String from = "XXXX@qq.com";
    String to = "YYYY@qq.com";
    String msgId = "09876";
    ReplyDestroyDTO replyDestroyDto = new ReplyDestroyDTO(from, to, msgId);
    Mockito.doNothing().when(usermailMsgReplyService)
        .destroyAfterRead(headerInfo, from, to, msgId);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        put("/usermail/msg/reply/destory")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(replyDestroyDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }
}
