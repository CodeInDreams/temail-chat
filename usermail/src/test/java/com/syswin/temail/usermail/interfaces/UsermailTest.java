package com.syswin.temail.usermail.interfaces;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.common.Constants.TemailStoreType;
import com.syswin.temail.usermail.common.Constants.TemailType;
import com.syswin.temail.usermail.common.ParamsKey;
import com.syswin.temail.usermail.core.dto.ResultDTO;
import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.dto.CreateUsermailDTO;
import com.syswin.temail.usermail.dto.MailboxDTO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 单聊集成测试
 */
@Ignore
public class UsermailTest {

  private static final String usermailUrl = "http://127.0.0.1:8090";
  private RestTemplate restTemplate = new RestTemplate();

  @Test
  @Ignore
  public void testUsermail() throws InterruptedException {
    //同步会话消息参数
    int pageSize = 20;
    Long seqId = 0L;
    List<String> msgIds = new ArrayList<>();
    String msgId = "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1539415620000";
    msgIds.add(msgId);
    CreateUsermailDTO nomalMsg = createMsg(TemailStoreType.STORE_TYPE_TO_1, TemailType.TYPE_NORMAL_0);
    //发送一条普通消息
    createUsermail(nomalMsg.getFrom(), nomalMsg.getTo(), nomalMsg.getMsgData(), TemailStoreType.STORE_TYPE_TO_1, nomalMsg.getMsgId(),
        TemailType.TYPE_NORMAL_0);
    //同步单聊会话列表
    String response = mailboxes(nomalMsg.getTo());
    assertMailboxes(response, nomalMsg.getMsgId());
    //同步单聊会话消息
    response = getMails(nomalMsg.getTo(), nomalMsg.getFrom(), pageSize, seqId);
    assertGetMails(response, nomalMsg.getMsgId());
    //删除消息, 重新同步会话列表
    removeMsg(nomalMsg.getTo(), nomalMsg.getFrom(), msgIds);
    response = getMails(nomalMsg.getTo(), nomalMsg.getFrom(), pageSize, seqId);
    assertGetMailsAfterRemove(response, nomalMsg.getMsgId());
    //撤回消息，重新同步会话列表
    revert(nomalMsg.getTo(), nomalMsg.getFrom(), nomalMsg.getMsgId());
    response = getMails(nomalMsg.getTo(), nomalMsg.getFrom(), pageSize, seqId);
    assertGetMailsAfterRevert(response, nomalMsg.getMsgId());
    //A重新发送一条阅后即焚消息，B拉取
    CreateUsermailDTO DARMsg = createMsg(TemailStoreType.STORE_TYPE_TO_1, TemailType.TYPE_NORMAL_0);
    //发送一条阅后即焚消息
    createUsermail(DARMsg.getFrom(), DARMsg.getTo(), DARMsg.getMsgData(), TemailStoreType.STORE_TYPE_TO_1, DARMsg.getMsgId(),
        TemailType.TYPE_DESTORY_AFTER_READ_1);
    String responseBeforeDestroy = getMails(DARMsg.getTo(), DARMsg.getFrom(), pageSize, seqId);
    destoryAfterRead(DARMsg.getTo(), DARMsg.getFrom(), DARMsg.getMsgId());
    String responseAfterDestroy = getMails(DARMsg.getTo(), DARMsg.getFrom(), pageSize, seqId);
    assertDestroyAfterRead(responseBeforeDestroy, responseAfterDestroy, DARMsg.getMsgId());
  }

  //生成一条消息。注意，只是生成一条消息内的数据元素，不发送
  private CreateUsermailDTO createMsg(int storeType, int msgType) {
    //生成时间戳
    long timestampLong = generateTimestamp();
    String timestamp = String.valueOf(timestampLong);
    String date = generateDate(timestampLong);
    String from = "A" + date;
    String to = "B" + date;
    String msg = "message" + date;
    String msgId = "syswin-" + timestamp;
    int attachmentSize = 100;
    //生成消息
    CreateUsermailDTO createUsermailDto = new CreateUsermailDTO(msgId, from, to, msgType, storeType,
        msg, attachmentSize);
    return createUsermailDto;
  }

  //从response中获取mailboxDto 会话列表
  private MailboxDTO getMailboxDtoInfo(String response) {
    Gson gs = new Gson();
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(response).getAsJsonObject();
    JsonObject data = root.get("data").getAsJsonArray().get(0).getAsJsonObject();
    MailboxDTO mailboxDto = gs.fromJson(data, MailboxDTO.class);
    //lastMsg在json转对象时有数据丢失，重设lastMsg
    //seqId --> seqNo  msgId -> msgid, 前面的是json tag，后面是对应类中的field
    JsonObject lastMsg = data.get("lastMsg").getAsJsonObject();
    Usermail usermail = gs.fromJson(lastMsg, Usermail.class);
    usermail.setSeqNo(lastMsg.get("seqId").getAsLong());
    usermail.setMsgid(lastMsg.get("msgId").getAsString());
    mailboxDto.setLastMsg(usermail);
    return mailboxDto;
  }

  //从response中获取usermail 消息列表
  private Usermail getLastMsgInfo(String response) {
    Gson gs = new Gson();
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(response).getAsJsonObject();
    JsonObject lastMsgObject = root.get("data").getAsJsonArray().get(0).getAsJsonObject();
    Usermail lastMsg = gs.fromJson(lastMsgObject, Usermail.class);
    //lastMsg在json转对象时有数据丢失，重设lastMsg
    //seqId --> seqNo  msgId -> msgid, 前面的是json tag，后面是对应类中的field
    lastMsg.setSeqNo(lastMsgObject.get("seqId").getAsLong());
    lastMsg.setMsgid(lastMsgObject.get("msgId").getAsString());
    return lastMsg;
  }

  //验证同步单聊会话 列表 的返回值response中，会话列表不为空，最后一条消息的msgId是否是参数中的msgId
  private void assertMailboxes(String response, String msgId) {
    MailboxDTO mailboxDto = getMailboxDtoInfo(response);
    Usermail usermail = mailboxDto.getLastMsg();
    Assert.assertNotNull(mailboxDto.getLastMsg());
    Assert.assertEquals(usermail.getMsgid(), msgId);
  }

  //验证同步单聊会话 消息 的返回值response中，最后一条消息的msgId是否是参数中的msgId
  private void assertGetMails(String response, String msgId) {
    Usermail usermail = getLastMsgInfo(response);
    Assert.assertEquals(usermail.getMsgid(), msgId);
  }

  //删除消息后重新同步会话消息
  private void assertGetMailsAfterRemove(String response, String msgId) {
    Usermail usermail = getLastMsgInfo(response);
    Assert.assertEquals(usermail.getMsgid(), msgId);
    //Assert.assertEquals(usermail.getStatus(), TemailStatus.STATUS_DELETE_3);
  }

  //撤回消息后重新同步会话消息
  private void assertGetMailsAfterRevert(String response, String msgId) {
    Usermail usermail = getLastMsgInfo(response);
    Assert.assertEquals(usermail.getMsgid(), msgId);
    Assert.assertEquals(TemailStatus.STATUS_REVERT_1, usermail.getStatus());
  }

  //验证阅后即焚前 和 阅后即焚后的消息
  private void assertDestroyAfterRead(String responseBeforeDestroy, String responseAfterDestroy,
      String msgId) {
    Usermail usermailBefore = getLastMsgInfo(responseBeforeDestroy);
    Usermail usermailAfter = getLastMsgInfo(responseAfterDestroy);
    Assert.assertTrue(
        usermailBefore.getMsgid().equals(msgId) && usermailAfter.getMsgid().equals(msgId));
    Assert.assertEquals(TemailStatus.STATUS_DESTORY_AFTER_READ_2, usermailAfter.getStatus());
    Assert.assertNotEquals(usermailBefore.getMessage(), usermailAfter.getMessage());
    Assert.assertEquals("", usermailAfter.getMessage());
  }

  //验证单个流程，发送消息(0x 0001)
  @Test
  public void testCreateUsermail() {
    String tag = Long.toString(System.currentTimeMillis());
    String from = "from" + tag;
    String to = "to" + tag;
    String msg = "message" + tag;
    String msgId = "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-" + tag;
    int storeType = TemailStoreType.STORE_TYPE_TO_1;
    int msgType = TemailType.TYPE_NORMAL_0;
    createUsermail(from, to, msg, storeType, msgId, msgType);
  }

  //验证单个流程，同步会话列表(0x 0002)
  @Test
  public void testMailboxes() {
    String from = "to20181013_1527";
    mailboxes(from);
  }

  //验证单个流程，同步单聊会话消息(0x 0003)
  @Test
  public void testGetMails() {
    String from = "from20181013_1527";
    String to = "to20181013_1527";
    int pageSize = 20;
    long seqId = 0L;
    getMails(from, to, pageSize, seqId);
  }

  //验证单个流程，删除消息(0x 0004)
  @Test
  public void testRemoveMsg() {
    String from = "from20181013_1527";
    String to = "to20181013_1527";
    List<String> msgIds = new ArrayList<>();
    String msgId = "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1539415620000";
    msgIds.add(msgId);
    removeMsg(from, to, msgIds);
  }

  //验证单个流程，撤回消息(0x 0005)
  @Test
  public void testRevert() {
    String from = "from20181013_1527";
    String to = "to20181013_1527";
    String msgId = "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1539415620000";

    revert(from, to, msgId);
  }

  //验证单个流程，阅后即焚消息已焚(0x 0006)
  @Test
  public void testDestoryAfterRead() {
    String from = "from20181013_1527";
    String to = "to20181013_1527";
    String msgId = "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1539415620000";

    destoryAfterRead(from, to, msgId);
  }

  //发送消息(0x 0001)
  private String createUsermail(String from, String to, String msg, int storeType, String msgId,
      int msgType) {
    HttpHeaders httpHeaders = getHttpHeaders();
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>();

    map.put("from", from);
    map.put("msgData", msg);
    map.put("msgId", msgId);
    map.put("storeType", storeType);
    map.put("to", to);
    map.put("type", msgType);

    String s = gs.toJson(map);
    HttpEntity<String> formEntity = new HttpEntity<>(s, httpHeaders);
    String result = restTemplate.postForObject(usermailUrl + "/usermail", formEntity, String.class);
    //验证Response
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(result).getAsJsonObject();
    Assert.assertEquals(200, root.get("code").getAsInt());
    Assert.assertEquals("success", root.get("message").getAsString());
    Assert.assertNotNull(root.get("data").getAsJsonObject());
    System.out.println("Response correct, assert success!");
    System.out.println("testCreateUsermail result = " + result);
    return result;
  }

  //同步会话列表(0x 0002)
  private String mailboxes(String from) {
    String url = usermailUrl + "/usermail/mailboxes" + "?from=" + from;
    String result = restTemplate.getForObject(url, String.class);
    //验证Response
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(result).getAsJsonObject();
    Assert.assertEquals(200, root.get("code").getAsInt());
    Assert.assertEquals("success", root.get("message").getAsString());
    Assert.assertNotNull(root.get("data"));
    System.out.println("Response correct, assert success!");
    System.out.println("testMailboxes result = " + result);
    return result;
  }

  //同步单聊会话消息(0x 0003)
  private String getMails(String from, String to, int pageSize, long seqId) {
    HttpHeaders headers = getHttpHeaders();
    String url = usermailUrl + "/usermail?" + "from=" + from + "&to=" + to + "&pageSize=" + pageSize
        + "&seqId=" + seqId;
    String result = restTemplate.getForObject(url, String.class);

    //验证Response
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(result).getAsJsonObject();
    Assert.assertEquals(200, root.get("code").getAsInt());
    Assert.assertEquals("success", root.get("message").getAsString());
    Assert.assertNotNull(root.get("data"));
    System.out.println("Response correct, assert success!");
    System.out.println("testGetMails result = " + result);
    return result;
  }

  //删除消息(0x 0004)
  private void removeMsg(String from, String to, List<String> msgIds) {
    HttpHeaders headers = getHttpHeaders();
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>();
    map.put("from", from);
    map.put("to", to);
    map.put("msgIds", msgIds);
    map.put("message", "empty");
    map.put("seqNo", 0);
    map.put("type", TemailType.TYPE_NORMAL_0);
    String s = gs.toJson(map);
    HttpEntity<String> formEntity = new HttpEntity<>(s, headers);
    ResponseEntity<String> result = restTemplate
        .exchange(usermailUrl + "/usermail/msg/remove", HttpMethod.PUT, formEntity, String.class);
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(result.getBody()).getAsJsonObject();
    Assert.assertEquals(200, root.get("code").getAsInt());
    Assert.assertEquals("success", root.get("message").getAsString());
    System.out.println("Response correct, assert success!");
    System.out.println("testRemoveMsg result = " + result);
  }

  //撤回消息(0x 0005)
  private void revert(String from, String to, String msgId) {
    HttpHeaders headers = getHttpHeaders();
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>();
    map.put("from", from);
    map.put("to", to);
    map.put("msgId", msgId);
    map.put("message", "empty");
    map.put("seqNo", 0);
    map.put("type", TemailType.TYPE_NORMAL_0);
    String s = gs.toJson(map);
    HttpEntity<String> formEntity = new HttpEntity<>(s, headers);
    ResponseEntity<String> result = restTemplate
        .exchange(usermailUrl + "/revert", HttpMethod.PUT, formEntity, String.class);
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(result.getBody()).getAsJsonObject();
    Assert.assertEquals(200, root.get("code").getAsInt());
    Assert.assertEquals("success", root.get("message").getAsString());
    System.out.println("Response correct, assert success!");
    System.out.println("testRevert result = " + result);
  }

  //阅后即焚消息已焚(0x 0006)
  private void destoryAfterRead(String from, String to, String msgId) {
    HttpHeaders headers = getHttpHeaders();
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>();
    map.put("from", from);
    map.put("to", to);
    map.put("msgId", msgId);
    map.put("message", "empty");
    map.put("seqNo", 0);
    map.put("type", TemailType.TYPE_DESTORY_AFTER_READ_1);
    String s = gs.toJson(map);
    HttpEntity<String> formEntity = new HttpEntity<>(s, headers);
    ResponseEntity<String> result = restTemplate
        .exchange(usermailUrl + "/usermail/msg/destory", HttpMethod.PUT, formEntity, String.class);
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(result.getBody()).getAsJsonObject();
    Assert.assertEquals(200, root.get("code").getAsInt());
    Assert.assertEquals("success", root.get("message").getAsString());
    System.out.println("Response correct, assert success!");
    System.out.println("testDestoryAfterRead result = " + result);
  }

  //todo fix 批量查询集成测试
  @Test
  public void batchQueryMsgs() {
    //版本1，usermailUrl变更为开发环境后，以下代码失效
    String from = "b@systoontest.com";
    String to = "c@systoontest.com";
    List<String> msgIds = new ArrayList<>();
    msgIds.add("6d19b1c4-2665-49aa-801b-eb40bc10f532");
    msgIds.add("74e3bccd-b894-42d0-b535-9cea0824147d");

    String msgIdsURLParam = msgIds.get(0) + "," + msgIds.get(1);

    String url = "http://localhost:8090" + "/usermail/msg?" + "from=" + from + "&to=" + to + "&msgIds=" + msgIdsURLParam;

    ResponseEntity<ResultDTO> result = restTemplate.getForEntity(url, ResultDTO.class);
    Assert.assertTrue(result.getStatusCode().is2xxSuccessful());
    ArrayList<LinkedHashMap> data = (ArrayList<LinkedHashMap>) result.getBody().getData();
    Assert.assertEquals(data.get(0).get("msgId"), msgIds.get(0));
    Assert.assertEquals(data.get(1).get("msgId"), msgIds.get(1));

    //版本2，正确的集成测试逻辑
//    String from = "a@systoontest.com";
//    String to = "b@systoontest.com";
//
//    String msgId1 = UUID.randomUUID().toString();
//    String msgId2 = UUID.randomUUID().toString();
//
//    //发送两条消息,一条存收件人，一条存发件人
//    createUsermail(from, to, "demo batch query msg-1", 1, msgId1, 0);
//    createUsermail(from, to, "demo batch query msg-1", 2, msgId1, 0);
//    createUsermail(from, to, "demo batch query msg-2", 1, msgId2, 0);
//    createUsermail(from, to, "demo batch query msg-2", 2, msgId2, 0);
//
//    String msgIdsURLParam = msgId1 + "," + msgId2;
//    String url = usermailUrl + "/usermail/msg?" + "from=" + from + "&to=" + to + "&msgIds=" + msgIdsURLParam;
//
//    ResponseEntity<ResultDTO> result = restTemplate.getForEntity(url, ResultDTO.class);
//    Assert.assertTrue(result.getStatusCode().is2xxSuccessful());
//    ArrayList<LinkedHashMap> data = (ArrayList<LinkedHashMap>) result.getBody().getData();
//    Assert.assertEquals(data.get(0).get("msgId"), msgId1);
//    Assert.assertEquals(data.get(1).get("msgId"), msgId2);
  }

  private HttpHeaders getHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
    headers.setContentType(type);
    headers.add("Accept", MediaType.APPLICATION_JSON.toString());
    headers.add(ParamsKey.HttpHeaderKey.CDTP_HEADER, "{CDTP-header:value}");
    headers.add(ParamsKey.HttpHeaderKey.X_PACKET_ID, "{xPacketId:value}");
    return headers;
  }

  private String generateDate(long time) {
    Calendar calendar = new GregorianCalendar();
    calendar.setTimeInMillis(time);
    return String.valueOf(calendar.get(Calendar.YEAR)) +
        String.valueOf(calendar.get(Calendar.MONTH) + 1) +
        String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
  }

  private long generateTimestamp() {
    return System.currentTimeMillis();
  }

}
