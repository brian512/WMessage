package com.brian.wmessage.entity;

import java.io.Serializable;

public class RecentListItemData implements Serializable {

    public String nickName;
    public String lastMsg;
    public String headUrl;
    public String time;

    public boolean isDraft = false;
}
