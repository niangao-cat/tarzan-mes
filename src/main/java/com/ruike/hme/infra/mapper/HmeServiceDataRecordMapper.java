package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeServiceDataRecord;
import com.ruike.hme.domain.vo.HmeServiceDataRecordVO;
import com.ruike.hme.domain.vo.HmeServiceDataRecordVO2;
import com.ruike.hme.domain.vo.HmeServiceDataRecordVO3;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 售后返品信息采集确认表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2020-09-03 15:20:59
 */
public interface HmeServiceDataRecordMapper extends BaseMapper<HmeServiceDataRecord> {


    /**
     * 查询返修码信息
     *
     * @param tenantId      租户id
     * @param siteId        站点
     * @param snNum         条码
     * @author sanfeng.zhang@hand-china.com 2020/9/3 16:41
     * @return com.ruike.hme.domain.vo.HmeServiceDataRecordVO
     */
    HmeServiceDataRecordVO queryRecordInfo(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("snNum") String snNum);

    /**
     * 数据采集组
     *
     * @param tenantId      租户id
     * @param operationId   工艺
     * @author sanfeng.zhang@hand-china.com 2020/9/3 19:34
     * @return java.util.List<com.ruike.hme.domain.vo.HmeServiceDataRecordVO2>
     */
    List<HmeServiceDataRecordVO2> queryTagInfo(@Param("tenantId") Long tenantId, @Param("operationId") String operationId);

    /**
     * 获取收集项信息
     *
     * @param tenantId                  租户id
     * @param ServiceDataRecordId       返品信息id
     * @author sanfeng.zhang@hand-china.com 2020/9/4 9:32
     * @return java.util.List<com.ruike.hme.domain.vo.HmeServiceDataRecordVO3>
     */
    List<HmeServiceDataRecordVO3> queryRecordLineList(@Param("tenantId") Long tenantId, @Param("serviceDataRecordId") String ServiceDataRecordId);
}
