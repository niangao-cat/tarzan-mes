package tarzan.general.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-09-27 14:35
 */
public class MtTagDTO2 implements Serializable {
    private static final long serialVersionUID = 1009854927259348960L;
    @ApiModelProperty(value = "来源数据项编码Id")
    @NotBlank
    private String sourceTagId;
    @ApiModelProperty(value = "目标数据项编码")
    @NotBlank
    private String tagCode;
    @ApiModelProperty(value = "目标数据项编码描述")
    private String tagDescription;
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();

    public String getSourceTagId() {
        return sourceTagId;
    }

    public void setSourceTagId(String sourceTagId) {
        this.sourceTagId = sourceTagId;
    }

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
}
