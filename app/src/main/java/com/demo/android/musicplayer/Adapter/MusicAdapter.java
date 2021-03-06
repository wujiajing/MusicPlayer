package com.demo.android.musicplayer.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.android.musicplayer.Base.music;
import com.demo.android.musicplayer.MainActivity;
import com.demo.android.musicplayer.R;
import com.demo.android.musicplayer.Utils.MusicUtil;

import java.util.List;

import static com.demo.android.musicplayer.R.layout.list_item;

/**
 * Created by kk on 2017/10/29.
 */

public class MusicAdapter extends BaseAdapter {
    private Context context;
    private List<music> list;

    public MusicAdapter(Context context, List<music> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            //引入布局
            view = View.inflate(context, list_item,null);
            //实例化对象
            holder.song = (TextView) view.findViewById(R.id.song);
            holder.singer = (TextView) view.findViewById(R.id.singer);
        //    holder.duration = (TextView) view.findViewById(R.id.time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //给控件赋值
        holder.song.setText(list.get(i).song);
        holder.singer.setText(list.get(i).singer);
    /*   //记住：时间需要转换一下
        int duration = list.get(i).duration;
        String time = MusicUtil.formatTime(duration);
        holder.duration.setText(time);
*/
        return view;
    }

    private class ViewHolder {
        TextView song;//歌曲名
        TextView singer;//歌手
  //      TextView duration;//时长
    }

}
