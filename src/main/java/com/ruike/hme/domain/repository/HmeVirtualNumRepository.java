package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeVirtualNumVO;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeVirtualNum;

import java.util.*;

/**
 * 虚拟号基础表资源库
 *
 * @author wenzhang.yu@hand-china.com 2020-09-28 19:47:55
 */
public interface HmeVirtualNumRepository extends BaseRepository<HmeVirtualNum> {

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
    List<HmeVirtualNumVO> queryVirtualNumByBarcode(Long tenantId, List<String> materialLotCodeList);

    /**
     *
     * @Description 根据物料批ID查询最大虚拟号ID对应的工单ID
     *
     * @author yuchao.wang
     * @date 2020/9/29 23:28
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @return String
     *
     */
    HmeVirtualNum queryMaxVirtualNumWoId(Long tenantId, String materialLotId);

    /**
     *
     * @Description 投料更新虚拟号
     *
     * @author yuchao.wang
     * @date 2020/9/30 12:26
     * @param tenantId 租户ID
     * @param eoId eoId
     * @param virtualIdList 虚拟号ID列表
     * @return void
     *
     */
    void batchUpdateVirtualNumForRelease(Long tenantId, String eoId, List<String> virtualIdList);

    /**
     *
     * @Description 投料退回更新虚拟号
     *
     * @author yuchao.wang
     * @date 2020/10/5 19:44
     * @param tenantId 租户ID
     * @param eoId eoId
     * @return void
     *
     */
    void updateVirtualNumForReleaseBack(Long tenantId, String eoId);
}
