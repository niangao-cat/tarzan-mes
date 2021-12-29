package tarzan.inventory.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2019/10/14 20:29
 * @Description:
 */
public class MtMaterialLotVO16 implements Serializable {

    private static final long serialVersionUID = 7620079640043190871L;
    @ApiModelProperty("物料批ID")
    private String materialLotId;
    @ApiModelProperty("顺序")
    private Long sequence;

    public String getMaterialLotId() {
        return materialLotId;
    }

    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
