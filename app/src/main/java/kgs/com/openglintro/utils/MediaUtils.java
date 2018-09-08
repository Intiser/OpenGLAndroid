package kgs.com.openglintro.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by developerios on 4/25/18.
 */

public class MediaUtils {
    public static long getDurationOfMediaFile(String path, Context context) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(context, Uri.parse(path));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time);

        retriever.release();
        return timeInMillisec;
    }

    public static int GetDuration(String path, Context context) {
//        Uri uri = Uri.parse(path);
//        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//        mmr.setDataSource(AMTVApplication.getContext(),uri);
        //try {
        String durationStr = getMetaDataOfAMediaFile(path, MediaMetadataRetriever.METADATA_KEY_DURATION, context);//mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int millSecond = Integer.parseInt(durationStr);
        return millSecond;
//        }catch (Exception e){
//            Log.d(MediaUtils.class.getName(), ""+e.getMessage());
//            e.printStackTrace();
//            return 0;
//        }
    }

    public static String GetTitile(String path, Context context) {
        return getMetaDataOfAMediaFile(path, MediaMetadataRetriever.METADATA_KEY_TITLE, context);
    }

    private static String getMetaDataOfAMediaFile(String path, int keyCode, Context context) {
        Uri uri = Uri.parse(path);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(context, Uri.fromFile(new File(path)));
            //mmr.setDataSource(path, new HashMap<String, String>());
            String metadata = mmr.extractMetadata(keyCode);
            metadata = metadata.isEmpty() ? "Unknown" : metadata;
            return metadata;
        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }

        return "Unknown";
    }

    public static boolean isVideoFile(String url, Context context) {
        File fileToTest = new File(url);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, Uri.fromFile(fileToTest));

        String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
        boolean isVideo = "yes".equals(hasVideo);
        return isVideo;
    }

    public static String getVideoHeight(String url) {
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(url);
        String metaRotation = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        if(metaRotation.equals("90") || metaRotation.equals("270")){
            return metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        }
        String height = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        return height;
    }

    public static String getVideoWidth(String url) {
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(url);
        String metaRotation = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        if(metaRotation.equals("90") || metaRotation.equals("270")){
            return metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        }
        String width = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        return width;
    }

    public static Pair getVideoRatio(String url, IMediaPlayer mediaPlayer){
        int width = mediaPlayer.getVideoWidth();
        int height = mediaPlayer.getVideoHeight();

        if(width == 0){
            width = Integer.parseInt(MediaUtils.getVideoWidth(url));
        }
        int gcd = gcd(width,height);

        float widthR = width/gcd;
        float heightR = height/gcd;

        return Pair.create(widthR,heightR);
    }

    static int gcd(int a, int b) {
        // Everything divides 0
        if (a == 0 || b == 0)
            return 0;

        // base case
        if (a == b)
            return a;

        // a is greater
        if (a > b)
            return gcd(a - b, b);
        return gcd(a, b - a);
    }


    public static boolean isMediaFileCorrupted(MediaPlayer mediaPlayer, String path) {
        try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static boolean isMediaVideo(String path) {

        if (!canRetrieveMetadataFromPath(path)) {
            return false;
        }

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        String hasVideo = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
        if (TextUtils.isEmpty(hasVideo)) {
            return false;
        }

        return "yes".equals(hasVideo);
    }

    public static boolean isDurationValid(String path) {

        if (!canRetrieveMetadataFromPath(path)) {
            return false;
        }

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        if (TextUtils.isEmpty(duration)) {
            return false;
        }

        long durationInMillis = Long.parseLong(duration);
        if (durationInMillis == 0) {
            return false;
        }

        return true;
    }

    public static boolean isVideoHaveAudioTrack(String path) {

        if (!canRetrieveMetadataFromPath(path)) {
            return false;
        }

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String hasAudioStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO);
        if (TextUtils.isEmpty(hasAudioStr)) {
            return false;
        }

        return hasAudioStr.equals("yes");
    }

    @Nullable
    public static String getVideoBitrate(String path) {
        if (!canRetrieveMetadataFromPath(path)) {
            return null;
        }

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        String bitrate = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);

        return bitrate;
    }

    @Nullable
    public static String getVideoFrameRate(String path) {
        if (!canRetrieveMetadataFromPath(path)) {
            return null;
        }

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        String frameRate = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE);

        return frameRate;
    }

    public static boolean isMediaFileSizeValid(String videoPath) {
        File file = new File(videoPath);
        if (file.length() > 0) {
            return true;
        }
        return false;
    }

    public static boolean canRetrieveMetadataFromPath(String path) {
        boolean canRetrieve = false;

        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
            canRetrieve = true;

        } catch (Exception e) {
            Log.d("TAG", "Failed to retrieve media metadata");
        }

        return canRetrieve;
    }



}
