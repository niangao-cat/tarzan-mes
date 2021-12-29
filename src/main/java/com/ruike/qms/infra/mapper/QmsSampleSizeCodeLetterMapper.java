package com.ruike.qms.infra.mapper;

import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO;
import com.ruike.qms.api.dto.QmsMaterialInspExemptDTO2;
import com.ruike.qms.domain.entity.QmsSampleSizeCodeLetter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 样本量字码表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:11
 */
public interface QmsSampleSizeCodeLetterMapper extends BaseMapper<QmsSampleSizeCodeLetter> {

    /**
     * 样本量字码查询
     * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:11
     * @param tenantId
     * @param lotSize
     * @return
     */
    List<QmsMaterialInspExemptDTO> selectByConditionForUi(@Param("tenantId") Long tenantId, @Param("lotSize") Long lotSize);

    /**
     * 上下限交叉查询
     * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:11
     * @param tenantId
     * @param letterId
     * @param lotSize
     * @return
     */
    int selectByConditionCount(@Param("tenantId") Long tenantId, @Param("letterId") String letterId, @Param("lotSize") Long lotSize);
}
