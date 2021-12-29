package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeDataRecordExtend;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采集项记录扩展表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2021-09-29 14:24:34
 */
public interface HmeDataRecordExtendMapper extends BaseMapper<HmeDataRecordExtend> {

    /**
     * 更新标准值
     *
     * @param tenantId
     * @param userId
     * @param updateDataRecordExtendList
     * @author sanfeng.zhang@hand-china.com 2021/9/29 15:32
     * @return void
     */
    void batchUpdateStandardValue(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("updateDataRecordExtendList") List<HmeDataRecordExtend> updateDataRecordExtendList);
}
