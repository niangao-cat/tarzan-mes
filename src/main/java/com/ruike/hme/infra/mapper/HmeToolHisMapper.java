package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeToolHisDTO;
import com.ruike.hme.domain.entity.HmeToolHis;
import com.ruike.hme.domain.vo.HmeToolHisVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工装基础数据历史表Mapper
 *
 * @author li.zhang13@hand-china.com 2021-01-07 10:06:46
 */
public interface HmeToolHisMapper extends BaseMapper<HmeToolHis> {

    /**
     * @Description: 查询工装修改历史记录
     * @author: li.zhang13@hand-china.com
     * @param ：@param tenantId
     * @param ：@param hmeToolHisDTO
     * @param ：@return
     * @return : HmeToolHisVO
     */
    List<HmeToolHisVO> selectHmeToolHis(@Param("tenantId")String tenantId, @Param("hmeToolHisDTO") HmeToolHisDTO hmeToolHisDTO);
}
