package com.ruike.wms.api.dto;

import io.choerodon.core.exception.CommonException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author ywj
 * @version 0.0.1
 * @description 工单在制明细查询报表
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @return
 */
@Data
public class WorkOrderInProcessDetailsQueryReportDTO implements Serializable {

    private static final long serialVersionUID = -8876624370365379544L;

    @ApiModelProperty(value = "工单ID")
    private List<String> workOrderId;

    @ApiModelProperty(value = "产品ID")
    private List<String> materialId;

    @ApiModelProperty(value = "生产线ID")
    private String prodLineId;

    @ApiModelProperty(value = "工单状态")
    private String woStatus;

    @ApiModelProperty(value = "质量状态")
    private String qualityStatus;

    @ApiModelProperty(value = "是否冻结")
    private String freezeFlag;

    @ApiModelProperty(value = "是否转型")
    private String transformFlag;

    @ApiModelProperty(value = "SN编码列表")
    private List<String> snList;

    @ApiModelProperty(value = "工序列表")
    private List<String> processList;

    @ApiModelProperty(value = "工位列表")
    private List<String> workcellList;

    @ApiModelProperty(value = "加工人员")
    private String realName;

    @ApiModelProperty(value = "最新不良代码项")
    private String latestNcTag;

    @ApiModelProperty(value = "产品类型列表")
    private List<String> productTypeList;

    @ApiModelProperty(value = "产品类型匹配")
    private String productMatch;

    public static void validParam(WorkOrderInProcessDetailsQueryReportDTO dto) {
        // 验证产品类型列表
        if (CollectionUtils.isNotEmpty(dto.getProductTypeList())) {
            if (dto.getProductTypeList().stream().allMatch(StringUtils::isBlank)) {
                dto.setProductTypeList(null);
            } else {
                int paramCount = 4;
                if (dto.getProductTypeList().size() != paramCount) {
                    throw new CommonException("产品类型传入参数必须为{0}个", paramCount);
                }
                dto.getProductTypeList().forEach(type -> {
                    if (StringUtils.isNotBlank(type) && type.length() > 1) {
                        throw new CommonException("产品类型列表中含有非法元素 {0} 长度大于1", type);
                    }
                });
                dto.setProductMatch(dto.getProductTypeList().stream().map(type -> StringUtils.isEmpty(type) ? "_" : type).reduce("", String::concat));
            }
        }
    }

}
