package com.syswin.temail.usermail.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.Test;

public class GzipUtilsTest {

  private GzipUtils gzipUtils = new GzipUtils();

  @Test
  public void zipTest() throws IOException {
    byte[] msg = "tset".getBytes();
    byte[] zipMsg = gzipUtils.zip(msg);

    assertThat(zipMsg).isNotNull();

    byte[] unzipMsg = gzipUtils.unzip(zipMsg);

    assertThat(unzipMsg).isEqualTo(msg);

  }

}