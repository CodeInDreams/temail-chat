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

package com.syswin.temail.usermail.core.util;

import com.syswin.temail.usermail.common.ResultCodeEnum;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

public class MsgCompressor {

  private GzipUtils gzip = new GzipUtils();
  private static final String CHARSET_ENCODE = "utf-8";

  public byte[] zip(byte[] data) {
    try {
      return gzip.zip(data);
    } catch (IOException e) {
      throw new IllegalGmArgsException(ResultCodeEnum.ERROR_MSG_ZIP);
    }
  }

  public byte[] zip(final String data) {
    byte[] zip;
    try {
      zip = gzip.zip(data.getBytes(CHARSET_ENCODE));
    } catch (IOException e) {
      throw new IllegalGmArgsException(ResultCodeEnum.ERROR_MSG_DECODE, data);
    }
    return zip;
  }

  public byte[] zipWithDecode(final String data) {
    byte[] zip;
    try {
      zip = gzip.zip(Base64.getUrlDecoder().decode(data.getBytes(Charset.forName(CHARSET_ENCODE))));
    } catch (IOException e) {
      throw new IllegalGmArgsException(ResultCodeEnum.ERROR_MSG_DECODE, data);
    }
    return zip;
  }

  public String unzipEncode(final byte[] data) {
    String s;
    try {
      s = new String(Base64.getUrlEncoder().encode(gzip.unzip(data)), CHARSET_ENCODE);
    } catch (IOException e) {
      throw new IllegalGmArgsException(ResultCodeEnum.ERROR_MSG_ENCODE);
    }
    return s;
  }

  public String unzip(final byte[] data) {
    String s;
    try {
      s = new String(gzip.unzip(data), CHARSET_ENCODE);
    } catch (IOException e) {
      throw new IllegalGmArgsException(ResultCodeEnum.ERROR_MSG_ENCODE);
    }
    return s;
  }
}
