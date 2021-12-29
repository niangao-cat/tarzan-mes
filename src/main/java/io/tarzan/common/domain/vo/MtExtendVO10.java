package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author peng.yuan
 * @ClassName MtExtendVO10
 * @description
 * @date 2019年11月12日 11:19
 */
public class MtExtendVO10 implements Serializable {
    private static final long serialVersionUID = -8763364967359914670L;

    @ApiModelProperty(value = "主键id")
    private String keyId;
    @ApiModelProperty(value = "事件id")
    private String eventId;
    @ApiModelProperty(value = "扩展属性")
    private List<MtExtendVO5> attrs = new ArrayList<>();

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<MtExtendVO5> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<MtExtendVO5> attrs) {
        this.attrs = attrs;
    }
}
