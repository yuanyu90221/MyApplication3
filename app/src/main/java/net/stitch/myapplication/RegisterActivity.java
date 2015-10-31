package net.stitch.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

public class RegisterActivity extends AppCompatActivity {
    Imgdialog mdialog;
    ImageView mImageView;
    EditText mEmailText, mPwdText, mNameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mImageView = (ImageView)findViewById(R.id.imageView2);
        mEmailText = (EditText)findViewById(R.id.emailText);
        mPwdText = (EditText)findViewById(R.id.pwdText);
        mNameText = (EditText)findViewById(R.id.nameText);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void img_onClick(View v) {
        //開啟選擇相片或照相的dialog
        mdialog = new Imgdialog(RegisterActivity.this, R.layout.content_imgdialog, mImageView);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.show();
    }

    public void register_onclick(View v){
        new RegisterTask().execute();
    }

    public  void clear_onClick(View v){
        mEmailText.setText("");
        mPwdText.setText("");
        mNameText.setText("");
        mImageView.setImageResource(R.drawable.ic_babys_room_50);
        mEmailText.requestFocus();
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        mdialog.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class RegisterTask extends AsyncTask<Void, Void, String> { //Params, Progress, Result
        protected String TAG = RegisterActivity.class.getSimpleName();
        private String account, password, nickName;
        private Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            // build the message object
            this.account = mEmailText.getText().toString();
            this.password = mPwdText.getText().toString();
            this.nickName = mNameText.getText().toString();
            this.bitmap = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();
        }

        @Override
        protected String doInBackground(Void... params) {
            // Populate the HTTP Basic Authentitcation header with the username and password
           String baseUrl = getString(R.string.base_uri);
//            String url = baseUrl + "/user/register";
//            HttpHeaders requestHeaders = new HttpHeaders();
//            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//            // Create a new RestTemplate instance
//            RestTemplate restTemplate = new RestTemplate(true);
//            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
//            MultiValueMap<String, Object> formData;
//            formData = new LinkedMultiValueMap<String, Object>();
//            formData.add("email", account);
//            formData.add("password", password);
//            formData.add("name", nickName);
//            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(formData, requestHeaders);
            try {
                //ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
               // System.out.println("user register response\t" + response.getBody());

                HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
                CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
                HttpPostHC4 httpPost = new HttpPostHC4(baseUrl+"/file/upload");
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                builder.addPart("name", new StringBody("test10291330", ContentType.TEXT_PLAIN));

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                //byte[] imageInByte = stream.toByteArray();
               // ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
                InputStream in = new ByteArrayInputStream(stream.toByteArray());
                ContentBody mimePart = new InputStreamBody(in, "test.jpg");

                //FileBody fileBody = new FileBody(new File("/storage/emulated/0/DCIM/Camera/P_123.jpg")); //image should be a String
                builder.addPart("file", mimePart);

                httpPost.setEntity(builder.build());
                CloseableHttpResponse httpResponse = null;
                try {
                    httpResponse = closeableHttpClient.execute(httpPost);
                    in = new BufferedInputStream(httpResponse.getEntity().getContent());
                    String response = IOUtils.toString(in, "UTF-8");
                    System.out.println(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return "";
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return "";
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
           // System.out.println("result\t"+result);
            //mShowMsg.setText(result);
        }

    }
}
