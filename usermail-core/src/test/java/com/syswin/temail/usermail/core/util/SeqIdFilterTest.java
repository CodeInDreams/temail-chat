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

import static org.assertj.core.api.Assertions.assertThat;

import com.syswin.temail.usermail.common.ResultCodeEnum;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import org.junit.Test;

public class SeqIdFilterTest {

  private SeqIdFilter seqIdFilter;
  String strFilter;
  boolean after;


  @Test
  public void filterLimitedRegionAndAfterFetch() {

    // 有限区间，向后拉取：after=true
    strFilter = "3_5,7_10";
    after = true;
    seqIdFilter = new SeqIdFilter(strFilter, after, 1, 11);

    long seqId = 4L;
    boolean result = seqIdFilter.filter(seqId);

    assertThat(result).isTrue();
  }

  @Test
  public void filterLimitedRegionAndBeforeFetch() {
    // 有限区间，向后拉取：after=false
    strFilter = "13_9,7_4,3_1";
    after = false;
    seqIdFilter = new SeqIdFilter(strFilter, after, 0, 14);

    long seqId = 4L;
    boolean result = seqIdFilter.filter(seqId);

    assertThat(result).isFalse();
  }

  @Test
  public void filterUnLimitedRegionAndAfterFetch() {

    // 有限区间，向后拉取：after=true
    strFilter = "3_5,7_-1";
    after = true;
    seqIdFilter = new SeqIdFilter(strFilter, after, 9, 0);

    long seqId = 9L;
    boolean result = seqIdFilter.filter(seqId);

    assertThat(result).isTrue();

  }

  @Test
  public void filterUnLimitedRegionAndBeforeFetch() {
    // 有限区间，向后拉取：after=false
    strFilter = "23_20,18_-1";
    after = false;
    seqIdFilter = new SeqIdFilter(strFilter, after, 24, 0);

    long seqId = 11L;
    boolean result = seqIdFilter.filter(seqId);

    assertThat(result).isTrue();
  }

  @Test
  public void filterExpectCatchException() {
    strFilter = "23,25";
    after = true;
    try {
      seqIdFilter = new SeqIdFilter(strFilter, after, 0, 26);
    } catch (IllegalGmArgsException e) {
      assertThat(e.getResultCode()).isEqualTo(ResultCodeEnum.ERROR_FILTER_SEQIDS);
      assertThat(e.getMessage()).isEqualTo(strFilter);
    }
  }
}