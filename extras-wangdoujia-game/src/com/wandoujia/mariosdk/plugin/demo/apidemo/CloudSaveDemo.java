package com.wandoujia.mariosdk.plugin.demo.apidemo;

import android.content.Context;

import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnCloudDataLoadedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnCloudDataSavedListener;
import com.wandoujia.mariosdk.plugin.api.model.model.result.CloudDataResult;
import com.wandoujia.mariosdk.plugin.api.model.model.result.ConflictResult;
import com.wandoujia.mariosdk.plugin.api.model.model.result.WandouGamesError;
import com.wandoujia.mariosdk.plugin.demo.MarioPluginApplication;

/**
 * @author liuxv@wandoujia.com
 */
public class CloudSaveDemo {

  private WandouGamesApi wandouGamesApi;
  private Context context;


  private static final String DEFAULT_SLOT_ID = "1";

  private static final String GAME_DATA = "{\"TAG\":\"hello world!\"}";

  public CloudSaveDemo(){
    wandouGamesApi = MarioPluginApplication.getWandouGamesApi();
  }

  public void apiDemo() {

    /**
     * 向云存储系统保存数据
     * <p>
     * 调用者使用此接口向云端存储数据，当成功时会回调
     * {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnCloudDataSavedListener}
     * 中的OnSavedSuccess 当本地版本和服务器版本有冲突的时候，会通过回调中的OnSavedFailed传会冲突数据
     * {@link com.wandoujia.mariosdk.plugin.api.model.model.result.ConflictResult}
     * 将云端数据和这次提交的数据全部返回，调用者在调用 resolveConflict(String slotId) 后，重新选择上传数据并上传。
     * <p/>
     * 
     * @param slotId 目标存储块ID
     * @param gameData 二进制的存储数据信息
     * @param listener 在调用成功或者失败时调用
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnCloudDataSavedListener}
     */
    wandouGamesApi.saveCloudData(DEFAULT_SLOT_ID, GAME_DATA.getBytes(),
        new OnCloudDataSavedListener() {
          @Override
          public void onSavedSuccess(String slotId) {

          }

          @Override
          public void onSavedFailed(String slotId, WandouGamesError error, ConflictResult result) {

          }
        });


    /**
     * 向云存储系统获取数据信息
     * <p>
     * 读取云端最新的数据，推荐在存储数据前，先读取最新的服务端数据，根据情况，再选择存储。
     * <p/>
     * 
     * @param slotId 目标存储块ID
     * @param listener 在调用成功或者失败时调用
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnCloudDataLoadedListener}
     */
    wandouGamesApi.loadCloudData(DEFAULT_SLOT_ID, new OnCloudDataLoadedListener() {
      @Override
      public void onLoadedSuccess(String slotId, CloudDataResult result) {

      }

      @Override
      public void onLoadedFailed(String slotId, WandouGamesError error) {

      }
    });



    /**
     * 解决数据冲突
     * <p>
     * 在向云端存储数据发生冲突时，调用此接口解决冲突。再解决冲突后，即可使用 saveCloudData 存储数据
     * <p/>
     * 
     * @param slotId 目标存储块ID
     */
    wandouGamesApi.resolveConflict(DEFAULT_SLOT_ID);

  }


}
