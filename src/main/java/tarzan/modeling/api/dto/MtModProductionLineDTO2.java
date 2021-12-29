package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModProductionLineDTO2 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6020619112817183462L;

    @ApiModelProperty(value = "生产线编号")
    private String prodLineCode;

    @ApiModelProperty(value = "生产线短描述")
    private String prodLineName;

    @ApiModelProperty(value = "生产线长描述")
    private String description;

    @ApiModelProperty(value = "生产线类型")
    private String prodLineType;

    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    public String getProdLineCode() {
        return prodLineCode;
    }

    public void setProdLineCode(String prodLineCode) {
        this.prodLineCode = prodLineCode;
    }

    public String getProdLineName() {
        return prodLineName;
    }

    public void setProdLineName(String prodLineName) {
        this.prodLineName = prodLineName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProdLineType() {
        return prodLineType;
    }

    public void setProdLineType(String prodLineType) {
        this.prodLineType = prodLineType;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

}
