package yksg.kuangkuang.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * <pre><font color ="red">
 * 创建这个自定义GridView
 * 用来解决ScroolView嵌套
 * GridView不能够正常显示的问题</font>
 * </pre>
 *
 */
public class SelfGridView extends GridView {
    public SelfGridView(Context context) {
        super(context);
    }
    public SelfGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    /**
     * 重写该方法，达到使GridView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }}
