package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeContainerCapacity;
import com.ruike.hme.domain.vo.HmeContainerCapacityVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 容器容量表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-10 15:08:58
 */
public interface HmeContainerCapacityMapper extends BaseMapper<HmeContainerCapacity> {

    /**
     * 容器容量列表
     *
     * @param tenantId                  租户id
     * @param containerTypeId           容器类型id
     * @author sanfeng.zhang@hand-china.com 2020/8/10 15:52
     * @return java.util.List<com.ruike.hme.domain.vo.HmeContainerCapacityVO>
     */
    List<HmeContainerCapacityVO> containerCapacityQuery(@Param("tenantId") Long tenantId, @Param("containerTypeId") String containerTypeId);

    /**
     *
     * @Description 取片平台查询容器信息
     *
     * @author yuchao.wang
     * @date 2020/8/25 15:40
     * @param tenantId 租户ID
     * @param containerTypeCode 容器类型
     * @param cosType COS类型
     * @param operationId 工艺ID
     * @return com.ruike.hme.domain.entity.HmeContainerCapacity
     *
     */
    HmeContainerCapacity queryContainerCapacityForGetChip(@Param("tenantId") Long tenantId,
                                                          @Param("containerTypeCode") String containerTypeCode,
                                                          @Param("cosType") String cosType,
                                                          @Param("operationId") String operationId);
}
