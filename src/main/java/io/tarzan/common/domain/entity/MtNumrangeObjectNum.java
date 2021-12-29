package io.tarzan.common.domain.entity;

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
 * 号码段按对象序列号记录表
 *
 * @author MrZ 2019-08-22 21:38:58
 */
@ApiModel("号码段按对象序列号记录表")
@ModifyAudit

@Table(name = "mt_numrange_object_num")
@CustomPrimary
public class MtNumrangeObjectNum extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_NUMRANGE_OBJECT_NUM_ID = "numrangeObjectNumId";
    public static final String FIELD_NUMRANGE_ID = "numrangeId";
    public static final String FIELD_OBJECT_COMBINATION = "objectCombination";
    public static final String FIELD_NUM_CURRENT = "numCurrent";
    public static final String FIELD_NUM_RESET_LASTDATE = "numResetLastdate";
    public static final String FIELD_CID = "cid";

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
    @ApiModelProperty("按对象序列号记录表主键")
    @Id
    @Where
    private String numrangeObjectNumId;
    @ApiModelProperty(value = "号码段定义表主键", required = true)
    @NotBlank
    @Where
    private String numrangeId;
    @ApiModelProperty(value = "对象拼接值", required = true)
    @NotBlank
    @Where
    private String objectCombination;
    @ApiModelProperty(value = "当前序号", required = true)
    @NotBlank
    @Where
    private String numCurrent;
    @ApiModelProperty(value = "序列号段上一次重置的日期", required = true)
    @NotNull
    @Where
    private String numResetLastdate;
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
     * @return 按对象序列号记录表主键
     */
    public String getNumrangeObjectNumId() {
        return numrangeObjectNumId;
    }

    public void setNumrangeObjectNumId(String numrangeObjectNumId) {
        this.numrangeObjectNumId = numrangeObjectNumId;
    }

    /**
     * @return 号码段定义表主键
     */
    public String getNumrangeId() {
        return numrangeId;
    }

    public void setNumrangeId(String numrangeId) {
        this.numrangeId = numrangeId;
    }

    /**
     * @return 对象拼接值
     */
    public String getObjectCombination() {
        return objectCombination;
    }

    public void setObjectCombination(String objectCombination) {
        this.objectCombination = objectCombination;
    }

    /**
     * @return 当前序号
     */
    public String getNumCurrent() {
        return numCurrent;
    }

    public void setNumCurrent(String numCurrent) {
        this.numCurrent = numCurrent;
    }

    public String getNumResetLastdate() {
        return numResetLastdate;
    }

    public void setNumResetLastdate(String numResetLastdate) {
        this.numResetLastdate = numResetLastdate;
    }

    /**
     * @return
     */
    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

}
