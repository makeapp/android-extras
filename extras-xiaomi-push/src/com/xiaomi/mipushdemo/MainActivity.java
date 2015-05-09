package com.xiaomi.mipushdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.xiaomi.mipush.sdk.Constants;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipushdemo.TimeIntervalDialog.TimeIntervalInterface;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 1、本demo可以直接运行，设置topic和alias。
 * 服务器端使用appsecret即可以向demo发送广播和单点的消息。
 *  2、为了修改本demo为使用你自己的appid，你需要修改几个地方：DemoApplication.java中的APP_ID,
 * APP_KEY，AndroidManifest.xml中的packagename，和权限permission.MIPUSH_RECEIVE的前缀为你的packagename。
 * 
 * @author wangkuiwei
 */
public class MainActivity extends Activity {

    public static List<String> logList = new ArrayList<String>();

    public static MainActivity sMainActivity = null;
    public TextView logView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sMainActivity = this;
        logView = (TextView)findViewById(R.id.log);
        // 设置别名
        findViewById(R.id.set_alias).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.set_alias)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String alias = editText.getText().toString();
                        MiPushClient.setAlias(MainActivity.this, alias, null);
                    }

                })
                .setNegativeButton("取消", null)
                .show();
            }
        });
        // 撤销别名
        findViewById(R.id.unset_alias).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.unset_alias)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String alias = editText.getText().toString();
                        MiPushClient.unsetAlias(MainActivity.this, alias, null);
                    }

                })
                .setNegativeButton("取消", null)
                .show();
            }
        });
        // 设置标签
        findViewById(R.id.subscribe_topic).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.subscribe_topic)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String topic = editText.getText().toString();
                        MiPushClient.subscribe(MainActivity.this, topic, null);
                    }

                })
                .setNegativeButton("取消", null)
                .show();
            }
        });
        // 撤销标签
        findViewById(R.id.unsubscribe_topic).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.unsubscribe_topic)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String topic = editText.getText().toString();
                        MiPushClient.unsubscribe(MainActivity.this, topic, null);
                    }

                })
                .setNegativeButton("取消", null)
                .show();
            }
        });
        // 设置接收消息时间
        findViewById(R.id.set_accept_time).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimeIntervalDialog(MainActivity.this, new TimeIntervalInterface() {

                    @Override
                    public void apply(int startHour, int startMin, int endHour,
                            int endMin) {
                        MiPushClient.setAcceptTime(MainActivity.this, startHour, startMin, endHour, endMin, null);
                    }

                    @Override
                    public void cancel() {
                        //ignore
                    }
                    
                })
                .show();
            }
        });
        // 暂停推送
        findViewById(R.id.pause_push).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MiPushClient.pausePush(MainActivity.this, null);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLogInfo();
    }

    public void refreshLogInfo() {
        String AllLog ="";
        for(String log : logList) {
            AllLog = AllLog + log + "\n\n";
        }
        logView.setText(AllLog);
    }
}
