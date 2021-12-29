package tarzan.inventory.domain.entity;

import java.io.Serializable;
import java.util.Objects;

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
 * 容器装载明细，记录具体容器装载的执行作业或物料批或其他容器的情况
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
@ApiModel("容器装载明细，记录具体容器装载的执行作业或物料批或其他容器的情况")

@ModifyAudit

@Table(name = "mt_container_load_detail")
@CustomPrimary
public class MtContainerLoadDetail extends AuditDomain implements Serializable {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CONTAINER_LOAD_DETAIL_ID = "containerLoadDetailId";
    public static final String FIELD_CONTAINER_ID = "containerId";
    public static final String FIELD_LOCATION_ROW = "locationRow";
    public static final String FIELD_LOCATION_COLUMN = "locationColumn";
    public static final String FIELD_LOAD_OBJECT_TYPE = "loadObjectType";
    public static final String FIELD_LOAD_QTY = "loadQty";
    public static final String FIELD_LOAD_EO_STEP_ACTUAL_ID = "loadEoStepActualId";
    public static final String FIELD_LOAD_OBJECT_ID = "loadObjectId";
    public static final String FIELD_LOAD_SEQUENCE = "loadSequence";
    public static final String FIELD_CID = "cid";
    private static final long serialVersionUID = -8478394102381238444L;

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
    @ApiModelProperty("作为容器装载明细的唯一标识，用于其他数据结构引用此容器装载明细")
    @Id
    @Where
    private String containerLoadDetailId;
    @ApiModelProperty(value = "指示该容器装载明细记录所属容器标识ID", required = true)
    @NotBlank
    @Where
    private String containerId;
    @ApiModelProperty(value = "描述装载对象被装载到容器对象的行位置")
    @Where
    private Long locationRow;
    @ApiModelProperty(value = "描述装载对象被装载到容器对象的列位置")
    @Where
    private Long locationColumn;
    @ApiModelProperty(value = "描述容器装载的对象类型，包括：", required = true)
    @NotBlank
    @Where
    private String loadObjectType;
    @ApiModelProperty(value = "装载数量")
    @Where
    private Double loadQty;
    @ApiModelProperty(value = "装载步骤")
    @Where
    private String loadEoStepActualId;
    @ApiModelProperty(value = "描述容器装入的具体对象，配合装载对象类型LOAD_OBJECT_TYPE一起使用，内容为EO、MATERIAL_LOT、CONTAINER的唯一标识ID",
                    required = true)
    @NotBlank
    @Where
    private String loadObjectId;
    @ApiModelProperty(value = "装载次序", required = true)
    @NotNull
    @Where
    private Long loadSequence;
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
     * @return 作为容器装载明细的唯一标识，用于其他数据结构引用此容器装载明细
     */
    public String getContainerLoadDetailId() {
        return containerLoadDetailId;
    }

    public void setContainerLoadDetailId(String containerLoadDetailId) {
        this.containerLoadDetailId = containerLoadDetailId;
    }

    /**
     * @return 指示该容器装载明细记录所属容器标识ID
     */
    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    /**
     * @return 描述装载对象被装载到容器对象的行位置
     */
    public Long getLocationRow() {
        return locationRow;
    }

    public void setLocationRow(Long locationRow) {
        this.locationRow = locationRow;
    }

    /**
     * @return 描述装载对象被装载到容器对象的列位置
     */
    public Long getLocationColumn() {
        return locationColumn;
    }

    public void setLocationColumn(Long locationColumn) {
        this.locationColumn = locationColumn;
    }

    /**
     * @return 描述容器装载的对象类型，包括：
     */
    public String getLoadObjectType() {
        return loadObjectType;
    }

    public void setLoadObjectType(String loadObjectType) {
        this.loadObjectType = loadObjectType;
    }

    /**
     * @return 装载数量
     */
    public Double getLoadQty() {
        return loadQty;
    }

    public void setLoadQty(Double loadQty) {
        this.loadQty = loadQty;
    }

    /**
     * @return 装载步骤
     */
    public String getLoadEoStepActualId() {
        return loadEoStepActualId;
    }

    public void setLoadEoStepActualId(String loadEoStepActualId) {
        this.loadEoStepActualId = loadEoStepActualId;
    }

    /**
     * @return 描述容器装入的具体对象，配合装载对象类型LOAD_OBJECT_TYPE一起使用，内容为EO、MATERIAL_LOT、CONTAINER的唯一标识ID
     */
    public String getLoadObjectId() {
        return loadObjectId;
    }

    public void setLoadObjectId(String loadObjectId) {
        this.loadObjectId = loadObjectId;
    }

    /**
     * @return 装载次序
     */
    public Long getLoadSequence() {
        return loadSequence;
    }

    public void setLoadSequence(Long loadSequence) {
        this.loadSequence = loadSequence;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtContainerLoadDetail that = (MtContainerLoadDetail) o;
        return Objects.equals(tenantId, that.tenantId)
                        && Objects.equals(containerLoadDetailId, that.containerLoadDetailId)
                        && Objects.equals(containerId, that.containerId)
                        && Objects.equals(locationRow, that.locationRow)
                        && Objects.equals(locationColumn, that.locationColumn)
                        && Objects.equals(loadObjectType, that.loadObjectType) && Objects.equals(loadQty, that.loadQty)
                        && Objects.equals(loadEoStepActualId, that.loadEoStepActualId)
                        && Objects.equals(loadObjectId, that.loadObjectId)
                        && Objects.equals(loadSequence, that.loadSequence) && Objects.equals(cid, that.cid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, containerLoadDetailId, containerId, locationRow, locationColumn, loadObjectType,
                        loadQty, loadEoStepActualId, loadObjectId, loadSequence, cid);
    }
}
