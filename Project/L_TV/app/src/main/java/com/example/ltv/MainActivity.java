package com.example.ltv;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    private SendThread sendthread;
    private  int  light_contral = 0;    //设置灯初始状态为关

    private String ip="183.230.40.33";  //MQTT服务器ip
    private int port=80;        //端口号
    private String id="592216019";  //设备ID
    private String apikey="X2tI6SGkCadfpztWEBGKZZSs7rc=";   //设备APIkey

    Handler mHandler = new Handler() {};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("L&TV 控制");

        /***************连接*****************/
        sendthread = new SendThread(ip, port, mHandler);
        new Thread(sendthread).start();     //创建一个新线程
        new Thread().start();
        /**********************************/

        //实例化按钮
        final Button light = (Button) findViewById(R.id.light);
        final Button up = (Button) findViewById(R.id.up);
        final Button down = (Button) findViewById(R.id.down);
        final Button left = (Button) findViewById(R.id.left);
        final Button right = (Button) findViewById(R.id.right);
        final Button ok = (Button) findViewById(R.id.ok);
        final Button kg = (Button) findViewById(R.id.kg);

        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (light_contral == 0) //开灯
                {
                    sendthread.send("POST http://api.heclouds.com/cmds?device_id=" + id + " HTTP/1.1\n" +
                            "api-key:" + apikey + "\n" +
                            "Host:api.heclouds.com\n" +
                            "Content-Length:1\n" +
                            "\n" +
                            "0");
                    light_contral = 1; //状态改为开
                }
                else  //关灯
                {
                    sendthread.send("POST http://api.heclouds.com/cmds?device_id=" + id + " HTTP/1.1\n" +
                            "api-key:" + apikey + "\n" +
                            "Host:api.heclouds.com\n" +
                            "Content-Length:1\n" +
                            "\n" +
                            "7");
                    light_contral = 0;//状态改为开
                }
            }
        });
        up.setOnClickListener(new View.OnClickListener() //上
        {
            @Override
            public void onClick(View v) {
                sendthread.send("POST http://api.heclouds.com/cmds?device_id="+id+" HTTP/1.1\n" +
                        "api-key:"+apikey+"\n" +
                        "Host:api.heclouds.com\n" +
                        "Content-Length:1\n" +
                        "\n" +
                        "2");
            }
        });
        down.setOnClickListener(new View.OnClickListener() //下
        {
            @Override
            public void onClick(View v) {
                sendthread.send("POST http://api.heclouds.com/cmds?device_id="+id+" HTTP/1.1\n" +
                        "api-key:"+apikey+"\n" +
                        "Host:api.heclouds.com\n" +
                        "Content-Length:1\n" +
                        "\n" +
                        "3");
            }
        });
        left.setOnClickListener(new View.OnClickListener() //左
        {
            @Override
            public void onClick(View v) {
                sendthread.send("POST http://api.heclouds.com/cmds?device_id="+id+" HTTP/1.1\n" +
                        "api-key:"+apikey+"\n" +
                        "Host:api.heclouds.com\n" +
                        "Content-Length:1\n" +
                        "\n" +
                        "4");
            }
        });
        right.setOnClickListener(new View.OnClickListener() //右
        {
            @Override
            public void onClick(View v) {
                sendthread.send("POST http://api.heclouds.com/cmds?device_id="+id+" HTTP/1.1\n" +
                        "api-key:"+apikey+"\n" +
                        "Host:api.heclouds.com\n" +
                        "Content-Length:1\n" +
                        "\n" +
                        "5");
            }
        });
        kg.setOnClickListener(new View.OnClickListener() //开关
        {
            @Override
            public void onClick(View v) {
                sendthread.send("POST http://api.heclouds.com/cmds?device_id="+id+" HTTP/1.1\n" +
                        "api-key:"+apikey+"\n" +
                        "Host:api.heclouds.com\n" +
                        "Content-Length:1\n" +
                        "\n" +
                        "1");
            }
        });
        ok.setOnClickListener(new View.OnClickListener() //开关
        {
            @Override
            public void onClick(View v) {
                sendthread.send("POST http://api.heclouds.com/cmds?device_id="+id+" HTTP/1.1\n" +
                        "api-key:"+apikey+"\n" +
                        "Host:api.heclouds.com\n" +
                        "Content-Length:1\n" +
                        "\n" +
                        "o");
            }
        });
    }

    //在这个活动点击返回的时候让他返回桌面
    public boolean onKeyDown (int keyCode , KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}