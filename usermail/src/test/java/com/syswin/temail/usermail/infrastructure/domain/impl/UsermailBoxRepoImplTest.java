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

import com.syswin.temail.usermail.domains.UsermailBoxDO;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailBoxMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsermailBoxRepoImplTest {

  private UsermailBoxRepoImpl usermailBoxRepoImpl;
  private UsermailBoxMapper usermailBoxMapper;

  @Before
  public void setup(){
    this.usermailBoxMapper = Mockito.mock(UsermailBoxMapper.class);
    this.usermailBoxRepoImpl = new UsermailBoxRepoImpl(usermailBoxMapper);
  }

  @Test
  public void testSaveUsermailBox() {
    ArgumentCaptor<UsermailBoxDO> usermailBoxCap = ArgumentCaptor.forClass(UsermailBoxDO.class);
    UsermailBoxDO usermailBox = new UsermailBoxDO(1,"sessionId","mail2","a.test@");
    usermailBoxRepoImpl.saveUsermailBox(usermailBox);
    Mockito.verify(usermailBoxMapper).saveUsermailBox(usermailBoxCap.capture());
    assertThat(usermailBoxCap.getValue()).isEqualTo(usermailBox);
  }

  @Test
  public void testListUsermailBoxsByOwner() {
    String mail1 = "mail1";
    int archiveStatus = 0;
    ArgumentCaptor<String> mailCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> archiveStatusCap = ArgumentCaptor.forClass(Integer.class);
    List<UsermailBoxDO> usermailBoxList = new ArrayList<UsermailBoxDO>();
    usermailBoxRepoImpl.listUsermailBoxsByOwner(mail1,archiveStatus);
    Mockito.verify(usermailBoxMapper).listUsermailBoxsByOwner(mailCap.capture(),archiveStatusCap.capture());
    assertThat(mailCap.getValue()).isEqualTo(mail1);
    assertThat(archiveStatusCap.getValue()).isEqualTo(archiveStatus);
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
}
