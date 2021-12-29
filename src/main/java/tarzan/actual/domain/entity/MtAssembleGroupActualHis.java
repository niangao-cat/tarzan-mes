package tarzan.actual.domain.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 装配组实绩历史,记录装配组所有安装位置历史记录
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
@ApiModel("装配组实绩历史,记录装配组所有安装位置历史记录")

@ModifyAudit

@Table(name = "mt_assemble_group_actual_his")
@CustomPrimary
public class MtAssembleGroupActualHis extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ASSEMBLE_GROUP_ACTUAL_HIS_ID = "assembleGroupActualHisId";
    public static final String FIELD_ASSEMBLE_GROUP_ACTUAL_ID = "assembleGroupActualId";
    public static final String FIELD_ASSEMBLE_GROUP_ID = "assembleGroupId";
    public static final String FIELD_WORKCELL_ID = "workcellId";
    public static final String FIELD_EVENT_ID = "eventId";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = 7391969190244958880L;

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID", required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("主键ID，标识唯一一条记录")
    @Id
    @Where
    private String assembleGroupActualHisId;
    @ApiModelProperty(value = "装配组实绩ID，标识唯一一组装配组工作单元装配关系", required = true)
    @NotBlank
    @Where
    private String assembleGroupActualId;
    @ApiModelProperty(value = "装配组ID", required = true)
    @NotBlank
    @Where
    private String assembleGroupId;
    @ApiModelProperty(value = "装配组安置的工作单元ID", required = true)
    @NotBlank
    @Where
    private String workcellId;
    @ApiModelProperty(value = "事件ID", required = true)
    @NotBlank
    @Where
    private String eventId;
    @Cid
    @Where
    private Long cid;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 租户ID
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 主键ID，标识唯一一条记录
     */
    public String getAssembleGroupActualHisId() {
        return assembleGroupActualHisId;
    }

    public void setAssembleGroupActualHisId(String assembleGroupActualHisId) {
        this.assembleGroupActualHisId = assembleGroupActualHisId;
    }

    /**
     * @return 装配组实绩ID，标识唯一一组装配组工作单元装配关系
     */
    public String getAssembleGroupActualId() {
        return assembleGroupActualId;
    }

    public void setAssembleGroupActualId(String assembleGroupActualId) {
        this.assembleGroupActualId = assembleGroupActualId;
    }

    /**
     * @return 装配组ID
     */
    public String getAssembleGroupId() {
        return assembleGroupId;
    }

    public void setAssembleGroupId(String assembleGroupId) {
        this.assembleGroupId = assembleGroupId;
    }

    /**
     * @return 装配组安置的工作单元ID
     */
    public String getWorkcellId() {
        return workcellId;
    }

    public void setWorkcellId(String workcellId) {
        this.workcellId = workcellId;
    }

    /**
     * @return 事件ID
     */
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * @return CID
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
