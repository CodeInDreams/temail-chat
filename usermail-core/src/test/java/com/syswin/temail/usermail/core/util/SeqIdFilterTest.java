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
    seqIdFilter = new SeqIdFilter(strFilter, after);

    long seqId = 4L;
    boolean result = seqIdFilter.filter(seqId);

    assertThat(result).isTrue();
  }

  @Test
  public void filterLimitedRegionAndBeforeFetch() {
    // 有限区间，向后拉取：after=false
    strFilter = "13_9,7_4,3_1";
    after = false;
    seqIdFilter = new SeqIdFilter(strFilter, after);

    long seqId = 4L;
    boolean result = seqIdFilter.filter(seqId);

    assertThat(result).isFalse();
  }

  @Test
  public void filterUnLimitedRegionAndAfterFetch() {

    // 有限区间，向后拉取：after=true
    strFilter = "3_5,7_-1";
    after = true;
    seqIdFilter = new SeqIdFilter(strFilter, after);

    long seqId = 9L;
    boolean result = seqIdFilter.filter(seqId);

    assertThat(result).isTrue();

  }

  @Test
  public void filterUnLimitedRegionAndBeforeFetch() {
    // 有限区间，向后拉取：after=false
    strFilter = "23_20,18_-1";
    after = false;
    seqIdFilter = new SeqIdFilter(strFilter, after);

    long seqId = 11L;
    boolean result = seqIdFilter.filter(seqId);

    assertThat(result).isTrue();
  }

  @Test
  public void filterExpectCatchException() {
    strFilter = "23,25";
    after = true;
    try {
      seqIdFilter = new SeqIdFilter(strFilter, after);
    } catch (IllegalGmArgsException e) {
      assertThat(e.getResultCode()).isEqualTo(ResultCodeEnum.ERROR_FILTER_SEQIDS);
      assertThat(e.getMessage()).isEqualTo(strFilter);
    }
  }
}