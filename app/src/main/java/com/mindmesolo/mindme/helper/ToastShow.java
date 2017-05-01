package com.mindmesolo.mindme.helper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mindmesolo.mindme.R;

/**
 * Created by enest_09 on 10/21/2016.
 */

public class ToastShow extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public ToastShow(Context context) {
        super(context);
    }

    public static Toast setText(Context context, CharSequence text, @Snackbar.Duration int duration) {
        Toast result = new Toast(context);
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.my_custom_toast, null);
        TextView tv = (TextView) v.findViewById(R.id.text);
        tv.setText(text);
        //    ImageView image = (ImageView) v.findViewById(R.id.image);
//      image.setImageResource(R.drawable.bellgrey);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 70);
        //toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(v);
        toast.show();
        return result;
    }
}
