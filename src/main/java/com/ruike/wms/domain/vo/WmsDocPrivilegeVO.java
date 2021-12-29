package com.ruike.wms.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;

/**
 * 单据授权输入
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/19 14:30
 */
@Data
public class WmsDocPrivilegeVO implements Serializable {


    private static final long serialVersionUID = -795119107733924015L;

    @ApiModelProperty(value = "用户组织关系ID")
    private String userOrganizationId;

    @ApiModelProperty(value = "主键")
    private String privilegeId;

    @ApiModelProperty(value = "单据类型")
    @LovValue(value = "WMS.USER_DOC_TYPE", meaningField = "docTypeMeaning")
    private String docType;

    @ApiModelProperty(value = "单据类型含义")
    private String docTypeMeaning;

    @ApiModelProperty(value = "仓库类型")
    @LovValue(value = "WMS.USER_WAREHOUSE_TYPE", meaningField = "locationTypeMeaning")
    private String locationType;

    @ApiModelProperty(value = "仓库类型含义")
    private String locationTypeMeaning;

    @ApiModelProperty(value = "操作类型")
    @LovValue(value = "WMS.USER_OPERATION_TYPE", meaningField = "operationTypeMeaning")
    private String operationType;

    @ApiModelProperty(value = "操作类型含义")
    private String operationTypeMeaning;

    @ApiModelProperty(value = "启用状态")
    private String enableFlag;

}
