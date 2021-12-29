package tarzan.material.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author {yiyang.xie@hand-china.com}
 */
public class MtUomVO4 implements Serializable {

    private static final long serialVersionUID = -8322956973485929929L;
    @ApiModelProperty("单位ID")
    private String uomId;
    @ApiModelProperty("单位类型编码")
    private String uomType;
    @ApiModelProperty("单位编码")
    private String uomCode;
    @ApiModelProperty("单位名称")
    private String uomName;
    @ApiModelProperty("主单位标识")
    private String primaryFlag;
    @ApiModelProperty("是否有效")
    private String enableFlag;

    public String getUomId() {
        return uomId;
    }

    public void setUomId(String uomId) {
        this.uomId = uomId;
    }

    public String getUomType() {
        return uomType;
    }

    public void setUomType(String uomType) {
        this.uomType = uomType;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    public String getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(String primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }
}
