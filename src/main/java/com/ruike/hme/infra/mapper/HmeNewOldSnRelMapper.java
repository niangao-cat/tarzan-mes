package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeNewOldSnRel;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 新旧SN关系表Mapper
 *
 * @author qinxia.Huang@raycus-china.com 2021-09-28 17:45:03
 */
public interface HmeNewOldSnRelMapper extends BaseMapper<HmeNewOldSnRel> {

    /**
     * 批量更新表
     *
     * @param tenantId
     * @param userId
     * @param updateList
     * @return void
     * @author qinxia.huang@raycus-china.com 2021-09-30 10:08:03
     */
    void myBatchUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("updateList") List<HmeNewOldSnRel> updateList);

    /**
     * 批量插入表
     *
     * @param domains
     * @return void
     * @author qinxia.huang@raycus-china.com 2021-09-30 10:08:03
     */
    void insertIface(@Param("domains") List<HmeNewOldSnRel> domains);

    /**
     *查询ID
     * @param  userName
     * @return long ID
     * @author qinxia.huang@raycus-china.com 2021-09-30 10:08:03
     */
    Long getUserId(@Param("userName") String userName);

}
