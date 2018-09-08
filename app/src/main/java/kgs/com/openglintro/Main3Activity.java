package kgs.com.openglintro;

import android.Manifest;
import android.content.Intent;
import android.media.MediaCodecInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;





import kgs.com.openglintro.media.IRenderView;
import kgs.com.openglintro.media.IjkVideoView;
import kgs.com.openglintro.utils.MediaUtils;
import kgs.com.openglintro.utils.RealPathUtil;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class Main3Activity extends AppCompatActivity {

    private static int SELECT_VIDEO_REQUEST_CODE = 100;

    private String[] writePermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private String[] readPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    IjkVideoView videoView;
    TableLayout tableLayout;


    String videoFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        videoView = findViewById(R.id.videoView);
        tableLayout = findViewById(R.id.hud_view);



//        videoFilePath = Environment.getExternalStorageDirectory() + "/All_Sample_Videos/10.mp4";
        //videoFilePath = Environment.getExternalStorageDirectory() + "/All_Sample_Videos/4KVideo.mp4";
        //videoFilePath = Environment.getExternalStorageDirectory() + "/All_Sample_Videos/Adele - Hello.mp4";
        videoFilePath = Environment.getExternalStorageDirectory() + "/All_Sample_Videos/Sea_waves_video.mp4";


        Nammu.init(this);
        writePermission(true);

        //initVideoView();

        //getFileInfo();

    }

    void writePermission(final boolean status) {
        Nammu.askForPermission(this, writePermissions, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                readPermission(status);
//
            }

            @Override
            public void permissionRefused() {

            }
        });

    }


    void readPermission(final boolean status) {
        Nammu.askForPermission(this, readPermissions, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                picker();
            }

            @Override
            public void permissionRefused() {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    void initVideoView() {
//        videoView.setVideoPath(Environment.getExternalStorageDirectory() + "/All_Sample_Videos/10.mp4");
//        videoView.setHudView(tableLayout);
//        videoView.setRender(IjkVideoView.RENDER_GL_SURFACE_VIEW);
//        videoView.setRendererEffect(1);
//        videoView.getmRenderView().setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
        initVideoEffect(1);
        //videoView.setRender(IjkVideoView.RENDER_SURFACE_VIEW);
        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                videoView.start();
            }
        });

    }

    void initVideoEffect(int effect) {
        videoView.setVideoPath(videoFilePath);
        videoView.setHudView(tableLayout);
        videoView.setRender(IjkVideoView.RENDER_GL_SURFACE_VIEW);
        videoView.setRendererEffect(effect);
        videoView.getmRenderView().setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
    }


    void picker() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(Intent.createChooser(intent, "Select Video"), SELECT_VIDEO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_VIDEO_REQUEST_CODE) {
                Uri uri = data.getData();
                String videoUrl = uri.toString();
                videoFilePath = RealPathUtil.getPath(this, uri);
                initVideoView();
                Log.d("TAG", "onActivityResult: " + videoFilePath);
            }
        }
    }



}
