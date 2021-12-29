package tarzan.inventory.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import tarzan.inventory.api.dto.MtContainerDTO3;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.vo.*;

/**
 * 容器，一个具体的容器并记录容器的业务属性，包括容器装载实物所有者、预留对象、位置状态等，提供执行作业、物料批、容器的装载结构资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
public interface MtContainerRepository extends BaseRepository<MtContainer>, AopProxy<MtContainerRepository> {

    /**
     * locatorLimitContainerQuery-获取指定库位下所有的容器
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/3
     */
    List<String> locatorLimitContainerQuery(Long tenantId, MtContainerVO dto);

    /**
     * locatorLimitMaterialLotQuery-获取指定库位下所有的物料批
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/3
     */
    List<String> locatorLimitMaterialLotQuery(Long tenantId, MtContainerVO dto);

    /**
     * containerLoadingObjectValidate-验证对象是否已装载于指定容器
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/3
     */
    void containerLoadingObjectValidate(Long tenantId, MtContainerVO19 dto);

    /**
     * containerStatusUpdateVerify-容器状态变更验证
     *
     * @param tenantId
     * @param containerId
     * @param targetStatus
     * @author sen.luo
     * @date 2019/4/3
     */
    void containerStatusUpdateVerify(Long tenantId, String containerId, String targetStatus);

    /**
     * containerPropertyGet-获取容器属性
     *
     * @param tenantId
     * @param containerId
     * @author sen.luo
     * @date 2019/4/3
     */
    MtContainer containerPropertyGet(Long tenantId, String containerId);

    /**
     * containerAndMaterialLotReserve-容器和容器内物料批预留
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/4/3
     */
    void containerAndMaterialLotReserve(Long tenantId, MtContainerVO8 dto);

    /**
     * containerAndMaterialLotReserveCancel-容器和容器内物料批预留取消
     *
     * @author chuang.yang
     * @date 2019/4/8
     * @param tenantId
     * @param dto
     * @return void
     */
    void containerAndMaterialLotReserveCancel(Long tenantId, MtContainerVO14 dto);

    /**
     * containerPackingLevelVerify-验证容器包装等级
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/3
     */
    void containerPackingLevelVerify(Long tenantId, MtContainerVO3 dto);

    /**
     * containerLimitContainerTypeGet-获取指定容器的容器类型
     *
     * @param tenantId
     * @param containerId
     * @return java.lang.String
     * @author chuang.yang
     * @date 2019/4/3
     */
    String containerLimitContainerTypeGet(Long tenantId, String containerId);

    /**
     * containerLoadVerify-容器装载验证
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/4/3
     */
    void containerLoadVerify(Long tenantId, MtContainerVO9 dto);

    /**
     * containerAvailableValidate-容器可用性验证
     *
     * @param tenantId
     * @param containerId
     * @author sen.luo
     * @date 2019/4/3
     */
    void containerAvailableValidate(Long tenantId, String containerId);

    /**
     * containerMaxLoadWeightExcessVerify-验证容器最大装载重
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/3
     */
    void containerMaxLoadWeightExcessVerify(Long tenantId, MtContainerDTO3 dto);

    /**
     * containerPropertyBatchGet-批量获取容器属性
     *
     * @param tenantId
     * @param containerIds
     * @author guichuan.li
     * @date 2019/4/3
     */
    List<MtContainer> containerPropertyBatchGet(Long tenantId, List<String> containerIds);

    /**
     * containerReserveVerify-容器预留验证
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/4/3
     */
    void containerReserveVerify(Long tenantId, MtContainerVO10 dto);


    /**
     * containerUnload-容器卸载
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/4/3
     */
    void containerUnload(Long tenantId, MtContainerVO25 dto);

    /**
     * containerBatchUnload-容器批量卸载
     *
     * @author chuang.yang
     * @date 2020/11/13
     */
    void containerBatchUnload(Long tenantId, MtContLoadDtlVO30 dto);

    /**
     * containerLoad-容器装载
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/4/3
     */
    void containerLoad(Long tenantId, MtContainerVO24 dto);

    /**
     * containerUpdate-容器更新&新增并记录容器变更历史
     *
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/4/3
     */
    MtContainerVO26 containerUpdate(Long tenantId, MtContainerVO12 dto, String fullUpdate);

    /**
     * containerFirstAvailableLocationGet-获取容器可用位置
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/4
     */
    MtContainerVO5 containerFirstAvailableLocationGet(Long tenantId, MtContainerVO4 dto);

    /**
     * containerMixedAllowVerify-验证容器是否允许混合装载
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/4
     */
    void containerMixedAllowVerify(Long tenantId, MtContainerVO21 dto);

    /**
     * containerCapacityExcessVerify-验证容器容量是否超限
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/4
     */
    void containerCapacityExcessVerify(Long tenantId, MtContainerVO2 dto);

    /**
     * containerLocationIsEmptyValidate-验证容器位置是否为空
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/4
     */
    void containerLocationIsEmptyValidate(Long tenantId, MtContainerVO6 dto);

    /**
     * containerTransfer-容器转移
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/4
     */
    void containerTransfer(Long tenantId, MtContainerVO7 dto);

    /**
     * containerMaterialConsume-容器物料消耗
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/8
     */
    void containerMaterialConsume(Long tenantId, MtContainerVO11 dto);

    void containerEoMaterialConsume(List<MtContLoadDtlVO6> list, MtContainerVO23 dto, Long tenantId);

    /**
     * containerIdentificationUpdate-更新容器标识
     *
     * @author chuang.yang
     * @date 2019/4/8
     * @param tenantId
     * @param dto
     * @return void
     */
    void containerIdentificationUpdate(Long tenantId, MtContainerVO14 dto);

    /**
     * propertyLimitContainerQuery-根据属性限制获取容器
     *
     * @author chuang.yang
     * @date 2019/4/8
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     */
    List<String> propertyLimitContainerQuery(Long tenantId, MtContainerVO13 dto);

    /**
     * containerRelease-容器下达
     *
     * @author chuang.yang
     * @date 2019/4/8
     * @param tenantId
     * @param dto
     * @return void
     */
    void containerRelease(Long tenantId, MtContainerVO14 dto);

    /**
     * containerHold-容器保留
     *
     * @author chuang.yang
     * @date 2019/4/8
     * @param tenantId
     * @param dto
     * @return void
     */
    void containerHold(Long tenantId, MtContainerVO15 dto);

    /**
     * containerHoldCancel-容器保留取消
     *
     * @author chuang.yang
     * @date 2019/4/8
     * @param tenantId
     * @param dto
     * @return void
     */
    void containerHoldCancel(Long tenantId, MtContainerVO16 dto);

    /**
     * containerAbandon-容器废弃
     *
     * @author chuang.yang
     * @date 2019/4/8
     * @param tenantId
     * @param dto
     * @return void
     */
    void containerAbandon(Long tenantId, MtContainerVO14 dto);

    /**
     * containerCopy-复制容器
     *
     * @author chuang.yang
     * @date 2019/4/8
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String containerCopy(Long tenantId, MtContainerVO17 dto);


    /**
     * containerNextCodeGet -获取下一个容器编码
     *
     * @Author lxs
     * @Date 2019/4/8
     * @Params [tenantId, siteId]
     * @Return java.lang.String
     */
    MtNumrangeVO5 containerNextCodeGet(Long tenantId, MtContainerVO33 dto);


    /**
     * objectContainerTransfer-容器内对象转移
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/8
     */
    void objectContainerTransfer(Long tenantId, MtContainerVO18 dto);

    /**
     * containerAllObjectUnload-容器内所有对象卸载
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/4/8
     */
    void containerAllObjectUnload(Long tenantId, MtContainerVO20 dto);

    /**
     * containerIdentify-容器识别
     *
     * @param tenantId
     * @param identification
     * @author guichuan.li
     * @date 2019/4/8
     */
    List<String> containerIdentify(Long tenantId, String identification);

    /**
     * containerLimitAttrQuery-获取容器拓展属性
     *
     * @author chuang.yang
     * @date 2019/4/17
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.container.view.MtContainerAttrVO2>
     */
    List<MtExtendAttrVO> containerLimitAttrQuery(Long tenantId, MtContainerAttrVO1 dto);

    /**
     * containerLimitAttrUpdate-新增&更新容器拓展属性同时记录容器拓展属性历史
     *
     * @author chuang.yang
     * @date 2019/4/17
     * @param tenantId
     * @param dto
     * @return void
     */
    void containerLimitAttrUpdate(Long tenantId, MtContainerAttrVO3 dto);

    /**
     * attrLimitContainerQuery-根据容器拓展属性获取容器
     *
     * @author chuang.yang
     * @date 2019/4/17
     * @param tenantId
     * @param dto
     * @return java.util.List<java.lang.String>
     */
    List<String> attrLimitContainerQuery(Long tenantId, MtContainerAttrVO2 dto);

    /**
     * containerAttrHisQuery-获取容器拓展属性变更历史
     *
     * @author chuang.yang
     * @date 2019/4/17
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.container_attr_his.dto.MtContainerAttrHis>
     */
    List<MtContainerAttrHisVO1> containerAttrHisQuery(Long tenantId, MtContainerAttrHisVO2 dto);

    /**
     * eventLimitContainerAttrHisBatchQuery-根据事件批量获取容器拓展属性变更历史.
     *
     * @author chuang.yang
     * @date 2019/4/17
     * @param tenantId
     * @param eventIds
     * @return java.util.List<hmes.container_attr_his.dto.MtContainerAttrHis>
     */
    List<MtContainerAttrHisVO1> eventLimitContainerAttrHisBatchQuery(Long tenantId, List<String> eventIds);

    /**
     * 根据属性获取容器信息
     * 
     * @Author peng.yuan
     * @Date 2019/10/18 15:34
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.inventory.domain.vo.MtContainerVO28>
     */
    List<MtContainerVO28> propertyLimitContainerPropertyQuery(Long tenantId, MtContainerVO27 dto);

    /**
     * containerBatchUpdate-容器批量更新&新增并记录容器
     *
     * @author chuang.yang
     * @date 2019/10/17
     * @param tenantId
     * @param dto
     * @param fullUpdate
     * @return java.util.List<hmes.container.view.MtContainerVO24>
     */
    List<MtContainerVO26> containerBatchUpdate(Long tenantId, MtContainerVO29 dto, String fullUpdate);

    /**
     * containerBatchLoad-容器装载
     *
     * @param tenantId
     * @param dto
     * @author chuang.yang
     * @date 2019/10/18
     */
    void containerBatchLoad(Long tenantId, MtContainerVO30 dto);

    /**
     * 容器新增&更新扩展表属性
     * 
     * @Author peng.yuan
     * @Date 2019/11/18 10:45
     * @param tenantId :
     * @param mtExtendVO10 :
     * @return void
     */
    void containerAttrPropertyUpdate(Long tenantId, MtExtendVO10 mtExtendVO10);

    /**
     * 批量获取容器编码
     * 
     * @Author peng.yuan
     * @Date 2019/12/3 18:53
     * @param tenantId :
     * @param mtContainerVO34 :
     * @return io.tarzan.common.domain.vo.MtNumrangeVO8
     */
    MtNumrangeVO8 containerBatchCodeGet(Long tenantId, MtContainerVO34 mtContainerVO34);


    /**
     * 批量获取指定对象所在的容器
     * 
     * @Author peng.yuan
     * @Date 2020/2/5 11:37
     * @param tenantId :
     * @param vo35List :
     * @return java.util.List<tarzan.inventory.domain.vo.MtContainerVO36>
     */
    List<MtContainerVO36> objectLimitLoadingContainerBatchQuery(Long tenantId, List<MtContainerVO35> vo35List);

    /**
     * 容器可用性批量验证
     * 
     * @Author peng.yuan
     * @Date 2020/2/6 14:32
     * @param tenantId :
     * @param containerIds :
     * @return void
     */
    void containerAvailableBatchValidate(Long tenantId, List<String> containerIds);

    /**
     * 批量验证容器包装等级
     * 
     * @Author peng.yuan
     * @Date 2020/2/6 14:55
     * @param tenantId :
     * @param voList :
     * @return void
     */
    void containerPackingLevelBatchVerify(Long tenantId, List<MtContLoadDtlVO18> voList);

    /**
     * containerLoadingObjectBatchValidate-批量批量验证对象是否已装载于指定容器
     * 
     * @param tenantId
     * @param dto
     */
    void containerLoadingObjectBatchValidate(Long tenantId, List<MtContainerVO19> dto);


    /**
     * 批量验证容器最大装载重量是否超限
     *
     * @Author peng.yuan
     * @Date 2020/2/10 16:19
     * @param tenantId :
     * @param dtos :
     * @return void
     */
    void containerMaxLoadWeightExcessBatchVerify(Long tenantId, List<MtContainerDTO3> dtos);


    /**
     * containerCapacityExcessBatchVerify-批量验证容器容量是否超限
     * 
     * @param tenantId
     * @param voList
     */
    void containerCapacityExcessBatchVerify(Long tenantId, List<MtContainerVO37> voList);

    /**
     * containerLocationIsEmptyBatchValidate-批量验证容器位置是否为空
     * 
     * @param tenantId
     * @param dto
     */
    void containerLocationIsEmptyBatchValidate(Long tenantId, List<MtContainerVO6> dto);

    /**
     * 批量验证容器是否允许混合装载
     * 
     * @Author peng.yuan
     * @Date 2020/2/10 16:55
     * @param tenantId :
     * @param dtos :
     * @return void
     */
    void containerMixedAllowBatchVerify(Long tenantId, List<MtContainerVO21> dtos);

    /**
     * 容器装载批量验证
     * 
     * @Author peng.yuan
     * @Date 2020/2/12 10:28
     * @param tenantId :
     * @param dtos :
     * @return void
     */
    void containerLoadBatchVerify(Long tenantId, List<MtContainerVO9> dtos);
}
