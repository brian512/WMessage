WMessage（微聊）是一款简单的IM即时聊天应用，https://fir.im/wmessage

![https://fir.im/wmessage](art/fir_im_code_image.png)

--------------
###项目介绍

 - 由于没有服务器，暂时使用的[Bmob](https://www.bmob.cn/)的IM数据服务


####已实现的功能：
 - 帐号注册，登录，退出
 - 聊天列表
 - 用户列表
 - 文本发送，接收
 - 新消息提醒


####代码结构
```
WMessage
    |——app      // 项目主体
    |——lib_base // 基础工具库

app
 |——account         // 帐号相关
 |——chat            // 聊天相关
 |——contact         // 联系人相关
 |——conversations   // 会话相关
 |——entity          // 实体类
 |——imservice       // IM服务
         |——IIMServiceStateListener   // IM连接状态回调接口
         |——IMServiceManager          // IM连接管理
         |——MessageDispatcher         // 推送消息订阅分发
         |——bmob/   // Bmob相关
                |——BmobHelper // Bmob接口调用
                |——BmobMessageHandler // 接收Bmob服务消息
```



---
####TODO LIST

 功能：
 - 更多消息类型支持，图片，语音，视频，网页等
 - 用户搜索
 - 查看用户资料
 - 好友关系
 - 群聊
 - 实时音视频聊天

 技术：
 - 数据本地缓存
 - 聊天消息加密

架构：
 - 用MVP结构替换当前的MVC结构
当前业务较为简单，先采用MVC结构；
后续每个模块可细分package，也可以独立moudle

--------

####遇到的问题：
 - Bmob聊天数据不完整
```
{
    "bmobIMConversation":
    {
        "conversationIcon":"",
        "conversationId":"0ad9727ce7",
        "conversationTitle":"0ad9727ce7",
        "conversationType":1,
        "id":1,
        "updateTime":1528783700570
    },
    "fromId":"0ad9727ce7",
    "toId":"842e3cbb16",
}
```
会话icon数据经常为空、会话title为fromId，需要根据conversationId（与fromId同）填充数据

 - 接口异常类型没有区分
例如，登录错误时，并没有区分是用户不存在 还是密码错误，异或是其他错误