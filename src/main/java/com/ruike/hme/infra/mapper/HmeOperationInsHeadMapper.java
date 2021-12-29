package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeOperationInsHead;
import com.ruike.hme.domain.vo.HmeOperationInsHeadVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 作业指导头表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-11-09 19:09:39
 */
public interface HmeOperationInsHeadMapper extends BaseMapper<HmeOperationInsHead> {

    /**
     * 作业指导书查询
     * 
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/11/9 20:44 
     * @return com.ruike.hme.domain.entity.HmeOperationInsHead
     */
    List<HmeOperationInsHeadDTO2> operationInsHeadQuery(@Param("tenantId")Long tenantId,
                                                        @Param("dto") HmeOperationInsHeadDTO dto);

    /**
     * 作业指导书查询
     *
     * @param tenantId
     * @param dto
     * @author penglin.sui@hand-china.com 2021/01/19 17:10
     * @return com.ruike.hme.domain.vo.HmeOperationInsHeadVO
     */
    List<HmeOperationInsHeadVO> eSopQuery(@Param("tenantId")Long tenantId,
                                          @Param("dto") HmeOperationInsHeadDTO3 dto);

    /**
     * 单件工序作业平台-工位未出站的eo信息查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/2/23 10:29:58
     * @return java.util.List<com.ruike.hme.api.dto.HmeEoJobSnDTO5>
     */
    List<HmeEoJobSnDTO5> noSiteOutEoQuery(@Param("tenantId") Long tenantId, @Param("dto") HmeEoJobSnDTO4 dto);
}
