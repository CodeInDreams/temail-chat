/*
 * MIT License
 *
 * Copyright (c) 2019 Syswin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
import com.syswin.temail.usermail.common.ParamsKey;
import com.syswin.temail.usermail.common.ResultCodeEnum;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.core.dto.ResultDTO;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.dto.CreateUsermailDTO;
import com.syswin.temail.usermail.dto.DeleteMailBoxQueryDTO;
import com.syswin.temail.usermail.dto.MailboxDTO;
import com.syswin.temail.usermail.dto.MoveTrashMailDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import com.syswin.temail.usermail.dto.TrashMailsDTO;
import com.syswin.temail.usermail.dto.UmDeleteMailDTO;
import com.syswin.temail.usermail.dto.UpdateArchiveDTO;
import com.syswin.temail.usermail.dto.UsermailDTO;
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
public class UsermailAgentControllerTest {

  private CdtpHeaderDTO headerInfo = new CdtpHeaderDTO("{CDTP-header:value}",
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
    CreateUsermailDTO usermailDto = new CreateUsermailDTO(
        "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707",
        "bob@temail.com", "alice@temail.com", 0, 1, "test_from_data", 100);
    Mockito.doThrow(new IllegalGmArgsException(ResultCodeEnum.ERROR_IN_BLACKLIST))
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
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(usermailDto)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.code").value(461));
  }

  @Test
  public void shouldSendWhenNotInBlacklist() throws Exception {
    CreateUsermailDTO usermailDto = new CreateUsermailDTO(
        "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707",
        "bob@temail.com", "alice@temail.com", 0, 1, "test_from_data", 100);
    CreateUsermailDTO usermailDto1 = new CreateUsermailDTO(
        "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707",
        "bob@temail.com", "alice@temail.com", 0, 2, "test_from_data", 100);
    CreateUsermailDTO usermailDto2 = new CreateUsermailDTO(
        "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707",
        "bob@temail.com", "alice@temail.com", 0, 0, "test_from_data", 100);
    Mockito.doNothing().when(mockUmBlacklistProxy)
        .checkInBlacklist("bob@temail.com", "alice@temail.com");
    Map<String, Object> map = new HashMap<>();
    map.put("msgId", "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707");
    map.put("seqNo", new Random().nextInt());
    Mockito.doReturn(map).when(usermailService).sendMail(headerInfo, usermailDto, "bob@temail.com", "alice@temail.com");
    Mockito.doReturn(map).when(usermailService).sendMail(headerInfo, usermailDto1, "bob@temail.com", "alice@temail.com");
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        post("/usermail")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(usermailDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
    mockMvc.perform(
        post("/usermail")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(usermailDto1)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
    mockMvc.perform(
        post("/usermail")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(usermailDto2)))
        .andExpect(jsonPath("$.code").value(459));
  }

  @Test
  public void shouldGetListWhenPullMessages() throws Exception {
    List<UsermailDO> usermails = Arrays.asList(
        new UsermailDO(1, "123213141", "4324234", "bob@temail.com", "alice@temail.com", 0, 0,
            "bob@temail.com", "test message", 10),
        new UsermailDO(2, "123213142", "4324234", "bob@temail.com", "alice@temail.com", 0, 0,
            "bob@temail.com", "test message1", 10)
    );
    Mockito.doReturn(usermails).when(usermailService)
        .getMails("bob@temail.com", "alice@temail.com", 0, 20, "", "before");
    mockMvc.perform(
        get("/usermail")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .param("from", "bob@temail.com")
            .param("to", "alice@temail.com")
            .param("seqId", "0"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200))
        .andExpect(jsonPath("$.data").isArray());
  }

  @Test
  public void shouldSuccessWhenRevert() throws Exception {
    UsermailDTO usermailDto = new UsermailDTO("123213123123", "bob@temail.com", "alice@temail.com",
        0, "test message", 0);
    Mockito.doNothing().when(usermailService)
        .revert(headerInfo, "bob@temail.com", "alice@temail.com", "123213123123");
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        put("/revert")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(usermailDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldGetListWhenPullMailboxes() throws Exception {
    List<MailboxDTO> mailboxDto = Arrays.asList(
        new MailboxDTO(1, "alice@temail.com", "title", false, new UsermailDO(), 0),
        new MailboxDTO(1, "jack@temail.com", "title", false, new UsermailDO(), 0)
    );
    Mockito.doReturn(mailboxDto).when(usermailService).mailboxes("bob@temail.com", -1, null);
    ObjectMapper mapper = new ObjectMapper();
    MvcResult result = mockMvc.perform(
        get("/usermail/mailboxes")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .param("from", "bob@temail.com"))
        .andReturn();
    ResultDTO resultDto = new ResultDTO();
    resultDto.setData(mailboxDto);
    assertThat(result.getResponse().getContentAsString())
        .isEqualTo(mapper.writeValueAsString(resultDto));
  }

  @Test
  public void shouldDeleteMessage() throws Exception {
    List<String> msgIds = new ArrayList<>();
    msgIds.add("123213123123");
    msgIds.add("59598095904");
    UmDeleteMailDTO umDeleteMailDto = new UmDeleteMailDTO(msgIds, "bob@temail.com", "alice@temail.com",
        0, "test message", 0);
    Mockito.doNothing().when(usermailService)
        .removeMsg(headerInfo, "bob@temail.com", "alice@temail.com", msgIds);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        put("/usermail/msg/remove")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(umDeleteMailDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldDestroyMessage() throws Exception {
    UsermailDTO usermailDto = new UsermailDTO("123213123123", "bob@temail.com", "alice@temail.com",
        0, "test message", 0);
    Mockito.doNothing().when(usermailService)
        .destroyAfterRead(headerInfo, "bob@temail.com", "alice@temail.com", "123213123123");
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        put("/usermail/msg/destory")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(usermailDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldDeleteSession() throws Exception {
    DeleteMailBoxQueryDTO queryDto = new DeleteMailBoxQueryDTO("bob@temail.com", "alice@temail.com", true);
    Mockito.doReturn(true).when(usermailService).deleteSession(headerInfo, queryDto);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        delete("/usermail/session")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
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
    List<UsermailDO> expectMailList = new ArrayList<>();

    UsermailDO mail1 = new UsermailDO();
    UsermailDO mail2 = new UsermailDO();
    UsermailDO mail3 = new UsermailDO();
    mail1.setMsgid(msgIds.get(0));
    mail2.setMsgid(msgIds.get(1));
    mail3.setMsgid(msgIds.get(2));
    expectMailList.add(mail1);
    expectMailList.add(mail2);
    expectMailList.add(mail3);
    Mockito.doReturn(expectMailList).when(usermailService).batchQueryMsgs(from, msgIds);

    JsonArray msgIdsArray = new JsonArray();
    msgIdsArray.add(msgIds.get(0));
    msgIdsArray.add(msgIds.get(1));
    msgIdsArray.add(msgIds.get(2));

    String[] msgIdStrArray = {msgIds.get(0), msgIds.get(1), msgIds.get(2)};

    MvcResult result = mockMvc.perform(get("/usermail/msg")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
        .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
        .param("from", from)
        .param("to", to)
        .param("msgIds", msgIdStrArray)
    ).andReturn();
    ObjectMapper mapper = new ObjectMapper();
    ResultDTO resultDto = new ResultDTO();
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
    List<UsermailDO> expectMailList = new ArrayList<>();

    UsermailDO mail1 = new UsermailDO();
    UsermailDO mail2 = new UsermailDO();
    UsermailDO mail3 = new UsermailDO();
    mail1.setMsgid(msgIds.get(0));
    mail1.setReplyCount(1);
    mail2.setMsgid(msgIds.get(1));
    mail2.setReplyCount(1);
    mail3.setMsgid(msgIds.get(2));
    mail3.setReplyCount(1);
    expectMailList.add(mail1);
    expectMailList.add(mail2);
    expectMailList.add(mail3);
    Mockito.doReturn(expectMailList).when(usermailService).batchQueryMsgsReplyCount(from, msgIds);

    JsonArray msgIdsArray = new JsonArray();
    msgIdsArray.add(msgIds.get(0));
    msgIdsArray.add(msgIds.get(1));
    msgIdsArray.add(msgIds.get(2));

    String[] msgIdStrArray = {msgIds.get(0), msgIds.get(1), msgIds.get(2)};

    MvcResult result = mockMvc.perform(get("/usermail/replyCount")
        .accept(MediaType.APPLICATION_JSON_UTF8)
        .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
        .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
        .param("from", from)
        .param("to", to)
        .param("msgIds", msgIdStrArray)
    ).andReturn();
    ObjectMapper mapper = new ObjectMapper();
    ResultDTO resultDto = new ResultDTO();
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
    MoveTrashMailDTO moveTrashMailDto = new MoveTrashMailDTO("bob@temail.com", "alice@temail.com", msgIds);
    Mockito.doNothing().when(usermailService)
        .moveMsgToTrash(headerInfo, "bob@temail.com", "alice@temail.com", msgIds);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        post("/usermail/msg/trash")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(moveTrashMailDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldRevertMsgToTrash() throws Exception {
    String msgId = "123213123123";
    String msgId2 = "59598095904";
    String from = "a.@temail";
    List<TrashMailDTO> mailDtoList = Arrays.asList(
        new TrashMailDTO("bob@temail.com", "alice@temail.com", msgId),
        new TrashMailDTO("bob2@temail.com", "alice2@temail.com", msgId2)
    );
    TrashMailsDTO trashMailsDto = new TrashMailsDTO(from, mailDtoList);
    Mockito.doNothing().when(usermailService)
        .revertMsgFromTrash(headerInfo, "bob@temail.com", mailDtoList);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        put("/usermail/msg/trash")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(trashMailsDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldRemoveMsgFromTrash() throws Exception {
    String msgId = "123213123123";
    String msgId2 = "59598095904";
    String from = "a.@temail";
    List<TrashMailDTO> mailDtoList = Arrays.asList(
        new TrashMailDTO("bob@temail.com", "alice@temail.com", msgId),
        new TrashMailDTO("bob2@temail.com", "alice2@temail.com", msgId2)
    );
    TrashMailsDTO trashMailsDto = new TrashMailsDTO(from, mailDtoList);
    Mockito.doNothing().when(usermailService)
        .removeMsgFromTrash(headerInfo, "bob@temail.com", mailDtoList);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        delete("/usermail/msg/trash")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
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
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
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
    List<UsermailDO> usermails = Arrays.asList(
        new UsermailDO(1, "123213141", "4324234", "bob@temail.com", "alice@temail.com", 0, 0,
            "bob@temail.com", "test message", 0),
        new UsermailDO(2, "123213142", "4324234", "bob@temail.com", "alice@temail.com", 0, 0,
            "bob@temail.com", "test message1", 0)
    );
    Mockito.doReturn(usermails).when(usermailService).getMsgFromTrash(from, timestamp, pageSize, signal);
    MvcResult result = mockMvc.perform(
        get("/usermail/msg/trash")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .param("from", "bob@temail.com")
            .param("pagesize", "20")
            .param("timestamp", "155048418")
            .param("signal", "before"))
        .andReturn();
    ObjectMapper mapper = new ObjectMapper();
    ResultDTO resultDto = new ResultDTO();
    resultDto.setData(usermails);
    String expect = mapper.writeValueAsString(resultDto);
    String actual = result.getResponse().getContentAsString();
    Assert.assertEquals(expect, actual);
  }

  @Test
  public void shouldUpdateUsermailBoxArchiveStatus() throws Exception {
    UpdateArchiveDTO archiveDto =
        new UpdateArchiveDTO("bob@temail.com", "alice@temail.com", 1);
    Mockito.doNothing().when(usermailService)
        .updateUsermailBoxArchiveStatus(headerInfo, archiveDto.getFrom(), archiveDto.getTo(),
            archiveDto.getArchiveStatus());
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        put("/usermail/msg/archive")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(ParamsKey.HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(ParamsKey.HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(archiveDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }
}
