package tarzan.general.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author benjamin
 */
public class MtTagGroupDTO6 implements Serializable {
    private static final long serialVersionUID = 4704969435585559796L;

    @ApiModelProperty(value = "目标数据收集组编码")
    @NotBlank
    private String tagGroupCode;
    @ApiModelProperty(value = "目标数据收集组描述")
    private String tagGroupDescription;
    @ApiModelProperty(value = "来源数据收集组Id")
    @NotBlank
    private String sourceTagGroupId;
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();

    public String getTagGroupCode() {
        return tagGroupCode;
    }

    public void setTagGroupCode(String tagGroupCode) {
        this.tagGroupCode = tagGroupCode;
    }

    public String getTagGroupDescription() {
        return tagGroupDescription;
    }

    public void setTagGroupDescription(String tagGroupDescription) {
        this.tagGroupDescription = tagGroupDescription;
    }

    public String getSourceTagGroupId() {
        return sourceTagGroupId;
    }

    public void setSourceTagGroupId(String sourceTagGroupId) {
        this.sourceTagGroupId = sourceTagGroupId;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
}
