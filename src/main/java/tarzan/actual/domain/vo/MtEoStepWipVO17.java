package tarzan.actual.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/26 16:48
 * @Description:
 */
public class MtEoStepWipVO17 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1945451705838709355L;
    private List<MtEoStepWipVO16> eoStepWipList;
    private String eventRequestId;
    @ApiModelProperty(value = "完工不一致标识")
    private String completeInconformityFlag;
    @ApiModelProperty(value = "全量清除标识")
    private String allClearFlag;

    public List<MtEoStepWipVO16> getEoStepWipList() {
        return eoStepWipList;
    }

    public void setEoStepWipList(List<MtEoStepWipVO16> eoStepWipList) {
        this.eoStepWipList = eoStepWipList;
    }

    public String getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(String eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    public String getCompleteInconformityFlag() {
        return completeInconformityFlag;
    }

    public void setCompleteInconformityFlag(String completeInconformityFlag) {
        this.completeInconformityFlag = completeInconformityFlag;
    }


    public String getAllClearFlag() {
        return allClearFlag;
    }

    public void setAllClearFlag(String allClearFlag) {
        this.allClearFlag = allClearFlag;
    }
}
