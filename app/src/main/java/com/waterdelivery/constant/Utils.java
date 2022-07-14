package com.waterdelivery.constant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;


import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Utils {


    public static Bitmap getBitmapFromImagePath(String path) {
        Bitmap bitmap = null;
        File imgFile = new File(path);
        if (imgFile.exists()) {
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

        }
        return bitmap;
    }
    /**
     * will toast message
     *
     * @param context
     * @param msg
     */
    public static void showLongToastMessage(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * show message In Toast.
     *
     * @param context
     * @param string
     */
    public static void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }






    public static int checkDates(String date1,String date2){
        Date date1output=null;
        Date date2output=null;
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
        try {
             date1output = format.parse(date1);
             date2output = format.parse(date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date1output!=null)
        return compareDates(date1output,date2output);
        else
            return  -2;
    }

    public  static int compareDates(Date date1, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

        try {
            date1 = sdf.parse(sdf.format(date1));
            date2 = sdf.parse(sdf.format(date2));
        } catch (ParseException e) {
            // e.printStackTrace();
            return -2;
        }

        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();

        cal1.setTime(date1);
        cal2.setTime(date2);

        if (cal1.equals(cal2)) {
            return 0;
        } else if (cal1.after(cal2)) {
            return 1;
        } else if (cal1.before(cal2)) {
            return -1;
        }

        return -2;
    }

    public static String dateModify(String jobModifiedDate) {
        String dater = "";
        try {
            DateFormat formatter;
            Date date;
            formatter = new SimpleDateFormat("dd-MM-yyyy");
            date = (Date) formatter.parse(jobModifiedDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM,yyyy");
            dater = simpleDateFormat.format(date);

        } catch (Exception e) {
            //  e.printStackTrace();
        }

        return dater;
    }
    public static String getCompleteAddressString(double LATITUDE, double LONGITUDE, Context context) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
    }


}
