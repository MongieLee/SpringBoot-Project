package cn.ml.utils;

public class AssertUtils {
    public static void assertTrue(boolean value,String msg){
        if(!value){
            throw new IllegalArgumentException(msg);
        }
    }
}
