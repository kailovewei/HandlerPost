package ykk.handlerpost.com.handlerpost;

import android.os.Handler;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button;
    private EditText editText;
    private Handler handler;
    private String line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.button_Id);
        editText= (EditText) findViewById(R.id.editText_Id);
        button.setOnClickListener(this);
        handler=new Handler();

    }

    @Override
    public void onClick(View v) {
        WorkThread thread=new WorkThread();
        thread.start();

    }
    class WorkThread extends Thread
    {
        @Override
        public void run() {
            HttpClient httpClient=new DefaultHttpClient();
            HttpGet httpGet=new HttpGet("http://www.marschen.com/data1.html");
            try {
                HttpResponse resp=httpClient.execute(httpGet);
                if(resp.getStatusLine().getStatusCode()== HttpStatus.SC_OK)
                {
                    HttpEntity entity=resp.getEntity();
                    InputStream in=entity.getContent();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                     line=reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Runnable r=new Runnable() {

                public void run() {
                            editText.setText(line);
                    }
            };
            handler.post(r);
        }
    }
}
