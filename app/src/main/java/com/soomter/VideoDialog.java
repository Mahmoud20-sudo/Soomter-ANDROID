package com.soomter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class VideoDialog extends DialogFragment {

    JzvdStd vid;

    public static VideoDialog newInstance(String path) {
        VideoDialog frag = new VideoDialog();
        Bundle args = new Bundle();
        args.putString("path", path);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.video_view, container, false);
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        vid = (JzvdStd)view.findViewById(R.id.videoView);

        vid.fullscreenButton.setVisibility(View.INVISIBLE);
        playVideo(getArguments().getString("path"));

        return view;
    }

    public void playVideo(String path) { //Your code here }

       // Uri video  = Uri.parse("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4");
        vid.setUp(path,
               "",
                Jzvd.SCREEN_WINDOW_NORMAL);
    }
}