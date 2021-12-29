package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import org.apache.ibatis.annotations.Param;
import tarzan.iface.domain.entity.MtMaterialBasic;

import java.util.List;

/**
 * 芯片转移
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/17 10:32
 */
public interface HmeChipTransferMapper {

    /**
     * 来料信息记录
     *
     * @param tenantId          租户id
     * @param cosRecordId       记录id
     * @author sanfeng.zhang@hand-china.com 2020/8/17 13:46
     * @return com.ruike.hme.domain.entity.HmeIncomingRecord
     */
    HmeCosOperationRecord queryHmeIncomingRecord(@Param("tenantId") Long tenantId, @Param("cosRecordId") String cosRecordId);


    /**
     * 物料业务属性关系
     * 
     * @param tenantId          租户id
     * @param materialId        物料id
     * @author sanfeng.zhang@hand-china.com 2020/8/27 14:51
     * @return java.util.List<tarzan.iface.domain.entity.MtMaterialBasic>
     */
    List<MtMaterialBasic> queryMtMaterialBasicInfo(@Param("tenantId") Long tenantId, @Param("materialId") String materialId);

    /**
     * 查询不良的装载信息
     *
     * @param tenantId              租户id
     * @param materialLotId         物料批
     * @author sanfeng.zhang@hand-china.com 2020/9/24 15:29
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     */
    List<HmeMaterialLotLoad> queryNgMaterialLotLoad(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 批量更新装载信息
     *
     * @param tenantId
     * @param userId
     * @param materialLotLoadList
     * @author sanfeng.zhang@hand-china.com 2020/9/25 13:51
     * @return void
     */
    void batchUpdateHmeMaterialLotLoad(@Param("tenantId") Long tenantId, @Param("userId") Long userId, @Param("materialLotLoadList") List<HmeMaterialLotLoad> materialLotLoadList);

    /**
     * 查询数据组
     * 
     * @param tenantId
     * @param materialId
     * @param siteId
     * @author sanfeng.zhang@hand-china.com 2020/11/24 14:52 
     * @return java.lang.String
     */
    String queryItemGroupByMaterialIdSite(@Param("tenantId") Long tenantId, @Param("materialId") String materialId, @Param("siteId") String siteId);
}
