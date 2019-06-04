package com.syswin.temail.usermail;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class Tests {

  @Test
  public void testStringHashCode() {
    System.out.println("abc QAWERGHJ,MJYTWE `Q1QERTHJYT4R2".hashCode());

    String str = "a,b,c,,";
    String[] ary = str.split(","); // 预期大于 3，结果是 3
    System.out.println(ary.length);

    List list = new ArrayList();
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);
    list.add(5);

    List l2 = list.subList(1,3);
    l2.add(1,"a");
    System.out.println(list);
    System.out.println(l2);


  }
}
