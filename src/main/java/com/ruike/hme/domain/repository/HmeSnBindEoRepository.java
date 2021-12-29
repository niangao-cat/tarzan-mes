package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeQualification;
import com.ruike.hme.domain.vo.HmeEoVO3;
import com.ruike.hme.domain.vo.HmeSnBindEoVO2;
import com.ruike.hme.domain.vo.HmeSnBindEoVO3;
import org.hzero.core.base.AopProxy;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.method.domain.entity.*;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;

import java.util.List;

/**
 * 基于工单生成SN绑定EO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/06 20:34
 */
public interface HmeSnBindEoRepository extends AopProxy<HmeWorkOrderManagementRepository> {

    /**
     * 基于工单生成SN绑定EO
     *
     * @param mtEo              eo信息
     * @param mtWorkOrder       生产指令
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/20 19:35
     * @return void
     */
    void createSnBindEo(List<MtEo> mtEo, MtWorkOrder mtWorkOrder);


    /**
     * 获取生产线简码
     *
     * @param tenantId              租户Id
     * @param productionLineIdList      产线Id
     * @param workOrderId           生产指令Id
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/20 19:41
     * @return java.lang.String
     */
    List<HmeSnBindEoVO2> productionLineAttrValueGet(Long tenantId, List<String> productionLineIdList,String workOrderId);


    /**
     * 根据站点id获取扩展表的站点简码
     *
     * @param tenantId          租户id
     * @param siteIdList            站点Id
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/20 19:44
     * @return java.lang.String
     */
    List<HmeSnBindEoVO2> modSiteAttrValueGet(Long tenantId, List<String> siteIdList);


    /**
     * 获取当前工单物料的产品类型
     *
     * @param tenantId          租户id
     * @param siteIdList            站点id
     * @param workOrderId       生产指令id
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/20 19:45
     * @return java.lang.String
     */
    List<HmeSnBindEoVO2> proItemTypeGet(Long tenantId,List<String> siteIdList,String workOrderId);


    /**
     * 查询当前EO所处工序
     *
     * @param tenantId          租户id
     * @param eoId              eoId
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/20 19:46
     * @return java.lang.String
     */
    String eoWorkcellIdDescQuery(Long tenantId,String eoId);

    /**
     * 查询当前EO所处工序
     *
     * @param tenantId          租户id
     * @param eoId              eoId
     * @author penglin.sui@hand-china.com 2020/8/15 14:44
     * @return java.lang.String
     */
    String eoWorkcellIdDescQuery2(Long tenantId,String eoId);

    /**
     * 根据eoId查找bom列表
     *
     * @param tenantId      租户id
     * @param eoIdList      eoId列表
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/20 16:08
     * @return java.util.List<java.lang.String>
     */
    List<MtBom> queryBomListByEoIds(Long tenantId, List<String> eoIdList);

    /**
     *  根据名称查找工艺
     *
     * @param tenantId          租户id
     * @param operationName     工艺名称
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/21 16:31
     * @return java.util.List<tarzan.method.domain.entity.MtOperation>
     */
    List<MtOperation> queryOperationIdByName(Long tenantId,String operationName);
    
    /** 
     * 根据名称查找资质
     * 
     * @param tenantId
     * @param qualityName
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/21 16:39
     * @return java.util.List<com.ruike.hme.domain.entity.HmeQualification>
     */
    List<HmeQualification> queryQualificationByName(Long tenantId,String qualityName);

    /**
     * 获取处置组id
     *
     * @param tenantId              租户id
     * @param dispositionGroup      编码
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/21 20:44
     * @return java.util.List<tarzan.method.domain.entity.MtDispositionGroup>
     */
    List<MtDispositionGroup> queryDispositionGroupIdByDesc(Long tenantId,String dispositionGroup);

    /**
     * 描述获取不良代码
     *
     * @param tenantId          租户Id
     * @param ncCodeDesc        编码
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/21 21:11
     * @return java.util.List<tarzan.method.domain.entity.MtNcCode>
     */
    List<MtNcCode> queryNcCodeInfo(Long tenantId,String ncCodeDesc);

    /**
     * 描述获取不良代码组
     *
     * @param tenantId          租户Id
     * @param ncGroupDesc       编码
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/21 21:18
     * @return java.util.List<tarzan.method.domain.entity.MtNcGroup>
     */
    List<MtNcGroup> queryNcGroupInfo(Long tenantId,String ncGroupDesc);

    /**
     * 单位编码获取对象
     * 
     * @param tenantId      租户id
     * @param uomCode       单位编码
     * @author sanfeng.zhang@hand-china.com 2020/8/4 15:30 
     * @return tarzan.material.domain.entity.MtUom
     */
    MtUom queryMtUomByUomCode(Long tenantId,String uomCode);

    /**
     * 编码查询物料
     *
     * @param tenantId          租户id
     * @param materialCode      编码
     * @author sanfeng.zhang@hand-china.com 2020/8/5 9:58
     * @return tarzan.material.domain.entity.MtMaterial
     */
    MtMaterial queryOneMaterialByCode(Long tenantId,String materialCode);

    /**
     * 名称查询工艺路线
     *
     * @param tenantId      租户id
     * @param routerName    工艺路线
     * @author sanfeng.zhang@hand-china.com 2020/8/5 10:11
     * @return tarzan.method.domain.entity.MtRouter
     */
    MtRouter queryOneRouterByName(Long tenantId,String routerName);

    /**
     * 名称查询工艺路线步骤
     *
     * @param tenantId         租户id
     * @param stepName         工艺路线步骤
     * @author sanfeng.zhang@hand-china.com 2020/8/5 10:30
     * @return tarzan.method.domain.entity.MtRouterStep
     */
    MtRouterStep queryOneRouterStepByName(Long tenantId,String stepName);

    /**
     * 查询工序组件数
     *
     * @param tenantId         租户id
     * @param workOrderId      工单
     * @author sanfeng.zhang@hand-china.com 2020/8/11 20:20
     * @return java.lang.Integer
     */
    Integer queryOperationComponentCount(Long tenantId, String workOrderId);

    /**
     * 查询bom组件数
     *
     * @param tenantId         租户id
     * @param workOrderId      工单
     * @author sanfeng.zhang@hand-china.com 2020/8/11 20:21
     * @return java.lang.Integer
     */
    Integer queryBomComponentCount(Long tenantId, String workOrderId);

    String handleMonth(int month);

    /**
     * 查询工单的销售订单
     *
     * @param tenantId
     * @param workOrderIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO3>
     * @author sanfeng.zhang@hand-china.com 2020/12/7 16:19
     */
    List<HmeEoVO3> batchQuerySoNum(Long tenantId, List<String> workOrderIdList);

    /**
     * 取工位最近作业的EO_ID
     *
     * @param tenantId
     * @param processId
     * @param siteId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2020/12/28 15:17
     */
    List<String> queryEoIdByProcessId(Long tenantId, String processId, String siteId);

    /***
     * 工单bom数量
     * @param tenantId
     * @param workOrderIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeSnBindEoVO3>
     * @author sanfeng.zhang@hand-china.com 2021/5/18
     */
    List<HmeSnBindEoVO3> queryBomComponentCountByWorkOrderIds(Long tenantId, List<String> workOrderIdList);
}
