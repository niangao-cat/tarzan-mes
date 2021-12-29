package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeProcessNcLine;
import com.ruike.hme.domain.vo.HmeProcessNcLineVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工序不良行表Mapper
 *
 * @author li.zhang13@hand-china.com 2021-01-21 09:36:44
 */
public interface HmeProcessNcLineMapper extends BaseMapper<HmeProcessNcLine> {

    List<HmeProcessNcLineVO> selectProcessLine(@Param("tenantId") Long tenantId, @Param("headerId") String headerId);

    void deleteLineByHeader(@Param("tenantId") Long tenantId, @Param("headerId") String headerId);

    /**
     * 批量更新行
     *
     * @param tenantId
     * @param userId
     * @param domains
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/3/30 16:56
     */
    void batchHeaderUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("domains") List<HmeProcessNcLine> domains);

    /**
     * 查询行数据
     *
     * @param tenantId
     * @param hmeProcessNcLine
     * @return java.util.List<com.ruike.hme.domain.entity.HmeProcessNcLine>
     * @author sanfeng.zhang@hand-china.com 2021/3/30 19:02
     */
    List<HmeProcessNcLine> queryProcessNcLineList(@Param("tenantId") Long tenantId, @Param("hmeProcessNcLine") HmeProcessNcLine hmeProcessNcLine);
}
