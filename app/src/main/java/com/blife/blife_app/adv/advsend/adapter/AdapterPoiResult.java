package com.blife.blife_app.adv.advsend.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advsend.bean.BeanPoiResult;

import java.util.List;

/**
 * Created by w on 2016/8/31.
 */
public class AdapterPoiResult extends RecyclerView.Adapter {

    public static final int TYPE_CURRENT = 0;
    public static final int TYPE_SEARCH = 1;

    private Context context;
    private List<BeanPoiResult> list;

    private InterfacePoiResult interfacePoiResult;

    public AdapterPoiResult(Context context, List<BeanPoiResult> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new POIResultViewHolder(LayoutInflater.from(context).inflate(R.layout.item_poi_result, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        POIResultViewHolder viewHolder = ((POIResultViewHolder) holder);
        BeanPoiResult bean = list.get(position);
        viewHolder.tv_resultname.setText(bean.getAddress());
        if (bean.getTYPE() == TYPE_CURRENT) {
            viewHolder.lin_result_bottom.setVisibility(View.GONE);
            viewHolder.iv_resulticon.setImageResource(R.mipmap.uploadadv_address_current);
        } else {
            viewHolder.lin_result_bottom.setVisibility(View.VISIBLE);
            viewHolder.iv_resulticon.setImageResource(R.mipmap.uploadadv_address_poi);
            viewHolder.tv_resultinfo.setText(bean.getAddressdetail());
        }
        viewHolder.frameLayout_addaddress.setOnClickListener(new onItemClick(position));
        viewHolder.lin_result_left.setOnClickListener(new onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setInterfacePoiResult(InterfacePoiResult interfacePoiResult) {
        this.interfacePoiResult = interfacePoiResult;
    }


    class onItemClick implements View.OnClickListener {
        private int pos;

        public onItemClick(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.frameLayout_addaddress:
                    if (interfacePoiResult != null) {
                        interfacePoiResult.onItemAdd(pos);
                    }
                    break;
                case R.id.lin_result_left:
                    if (interfacePoiResult != null) {
                        interfacePoiResult.onItemClick(pos);
                    }
                    break;
            }
        }
    }

    class POIResultViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout lin_result_left, lin_result_bottom;
        private FrameLayout frameLayout_addaddress;

        private ImageView iv_resulticon;
        private TextView tv_resultname, tv_resultinfo;

        public POIResultViewHolder(View itemView) {
            super(itemView);
            lin_result_left = (LinearLayout) itemView.findViewById(R.id.lin_result_left);
            lin_result_bottom = (LinearLayout) itemView.findViewById(R.id.lin_result_bottom);
            frameLayout_addaddress = (FrameLayout) itemView.findViewById(R.id.frameLayout_addaddress);
            iv_resulticon = (ImageView) itemView.findViewById(R.id.iv_resulticon);
            tv_resultname = (TextView) itemView.findViewById(R.id.tv_resultname);
            tv_resultinfo = (TextView) itemView.findViewById(R.id.tv_resultinfo);
        }
    }
}
