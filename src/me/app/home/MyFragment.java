package me.app.home;

import me.app.global.MyApplication;
import me.where.home.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MyFragment extends Fragment {

	private View view;
	private LinearLayout mLayoutViewPwd, mLayoutMember, mLayoutUs, mSignToday,
			mSoftUse, mLeaveMsg, mLayoutApp,mLayoutGame;
	private MyApplication mApplication;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.view = inflater.inflate(R.layout.myfragment, null);
		return view;
	}

	public interface Refresh {
		public void refresh(View login, View logout);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		this.mLayoutViewPwd = (LinearLayout) view
				.findViewById(R.id.linear_view_pwd);
		this.mLayoutMember = (LinearLayout) view
				.findViewById(R.id.linear_member);
		this.mSignToday = (LinearLayout) view.findViewById(R.id.sign_in);
		this.mSoftUse = (LinearLayout) view.findViewById(R.id.use_instruction);
		this.mLeaveMsg = (LinearLayout) view
				.findViewById(R.id.linear_leave_msg);
		this.mLayoutApp = (LinearLayout) view.findViewById(R.id.linear_app);
		this.mLayoutGame = (LinearLayout) view.findViewById(R.id.linear_game);
		View.OnClickListener mClickListener = (View.OnClickListener) this
				.getActivity();
		this.mLayoutUs = (LinearLayout) view.findViewById(R.id.linear_us);
		this.mLayoutViewPwd.setOnClickListener(mClickListener);
		this.mLayoutMember.setOnClickListener(mClickListener);
		this.mLayoutUs.setOnClickListener(mClickListener);
		this.mSignToday.setOnClickListener(mClickListener);
		this.mSoftUse.setOnClickListener(mClickListener);
		this.mLeaveMsg.setOnClickListener(mClickListener);
		this.mLayoutApp.setOnClickListener(mClickListener);
		this.mLayoutGame.setOnClickListener(mClickListener);
		this.mApplication = (MyApplication) this.getActivity().getApplication();
	}
}
