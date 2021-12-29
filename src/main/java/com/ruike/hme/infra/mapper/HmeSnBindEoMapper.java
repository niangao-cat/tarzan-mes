package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.entity.HmeQualification;
import com.ruike.hme.domain.vo.HmeEoVO3;
import com.ruike.hme.domain.vo.HmeSnBindEoVO2;
import com.ruike.hme.domain.vo.HmeSnBindEoVO3;
import org.apache.ibatis.annotations.Param;
import tarzan.material.domain.entity.MtUom;
import tarzan.method.domain.entity.*;
import tarzan.modeling.domain.entity.MtModSite;

import java.util.List;

/**
 * 基于工单生成SN绑定EO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/06 20:34
 */
public interface HmeSnBindEoMapper {

    /**
     * 根据siteId获取工厂简码
     * @param siteIdList
     * @param tenantId
     * @return
     */
    List<HmeSnBindEoVO2> modSiteAttrValueGet(@Param("tenantId") Long tenantId, @Param("siteIdList") List<String> siteIdList);

    /**
     * 获取当前工单的产品类型
     * @param tenantId
     * @param siteIdList
     * @param workOrderId
     * @return
     */
    List<HmeSnBindEoVO2> proItemTypeGet(@Param("tenantId") Long tenantId,@Param("siteIdList") List<String> siteIdList,@Param("workOrderId") String workOrderId);

    /**
     * 获取当前工单的生产线简码
     * @param productionLineIdList
     * @param workOrderId
     * @return
     */
    List<HmeSnBindEoVO2> productionLineAttrValueGet(@Param("tenantId") Long tenantId, @Param("productionLineIdList") List<String> productionLineIdList, @Param("workOrderId") String workOrderId);

    /***
     * @Description: EO所在工序
     * @author: sanfeng.zhang
     * @date 2020/7/10 10:34
     * @param tenantId
     * @param eoId
     * @return : java.lang.String
     * @version 1.0
     */
    String eoWorkcellIdDescQuery(@Param("tenantId") Long tenantId,@Param("eoId") String eoId);

    /***
     * @Description: EO所在工序
     * @author: penglin.sui
     * @date 2020/8/15 14:46
     * @param tenantId
     * @param eoId
     * @return : java.lang.String
     * @version 1.0
     */
    String eoWorkcellIdDescQuery2(@Param("tenantId") Long tenantId,@Param("eoId") String eoId);


    /**
     *  根据eoId查找bom列表
     *
     * @param tenantId      租户id
     * @param eoIdList      eoId列表
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/20 16:11
     * @return java.util.List<tarzan.method.domain.entity.MtBom>
     */
    List<MtBom> queryBomListByEoIds(@Param("tenantId") Long tenantId, @Param("eoIdList") List<String> eoIdList);

    /**
     *  根据名称查找工艺
     *
     * @param tenantId          租户id
     * @param operationName     工艺名称
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/21 16:31
     * @return java.util.List<tarzan.method.domain.entity.MtOperation>
     */
    List<MtOperation> queryOperationIdByName(@Param("tenantId") Long tenantId,@Param("operationName") String operationName);

    /**
     * 根据名称查找资质
     *
     * @param tenantId          租户id
     * @param qualityName       资质名称
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/21 16:39
     * @return java.util.List<com.ruike.hme.domain.entity.HmeQualification>
     */
    List<HmeQualification> queryQualificationByName(@Param("tenantId") Long tenantId,@Param("qualityName") String qualityName);

    /**
     * 获取处置组id
     *
     * @param tenantId              租户id
     * @param dispositionGroup      编码
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/21 20:44
     * @return java.util.List<tarzan.method.domain.entity.MtDispositionGroup>
     */
    List<MtDispositionGroup> queryDispositionGroupIdByDesc(@Param("tenantId") Long tenantId,@Param("dispositionGroup") String dispositionGroup);

    /**
     * 描述获取不良代码
     *
     * @param tenantId          租户Id
     * @param ncCodeDesc        编码
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/21 21:11
     * @return java.util.List<tarzan.method.domain.entity.MtNcCode>
     */
    List<MtNcCode> queryNcCodeInfo(@Param("tenantId") Long tenantId,@Param("ncCodeDesc") String ncCodeDesc);

    /**
     * 描述获取不良代码组
     *
     * @param tenantId          租户id
     * @param ncGroupDesc       编码
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/21 21:18
     * @return java.util.List<tarzan.method.domain.entity.MtNcGroup>
     */
    List<MtNcGroup> queryNcGroupInfo(@Param("tenantId") Long tenantId,@Param("ncGroupDesc") String ncGroupDesc);

    /**
     * 插入不良代码多语言
     *
     * @param ncCodeId          不良代码Id
     * @param lang              语言
     * @param description       描述
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/21 21:44
     * @return void
     */
    void insetMtNcCodeTl(@Param("ncCodeId") String ncCodeId,@Param("lang") String lang,@Param("description") String description);

    /**
     *  插入不良代码组多语言
     *
     * @param ncGroupId         不良代码组
     * @param lang              语言
     * @param description       描述
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/21 21:54
     * @return void
     */
    void insetMtNcGroupTl(@Param("ncGroupId") String ncGroupId,@Param("lang") String lang,@Param("description") String description);

    /**
     * 编码查询站点
     *
     * @param tenantId          租户id
     * @param siteCode          编码
     * @author sanfeng.zhang sanfeng.zhang@hand-china.com 2020/7/22 16:54
     * @return java.util.List<tarzan.method.domain.entity.MtModSite>
     */
    List<MtModSite> queryModSiteByCode(@Param("tenantId") Long tenantId,@Param("siteCode") String siteCode);

    /**
     * 单位编码获取对象
     *
     * @param tenantId      租户id
     * @param uomCode       单位编码
     * @author sanfeng.zhang@hand-china.com 2020/8/4 15:31
     * @return tarzan.material.domain.entity.MtUom
     */
    MtUom queryMtUomByUomCode(@Param("tenantId") Long tenantId,@Param("uomCode") String uomCode);

    /**
     * 名称查询工艺路线
     *
     * @param tenantId
     * @param routerName
     * @author sanfeng.zhang@hand-china.com 2020/8/5 10:20
     * @return tarzan.method.domain.entity.MtRouter
     */
    MtRouter queryOneRouterByName(@Param("tenantId") Long tenantId,@Param("routerName") String routerName);

    /**
     * 查询工序组件数
     *
     * @param tenantId         租户id
     * @param workOrderId      工单
     * @author sanfeng.zhang@hand-china.com 2020/8/11 20:20
     * @return java.lang.Integer
     */
    Integer queryOperationComponentCount(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId);

    /**
     * 查询bom组件数
     *
     * @param tenantId         租户id
     * @param workOrderId      工单
     * @author sanfeng.zhang@hand-china.com 2020/8/11 20:21
     * @return java.lang.Integer
     */
    Integer queryBomComponentCount(@Param("tenantId") Long tenantId, @Param("workOrderId") String workOrderId);

    /**
     * 取物料扩展表的扩展字段11作为生产类型
     *
     * @param tenantId
     * @param materialId
     * @param siteId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2020/10/3 15:19
     */
    String queryProductionManager(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("materialId") String materialId);

    /**
     * 查询工单的销售订单
     *
     * @param tenantId
     * @param workOrderIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoVO3>
     * @author sanfeng.zhang@hand-china.com 2020/12/7 16:26
     */
    List<HmeEoVO3> batchQuerySoNum(@Param("tenantId") Long tenantId, @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     * 取工位最近作业的EO_ID
     *
     * @param tenantId
     * @param processId
     * @param siteId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2020/12/28 15:25
     */
    List<String> queryEoIdByProcessId(@Param("tenantId") Long tenantId, @Param("processId") String processId, @Param("siteId") String siteId);

    /**
     * 取物料扩展表的扩展字段18
     *
     * @param tenantId
     * @param materialId
     * @param siteId
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2020/10/3 15:19
     */
    String select(@Param("tenantId")Long tenantId, @Param("materialId")String materialId, @Param("siteId")String siteId);

    /***
     * 工单bom数量
     * @param tenantId
     * @param workOrderIdList
     * @return java.util.List<com.ruike.hme.domain.vo.HmeSnBindEoVO3>
     * @author sanfeng.zhang@hand-china.com 2021/5/18
     */
    List<HmeSnBindEoVO3> queryBomComponentCountByWorkOrderIds(@Param("tenantId")Long tenantId, @Param("workOrderIdList") List<String> workOrderIdList);

    /**
     * 查询机型
     * @param tenantId
     * @param siteId
     * @param materialCode
     * @return java.lang.String
     * @author sanfeng.zhang@hand-china.com 2021/8/12
     */
    String queryModelName(@Param("tenantId") Long tenantId, @Param("siteId") String siteId, @Param("materialCode") String materialCode);
}
