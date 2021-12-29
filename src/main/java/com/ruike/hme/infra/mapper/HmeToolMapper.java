package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeToolDTO;
import com.ruike.hme.domain.entity.HmeTool;
import com.ruike.hme.domain.vo.HmeToolCheckVO;
import com.ruike.hme.domain.vo.HmeToolVO;
import com.ruike.hme.domain.vo.HmeToolVO2;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工装基础数据表Mapper
 *
 * @author li.zhang13@hand-china.com 2021-01-07 10:06:45
 */
public interface HmeToolMapper extends BaseMapper<HmeTool> {

    /**
     * @Description: 查询工装基本数据
     * @author: li.zhang13@hand-china.com
     * @param ：@param tenantId
     * @param ：@param hmeTooldto
     * @param ：@return
     * @return : HmeToolVO
     */
    List<HmeToolVO> selectHmeTOOLs(@Param("tenantId") Long tenantId, @Param("hmeTooldto") HmeToolDTO hmeTooldto);

    /**
     * 根据工位获取工装信息
     * @param tenantId    租户ID
     * @param workcellId 工位ID
     * @return java.util<com.ruike.hme.domain.entity.HmeTool>
     */
    List<HmeToolVO2> selectToolByWorkcellId(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "workcellId") String workcellId);
}
