package net.stitch.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.SSLContext;

public class MainActivity extends AppCompatActivity {
   TextView mShowMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
  //      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
 //       setSupportActionBar(toolbar);

//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        mShowMsg = (TextView)findViewById(R.id.serverText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void rg_onClick(View v) {
        Intent rg_intent = new Intent();
        rg_intent.setClass(MainActivity.this, RegisterActivity.class);
        startActivity(rg_intent);    //觸發換頁
        finish();   //結束本頁
    }

    public void login_onClick(View v){
        Intent main_page_intent = new Intent();
        main_page_intent.setClass(MainActivity.this, GoogleMapActivity.class);
        startActivity(main_page_intent);    //觸發換頁
        finish();   //結束本頁

       // new FetchSecuredResourceTask().execute();

    }

    private class FetchSecuredResourceTask extends AsyncTask<Void, Void, Message> { //Params, Progress, Result
        protected String TAG = MainActivity.class.getSimpleName();

        private String account;

        private String password;

        @Override
        protected void onPreExecute() {
            // build the message object
            EditText editText = (EditText) findViewById(R.id.AccountText);
            this.account = editText.getText().toString();

            editText = (EditText) findViewById(R.id.pwdText);
            this.password = editText.getText().toString();
        }

        @Override
        protected Message doInBackground(Void... params) {
            // Populate the HTTP Basic Authentitcation header with the username and password
            String url = getString(R.string.base_uri)+"/user/register";
            HttpAuthentication authHeader = new HttpBasicAuthentication(account, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate(true);
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
            MultiValueMap<String, Object> formData;
            formData = new LinkedMultiValueMap<String, Object>();
            formData.add("email", "123@123.com");
            formData.add("password", "1234567");
            formData.add("nickName", "Tina");
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(
                    formData, requestHeaders);
            try {
                ResponseEntity<Message> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Message.class);
                return response.getBody();
            } catch (HttpClientErrorException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new Message(0, e.getStatusText(), e.getLocalizedMessage());
            } catch (ResourceAccessException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
                return new Message(0, e.getClass().getSimpleName(), e.getLocalizedMessage());
            }
        }

        @Override
        protected void onPostExecute(Message result) {
            System.out.println("result\t"+result.getText());
            //mShowMsg.setText(result);
        }

    }

}
