package com.td;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {



    @Test
     public  void testOne(){
         Pattern pattern = Pattern.compile("[F,f]rom");
         Matcher from = pattern.matcher("From");
         System.out.println(from.find());
     }

     @Test
     public  void testTwo(){
         Pattern pattern = Pattern.compile(".发件人:.{0,}@.{0,}");
         Matcher matcher = pattern.matcher("<div> <b>发件人:</b> xiang jiali &lt;jialixiang.john.test@outlook.com&gt; ");
         boolean b = matcher.find();
         System.out.println(b);
     }
}
