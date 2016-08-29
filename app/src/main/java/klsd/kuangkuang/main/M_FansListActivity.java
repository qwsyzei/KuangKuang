package klsd.kuangkuang.main;

import android.os.Bundle;
import android.widget.ListView;
import com.lidroid.xutils.http.client.HttpRequest;
import java.util.ArrayList;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.M_FansListAdapter;
import klsd.kuangkuang.models.Fans;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.utils.UIutils;

/**
 * 粉丝列表
 */
public class M_FansListActivity extends BaseActivity {
    private ListView listView;
    private ArrayList<Fans> tlist;
    private M_FansListAdapter fAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_fans);
        setTitle(getString(R.string.my_fans));
        initView();
    }
    private void initView() {
        tlist = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview_fanslist);
        UIutils.showLoading(M_FansListActivity.this);
        getfansList();
    }
    /**
     * 获取粉丝列表
     */
    public void getfansList() {
        new MyHTTP(this).baseRequest(Consts.fanslistApi, JSONHandler.JTYPE_FANS_LIST,
                HttpRequest.HttpMethod.GET, null, getHandler());
    }
    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_FANS_LIST)) {
            UIutils.cancelLoading();
            tlist = (ArrayList<Fans>) handlerBundler.getSerializable("fans");
            if (tlist.size() == 0) {
                ToastUtil.show(M_FansListActivity.this, getString(R.string.no_data));
                return;
            }
            fAdapter = new M_FansListAdapter(M_FansListActivity.this, tlist,getHandler());
            listView.setAdapter(fAdapter);
        }

    }
}
