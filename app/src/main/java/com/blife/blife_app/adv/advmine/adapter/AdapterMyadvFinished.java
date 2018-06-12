package com.blife.blife_app.adv.advmine.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blife.blife_app.R;
import com.blife.blife_app.adv.advmine.activity.ActivityMyadvFinishedDetail;
import com.blife.blife_app.adv.advmine.activity.ActivityRefundSchedule;
import com.blife.blife_app.adv.advmine.bean.BeanMyadv;
import com.blife.blife_app.adv.advmine.interfacer.DialogListener;
import com.blife.blife_app.tools.BitmapHelp;
import com.blife.blife_app.tools.DateUtils;
import com.blife.blife_app.tools.JsonObjUItils;
import com.blife.blife_app.tools.data.DataManager;
import com.blife.blife_app.tools.http.Constants;
import com.blife.blife_app.tools.view.ConfirmDialog;
import com.blife.blife_app.utils.net.NetWorkUtil;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.StringUtils;
import com.blife.blife_app.utils.util.ToastUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Somebody on 2016/8/29.
 */
public class AdapterMyadvFinished extends RecyclerView.Adapter {

    private ConfirmDialog confirmDialog;
    private DataManager dataManager;
    private String ACCESS_TOKEN;
    private Context context;
    private List<BeanMyadv> list;
    private Notify notify;

    public AdapterMyadvFinished(Context context, List<BeanMyadv> list, String ACCESS_TOKEN) {
        this.context = context;
        this.list = list;
        this.ACCESS_TOKEN = ACCESS_TOKEN;
        confirmDialog = new ConfirmDialog(context, R.layout.dialog_confirm, "您确定要申请余额退款吗？");
        confirmDialog.setViewDismiss(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyadvViewHolder(LayoutInflater.from(context).inflate(R.layout.item_myadv_finished, null));
    }

    public interface Notify {
        public void setNotify(String adv_id, int position);
    }

    public void setNotifyDataSetChanged(Notify notify) {
        this.notify = notify;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final BeanMyadv beanMyadv = list.get(position);

        final MyadvViewHolder myadvViewHolder = (MyadvViewHolder) holder;
        if (list.get(position).getContent() != null) {
            List<String> images = list.get(position).getContent().getImages();
            if (images != null && images.size() > 0) {
                BitmapUtils bitmapHelp = BitmapHelp.getBitmapUtils(context);
                bitmapHelp.display(myadvViewHolder.iv_advdimages, images.get(0));
            }
        }
        myadvViewHolder.tv_status_refund.setVisibility(View.GONE);
        if (!StringUtils.isImpty(list.get(position).getTitle())) {
            myadvViewHolder.tv_title.setText(list.get(position).getTitle());
        }
        if (!StringUtils.isImpty(list.get(position).getPub_name())) {
            myadvViewHolder.tv_pubname.setText(list.get(position).getPub_name());
        }
        if (!StringUtils.isImpty(list.get(position).getCreate_time())) {
            myadvViewHolder.tv_creattime.setText(DateUtils.getTime(list.get(position).getCreate_time()));
        }
        if (!StringUtils.isImpty(beanMyadv.getStatus())) {
            int status = Integer.parseInt(beanMyadv.getStatus());
            int refund_status = Integer.parseInt(beanMyadv.getRefund_status());
            if (status == 12) {
                if (refund_status == 0) {
                    myadvViewHolder.tv_status.setText("已完成");
                    myadvViewHolder.tv_status_handle.setVisibility(View.VISIBLE);
                    myadvViewHolder.tv_status_refund.setVisibility(View.GONE);
                } else if (refund_status == 1) {
                    myadvViewHolder.tv_status.setText("退款中");
                    myadvViewHolder.tv_status_handle.setVisibility(View.GONE);
                    myadvViewHolder.tv_status_refund.setVisibility(View.VISIBLE);
                } else if (refund_status == 2) {
                    myadvViewHolder.tv_status.setText("退款中");
                    myadvViewHolder.tv_status_handle.setVisibility(View.GONE);
                    myadvViewHolder.tv_status_refund.setVisibility(View.VISIBLE);
                } else if (refund_status == 8) {
                    myadvViewHolder.tv_status_handle.setVisibility(View.GONE);
                    myadvViewHolder.tv_status_refund.setVisibility(View.GONE);
                    myadvViewHolder.tv_status.setText("已完成");
                } else if (refund_status == 9) {
                    myadvViewHolder.tv_status_handle.setVisibility(View.GONE);
                    myadvViewHolder.tv_status_refund.setVisibility(View.GONE);
                    myadvViewHolder.tv_status.setText("退款完成");
                } else {
                    myadvViewHolder.tv_status_handle.setVisibility(View.GONE);
                    myadvViewHolder.tv_status_refund.setVisibility(View.GONE);
                    myadvViewHolder.tv_status.setText("状态请求失败");
                }
            }
            //退款进度
            myadvViewHolder.tv_status_refund.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.ADV_ID, beanMyadv.getAdv_id());
                    intent.putExtras(bundle);
                    intent.setClass(context, ActivityRefundSchedule.class);
                    context.startActivity(intent);
                }
            });
            //点击结算
            myadvViewHolder.tv_status_handle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(beanMyadv.getBonus_total_num()!=null&&beanMyadv.getBonus_accepted_num()!=null){
                        if(Long.parseLong(beanMyadv.getBonus_total_num()) == Long.parseLong(beanMyadv.getBonus_accepted_num())){
                            confirmDialog.setTitle("您确定要申请结算吗?");
                        }else{
                            confirmDialog.setTitle("您确定要申请余额退款吗？");
                        }
                    }
                    confirmDialog.show(v);
                    confirmDialog.setDialogListener(new DialogListener() {
                        @Override
                        public void dialogConfirmListener() {
                            if (!NetWorkUtil.isNetwork(context)) {
                                JsonObjUItils.showFailedDialog(R.string.toast_network_Available, myadvViewHolder.tv_status_handle, context);
                                confirmDialog.dismiss();
                                return;
                            } else if (beanMyadv.getAdv_id() != null) {
                                notify.setNotify(beanMyadv.getAdv_id(), position);
                            }
                            confirmDialog.dismiss();
                        }

                        @Override
                        public void dialogCacleListener() {
                            confirmDialog.dismiss();
                        }
                    });
                }
            });
        }
        if (beanMyadv.getBonus_total_num() != null) {
            if (beanMyadv.getBonus_accepted_num() != null) {
                if (Long.parseLong(beanMyadv.getBonus_total_num()) == Long.parseLong(beanMyadv.getBonus_accepted_num())) {
                    myadvViewHolder.tv_status_handle.setText("确认完成");
                } else {
                    myadvViewHolder.tv_status_handle.setText(context.getString(R.string.myadv_request_refund));
                }
                myadvViewHolder.tv_accepted_number.setText(beanMyadv.getBonus_accepted_num() + "/" + beanMyadv.getBonus_total_num());
            } else {
                myadvViewHolder.tv_accepted_number.setText(0 + "/" + beanMyadv.getBonus_total_num());
            }
        } else {
            myadvViewHolder.tv_accepted_number.setText("0");
        }

        myadvViewHolder.lin_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCache = false;
                if (!StringUtils.isImpty(beanMyadv.getStatus())) {
                    int status = Integer.parseInt(beanMyadv.getStatus());
                    int refund_status = Integer.parseInt(beanMyadv.getRefund_status());
                    if (status == 12) {
                        if (refund_status == 9) {
                            isCache = true;
                        } else {
                            isCache = false;
                        }
                    }
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("adv_id", beanMyadv.getAdv_id());
                bundle.putBoolean("isCache", isCache);
                intent.putExtras(bundle);
                intent.setClass(context, ActivityMyadvFinishedDetail.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null && list.size() > 0)
            return list.size();
        else {
            return 0;
        }
    }

    class MyadvViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_pubname, tv_creattime, tv_accepted_number, tv_status, tv_status_handle, tv_status_refund;
        private ImageView iv_advdimages;
        private LinearLayout lin_list;

        public MyadvViewHolder(View itemView) {
            super(itemView);
            iv_advdimages = (ImageView) itemView.findViewById(R.id.iv_advdimages);
            lin_list = (LinearLayout) itemView.findViewById(R.id.lin_list);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_pubname = (TextView) itemView.findViewById(R.id.tv_pubname);
            tv_creattime = (TextView) itemView.findViewById(R.id.tv_creattime);
            tv_accepted_number = (TextView) itemView.findViewById(R.id.tv_accepted_number);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_status_handle = (TextView) itemView.findViewById(R.id.tv_status_handle);
            tv_status_refund = (TextView) itemView.findViewById(R.id.tv_status_refund);
        }
    }
}
