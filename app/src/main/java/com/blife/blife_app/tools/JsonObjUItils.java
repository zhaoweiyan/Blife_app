package com.blife.blife_app.tools;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.blife.blife_app.R;
import com.blife.blife_app.tools.pulltorefresh.pulltorefreshview.PullToRefreshRecyclerView;
import com.blife.blife_app.tools.view.MessageDialog;
import com.blife.blife_app.utils.net.NetWorkUtil;
import com.blife.blife_app.utils.util.GsonUtil;
import com.blife.blife_app.utils.util.LogUtils;
import com.blife.blife_app.utils.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Somebody on 2016/8/23.
 */
public class JsonObjUItils {

    private static MessageDialog messageDialog;

    //是否成功
    public static Boolean getJsonObject(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.has("message")) {
            if (jsonObject.getString("message").equals("OK") || jsonObject.getString("message").equals("SUCCESS")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static void showFailedDialog(String message, View view, Context context) {
        if (messageDialog == null)
            messageDialog = new MessageDialog(context, R.layout.dialog_hintmsg);
        messageDialog.setViewDismiss(true);
        messageDialog.setTitle(R.string.dialog_phone_failed_title);
        messageDialog.setMessage(message);
        if (messageDialog.isShowing()) {
            messageDialog.dismiss();
            messageDialog.show(view);
        } else {
            messageDialog.show(view);
        }
    }

    public static void showFailedDialog(int message, View view, Context context) {
        if (messageDialog == null)
            messageDialog = new MessageDialog(context, R.layout.dialog_hintmsg);
        messageDialog.setViewDismiss(true);
        messageDialog.setTitle(R.string.dialog_phone_failed_title);
        messageDialog.setMessage(message);
        if (messageDialog.isShowing()) {
            messageDialog.dismiss();
            messageDialog.show(view);
        } else {
            messageDialog.show(view);
        }
    }

    //失败code
    public static void getJsonCode(String json, View view, Context context) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            if (jsonObject.has("message")) {
                if (!(jsonObject.getString("message").equals("OK")) && !(jsonObject.getString("message").equals("SUCCESS"))) {
                    if (jsonObject.has("detail") && jsonObject.has("code")) {
                        String code = jsonObject.getString("code");
                        String detail = jsonObject.getString("detail");
                        if (code.equals("60000001")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000002")) {
                            showFailedDialog("手机号未注册", view, context);
                        } else if (code.equals("60000003")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000004")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000005")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000006")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000007")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000008")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000009")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000010")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000011")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000012")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000013")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000014")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000015")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000016")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000017")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000018")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000019")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000020")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000021")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000022")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000023")) {
                            showFailedDialog("手机号输入有误", view, context);
                        } else if (code.equals("60000024")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000025")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000026")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000027")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000028")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000029")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000030")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000031")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000032")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000033")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("60000034")) {
                            showFailedDialog("账号密码不匹配", view, context);
                        } else if (code.equals("70000001")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("70000002")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("70000003")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("70000004")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("80000001")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("80000002")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("80000003")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("80000004")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("90000001")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("90000002")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("90000003")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("11000001")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("11000002")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("11000003")) {
                            showFailedDialog("验证码输入有误", view, context);
                        } else if (code.equals("11000004")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("11000005")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("11000006")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("12000001")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("12000002")) {
                            showFailedDialog(R.string.login_file, view, context);
                        } else if (code.equals("13000001")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000002")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000003")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000004")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000005")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000006")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000007")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000008")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000009")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000010")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000011")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000012")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000013")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000014")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000015")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000016")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000017")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000018")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000019")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000020")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000021")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000022")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000023")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000024")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000025")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000026")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000027")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000028")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000029")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("13000030")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("14000001")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("15000001")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("15000002")) {
                            showFailedDialog(detail, view, context);
                        } else if (code.equals("15000003")) {
                            showFailedDialog(detail, view, context);
                        }
                    }
                }
            } else {
                showFailedDialog(json, view, context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //失败code,转为区分除了验证码失败。账号密码不匹配。手机号未注册，手机号输入有误四种失败原因之外的（登录失败，请重新登录）
    public static void getJsonCode(String json, View view, Context context, int message) {
        if (json == null) {
            return;
        }
        if (json.trim().equals("timeout")) {
            json = "{\"code\":\"44444444\",\"message\":\"ERROR\",\"detail\":\"请检查网络设置\"}";
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            if (jsonObject.has("message")) {
                if (!(jsonObject.getString("message").equals("OK")) && !(jsonObject.getString("message").equals("SUCCESS"))) {
                    if (jsonObject.has("detail") && jsonObject.has("code")) {
                        String code = jsonObject.getString("code");
                        if (code.equals("60000002")) {
                            showFailedDialog("手机号未注册", view, context);
                        } else if (code.equals("60000023")) {
                            showFailedDialog("手机号输入有误", view, context);
                        } else if (code.equals("60000036")) {
                            showFailedDialog("手机号已注册，如需修改密码,\n请打开\"我的\"→\"个人中心\"去完成重置密码", view, context);
                        } else if (code.equals("60000034")) {
                            showFailedDialog("账号密码不匹配", view, context);
                        } else if (code.equals("11000003")) {
                            showFailedDialog("验证码输入有误", view, context);
                        } else if (code.equals("44444444")) {
                            showFailedDialog("请检查网络设置", view, context);
                        } else if (code.equals("6000")) {
                            showFailedDialog(jsonObject.getString("detail"), view, context);
                        } else {
                            showFailedDialog(message, view, context);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //失败信息
    public static String getERRORJsonDetail(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("message")) {
                if (!(jsonObject.getString("message").equals("OK")) && !(jsonObject.getString("message").equals("SUCCESS"))) {
                    if (jsonObject.has("detail")) {
                        return jsonObject.getString("detail");
                    }
                }
            } else {
                return "请检查网络设置";
            }
        } catch (JSONException e) {
            return "";
        }

        return "请检查网络设置";
    }

    //失败信息
    public static String getERRORJsonCode(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("message")) {
                if (!(jsonObject.getString("message").equals("OK")) && !(jsonObject.getString("message").equals("SUCCESS"))) {
                    if (jsonObject.has("code")) {
                        return jsonObject.getString("code");
                    }
                }
            }
        } catch (JSONException e) {
            return "-1";
        }

        return "-1";
    }

    //失败信息
    public static String getERRORJsonMessage(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("message")) {
                if (!(jsonObject.getString("message").equals("OK")) && !(jsonObject.getString("message").equals("SUCCESS"))) {
                    if (jsonObject.has("message")) {
                        return jsonObject.getString("message");
                    }
                }
            }
        } catch (JSONException e) {
            return "";
        }

        return "";
    }

    public static Object fromJson(String json, Class<?> cls) {
        try {
            if (!TextUtils.isEmpty(json)) {
                GsonUtil gson = GsonUtil.getInstance();
                String jsonData = gson.getData(json, "data");
                if (!TextUtils.isEmpty(jsonData)) {
                    return GsonUtil.getInstance().FromJson(jsonData, cls);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object();
    }

    public static String getJsonData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("data")) {
                return jsonObject.getString("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void isEmptyJson(Context context, PullToRefreshRecyclerView refresh_recycler) {
        if (!NetWorkUtil.isNetwork(context)) {
            ToastUtils.showShort(context, "请检查网络");
        } else {
            ToastUtils.showShort(context, "已加载全部");
        }
        refresh_recycler.Finish();
    }

    public static void isEmptyJson(Context context) {
        if (!NetWorkUtil.isNetwork(context)) {
            ToastUtils.showShort(context, "请检查网络");
        } else {
//            ToastUtils.showShort(context, "已加载全部");
        }
    }
}
