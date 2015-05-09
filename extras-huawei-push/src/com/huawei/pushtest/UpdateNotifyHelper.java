package com.huawei.pushtest;

import java.text.NumberFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
 
import com.huawei.deviceCloud.microKernel.manager.update.IUpdateNotifier;
import com.huawei.deviceCloud.microKernel.manager.update.info.ComponentInfo;
import com.huawei.deviceCloud.microKernel.push.UpdateHelper;

public class UpdateNotifyHelper implements IUpdateNotifier {

    private final static String TAG = UpdateNotifyHelper.class.getSimpleName();

    private Activity context = null;
    private ProgressDialog progressDialog = null; 

    private final static NumberFormat nf = NumberFormat.getPercentInstance();
    static {
        nf.setMaximumFractionDigits(1);

    }
    
    public UpdateNotifyHelper(Activity context){
        
        this.context = context;
    }

    /**
     * 通知某个插件有新版本
     * 
     * @param ci
     *            插件信息
     * @param existsOldVersion
     *            是否存在老版本的插件，如果存在，由应用决定是否立即加载 在调用UpdateHelper.check的线程中运行
     */
    @Override
    public void thereAreNewVersion(ComponentInfo ci, final Runnable downloadHandler, boolean existsOldVersion) {
        Log.d(TAG, "thereAreNewVersion:" + ci.getPackageName() + ", versionCode:" + ci.getVersionCode());

        // 开发者可以自己定制自己的插件升级提示，以下是示例
        // start
        DialogInterface.OnClickListener update = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                /**
                 * 当用户确定下载时，开始下载，其他的都不要调用此函数 非阻塞，会启动新的线程下载
                 */
                downloadHandler.run();
                dialog.dismiss();
            }
        };

        DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(context).setTitle("ALERT: Plugin has new version" + ci.getPackageName())
                .setMessage(ci.getPackageName() + " has new version " + ci.getVersionName() + ". Do you want to update it?")
                .setNegativeButton("Cancel", cancel).setPositiveButton("Update", update).show();
        // end

    }

    /**
     * 开始下载插件，可以在这里显示一个进度条
     * @param ci 插件信息
     * 在调用UpdateHelper.check的线程中运行
     */ 
    @Override
    public void startDownload(ComponentInfo ci) {

        Log.d(TAG, "startDownload:" + ci.getPackageName() + ", versionCode:" + ci.getVersionCode());

        // 开发者可以自己定制自己的插件升级提示，以下是示例
        // start
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Download " + ci.getPackageName());
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                
                /**
                 * 当用户下载时，中途取消下载，可以调用 UpdateHelper.cancelDownload(); 并且释放UpdateHelper
                 */ 
                UpdateHelper.cancelDownload(); 
                UpdateHelper.onDestory();
                progressDialog.dismiss();
            }
        });
        progressDialog.show();
        // end

    }

    /**
     * 开始下载插件
     * 
     * @param ci
     *            插件信息 在下载线程中运行，所以要注意，使用ui线程运行界面操作
     */
    @Override
    public void downloading(final ComponentInfo ci, final long downloadedSize, final long totalSize) {

        Log.d(TAG, "downloading:" + ci.getPackageName() + ", versionCode:" + ci.getVersionCode());

        // 开发者可以自己定制自己的插件升级提示，以下是示例
        // start
        context.runOnUiThread(new Runnable() {
            public void run() {
                progressDialog.setMessage("Load..." + nf.format(((double) downloadedSize / totalSize)));
            }
        });
        // end
    }

    /**
     * 插件下载完成
     * @param ci 插件信息
     * 在下载线程中运行，所以要注意，使用ui线程运行界面操作
     */
    @Override
    public void downloaded(final ComponentInfo ci) {
        Log.d(TAG, "downloaded:" + ci.getPackageName() + ", versionCode:" + ci.getVersionCode());
        // 开发者可以自己定制自己的插件升级提示，以下是示例
        // start
        context.runOnUiThread(new Runnable() {
            public void run(){ 
                progressDialog.dismiss();
                Toast.makeText(context, "Success to download " + ci.getPackageName(), Toast.LENGTH_LONG).show();
            }
        }); 
        // end
        
        //释放UpdateHelper 
        UpdateHelper.onDestory();
    }

    /**
     * 插件下载失败
     * @param ci 插件信息
     * 在下载线程中运行，所以要注意，使用ui线程运行界面操作
     */
    @Override
    public void downloadFailed(final ComponentInfo ci, boolean existsOldVersion, int errorCode) {
        Log.d(TAG, "downloadFailed:" + ci.getPackageName() + ", versionCode:" + ci.getVersionCode() + ",errorCode:" + errorCode);
        // 开发者可以自己定制自己的插件升级提示，以下是示例
        // start
        context.runOnUiThread(new Runnable() {
            public void run() {
                progressDialog.dismiss();
                Toast.makeText(context, "Fail to download " + ci.getPackageName(), Toast.LENGTH_LONG).show();
            }
        }); 
        // end
        //释放UpdateHelper 
        UpdateHelper.onDestory();
    }
  

    /**
     * 插件未更新，本地插件仍然有效
     * 
     * @param ci
     *            插件信息 在调用checkSinglePlugin的线程中运行
     */
    @Override
    public void localIsRecent(ComponentInfo ci) {
        Log.d(TAG, "localIsRecent:" + ci.getPackageName() + ", versionCode:" + ci.getVersionCode());
        // 开发者可以不用处理该事件
    }
  
}
