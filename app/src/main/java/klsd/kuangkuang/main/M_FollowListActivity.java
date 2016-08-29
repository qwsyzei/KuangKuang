package klsd.kuangkuang.main;

import android.os.Bundle;
import android.widget.ListView;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.ArrayList;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.M_FollowListAdapter;
import klsd.kuangkuang.models.Follows;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.utils.UIutils;

/**
 * 关注列表
 */
public class M_FollowListActivity extends BaseActivity {
    private ListView listView;
    private ArrayList<Follows> tlist;
    private M_FollowListAdapter fAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m__follow_list);
        setTitle(getString(R.string.my_follows));
        initView();
    }
    private void initView() {
        tlist = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview_followlist);
        UIutils.showLoading(M_FollowListActivity.this);
        getfollowList();
    }
    /**
     * 获取关注列表
     */
    public void getfollowList() {
        new MyHTTP(this).baseRequest(Consts.followslistApi, JSONHandler.JTYPE_FOLLOW_LIST,
                HttpRequest.HttpMethod.GET, null, getHandler());
    }
    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_FOLLOW_LIST)) {
            UIutils.cancelLoading();
            tlist = (ArrayList<Follows>) handlerBundler.getSerializable("follows");
            if (tlist.size() == 0) {
                ToastUtil.show(M_FollowListActivity.this,getString(R.string.no_data));
                return;
            }
            fAdapter = new M_FollowListAdapter(M_FollowListActivity.this, tlist,getHandler());
            listView.setAdapter(fAdapter);
        }

    }
}
