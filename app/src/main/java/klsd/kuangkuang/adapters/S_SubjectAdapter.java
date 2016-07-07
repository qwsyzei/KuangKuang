package klsd.kuangkuang.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import klsd.kuangkuang.R;
import klsd.kuangkuang.main.S_ArticleActivity;
import klsd.kuangkuang.models.Subject;

/**
 * 专题文章列表的adapter
 * Created by qiwei on 2016/7/6.
 */
public class S_SubjectAdapter extends ArrayAdapter<Subject> {

    private Context ctx;

    public S_SubjectAdapter(Context context, List<Subject> objects) {
        super(context, R.layout.item_s_subject, objects);
        this.ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Subject subject = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_s_subject, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_subject_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.item_subject_content);
            viewHolder.author = (TextView) convertView.findViewById(R.id.item_subject_author_name);
            viewHolder.looked = (TextView) convertView.findViewById(R.id.item_subject_looked);
            viewHolder.praise = (TextView) convertView.findViewById(R.id.item_subject_praise);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.item_subject_comment);
            viewHolder.im_background = (ImageView) convertView.findViewById(R.id.item_subject_background);
            viewHolder.im_head_pic = (ImageView) convertView.findViewById(R.id.item_subject_head_pic);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(subject.getTitle());
        viewHolder.content.setText(subject.getContent());
        viewHolder.author.setText(subject.getAuthor());
        viewHolder.looked.setText(subject.getLooked());
        viewHolder.praise.setText(subject.getPraise());
        viewHolder.comment.setText(subject.getComment());
        viewHolder.im_background.setImageBitmap(subject.getBackground());
        viewHolder.im_head_pic.setImageBitmap(subject.getHead_pic());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ctx, S_ArticleActivity.class);
                ctx.startActivity(intent);
            }
        });

        return convertView;
    }

    public final class ViewHolder {
        public TextView title, content, author, looked, praise, comment;
        public ImageView im_background,im_head_pic;
    }
}