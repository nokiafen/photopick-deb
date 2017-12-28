package com.single.mypicktest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.single.photopick.ImagePickActivity;
import com.single.photopick.NetUtil;
import com.single.photopick.ToastUtil;
import com.single.photopick.UploadUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public  void toPick(View v){
        startActivityForResult(new Intent(this, ImagePickActivity.class),ImagePickActivity.PICK_REQUESTCODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode==ImagePickActivity.PICK_REQUESTCODE&&resultCode==Activity.RESULT_OK){
       String path= (String) data.getExtras().get(ImagePickActivity.PATH);
            Bitmap bmp=  UploadUtil.getTargetBitmap(path,800,800);
           File file=new File(path);
           compressBmpToFile(bmp,file);
           uploadFile(file,"http://172.16.227.16:8181/wangxingtong-web/api/image/upload");
       }
        super.onActivityResult(requestCode, resultCode, data);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //ProgressUtil.dismissLoading();
                    try {
                        // 返回数据示例，根据需求和后台数据灵活处理
                        JSONObject jsonObject = new JSONObject(resultStr);
                        // 服务端以字符串“1”作为操作成功标记
                        if (jsonObject.optString("code").equals("0")) {
                            ToastUtil.showToastShort(MainActivity.this, "头像上传成功");

                        } else {
                            ToastUtil.showToastShort(MainActivity.this, jsonObject.optString("message"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
//                        ProgressUtil.dismissLoading();
                    }

                    break;

                default:
                    break;
            }
            return false;
        }
    });

    /**
     * 图片压缩
     *
     * @param bmp
     * @param file
     */
    public static void compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String resultStr = "";    // 服务端返回结果集

    /**
     * 上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param requestURL 请求的rul
     * @return 返回响应的内容
     */
    public void uploadFile(final File file, final String requestURL) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, File> fileparams = new HashMap<String, File>();
                try {
                    // 创建一个URL对象
                    URL url = new URL(requestURL);
                    // 要上传的图片文件
                    fileparams.put("file", file);
                    // 利用HttpURLConnection对象从网络中获取网页数据
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 设置连接超时（记得设置连接超时,如果网络不好,Android系统在超过默认时间会收回资源中断操作）
                    conn.setConnectTimeout(5000);
                    // 设置允许输出（发送POST请求必须设置允许输出）
                    conn.setDoOutput(true);
                    // 设置使用POST的方式发送
                    conn.setRequestMethod("POST");
                    // 设置不使用缓存（容易出现问题）
                    conn.setUseCaches(false);
                    conn.setRequestProperty("Charset", "UTF-8");//设置编码
                    // 在开始用HttpURLConnection对象的setRequestProperty()设置,就是生成HTML文件头
                    conn.setRequestProperty("ser-Agent", "Fiddler");
                    // 设置contentType
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
//                    conn.setRequestProperty("_SESSION_ID",mSessionId);
                    OutputStream os = conn.getOutputStream();
                    DataOutputStream ds = new DataOutputStream(os);
//                    NetUtil.writeStringParams(textParams, ds);
                    NetUtil.writeFileParams(fileparams, ds);
                    NetUtil.paramsEnd(ds);
                    // 对文件流操作完,要记得及时关闭
                    os.close();
                    // 服务器返回的响应吗
                    int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
                    // 对响应码进行判断
                    if (code == 200) {// 返回的响应码200,是成功
                        // 得到网络返回的输入流
                        InputStream is = conn.getInputStream();
                        resultStr = NetUtil.readString(is);
                    } else {
                        ToastUtil.showToastShort(MainActivity.this, "请求URL失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
            }
        });
        thread.start();
    }
}
