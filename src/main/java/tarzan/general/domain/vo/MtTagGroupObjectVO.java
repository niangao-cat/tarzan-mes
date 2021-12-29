package tarzan.general.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-09-18 15:18
 */
public class MtTagGroupObjectVO implements Serializable {

    private static final long serialVersionUID = -3274987862974674795L;
    @ApiModelProperty(value = "收集组编码")
    private String tagGroupCode;
    @ApiModelProperty(value = "物料")
    private String materialCode;
    @ApiModelProperty(value = "工艺")
    private String operationName;
    @ApiModelProperty(value = "工艺路线")
    private String routerName;
    @ApiModelProperty(value = "工艺路线步骤")
    private String routerStepName;
    @ApiModelProperty(value = "工作单元")
    private String workcellCode;
    @ApiModelProperty(value = "生产指令")
    private String workOrderNum;
    @ApiModelProperty(value = "执行作业")
    private String eoNum;
    @ApiModelProperty(value = "不良代码")
    private String ncCode;
    @ApiModelProperty(value = "装配清单")
    private String bomName;
    @ApiModelProperty(value = "装配清单行")
    private String bomComponentCode;

    @ApiModelProperty(value = "物料")
    private String materialLotCode;

    public String getTagGroupCode() {
        return tagGroupCode;
    }

    public void setTagGroupCode(String tagGroupCode) {
        this.tagGroupCode = tagGroupCode;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public String getRouterStepName() {
        return routerStepName;
    }

    public void setRouterStepName(String routerStepName) {
        this.routerStepName = routerStepName;
    }

    public String getWorkcellCode() {
        return workcellCode;
    }

    public void setWorkcellCode(String workcellCode) {
        this.workcellCode = workcellCode;
    }

    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    public String getEoNum() {
        return eoNum;
    }

    public void setEoNum(String eoNum) {
        this.eoNum = eoNum;
    }

    public String getNcCode() {
        return ncCode;
    }

    public void setNcCode(String ncCode) {
        this.ncCode = ncCode;
    }

    public String getBomName() {
        return bomName;
    }

    public void setBomName(String bomName) {
        this.bomName = bomName;
    }

    public String getBomComponentCode() {
        return bomComponentCode;
    }

    public void setBomComponentCode(String bomComponentCode) {
        this.bomComponentCode = bomComponentCode;
    }

    public String getMaterialLotCode() {
        return materialLotCode;
    }

    public void setMaterialLotCode(String materialLotCode) {
        this.materialLotCode = materialLotCode;
    }
}
