package tarzan.actual.api.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2020/2/20 3:05 下午
 */
public class MtProcessWorkingDTO29 implements Serializable {
    private static final long serialVersionUID = 8635891428758202904L;
    @ApiModelProperty("数据项ID")
    private String tagId;
    @ApiModelProperty("采集状态-采集值")
    private Map<String, List<String>> tagValues = new HashMap<>();

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public Map<String, List<String>> getTagValues() {
        return tagValues;
    }

    public void setTagValues(Map<String, List<String>> tagValues) {
        this.tagValues = tagValues;
    }
}
