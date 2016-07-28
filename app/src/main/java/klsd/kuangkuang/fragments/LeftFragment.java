package klsd.kuangkuang.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.M_FeedBackActivity;
import klsd.kuangkuang.main.MainActivity;
import klsd.kuangkuang.utils.ToastUtil;

/**
 * @description 侧边栏菜单
 */
public class LeftFragment extends Fragment implements OnClickListener {
    private View settingsView;
    private View favoritesView1;
    private View commentsView1;
    private View settingsView1;
    private TextView tv_feedback;

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
        tv_feedback = (TextView) view.findViewById(R.id.tv_feedback);

        settingsView = view.findViewById(R.id.all_total);
        favoritesView1 = view.findViewById(R.id.tvzhubao);
        commentsView1 = view.findViewById(R.id.tvzhubaogonglue);
        settingsView1 = view.findViewById(R.id.tvkuangkuang);

        tv_feedback.setOnClickListener(this);

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
            case R.id.tv_feedback:// 意见反馈
                Intent intent = new Intent(getActivity(), M_FeedBackActivity.class);
                getActivity().startActivity(intent);
                break;

            case R.id.all_total: // 全部
                newContent = new MSubjectFragment("0");
                break;
            case R.id.tvzhubao: // 珠宝故事
                newContent = new MSubjectFragment("1");
                break;
            case R.id.tvzhubaogonglue: // 首饰攻略
                newContent = new MSubjectFragment("2");
                break;
            case R.id.tvkuangkuang: // 硄硄说
                ToastUtil.show(getActivity(),getString(R.string.please_wait));
            default:
                break;
        }
        if (newContent != null) {
            switchFragment(newContent);
        }
    }

    /**
     * 切换fragment
     *
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
