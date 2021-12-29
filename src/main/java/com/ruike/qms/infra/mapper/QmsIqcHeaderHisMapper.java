package com.ruike.qms.infra.mapper;

import com.ruike.qms.domain.entity.QmsIqcHeaderHis;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 质检单头历史表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-06 11:43:28
 */
public interface QmsIqcHeaderHisMapper extends BaseMapper<QmsIqcHeaderHis> {

    /**
     * 物料批id
     *
     * @param tenantId
     * @param instructionId
     * @author sanfeng.zhang@hand-china.com 2020/8/6 16:10
     * @return java.util.List<java.lang.String>
     */
    List<String> queryMaterialLotIdByLine(@Param("tenantId") Long tenantId, @Param("instructionId") String instructionId);
}
