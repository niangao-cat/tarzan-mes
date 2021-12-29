package com.ruike.hme.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hzero.boot.platform.lov.annotation.LovValue;

import java.io.Serializable;
import java.util.List;

/**
* @Classname HmeExceptionHandlePlatFormVO
* @Description 异常处理平台-主界面查询VO
* @Date  2020/6/22 14:11
* @Created by Deng xu
*/

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HmeExceptionHandlePlatFormVO implements Serializable {

    private static final long serialVersionUID = -4741515128417787430L;

    /**
        查询字段
     */
    @ApiModelProperty("租户ID")
    private Long tenantId;

    @ApiModelProperty("工位ID")
    private String workcellId;

    @ApiModelProperty("工位编码")
    private String workcellCode;

    @ApiModelProperty("工序ID")
    private String parentWorkcellId;

    @ApiModelProperty("当前用户ID")
    private Long userId;

    @ApiModelProperty("工位班次ID")
    private String wkcShiftId;

    /**
        界面显示字段
     */
    @ApiModelProperty("异常类别-MAN、EQUIPMENT、MATERIAL、METHOD、ENVIRONMENTS")
    private String exceptionType;

    @ApiModelProperty("异常类别含义")
    private String exceptionTypeMeaning;

    @ApiModelProperty("未解决异常数量")
    private Long unresolvedExcQty;

    @ApiModelProperty("本班次未解决异常数量")
    private Long shiftExcQty;

    @ApiModelProperty("异常总数")
    private Long totalExcQty;

    @ApiModelProperty("异常标签列表")
    private List<HmeExceptionRecordQueryVO> exceptionLabelList;

    @ApiModelProperty("异常清单列表")
    private List<HmeExceptionRecordQueryVO> exceptionRecordList;

}
