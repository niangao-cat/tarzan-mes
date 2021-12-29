package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;
import com.ruike.wms.domain.vo.WmsMaterialLotExtendAttrVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.inventory.domain.vo.MtMaterialLotVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.modeling.domain.entity.MtModSite;

import java.util.List;

/**
 * @Classname WmsMaterialLotService
 * @Description TODO
 * @Date 2019/9/16 14:54
 * @Created by admin
 */
public interface WmsMaterialLotService {

    /**
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @Description 创建条码
     * @Date 2019/9/16 14:57
     * @Created by admin
     */
    String materialLotCreate(Long tenantId, WmsMaterialLotAddDTO dto);

    /**
     * @param tenantId
     * @return MtModSite
     * @Description 获取工厂
     * @Date 2020/04/07
     * @Created by jiangling.zheng
     */
    MtModSite siteBasicPropertyGet(Long tenantId);

    /**
     * @param materialLotQryDTO
     * @return java.util.List<com.ruike.wms.api.dto.ZMaterialLotQryResultDTO>
     * @Description 条件查询条码
     * @Date 2019/9/18 16:41
     * @Created by admin
     */
    Page<WmsMaterialLotQryResultDTO> selectBarCodeCondition(PageRequest pageRequest, WmsMaterialLotQryDTO materialLotQryDTO, Long tenantId);

    /***
     * @Description 条码历史查询
     * @param pageRequest
     * @param materialLotQryDTO
     * @param tenantId
     * @return io.choerodon.core.domain.Page<com.ruike.wms.api.dto.ZMaterialLotHisResultDTO>
     * @Date 2019/9/19 9:50
     * @Created by admin
     */
    Page<WmsMaterialLotHisResultDTO> selectBarCodeHis(PageRequest pageRequest, WmsMaterialLotHisQryDTO materialLotQryDTO, Long tenantId);

    /**
     * @param materialLotIds
     * @return java.util.List<com.ruike.wms.domain.entity.MaterialLotPntVO>
     * @Description 查询打印数据, 生成PDF
     * @Date 2019/11/25 18:55
     * @Author weihua.liao
     */
    String queryPrintData(Long tenantId, List<String> materialLotIds);

    /**
     * @param materialLotIds
     * @return java.util.List<com.ruike.wms.domain.entity.MaterialLotPntVO>
     * @Description 查询打印数据
     * @Date 2019/11/25 18:55
     * @Author weihua.liao
     */
    String queryPrintData2(Long tenantId, List<String> materialLotIds);

    /**
     * 打印
     *
     * @param tenantId
     */
    String print(Long tenantId, List<String> materialLotIds);

    /**
     * 查询带拓展字段的列表
     *
     * @param tenantId       租户
     * @param extendAttrList 拓展属性列表
     * @return java.util.List<com.ruike.wms.domain.vo.WmsMaterialLotAttrVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/26 05:16:23
     */
    List<WmsMaterialLotAttrVO> batchSaveExtendAttr(Long tenantId, List<WmsMaterialLotExtendAttrVO> extendAttrList);

    /**
     * 条码历史数据导出
     *
     * @param tenantId
     * @param dto
     * @return
     * @author yapeng.yao@hand-china.com 2020/10/15 14:32:23
     */
    List<WmsMaterialLotHisExportResultDTO> barCodeHisExport(Long tenantId, WmsMaterialLotHisQryDTO dto);

    /**
     * 条码数据导出
     *
     * @param tenantId
     * @param dto
     * @return
     * @author yapeng.yao@hand-china.com 2020/10/15 14:32:23
     */
    List<WmsMaterialLotQryExportResultDTO> barCodeQueryExport(Long tenantId, WmsMaterialLotQryDTO dto);


    MtMaterialLotVO13 materialLotUpdate(Long tenantId, MtMaterialLotVO2 dto, String fullUpdate);

    /**
     * 更新物料批拓展字段状态
     *
     * @param tenantId      租户
     * @param eventId       事件
     * @param materialLotId 物料批
     * @param status        状态
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 12:37:32
     */
    void updateMaterialLotStatus(Long tenantId, String eventId, String materialLotId, String status);

    /**
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @Description 条码新建
     * @Date 2019/9/16 14:57
     * @Created by zhangli
     */
    String materialLotNew(Long tenantId, WmsMaterialLotAddDTO dto);
}
