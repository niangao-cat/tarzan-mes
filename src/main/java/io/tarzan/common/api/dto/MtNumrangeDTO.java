package io.tarzan.common.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.vo.MtNumrangeRuleVO2;

/**
 * @author yuan.yuan@hand-china.com
 * @ClassName MtNumrangeDTO
 * @createTime 2019年08月16日 16:32:00
 */
public class MtNumrangeDTO implements Serializable {
    private static final long serialVersionUID = -5030832268111186239L;
    @ApiModelProperty(value = "号码段定义表主键")
    private String numrangeId;
    @ApiModelProperty(value = "编码对象ID")
    private String objectId;
    @ApiModelProperty(value = "号段组号")
    private String numrangeGroup;
    @ApiModelProperty(value = "号段描述")
    private String numDescription;
    @ApiModelProperty(value = "号段示例")
    private String numExample;
    @ApiModelProperty(value = "外部输入编码标识")
    private String outsideNumFlag;
    @ApiModelProperty(value = "是否有效")
    private String enableFlag;
    @ApiModelProperty(value = "对象编码")
    private String objectCode;
    @ApiModelProperty(value = "对象描述")
    private String objectName;
    @ApiModelProperty(value = "规则列表")
    private List<MtNumrangeRuleVO2> rules;
    @ApiModelProperty(value = "多语言信息")
    private Map<String, Map<String, String>> _tls = Collections.emptyMap();

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }

    public String getNumrangeId() {
        return numrangeId;
    }

    public void setNumrangeId(String numrangeId) {
        this.numrangeId = numrangeId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getNumrangeGroup() {
        return numrangeGroup;
    }

    public void setNumrangeGroup(String numrangeGroup) {
        this.numrangeGroup = numrangeGroup;
    }

    public String getNumDescription() {
        return numDescription;
    }

    public void setNumDescription(String numDescription) {
        this.numDescription = numDescription;
    }

    public String getOutsideNumFlag() {
        return outsideNumFlag;
    }

    public void setOutsideNumFlag(String outsideNumFlag) {
        this.outsideNumFlag = outsideNumFlag;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public List<MtNumrangeRuleVO2> getRules() {
        return rules;
    }

    public void setRules(List<MtNumrangeRuleVO2> rules) {
        this.rules = rules;
    }

    public String getNumExample() {
        return numExample;
    }

    public void setNumExample(String numExample) {
        this.numExample = numExample;
    }
}
