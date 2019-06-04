package com.syswin.temail.usermail.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

class GzipUtils {

  public byte[] zip(final byte[] data) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    GZIPOutputStream gzip = new GZIPOutputStream(bos);
    gzip.write(data);
    gzip.finish();
    return bos.toByteArray();
  }

  public byte[] unzip(final byte[] data) throws IOException {
    ByteArrayInputStream bis = new ByteArrayInputStream(data);
    GZIPInputStream gzip = new GZIPInputStream(bis);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    return unzipByteArrayOutputStream(gzip, bos).toByteArray();
  }

  private ByteArrayOutputStream unzipByteArrayOutputStream(GZIPInputStream gzip, ByteArrayOutputStream bos)
      throws IOException {
    byte[] buf = new byte[1024];
    int num = -1;
    while ((num = gzip.read(buf, 0, buf.length)) != -1) {
      bos.write(buf, 0, num);
    }
    return bos;
  }

}