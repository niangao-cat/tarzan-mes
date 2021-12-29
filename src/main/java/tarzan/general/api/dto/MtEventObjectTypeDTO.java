package tarzan.general.api.dto;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;


/**
 * 对象类型维护 前端使用DTO
 * 
 * @author benjamin
 */
public class MtEventObjectTypeDTO implements Serializable {
    private static final long serialVersionUID = -1924639858126763974L;

    @ApiModelProperty("对象类型ID")
    private String objectTypeId;

    @ApiModelProperty(value = "对象类型编码(数据修改时必输)")
    @NotBlank
    private String objectTypeCode;

    @ApiModelProperty(value = "对象类型描述")
    private String description;

    @ApiModelProperty(value = "是否有效(数据修改时必输)")
    @NotBlank
    private String enableFlag;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "查询条件")
    private String whereClause;

    private Map<String, Map<String, String>> _tls;

    public String getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(String objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public Map<String, Map<String, String>> get_tls() {
        return _tls;
    }

    public void set_tls(Map<String, Map<String, String>> _tls) {
        this._tls = _tls;
    }
}
