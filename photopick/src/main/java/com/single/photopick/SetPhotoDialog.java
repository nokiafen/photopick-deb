package com.single.photopick;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.File;

/**
 * Created by jinjian on 2017/3/9.
 */

public class SetPhotoDialog extends Dialog implements View.OnClickListener{

    private TextView mTakePictureBtn;
    private TextView mSelectPictureBtn;
    private TextView mCancelBtn;
    private View.OnClickListener mOnClickListener;
    private  ImagePickActivity act;

    public SetPhotoDialog(Context context) {
        super(context, R.style.SetPhotoDialog);
        act= (ImagePickActivity) context;
        setCanceledOnTouchOutside(false);
    }

    public void setmOnClickListener(View.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_set_photo);
        m_InitView();
    }

    @Override
    public void onClick(View view) {

        File temp = new File(ImagePickActivity.imageFilePath);  //创建文件指定选择/拍照  的文件存储位置
        int id = view.getId();
        if (id == R.id.id_set_photo_dailog_take_picture) {
//            Uri imageFileUri = Uri.fromFile(temp);
//            Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                it.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
//             ImagePickActivity.instance.startActivityForResult(it, ImagePickActivity.SELECT_BY_CAMERA);
            act.openCamera();
        } else if (id == R.id.id_set_photo_dailog_select_picture) {
            Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            picture.putExtra(
                    MediaStore.EXTRA_OUTPUT,  //指定返回方式  非常重要 否则 图片过大内存 会有挂掉的风险
                    Uri.fromFile(temp));
            act.startActivityForResult(picture, ImagePickActivity.SELECT_BY_PICTURE_LIST);
        } else if (id == R.id.id_set_photo_dailog_cancel) {
            hide();
            act.finish();
        }
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity= Gravity.BOTTOM;

        layoutParams.width= LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height= LinearLayout.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
        getWindow().setWindowAnimations(R.style.DailDialogAnimation);

    }

    private void m_InitView(){
        mTakePictureBtn = (TextView) findViewById(R.id.id_set_photo_dailog_take_picture);
        mSelectPictureBtn = (TextView) findViewById(R.id.id_set_photo_dailog_select_picture);
        mCancelBtn = (TextView) findViewById(R.id.id_set_photo_dailog_cancel);
        if(mOnClickListener!=null){
            mTakePictureBtn.setOnClickListener(mOnClickListener);
            mSelectPictureBtn.setOnClickListener(mOnClickListener);
            mCancelBtn.setOnClickListener(mOnClickListener);
        }else{
            mTakePictureBtn.setOnClickListener(this);
            mSelectPictureBtn.setOnClickListener(this);
            mCancelBtn.setOnClickListener(this);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {

        switch(event.getKeyCode())
        {
            case KeyEvent.KEYCODE_BACK:
               this.hide();
                act.finish();
                break;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }
}
