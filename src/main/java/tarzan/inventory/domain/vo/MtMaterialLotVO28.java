package tarzan.inventory.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/1/13 11:09
 * @Description:
 */
public class MtMaterialLotVO28 implements Serializable {
    private static final long serialVersionUID = -2944109169024060093L;

    @ApiModelProperty("装载对象ID")
    private String loadObjectId;

    @ApiModelProperty("装载对象类型")
    private String loadObjectType;

    @ApiModelProperty("装载容器信息-嵌套关系")
    private List<MtMaterialLotVO28> loadContainer;

    public String getLoadObjectId() {
        return loadObjectId;
    }

    public void setLoadObjectId(String loadObjectId) {
        this.loadObjectId = loadObjectId;
    }

    public String getLoadObjectType() {
        return loadObjectType;
    }

    public void setLoadObjectType(String loadObjectType) {
        this.loadObjectType = loadObjectType;
    }

    public List<MtMaterialLotVO28> getLoadContainer() {
        return loadContainer;
    }

    public void setLoadContainer(List<MtMaterialLotVO28> loadContainer) {
        this.loadContainer = loadContainer;
    }
}
