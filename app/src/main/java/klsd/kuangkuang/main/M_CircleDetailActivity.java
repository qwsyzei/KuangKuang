package klsd.kuangkuang.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.adapters.C_CircleCommentAdapter;
import klsd.kuangkuang.adapters.C_CircleGridAdapter;
import klsd.kuangkuang.adapters.M_DetailLikeAdapter;
import klsd.kuangkuang.fragments.MMeFragment;
import klsd.kuangkuang.models.CircleAllComment;
import klsd.kuangkuang.models.CircleGridViewEntity;
import klsd.kuangkuang.models.CircleLike;
import klsd.kuangkuang.utils.Consts;
import klsd.kuangkuang.utils.DataCenter;
import klsd.kuangkuang.utils.JSONHandler;
import klsd.kuangkuang.utils.KelaParams;
import klsd.kuangkuang.utils.MyDate;
import klsd.kuangkuang.utils.MyHTTP;
import klsd.kuangkuang.utils.ToastUtil;
import klsd.kuangkuang.views.ContainsEmojiEditText;
import klsd.kuangkuang.views.ExitDialog;
import klsd.kuangkuang.views.PullToRefresh123View;
import klsd.kuangkuang.views.SelfGridView;
import klsd.kuangkuang.views.SelfListView;

import static klsd.kuangkuang.R.id.dialog_exit_title;
import static klsd.kuangkuang.utils.MyApplication.initImageLoader;

/**
 * 朋友圈详情页
 */
public class M_CircleDetailActivity extends BaseActivity implements View.OnClickListener, PullToRefresh123View.OnFooterRefreshListener {
    private String id, head_pic, time, nickname, content, like_number, comment_number;
    private String url1, url2, url3, url4, url5, url6, url7, url8, url9;

    private ImageView im_head;
    private TextView tv_time, tv_nickname, tv_content, tv_like, tv_comment;
    private LinearLayout layout_like, layout_comment, layout_delete;
    private SelfGridView gridview, gridview_like;
    private int number;//9宫格图片的个数
    private List<CircleGridViewEntity> headerEntitiesList;
    private C_CircleGridAdapter cGridAdapter;
    private M_DetailLikeAdapter mdAdapter;
    ArrayList<CircleAllComment> mylist;
    private C_CircleCommentAdapter allAdapter;
    private int page = 1;
    private SelfListView listView;
    private PopupWindow cPopwindow;
    private TextView tv_dialog_send;
    private ContainsEmojiEditText edit_dialog_comment;
    // 自定义的listview的上下拉动刷新
    private PullToRefresh123View mPullToRefreshView;
    private TextView tv_dialog_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_circle_detail);
        setTitle(getString(R.string.details));
        Context context = getApplicationContext();
        initImageLoader(context);
        initView();

    }

    private void initView() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        head_pic = intent.getStringExtra("head_pic");
        time = intent.getStringExtra("created_at");
        nickname = intent.getStringExtra("nickname");
        content = intent.getStringExtra("content");
        like_number = intent.getStringExtra("like");
        comment_number = intent.getStringExtra("comment");
        url1 = intent.getStringExtra("url1");
        url2 = intent.getStringExtra("url2");
        url3 = intent.getStringExtra("url3");
        url4 = intent.getStringExtra("url4");
        url5 = intent.getStringExtra("url5");
        url6 = intent.getStringExtra("url6");
        url7 = intent.getStringExtra("url7");
        url8 = intent.getStringExtra("url8");
        url9 = intent.getStringExtra("url9");
        likeList();
        if (url1.equals("null")) {
            number = 0;
        } else if (url2.equals("null")) {
            number = 1;
        } else if (url3.equals("null")) {
            number = 2;
        } else if (url4.equals("null")) {
            number = 3;
        } else if (url5.equals("null")) {
            number = 4;
        } else if (url6.equals("null")) {
            number = 5;
        } else if (url7.equals("null")) {
            number = 6;
        } else if (url8.equals("null")) {
            number = 7;
        } else if (url9.equals("null")) {
            number = 8;
        } else {
            number = 9;
        }
        mylist = new ArrayList<>();
        mPullToRefreshView = (PullToRefresh123View) findViewById(R.id.pull_refresh_view_circle_detail);
        listView = (SelfListView) findViewById(R.id.listview_circle_detail);
//        listView.setOnScrollListener(this);
        gridview = (SelfGridView) findViewById(R.id.gridview_circle_detail);
        gridview_like = (SelfGridView) findViewById(R.id.gridview_circle_detail_like);
        im_head = (ImageView) findViewById(R.id.circle_detail_head_pic);
        tv_time = (TextView) findViewById(R.id.circle_detail_time);
        tv_nickname = (TextView) findViewById(R.id.circle_detail_name);
        tv_content = (TextView) findViewById(R.id.circle_detail_content);
        layout_like = (LinearLayout) findViewById(R.id.layout_circle_detail_like);
        layout_comment = (LinearLayout) findViewById(R.id.layout_circle_detail_comment);
        layout_delete = (LinearLayout) findViewById(R.id.layout_circle_detail_delete);
        tv_like = (TextView) findViewById(R.id.circle_detail_like);
        tv_comment = (TextView) findViewById(R.id.circle_detail_comment);
        layout_like.setOnClickListener(this);
        layout_comment.setOnClickListener(this);
        layout_delete.setOnClickListener(this);
        mPullToRefreshView.setOnFooterRefreshListener(this);

        if (!head_pic.equals("null")) {
            ImageLoader.getInstance().displayImage(Consts.host + "/" + head_pic, im_head);
        }
        tv_time.setText(MyDate.monthDay(time));
        tv_nickname.setText(nickname);
        tv_content.setText(content);
        if (like_number.equals("null")) {
            tv_like.setText("0");
        } else if (like_number.contains(".0")) {
            tv_like.setText(like_number.replace(".0", ""));
        } else {
            tv_like.setText(like_number);
        }
        if (comment_number.equals("null")) {
            tv_comment.setText("0");
        } else if (comment_number.contains(".0")) {
            tv_comment.setText(comment_number.replace(".0", ""));
        } else {
            tv_comment.setText(comment_number);
        }
        headerEntitiesList = new ArrayList<>();
        String[] url = new String[]{url1, url2, url3, url4, url5, url6, url7, url8, url9};
        for (int i = 0; i < number; i++) {
            CircleGridViewEntity cirEntity = new CircleGridViewEntity(M_CircleDetailActivity.this, Consts.host + "/" + url[i]);
            headerEntitiesList.add(cirEntity);
        }
        final ArrayList<String> imageUrls;//9宫格URL列表(final必须加，目前不知道为什么)
        imageUrls = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            imageUrls.add(Consts.host + "/" + url[i]);
        }
        cGridAdapter = new C_CircleGridAdapter(M_CircleDetailActivity.this, headerEntitiesList);
        gridview.setAdapter(cGridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                imageBrower(i, imageUrls);
            }
        });
    }


    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(M_CircleDetailActivity.this, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_circle_detail_like:
                gotoLike();
                break;
            case R.id.layout_circle_detail_comment:
                Comment_Dialog(view);
                break;
            case R.id.dialog_comment_send_send:
                gotoComment();
                break;
            case R.id.layout_circle_detail_delete:
              Delete_Dialog();
                break;
            case R.id.exit_yes:
                delete();
                exitDialog.dismiss();
                break;
            case R.id.exit_no:
                exitDialog.dismiss();
                break;
        }
    }

    MyHTTP http;

    private void gotoLike() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("object_id", id);
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        params.addQueryStringParameter("species", "micropost");
        if (http == null) http = new MyHTTP(M_CircleDetailActivity.this);
        http.baseRequest(Consts.addLikeApi, JSONHandler.JTYPE_ARTICLES_LIKE, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }

    private void gotoComment() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("micropost_id", id);
        params.addQueryStringParameter("member_id", DataCenter.getMember_id());
        params.addQueryStringParameter("content", edit_dialog_comment.getText().toString());
//        params.addQueryStringParameter("object", "");

        if (http == null) http = new MyHTTP(M_CircleDetailActivity.this);
        http.baseRequest(Consts.commentCircleApi, JSONHandler.JTYPE_ARTICLES_COMMENT, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }


    private void delete() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", id);
        if (http == null) http = new MyHTTP(M_CircleDetailActivity.this);
        http.baseRequest(Consts.deleteMywordApi, JSONHandler.JTYPE_DELETE_MYWORD, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }

    private void likeList() {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("micropost_id", id);
        if (http == null) http = new MyHTTP(M_CircleDetailActivity.this);
        http.baseRequest(Consts.circlelikeListApi, JSONHandler.JTYPE_CIRCLE_LIKE_LIST, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }

    private void commentList() {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("micropost_id", id);
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("limit", "8");
        params = KelaParams.generateSignParam("GET", Consts.circlecommentListApi, params);
        if (http == null) http = new MyHTTP(M_CircleDetailActivity.this);
        http.baseRequest(Consts.circlecommentListApi, JSONHandler.JTYPE_CIRCLE_ALL_COMMENT, HttpRequest.HttpMethod.GET,
                params, getHandler());
    }

    public void updateData() {
        super.updateData();
        if (jtype.equals(JSONHandler.JTYPE_ARTICLES_LIKE)) {
            ToastUtil.show(M_CircleDetailActivity.this, "已赞");
        } else if (jtype.equals(JSONHandler.JTYPE_DELETE_MYWORD)) {
            ToastUtil.show(M_CircleDetailActivity.this, "删除成功");
            Intent intent = new Intent(M_CircleDetailActivity.this, MainActivity.class);
            intent.putExtra("release", "123");
            startActivity(intent);
            finish();

        } else if (jtype.equals(JSONHandler.JTYPE_ARTICLES_COMMENT)) {
            ToastUtil.show(M_CircleDetailActivity.this, "评论成功");
            cPopwindow.dismiss();
            edit_dialog_comment.setText("");

        } else if (jtype.equals(JSONHandler.JTYPE_CIRCLE_LIKE_LIST)) {
            commentList();
            ArrayList<CircleLike> os = (ArrayList<CircleLike>) handlerBundler.getSerializable("circle_like_list");
            if (os.size() == 0) {
                return;
            }
            mdAdapter = new M_DetailLikeAdapter(M_CircleDetailActivity.this, os);
            gridview_like.setAdapter(mdAdapter);

        } else if (jtype.equals(JSONHandler.JTYPE_CIRCLE_ALL_COMMENT)) {
            int curTradesSize = mylist.size();
            ArrayList<CircleAllComment> os = (ArrayList<CircleAllComment>) handlerBundler.getSerializable("circle_all_comment");
            if (os.size() == 0) {
                ToastUtil.show(M_CircleDetailActivity.this, getString(R.string.no_more_data));
                return;
            }
            addTrades("bottom", os);//用于添加数据
            if (curTradesSize == 0) {
                mylist = os;
                allAdapter = new C_CircleCommentAdapter(M_CircleDetailActivity.this, mylist);
                listView.setAdapter(allAdapter);

            } else {

                allAdapter.notifyDataSetChanged();
            }
            page += 1;
        }

    }

    public void addTrades(String from, List<CircleAllComment> ess) {
        List<String> ids = new ArrayList<String>();
        for (CircleAllComment o : mylist)
            ids.add(o.getId());

        for (CircleAllComment e : ess) {
            if (!ids.contains(e.getId())) {
                int i = from.equals("top") ? 0 : mylist.size();
                mylist.add(i, e);
            }
        }
        if (allAdapter != null) {
            allAdapter.notifyDataSetChanged();
        }
    }

    private void Comment_Dialog(View v) {
        View pop_view = getLayoutInflater().inflate(R.layout.dialog_comment, null, false);
        cPopwindow = new PopupWindow(pop_view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        cPopwindow.setAnimationStyle(R.style.mystyle);
        cPopwindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        cPopwindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        ColorDrawable dw = new ColorDrawable(0xb01b1b1b);
        pop_view.setBackgroundDrawable(dw);
        pop_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                cPopwindow.dismiss();
                return true;
            }
        });

        edit_dialog_comment = (ContainsEmojiEditText) pop_view.findViewById(R.id.dialog_comment_edit);
        tv_dialog_send = (TextView) pop_view.findViewById(R.id.dialog_comment_send_send);
        tv_dialog_send.setOnClickListener(this);
        edit_dialog_comment.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    @Override
    public void onFooterRefresh(PullToRefresh123View view) {
        mPullToRefreshView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mPullToRefreshView.onFooterRefreshComplete();

                commentList();
                ToastUtil.show(M_CircleDetailActivity.this, "加载更多数据");
            }

        }, 2200);
    }
    private void Delete_Dialog() {
        exitDialog = new ExitDialog(M_CircleDetailActivity.this, R.style.MyDialogStyle, R.layout.dialog_exit);

        exitDialog.show();
        tv_dialog_title= (TextView) exitDialog.findViewById(dialog_exit_title);
        tv_dialog_title.setText(getString(R.string.if_delete));
        tv_yes = (TextView) exitDialog.findViewById(R.id.exit_yes);
        tv_no = (TextView) exitDialog.findViewById(R.id.exit_no);
        tv_yes.setOnClickListener(this);
        tv_no.setOnClickListener(this);

    }
}
