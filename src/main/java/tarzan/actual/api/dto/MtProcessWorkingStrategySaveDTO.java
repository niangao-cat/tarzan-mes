package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description：UI保存头行数据参数
 *
 * @Author: chuang.yang
 * @Date: 2020-02-06
 */
public class MtProcessWorkingStrategySaveDTO implements Serializable {

    private static final long serialVersionUID = 649622067219977465L;

    @ApiModelProperty("头数据")
    private MtProcessWorkingStrategyDTO processWorkingStrategy;

    @ApiModelProperty("行数据集合")
    private List<MtProcessWorkingStrategyDetailDTO> detailList;

    public MtProcessWorkingStrategyDTO getProcessWorkingStrategy() {
        return processWorkingStrategy;
    }

    public void setProcessWorkingStrategy(MtProcessWorkingStrategyDTO processWorkingStrategy) {
        this.processWorkingStrategy = processWorkingStrategy;
    }

    public List<MtProcessWorkingStrategyDetailDTO> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<MtProcessWorkingStrategyDetailDTO> detailList) {
        this.detailList = detailList;
    }
}
