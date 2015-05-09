package com.makeapp.android.extras.tencent;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;
import com.makeapp.android.extras.FunctionAndroid;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.*;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import junit.framework.Assert;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.*;

/**
 * Created by yuanyou on 2014/8/5.
 */
public class TencentFunctionWeiXin
        extends FunctionAndroid<String, String> implements IWXAPIEventHandler {

    private static final int THUMB_SIZE = 150;
    private static final String TAG = "TencentFunctionWeiXin";
    private static final char SPLIT = '&';
    private static String API_KEY = "8934e7d15453e97507ef794cf7b0519d";

    private IWXAPI api;
    String appId;
    String appSecret;
    String mchId;
    String notifyUrl;

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        appId = getConfig("app_id");
        appSecret = getConfig("app_secret");
        mchId = getConfig("mch_id");
        API_KEY = getConfig("api_key");
        notifyUrl = getConfig("notify_url");
        api = WXAPIFactory.createWXAPI(context, appId, false);
        api.registerApp(appId);
    }

    public String getAppId() {
        return appId = getConfig("app_id");
    }

    @Override
    public void onReq(BaseReq baseReq) {
        System.out.println("" + baseReq);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        System.out.println("" + baseResp);
        if (baseResp.errCode == 0) {
            onResult("pay", "success");
        } else {
            onResult("pay", "fault");
        }
    }

    @Override
    public String apply(String s) {
        String[] params = s.split(" ");
        String command = params[0];
        if ("open".equalsIgnoreCase(params[0])) {
            api.openWXApp();
        } else if ("config".equalsIgnoreCase(command)) {
            if ("notify_url".equalsIgnoreCase(params[1])) {
                notifyUrl = params[2];
            }
        } else if ("login".equalsIgnoreCase(command)) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            api.sendReq(req);
        } else if ("pay".equalsIgnoreCase(command)) {
            boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
            if (isPaySupported) {
                String payCode = params[1];
                String orderId = params[2];
                new GetPrepayIdTask(payCode, orderId).execute();
            } else {
                Toast.makeText(context, "不支持微信支付，请安装或者更新微信客户端", Toast.LENGTH_LONG).show();
            }
        } else if ("register".equalsIgnoreCase(params[0])) {
            api.registerApp(appId);
        } else if ("version".equalsIgnoreCase(params[0])) {
            int wxSdkVersion = api.getWXAppSupportAPI();
            return String.valueOf(wxSdkVersion);
        } else if ("sendSession".equalsIgnoreCase(command)
                || "sendTimeline".equalsIgnoreCase(command)
                || "sendFavorite".equalsIgnoreCase(command)) {
            String mediaType = params[1];
            WXMediaMessage msg = new WXMediaMessage();
            msg.title = params[2];
            msg.description = params[3];
            msg.thumbData = getAppIcon();
            String attach1 = params.length >= 5 ? params[4] : "";

            if ("text".equalsIgnoreCase(mediaType)) {
                String text = msg.description;
                WXTextObject textObj = new WXTextObject();
                textObj.text = text;
                msg.mediaObject = textObj;
            } else if ("imageRes".equalsIgnoreCase(mediaType)) {
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), Integer.parseInt(attach1));
                WXImageObject imgObj = new WXImageObject(bmp);
                msg.mediaObject = imgObj;
                msg.thumbData = bmpToByteArray(bmp, false);
            } else if ("imageFile".equalsIgnoreCase(mediaType)) {
                WXImageObject imgObj = new WXImageObject();
                imgObj.setImagePath(attach1);
                msg.mediaObject = imgObj;
                Bitmap bmp = BitmapFactory.decodeFile(attach1);
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                bmp.recycle();
                msg.thumbData = bmpToByteArray(thumbBmp, true);
            } else if ("imageUrl".equalsIgnoreCase(mediaType)) {
                WXImageObject imgObj = new WXImageObject();
                imgObj.imageUrl = attach1;
                msg.mediaObject = imgObj;
                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeStream(new URL(attach1).openStream());
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                    bmp.recycle();
                    msg.thumbData = bmpToByteArray(thumbBmp, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if ("musicUrl".equalsIgnoreCase(mediaType)) {
                WXMusicObject music = new WXMusicObject();
                music.musicUrl = attach1;
                msg.mediaObject = music;
            } else if ("musicUrlLow".equalsIgnoreCase(mediaType)) {
                WXMusicObject music = new WXMusicObject();
                music.musicLowBandUrl = attach1;
                msg.mediaObject = music;
            } else if ("videoUrl".equalsIgnoreCase(mediaType)) {
                String path = attach1;
                WXVideoObject video = new WXVideoObject();
                video.videoUrl = path;
                msg.mediaObject = video;
            } else if ("videoUrlLow".equalsIgnoreCase(mediaType)) {
                WXVideoObject video = new WXVideoObject();
                video.videoLowBandUrl = attach1;
                msg.mediaObject = video;
            } else if ("url".equalsIgnoreCase(mediaType)) {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = attach1;
                msg.mediaObject = webpage;
            } else if ("appData".equalsIgnoreCase(mediaType) || "app".equalsIgnoreCase(mediaType)) {
                msg.thumbData = null;
                WXAppExtendObject appdata = new WXAppExtendObject();
//                appdata.extInfo = params.length >= 6 ? params[5] : "";
                if (attach1 != null && attach1.length() > 0) {
                    appdata.fileData = readFromFile(attach1, 0, -1);
                }
                appdata.filePath = params[2];
                msg.mediaObject = appdata;
            } else if ("emoji".equalsIgnoreCase(mediaType)) {
                WXEmojiObject emoji = new WXEmojiObject();
                emoji.emojiPath = attach1;
                msg.mediaObject = emoji;
                msg.thumbData = readFromFile(attach1, 0, (int) new File(attach1).length());
            }

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction(mediaType);
            req.message = msg;
            if ("sendTimeline".equalsIgnoreCase(command)) {
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
            } else if ("sendFavorite".equalsIgnoreCase(command)) {
                req.scene = SendMessageToWX.Req.WXSceneFavorite;
            } else {
                req.scene = SendMessageToWX.Req.WXSceneSession;
            }
            return api.sendReq(req) ? "true" : "false";
        }
        return null;
    }

    public final static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    private String genNonceStr() {
        Random random = new Random();
        return getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte[] getHtmlByteArray(final String url) {
        URL htmlUrl = null;
        InputStream inStream = null;
        try {
            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = inputStreamToByte(inStream);

        return data;
    }

    public byte[] getAppIcon() {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Drawable drawable = applicationInfo.loadIcon(context.getPackageManager());
            if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                return bmpToByteArray(bitmapDrawable.getBitmap(), false);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] inputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            Log.i(TAG, "readFromFile: file not found");
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

        if (offset < 0) {
            Log.e(TAG, "readFromFile invalid offset:" + offset);
            return null;
        }
        if (len <= 0) {
            Log.e(TAG, "readFromFile invalid len:" + len);
            return null;
        }
        if (offset + len > (int) file.length()) {
            Log.e(TAG, "readFromFile invalid file len:" + file.length());
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len]; // ���������ļ���С������
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
            e.printStackTrace();
        }
        return b;
    }

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }

            Log.d(TAG, "extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            Log.d(TAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;

            Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                Log.e(TAG, "bitmap decode failed");
                return null;
            }

            Log.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
                Log.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            Log.e(TAG, "decode bitmap failed: " + e.getMessage());
            options = null;
        }

        return null;
    }


    private class GetPrepayIdTask extends AsyncTask<Void, Void, PayReq> {

        String payCode = null;
        String orderId = null;

        public GetPrepayIdTask(String payCode, String orderId) {
            this.payCode = payCode;
            this.orderId = orderId;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(PayReq result) {
//            if (result.localRetCode == LocalRetCode.ERR_OK) {
            api.sendReq(result);
//            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected PayReq doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs(payCode, orderId);

            Log.e("orion", entity);

            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);
            Log.e("orion", content);
            Map<String, String> xml = decodeXml(content);

            PayReq req = new PayReq();
            req.appId = appId;
            req.partnerId = mchId;
            req.prepayId = xml.get("prepay_id");
            req.packageValue = "Sign=WXPay";
            req.nonceStr = genNonceStr();
            req.timeStamp = String.valueOf(genTimeStamp());


            List<NameValuePair> signParams = new LinkedList<NameValuePair>();
            signParams.add(new BasicNameValuePair("appid", req.appId));
            signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
            signParams.add(new BasicNameValuePair("package", req.packageValue));
            signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
            signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
            signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

            req.sign = genAppSign(signParams);

            return req;
        }
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(API_KEY);

//        this.sb.append("sign str\n"+sb.toString()+"\n\n");
        String appSign = getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }

    private String genProductArgs(String payCode, String orderId) {
        StringBuffer xml = new StringBuffer();
        try {
            String price = getConfig(payCode + ".price");
            String subject = getConfig(payCode + ".subject");

            String nonceStr = genNonceStr();
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", appId));
            packageParams.add(new BasicNameValuePair("body", subject));
            packageParams.add(new BasicNameValuePair("mch_id", mchId));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", notifyUrl));
            packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", price));
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));


            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);
            return new String(xmlstring.getBytes(), "ISO8859-1");
        } catch (Exception e) {
            Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");


            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("orion", sb.toString());
        return sb.toString();
    }

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(API_KEY);


        String packageSign = getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", packageSign);
        return packageSign;
    }

    private static enum LocalRetCode {
        ERR_OK, ERR_HTTP, ERR_JSON, ERR_OTHER
    }


    /**
     * 建议 traceid 字段包含用户信息及订单信息，方便后续对订单状态的查询和跟踪
     */
    private String getTraceId() {
        return "crestxu_" + genTimeStamp();
    }

    /**
     * 注意：商户系统内部的订单号,32个字符内、可包含字母,确保在商户系统唯一
     */
    private String genOutTradNo() {
        Random random = new Random();
        return getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long timeStamp;
    private String nonceStr, packageValue;


    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


}
