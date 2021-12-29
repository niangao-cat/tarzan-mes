package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/1/13 11:15
 * @Description:
 */
public class MtMaterialLotVO30 implements Serializable {
    private static final long serialVersionUID = 6872940263103570567L;

    @ApiModelProperty("条码号")
    private String code;

    @ApiModelProperty("标识")
    private String Identification;

    @ApiModelProperty("是否获取所有层级")
    private String allLevelFlag;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdentification() {
        return Identification;
    }

    public void setIdentification(String identification) {
        this.Identification = identification;
    }

    public String getAllLevelFlag() {
        return allLevelFlag;
    }

    public void setAllLevelFlag(String allLevelFlag) {
        this.allLevelFlag = allLevelFlag;
    }
}
