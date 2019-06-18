package com.syswin.temail.usermail.interfaces;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.common.Constants.TemailStoreType;
import com.syswin.temail.usermail.common.Constants.TemailType;
import com.syswin.temail.usermail.common.ParamsKey;
import com.syswin.temail.usermail.domains.UsermailMsgReplyDO;
import com.syswin.temail.usermail.dto.UsermailMsgReplyDTO;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
 * 单聊回复集成测试
 */
public class UsermailMsgReplyInterfaceTest {

  private static final String usermailUrl = "http://127.0.0.1:8090";
  private RestTemplate restTemplate = new RestTemplate();

  @Test
  @Ignore
  public void testUsermailMsgReply() throws InterruptedException {
    UsermailMsgReplyDTO msgReply = createMsg(TemailType.TYPE_NORMAL_0, TemailStoreType.STORE_TYPE_FROM_2);
    UsermailMsgReplyDTO msgReply2 = createMsg(TemailType.TYPE_NORMAL_0, TemailStoreType.STORE_TYPE_FROM_2);

    //发送一条回复消息
    createMsyReply(msgReply.getFrom(), msgReply.getTo(), msgReply.getMsgId()
        , msgReply.getType(), msgReply.getMsgIds(), msgReply.getStoreType(), msgReply.getParentMsgId(),
        msgReply.getMsgData(), msgReply.getAttachmentSize());

    //撤回单聊回复消息
    revertMsgReply(msgReply.getFrom(), msgReply.getTo(), msgReply.getMsgId());
    ResponseEntity<String> replyMsgs = getReplyMsgs(msgReply.getFrom(), msgReply.getParentMsgId());
    assertAfterRevert(replyMsgs, msgReply.getParentMsgId());

    //删除单聊回复消息
    //UsermailMsgReplyDTO msgReply2 = createMsg(TemailType.TYPE_NORMAL_0, TemailStoreType.STORE_TYPE_TO_1);

    createMsyReply(msgReply2.getFrom(), msgReply2.getTo(), msgReply2.getMsgId()
        , msgReply2.getType(), msgReply2.getMsgIds(), msgReply2.getStoreType(), msgReply2.getParentMsgId(),
        msgReply2.getMsgData(), msgReply2.getAttachmentSize());
    removeMsgReply(msgReply2.getFrom(), msgReply2.getTo(), msgReply2.getMsgIds(), msgReply2.getMsgId());
    ResponseEntity<String> deletereplyMsg = getReplyMsgs(msgReply2.getFrom(), msgReply2.getParentMsgId());
    assertAfterRemove(deletereplyMsg, msgReply2.getParentMsgId());

    //拉取消息
    getReplyMsgs(msgReply2.getFrom(), msgReply2.getParentMsgId());

  }

  private void assertAfterRemove(ResponseEntity<String> deletereplyMsg, String parentMsgId) {
    UsermailMsgReplyDO lastMsgInfos = getLastMsgInfo(deletereplyMsg);
    Assert.assertEquals(lastMsgInfos.getParentMsgid(), parentMsgId);
    //Assert.assertEquals(lastMsgInfos.getStatus(), TemailStatus.STATUS_DELETE_3);
    System.out.println("removeMsgReply success--------------------------");
  }

  private void assertAfterRevert(ResponseEntity<String> replyMsgs, String parentMsgId) {
    UsermailMsgReplyDO lastMsgInfos = getLastMsgInfo(replyMsgs);
    Assert.assertEquals(lastMsgInfos.getParentMsgid(), parentMsgId);
    Assert.assertEquals(TemailStatus.STATUS_REVERT_1, lastMsgInfos.getStatus());
    System.out.println("revertMsgReply success--------------------------");
  }

  private UsermailMsgReplyDO getLastMsgInfo(ResponseEntity<String> responseEntity) {
    GsonBuilder builder = new GsonBuilder();
    // Register an adapter to manage the date types as long values
    builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
      public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Date(json.getAsJsonPrimitive().getAsLong());
      }
    });

    Gson gson = builder.create();
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(responseEntity.getBody()).getAsJsonObject();
    JsonObject lastMsgObject = root.get("data").getAsJsonArray().get(0).getAsJsonObject();
    UsermailMsgReplyDO lastMsg = gson.fromJson(lastMsgObject, UsermailMsgReplyDO.class);
    lastMsg.setParentMsgid(lastMsgObject.get("parentMsgId").getAsString());
    return lastMsg;
  }

  private String createMsyReply(String from, String to, String msgId, int type, List<String> msgIds, int storeType,
      String parentMsgId, String message, int attachmentSize) {
    HttpHeaders httpHeaders = getHttpHeaders();
    Map<String, Object> map = new HashMap<>();

    Gson gs = new Gson();

    map.put("from", from);
    map.put("to", to);
    map.put("msgId", msgId);
    map.put("type", type);
    map.put("msgIds", msgIds);
    map.put("storeType", storeType);
    map.put("parentMsgId", parentMsgId);
    map.put("message", message);
    map.put("attachmentSize", attachmentSize);

    String s = gs.toJson(map);

    HttpEntity<String> formEntity = new HttpEntity<>(s, httpHeaders);

    ResponseEntity<String> response = restTemplate
        .exchange(usermailUrl + "/usermail/msg/reply", HttpMethod.POST, formEntity, String.class);
    String result = response.getBody();
    //验证response
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(result).getAsJsonObject();
    Assert.assertEquals(200, root.get("code").getAsInt());
    Assert.assertEquals("success", root.get("message").getAsString());
    Assert.assertNotNull(root.get("data").getAsJsonObject());
    System.out.println("Response correct, assert success!");
    System.out.println("test result = " + result);
    return result;
  }


  private UsermailMsgReplyDTO createMsg(int msgType, int stortType) {
    long timestampLong = generateTimestamp();
    String timestamp = String.valueOf(timestampLong);
    String date = generateDate(timestampLong);
    String from = "A" + 2018;
    String to = "B" + 2018;
    String msg = "message" + date;
    String msgId = "syswin-" + timestamp;
    List<String> msgIds = new ArrayList<>();
    msgIds.add(msgId);
    String parentMsgId = "syswin-1543456947958";
    int attachmentSize = 100;
    UsermailMsgReplyDTO usermailMsgReplyDto = new UsermailMsgReplyDTO(msgId, from, to, msgType, msg, parentMsgId,
        attachmentSize, msgIds, stortType);
    return usermailMsgReplyDto;
  }

  private void revertMsgReply(String from, String to, String msgId) {
    HttpHeaders headers = getHttpHeaders();
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>();
    long timestampLong = generateTimestamp();
    String date = generateDate(timestampLong);
    String msg = "message" + date;
    String parentMsgId = "syswin-1543370025665";
    int attachmentSize = 100;
    map.put("from", from);
    map.put("to", to);
    map.put("msgId", msgId);
    map.put("parentMsgId", parentMsgId);
    map.put("message", msg);
    map.put("attachmentSize", attachmentSize);
    map.put("type", TemailType.TYPE_NORMAL_0);
    String s = gs.toJson(map);
    HttpEntity<String> formEntity = new HttpEntity<>(s, headers);
    ResponseEntity<String> result = restTemplate
        .exchange(usermailUrl + "/usermail/msg/reply", HttpMethod.PUT, formEntity, String.class);
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(result.getBody()).getAsJsonObject();
    Assert.assertEquals(200, root.get("code").getAsInt());
    Assert.assertEquals("success", root.get("message").getAsString());
    System.out.println("Response correct, assert success!");
    System.out.println("testRevert result = " + result);
  }

  public void removeMsgReply(String from, String to, List<String> msgIds, String msgId) {
    HttpHeaders httpHeaders = getHttpHeaders();
    Map<String, Object> map = new HashMap<>();
    String parentMsgId = "syswin-1543456947958";
    int type = TemailType.TYPE_NORMAL_0;
    msgIds.add("skjgi");
    map.put("from", from);
    map.put("to", to);
    map.put("msgIds", msgIds);
    map.put("parentMsgId", parentMsgId);
    map.put("type", type);

    Gson gs = new Gson();
    String s = gs.toJson(map);

    HttpEntity<String> formEntity = new HttpEntity<>(s, httpHeaders);
    ResponseEntity<String> result = restTemplate
        .exchange(usermailUrl + "/usermail/msg/reply", HttpMethod.DELETE, formEntity, String.class);
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(result.getBody()).getAsJsonObject();
    Assert.assertEquals(200, root.get("code").getAsInt());
    Assert.assertEquals("success", root.get("message").getAsString());
    System.out.println("Response correct, assert success!");
    System.out.println("testRemoveMsgReply result = " + result);

  }

  public ResponseEntity<String> getReplyMsgs(String owner, String parentMsgId) {
    HttpHeaders httpHeaders = getHttpHeaders();
    long seqNo = 0;
    String signal = "before";
    Map<String, Object> map = new HashMap<>();

    String url =
        usermailUrl + "/usermail/msg/reply?owner=" + owner + "&parentMsgid=" + parentMsgId + "&seqId=" + seqNo + "&signal="
            + signal;
    HttpEntity<String> entity = new HttpEntity<String>(null, httpHeaders);
    ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class, map);
    JsonParser jsonParser = new JsonParser();
    JsonObject root = jsonParser.parse(result.getBody()).getAsJsonObject();
    Assert.assertEquals(200, root.get("code").getAsInt());
    Assert.assertEquals("success", root.get("message").getAsString());
    System.out.println("Response correct, assert success!");
    System.out.println("testGetMsgReply result = " + result);
    return result;
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