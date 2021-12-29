package tarzan.method.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtAssembleGroupVO4
 * @description
 * @date 2019年10月09日 15:15
 */
public class MtAssembleGroupVO4 implements Serializable {
    private static final long serialVersionUID = -4223691183542728556L;

    private String assembleGroupId;// 装配组ID
    private String siteId;// 站点ID
    private String assembleGroupCode;// 装配组编码
    private String description;// 装配组描述
    private String autoInstallPointFlag;// 自动安装装配点标识
    private String assembleControlFlag;// 启用装配控制标识
    private String assembleSequenceFlag;// 按顺序装配标识
    private String status;// 状态

    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getAssembleGroupCode() {
        return assembleGroupCode;
    }

    public void setAssembleGroupCode(String assembleGroupCode) {
        this.assembleGroupCode = assembleGroupCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAutoInstallPointFlag() {
        return autoInstallPointFlag;
    }

    public void setAutoInstallPointFlag(String autoInstallPointFlag) {
        this.autoInstallPointFlag = autoInstallPointFlag;
    }

    public String getAssembleControlFlag() {
        return assembleControlFlag;
    }

    public void setAssembleControlFlag(String assembleControlFlag) {
        this.assembleControlFlag = assembleControlFlag;
    }

    public String getAssembleSequenceFlag() {
        return assembleSequenceFlag;
    }

    public void setAssembleSequenceFlag(String assembleSequenceFlag) {
        this.assembleSequenceFlag = assembleSequenceFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
