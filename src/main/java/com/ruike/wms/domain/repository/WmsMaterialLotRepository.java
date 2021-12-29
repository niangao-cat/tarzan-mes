package com.ruike.wms.domain.repository;

import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;
import com.ruike.wms.domain.vo.WmsMaterialLotExtendAttrVO;
import org.apache.ibatis.annotations.Param;
import tarzan.general.api.dto.MtTagGroupObjectDTO3;

import java.util.List;

/**
 * 物料批 资源库
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 17:18
 */
public interface WmsMaterialLotRepository {
    /**
     * 查询带拓展字段的列表
     *
     * @param tenantId       租户
     * @param materialLotIds 物料批
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialLotAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 05:16:23
     */
    List<WmsMaterialLotAttrVO> selectListWithAttrByIds(Long tenantId, List<String> materialLotIds);

    /**
     * 查询带拓展字段的列表
     *
     * @param tenantId      租户
     * @param materialLotId 物料批
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialLotAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 05:16:23
     */
    WmsMaterialLotAttrVO selectListWithAttrById(Long tenantId, String materialLotId);

    /**
     * 查询带拓展字段的列表
     *
     * @param tenantId        租户
     * @param materialLotCode 物料批
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialLotAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 05:16:23
     */
    WmsMaterialLotAttrVO selectWithAttrByCode(Long tenantId, String materialLotCode);

    /**
     * 批量保存物料批的拓展属性
     *
     * @param tenantId       租户
     * @param eventId        事件ID
     * @param extendAttrList 拓展属性列表
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialLotAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 05:16:23
     */
    List<WmsMaterialLotAttrVO> batchSaveExtendAttr(Long tenantId, String eventId, List<WmsMaterialLotExtendAttrVO> extendAttrList);

    /**
     * 批量获取生产版本信息
     *
     * @param tenantId
     * @param siteId
     * @param mtMaterialIdList
     * @param materialVersionList
     * @return java.util.List<tarzan.general.api.dto.MtTagGroupObjectDTO3>
     * @author sanfeng.zhang@hand-china.com 2020/9/27 10:20
     */
    List<MtTagGroupObjectDTO3> batchProductionVersionQuery(@Param(value = "tenantId") Long tenantId, @Param("siteId") String siteId, @Param("mtMaterialIdList") List<String> mtMaterialIdList, @Param("materialVersionList") List<String> materialVersionList);
}
