package tarzan.order.api.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019/12/24 2:19 下午
 */
public class MtEoRouterDTO2 implements Serializable {
    private static final long serialVersionUID = -7982839914531300530L;
    @ApiModelProperty("不良事故编码ID")
    private String ncIncidentId;
    @ApiModelProperty("不良事故编码")
    private String ncIncidentCode;
    @ApiModelProperty("不良事故状态")
    private String ncIncidentStatus;
    @ApiModelProperty("不良事故状态描述")
    private String ncIncidentStatusDesc;

    @ApiModelProperty("不良代码ID")
    private String ncCodeId;
    @ApiModelProperty("不良代码")
    private String ncCode;
    @ApiModelProperty("不良代码描述")
    private String ncCodeDesc;

    @ApiModelProperty("不良记录ID")
    private String ncRecordId;
    @ApiModelProperty("不良记录状态")
    private String ncRecordStatus;
    @ApiModelProperty("不良记录状态描述")
    private String ncRecordStatusDesc;
    @ApiModelProperty("记录人ID")
    private Long userId;
    @ApiModelProperty("记录人")
    private String userName;

    public String getNcIncidentId() {
        return ncIncidentId;
    }

    public void setNcIncidentId(String ncIncidentId) {
        this.ncIncidentId = ncIncidentId;
    }

    public String getNcIncidentCode() {
        return ncIncidentCode;
    }

    public void setNcIncidentCode(String ncIncidentCode) {
        this.ncIncidentCode = ncIncidentCode;
    }

    public String getNcIncidentStatus() {
        return ncIncidentStatus;
    }

    public void setNcIncidentStatus(String ncIncidentStatus) {
        this.ncIncidentStatus = ncIncidentStatus;
    }

    public String getNcCodeId() {
        return ncCodeId;
    }

    public void setNcCodeId(String ncCodeId) {
        this.ncCodeId = ncCodeId;
    }

    public String getNcCode() {
        return ncCode;
    }

    public void setNcCode(String ncCode) {
        this.ncCode = ncCode;
    }

    public String getNcCodeDesc() {
        return ncCodeDesc;
    }

    public void setNcCodeDesc(String ncCodeDesc) {
        this.ncCodeDesc = ncCodeDesc;
    }

    public String getNcRecordId() {
        return ncRecordId;
    }

    public void setNcRecordId(String ncRecordId) {
        this.ncRecordId = ncRecordId;
    }

    public String getNcRecordStatus() {
        return ncRecordStatus;
    }

    public void setNcRecordStatus(String ncRecordStatus) {
        this.ncRecordStatus = ncRecordStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNcIncidentStatusDesc() {
        return ncIncidentStatusDesc;
    }

    public void setNcIncidentStatusDesc(String ncIncidentStatusDesc) {
        this.ncIncidentStatusDesc = ncIncidentStatusDesc;
    }

    public String getNcRecordStatusDesc() {
        return ncRecordStatusDesc;
    }

    public void setNcRecordStatusDesc(String ncRecordStatusDesc) {
        this.ncRecordStatusDesc = ncRecordStatusDesc;
    }
}
