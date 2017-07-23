package com.phannguyen.statusshare.utils;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

import com.phannguyen.statusshare.R;
import com.phannguyen.statusshare.firebase.FirebaseHelper;
import com.phannguyen.statusshare.global.AppPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by phannguyen on 4/13/17.
 */

public class FnUtils {
    public static final int LAST_POST_SERVER_STAMP_ID = 1;
    public static final int LAST_DELETE_SERVER_STAMP_ID = 2;
    public static final String IMAGE_URL_SPLIT_CHAR = "_";
    public static final String FORMAT_DATE = "EEEE, MMMM dd, yyyy";

    public static String getFormatCurrentDate(){
        return new SimpleDateFormat(FORMAT_DATE, Locale.ROOT).format(new Date());
    }
    public static double getImageRatioFromUrl(String url){
        double ratio = generateRatio();//for testing with url have no ratio, will remove later
        if(url != null && url.length() > 0) {
            int idx = url.lastIndexOf(".");
            String[] urlParts = url.substring(0,idx).split(IMAGE_URL_SPLIT_CHAR);
            if(urlParts.length>=2){
                try {
                    ratio = Double.parseDouble(urlParts[1]);
                }catch (NumberFormatException ex){
                    ex.printStackTrace();
                }

            }
        }
        return ratio;
    }

    private static double generateRatio(){
        double[] arrRatio = {1.0,1.2,0.4,0.8,1.5,1,0.9,0.5,1.6,1.3,1.2,0.7,0.8};
        double mRatio = 1.0;
        // Create psuedo
        Random random = new Random();
        int rand = random.nextInt(arrRatio.length);
        mRatio = arrRatio[rand];
        return mRatio;

    }

    public static String getDisplayTime(long timeInMili){
        Calendar curCalendar = Calendar.getInstance();
        long curMils = curCalendar.getTimeInMillis();

        long distanceInSecond = Math.abs(curMils - timeInMili) / 1000;

        if(distanceInSecond < 5){
            return "Just now";
        }
        if(distanceInSecond < 60){
            return "A few seconds ago";
        }
        long distanceInMinute = distanceInSecond / 60;
        if(distanceInMinute < 60) {
            String minuteFormat = "%d mins ago";
            if (distanceInMinute == 1) {
                minuteFormat = "%d min ago";
            }
            return String.format(Locale.ROOT,minuteFormat, distanceInMinute);
        }
        long distanceInHour = distanceInMinute / 60;
        if(distanceInHour < 24){
            String hourFormat = "%d hours ago";
            if(distanceInHour == 1){
                hourFormat = "%d hour ago";
            }
            return String.format(Locale.ROOT,hourFormat, distanceInHour);
        }
        long distanceInDay = distanceInHour / 24;
        if(distanceInDay < 4){
            if(distanceInDay == 1){
                return "Yesterday";
            }
            String dayFormat = "%d days ago";
            return String.format(Locale.ROOT,dayFormat, distanceInDay);
        }
        return new SimpleDateFormat(FORMAT_DATE, Locale.ROOT).format(new Date(timeInMili));

    }

    public static void showConfirmDialog(Context context, String header,
                                         String message, String positiveLabel,
                                         DialogInterface.OnClickListener positiveAction, String negativeLabel, DialogInterface.OnClickListener negativeAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
        if (header != null && header.length() != 0)
            builder.setTitle(header);


        Dialog dialog = builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(positiveLabel, positiveAction)
                .setNegativeButton(negativeLabel, negativeAction)
                .show();

    }


    public  static ObjectAnimator popout(final View view, final long duration, final AnimatorListenerAdapter animatorListenerAdapter) {
        ObjectAnimator popout = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("alpha", 1f, 0f),
                PropertyValuesHolder.ofFloat("scaleX", 1f, 0f),
                PropertyValuesHolder.ofFloat("scaleY", 1f, 0f));
        popout.setDuration(duration);
        popout.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
                if (animatorListenerAdapter != null) {
                    animatorListenerAdapter.onAnimationEnd(animation);
                }
            }
        });
        popout.setInterpolator(new AnticipateOvershootInterpolator());

        return popout;
    }

    /**
     * Check if email and device info are logged in email on current device
     * This will allow 1 user login multi devices
     * @param email
     * @param deviceId
     * @return
     */
    public static boolean isFromUserOnThisDevice(String email,String deviceId){
        return FirebaseHelper.Instance().isCurrentUser(email) &&
                AppPresenter.Instance().deviceId().equals(deviceId);
    }
    private static ColorDrawable[] vibrantLightColorList =
            {
                    new ColorDrawable(Color.parseColor("#9ACCCD")), new ColorDrawable(Color.parseColor("#8FD8A0")),
                    new ColorDrawable(Color.parseColor("#CBD890")), new ColorDrawable(Color.parseColor("#DACC8F")),
                    new ColorDrawable(Color.parseColor("#D9A790")), new ColorDrawable(Color.parseColor("#D18FD9")),
                    new ColorDrawable(Color.parseColor("#FF6772")), new ColorDrawable(Color.parseColor("#DDFB5C"))
            };

    public static ColorDrawable getRandomDrawbleColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}
