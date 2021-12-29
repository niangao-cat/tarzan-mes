package com.ruike.itf.app.weixin.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskCard {
    private String title = "异常消息通知";//标题，不超过128个字节，超过会自动截断（支持id转译）
    private String description;//描述，不超过512个字节，超过会自动截断（支持id转译）
    private String url = "https://baidu.com";//点击后跳转的链接。最长2048字节，请确保包含了协议头(http/https)
    private String task_id = String.valueOf(UUID.randomUUID());//任务id，同一个应用发送的任务卡片消息的任务id不能重复，只能由数字、字母和“_-@”组成，最长支持128字节
    private List<TaskCardBnt> btn;//按钮列表，按钮个数为1~2个。

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public List<TaskCardBnt> getBtn() {
        if(btn == null){
            List<TaskCardBnt> bnts = new ArrayList<>();
            bnts.add(new TaskCardBnt("1","同意","已同意"));
            bnts.add(new TaskCardBnt("0","驳回","已驳回"));
            this.btn = bnts;
        }
        return btn;
    }

    public void setBtn(List<TaskCardBnt> btn) {

        this.btn = btn;
    }
}
