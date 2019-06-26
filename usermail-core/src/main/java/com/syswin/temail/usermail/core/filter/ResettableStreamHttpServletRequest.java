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

package com.syswin.temail.usermail.core.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;

public class ResettableStreamHttpServletRequest extends
    HttpServletRequestWrapper {

  private byte[] rawData;
  private HttpServletRequest request;
  private ResettableStreamHttpServletRequest.ResettableServletInputStream servletStream;

  public ResettableStreamHttpServletRequest(HttpServletRequest request) {
    super(request);
    this.request = request;
    this.servletStream = new ResettableStreamHttpServletRequest.ResettableServletInputStream();
  }


  public void resetInputStream() {
    servletStream.stream = new ByteArrayInputStream(rawData);
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {
    if (rawData == null) {
      rawData = IOUtils.toByteArray(this.request.getReader(), Charset.defaultCharset());
      servletStream.stream = new ByteArrayInputStream(rawData);
    }
    return servletStream;
  }

  @Override
  public BufferedReader getReader() throws IOException {
    if (rawData == null) {
      rawData = IOUtils.toByteArray(this.request.getReader(), Charset.defaultCharset());
      servletStream.stream = new ByteArrayInputStream(rawData);
    }
    return new BufferedReader(new InputStreamReader(servletStream));
  }


  public class ResettableServletInputStream extends ServletInputStream {

    private InputStream stream;

    @Override
    public int read() throws IOException {
      return stream.read();
    }

    @Override
    public boolean isFinished() {
      return false;
    }

    @Override
    public boolean isReady() {
      return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
      // Do nothing
    }
  }
}