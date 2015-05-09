package com.example.gamefloatingdemo;

import java.text.NumberFormat;

import com.huawei.deviceCloud.microKernel.core.MicroKernelFramework;
import com.huawei.deviceCloud.microKernel.manager.update.IUpdateNotifier;
import com.huawei.deviceCloud.microKernel.manager.update.info.ComponentInfo;
import com.huawei.gamebox.buoy.sdk.util.DebugConfig;

/**
 * 插件升级检测的通知，各个接口的具体处理逻辑可由业务自行定制
 * 
 * @author  c00206870
 * @version  [版本号, 2014-4-18]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UpdateNotifierHandler implements IUpdateNotifier
{
    private final static String TAG = UpdateNotifierHandler.class.getSimpleName();
    
    private MicroKernelFramework framework = null;
    
    private final static NumberFormat nf = NumberFormat.getPercentInstance();
    static
    {
        nf.setMaximumFractionDigits(1);
    }
    
    public UpdateNotifierHandler(MicroKernelFramework framework)
    {
        this.framework = framework;
    }
    
    /**
     * 通知某个插件有新版本
     * @param ci 插件信息
     * @param existsOldVersion 是否存在老版本的插件，如果存在，由应用决定是否立即加载
     * 在调用checkSinglePlugin的线程中运行
     */
    @Override
    public void thereAreNewVersion(final ComponentInfo ci, final Runnable downloadHandler, boolean existsOldVersion)
    {
        DebugConfig.d(TAG, "thereAreNewVersion="+ci.toJsonString());
        //开始下载，为了静默下载和加载，所以不弹出对话框
        downloadHandler.run();
    }
    
    /**
     * 开始下载插件，可以在这里显示一个进度条
     * @param ci 插件信息
     * 在调用checkSinglePlugin的线程中运行
     */
    @Override
    public void startDownload(ComponentInfo ci)
    {
        DebugConfig.d(TAG, "startDownload="+ci.toJsonString());
    }
    
    /**
     * 开始下载插件
     * @param ci 插件信息
     * 在下载线程中运行，所以要注意，使用ui线程运行界面操作
     */
    @Override
    public void downloading(final ComponentInfo ci, final long downloadedSize, final long totalSize)
    {
        DebugConfig.d(TAG, "downloading="+ci.toJsonString());
    }
    
    /**
     * 插件下载完成
     * @param ci 插件信息
     * 在下载线程中运行，所以要注意，使用ui线程运行界面操作
     */
    @Override
    public void downloaded(final ComponentInfo ci)
    {
        DebugConfig.d(TAG, "downloaded="+ci.toJsonString());
        framework.loadPlugin(ci.getPackageName());
    }
    
    /**
     * 插件下载失败
     * @param ci 插件信息
     * 在下载线程中运行，所以要注意，使用ui线程运行界面操作
     */
    @Override
    public void downloadFailed(final ComponentInfo ci, boolean existsOldVersion, int errorCode)
    {
        DebugConfig.d(TAG, "downloadFailed="+ci.toJsonString());
    }
    
    /**
     * 插件未更新，本地插件仍然有效
     * @param ci 插件信息
     * 在调用checkSinglePlugin的线程中运行
     */
    @Override
    public void localIsRecent(ComponentInfo ci)
    {
        DebugConfig.d(TAG, "localIsRecent="+ci.toJsonString());
        framework.loadPlugin(ci.getPackageName());
    }
}
