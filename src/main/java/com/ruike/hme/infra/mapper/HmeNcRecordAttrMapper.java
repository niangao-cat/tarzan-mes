package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeNcRecordAttr;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 不良记录扩展属性表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020-07-23 10:53:45
 */
public interface HmeNcRecordAttrMapper extends BaseMapper<HmeNcRecordAttr> {

    /**
     * 批量更新
     * @param tenantId
     * @param userId
     * @param hmeNcRecordAttrList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/6/4
     */
    void myBatchUpdate(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("hmeNcRecordAttrList") List<HmeNcRecordAttr> hmeNcRecordAttrList);

}
