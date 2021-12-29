package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.vo.MtExtendVO5;

/**
 * @Description:
 * @Date: 2019/11/4 17:38
 * @Author: ${yiyang.xie}
 */
public class MtWorkOrderVO32 implements Serializable {
    private static final long serialVersionUID = -3506895903168996703L;
    @ApiModelProperty("主键ID")
    private String keyId;
    @ApiModelProperty("事件ID")
    private String eventId;
    @ApiModelProperty("扩展字段属性列表")
    private List<MtExtendVO5> attrs;

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
