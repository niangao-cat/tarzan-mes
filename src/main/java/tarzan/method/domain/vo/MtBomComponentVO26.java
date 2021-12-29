package tarzan.method.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/7/10 10:53
 * @Description:
 */
public class MtBomComponentVO26 implements Serializable {
    private static final long serialVersionUID = 7585269516584574357L;

    @ApiModelProperty("装配清单组件ID")
    private String bomComponentId;
    @ApiModelProperty("装配清单ID")
    private String bomId;
    @ApiModelProperty("物料ID")
    private String materialId;
    @ApiModelProperty("组件类型")
    private String bomComponentType;
    @ApiModelProperty("装配清单ID")
    private Long lineNumber;

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getBomComponentType() {
        return bomComponentType;
    }

    public void setBomComponentType(String bomComponentType) {
        this.bomComponentType = bomComponentType;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }
}
