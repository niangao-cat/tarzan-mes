package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeVirtualNum;
import com.ruike.hme.domain.vo.HmeVirtualNumVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 虚拟号基础表Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-09-28 19:47:55
 */
public interface HmeVirtualNumMapper extends BaseMapper<HmeVirtualNum> {

    /**
     *
     * @Description 查询条码下的虚拟号
     *
     * @author yuchao.wang
     * @date 2020/9/29 23:07
     * @param tenantId 租户ID
     * @param materialLotCodeList 条码号
     * @return java.util.List<com.ruike.hme.domain.vo.HmeVirtualNumVO>
     *
     */
    List<HmeVirtualNumVO> queryVirtualNumByBarcode(@Param("tenantId") Long tenantId,
                                                   @Param("materialLotCodeList") List<String> materialLotCodeList);

    /**
     *
     * @Description 根据物料批ID查询最大虚拟号ID对应的工单ID
     *
     * @author yuchao.wang
     * @date 2020/9/29 23:28
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @return com.ruike.hme.domain.entity.HmeVirtualNum
     *
     */
    HmeVirtualNum queryMaxVirtualNumWoId(@Param("tenantId") Long tenantId,
                                         @Param("materialLotId") String materialLotId);

    /**
     *
     * @Description 投料更新虚拟号
     *
     * @author yuchao.wang
     * @date 2020/9/30 12:26
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param userId 用户ID
     * @param virtualIdList 虚拟号ID列表
     * @return void
     *
     */
    void batchUpdateVirtualNumForRelease(@Param("tenantId") Long tenantId,
                                         @Param(value = "eoId") String eoId,
                                         @Param(value = "userId") Long userId,
                                         @Param(value = "virtualIdList") List<String> virtualIdList);

    /**
     *
     * @Description 投料退回更新虚拟号
     *
     * @author yuchao.wang
     * @date 2020/10/5 19:43
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param userId userId
     * @return void
     *
     */
    void updateVirtualNumForReleaseBack(@Param("tenantId") Long tenantId,
                                        @Param(value = "eoId") String eoId,
                                        @Param(value = "userId") Long userId);
}
