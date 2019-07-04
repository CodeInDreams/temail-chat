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

package com.syswin.temail.usermail.infrastructure.domain.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.syswin.temail.usermail.domains.UsermailBoxDO;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailBoxMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UsermailBoxRepoImplTest {

  @InjectMocks
  private UsermailBoxRepoImpl usermailBoxRepoImpl;
  @Mock
  private UsermailBoxMapper usermailBoxMapper;

  @Test
  public void testSaveUsermailBox() {
    ArgumentCaptor<UsermailBoxDO> usermailBoxCap = ArgumentCaptor.forClass(UsermailBoxDO.class);
    UsermailBoxDO usermailBox = new UsermailBoxDO(1,"sessionId","mail2","a.test@", "sessionExtData");
    usermailBoxRepoImpl.saveUsermailBox(usermailBox);
    Mockito.verify(usermailBoxMapper).saveUsermailBox(usermailBoxCap.capture());
    assertThat(usermailBoxCap.getValue()).isEqualTo(usermailBox);
  }

  @Test
  public void testListUsermailBoxsByOwner() {
    String mail1 = "mail1";
    int archiveStatus = 0;
    List<UsermailBoxDO> usermailBoxList = new ArrayList<>();
    Mockito.when(usermailBoxMapper.listUsermailBoxsByOwner(mail1, archiveStatus)).thenReturn(usermailBoxList);
    List<UsermailBoxDO> result = usermailBoxRepoImpl.listUsermailBoxsByOwner(mail1, archiveStatus);
    Mockito.verify(usermailBoxMapper).listUsermailBoxsByOwner(mail1, archiveStatus);
    assertThat(result).isEqualTo(usermailBoxList);
  }

  @Test
  public void testDeleteUsermailBox() {
    String from = "from";
    String to = "to";
    ArgumentCaptor<String> fromCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> toCap = ArgumentCaptor.forClass(String.class);
    usermailBoxRepoImpl.deleteByOwnerAndMail2(from,to);
    Mockito.verify(usermailBoxMapper).deleteByOwnerAndMail2(fromCap.capture(),toCap.capture());
    assertThat(fromCap.getValue()).isEqualTo(from);
    assertThat(toCap.getValue()).isEqualTo(to);
  }

  @Test
  public void testListUsermailBoxsByOwnerAndTo() {
    String fromStr = "from";
    String toStr = "to";
    ArgumentCaptor<String> fromCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> toCap = ArgumentCaptor.forClass(String.class);
    usermailBoxRepoImpl.listUsermailBoxsByOwnerAndTo(fromStr,toStr);
    Mockito.verify(usermailBoxMapper).listUsermailBoxsByOwnerAndTo(fromCap.capture(),toCap.capture());
    assertThat(fromCap.getValue()).isEqualTo(fromStr);
    assertThat(toCap.getValue()).isEqualTo(toStr);
  }

  @Test
  public void updateArchiveStatus() {
    String from = "from";
    String to = "to";
    int archiveStatus = 0;
    ArgumentCaptor<String> fromCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> toCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> archiveStatusCap = ArgumentCaptor.forClass(Integer.class);
    usermailBoxRepoImpl.updateArchiveStatus(from,to,archiveStatus);
    Mockito.verify(usermailBoxMapper).updateArchiveStatus(fromCap.capture(),toCap.capture(),archiveStatusCap.capture());
    assertThat(fromCap.getValue()).isEqualTo(from);
    assertThat(toCap.getValue()).isEqualTo(to);
    assertThat(archiveStatusCap.getValue()).isEqualTo(archiveStatus);
  }

  @Test
  public void testGetUsermailBox() {
    String owner = "ower";
    String to = "to";
    ArgumentCaptor<String> owerCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> toCap = ArgumentCaptor.forClass(String.class);
    usermailBoxRepoImpl.selectByOwnerAndMail2(owner,to);
    Mockito.verify(usermailBoxMapper).selectByOwnerAndMail2(owerCap.capture(),toCap.capture());
    assertThat(owerCap.getValue()).isEqualTo(owner);
    assertThat(toCap.getValue()).isEqualTo(to);
  }

  @Test
  public void updateSessionExtDataTest() {
    UsermailBoxDO usermailBoxDO = new UsermailBoxDO("owner", "mail2", "sessionExtData");
    usermailBoxRepoImpl.updateSessionExtData(usermailBoxDO);
    Mockito.verify(usermailBoxMapper).updateSessionExtData(usermailBoxDO);
  }


  @Test
  public void testGetTopNMailboxes(){
    String from = "from@t.email";
    usermailBoxRepoImpl.selectTopNByOwner(from,0);
    ArgumentCaptor<String> fromCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    Mockito.verify(usermailBoxMapper).selectTopNByOwner(fromCaptor.capture(),statusCaptor.capture());
    assertThat(fromCaptor.getValue()).isEqualTo(from);
  }

  @Test
  public void removeDomainTest() {
    String domain = "domain";
    int pageSize = 100;

    usermailBoxRepoImpl.deleteDomain(domain, pageSize);
    verify(usermailBoxMapper).deleteDomain("%@" + domain, pageSize);
  }

}
