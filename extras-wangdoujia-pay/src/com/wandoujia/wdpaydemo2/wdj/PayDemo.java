package com.wandoujia.wdpaydemo2.wdj;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.wandoujia.sdk.plugin.paydef.LoginCallBack;
import com.wandoujia.sdk.plugin.paydef.PayCallBack;
import com.wandoujia.sdk.plugin.paydef.User;
import com.wandoujia.sdk.plugin.paydef.WandouAccount;
import com.wandoujia.sdk.plugin.paydef.WandouOrder;
import com.wandoujia.sdk.plugin.paydef.WandouPay;
import com.wandoujia.sdk.plugin.paysdkimpl.PayConfig;
import com.wandoujia.sdk.plugin.paysdkimpl.WandouAccountImpl;
import com.wandoujia.sdk.plugin.paysdkimpl.WandouPayImpl;
import com.wandoujia.wandoujiapaymentplugin.utils.MSG;

public class PayDemo extends Activity implements OnClickListener {
    private static final String TAG = "PayDemo";

    final String appkey_id = "100000000";//"100000000";
    // 开发者 安全秘钥
    final String secretkey = "99b4efb45d49338573a00be7a1431511";//"99b4efb45d49338573a00be7a1431511";


    private WandouAccount account = new WandouAccountImpl();
    private WandouPay wandoupay = new WandouPayImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "start onCreate~~~" + android.os.Build.VERSION.RELEASE);
        super.onCreate(savedInstanceState);
        //在onCreate中调用PayConfig.init初始化
        PayConfig.init(this, appkey_id, secretkey);
        gameLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "start onDestroy~~~" + android.os.Build.VERSION.RELEASE);
    }

    public void gameLayout() {
        setContentView(R.layout.activity_main);

        findViewById(R.id.pay).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.logout_button).setOnClickListener(this);
        findViewById(R.id.reLogin_button).setOnClickListener(this);
    }

    public String textString(int id) {
        return ((TextView) findViewById(id)).getText().toString();
    }

    public void setText(final int id, final String str) {
        // Request UI update on main thread.
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                ((TextView) findViewById(id)).setText(str);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (R.id.pay == viewId) {
            Log.w(TAG, "doPayment!");
            float money = Float.parseFloat(textString(R.id.money));
            long moneyInFen = (long) (money * 100);

            WandouOrder order =
                    new WandouOrder(textString(R.id.subject), textString(R.id.desc), moneyInFen);
            // 设置游戏订单号，最长50个字符
            order.setOut_trade_no("GameOrderIdMaxLenth50");
            // 触发支付
            wandoupay.pay(PayDemo.this, order, new PayCallBack() {

                @Override
                public void onSuccess(User user, WandouOrder order) {
                    Log.w("DemoPay", "onSuccess:" + order);
                    setText(R.id.orderInfo, user.getNick() + " 支付成功！" + order);
                }

                @Override
                public void onError(User user, WandouOrder order) {
                    Log.w("DemoPay", "onError:" + order);
                    setText(R.id.orderInfo, user.getNick() + " 支付失败！" + order);
                }
            });
        } else if (R.id.login_button == viewId) {
            // Log.i(TAG, "PaySdk init.");
            // PayConfig.init(this, appkey_id, secretkey);
            Log.w(TAG, "doLogin!");
            //触发登录
            account.doLogin(PayDemo.this, new LoginCallBack() {

                @Override
                public void onSuccess(User user, int type) {

                    Log.w("login", "success:+" + user);
                    setText(R.id.account, user.toString());
                    // 豌豆荚账户UID
                    Long uid = user.getUid();
                    // 豌豆荚账户昵称
                    String nick = user.getNick();
                    // 豌豆荚账户登录验证 Token ，15分钟内有效
                    String token = user.getToken();
                    // 1.请把uid,token 提交游戏服务器
                    // 2.游戏服务器收到uid,token后提交给豌豆荚服务器验证
                    // 3.验证通过后，游戏服务器生成一个 cookie 给游戏客户端使用
                    // 4.游戏客户端得到游戏的cookie 与游戏服务器进行交互通信，保证身份验证安全
                }

                @Override
                public void onError(int returnCode, String info) {
                    // 请不要在这里重新调用 doLogin
                    // 游戏界面上应该留有一个登录按钮，来触发 doLogin登录
                    setText(R.id.account, "Demo中登陆失败:" + MSG.trans(info));
                    Log.e(TAG, MSG.trans(info));
                }
            });
        } else if (R.id.logout_button == viewId) {
            Log.w(TAG, "doLogout!");
            // doLogout无需处理回掉方法
            account.doLogout(getApplicationContext(), new LoginCallBack() {

              @Override
              public void onSuccess(User user, int type) {

                Log.d("logout", "success:+" + user);
                setText(R.id.account, "成功登出: " + user.toString());
              }

              @Override
              public void onError(int returnCode, String info) {
                // 请不要在这里重新调用 doLogin
                // 游戏界面上应该留有一个登录按钮，来触发 doLogin登录
                setText(R.id.account, "Demo中登出:" + MSG.trans(info));
                Log.e(TAG, MSG.trans(info));
              }
            });
            // Do the subsequent directly after doLogut was called.
            // i.e. Finish your application.
            // Then when launching your app next time, user will see the login UI again.
        } else if (R.id.reLogin_button == viewId) {
          account.reLogin(PayDemo.this, new LoginCallBack() {

            @Override
            public void onSuccess(User user, int type) {

              Log.d("re-login", "success:+" + user);
              setText(R.id.account, user.toString());
              // 豌豆荚账户UID
              Long uid = user.getUid();
              // 豌豆荚账户昵称
              String nick = user.getNick();
              // 豌豆荚账户登录验证 Token ，15分钟内有效
              String token = user.getToken();
              // 1.请把uid,token 提交游戏服务器
              // 2.游戏服务器收到uid,token后提交给豌豆荚服务器验证
              // 3.验证通过后，游戏服务器生成一个 cookie 给游戏客户端使用
              // 4.游戏客户端得到游戏的cookie 与游戏服务器进行交互通信，保证身份验证安全
            }

            @Override
            public void onError(int returnCode, String info) {
              // 请不要在这里重新调用 doLogin
              // 游戏界面上应该留有一个登录按钮，来触发 doLogin登录
              setText(R.id.account, "Demo中登陆失败:" + MSG.trans(info));
              Log.e(TAG, MSG.trans(info));
            }
          });
        }

    }
}
