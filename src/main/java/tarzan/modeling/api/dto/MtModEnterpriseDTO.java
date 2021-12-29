package tarzan.modeling.api.dto;

import java.io.Serializable;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author yuan.yuan@hand-china.com
 */
public class MtModEnterpriseDTO implements Serializable {
    private static final long serialVersionUID = 5442835190477815549L;

    @ApiModelProperty("企业ID，主键，标识唯一一条记录")
    private String enterpriseId;
    @ApiModelProperty(value = "企业编码")
    private String enterpriseCode;
    @ApiModelProperty(value = "企业名称")
    private String enterpriseName;
    @ApiModelProperty(value = "企业简称")
    private String enterpriseShortName;
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;

    private Map<String, Map<String, String>> _tls;
    /**
     * @return 企业ID，主键，标识唯一一条记录
     */
    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    /**
     * @return 企业编码
     */
    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    /**
     * @return 企业名称
     */
    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
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

    public String getEnterpriseShortName() {
        return enterpriseShortName;
    }

    public void setEnterpriseShortName(String enterpriseShortName) {
        this.enterpriseShortName = enterpriseShortName;
    }


    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
}
