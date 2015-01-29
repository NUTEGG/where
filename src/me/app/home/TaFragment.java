package me.app.home;

import me.app.global.Constant;
import me.app.global.MyApplication;
import me.where.home.R;
import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ab.util.AbStrUtil;

public class TaFragment extends Fragment {

	private View view;
	private EditText taUserName, taUserPwd;
	private ImageButton taClearName, taClearPwd;
	private Button btnSearch = null;
	private CheckBox cbCheck;
	private boolean remberTa = false;
	private TaCheck mTaCheck;
	private MyApplication mApplication;

	public interface TaCheck {
		public void checkTaLogin(String name, String pwd, boolean isRem);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.second_fragment, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if (Constant.MI) {
			AdManager.getInstance(this.getActivity().getApplicationContext())
					.init("aaab3ee0a6e55113", "835c8257524ceeb5", false);

			// 广告条接口调用（适用于应用）
			// 将广告条adView添加到需要展示的layout控件中
			LinearLayout adLayout = (LinearLayout) view
					.findViewById(R.id.adLayout);
			AdView adView = new AdView(this.getActivity()
					.getApplicationContext(), AdSize.FIT_SCREEN);
			adLayout.addView(adView);
		}
		this.btnSearch = (Button) view.findViewById(R.id.btn_search);
		this.taUserName = (EditText) view.findViewById(R.id.ta_userName);
		this.taUserPwd = (EditText) view.findViewById(R.id.ta_userPwd);
		this.taClearName = (ImageButton) view.findViewById(R.id.ta_clearName);
		this.taClearPwd = (ImageButton) view.findViewById(R.id.ta_clearPwd);
		this.cbCheck = (CheckBox) view.findViewById(R.id.login_ta_check);
		this.mTaCheck = (TaCheck) this.getActivity();
		this.mApplication = (MyApplication) this.getActivity().getApplication();
		String name = mApplication.ta.getName();
		String pwd = mApplication.ta.getPwd();
		if (name != null && pwd != null) {
			taUserName.setText(name);
			taUserPwd.setText(pwd);
			remberTa = mApplication.ta.isRember();
			cbCheck.setChecked(remberTa);
		}
		this.taUserName.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String name = taUserName.getText().toString().trim();
				int len = name.length();
				if (len > 0) {
					taClearName.setVisibility(View.VISIBLE);
					taClearName.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							taClearName.setVisibility(View.INVISIBLE);
						}

					}, 3000);
				} else {
					taClearName.setVisibility(View.INVISIBLE);
				}
			}

		});

		this.taUserPwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String pwd = taUserPwd.getText().toString().trim();
				int len = pwd.length();
				if (len > 0) {
					taClearPwd.setVisibility(View.VISIBLE);
					taClearPwd.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							taClearPwd.setVisibility(View.INVISIBLE);
						}

					}, 3000);
				} else {
					taClearPwd.setVisibility(View.INVISIBLE);
				}
			}

		});

		this.taClearName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				taUserName.setText("");
			}

		});
		this.taClearPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				taUserPwd.setText("");
			}

		});

		this.cbCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton btnCb, boolean isChecked) {
				// TODO Auto-generated method stub
				remberTa = isChecked;
			}

		});
		this.btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!mApplication.mUser.isLogin()) {
					Toast.makeText(getActivity().getApplicationContext(),
							"亲，登陆后才能查看Ta的信息哦！", Toast.LENGTH_SHORT).show();
					return;
				}
				String name = taUserName.getText().toString().trim();
				String pwd = taUserPwd.getText().toString().trim();
				if (TextUtils.isEmpty(name)) {
					Toast.makeText(getActivity().getApplicationContext(),
							R.string.error_name, Toast.LENGTH_SHORT).show();
					taUserName.setFocusable(true);
					taUserName.requestFocus();
					return;
				}
				if (AbStrUtil.strLength(name) >= 10) {
					Toast.makeText(getActivity().getApplicationContext(),
							R.string.error_name_len2, Toast.LENGTH_SHORT)
							.show();
					taUserName.setFocusable(true);
					taUserName.requestFocus();
					return;
				}
				if (TextUtils.isEmpty(pwd)) {
					Toast.makeText(getActivity().getApplicationContext(),
							R.string.error_pwd, Toast.LENGTH_SHORT).show();
					taUserPwd.setFocusable(true);
					taUserPwd.requestFocus();
					return;
				}
				if (!AbStrUtil.isNumberLetter(pwd)) {
					Toast.makeText(getActivity().getApplicationContext(),
							R.string.error_pwd_expr, Toast.LENGTH_SHORT).show();
				}
				if (AbStrUtil.strLength(pwd) <= 3) {
					Toast.makeText(getActivity().getApplicationContext(),
							R.string.error_pwd_len1, Toast.LENGTH_SHORT).show();
					taUserPwd.setFocusable(true);
					taUserPwd.setFocusable(true);
					return;
				}
				if (AbStrUtil.strLength(pwd) >= 15) {
					Toast.makeText(getActivity().getApplicationContext(),
							R.string.error_pwd_len2, Toast.LENGTH_SHORT).show();
					taUserPwd.setFocusable(true);
					taUserPwd.setFocusable(true);
					return;
				}
				mTaCheck.checkTaLogin(name, pwd, remberTa);
			}

		});
	}
}
