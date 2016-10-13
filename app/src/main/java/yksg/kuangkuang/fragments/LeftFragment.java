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

/**
 * @description 侧边栏菜单
 */
public class LeftFragment extends MyBaseFragment implements OnClickListener {

    private View allView;
    private View jiangxinView;
    private View zhexiangView;
    private View shanyaoView;
    private View kuangkuangView;
    private TextView tv_feedback;
    String subject_key = "all";

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
        allView = view.findViewById(R.id.all_total);
        jiangxinView = view.findViewById(R.id.tv_jiangxinyike);
        zhexiangView = view.findViewById(R.id.tv_zhexiangyouli);
        shanyaoView = view.findViewById(R.id.tv_shanyaozhenqing);
        kuangkuangView = view.findViewById(R.id.tv_kuangkuang);


        tv_feedback.setOnClickListener(this);
        allView.setOnClickListener(this);
        jiangxinView.setOnClickListener(this);
        zhexiangView.setOnClickListener(this);
        shanyaoView.setOnClickListener(this);
        kuangkuangView.setOnClickListener(this);
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
                subject_key = "all";
                newContent = new MSubjectFragment(subject_key);
                break;
            case R.id.tv_jiangxinyike: // 匠心一克
                subject_key = "匠心一克";
                newContent = new MSubjectFragment(subject_key);
                break;
            case R.id.tv_zhexiangyouli: // 这厢有礼
                subject_key = "这厢有礼";
                newContent = new MSubjectFragment(subject_key);
                break;
            case R.id.tv_shanyaozhenqing: // 闪耀真情
                subject_key = "闪耀真情";
                newContent = new MSubjectFragment(subject_key);
                break;
            case R.id.tv_kuangkuang: // 硄硄说
                subject_key = "硄硄说";
                newContent = new MSubjectFragment(subject_key);
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
