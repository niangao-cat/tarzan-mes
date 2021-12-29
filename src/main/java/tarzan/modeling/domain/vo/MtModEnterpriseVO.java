package tarzan.modeling.domain.vo;

import java.io.Serializable;

public class MtModEnterpriseVO implements Serializable {
    private static final long serialVersionUID = -1278353223583155742L;

    private String enterpriseCode; // 企业编号
    private String enterpriseName; // 企业名称
    private String enterpriseShortName; // 企业简称
    private String enableFlag; // 是否有效


    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseShortName() {
        return enterpriseShortName;
    }

    public void setEnterpriseShortName(String enterpriseShortName) {
        this.enterpriseShortName = enterpriseShortName;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

}
