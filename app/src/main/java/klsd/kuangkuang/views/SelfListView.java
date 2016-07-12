package klsd.kuangkuang.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义listview用于适应ScrollView
 * Created by qiwei on 2016/7/12.
 */
public class SelfListView extends ListView{
    public SelfListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelfListView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
