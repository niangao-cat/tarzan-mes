package com.ruike.hme.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import com.ruike.hme.domain.vo.*;

import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.order.domain.vo.MtEoVO20;
import tarzan.order.domain.vo.MtWorkOrderVO8;

/**
 * 工序作业平台-产品SN批次投料资源库
 *
 * @author liyuan.lv@hand-china.com 2020-03-21 17:55:01
 */
public interface HmeEoJobSnLotMaterialRepository
                extends BaseRepository<HmeEoJobSnLotMaterial>, AopProxy<HmeEoJobSnLotMaterialRepository> {

    /**
     * 批次时效物料出站
     *
     * @param tenantId 租户ID
     * @param dto 数据
     * @param lotDto 批次列表
     * @param timeDto 时效物料列表
     */
    void lotMaterialOutSite(Long tenantId, HmeEoJobSnVO3 dto, List<HmeEoJobLotMaterialVO> lotDto,
                    List<HmeEoJobTimeMaterialVO> timeDto);

    /**
     * 批次时效物料出站
     *
     * @param tenantId 租户ID
     * @param dto 数据
     */
    HmeEoJobSnVO3 lotMaterialOutSite2(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 预装批次时效物料出站
     *
     * @param tenantId 租户ID
     * @param dto 数据
     */
    HmeEoJobSnVO3 prepareLotMaterialOutSite2(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 反冲物料出站
     *
     * @param tenantId 租户ID
     * @param dto 工序作业参数
     * @author penglin.sui@hand-china.com 2020.09.27 16:54
     */
    void backFlushMaterialOutSite(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 批量获取物料批扩展属性
     *
     * @author chuang.yang
     * @date 2020/10/21
     * @param tenantId
     * @param materialLotIds
     */
    List<HmeEoJobSnMaterialLotAttrVO> getMaterialLotAttrBatch(Long tenantId, List<String> materialLotIds);

    /**
     * 批量获取组件扩展属性
     *
     * @author chuang.yang
     * @date 2020/10/21
     * @param tenantId
     * @param bomComponentIds
     */
    List<HmeEoJobSnBomCompAttrVO> getBomComponentAttrBatch(Long tenantId, List<String> bomComponentIds);

    /**
     * 预装反冲物料出站
     *
     * @param tenantId 租户ID
     * @param dto 工序作业参数
     * @author penglin.sui@hand-china.com 2020.09.27 21:11
     */
    void prepareBackFlushMaterialOutSite(Long tenantId, HmeEoJobSnVO3 dto);

    /**
     * 批次物料投料出站
     *
     * @param tenantId 租户ID
     * @param jobId 工序作业ID
     * @param eoId 执行作业ID
     * @param dtoList 批次物料投料参数
     * @author liyuan.lv@hand-china.com 20.7.16 05:20:36
     */
    void eoJobLotMaterialRelease(Long tenantId, String jobId, String eoId, List<HmeEoJobLotMaterialVO> dtoList);

    /**
     * 时效物料投料出站
     *
     * @param tenantId 租户ID
     * @param jobId 工序作业ID
     * @param eoId 执行作业ID
     * @param dtoList 时效物料投料参数
     * @author liyuan.lv@hand-china.com 20.7.16 05:19:33
     */
    void eoJobTimeMaterialRelease(Long tenantId, String jobId, String eoId, List<HmeEoJobTimeMaterialVO> dtoList);

    /**
     * 批次物料投料出站
     *
     * @param tenantId 租户ID
     * @param jobId 工序作业ID
     * @param eoId 执行作业ID
     * @param dtoList 批次物料投料参数
     * @author liyuan.lv@hand-china.com 20.7.16 05:20:36
     */
    void eoJobLotMaterialRelease2(Long tenantId, String jobId, String eoId, List<HmeEoJobLotMaterialVO> dtoList);

    /**
     * 时效物料投料出站
     *
     * @param tenantId 租户ID
     * @param jobId 工序作业ID
     * @param eoId 执行作业ID
     * @param dtoList 时效物料投料参数
     * @author liyuan.lv@hand-china.com 20.7.16 05:19:33
     */
    void eoJobTimeMaterialRelease2(Long tenantId, String jobId, String eoId, List<HmeEoJobTimeMaterialVO> dtoList);

    /**
     * 校验物料是否有替代关系
     *
     * @param tenantId 租户ID
     * @param materialId 条码物料ID
     * @param dto 绑定条码传入参数
     * @param woComponentList wo组件清单
     * @param eoComponentList eo组件清单
     * @author penglin.sui@hand-china.com 2020.9.25 14:38
     */
    Boolean checkSubstituteRelExists(Long tenantId, String materialId, HmeEoJobMaterialVO dto,
                    List<MtWorkOrderVO8> woComponentList, List<MtEoVO20> eoComponentList);

    /**
     * 获取物料替代料
     *
     * @param tenantId 租户ID
     * @param materialId 条码物料ID
     * @param dto 绑定条码传入参数
     * @param woComponentList wo组件清单
     * @param eoComponentList eo组件清单
     * @author penglin.sui@hand-china.com 2020.10.14 20:28
     */
    List<String> querySubstituteMaterial(Long tenantId, String materialId, HmeEoJobMaterialVO dto,
                    List<MtWorkOrderVO8> woComponentList, List<MtEoVO20> eoComponentList);

    /**
     * 库位校验
     *
     * @param tenantId 租户ID
     * @param locatorId 库位ID
     * @param workcellId 工位ID
     * @author penglin.sui@hand-china.com 2020.9.25 14:38
     */
    void CheckLocator(Long tenantId, String locatorId, String workcellId);

    /**
     * 查询库位
     *
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @author penglin.sui@hand-china.com 2020.9.29 14:38
     */
    MtModLocator selectLocator(Long tenantId, String locatorType, String workcellId);

    /**
     * 单位校验
     *
     * @param tenantId 租户ID
     * @param primaryUomId 单位ID
     * @param materialId 物料ID
     * @author penglin.sui@hand-china.com 2020.10.01 16:23
     */
    void CheckUom(Long tenantId, String primaryUomId, String materialId);

    /**
     * 更新该条码与工位绑定关系的cost_qty
     *
     * @param tenantId 租户ID
     * @param workcellId 工位ID
     * @param jobId 工序作业ID
     * @author penglin.sui@hand-china.com 2020-10-08 15:18
     */
    void lotTimeMaterialInSite(Long tenantId, String workcellId, String jobId);

    /**
     * 销售订单校验
     *
     * @param tenantId 租户ID
     * @param workOrderId 工单ID
     * @param materialLotId 条码ID
     * @param bomComponentId 组件ID
     * @author penglin.sui@hand-china.com 2020.10.10 16:27
     */
    void CheckSoNum(Long tenantId, String workOrderId, String materialLotId, String bomComponentId);

    /**
     * 获取产线是否做反冲料投料标识
     *
     * @param tenantId 租户ID
     * @param prodLineId 产线ID
     * @author penglin.sui@hand-china.com 2020.10.20 23:31
     */
    String selectBackFlushReleaseFlag(Long tenantId, String prodLineId);

    /**
     * 条码扩展属性校验
     *
     * @param tenantId 租户ID
     * @param materialLotId 条码ID
     * @author penglin.sui@hand-china.com 2020.10.26 15:23
     */
    void CheckMaterialAttr(Long tenantId, String materialLotId);

    /**
     *
     * @Description 批量新增
     *
     * @author yuchao.wang
     * @date 2020/12/25 15:33
     * @param insertList 新增数据列表
     * @return void
     *
     */
    void myBatchInsert(List<HmeEoJobSnLotMaterial> insertList);

}
