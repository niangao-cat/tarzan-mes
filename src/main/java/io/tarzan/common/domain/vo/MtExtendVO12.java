package io.tarzan.common.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Description:
 * @Date: 2020/1/2 15:24
 * @Author: ${yiyang.xie}
 */
public class MtExtendVO12 implements Serializable {
    private static final long serialVersionUID = -4399242949969065300L;

    @ApiModelProperty("最新历史ID")
    private String latestHisId;

    @ApiModelProperty("事件Id")
    private String eventId;

    public String getLatestHisId() {
        return latestHisId;
    }

    public void setLatestHisId(String latestHisId) {
        this.latestHisId = latestHisId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}

