package com.syswin.temail.usermail.interfaces;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syswin.temail.usermail.application.UsermailBlacklistService;
import com.syswin.temail.usermail.common.Contants.HttpHeaderKey;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDto;
import com.syswin.temail.usermail.core.dto.ResultDto;
import com.syswin.temail.usermail.domains.UsermailBlacklist;
import com.syswin.temail.usermail.dto.BlacklistDTO;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
public class UsermailBlacklistControllerTest {

  private CdtpHeaderDto headerInfo = new CdtpHeaderDto("{CDTP-header:value}", "{xPacketId:value}");
  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @MockBean
  private UsermailBlacklistService usermailBlacklistService;

  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  public void shouldAddBlacklist() throws Exception {
    BlacklistDTO blacklistDto = new BlacklistDTO("temail", "blacklist");
    UsermailBlacklist blacklist = new UsermailBlacklist(blacklistDto.getTemailAddress(),
        blacklistDto.getBlackedAddress());
    Mockito.doReturn(1).when(usermailBlacklistService).save(blacklist);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        post("/blacklist")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(blacklistDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void shouldRemoveBlacklist() throws Exception {
    BlacklistDTO blacklistDto = new BlacklistDTO("temail", "blacklist");
    UsermailBlacklist blacklist = new UsermailBlacklist(blacklistDto.getTemailAddress(),
        blacklistDto.getBlackedAddress());
    Mockito.doReturn(1).when(usermailBlacklistService).remove(blacklist);
    ObjectMapper mapper = new ObjectMapper();
    mockMvc.perform(
        delete("/blacklist")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .content(mapper.writeValueAsString(blacklistDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }

  @Test
  public void findBlacklists() throws Exception {
    String teamilAddress = "to@msgseal.com";
    List<UsermailBlacklist> usermailBlacklists = Arrays.asList(
        new UsermailBlacklist(teamilAddress, "a@msgseal.com"),
        new UsermailBlacklist(teamilAddress, "b@msgseal.com")
    );
    Mockito.doReturn(usermailBlacklists).when(usermailBlacklistService).findByTemailAddress(teamilAddress);
    List<String> blackedAddresses = usermailBlacklists.stream()
        .map(UsermailBlacklist::getBlackedAddress).collect(Collectors.toList());
    ObjectMapper mapper = new ObjectMapper();
    MvcResult result = mockMvc.perform(
        get("/blacklist")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .param("temailAddress", teamilAddress))
        .andReturn();
    ResultDto resultDto = new ResultDto();
    resultDto.setData(blackedAddresses);
    assertThat(result.getResponse().getContentAsString())
        .isEqualTo(mapper.writeValueAsString(resultDto));
  }

  @Test
  public void shouldInBlacklist() throws Exception {
    String from = "from@msgseal.com";
    String to = "to@msgseal.com";

    Mockito.doReturn(1).when(usermailBlacklistService).isInBlacklist(from, to);
    mockMvc.perform(
        get("/inblacklist")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .header(HttpHeaderKey.CDTP_HEADER, headerInfo.getCdtpHeader())
            .header(HttpHeaderKey.X_PACKET_ID, headerInfo.getxPacketId())
            .param("from", "from@msgseal.com")
            .param("to", "to@msgseal.com"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200));
  }
}
