package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：
 *
 * @Author: chuang.yang
 * @Date: 2020-02-06
 */
public class MtProcessWorkingStrategyOrgDto1 implements Serializable {
    private static final long serialVersionUID = 5558983833732337852L;

    @ApiModelProperty("工序作业配置ID")
    private String strategyId;

    @ApiModelProperty("工序作业配置组织关系集合")
    private List<MtProcessWorkingStrategyOrgDto> processWorkingStrategyOrgRelList;

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public List<MtProcessWorkingStrategyOrgDto> getProcessWorkingStrategyOrgRelList() {
        return processWorkingStrategyOrgRelList;
    }

    public void setProcessWorkingStrategyOrgRelList(List<MtProcessWorkingStrategyOrgDto> processWorkingStrategyOrgRelList) {
        this.processWorkingStrategyOrgRelList = processWorkingStrategyOrgRelList;
    }
}
