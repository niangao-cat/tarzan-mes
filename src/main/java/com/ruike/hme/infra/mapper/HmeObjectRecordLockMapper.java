package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeObjectRecordLockParamDTO;
import com.ruike.hme.api.dto.HmeObjectRecordLockReturnDTO;
import com.ruike.hme.domain.entity.HmeFifthAreaKanban;
import com.ruike.hme.domain.entity.HmeObjectRecordLock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 记录锁定表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-09-12 11:33:17
 */
public interface HmeObjectRecordLockMapper extends BaseMapper<HmeObjectRecordLock> {

    /**
     * 查询过期锁
     *
     * @param tenantId
     * @author jiangling.zheng@hand-china.com 2020/9/12 14:25
     * @return java.util.List<com.ruike.hme.domain.entity.HmeObjectRecordLock>
     */

    List<HmeObjectRecordLock> queryLock(@Param("tenantId") Long tenantId);

    /**
     * UI查询
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/9/12 16:45
     * @return java.util.List<com.ruike.hme.api.dto.HmeObjectRecordLockReturnDTO>
     */

    List<HmeObjectRecordLockReturnDTO> queryLockData(@Param("tenantId") Long tenantId,
                                                     @Param("dto") HmeObjectRecordLockParamDTO dto);

    /**
     * 批量查询锁定记录
     *
     * @param tenantId
     * @param dtoList
     * @author penglin.sui@hand-china.com 2021/6/18 15:42
     * @return java.util.List<com.ruike.hme.domain.entity.HmeObjectRecordLock>
     */

    List<HmeObjectRecordLock> batchQueryLockData(@Param("tenantId") Long tenantId,
                                                 @Param("dtoList") List<HmeObjectRecordLock> dtoList);

    /**
     * 批量新增数据
     * @param domains 数据集合
     * @return
     * @author penglin.sui@hand-china.com 2021/6/18 16:24
     */
    void batchInsertLock(@Param("domains")List<HmeObjectRecordLock> domains);
}
