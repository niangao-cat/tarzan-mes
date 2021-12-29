package tarzan.general.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-09-19 19:32
 */
public class MtTagApiDTO implements Serializable {
    private static final long serialVersionUID = -5521696016640798159L;
    @ApiModelProperty("主键")
    private String apiId;
    @ApiModelProperty(value = "类名")
    private String apiClass;
    @ApiModelProperty(value = "API名称")
    private String apiName;
    @ApiModelProperty(value = "API函数")
    private String apiFunction;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiClass() {
        return apiClass;
    }

    public void setApiClass(String apiClass) {
        this.apiClass = apiClass;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiFunction() {
        return apiFunction;
    }

    public void setApiFunction(String apiFunction) {
        this.apiFunction = apiFunction;
    }
}
