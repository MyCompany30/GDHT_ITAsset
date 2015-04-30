package com.gdht.itasset.version;

import com.gdht.itasset.PlanListActivity;
import com.gdht.itasset.PlanListActivity;
import com.gdht.itasset.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

public class VersionServiceIndex extends Service {
	private NotificationManager notificationMrg;
	private int old_process = 0;
	private boolean isFirstStart=false;

	public void onCreate() {
		super.onCreate();isFirstStart=true;
		notificationMrg = (NotificationManager) this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
//		System.out.println(PlanListActivity.loading_process+"==");
		mHandler.handleMessage(new Message());
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 1为出现，2为隐藏
			if(PlanListActivity.loading_process>99){
				notificationMrg.cancel(0);
				stopSelf();
				return;
			}
			if(PlanListActivity.loading_process>old_process){
				displayNotificationMessage(PlanListActivity.loading_process);
			}
			
			new Thread() {
				public void run() {
					isFirstStart=false;
					Message msg = mHandler.obtainMessage();
					mHandler.sendMessage(msg);
				}
			}.start();
			old_process =PlanListActivity.loading_process;
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void displayNotificationMessage(int count) {

		// Notification的Intent，即点击后转向的Activity
		Intent notificationIntent1 = new Intent(this, this.getClass());
		notificationIntent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent1 = PendingIntent.getActivity(this, 0,
				notificationIntent1, 0);

		// 创建Notifcation
		Notification notification = new Notification(R.drawable.tubiao,
				"IT资产管理更新", System.currentTimeMillis());// 设定Notification出现时的声音，一般不建议自定义
		if(isFirstStart || PlanListActivity.loading_process>97){
			notification.defaults |= Notification.DEFAULT_SOUND;// 设定是否振动
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}notification.flags |= Notification.FLAG_ONGOING_EVENT;

		notification.icon = R.drawable.tubiao;
		
		// 创建RemoteViews用在Notification中
		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.notification_version);
		contentView.setTextViewText(R.id.n_title,
				"升级提示");
		contentView.setTextViewText(R.id.n_text, "当前进度："+count+"% ");
		contentView.setProgressBar(R.id.n_progress, 100, count, false);
		contentView.setImageViewResource(R.id.imageView, R.drawable.tubiao);

		notification.contentView = contentView;
		notification.contentIntent = contentIntent1;
		notificationMrg.notify(0, notification);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
