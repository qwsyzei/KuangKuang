package klsd.kuangkuang.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import klsd.kuangkuang.R;
import klsd.kuangkuang.fragment.DiscussFragment;
import klsd.kuangkuang.fragment.MyCommentsFragment;
import klsd.kuangkuang.fragment.MyFavoritesFragment;
import klsd.kuangkuang.fragment.MySettingsFragment;
import klsd.kuangkuang.fragments.MSubjectFragment;
import klsd.kuangkuang.main.MainActivity;

/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description 侧边栏菜单
 */
public class LeftFragment extends Fragment implements OnClickListener{
	private View lastListView;
	private View discussView;
	private View favoritesView;
	private View commentsView;
	private View settingsView;
	
	
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
		lastListView = view.findViewById(R.id.tvLastlist);
		discussView = view.findViewById(R.id.tvDiscussMeeting);
		favoritesView = view.findViewById(R.id.tvMyFavorites);
		commentsView = view.findViewById(R.id.tvMyComments);
		settingsView = view.findViewById(R.id.tvMySettings);

		lastListView.setOnClickListener(this);
		discussView.setOnClickListener(this);
		favoritesView.setOnClickListener(this);
		commentsView.setOnClickListener(this);
		settingsView.setOnClickListener(this);
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

		case R.id.tvLastlist:// 往期列表
			newContent = new MSubjectFragment();
			break;
		case R.id.tvDiscussMeeting: // 讨论集会
			newContent = new DiscussFragment();
			break;
		case R.id.tvMyFavorites: // 我的收藏
			newContent = new MyFavoritesFragment();
			break;
		case R.id.tvMyComments: // 我的评论
			newContent = new MyCommentsFragment();

			break;
		case R.id.tvMySettings: // 设置
			newContent = new MySettingsFragment();
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
