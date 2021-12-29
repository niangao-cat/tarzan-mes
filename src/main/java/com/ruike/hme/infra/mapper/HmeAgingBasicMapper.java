package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeAgingBasicDTO;

import com.ruike.hme.domain.entity.HmeAgingBasic;
import com.ruike.hme.domain.vo.HmeAgingBasicVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 老化基础数据Mapper
 *
 * @author junfeng.chen@hand-china.com 2021-03-02 11:56:36
 */
public interface HmeAgingBasicMapper extends BaseMapper<HmeAgingBasic> {


    /**
     * 老化基础数据列表
     *
     * @param dto
     * @param tenantId
     * @author JUNFENG.CHEN@HAND-CHINA.COM 2021-03-03 14:23
     * @return HmeAgingBasicVO
     */
    List<HmeAgingBasicVO> pageList(@Param("tenantId") Long tenantId, @Param("dto") HmeAgingBasicDTO dto);
}
