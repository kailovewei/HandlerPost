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
/*
private static Message getPostMessage(Runnable r)
1.该方法完成了两个操作，第一生成了一个Message对象。第二,将r对象赋值给Message对象的callback属性
 第一个问题：如何把一个Runnable对象放置在一个消息队列当中；实际是生成一个Message对象，
 并将r赋值给Message的callback属性，然后将Message放置在消息队列当中。
 第二个问题：Looper取出了携带r对象的Message对象之后，调用了dispatchMessage()方法，然后判断Message的callback属性是否为空，
 此时callback属性有值，所以执行了handleCallback(Message msg),在方法中执行了msg.callback.run();
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button;
    private EditText editText;
    private Handler handler;
    private static String line;
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
            //从网络中取回数据
            HttpClient httpClient=new DefaultHttpClient();
            HttpGet httpGet=new HttpGet("http://www.marschen.com/data1.html");
            try {
                HttpResponse resp=httpClient.execute(httpGet);
                //判断是否等于200，则服务器响应正常。
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
                    //在这里更新UI代码
                            editText.setText(line);
                    }
            };
            //post(r)方法将应用r对象放置在消息队列当中，Looper对象（主线程）从消息队列当中取出了r对象。
            handler.post(r);
        }
    }
}
