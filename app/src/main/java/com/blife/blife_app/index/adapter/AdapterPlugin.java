package com.blife.blife_app.index.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.index.bean.BeanPlugin;
import com.blife.blife_app.tools.BitmapHelp;
import com.blife.blife_app.tools.view.RoundImageView;
import com.blife.blife_app.utils.net.Imageloader.ImageLoader;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/*首页分类小模块GridView的适配器*/
public class AdapterPlugin extends BaseAdapter {

    private List<BeanPlugin> list;
    private LayoutInflater layoutInflater;
    private Context context;

    public AdapterPlugin() {
    }

    public AdapterPlugin(List<BeanPlugin> list,
                         Context context) {
        this.list = list;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        else
            return 0;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.item_fragment_home_gridview, viewGroup, false);
            vHolder = new ViewHolder(convertView);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }

        BeanPlugin hpi = list.get(position);
        if (!TextUtils.isEmpty(hpi.getLogo())) {
            ImageLoader.getInstance().loadImage(hpi.getLogo(), vHolder.iv_icon, true);
        }
        vHolder.tv_title.setText(hpi.getName());
        return convertView;
    }

    class ViewHolder {
        private TextView tv_title;
        private ImageView iv_icon;

        public ViewHolder(View convertView) {
            tv_title = (TextView) convertView
                    .findViewById(R.id.tv_hpigvitem_title);
            iv_icon = (ImageView) convertView
                    .findViewById(R.id.iv_hpigvitem_icon);
        }
    }
}
