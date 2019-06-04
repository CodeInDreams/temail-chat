package com.syswin.temail.usermail.core.util;

import com.syswin.temail.usermail.common.Contants.RESULT_CODE;
import com.syswin.temail.usermail.core.exception.IllegalGMArgsException;
import java.util.ArrayList;
import java.util.List;

public class SeqIdFilter {

  private List<Long> existSeqId;
  private long last = -1L;
  private boolean after = true;

  public SeqIdFilter(String strFilter, boolean after) {
    this.after = after;
    init(strFilter);
  }

  public SeqIdFilter(String strFilter) {
    init(strFilter);
  }

  private void init(String strFilter) {
    try {
      String[] ss = strFilter.split(",");
      existSeqId = new ArrayList<>();
      for (int i = 0; i < ss.length; i++) {
        String tmp = ss[i];
        String[] sss = tmp.split("_");
        long start = Long.parseLong(sss[0]);
        long end = Long.parseLong(sss[1]);
        if (end == -1) {
          last = start;
        } else {
          if (after) {
            for (long j = start + 1; j < end; j++) {
              existSeqId.add(j);
            }
          } else {
            for (long j = start - 1; j > end; j--) {
              existSeqId.add(j);
            }
          }
        }
      }
    } catch (Exception e) {
      throw new IllegalGMArgsException(RESULT_CODE.ERROR_FILTER_SEQIDS, strFilter);
    }
  }

  public boolean filter(long seqId) {
    return existSeqId.contains(seqId) || lastSeqId(seqId);
  }

  private boolean lastSeqId(long seqId) {
    if (last == -1l) {
      return false;
    }
    if (after) {
      return (seqId > last);
    } else {
      return (seqId < last);
    }
  }
}