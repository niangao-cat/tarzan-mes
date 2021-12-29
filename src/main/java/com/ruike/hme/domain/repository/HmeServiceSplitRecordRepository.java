package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO3;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO4;
import com.ruike.hme.domain.entity.HmeServiceSplitRecord;
import com.ruike.hme.domain.vo.HmeServiceSplitBomHeaderVO;
import com.ruike.hme.domain.vo.HmeServiceSplitBomLineVO;
import com.ruike.hme.domain.vo.HmeServiceSplitRecordVO;
import com.ruike.hme.domain.vo.HmeServiceSplitReturnCheckVO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;

import java.util.List;

/**
 * 售后返品拆机表资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 14:21:01
 */
public interface HmeServiceSplitRecordRepository extends BaseRepository<HmeServiceSplitRecord>, AopProxy<HmeServiceSplitRecordRepository> {

    /**
     * 查询返修序列号信息
     *
     * @param tenantId
     * @param snNum
     * @return java.util.List<com.ruike.hme.api.dto.HmeServiceSplitRecordDTO>
     * @author jiangling.zheng@hand-china.com 2020/9/8 17:27
     */
    HmeServiceSplitRecordDTO splitRecordBySnQuery(Long tenantId, String snNum);

    /**
     * 新增整机拆机信息
     *
     * @param tenantId
     * @param vo
     * @return java.lang.String
     * @author jiangling.zheng@hand-china.com 2020/9/9 14:15
     */
    HmeServiceSplitRecordDTO3 splitRecordInsert(Long tenantId, HmeServiceSplitRecordVO vo);

    /**
     * 拆机条码新增
     *
     * @param tenantId
     * @param materialId
     * @param vo
     * @param locatorId
     * @return tarzan.inventory.domain.entity.MtMaterialLot
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/10 11:40:24
     */
    MtMaterialLot splitMaterialLotCreate(Long tenantId, String materialId, HmeServiceSplitRecordVO vo, String locatorId);

    /**
     * 调用SAP接口创建工单
     *
     * @param tenantId   租户
     * @param snNum      sn
     * @param materialId 物料ID
     * @param siteId     站点
     * @param locatorId  货位
     * @return java.lang.String
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/29 05:32:52
     */
    String createWorkOrder(Long tenantId, String snNum, String materialId, String siteId, String locatorId);

    /**
     * 新增拆机行信息
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author jiangling.zheng@hand-china.com 2020/9/9 20:00
     */

    HmeServiceSplitRecord splitRecordLineInsert(Long tenantId, HmeServiceSplitRecordDTO3 dto);

    /**
     * 查询组件信息
     *
     * @param tenantId
     * @param materialLotCode
     * @return com.ruike.hme.api.dto.HmeServiceSplitRecordDTO3
     * @author jiangling.zheng@hand-china.com 2020/9/9 16:22
     */
    HmeServiceSplitRecordDTO3 materialLotGet(Long tenantId, String materialLotCode);

    /**
     * 工单创建
     *
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @author jiangling.zheng@hand-china.com 2020/9/9 19:42
     */
    String workOrderUpdate(Long tenantId, HmeServiceSplitRecordDTO3 dto);

    /**
     * 组件回传
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author jiangling.zheng@hand-china.com 2020/9/14 18:58
     */

    HmeServiceSplitRecordDTO3 bomUpdate(Long tenantId, HmeServiceSplitRecordDTO3 dto);

    /**
     * 库存变更
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author jiangling.zheng@hand-china.com 2020/9/15 15:04
     */
    void onhandQtyUpdateProcess(Long tenantId, HmeServiceSplitRecordDTO3 dto, MtMaterialLot materialLot);

    /**
     * 查询物料对应的最新的bom
     *
     * @param tenantId   租户
     * @param siteId     站点
     * @param materialId 物料ID
     * @return tarzan.method.domain.entity.MtBom
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 02:58:16
     */
    HmeServiceSplitBomHeaderVO selectLatestBomByMaterial(Long tenantId,
                                                         String siteId,
                                                         String materialId);

    /**
     * 查询bom行信息
     *
     * @param tenantId 租户
     * @param siteId   站点
     * @param bomId    bom
     * @return java.util.List<com.ruike.hme.domain.vo.HmeServiceSplitBomLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 03:09:17
     */
    List<HmeServiceSplitBomLineVO> selectBomLineList(Long tenantId,
                                                     String siteId,
                                                     String bomId);

    /**
     * 创建物料批
     *
     * @param tenantId 租户
     * @param dto      参数
     * @return tarzan.inventory.domain.vo.MtMaterialLotVO13
     * @author jiangling.zheng@hand-china.com 2020/10/4 18:20
     */
    MtMaterialLot materialLotCreate(Long tenantId, HmeServiceSplitRecordDTO3 dto);

    /**
     * 查询退库检测列表
     *
     * @param tenantId 租户
     * @param snNum    序列号
     * @param allFlag  显示所有标志
     * @return java.util.List<com.ruike.hme.domain.vo.HmeServiceSplitReturnCheckVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/1/5 07:41:45
     */
    List<HmeServiceSplitReturnCheckVO> returnCheckListGet(Long tenantId,
                                                          String snNum,
                                                          String allFlag);
    /**
     * 获取当前用户所属部门生产版本
     *
     * @param tenantId
     * @author sanfeng.zhang@hand-china.com 2021/4/1 14:17
     * @return java.lang.String
     */
    String productionVersionGet(Long tenantId);

    /**
     * 登记撤销时，更新登记表并记录历史
     *
     * @param tenantId 租户ID
     * @param serviceReceiveId 主键
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/25 03:03:44
     * @return void
     */
    void registerCancel(Long tenantId, String serviceReceiveId);
}
