package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 事件
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 19:37
 */
@Data
public class WmsEventVO {
    @ApiModelProperty("事件类型")
    private String requestTypeCode;
    @ApiModelProperty("事件")
    private String eventId;
    @ApiModelProperty("事件请求")
    private String eventRequestId;

    public WmsEventVO() {
    }

    private WmsEventVO(Builder builder) {
        setRequestTypeCode(builder.requestTypeCode);
        setEventId(builder.eventId);
        setEventRequestId(builder.eventRequestId);
    }

    public static final class Builder {
        private String requestTypeCode;
        private String eventId;
        private String eventRequestId;

        public Builder() {
        }

        public Builder requestTypeCode(String val) {
            requestTypeCode = val;
            return this;
        }

        public Builder eventId(String val) {
            eventId = val;
            return this;
        }

        public Builder eventRequestId(String val) {
            eventRequestId = val;
            return this;
        }

        public WmsEventVO build() {
            return new WmsEventVO(this);
        }
    }
}
