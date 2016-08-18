package klsd.kuangkuang.main;

import android.os.Bundle;
import android.widget.ListView;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.ArrayList;
import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.M_BlackListAdapter;
import klsd.kuangkuang.models.Blacklist;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.utils.UIutils;

/**
 * 黑名单界面
 */
public class M_BlackListActivity extends BaseActivity {
    private ListView listView;
    private ArrayList<Blacklist> tlist;
    private M_BlackListAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_black_list);
        setTitle(getString(R.string.black_list));
        initView();
    }

    private void initView() {
        tlist = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview_blacklist);
        UIutils.showLoading(M_BlackListActivity.this);
        getBlackList();
    }
    /**
     * 获取黑名单列表
     */
    public void getBlackList() {
        new MyHTTP(this).baseRequest(Consts.blacklistApi, JSONHandler.JTYPE_BLACK_LIST,
                HttpRequest.HttpMethod.GET, null, getHandler());
    }
    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_BLACK_LIST)) {
            UIutils.cancelLoading();
            tlist = (ArrayList<Blacklist>) handlerBundler.getSerializable("blacklist");
            if (tlist.size() == 0) {
                ToastUtil.show(M_BlackListActivity.this, getString(R.string.no_data));
                return;
            }
            sAdapter = new M_BlackListAdapter(M_BlackListActivity.this, tlist,getHandler());
            listView.setAdapter(sAdapter);
        }

    }
}
