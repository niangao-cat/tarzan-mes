package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsSampleSchemeDTO;
import com.ruike.qms.api.dto.QmsSampleSchemeDTO2;
import com.ruike.qms.api.dto.QmsSampleSchemeDTO4;
import com.ruike.qms.domain.entity.QmsSampleScheme;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 抽样方案表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:10
 */
public interface QmsSampleSchemeMapper extends BaseMapper<QmsSampleScheme> {

    /**
     * 样本量字码查询
     * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:11
     * @param tenantId
     * @param dto
     * @return
     */
    List<QmsSampleSchemeDTO> selectByConditionForUi(@Param("tenantId") Long tenantId, @Param("dto") QmsSampleSchemeDTO2 dto);

    /**
     * 上下限交叉查询
     * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:11
     * @param tenantId
     * @param schemeId
     * @param aql
     * @param lotSize
     * @return
     */
    int selectByConditionCount(@Param("tenantId") Long tenantId, @Param("schemeId") String schemeId,
                               @Param("aql") String aql, @Param("lotSize") Long lotSize);

    /**
     * 抽样方案LOV查询
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/10/15 08:30:08
     * @return java.util.List<com.ruike.qms.api.dto.QmsSampleSchemeDTO>
     */
    List<QmsSampleSchemeDTO> qmsSampleSchemeQuery(@Param("tenantId") Long tenantId, @Param("dto") QmsSampleSchemeDTO4 dto);
}
