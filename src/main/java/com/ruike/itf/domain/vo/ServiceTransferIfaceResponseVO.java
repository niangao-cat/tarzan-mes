package com.ruike.itf.domain.vo;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 售后大仓回调 响应对象
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/1 16:28
 */
@Data
public class ServiceTransferIfaceResponseVO {
    @JsonProperty("IFACEID")
    private String interfaceId;
    @JsonProperty("ZFLAG")
    private String processStatus;
    @JsonProperty("ZMESSAGE")
    private String processMessage;
    @JsonProperty("MAT_DOC")
    private String document;

    public ServiceTransferIfaceResponseVO(Map<String, Object> fieldMap) {
        this.interfaceId = Optional.ofNullable(fieldMap.get("IFACEID")).orElse(new Object()).toString();
        this.processStatus = Optional.ofNullable(fieldMap.get("ZFLAG")).orElse(new Object()).toString();
        this.processMessage = Optional.ofNullable(fieldMap.get("ZMESSAGE")).orElse(new Object()).toString();
        this.document = Optional.ofNullable(fieldMap.get("MAT_DOC")).orElse(new Object()).toString();
    }
}
