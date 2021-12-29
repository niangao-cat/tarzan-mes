package com.ruike.itf.app.weixin.vo;

public class TaskCardBnt {

    private String key;//按钮key值，用户点击后，会产生任务卡片回调事件，回调事件会带上该key值，只能由数字、字母和“_-@”组成，最长支持128字节
    private String name;//按钮名称
    private String replace_name;//点击按钮后显示的名称，默认为“已处理”
    private String color;//按钮字体颜色，可选“red”或者“blue”,默认为“blue”
    private boolean is_bold;

    public TaskCardBnt(String key, String name, String replace_name) {
        this.key = key;
        this.name = name;
        this.replace_name = replace_name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReplace_name() {
        return replace_name;
    }

    public void setReplace_name(String replace_name) {
        this.replace_name = replace_name;
    }

    public String getColor() {
        if(color == "" | color == null){
            color = "blue";
        }
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isIs_bold() {
        if(!is_bold){
            is_bold = false;
        }
        return is_bold;
    }

    public void setIs_bold(boolean is_bold) {
        this.is_bold = is_bold;
    }
}
