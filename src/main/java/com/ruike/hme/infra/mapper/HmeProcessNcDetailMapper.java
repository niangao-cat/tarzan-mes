package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeProcessNcDetail;
import com.ruike.hme.domain.entity.HmeProcessNcLine;
import com.ruike.hme.domain.vo.HmeProcessNcDetailVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工序不良明细表Mapper
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
public interface HmeProcessNcDetailMapper extends BaseMapper<HmeProcessNcDetail> {

    List<HmeProcessNcDetailVO> selectDetail(@Param("tenantId") Long tenantId, @Param("lineId") String lineId);

    void deleteByLine(@Param("tenantId") Long tenantId, @Param("hmeProcessNcLine") HmeProcessNcLine hmeProcessNcLine);

    void deleteDetailByHeader(@Param("tenantId") Long tenantId, @Param("headerId") String headerId);

    /**
     * 批量更新工序不良明细
     *
     * @param tenantId
     * @param userId
     * @param domains
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/30 17:01
     */
    void batchHeaderUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("domains") List<HmeProcessNcDetail> domains);
}
