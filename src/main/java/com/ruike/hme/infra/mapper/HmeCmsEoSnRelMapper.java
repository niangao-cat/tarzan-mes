package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCmsEoSnRel;
import com.ruike.hme.domain.vo.HmeCmsEoSnRelVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * CMS-EO_SN关系表Mapper
 *
 * @author qinxia.Huang@raycus-china.com 2021-09-28 17:45:03
 */
public interface HmeCmsEoSnRelMapper extends BaseMapper<HmeCmsEoSnRel> {

    /**
     * 批量更新CMS-EO_SN关系表
     *
     * @param tenantId
     * @param userId
     * @param updateList
     * @return void
     * @author qinxia.huang@raycus-china.com 2021-09-30 10:08:03
     */
    void myBatchUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("updateList") List<HmeCmsEoSnRel> updateList);
}
