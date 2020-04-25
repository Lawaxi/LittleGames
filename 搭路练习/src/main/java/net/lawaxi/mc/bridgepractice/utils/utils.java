package net.lawaxi.mc.bridgepractice.utils;

import java.util.Calendar;

public class utils {

    //取小数点后一位
    public static double getDouble(double d){
        return (int)(d*10)/10.0;
    }
    public static int getSecond(){
        return Calendar.getInstance().get(Calendar.SECOND)*1000+Calendar.getInstance().get(Calendar.MILLISECOND);
    }
}
