package com.wandoujia.mariosdk.plugin.demo.apidemo;

import android.content.Context;

import com.wandoujia.mariosdk.plugin.api.api.WandouGamesApi;
import com.wandoujia.mariosdk.plugin.api.model.RankingListParams;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnLeaderboardFinishedListener;
import com.wandoujia.mariosdk.plugin.api.model.callback.OnScoreSubmittedListener;
import com.wandoujia.mariosdk.plugin.api.model.exception.LeaderboardException;
import com.wandoujia.mariosdk.plugin.api.model.model.Ranking;
import com.wandoujia.mariosdk.plugin.api.model.model.RankingList;
import com.wandoujia.mariosdk.plugin.api.model.model.result.WandouGamesError;
import com.wandoujia.mariosdk.plugin.demo.MarioPluginApplication;

/**
 * @author liuxv@wandoujia.com
 */
public class LeaderBoardDemo {

  private WandouGamesApi wandouGamesApi;
  private Context context;

  private static final long DEFAULT_LEADERBOARD_ID = 0L;

  public LeaderBoardDemo(){
    wandouGamesApi = MarioPluginApplication.getWandouGamesApi();
  }

  public void apiDemo() {

    /**
     * 上传分数，<b>推荐使用此接口<b/>
     * <p>
     * 调用者不需要关注网络状态等其他失败情况，豌豆荚 SDK 会在调用失败后，在网络状态良好时重试，直到成功。
     * <p/>
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @param leaderboardId 上传分数的目标排行榜ID，开发者可以创建最多10个排行榜
     * @param score 分数值
     */
    wandouGamesApi.submitScore(DEFAULT_LEADERBOARD_ID, 100);


    /**
     * 上传分数
     * <p>
     * 无论成功或者失败，都立即返回，豌豆荚 SDK 将不会重试。
     * <p/>
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @param leaderboardId 上传分数的目标排行榜ID，开发者可以创建最多10个排行榜
     * @param score 分数值
     * @param listener 在调用成功或者失败时回调
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnScoreSubmittedListener}
     */
    wandouGamesApi.submitScoreOneShot(DEFAULT_LEADERBOARD_ID, 100,
        new OnScoreSubmittedListener() {
          @Override
          public void onScoreSubmittedSuccess(long leaderboardId, double score) {

          }

          @Override
          public void onScoreSubmittedFailed(WandouGamesError wandouGamesError, long leaderboardId,
              double score) {

          }
        });



    /**
     * 获取当前用户的排名
     * <p>
     * <b>阻塞方法，请勿在UI线程调用<b/>
     * <p/>
     * <p>
     * <b>注意：需要登录<b/>
     * <p/>
     * 
     * @param params 排行榜参数 {@link com.wandoujia.mariosdk.plugin.api.model.RankingListParams}
     * @return 当前用户的排名 {@link com.wandoujia.mariosdk.plugin.api.model.model.Ranking}
     * @throws com.wandoujia.mariosdk.plugin.api.model.exception.LeaderboardException
     *           {@link com.wandoujia.mariosdk.plugin.api.model.exception.LeaderboardException}
     */
    new Thread(new Runnable() {
      @Override
      public void run() {


        try {
          Ranking ranking =
              wandouGamesApi.getCurrentPlayerRanking(new RankingListParams.Builder(
                  DEFAULT_LEADERBOARD_ID)
                  .setSpanType(RankingListParams.SpanType.WEEKLY)
                  .setScoreType(RankingListParams.ScoreType.HIGH_SCORE)
                  .setPlayerType(RankingListParams.PlayerType.FRIENDS).create());
        } catch (LeaderboardException e) {
          e.printStackTrace();
        }


      }
    }).start();


    /**
     * 获取当前游戏的排行榜列表
     * <p>
     * <b>阻塞方法，请勿在UI线程调用<b/>
     * <p/>
     * 
     * @param start 起始值，从0开始
     * @param length 获取长度
     * @param params 排行榜参数 {@link com.wandoujia.mariosdk.plugin.api.model.RankingListParams}
     * @return {@link com.wandoujia.mariosdk.plugin.api.model.model.RankingList}
     * @throws com.wandoujia.mariosdk.plugin.api.model.exception.LeaderboardException
     *           {@link com.wandoujia.mariosdk.plugin.api.model.exception.LeaderboardException}
     */
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          RankingList rankingList =
              wandouGamesApi.getRankingList(
                  0, 10, new RankingListParams.Builder(DEFAULT_LEADERBOARD_ID)
                  .setSpanType(RankingListParams.SpanType.HISTORY)
                  .setScoreType(RankingListParams.ScoreType.HIGH_SCORE)
                  .setPlayerType(RankingListParams.PlayerType.FRIENDS).create());
        } catch (LeaderboardException e) {
          e.printStackTrace();
        }

      }
    }).start();


    /**
     * 启动 SDK 默认的排行榜展示界面
     * 
     * @param listener 回调
     *          {@link com.wandoujia.mariosdk.plugin.api.model.callback.OnLeaderboardFinishedListener}
     */
    wandouGamesApi.startLeaderboardActivity(new OnLeaderboardFinishedListener() {
      @Override
      public void onLeaderboardFinished() {

      }
    });

  }


}
