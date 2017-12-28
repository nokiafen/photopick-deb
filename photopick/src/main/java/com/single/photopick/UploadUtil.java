package com.single.photopick;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by chenhailin on 2017/7/17.
 */

public class UploadUtil {

    public  static Bitmap getTargetBitmap(String path,int with,int height){
        BitmapFactory.Options opts = new BitmapFactory.Options();
                BitmapFactory
                .decodeFile(path);

        opts.inSampleSize = getTargetScaleSize(opts, with, height);
        opts.inJustDecodeBounds = false;
        Bitmap     iBitmap1 = BitmapFactory
                .decodeFile(
                        path);
        return  iBitmap1;
    }


    private static int getTargetScaleSize(BitmapFactory.Options options, int reqWidth, int reqHeight ){
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            // 先根据宽度进行缩小
            while (width / inSampleSize > reqWidth) {
                inSampleSize++;
            }
            // 然后根据高度进行缩小
            while (height / inSampleSize > reqHeight) {
                inSampleSize++;
            }
            return inSampleSize;
        }


    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


}
