package klsd.kuangkuang.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.MainActivity;

/**
 * @description 侧边栏菜单
 */
public class LeftFragment extends Fragment implements OnClickListener{
	private View lastListView;
	private View discussView;
	private View favoritesView;
	private View commentsView;
	private View settingsView;
	private View favoritesView1;
	private View commentsView1;
	private View settingsView1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		
		return view;
	}
	
	
	public void findViews(View view) {
		lastListView = view.findViewById(R.id.tvmingxing);
		discussView = view.findViewById(R.id.tvhunlian);
		favoritesView = view.findViewById(R.id.tvzuanshi);
		commentsView = view.findViewById(R.id.tvmizuan);
		settingsView = view.findViewById(R.id.tvzhuanjia);
		favoritesView1 = view.findViewById(R.id.tvzhubao);
		commentsView1 = view.findViewById(R.id.tvzhubaogonglue);
		settingsView1 = view.findViewById(R.id.tvkuangkuang);

		lastListView.setOnClickListener(this);
		discussView.setOnClickListener(this);
		favoritesView.setOnClickListener(this);
		commentsView.setOnClickListener(this);
		settingsView.setOnClickListener(this);
		favoritesView1.setOnClickListener(this);
		commentsView1.setOnClickListener(this);
		settingsView1.setOnClickListener(this);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		switch (v.getId()) {

		case R.id.tvmingxing:// 往期列表
			newContent = new MSubjectFragment();
			break;
		case R.id.tvhunlian: // 讨论集会
			newContent = new MSubjectFragment();
			break;
		case R.id.tvzuanshi: // 我的收藏
			newContent = new MSubjectFragment();
			break;
		case R.id.tvmizuan: // 我的评论
			newContent = new MSubjectFragment();

			break;
		case R.id.tvzhuanjia: // 设置
			newContent = new MSubjectFragment();
			break;
			case R.id.tvzhubao: // 设置
				newContent = new MSubjectFragment();
				break;
			case R.id.tvzhubaogonglue: // 设置
				newContent = new MSubjectFragment();
				break;
			case R.id.tvkuangkuang: // 设置
				newContent = new MSubjectFragment();
				break;
		default:
			break;
		}
		if (newContent != null) {
			switchFragment(newContent);
		}
	}
	
	/**
	 * 切换fragment
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchConent(fragment);
		}
	}
	
}
