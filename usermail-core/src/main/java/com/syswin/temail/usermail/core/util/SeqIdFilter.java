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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SeqIdFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(SeqIdFilter.class);

  private static final int MAX_SEQ_ID_SIZE = 100;

  private List<Long> existSeqId;
  private long last = -1L;
  private boolean after = true;
  private long dataBeginSeqId = -1L;
  private long dataEndSeqId = -1L;

  public SeqIdFilter(String strFilter, boolean after, long dataBeginSeqId, long dataEndSeqId) {
    this.after = after;
    this.dataBeginSeqId = dataBeginSeqId;
    this.dataEndSeqId = dataEndSeqId;
    if(after) {
      this.dataBeginSeqId = Math.min(dataBeginSeqId, dataEndSeqId);
      this.dataEndSeqId = Math.max(dataBeginSeqId, dataEndSeqId);
    }
    init(strFilter, this.dataBeginSeqId, this.dataEndSeqId);
  }

  public SeqIdFilter(String strFilter) {
    init(strFilter, dataBeginSeqId, dataEndSeqId);
  }

  private void init(String strFilter, long dataBeginSeqId, long dataEndSeqId) {
    try {
      String[] filters = strFilter.split(",");
      existSeqId = new ArrayList<>(MAX_SEQ_ID_SIZE);
      for (int i = 0; i < filters.length; i++) {
        String tmp = filters[i];
        String[] filterRange = tmp.split("_");
        long rangeBeginSeqId = Long.parseLong(filterRange[0]);
        long rangeEndSeqId = Long.parseLong(filterRange[1]);
        if (rangeEndSeqId == -1) {
          last = rangeBeginSeqId;
        } else {
          if (after) {
            rangeBeginSeqId = rangeBeginSeqId + 1;
            rangeBeginSeqId = Math.max(rangeBeginSeqId, dataBeginSeqId);

            rangeEndSeqId = rangeEndSeqId - 1;
            rangeEndSeqId = Math.min(rangeEndSeqId, rangeEndSeqId);

            for (long j = rangeBeginSeqId; j <= rangeEndSeqId; j++) {
              existSeqId.add(j);
              if (existSeqId.size() >= MAX_SEQ_ID_SIZE){
                LOGGER.warn("existSeqId List Size is full, strFiler={}, dataBeginSeqId={}, dataEndSeqId={}", strFilter, dataBeginSeqId, dataEndSeqId);
                break;
              }
            }
          } else {
            rangeBeginSeqId = rangeBeginSeqId - 1;
            rangeBeginSeqId = Math.min(rangeBeginSeqId, dataBeginSeqId);

            rangeEndSeqId = rangeEndSeqId + 1;
            rangeEndSeqId = Math.max(rangeEndSeqId, dataEndSeqId);

            for (long j = rangeBeginSeqId; j >= rangeEndSeqId; j--) {
              existSeqId.add(j);
              if (existSeqId.size() >= MAX_SEQ_ID_SIZE){
                LOGGER.warn("existSeqId List Size is full, strFiler={}, dataBeginSeqId={}, dataEndSeqId={}", strFilter, dataBeginSeqId, dataEndSeqId);
                break;
              }
            }
          }
        }
      }
    } catch (Exception e) {
      throw new IllegalGmArgsException(ResultCodeEnum.ERROR_FILTER_SEQIDS, strFilter);
    }
  }

  public boolean filter(long seqId) {
    return existSeqId.contains(seqId) || lastSeqId(seqId);
  }

  private boolean lastSeqId(long seqId) {
    if (last == -1L) {
      return false;
    }
    if (after) {
      return (seqId > last);
    } else {
      return (seqId < last);
    }
  }
}