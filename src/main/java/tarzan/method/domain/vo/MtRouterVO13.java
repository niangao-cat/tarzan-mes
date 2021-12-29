package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.vo.MtExtendVO5;

/**
 * @author peng.yuan
 * @ClassName MtRouterVO13
 * @description
 * @date 2019年11月12日 9:57
 */
public class MtRouterVO13 implements Serializable {
    private static final long serialVersionUID = 1290410208124918179L;

    @ApiModelProperty(value = "主键id")
    private String keyId;
    @ApiModelProperty(value = "事件id")
    private String eventId;
    @ApiModelProperty(value = "扩展属性")
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
