package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeInStorageMaterialVO;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * 半成品/成品入库扫描 Mapper
 *
 * @author Deng xu 2020/6/2 16:28
 */
public interface HmeFinishProductsInStorageMapper {

    /**
     * 查询物料批中物料信息
     *
     * @param materialLotList 物料批ID集合
     * @return : java.util.List<com.ruike.hme.domain.vo.HmeInStorageMaterialVO>
     */
    List<HmeInStorageMaterialVO> queryMaterialLot(@Param(value = "materialLotList") List<String> materialLotList);

    /**
     * 目标仓库
     *
     * @param tenantId              租户id
     * @param organizationIdList    组织
     * @author sanfeng.zhang@hand-china.com 2020/8/13 14:59
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     */
    List<MtModLocator> queryLocatorList(@Param("tenantId") Long tenantId, @Param("organizationIdList") List<String> organizationIdList);

    /**
     * 仓库扩展信息
     * 
     * @param tenantId        租户id
     * @param locatorId       仓库id
     * @author sanfeng.zhang@hand-china.com 2020/8/13 17:29 
     * @return java.lang.String
     */
    String queryLocatorAttr(@Param("tenantId") Long tenantId, @Param("locatorId") String locatorId);
    
    /**
     * 仓库id
     * 
     * @param tenantId         租户id
     * @param locatorId         仓库id
     * @author sanfeng.zhang@hand-china.com 2020/8/13 20:55 
     * @return java.lang.String
     */
    String queryLocatorCodeByLocatorId(@Param("tenantId") Long tenantId, @Param("locatorId") String locatorId);

    /**
     * 统计条码不良数
     *
     * @param tenantId              租户id
     * @param materialLotId         物料批id
     * @author sanfeng.zhang@hand-china.com 2020/9/22 13:37
     * @return java.lang.Integer
     */
    Integer queryMaterialLotNcCount(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 查询cos_num不为1的装载信息
     *
     * @param tenantId          租户id
     * @param materialLotId     物料批id
     * @author sanfeng.zhang@hand-china.com 2020/9/22 13:51
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     */
    List<HmeMaterialLotLoad> queryMaterialLotLoadList(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);

    /**
     * 工段下货位
     * 
     * @param tenantId          租户id
     * @param workcellId        工位id
     * @author sanfeng.zhang@hand-china.com 2020/9/23 20:22 
     * @return java.util.List<java.lang.String>
     */
    List<String> queryLocatorByWorkCell(@Param("tenantId") Long tenantId, @Param("workcellId") String workcellId);

    /**
     * 查询工序号
     *
     * @param tenantId          租户id
     * @param workOrderId       工单id
     * @param operationId       工艺id
     * @author sanfeng.zhang@hand-china.com 2020/10/2 10:19
     * @return java.util.List<java.lang.String>
     */
    List<String> queryOperationSequence(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId, @Param("operationId") String operationId);

    /**
     * 有权限的目标仓库
     *
     * @param tenantId              租户id
     * @param organizationIdList    组织
     * @author sanfeng.zhang@hand-china.com 2020/8/13 14:59
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     */
    List<MtModLocator> queryLocatorListPermission(@Param("tenantId")Long tenantId, @Param("userId")Long userId, @Param("organizationIdList")List<String> organizationIdList);
}
