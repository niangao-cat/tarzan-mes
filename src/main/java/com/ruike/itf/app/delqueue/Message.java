package com.ruike.itf.app.delqueue;


import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 消息体定义 实现Delayed接口就是实现两个方法即compareTo 和 getDelay最重要的就是getDelay方法，这个方法用来判断是否到期…… */
public class Message<T> implements Delayed {
    private Integer index;
    private T body; // 消息内容
    private long excuteTime;// 延迟时长，这个是必须的属性因为要按照这个判断延时时长。

    public Integer getIndex() {
        return index;
    }

    public T getBody() {
        return body;
    }

    public long getExcuteTime() {
        return excuteTime;
    }

    public Message(Integer index, T body, long delayTime) {
        this.index = index;
        this.body = body;
        this.excuteTime = TimeUnit.NANOSECONDS.convert(delayTime, TimeUnit.MILLISECONDS) + System.nanoTime();
    }

    // 自定义实现比较方法返回 1 0 -1三个参数
    @Override
    public int compareTo(Delayed delayed) {
        Message msg = (Message) delayed;
        return Integer.valueOf(this.index) > Integer.valueOf(msg.index) ? 1
                : (Integer.valueOf(this.index) < Integer.valueOf(msg.index) ? -1 : 0);
    }

    // 延迟任务是否到时就是按照这个方法判断如果返回的是负数则说明到期否则还没到期
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.excuteTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }
}