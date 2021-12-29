package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/4/3 10:13 上午
 */
public class MtBomComponentVO24 implements Serializable {
    private static final long serialVersionUID = 177725522743098331L;

    @ApiModelProperty("组件行Id")
    private String bomComponentId;

    @ApiModelProperty("组件行Id")
    private String bomComponentType;

    @ApiModelProperty("关键件标识")
    private String keyMaterialFlag;

    @ApiModelProperty("替代标识")
    private String substituteFlag;

    @ApiModelProperty("组件行对应物料信息列表")
    private List<MtBomComponentVO23> materialMessageList = new ArrayList<>();

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomComponentType() {
        return bomComponentType;
    }

    public void setBomComponentType(String bomComponentType) {
        this.bomComponentType = bomComponentType;
    }

    public String getKeyMaterialFlag() {
        return keyMaterialFlag;
    }

    public void setKeyMaterialFlag(String keyMaterialFlag) {
        this.keyMaterialFlag = keyMaterialFlag;
    }

    public List<MtBomComponentVO23> getMaterialMessageList() {
        return materialMessageList;
    }

    public void setMaterialMessageList(List<MtBomComponentVO23> materialMessageList) {
        this.materialMessageList = materialMessageList;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }
}
