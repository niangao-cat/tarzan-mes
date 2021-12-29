package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.HmeBomComponentTrxVO;
import com.ruike.hme.domain.vo.HmeRouterOperationVO;
import com.ruike.hme.domain.vo.HmeWorkOrderVO58;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import org.hzero.core.base.AopProxy;
import tarzan.method.domain.entity.MtBom;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.vo.MtBomHisVO1;
import tarzan.method.domain.vo.MtBomHisVO4;
import tarzan.method.domain.vo.MtBomVO2;
import tarzan.method.domain.vo.MtRouterVO1;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.order.domain.vo.*;

import java.util.List;

/**
 * 工单管理资源库
 *
 * @author jiangling.zheng@hand-china.com 2020-03-30 13:36:08
 */
public interface HmeWorkOrderManagementRepository extends AopProxy<HmeWorkOrderManagementRepository> {

    /**
     * 获取事业部/车间说明
     *
     * @param tenantId
     * @param siteId
     * @param organizationId
     * @param queryType
     * @return
     */
    MtModArea mtModAreaGet(Long tenantId, String siteId, String organizationId, String queryType);

    /**
     * 获取类型说明
     *
     * @param tenantId
     * @param typeGroup
     * @param typeCode
     * @return
     */
    String genTypeDescGet(Long tenantId, String typeGroup, String typeCode);

    /**
     * 获取扩展属性值
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtExtendAttrDTO3 woAttrValueQuery(Long tenantId, MtWorkOrderAttrVO2 dto);

    /**
     * 封装查询结果集
     *
     * @param tenantId
     * @param siteId
     * @param wo
     * @return
     */
    HmeWorkOrderVO58 woLimitGet(Long tenantId, String siteId, HmeWorkOrderVO58 wo);

    /**
     * EO创建
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     * @author jiangling.zheng@hand-china.com 2020/10/13 10:50
     */

    List<String> eoCreateForUi(Long tenantId, MtWorkOrderVO50 dto);

    /**
     * EO创建
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     * @author jiangling.zheng@hand-china.com 2020/10/13 17:16
     */

    List<String> woLimitEoBatchCreate(Long tenantId, MtEoVO14 dto);

    /**
     * woLimitEoCreate-根据生产指令生成执行作业
     * API优化
     *
     * @param tenantId
     * @param vo
     * @return java.lang.String
     * @author jiangling.zheng@hand-china.com 2020/10/13 17:18
     */
    String woLimitEoCreate(Long tenantId, MtEoVO6 vo);

    /**
     * eoBatchUpdate-批量更新执行作业属性
     * API优化
     *
     * @param tenantId
     * @param dto
     * @param fullUpdate
     * @return java.util.List<tarzan.order.domain.vo.MtEoVO29>
     * @author jiangling.zheng@hand-china.com 2020/10/13 17:24
     */

    List<MtEoVO29> eoBatchUpdate(Long tenantId, MtEoVO39 dto, String fullUpdate);

    /**
     * bomCopy-复制装配清单
     * API优化
     *
     * @param tenantId
     * @param condition
     * @return java.lang.String
     * @author jiangling.zheng@hand-china.com 2020/10/13 20:11
     */
    String bomCopy(Long tenantId, MtBomVO2 condition);

    /**
     * routerCopy-工艺路线复制
     *
     * @param tenantId
     * @param condition
     * @return java.lang.String
     * @author jiangling.zheng@hand-china.com 2020/10/13 21:10
     */
    String routerCopy(Long tenantId, MtRouterVO1 condition);

    /**
     * eoBomBatchUpdate-批量更新执行作业清单
     * API优化
     *
     * @param tenantId
     * @param dto
     * @return void
     * @author jiangling.zheng@hand-china.com 2020/10/14 9:41
     */

    void eoBomBatchUpdate(Long tenantId, MtEoBomVO3 dto);

    /**
     * bomPropertyUpdate-新增更新装配清单基础属性
     * API优化
     *
     * @param tenantId
     * @param bomList
     * @param fullUpdate
     * @return java.lang.String
     * @author jiangling.zheng@hand-china.com 2020/10/15 17:55
     */
    List<String> bomBatchPropertyUpdate(Long tenantId, List<MtBom> bomList, String fullUpdate);

    /**
     * bomAllHisCreate-创建装配清单历史
     * API优化
     *
     * @param tenantId
     * @param dto
     * @return tarzan.method.domain.vo.MtBomHisVO4
     * @author jiangling.zheng@hand-china.com 2020/10/19 11:20
     */

    MtBomHisVO4 bomAllHisCreate(Long tenantId, MtBomHisVO1 dto);

    /**
     * 查询bom物料下物料的列表
     *
     * @param tenantId    租户
     * @param workOrderId 工单
     * @param materialId  物料
     * @return java.util.List<tarzan.method.domain.entity.MtBomComponent>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/4 11:29:58
     */
    List<MtBomComponent> bomMaterialListGet(Long tenantId, String workOrderId, String materialId);

    /**
     * 查询工单下的对应物料的bom行信息
     *
     * @param tenantId       租户
     * @param workOrderId    工单
     * @param materialIdList 物料列表
     * @return java.util.List<com.ruike.hme.domain.vo.HmeBomComponentTrxVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/4 04:47:08
     */
    List<HmeBomComponentTrxVO> bomMaterialTrxListGet(Long tenantId, String workOrderId, List<String> materialIdList);

    /**
     * 根据工单查询工序名称
     *
     * @param tenantId    租户
     * @param workOrderId 工单
     * @return java.lang.String
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/5 02:35:26
     */
    HmeRouterOperationVO routerStepGetByWo(Long tenantId, String workOrderId);
}
