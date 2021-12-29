package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosTestMonitorLine;
import com.ruike.hme.domain.vo.HmeCosTestMonitorLineVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * COS测试良率监控行表Mapper
 *
 * @author wengang.qiang@hand-china.com 2021-09-16 14:29:13
 */
public interface HmeCosTestMonitorLineMapper extends BaseMapper<HmeCosTestMonitorLine> {

    /**
     * COS测试良率监控行表 数据查询
     *
     * @param tenantId           租户id
     * @param cosMonitorHeaderId 头表id
     * @return
     */
    List<HmeCosTestMonitorLineVO> queryCosMonitorLine(@Param("tenantId") Long tenantId, @Param("cosMonitorHeaderId") String cosMonitorHeaderId);

    /**
     * 根据materialLotId去查 CosType 和 Wafer
     *
     * @param tenantId          租户id
     * @param materialLotIdList 条码id
     * @return
     */
    List<HmeCosTestMonitorLineVO> queryCosTypeAndWafer(@Param("tenantId") Long tenantId, @Param("materialLotIdList") List<String> materialLotIdList);

    /**
     * 根据登录人 id 去 iam_user 表查name
     * @param userId
     * @return
     */
    String queryLoginName(@Param("userId")Long userId);

}
