package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月20日 下午6:38:45
 *
 */
public class MtWorkOrderVO56 implements Serializable {
    
    private static final long serialVersionUID = -2451409199928730165L;
    
    @ApiModelProperty(value = "主生产指令ID")
    private String primaryWorkOrderId;
    @ApiModelProperty(value = "副生产指令ID列表")
    private List<String> secondaryWorkOrderIds;
                    
    public String getPrimaryWorkOrderId() {
        return primaryWorkOrderId;
    }
    public void setPrimaryWorkOrderId(String primaryWorkOrderId) {
        this.primaryWorkOrderId = primaryWorkOrderId;
    }
    public List<String> getSecondaryWorkOrderIds() {
        return secondaryWorkOrderIds;
    }
    public void setSecondaryWorkOrderIds(List<String> secondaryWorkOrderIds) {
        this.secondaryWorkOrderIds = secondaryWorkOrderIds;
    }
    
}
