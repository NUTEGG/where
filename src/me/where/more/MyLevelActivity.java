package me.where.more;

import me.app.data.Member;
import me.app.global.MyApplication;
import me.where.home.R;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.ab.activity.AbActivity;

public class MyLevelActivity extends AbActivity {

	private TextView tvMemberMore, tvMemberInstruction;
	private MyApplication mApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.activity_my_level);
		overridePendingTransition(R.anim.anim_into, R.anim.anim_back);
		this.mAbTitleBar.setTitleText(R.string.member_instruct);
		this.mAbTitleBar.setLogo(R.drawable.button_selector_back);
		this.mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		this.mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		this.mAbTitleBar.setLogoLine(R.drawable.line);
		this.tvMemberMore = (TextView) this.findViewById(R.id.m_member_score);
		this.tvMemberInstruction = (TextView) this
				.findViewById(R.id.member_instruction);
		this.mApplication = (MyApplication) this.getApplication();
		Member member = mApplication.mapMember.get(mApplication.mUser
				.getMember());
		StringBuilder sb = new StringBuilder();
		if (member != null) {
			sb.append("亲,您当前的会员积分是：").append(mApplication.mUser.getScore())
					.append("\n您的会员等级是：").append(member.getName())
					.append("\n您可以查看好友位置数量为：").append(member.getViewNum());
		} else {
			sb.append("对不起，您登录后才能看到您的会员结果哦！");
		}
		this.tvMemberMore.setText(sb.toString());
		StringBuilder sb2 = new StringBuilder();
		sb2.append("会员等级说明\n");
		if (mApplication.mapMember != null) {
			for (int i = 1, len = mApplication.mapMember.size(); i <= len; i++) {
				Member mMember = mApplication.mapMember.get(i);
				if (mMember != null) {
					sb2.append(mMember.getName() + "积分：")
							.append(mMember.getScore() + "\t可查看好友位置数量：")
							.append(mMember.getViewNum() + "\n");
				}
			}
		}
		this.tvMemberInstruction.setText(sb2.toString());
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.close_enter, R.anim.close_exit);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_level, menu);
		return true;
	}

}
