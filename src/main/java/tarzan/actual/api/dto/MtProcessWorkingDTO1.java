package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-12
 */
public class MtProcessWorkingDTO1 implements Serializable {
    private static final long serialVersionUID = -4455464391421164102L;

    @ApiModelProperty("任务来源")
    private String taskSource;
    
    @ApiModelProperty("任务来源头主键")
    private String taskSourceStrategyId;

    @ApiModelProperty("Tab页配置")
    private List<MtProcessWorkingStrategyDetailDTO1> moveStatusList;

    @ApiModelProperty("按钮配置配置")
    private List<MtProcessWorkingStrategyDetailDTO1> buttonFLagList;

    @ApiModelProperty("EO移动配置")
    private List<MtProcessWorkingStrategyDetailDTO1> eoMoveList;

    @ApiModelProperty("EO装配配置")
    private List<MtProcessWorkingStrategyDetailDTO1> eoAssembleList;

    @ApiModelProperty("数据采集配置")
    private List<MtProcessWorkingStrategyDetailDTO1> dataCollectList;

    public String getTaskSource() {
        return taskSource;
    }

    public void setTaskSource(String taskSource) {
        this.taskSource = taskSource;
    }

    public List<MtProcessWorkingStrategyDetailDTO1> getMoveStatusList() {
        return moveStatusList;
    }

    public void setMoveStatusList(List<MtProcessWorkingStrategyDetailDTO1> moveStatusList) {
        this.moveStatusList = moveStatusList;
    }

    public List<MtProcessWorkingStrategyDetailDTO1> getButtonFLagList() {
        return buttonFLagList;
    }

    public void setButtonFLagList(List<MtProcessWorkingStrategyDetailDTO1> buttonFLagList) {
        this.buttonFLagList = buttonFLagList;
    }

    public List<MtProcessWorkingStrategyDetailDTO1> getEoMoveList() {
        return eoMoveList;
    }

    public void setEoMoveList(List<MtProcessWorkingStrategyDetailDTO1> eoMoveList) {
        this.eoMoveList = eoMoveList;
    }

    public List<MtProcessWorkingStrategyDetailDTO1> getEoAssembleList() {
        return eoAssembleList;
    }

    public void setEoAssembleList(List<MtProcessWorkingStrategyDetailDTO1> eoAssembleList) {
        this.eoAssembleList = eoAssembleList;
    }

    public List<MtProcessWorkingStrategyDetailDTO1> getDataCollectList() {
        return dataCollectList;
    }

    public void setDataCollectList(List<MtProcessWorkingStrategyDetailDTO1> dataCollectList) {
        this.dataCollectList = dataCollectList;
    }

    public String getTaskSourceStrategyId() {
        return taskSourceStrategyId;
    }

    public void setTaskSourceStrategyId(String taskSourceStrategyId) {
        this.taskSourceStrategyId = taskSourceStrategyId;
    }
}
