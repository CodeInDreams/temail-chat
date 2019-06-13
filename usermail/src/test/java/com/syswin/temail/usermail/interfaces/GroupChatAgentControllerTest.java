package com.syswin.temail.usermail.interfaces;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syswin.temail.usermail.application.GroupChatService;
import com.syswin.temail.usermail.common.Constants.HttpHeaderKey;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.dto.GroupChatEventDTO;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GroupChatAgentControllerTest {

  private CdtpHeaderDTO headerInfo = new CdtpHeaderDTO("{CDTP-header:value}",
      "{xPacketId:value}");

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @MockBean
  private GroupChatService groupChatService;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  public void createGroupChatMailBox() throws Exception {
    GroupChatEventDTO groupChatEventDto = new GroupChatEventDTO();
    groupChatEventDto.setFrom("asd@t.email");
    groupChatEventDto.setTo("groupChat@t.email");
    Mockito.doNothing().when(groupChatService).syncGroupChatMemberEvent(groupChatEventDto);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        post("/groupchat/event")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(groupChatEventDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void deleteGroupChatMailBox() throws Exception {
    GroupChatEventDTO groupChatEventDto = new GroupChatEventDTO();
    groupChatEventDto.setFrom("asd@t.email");
    groupChatEventDto.setTo("groupChat@t.email");
    Mockito.doNothing().when(groupChatService).removeGroupChatMemeberEvent(groupChatEventDto);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        delete("/groupchat/event")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(groupChatEventDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

}
