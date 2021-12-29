package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO;
import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO2;
import com.ruike.qms.domain.entity.QmsMaterialInspExempt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 物料免检表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-04-26 12:06:18
 */
public interface QmsMaterialInspExemptMapper extends BaseMapper<QmsMaterialInspExempt> {


    /**
     * 物料免检查询
     * @author jiangling.zheng@hand-china.com 2020-04-26 12:06:18
     * @param tenantId
     * @param dto
     * @return
     */
    List<QmsMaterialInspExemptDTO> selectByConditionForUi(@Param("tenantId") Long tenantId, @Param("dto") QmsMaterialInspExemptDTO2 dto);

    /**
     * 物料免检查询(成本中心退料单创建,查询物料免检标识)
     * @param tenantId 租户id
     * @param dto  工厂id,物料id,供应商id包装对象
     * @author wsg guijie.wu@hand-china.com 2020/7/15 17:22
     * @return java.lang.String
     */
    String getExemptionFlag(@Param("tenantId")Long tenantId,@Param("dto") QmsMaterialInspExemptDTO2 dto);
}