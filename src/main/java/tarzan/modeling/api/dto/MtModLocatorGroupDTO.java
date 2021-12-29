package tarzan.modeling.api.dto;

import java.io.Serializable;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModLocatorGroupDTO implements Serializable {
    private static final long serialVersionUID = 6279178709102327120L;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    private String locatorGroupId;
    @ApiModelProperty(value = "库位组编码", required = true)
    private String locatorGroupCode;
    @ApiModelProperty(value = "库位组名称", required = true)
    private String locatorGroupName;
    @ApiModelProperty(value = "是否有效", required = true)
    private String enableFlag;
    private Map<String, Map<String, String>> _tls;

    /**
     * @return 主键ID，标识唯一一条记录
     */
    public String getLocatorGroupId() {
        return locatorGroupId;
    }

    public void setLocatorGroupId(String locatorGroupId) {
        this.locatorGroupId = locatorGroupId;
    }

    /**
     * @return 库位组编码
     */
    public String getLocatorGroupCode() {
        return locatorGroupCode;
    }

    public void setLocatorGroupCode(String locatorGroupCode) {
        this.locatorGroupCode = locatorGroupCode;
    }

    /**
     * @return 库位组名称
     */
    public String getLocatorGroupName() {
        return locatorGroupName;
    }

    public void setLocatorGroupName(String locatorGroupName) {
        this.locatorGroupName = locatorGroupName;
    }

    /**
     * @return 是否有效
     */
    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
}
