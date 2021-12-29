package tarzan.actual.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/14 5:22 下午
 */
public class MtProcessWorkingDTO12 implements Serializable {
    private static final long serialVersionUID = 6193027847113403208L;
    @ApiModelProperty(value = "工作单元Id", required = true)
    private String workcellId;
    @ApiModelProperty(value = "站点ID", required = true)
    private String siteId;
    @ApiModelProperty(value = "执行作业Id", required = true)
    private String eoId;
    @ApiModelProperty(value = "装配清单Id", required = true)
    private String bomId;
    @ApiModelProperty("步骤Id")
    private String routerStepId;

    @ApiModelProperty("按工序装配标识")
    private String operationAssembleFlag;

    @ApiModelProperty("输入编码")
    private String inputCode;

    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getEoId() {
        return eoId;
    }

    public void setEoId(String eoId) {
        this.eoId = eoId;
    }

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public String getOperationAssembleFlag() {
        return operationAssembleFlag;
    }

    public void setOperationAssembleFlag(String operationAssembleFlag) {
        this.operationAssembleFlag = operationAssembleFlag;
    }

    public String getInputCode() {
        return inputCode;
    }

    public void setInputCode(String inputCode) {
        this.inputCode = inputCode;
    }
}
