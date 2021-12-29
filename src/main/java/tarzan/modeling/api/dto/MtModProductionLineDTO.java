package tarzan.modeling.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModProductionLineDTO implements Serializable {
    private static final long serialVersionUID = -1633503054547011630L;

    @ApiModelProperty(value = "主键ID ,表示唯一一条记录", required = true)
    private String prodLineId;
    @ApiModelProperty(value = "生产线编号", required = true)
    private String prodLineCode;
    @ApiModelProperty(value = "生产线名称", required = true)
    private String prodLineName;
    @ApiModelProperty(value = "生产线类型，区分生产线类型为自有、外协或是采购", required = true)
    private String prodLineType;
    @ApiModelProperty(value = "是否有效", required = true)
    private String enableFlag;


    /**
     * @return 主键ID ,表示唯一一条记录
     */
    public String getProdLineId() {
        return prodLineId;
    }

    public void setProdLineId(String prodLineId) {
        this.prodLineId = prodLineId;
    }

    /**
     * @return 生产线编号
     */
    public String getProdLineCode() {
        return prodLineCode;
    }

    public void setProdLineCode(String prodLineCode) {
        this.prodLineCode = prodLineCode;
    }

    /**
     * @return 生产线名称
     */
    public String getProdLineName() {
        return prodLineName;
    }

    public void setProdLineName(String prodLineName) {
        this.prodLineName = prodLineName;
    }


    /**
     * @return 生产线类型，区分生产线类型为自有、外协或是采购
     */
    public String getProdLineType() {
        return prodLineType;
    }

    public void setProdLineType(String prodLineType) {
        this.prodLineType = prodLineType;
    }


    /**
     * @return 是否有效
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

}
