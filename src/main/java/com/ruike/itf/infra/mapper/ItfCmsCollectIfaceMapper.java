package com.ruike.itf.infra.mapper;

import com.ruike.hme.domain.entity.HmeCmsEoSnRel;
import com.ruike.itf.domain.entity.ItfCmsCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * CMS数据采集接口表Mapper
 *
 * @author yonghui.zhu@hand-china.com 2020-08-04 19:51:39
 */

public interface ItfCmsCollectIfaceMapper extends BaseMapper<ItfCmsCollectIface> {
    /**
     * 批量插入接口表
     *
     * @param domains
     * @return void
     * @author qinxia.huang@raycus-china.com 2021-09-30 10:08:03
     */
    void insertIface(@Param("domains") List<ItfCmsCollectIface> domains);
    /**
     * 批量更新接口表
     *
     * @param updateList
     * @return void
     * @author qinxia.huang@raycus-china.com 2021-09-30 10:08:03
     */
    void myBatchUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("updateList") List<ItfCmsCollectIface> updateList);
}
