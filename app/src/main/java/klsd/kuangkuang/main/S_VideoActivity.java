package klsd.kuangkuang.main;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import klsd.kuangkuang.R;

/**
 * 播放视频
 */
public class S_VideoActivity extends BaseActivity implements View.OnClickListener {
    private VideoView videoView;
    private Button play;
    private Button pause;
    private Button replay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_video);

        play = (Button) findViewById(R.id.play);
        pause = (Button) findViewById(R.id.pause);
        replay = (Button) findViewById(R.id.replay);
        videoView = (VideoView) findViewById(R.id.videoview);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        replay.setOnClickListener(this);
        initVideoPath();
    }

    private void initVideoPath() {
//        File file = new File(Environment.getExternalStorageDirectory(), "movie.3gp");
//        videoView.setVideoPath(file.getPath()); // 指定视频文件的路径
        Uri uri = Uri.parse("http://119.254.211.82:8080/uploads/%E5%B9%BF%E7%94%B5%E6%80%BB%E5%B1%80.MP4");
        videoView.setVideoURI(uri);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:

                if (!videoView.isPlaying()) {
                    videoView.start(); // 开始播放
                }
                break;
            case R.id.pause:
                if (videoView.isPlaying()) {
                    videoView.pause(); // 暂时播放
                }
                break;
            case R.id.replay:
                if (videoView.isPlaying()) {
                    videoView.resume(); // 重新播放 }
                    break;
                }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.suspend();
        }
    }
}