package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/11/3 19:46
 */
public class MtEoStepWipVO18 implements Serializable {
    private static final long serialVersionUID = 6952292389421136313L;
    @ApiModelProperty(value = "来源状态")
    private String sourceStatus;

    @ApiModelProperty(value = "全量清除标识")
    private String allClearFlag;

    @ApiModelProperty("完工不一致标识")
    private String completeInconformityFlag;

    @ApiModelProperty("工艺路线实绩唯一标识")
    private String eoRouterActualId;

    @ApiModelProperty("步骤唯一标识")
    private String routerStepId;

    @ApiModelProperty("本次验证数据集合")
    private List<MtEoStepWipVO19> calculateDataList;

    public String getSourceStatus() {
        return sourceStatus;
    }

    public void setSourceStatus(String sourceStatus) {
        this.sourceStatus = sourceStatus;
    }

    public String getAllClearFlag() {
        return allClearFlag;
    }

    public void setAllClearFlag(String allClearFlag) {
        this.allClearFlag = allClearFlag;
    }

    public String getCompleteInconformityFlag() {
        return completeInconformityFlag;
    }

    public void setCompleteInconformityFlag(String completeInconformityFlag) {
        this.completeInconformityFlag = completeInconformityFlag;
    }

    public String getEoRouterActualId() {
        return eoRouterActualId;
    }

    public void setEoRouterActualId(String eoRouterActualId) {
        this.eoRouterActualId = eoRouterActualId;
    }

    public String getRouterStepId() {
        return routerStepId;
    }

    public void setRouterStepId(String routerStepId) {
        this.routerStepId = routerStepId;
    }

    public List<MtEoStepWipVO19> getCalculateDataList() {
        return calculateDataList;
    }

    public void setCalculateDataList(List<MtEoStepWipVO19> calculateDataList) {
        this.calculateDataList = calculateDataList;
    }
}
