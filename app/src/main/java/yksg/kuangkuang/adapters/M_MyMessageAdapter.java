package yksg.kuangkuang.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import yksg.kuangkuang.R;
import yksg.kuangkuang.models.MyMessage;

/**
 * 我的消息listView的adapter
 */
public class M_MyMessageAdapter extends ArrayAdapter<MyMessage> {

    private Context ctx;

    public M_MyMessageAdapter(Context context, List<MyMessage> objects) {
        super(context, R.layout.item_my_message, objects);
        this.ctx = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyMessage mm = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_my_message, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_my_message_title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.item_my_message_time);
            viewHolder.content = (TextView) convertView.findViewById(R.id.item_my_message_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(mm.getTitle());
        viewHolder.time.setText(mm.getTime());
        viewHolder.content.setText(mm.getContent());
        return convertView;
    }


    public final class ViewHolder {
        public TextView title, time, content;
    }
}