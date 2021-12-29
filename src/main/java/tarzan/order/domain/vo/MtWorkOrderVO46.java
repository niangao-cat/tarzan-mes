package tarzan.order.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author xiao.tang02@hand-china.com 2019年12月16日 下午2:45:33
 *
 */
public class MtWorkOrderVO46 implements Serializable {
    
    private static final long serialVersionUID = 7081868436606829731L;
    
    @ApiModelProperty(value = "生产指令ID",required = true)
    private String workOrderId;
    @ApiModelProperty(value = "EO编码")
    private String eoNum;
    @ApiModelProperty(value = "EO状态")
    private List<String> status;
    @ApiModelProperty(value = "EO类型")
    private List<String> eoType;
    @ApiModelProperty(value = "物料编码")
    private String materialCode;
    @ApiModelProperty(value = "物料名称")
    private String materialName;
    //add by sanfeng.zhang for xuni 2020/10/01
    @ApiModelProperty(value = "EO标识")
    private String eoIdentification;
    @ApiModelProperty(value = "装配清单编码")
    private String eoBomName;
    public String getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }
    public String getEoNum() {
        return eoNum;
    }
    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }
    public String getMaterialCode() {
        return materialCode;
    }
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }
    public String getMaterialName() {
        return materialName;
    }
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
    public List<String> getStatus() {
        return status;
    }
    public void setStatus(List<String> status) {
        this.status = status;
    }
    public List<String> getEoType() {
        return eoType;
    }
    public void setEoType(List<String> eoType) {
        this.eoType = eoType;
    }

    public String getEoIdentification() {
        return eoIdentification;
    }

    public void setEoIdentification(String eoIdentification) {
        this.eoIdentification = eoIdentification;
    }

    public String getEoBomName() {
        return eoBomName;
    }

    public void setEoBomName(String eoBomName) {
        this.eoBomName = eoBomName;
    }
}
