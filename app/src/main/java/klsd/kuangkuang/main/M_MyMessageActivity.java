package klsd.kuangkuang.main;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.M_MyMessageAdapter;
import klsd.kuangkuang.models.My_message;

/**
 * 我的消息
 */
public class M_MyMessageActivity extends BaseActivity {
private ListView listView;
    M_MyMessageAdapter m_myMessageAdapter;
    private ArrayList<My_message> my_messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m__my_message);
        setTitle(getString(R.string.my_message));
        initView();
    }

    private void initView() {
        listView= (ListView) findViewById(R.id.listview_my_message);
        getsysList();
        m_myMessageAdapter=new M_MyMessageAdapter(M_MyMessageActivity.this,my_messageList);
        listView.setAdapter(m_myMessageAdapter);
    }
    private ArrayList<My_message> getsysList(){
        my_messageList=new ArrayList<My_message>();
        for (int i = 0; i <10 ; i++) {
            My_message sys=new My_message(M_MyMessageActivity.this);
            sys.setTitle("标题" + i);
            sys.setContent("内容" + i);
            sys.setTime("时间" + i);
            my_messageList.add(sys);

        }
        return my_messageList;
    }
}
