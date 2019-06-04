package com.syswin.temail.usermail.interfaces;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.syswin.temail.usermail.application.UmBlacklistProxy;
import com.syswin.temail.usermail.application.UsermailService;
import com.syswin.temail.usermail.common.Contants.HttpHeaderKey;
import com.syswin.temail.usermail.common.Contants.RESULT_CODE;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDto;
import com.syswin.temail.usermail.core.dto.ResultDto;
import com.syswin.temail.usermail.core.exception.IllegalGMArgsException;
import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.dto.CreateUsermailDto;
import com.syswin.temail.usermail.dto.DeleteMailBoxQueryDto;
import com.syswin.temail.usermail.dto.MailboxDto;
import com.syswin.temail.usermail.dto.MoveTrashMailDto;
import com.syswin.temail.usermail.dto.TrashMailDto;
import com.syswin.temail.usermail.dto.TrashMailsDto;
import com.syswin.temail.usermail.dto.UmDeleteMailDto;
import com.syswin.temail.usermail.dto.UpdateArchiveDto;
import com.syswin.temail.usermail.dto.UsermailDto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsermailAgentControllerTests {

  private CdtpHeaderDto headerInfo = new CdtpHeaderDto("{CDTP-header:value}",
      "{xPacketId:value}");

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @MockBean
  private UmBlacklistProxy mockUmBlacklistProxy;

  @MockBean
  private UsermailService usermailService;

  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  public void shouldNotSendWhenInBlacklist() throws Exception {
    CreateUsermailDto usermailDto = new CreateUsermailDto(
        "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707",
        "bob@temail.com", "alice@temail.com", 0, 1, "test_from_data", 100);
    Mockito.doThrow(new IllegalGMArgsException(RESULT_CODE.ERROR_IN_BLACKLIST))
        .when(mockUmBlacklistProxy).checkInBlacklist("bob@temail.com", "alice@temail.com");
    Map<String, Object> map = new HashMap<>();
    map.put("msgId", "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707");
    map.put("seqNo", new Random().nextInt());
    Mockito.doReturn(map).when(usermailService).sendMail(headerInfo, usermailDto, "bob@temail.com", "alice@temail.com");
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        post("/usermail")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(usermailDto)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(461));
  }

  @Test
  public void shouldSendWhenNotInBlacklist() throws Exception {
    CreateUsermailDto usermailDto = new CreateUsermailDto(
        "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707",
        "bob@temail.com", "alice@temail.com", 0, 1, "test_from_data", 100);
    Mockito.doNothing().when(mockUmBlacklistProxy)
        .checkInBlacklist("bob@temail.com", "alice@temail.com");
    Map<String, Object> map = new HashMap<>();
    map.put("msgId", "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707");
    map.put("seqNo", new Random().nextInt());
    Mockito.doReturn(map).when(usermailService).sendMail(headerInfo, usermailDto, "bob@temail.com", "alice@temail.com");
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        post("/usermail")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(usermailDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldGetListWhenPullMessages() throws Exception {
    List<Usermail> usermails = Arrays.asList(
        new Usermail(1, "123213141", "4324234", "bob@temail.com", "alice@temail.com", 0, 0,
            "bob@temail.com", "test message", 10),
        new Usermail(2, "123213142", "4324234", "bob@temail.com", "alice@temail.com", 0, 0,
            "bob@temail.com", "test message1", 10)
    );
    Mockito.doReturn(usermails).when(usermailService)
        .getMails(headerInfo, "bob@temail.com", "alice@temail.com", 0, 20, "", "before");
    mockMvc.perform(
        get("/usermail")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .param("from", "bob@temail.com")
            .param("to", "alice@temail.com")
            .param("seqId", "0"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200))
        .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  public void shouldSuccessWhenRevert() throws Exception {
    UsermailDto usermailDto = new UsermailDto("123213123123", "bob@temail.com", "alice@temail.com",
        0, "test message", 0);
    Mockito.doNothing().when(usermailService)
        .revert(headerInfo, "bob@temail.com", "alice@temail.com", "123213123123");
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        put("/revert")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(usermailDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldGetListWhenPullMailboxes() throws Exception {
    List<MailboxDto> mailboxDto = Arrays.asList(
        new MailboxDto(1, "alice@temail.com", "title", false, new Usermail(), 0),
        new MailboxDto(1, "jack@temail.com", "title", false, new Usermail(), 0)
    );
    Mockito.doReturn(mailboxDto).when(usermailService).mailboxes(headerInfo, "bob@temail.com", -1, null);
    ObjectMapper mapper = new ObjectMapper();
    MvcResult result = mockMvc.perform(
        get("/usermail/mailboxes")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .param("from", "bob@temail.com"))
        .andReturn();
    ResultDto resultDto = new ResultDto();
    resultDto.setData(mailboxDto);
    assertThat(result.getResponse().getContentAsString())
        .isEqualTo(mapper.writeValueAsString(resultDto));
  }

  @Test
  public void shouldDeleteMessage() throws Exception {
    List<String> msgIds = new ArrayList<>();
    msgIds.add("123213123123");
    msgIds.add("59598095904");
    UmDeleteMailDto umDeleteMailDto = new UmDeleteMailDto(msgIds, "bob@temail.com", "alice@temail.com",
        0, "test message", 0);
    Mockito.doNothing().when(usermailService)
        .removeMsg(headerInfo, "bob@temail.com", "alice@temail.com", msgIds);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        put("/usermail/msg/remove")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(umDeleteMailDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldDestoryMessage() throws Exception {
    UsermailDto usermailDto = new UsermailDto("123213123123", "bob@temail.com", "alice@temail.com",
        0, "test message", 0);
    Mockito.doNothing().when(usermailService)
        .destroyAfterRead(headerInfo, "bob@temail.com", "alice@temail.com", "123213123123");
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        put("/usermail/msg/destory")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(usermailDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldDeleteSession() throws Exception {
    DeleteMailBoxQueryDto queryDto = new DeleteMailBoxQueryDto("bob@temail.com", "alice@temail.com", true);
    Mockito.doReturn(true).when(usermailService).deleteSession(headerInfo, queryDto);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        delete("/usermail/session")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(queryDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldGetMsgByIds() throws Exception {
    String from = "from@systoontest.com";
    String to = "to@systoontest.com";
    List<String> msgIds = new ArrayList<>();
    msgIds.add("6d19b1c4-2665-49aa-801b-eb40bc10f532");
    msgIds.add("74e3bccd-b894-42d0-b535-9cea0824147d");
    msgIds.add("7a14838f-c003-41eb-b360-814265e4fcda");
    List<Usermail> expectMailList = new ArrayList<>();

    Usermail mail1 = new Usermail();
    Usermail mail2 = new Usermail();
    Usermail mail3 = new Usermail();
    mail1.setMsgid(msgIds.get(0));
    mail2.setMsgid(msgIds.get(1));
    mail3.setMsgid(msgIds.get(2));
    expectMailList.add(mail1);
    expectMailList.add(mail2);
    expectMailList.add(mail3);
    Mockito.doReturn(expectMailList).when(usermailService).batchQueryMsgs(headerInfo, from, to, msgIds);

    JsonArray msgIdsArray = new JsonArray();
    msgIdsArray.add(msgIds.get(0));
    msgIdsArray.add(msgIds.get(1));
    msgIdsArray.add(msgIds.get(2));

    String[] msgIdStrArray = {msgIds.get(0), msgIds.get(1), msgIds.get(2)};

    MvcResult result = mockMvc.perform(get("/usermail/msg")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
        .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
        .param("from", from)
        .param("to", to)
        .param("msgIds", msgIdStrArray)
    ).andReturn();
    ObjectMapper mapper = new ObjectMapper();
    ResultDto resultDto = new ResultDto();
    resultDto.setData(expectMailList);
    String expect = mapper.writeValueAsString(resultDto);
    String actual = result.getResponse().getContentAsString();
    Assert.assertEquals(expect, actual);
  }

  @Test
  public void shouldGetReplyCountByMsgIds() throws Exception {
    String from = "from@systoontest.com";
    String to = "to@systoontest.com";
    List<String> msgIds = new ArrayList<>();
    msgIds.add("6d19b1c4-2665-49aa-801b-eb40bc10f532");
    msgIds.add("74e3bccd-b894-42d0-b535-9cea0824147d");
    msgIds.add("7a14838f-c003-41eb-b360-814265e4fcda");
    List<Usermail> expectMailList = new ArrayList<>();

    Usermail mail1 = new Usermail();
    Usermail mail2 = new Usermail();
    Usermail mail3 = new Usermail();
    mail1.setMsgid(msgIds.get(0));
    mail1.setReplyCount(1);
    mail2.setMsgid(msgIds.get(1));
    mail2.setReplyCount(1);
    mail3.setMsgid(msgIds.get(2));
    mail3.setReplyCount(1);
    expectMailList.add(mail1);
    expectMailList.add(mail2);
    expectMailList.add(mail3);
    Mockito.doReturn(expectMailList).when(usermailService).batchQueryMsgsReplyCount(headerInfo, from, to, msgIds);

    JsonArray msgIdsArray = new JsonArray();
    msgIdsArray.add(msgIds.get(0));
    msgIdsArray.add(msgIds.get(1));
    msgIdsArray.add(msgIds.get(2));

    String[] msgIdStrArray = {msgIds.get(0), msgIds.get(1), msgIds.get(2)};

    MvcResult result = mockMvc.perform(get("/usermail/replyCount")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
        .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
        .param("from", from)
        .param("to", to)
        .param("msgIds", msgIdStrArray)
    ).andReturn();
    ObjectMapper mapper = new ObjectMapper();
    ResultDto resultDto = new ResultDto();
    resultDto.setData(expectMailList);
    String expect = mapper.writeValueAsString(resultDto);
    String actual = result.getResponse().getContentAsString();
    Assert.assertEquals(expect, actual);
  }

  @Test
  public void shouldMoveMsgToTrash() throws Exception {
    List<String> msgIds = new ArrayList<>();
    msgIds.add("123213123123");
    msgIds.add("59598095904");
    MoveTrashMailDto moveTrashMailDto = new MoveTrashMailDto("bob@temail.com", "alice@temail.com", msgIds);
    Mockito.doNothing().when(usermailService)
        .moveMsgToTrash(headerInfo, "bob@temail.com", "alice@temail.com", msgIds);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        post("/usermail/msg/trash")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(moveTrashMailDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldRevertMsgToTrash() throws Exception {
    String msgId = "123213123123";
    String msgId2 = "59598095904";
    String from = "a.@temail";
    List<TrashMailDto> mailDtoList = Arrays.asList(
        new TrashMailDto("bob@temail.com", "alice@temail.com", msgId),
        new TrashMailDto("bob2@temail.com", "alice2@temail.com", msgId2)
    );
    TrashMailsDto trashMailsDto = new TrashMailsDto(from, mailDtoList);
    Mockito.doNothing().when(usermailService)
        .revertMsgToTrash(headerInfo, "bob@temail.com", mailDtoList);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        put("/usermail/msg/trash")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(trashMailsDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldRemoveMsgFromTrash() throws Exception {
    String msgId = "123213123123";
    String msgId2 = "59598095904";
    String from = "a.@temail";
    List<TrashMailDto> mailDtoList = Arrays.asList(
        new TrashMailDto("bob@temail.com", "alice@temail.com", msgId),
        new TrashMailDto("bob2@temail.com", "alice2@temail.com", msgId2)
    );
    TrashMailsDto trashMailsDto = new TrashMailsDto(from, mailDtoList);
    Mockito.doNothing().when(usermailService)
        .removeMsgFromTrash(headerInfo, "bob@temail.com", mailDtoList);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        delete("/usermail/msg/trash")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(trashMailsDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldClearMsgFromTrash() throws Exception {
    String from = "s@t.email";
    Mockito.doNothing().when(usermailService).removeMsgFromTrash(headerInfo, from, null);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        delete("/usermail/msg/trash/clear")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(from)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldGetMsgFromTrash() throws Exception {
    String from = "bob@temail.com";
    int pageSize = 20;
    long timestamp = 155048418;
    String signal = "before";
    List<Usermail> usermails = Arrays.asList(
        new Usermail(1, "123213141", "4324234", "bob@temail.com", "alice@temail.com", 0, 0,
            "bob@temail.com", "test message", 0),
        new Usermail(2, "123213142", "4324234", "bob@temail.com", "alice@temail.com", 0, 0,
            "bob@temail.com", "test message1", 0)
    );
    Mockito.doReturn(usermails).when(usermailService).getMsgFromTrash(headerInfo, from, timestamp, pageSize, signal);
    MvcResult result = mockMvc.perform(
        get("/usermail/msg/trash")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .param("from", "bob@temail.com")
            .param("pagesize", "20")
            .param("timestamp", "155048418")
            .param("signal", "before"))
        .andReturn();
    ObjectMapper mapper = new ObjectMapper();
    ResultDto resultDto = new ResultDto();
    resultDto.setData(usermails);
    String expect = mapper.writeValueAsString(resultDto);
    String actual = result.getResponse().getContentAsString();
    Assert.assertEquals(expect, actual);
  }
  @Test
  public void shouldUpdateUsermailBoxArchiveStatus() throws Exception {
    UpdateArchiveDto archiveDto =
        new UpdateArchiveDto("bob@temail.com", "alice@temail.com", 1);
    Mockito.doNothing().when(usermailService)
        .updateUsermailBoxArchiveStatus(headerInfo, archiveDto.getFrom(), archiveDto.getTo(),
            archiveDto.getArchiveStatus());
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        put("/usermail/msg/archive")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(archiveDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }
}
