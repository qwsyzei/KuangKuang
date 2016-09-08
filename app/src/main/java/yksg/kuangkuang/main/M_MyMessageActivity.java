package yksg.kuangkuang.main;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import yksg.kuangkuang.R;
import yksg.kuangkuang.adapters.M_MyMessageAdapter;
import yksg.kuangkuang.models.MyMessage;

/**
 * 我的消息
 */
public class M_MyMessageActivity extends BaseActivity {
private ListView listView;
    M_MyMessageAdapter m_myMessageAdapter;
    private ArrayList<MyMessage> my_messageList;
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
    private ArrayList<MyMessage> getsysList(){
        my_messageList=new ArrayList<MyMessage>();
        for (int i = 0; i <10 ; i++) {
            MyMessage sys=new MyMessage(M_MyMessageActivity.this);
            sys.setTitle("标题" + i);
            sys.setContent("内容" + i);
            sys.setTime("时间" + i);
            my_messageList.add(sys);

        }
        return my_messageList;
    }
}
