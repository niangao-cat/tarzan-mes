package tarzan.pull.domain.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * MtPullOnhandSnapshotVO7
 *
 * @author: {xieyiyang}
 * @date: 2020/2/6 12:47
 * @description:
 */
public class MtPullOnhandSnapshotVO7 implements Serializable {
    private static final long serialVersionUID = -5976546020498813042L;

    @ApiModelProperty(value = "配送路线ID")
    private String areaId;
    @ApiModelProperty(value = "创建人")
    private Long createdBy;
    @ApiModelProperty(value = "编码参数列表")
    private List<String> numIncomingValueList;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getNumIncomingValueList() {
        return numIncomingValueList;
    }

    public void setNumIncomingValueList(List<String> numIncomingValueList) {
        this.numIncomingValueList = numIncomingValueList;
    }
}
