package com.syswin.temail.usermail.core.util;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.syswin.temail.usermail.common.ResultCodeEnum;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MsgCompressorTest {

  private static final String CHARSET_ENCODE = "utf-8";


  @InjectMocks
  private MsgCompressor msgCompressor;
  @Mock
  private GzipUtils gzipUtils;

  @Test
  public void zipByteDataSuccessfully() throws IOException {

    byte[] sourceBytes = "test".getBytes();
    byte[] zipBytes = "test-zip".getBytes();
    when(gzipUtils.zip(sourceBytes)).thenReturn(zipBytes);

    byte[] result = msgCompressor.zip(sourceBytes);

    assertThat(result).isEqualTo(zipBytes);
  }

  @Test
  public void zipByteDataIfCatchIoException() {

    byte[] sourceBytes = "test".getBytes();
    byte[] zipBytes = "test-zip".getBytes();
    try {
      when(gzipUtils.zip(sourceBytes)).thenThrow(IOException.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    byte[] result = new byte[0];
    try {
      result = msgCompressor.zip(sourceBytes);
    } catch (IllegalGmArgsException e) {
      assertThat(e.getResultCode()).isEqualTo(ResultCodeEnum.ERROR_MSG_ZIP);
    }
  }

  @Test
  public void zipWithDecodeSuccessfully() throws IOException {
    String sourceData = "test";
    byte[] sourceDecodeData = Base64.getUrlDecoder().decode(sourceData.getBytes(Charset.forName(CHARSET_ENCODE)));
    byte[] zipBytes = "zip-test".getBytes();
    when(gzipUtils.zip(sourceDecodeData)).thenReturn(zipBytes);

    byte[] result = msgCompressor.zipWithDecode(sourceData);
    assertThat(result).isEqualTo(zipBytes);
  }

  @Test
  public void zipWithDecodeIfCatchIoException() throws IOException {
    String sourceData = "test";
    byte[] sourceDecodeData = Base64.getUrlDecoder().decode(sourceData.getBytes(Charset.forName(CHARSET_ENCODE)));
    byte[] zipBytes = "zip-test".getBytes();
    when(gzipUtils.zip(sourceDecodeData)).thenReturn(zipBytes);

    byte[] result = new byte[0];
    try {
      result = msgCompressor.zipWithDecode(sourceData);
    } catch (IllegalGmArgsException e) {
      assertThat(e.getResultCode()).isEqualTo(ResultCodeEnum.ERROR_MSG_DECODE);
      assertThat(e.getMessage()).isEqualTo(sourceData);
    }
  }

  @Test
  public void unzipEncodeSuccessfully() throws IOException {
    byte[] sourceBytes = "test".getBytes();
    byte[] unzipBytes = "unzip-data".getBytes();
    when(gzipUtils.unzip(sourceBytes)).thenReturn(unzipBytes);

    String result = msgCompressor.unzipEncode(sourceBytes);

    assertThat(result).isEqualTo(new String(Base64.getUrlEncoder().encode(unzipBytes), CHARSET_ENCODE));
  }

  @Test
  public void unzipEncodeIfCatchIoException() throws IOException {
    byte[] sourceBytes = "test".getBytes();
    byte[] unzipBytes = "unzip-data".getBytes();
    when(gzipUtils.unzip(sourceBytes)).thenThrow(IOException.class);

    String result = null;
    try {
      result = msgCompressor.unzipEncode(sourceBytes);
    } catch (IllegalGmArgsException e) {
      assertThat(e.getResultCode()).isEqualTo(ResultCodeEnum.ERROR_MSG_ENCODE);
    }
  }


  @Test
  public void zipStringDataSuccessfully() throws IOException {
    String data = "test";

    byte[] zipData = "zipData".getBytes();
    when(gzipUtils.zip(data.getBytes(CHARSET_ENCODE))).thenReturn(zipData);

    byte[] result = msgCompressor.zip(data);

    assertThat(result).isEqualTo(zipData);

  }

  @Test
  public void zipStringDataIfCatchIoException() {
    String data = "test";

    byte[] zipData = "zipData".getBytes();
    try {
      when(gzipUtils.zip(data.getBytes(CHARSET_ENCODE))).thenThrow(IOException.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    byte[] result = new byte[0];
    try {
      result = msgCompressor.zip(data);
    } catch (IllegalGmArgsException e) {
      ResultCodeEnum resultCode = e.getResultCode();
      assertThat(resultCode).isEqualTo(ResultCodeEnum.ERROR_MSG_DECODE);
      assertThat(e.getMessage()).isEqualTo(data);
    }

  }

  @Test
  public void unzipSuccessfully() throws IOException {

    byte[] sourceBytes = "test".getBytes();
    String unzipData = "test-unzip";
    when(gzipUtils.unzip(sourceBytes)).thenReturn(unzipData.getBytes());

    String result = msgCompressor.unzip(sourceBytes);

    assertThat(result).isEqualTo(unzipData);

  }

  @Test
  public void unzipIfCatchIoException() {

    byte[] sourceBytes = "test".getBytes();
    String unzipData = "test-unzip";
    try {
      when(gzipUtils.unzip(sourceBytes)).thenThrow(IOException.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    String result = null;
    try {
      result = msgCompressor.unzip(sourceBytes);
    } catch (IllegalGmArgsException e) {
      assertThat(e.getResultCode()).isEqualTo(ResultCodeEnum.ERROR_MSG_ENCODE);
    }
  }
}