package kgs.com.openglintro.utils;

import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.util.Log;



import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;


/**
 * Created by developerios on 5/8/18.
 */

public class CalcUtils {
    /*
        r1 / r2 = num / dNum

        So , r1 = r2 * (num / dNum)
     */


    public static int getRatioR(int r2, int nNum, int dNum) {
        double ratio = (nNum * 1.0) / dNum;
        int r1 = (int) (ratio * r2);
        return r1;
    }

    /*


     */
    public static int getPixelFromDuration(long duration, int msPerPix) {
        if (duration < 0) return 0;
        int pixel = 0;
        pixel = (int) (duration / msPerPix);
        return pixel;
    }

    /*
      Calculation how many pixels to scrool according to selected video

     */





    public static int getDurationPerPixel(int duration, int width) {
        int calcDuration = 0;
        calcDuration = duration / width;
        return calcDuration;
    }

    public static int getPixelValue(int duration, int total, int width) {
        int pixel = 0;
        pixel = (width * duration) / total;
        return pixel;
    }




    public static String getTimeString(int value) {
        int seconds = getSeconds(value);
        //Log.d(TAG, "getTimeString: " + seconds + " mili " + value);
        int min = seconds / 60;
        String result = timeTypeString(min);
        result += ":";
        int sec = seconds % 60;
        result += timeTypeString(sec);
        return result;
    }

    public static int getSeconds(int milis) {
        if (milis < 0) return 0;
        int result = 0;
//        if (milis % 1000 >= 500) {
//            result++;
//        }
        result += milis / 1000;
        return result;
    }

    public static String timeTypeString(int val) {
        if (val > 9) return val + "";
        return "0" + val;
    }





    public static int calculateProgress(String message, long totalDuration) {
        Pattern timePattern = Pattern.compile("(?<=time=)[\\d:.]*");
        Scanner sc = new Scanner(message);

        String match = sc.findWithinHorizon(timePattern, 0);
        if (match != null && (match.toCharArray().length > 0)) {
            String[] matchSplit = match.split(":");
            float progress = (Integer.parseInt(matchSplit[0]) * 3600 +
                    Integer.parseInt(matchSplit[1]) * 60 +
                    Float.parseFloat(matchSplit[2])) / totalDuration;
            float showProgress = (progress * 100);
            Log.d("TAG", "=======PROGRESS======== " + showProgress + "===>> " + match + " TD: " + totalDuration);
            //progressDialog.setMessage(""+(showProgress*100*10)+ "progress: "+ progress);
            float finalProgress = showProgress * 100 * 10;
            if (finalProgress < 1) {
                finalProgress = 1;
            } else if (finalProgress > 100) {
                finalProgress = 100;
            }
            return (int) finalProgress;
        }
        return -1;

    }



    public static long getOutputVideoBitrate(long inputBitRate,long inputPixel,long outputPixel){
        double out = ( outputPixel * 1.0) / inputPixel;
        long outputBitRate = (long) (inputBitRate * out);
        return outputBitRate;
    }

}
