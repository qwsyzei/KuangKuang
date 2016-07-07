package klsd.kuangkuang.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.S_SubjectAdapter;
import klsd.kuangkuang.models.Subject;

/**
 * 专题
 */
public class MSubjectFragment extends MyBaseFragment {
    View view;
    private S_SubjectAdapter sAdapter;
    private ListView listView;
    private ArrayList<Subject> sList;

    public MSubjectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_msubject, container, false);

        setTitle(getString(R.string.main_subject));
        initView();
        return view;
    }

    private void initView() {
        listView = (ListView) view.findViewById(R.id.listview_msubject);
        getSubjectList();
        sAdapter = new S_SubjectAdapter(getContext(), sList);

        listView.setAdapter(sAdapter);
    }

    public void setTitle(String title) {
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        if (textView != null) textView.setText(title);
    }

    public void setTitleRight(String tv_right) {
        TextView textView = (TextView) view.findViewById(R.id.tv_title_right);
        if (textView != null) textView.setText(tv_right);
    }

    /**
     * 假数据
     *
     * @return
     */
    private Bitmap bit_back, bit_head;

    private ArrayList<Subject> getSubjectList() {
        bit_back = BitmapFactory.decodeResource(getResources(), R.mipmap.abcd);
        bit_head = BitmapFactory.decodeResource(getResources(), R.mipmap.head_pic);
        sList = new ArrayList<Subject>();
        for (int i = 0; i < 11; i++) {
            Subject sub = new Subject(getContext());
            sub.setTitle("钻石趣闻" + i);
            sub.setContent("    四月生日石。 地球上最坚硬的天然物质。 已有十亿年以上的历史。此种风靡全球的宝石，不仅……" + i);
            sub.setAuthor("刘关张" + i);
            sub.setLooked("78");
            sub.setPraise("30");
            sub.setComment("10");
            sub.setBackground(bit_back);
            sub.setHead_pic(bit_head);
            sList.add(sub);

        }
        return sList;
    }

}
