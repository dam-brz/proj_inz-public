package com.projekt.zaliczeniowy.penny_pincher.ui.utils;

import android.app.Activity;
import android.content.Context;

import com.projekt.zaliczeniowy.penny_pincher.R;

public class AnimationHelper {

    public static void customType(Context context, String animtype){
        Activity act = (Activity)context;
        switch (animtype){
            case "left-to-right":
                act.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
            case "right-to-left":
                act.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                break;
            case "bottom-to-up":
                act.overridePendingTransition(R.anim.bottom_to_up, R.anim.up_to_bottom);
                break;
            case "up-to-bottom":
                act.overridePendingTransition(R.anim.up_to_bottom2, R.anim.bottom_to_up2);
                break;
            case "fadein-to-fadeout":
                act.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            default:
                break;

        }
    }
}
