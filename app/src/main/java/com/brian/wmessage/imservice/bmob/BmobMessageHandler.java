package com.brian.wmessage.imservice.bmob;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.brian.common.tools.Env;
import com.brian.common.tools.GsonHelper;
import com.brian.common.utils.EncryptUtil;
import com.brian.common.utils.LogUtil;
import com.brian.wmessage.MainActivity;
import com.brian.wmessage.R;
import com.brian.wmessage.entity.IMMessage;
import com.brian.wmessage.imservice.MessageDispatcher;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;

/**
 * Bmob消息接收处理
 * @author huamm
 */
public class BmobMessageHandler extends BmobIMMessageHandler {


    @Override
    public void onMessageReceive(MessageEvent event) {
        super.onMessageReceive(event);
        //当接收到服务器发来的消息时，此方法被调用
        executeMessage(event);
    }

    @Override
    public void onOfflineReceive(OfflineMessageEvent event) {
        super.onOfflineReceive(event);
        //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
        Map<String, List<MessageEvent>> map = event.getEventMap();
        LogUtil.i("有" + map.size() + "个用户发来离线消息");
        //挨个检测下离线消息所属的用户的信息是否需要更新
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list = entry.getValue();
            int size = list.size();
            LogUtil.i("用户" + entry.getKey() + "发来" + size + "条消息");
            for (int i = 0; i < size; i++) {
                //处理每条消息
                executeMessage(list.get(i));
            }
        }
    }

    /**
     * 处理消息
     */
    private void executeMessage(final MessageEvent event) {
        LogUtil.d("event=" + GsonHelper.toJson(event));
        BmobIMMessage msg = event.getMessage();
//        msg.setContent(EncryptUtil.decryptAES(msg.getContent(), msg.getConversationId()));
        if (BmobIMMessageType.getMessageTypeValue(msg.getMsgType()) == 0) {
            //自定义消息类型：0
            processCustomMessage(IMMessage.convert(msg), event.getFromUserInfo());
        } else {
            //SDK内部内部支持的消息类型
            processSDKMessage(msg, event);
        }
    }


    /**
     * 处理SDK支持的消息
     */
    private void processSDKMessage(BmobIMMessage msg, MessageEvent event) {
        Context context = Env.getContext();
        if (BmobNotificationManager.getInstance(context).isShowNotification()) {
            //如果需要显示通知栏，SDK提供以下两种显示方式：
            Intent pendingIntent = new Intent(context, MainActivity.class);
            pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            BmobNotificationManager.getInstance(context).showNotification(event, pendingIntent);

            BmobIMUserInfo info = event.getFromUserInfo();
            //这里可以是应用图标，也可以将聊天头像转成bitmap
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            BmobNotificationManager.getInstance(context).showNotification(largeIcon,
                    info.getName(), msg.getContent(), "您有一条新消息", pendingIntent);
        } else {
            //直接发送消息事件
            processCustomMessage(IMMessage.convert(msg), event.getFromUserInfo());
        }
    }


    /**
     * 处理自定义消息类型
     */
    private void processCustomMessage(IMMessage msg, BmobIMUserInfo info) {
        MessageDispatcher.getInstance().dispatchMessage(msg);
    }
}
