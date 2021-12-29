package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeTagPassRateHeaderDTO;
import com.ruike.hme.domain.entity.HmeTagPassRateHeader;
import com.ruike.hme.domain.vo.HmeTagPassRateHeaderVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 偏振度和发散角良率维护头表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021-09-14 10:10:35
 */
public interface HmeTagPassRateHeaderMapper extends BaseMapper<HmeTagPassRateHeader> {
    /**
     * 偏振度&发散角良率维护 头表查询
     *
     * @param tenantId 租户id
     * @param dto      查询条件
     * @return
     */
    List<HmeTagPassRateHeaderVO> queryTagPassRateHeader(@Param("tenantId") Long tenantId, @Param("dto") HmeTagPassRateHeaderDTO dto);

}
