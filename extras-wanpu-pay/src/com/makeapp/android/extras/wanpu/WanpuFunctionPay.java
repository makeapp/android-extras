package com.makeapp.android.extras.wanpu;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import com.makeapp.android.extras.FunctionAndroid;
import com.wanpu.pay.PayConnect;
import com.wanpu.pay.PayResultListener;

/**
 * Created by yuanyou on 2014/7/10.
 */
public class WanpuFunctionPay
        extends FunctionAndroid<String, Boolean> {

    @Override
    public Boolean apply(String s) {

        String params[] = s.split(" ");
        String userId = PayConnect.getInstance(FunctionAndroid.context).getDeviceId(FunctionAndroid.context);

        PayConnect.getInstance(FunctionAndroid.context).pay(FunctionAndroid.context, params[0], userId, Float.parseFloat(params[3]), params[1], params[2], "", new MyPayResultListener(activity));

        return true;
    }

    private static class MyPayResultListener implements PayResultListener {

        private Activity mActivity;

        public MyPayResultListener(Activity activity) {
            mActivity = activity;
        }

        @Override
        public void onPayFinish(Context payViewContext, String orderId, int resultCode, String resultString, int payType,
                                float amount, String goodsName) {
            // �ɸ���resultCode�����ж�
            if (resultCode == 0) {
                Toast.makeText(payViewContext.getApplicationContext(), resultString + "��" + amount + "Ԫ", Toast.LENGTH_LONG).show();
                // ֧���ɹ�ʱ�رյ�ǰ֧������
                PayConnect.getInstance(mActivity).closePayView(payViewContext);

                // TODO �ڿͻ��˴���֧���ɹ��Ĳ���

                // δָ��notifyUrl������£����׳ɹ��󣬱��뷢�ͻ�ִ
                PayConnect.getInstance(mActivity).confirm(orderId, payType);
            } else {
                Toast.makeText(payViewContext.getApplicationContext(), resultString, Toast.LENGTH_LONG).show();
            }
        }
    }

}
