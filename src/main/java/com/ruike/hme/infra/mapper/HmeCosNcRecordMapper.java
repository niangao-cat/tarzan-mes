package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosNcRecord;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 芯片不良记录表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2021-01-07 15:42:10
 */
public interface HmeCosNcRecordMapper extends BaseMapper<HmeCosNcRecord> {

    /**
     * 批量更新状态
     *
     * @param tenantId
     * @param userId
     * @param ncRecordList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/11 17:42
     */
    void batchUpdateNcRecordStatus(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("ncRecordList") List<HmeCosNcRecord> ncRecordList);

}

