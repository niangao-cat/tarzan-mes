package tarzan.instruction.domain.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hzero.mybatis.common.query.Where;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.Cid;
import io.choerodon.mybatis.annotation.CustomPrimary;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 指令明细行
 *
 * @author yiyang.xie@hand-china.com 2019-10-16 10:19:53
 */
@ApiModel("指令明细行")

@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "mt_instruction_detail")
@CustomPrimary
public class MtInstructionDetail extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_INSTRUCTION_DETAIL_ID = "instructionDetailId";
    public static final String FIELD_INSTRUCTION_ID = "instructionId";
    public static final String FIELD_MATERIAL_LOT_ID = "materialLotId";
    public static final String FIELD_CID = "cid";

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty(value = "租户ID",required = true)
    @NotNull
    @Where
    private Long tenantId;
    @ApiModelProperty("指令明细ID,唯一标识")
    @Id
    @Where
    private String instructionDetailId;
    @ApiModelProperty(value = "指令ID",required = true)
    @NotBlank
    @Where
    private String instructionId;
    @ApiModelProperty(value = "物料批ID",required = true)
    @NotBlank
    @Where
    private String materialLotId;
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
     * @return 指令明细ID,唯一标识
     */
	public String getInstructionDetailId() {
		return instructionDetailId;
	}

	public void setInstructionDetailId(String instructionDetailId) {
		this.instructionDetailId = instructionDetailId;
	}
    /**
     * @return 指令ID
     */
	public String getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(String instructionId) {
		this.instructionId = instructionId;
	}
    /**
     * @return 物料批ID
     */
	public String getMaterialLotId() {
		return materialLotId;
	}

	public void setMaterialLotId(String materialLotId) {
		this.materialLotId = materialLotId;
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
