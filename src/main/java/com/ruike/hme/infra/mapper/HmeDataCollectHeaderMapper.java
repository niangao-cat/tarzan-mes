package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeDataCollectHeader;
import com.ruike.hme.domain.entity.HmeDataCollectLine;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO2;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO4;
import com.ruike.hme.domain.vo.HmeDataCollectLineVO5;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.List;

/**
 * 生产数据采集头表Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2020-07-16 19:35:58
 */
public interface HmeDataCollectHeaderMapper extends BaseMapper<HmeDataCollectHeader> {

    /**
     * 物料id获取物料类型
     *
     * @param tenantId      租户Id
     * @param defaultSiteId 用户站点Id
     * @param materialId    物料Id
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/16 21:12
     * @return java.lang.String
     */
    String querySnMaterialType(@Param("tenantId") Long tenantId,@Param("defaultSiteId") String defaultSiteId,@Param("materialId") String materialId);

    /**
     * 工位查询工艺信息
     *
     * @param tenantId      租户Id
     * @param defaultSiteId 用户站点Id
     * @param workcellId    工位Id
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/17 9:47
     * @return java.util.List<com.ruike.hme.domain.vo.HmeDataCollectLineVO5>
     */
    List<HmeDataCollectLineVO5> queryOperationIdByWorkcellId(@Param("tenantId") Long tenantId, @Param("defaultSiteId") String defaultSiteId, @Param("workcellId") String workcellId);

    /**
     * 获取表头下行列表信息
     *
     * @param tenantId          租户Id
     * @param collectHeaderId   头表Id
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/17 10:39
     * @return java.util.List<com.ruike.hme.domain.vo.HmeDataCollectLineVO2>
     */
    List<HmeDataCollectLineVO2> queryDataCollectLineList(@Param("tenantId") Long tenantId,@Param("collectHeaderId") String collectHeaderId);

    /**
     *  物料批编码获取物料批信息
     *
     * @param tenantId          租户Id
     * @param materialLotCode   物料批编码
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/17 10:54
     * @return tarzan.inventory.domain.entity.MtMaterialLot
     */
    MtMaterialLot queryMaterialLotInfoByCode(@Param("tenantId") Long tenantId,@Param("materialLotCode") String materialLotCode);

    /**
     * 查询行信息（保存行表用）
     *
     * @param tenantId          租户Id
     * @param operationId       工艺Id
     * @param materialId        物料Id
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/17 11:41
     * @return java.util.List<com.ruike.hme.domain.entity.HmeDataCollectLine>
     */
    List<HmeDataCollectLine> queryDataCollectLineInfoList(@Param("tenantId") Long tenantId,@Param("operationId") String operationId,@Param("materialId") String materialId);

    /**
     * 工位Id获取工段Id
     *
     * @param tenantId          租户Id
     * @param workcellId        工位Id
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/17 16:11
     * @return java.lang.String
     */
    String queryLineWorkcellIdByWorkCellId(@Param("tenantId") Long tenantId,@Param("workcellId") String workcellId);

    /**
     * 生产数据采集-工位扫描
     *
     * @param tenantId          租户id
     * @param workcellCode      工位编码
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/17 17:23
     * @return com.ruike.hme.api.dto.HmeDataCollectLineVO4
     */
    HmeDataCollectLineVO4 workcellCodeScan(@Param("tenantId") Long tenantId, @Param("workcellCode") String workcellCode);

    /**
     *@description 工艺+物料+版本为空
     *@author wenzhang.yu@hand-china.com
     *@date 2020/11/17 14:22
     *@param tenantId
     *@param operationId
     *@param materialId
     *@return java.util.List<com.ruike.hme.domain.entity.HmeDataCollectLine>
     **/
    List<HmeDataCollectLine> queryDataCollectLineInfoListNew1(@Param("tenantId")Long tenantId, @Param("operationId")String operationId, @Param("materialId")String materialId);

    /**
     *@description 工艺+物料类型
     *@author wenzhang.yu@hand-china.com
     *@date 2020/11/17 14:22
     *@param tenantId
     *@param operationId
     *@param materialId
     *@return java.util.List<com.ruike.hme.domain.entity.HmeDataCollectLine>
     **/
    List<HmeDataCollectLine> queryDataCollectLineInfoListNew2(@Param("tenantId")Long tenantId, @Param("operationId")String operationId, @Param("materialId")String materialId,@Param("siteId")String defaultSiteId);

    /**
     *@description 工艺
     *@author wenzhang.yu@hand-china.com
     *@date 2020/11/17 15:28
     *@param tenantId
     *@param operationId
     *@return java.util.List<com.ruike.hme.domain.entity.HmeDataCollectLine>
     **/
    List<HmeDataCollectLine> queryDataCollectLineInfoListNew3(Long tenantId, String operationId);
}
