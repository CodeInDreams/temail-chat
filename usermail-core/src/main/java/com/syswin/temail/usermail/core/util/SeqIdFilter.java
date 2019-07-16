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
  private long actualBeginSeqId = -1l;
  private long actualEndSeqId = -1l;

  public SeqIdFilter(String strFilter, boolean after, long actualBeginSeqId, long actualEndSeqId) {
    this.after = after;
    this.actualBeginSeqId = actualBeginSeqId;
    this.actualEndSeqId = actualEndSeqId;
    //兼容查询参数seqNo为0，升序查询的场景
    if(after && actualBeginSeqId > actualEndSeqId){
      this.actualBeginSeqId = actualEndSeqId;
      this.actualEndSeqId = actualBeginSeqId;
    }
    init(strFilter, this.actualBeginSeqId, this.actualEndSeqId);
  }

  public SeqIdFilter(String strFilter, long actualBeginSeqId, long actualEndSeqId) {
    init(strFilter, actualBeginSeqId, actualEndSeqId);
  }

  private void init(String strFilter, long actualBeginSeqId, long actualEndSeqId) {
    try {
      String[] filters = strFilter.split(",");
      existSeqId = new ArrayList<>(MAX_SEQ_ID_SIZE);
      for (int i = 0; i < filters.length; i++) {
        String tmp = filters[i];
        String[] filterRange = tmp.split("_");
        long expectedBeginSeqId = Long.parseLong(filterRange[0]);
        long expectedEndSeqId = Long.parseLong(filterRange[1]);
        if (expectedEndSeqId == -1) {
          last = expectedBeginSeqId;
        } else {
          if (after) {
            if(expectedBeginSeqId  < actualBeginSeqId){
              expectedBeginSeqId = actualBeginSeqId - 1;
            }
            if(expectedEndSeqId > actualEndSeqId){
              expectedEndSeqId = actualEndSeqId + 1;
            }
            for (long j = expectedBeginSeqId + 1; j < expectedEndSeqId; j++) {
              existSeqId.add(j);
              if (existSeqId.size() >= MAX_SEQ_ID_SIZE){
                LOGGER.warn("Usermail existSeqId List Size is full, strFiler={}, actualBeginSeqId={}, actualEndSeqId={}", strFilter, actualBeginSeqId, actualEndSeqId);
                break;
              }
            }
          } else {
            if(expectedBeginSeqId > actualBeginSeqId){
              expectedBeginSeqId = actualBeginSeqId + 1;
            }
            if(expectedEndSeqId < actualEndSeqId){
              expectedEndSeqId = actualEndSeqId - 1;
            }
            for (long j = expectedBeginSeqId - 1; j > expectedEndSeqId; j--) {
              existSeqId.add(j);
              if (existSeqId.size() >= MAX_SEQ_ID_SIZE){
                LOGGER.warn("Usermail existSeqId List Size is full, strFiler={}, actualBeginSeqId={}, actualEndSeqId={}", strFilter, actualBeginSeqId, actualEndSeqId);
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