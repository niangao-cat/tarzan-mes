package com.ruike.itf.app.weixin.vo;

public class TaskCardP {
    private String touser;// 接收人，@all发送全部，指定用户写企业员工ID，多个用|隔开
    private String toparty;//部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
    private String totag;//标签ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
    private String msgtype = "taskcard";//消息类型，此时固定为：taskcard
    private String agentid;//企业应用的id，整型。企业内部开发，可在应用的设置页面查看；第三方服务商，可通过接口 获取企业授权信息 获取该参数值
    private TaskCard taskcard = new TaskCard();
    private String enable_id_trans = "0";//表示是否开启id转译，0表示否，1表示是，默认0
    private String enable_duplicate_check = "0";//表示是否开启重复消息检查，0表示否，1表示是，默认0
    private String duplicate_check_interval = "1800";//表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时


    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public TaskCard getTaskcard() {
        return taskcard;
    }

    public void setTaskcard(String content) {
        TaskCard card = new TaskCard();
        card.setDescription(content);
        this.taskcard = card;
    }

    public String getEnable_id_trans() {
        return enable_id_trans;
    }

    public void setEnable_id_trans(String enable_id_trans) {
        this.enable_id_trans = enable_id_trans;
    }

    public String getEnable_duplicate_check() {
        return enable_duplicate_check;
    }

    public void setEnable_duplicate_check(String enable_duplicate_check) {
        this.enable_duplicate_check = enable_duplicate_check;
    }

    public String getDuplicate_check_interval() {
        return duplicate_check_interval;
    }

    public void setDuplicate_check_interval(String duplicate_check_interval) {
        this.duplicate_check_interval = duplicate_check_interval;
    }
}
