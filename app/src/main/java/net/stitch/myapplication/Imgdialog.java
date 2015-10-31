package net.stitch.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

public class Imgdialog extends Dialog implements View.OnClickListener{
    int id;
    private DisplayMetrics mPhone;
    Activity mActivity;
    int PHOTO =1;
    int CAMERA =2;
    private ImageView mImg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(id);
        //讀取手機解析度
        mPhone = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(mPhone);
        ImageButton btn1=(ImageButton)findViewById(R.id.picImg);
        btn1.setOnClickListener(this);
        ImageButton btn2=(ImageButton)findViewById(R.id.cameraImg);
        btn2.setOnClickListener(this);
    }

    public Imgdialog(RegisterActivity rgActivity, int id, ImageView imgView) {
        super(rgActivity);
        this.id = id;
        mActivity = rgActivity;
        mImg = imgView;
    }

    @Override
    public void onClick(View v) {
        if (R.id.picImg == v.getId()) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            mActivity.startActivityForResult(intent, PHOTO);
        }
        else if(R.id.cameraImg == v.getId()){
            //開啟相機功能，並將拍照後的圖片存入SD卡相片集內，須由startActivityForResult且帶入
          //  requestCode進行呼叫，原因為拍照完畢後返回程式後則呼叫onActivityResult
            ContentValues value = new ContentValues();
            value.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri uri=  mActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());
            mActivity.startActivityForResult(intent, CAMERA);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data)
    {
        //藉由requestCode判斷是否為開啟相機或開啟相簿而呼叫的，且data不為null
        if ((requestCode == CAMERA || requestCode == PHOTO ) && data != null)
        {
                Bitmap bitmap = null;
                //讀取照片，型態為Bitmap
                if(requestCode == PHOTO){
                    //取得照片路徑uri
                    Uri uri = data.getData();
                    ContentResolver cr = mActivity.getContentResolver();
                    try {
                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else if(requestCode == CAMERA){
                    bitmap = (Bitmap) data.getExtras().get("data");
                }
                //判斷照片為橫向或者為直向，並進入ScalePic判斷圖片是否要進行縮放
               if(bitmap != null){
                    if(bitmap.getWidth() > bitmap.getHeight()){
                        ScalePic(bitmap, mPhone.heightPixels);
                    }
                    else{
                        ScalePic(bitmap, mPhone.widthPixels);
                    }
            }

        }
    }

    private void ScalePic(Bitmap bitmap, int phone)
    {
        //縮放比例預設為1
        float mScale = 1 ;

        //如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入ImageView內
        if(bitmap.getWidth() > phone )
        {
            //判斷縮放比例
            mScale = (float)phone/(float)bitmap.getWidth();

            Matrix mMat = new Matrix() ;
            mMat.setScale(mScale, mScale);

            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    mMat,
                    false);
            mImg.setImageBitmap(mScaleBitmap);
        }
        else mImg.setImageBitmap(bitmap);
    }
}


