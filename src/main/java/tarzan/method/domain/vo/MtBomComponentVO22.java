package tarzan.method.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/4/3 9:41 上午
 */
public class MtBomComponentVO22 implements Serializable {
    private static final long serialVersionUID = -4166548282482111864L;
    @ApiModelProperty("装配清单信息")
    private List<MtBomComponentVO21> bomMessages;

    @ApiModelProperty("物料Id信息")
    private List<String> materialIds;

    @ApiModelProperty("BOM组件Id信息")
    private List<String> bomComponentIds;

    @ApiModelProperty("是否考虑损耗")
    private String attritionFlag;

    @ApiModelProperty("是否考虑替代")
    private String substituteFlag;

    @ApiModelProperty("是否仅考虑关键件")
    private String keyComponentFlag;

    @ApiModelProperty("是否展开虚拟件")
    private String isPhantomUnfold;

    @ApiModelProperty("装配方式")
    private String assembleMethod;

    public List<MtBomComponentVO21> getBomMessages() {
        return bomMessages;
    }

    public void setBomMessages(List<MtBomComponentVO21> bomMessages) {
        this.bomMessages = bomMessages;
    }

    public List<String> getMaterialIds() {
        return materialIds;
    }

    public void setMaterialIds(List<String> materialIds) {
        this.materialIds = materialIds;
    }

    public List<String> getBomComponentIds() {
        return bomComponentIds;
    }

    public void setBomComponentIds(List<String> bomComponentIds) {
        this.bomComponentIds = bomComponentIds;
    }

    public String getAttritionFlag() {
        return attritionFlag;
    }

    public void setAttritionFlag(String attritionFlag) {
        this.attritionFlag = attritionFlag;
    }

    public String getSubstituteFlag() {
        return substituteFlag;
    }

    public void setSubstituteFlag(String substituteFlag) {
        this.substituteFlag = substituteFlag;
    }

    public String getKeyComponentFlag() {
        return keyComponentFlag;
    }

    public void setKeyComponentFlag(String keyComponentFlag) {
        this.keyComponentFlag = keyComponentFlag;
    }

    public String getIsPhantomUnfold() {
        return isPhantomUnfold;
    }

    public void setIsPhantomUnfold(String isPhantomUnfold) {
        this.isPhantomUnfold = isPhantomUnfold;
    }

    public String getAssembleMethod() {
        return assembleMethod;
    }

    public void setAssembleMethod(String assembleMethod) {
        this.assembleMethod = assembleMethod;
    }
}
