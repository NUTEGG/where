package me.where.more;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import me.app.global.Constant;
import me.app.global.MyApplication;
import me.http.utils.MyHttpUtils;
import me.http.utils.MyThreadPool;
import me.where.home.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.util.AbStrUtil;

public class LeaveMsgActivity extends AbActivity {

	private Button btnMsg;
	private TextView tvMsg;
	private MyHandler mHandler;
	private MyApplication mApplication = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_leave_msg);
		overridePendingTransition(R.anim.anim_into, R.anim.anim_back);
		this.mAbTitleBar.setTitleText(R.string.title_activity_leave_msg);
		this.mAbTitleBar.setLogo(R.drawable.button_selector_back);
		this.mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		this.mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		this.mAbTitleBar.setLogoLine(R.drawable.line);
		this.btnMsg = (Button) findViewById(R.id.leaveMsg);
		this.tvMsg = (TextView) findViewById(R.id.tv_leave_msg);
		this.mApplication = (MyApplication) this.getApplication();
		this.mHandler = new MyHandler(this);
		this.btnMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String msg = tvMsg.getText().toString().trim();
				if (!AbStrUtil.isEmpty(msg)) {
					MyThreadPool.getInstance().getExecutorService()
							.submit(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Map<String, String> params = new HashMap<String, String>();
									if (mApplication.mUser != null
											&& mApplication.mUser.getName() != null) {
										params.put("msg",
												mApplication.mUser.getName()
														+ "发送的：" + msg);
									} else {
										params.put("msg", msg);
									}
									MyHttpUtils.postDataByUrl(Constant.WBSITE
											+ "msg", params);
									mHandler.sendEmptyMessage(1);
								}

							});
				} else {
					Toast.makeText(
							LeaveMsgActivity.this.getApplicationContext(),
							"留言内容不能为空", Toast.LENGTH_SHORT).show();
				}
			}

		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.close_enter, R.anim.close_exit);
	}

	private static final class MyHandler extends Handler {
		private final WeakReference<LeaveMsgActivity> mMsgActivity;

		private MyHandler(LeaveMsgActivity msgActivity) {
			this.mMsgActivity = new WeakReference<LeaveMsgActivity>(msgActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			LeaveMsgActivity msgActivity = mMsgActivity.get();
			if (msgActivity != null) {
				switch (msg.what) {
				case 1:
					Toast.makeText(msgActivity.getApplicationContext(),
							"发送成功，感谢您的留言！", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leave_msg, menu);
		return true;
	}

}
