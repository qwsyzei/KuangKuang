package yksg.kuangkuang.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import yksg.kuangkuang.R;
import yksg.kuangkuang.main.M_FeedBackActivity;
import yksg.kuangkuang.main.MainActivity;
import yksg.kuangkuang.utils.ToastUtil;

/**
 * @description 侧边栏菜单
 */
public class LeftFragment extends MyBaseFragment implements OnClickListener {
    private View settingsView;
    private View favoritesView1;
    private View commentsView1;
    private View settingsView1;
    private TextView tv_feedback;
    String subject_key = "0";

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
                subject_key = "0";
                newContent = new MSubjectFragment(subject_key);
                break;
            case R.id.tvzhubao: // 珠宝故事
                subject_key = "1";
                newContent = new MSubjectFragment(subject_key);
                break;
            case R.id.tvzhubaogonglue: // 首饰攻略
                subject_key = "2";
                newContent = new MSubjectFragment(subject_key);
                break;
            case R.id.tvkuangkuang: // 硄硄说
                ToastUtil.show(getActivity(), getString(R.string.please_wait));
            default:
                break;
        }
        if (newContent != null) {
            switchFragment(subject_key);
        }
    }

    /**
     * 切换fragment
     */
    private void switchFragment(String subject_key) {
        if (getActivity() == null) {
            return;
        }
        if (getActivity() instanceof MainActivity) {
            MainActivity fca = (MainActivity) getActivity();

            Intent intent = new Intent(fca, MainActivity.class);
            intent.putExtra("subject", subject_key);
            startActivity(intent);
            fca.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            fca.finish();

        }
    }

}
