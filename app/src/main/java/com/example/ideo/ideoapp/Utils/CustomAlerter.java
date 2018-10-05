package com.example.ideo.ideoapp.Utils;

import android.app.Activity;
import android.view.View;

import com.example.ideo.ideoapp.R;
import com.tapadoo.alerter.Alerter;

public class CustomAlerter {


    public static Alerter getAlerter(final Activity activity, final String title, final String content, boolean swipeToDismiss, final Runnable onClick) {
        Alerter alerter = Alerter.create(activity);
        alerter
                .setBackgroundColorRes(R.color.colorDarkGrey)
                .setProgressColorRes(R.color.colorBackgroundDarker)
                .setIcon(R.mipmap.ic_launcher)
                .setIconColorFilter(0)
                .enableVibration(true)
                .setDuration(30000)
                .setText(content)
                .setTitle(title)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onClick != null)
                            onClick.run();
                    }
                });
        if (swipeToDismiss) alerter.enableSwipeToDismiss();
        return alerter;
    }
}