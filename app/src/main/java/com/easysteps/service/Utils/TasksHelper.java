package com.easysteps.service.Utils;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

public class TasksHelper {
    public static void scheduleTimeChange(final TextView textView, final Context context) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                UIHelper.setTimeTillDayEnd(textView, context);
                TasksHelper.scheduleTimeChange(textView, context);
            }
        }, 1000);
    }
}
