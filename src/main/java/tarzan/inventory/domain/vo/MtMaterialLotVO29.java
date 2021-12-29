package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/1/13 11:11
 * @Description:
 */
public class MtMaterialLotVO29 implements Serializable {
    private static final long serialVersionUID = -475896269997722239L;

    @ApiModelProperty("条码类型")
    private String codeType;

    @ApiModelProperty("条码ID")
    private String codeId;

    @ApiModelProperty("容器类型")
    private String containerType;

    @ApiModelProperty("装载容器ID")
    private String loadingContainerId;

    @ApiModelProperty("装载对象")
    private List<MtMaterialLotVO28> loadingObjectlList;

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getLoadingContainerId() {
        return loadingContainerId;
    }

    public void setLoadingContainerId(String loadingContainerId) {
        this.loadingContainerId = loadingContainerId;
    }

    public List<MtMaterialLotVO28> getLoadingObjectlList() {
        return loadingObjectlList;
    }

    public void setLoadingObjectlList(List<MtMaterialLotVO28> loadingObjectlList) {
        this.loadingObjectlList = loadingObjectlList;
    }
}
