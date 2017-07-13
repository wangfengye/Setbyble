package com.ascend.wangfeng.setbyble;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testChar() {
        final String finalValue = "MAC:123";
        char firstChar = finalValue.toCharArray()[0];
        int firstAscii = firstChar;
        System.out.print((int) firstChar);
        if (firstAscii >= 65 && firstAscii <= 90) {
            //请求回调
            String[] params = finalValue.split(":");
            System.out.print(params[0] + "\n" + params[1]);
        }
    }
    @Test
    public void testGetData(){
        String subValue="";
        String separator = "a";
        String value ="bbbba456afsf";
        int end = 0;
        while (end==0){
            end =value.indexOf(separator);
            if (end == -1) return;
            subValue =value.substring(0,end);
            value = value.substring(end+1,value.length());
        }
        System.out.println("sub: "+subValue);
        System.out.println("extra: "+value);
    }
    public static int countStr(String str1, String str2) {
        String tag = str1;
        int count = 0;
        while (tag.indexOf(str2) != -1) {
            count++;
             tag =tag.substring(0,tag.indexOf(str2))+tag.substring(tag.indexOf(str2)+str2.length(),tag.length());
        }
        return count;
    }
    @Test
    public  void testString(){
        String s = "";
        System.out.print(s== null);
        System.out.print("".equals(s));
    }

}