package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.vo.MtExtendVO5;

/**
 * @Description:
 * @Date: 2019/11/12 10:19
 * @Author: ${yiyang.xie}
 */
public class MtRouterStepGroupStepVO1 implements Serializable {
    private static final long serialVersionUID = -4627929875766281298L;
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
