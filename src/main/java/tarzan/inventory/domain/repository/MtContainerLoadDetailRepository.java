package tarzan.inventory.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.inventory.domain.entity.MtContainerLoadDetail;
import tarzan.inventory.domain.vo.*;

/**
 * 容器装载明细，记录具体容器装载的执行作业或物料批或其他容器的情况资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
public interface MtContainerLoadDetailRepository
                extends BaseRepository<MtContainerLoadDetail>, AopProxy<MtContainerLoadDetailRepository> {

    /**
     * containerLimitObjectQuery-获取指定容器下的装载对象
     *
     * @author chuang.yang
     * @date 2019/4/3
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.container_load_detail.view.MtContainerLoadDetailV>
     */
    List<MtContLoadDtlVO6> containerLimitObjectQuery(Long tenantId, MtContLoadDtlVO dto);

    /**
     * containerLimitEoQuery-获取指定容器下的执行作业
     *
     * @author chuang.yang
     * @date 2019/4/3
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.container_load_detail.view.MtConLoadDetailVO1>
     */

    List<MtContLoadDtlVO1> containerLimitEoQuery(Long tenantId, MtContLoadDtlVO10 dto);

    /**
     * containerLimitContainerQuery-获取指定容器已装载的所有容器
     *
     * @author chuang.yang
     * @date 2019/4/3
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.container_load_detail.view.MtContainerLoadDetailVO3>
     */
    List<MtContLoadDtlVO3> containerLimitContainerQuery(Long tenantId, MtContLoadDtlVO10 dto);

    /**
     * containerLimitMaterialLotQuery-获取指定容器已装载的物料批
     *
     * @author chuang.yang
     * @date 2019/4/3
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.container_load_detail.view.MtContainerLoadDetailVO4>
     */
    List<MtContLoadDtlVO4> containerLimitMaterialLotQuery(Long tenantId, MtContLoadDtlVO10 dto);

    /**
     * containerLimitMaterialQtyQuery-获取指定容器已装载的物料和物料数量
     *
     * @author chuang.yang
     * @date 2019/4/3
     * @param tenantId
     * @param dto
     * @return java.util.List<hmes.container_load_detail.view.MtConLoadDetailVO7>
     */
    List<MtContLoadDtlVO7> containerLimitMaterialQtyQuery(Long tenantId, MtContLoadDtlVO2 dto);


    /**
     * objectLimitLoadingContainerQuery-获取指定对象所在的容器
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> objectLimitLoadingContainerQuery(Long tenantId, MtContLoadDtlVO5 dto);

    /**
     * containerLoadDetailUpdate-容器装载明细更新&新增
     *
     * update remarks
     * <ul>
     * <li>2019-09-16 benjamin 修改返回参数 添加容器装载明细历史Id</li>
     * </ul>
     * 
     * @author lxs
     * @date 2019/4/3
     * @param tenantId 租户Id
     * @param dto MtContLoadDtlVO8
     * @return MtContLoadDtlVO11
     */
    MtContLoadDtlVO11 containerLoadDetailUpdate(Long tenantId, MtContLoadDtlVO8 dto);

    /**
     * containerLoadDetailDelete -删除容器装载明细
     *
     * @Author lxs
     * @Date 2019/4/3
     * @Return void
     */
    void containerLoadDetailDelete(Long tenantId, MtContLoadDtlVO9 dto);

    /**
     * containerLoadDetailBatchDelete-容器装载明细批量删除
     *
     * @author chuang.yang
     * @date 2020/11/13
     */
    void containerLoadDetailBatchDelete(Long tenantId, MtContLoadDtlVO31 dto);


    /**
     * containerIsEmptyValidate -验证容器是否未装载任何对象
     *
     * @Author lxs
     * @Date 2019/4/3
     * @Return void
     */
    void containerIsEmptyValidate(Long tenantId, String containerId);

    /**
     * eoLimitLoadingEoQtyGet-获取执行作业已装载数量
     *
     * @author chuang.yang
     * @date 2019/6/13
     * @param tenantId
     * @param eoId
     * @return java.lang.Double
     */
    Double eoLimitLoadingEoQtyGet(Long tenantId, String eoId);

    /**
     * 根据装载id集合，批量查询容器装载数据
     *
     * @author chuang.yang
     * @date 2019/10/14
     * @param tenantId
     * @param loadObjectType
     * @param loadObjectIds
     * @return java.util.List<hmes.container_load_detail.dto.MtContainerLoadDetail>
     */
    List<MtContainerLoadDetail> loadObjectLimitBatchGet(Long tenantId, String loadObjectType,
                                                        List<String> loadObjectIds);

    /**
     * 根据containerId装载的物料批和容器
     *
     * @author chuang.yang
     * @date 2019/10/15
     * @param tenantId
     * @param containerId
     * @return java.util.List<hmes.container_load_detail.dto.MtContainerLoadDetail>
     */
    List<MtContainerLoadDetail> containerLimitMaterialLotAndContainer(Long tenantId, String containerId);

    /**
     * containerLoadDetailBatchUpdate-容器装载明细批量更新&新增并记录历史
     *
     * @author guichuan.li
     * @date 2019/10/15
     * @param tenantId
     * @param dto
     * @param fullUpdate
     * @return java.util.List
     */
    List<MtContLoadDtlVO11> containerLoadDetailBatchUpdate(Long tenantId, MtContLoadDtlVO12 dto, String fullUpdate);

    /**
     * 根据来源物料批id批量查找对应的容器id
     * 
     * @Author peng.yuan
     * @Date 2019/11/13 10:54
     * @param tenantId :
     * @param sourceMaterialLotId :
     * @return java.util.List<tarzan.inventory.domain.vo.MtContLoadDtlVO17>
     */
    List<MtContLoadDtlVO17> selectByMaterialLodIds(Long tenantId, List<String> sourceMaterialLotId);

    /**
     * 批量获取指定容器下的装载对象
     * 
     * @Author peng.yuan
     * @Date 2020/2/5 15:15
     * @param tenantId :
     * @param voList :
     * @return java.util.List<tarzan.inventory.domain.vo.MtContLoadDtlVO19>
     */
    List<MtContLoadDtlVO19> containerLimitObjectBatchGet(Long tenantId, List<MtContLoadDtlVO18> voList);

    /**
     * eoLimitLoadingEoQtyBatchGet-批量获取执行作业已装载数量
     * 
     * @param tenantId
     * @param eoIdList
     * @return
     */
    List<MtContLoadDtlVO20> eoLimitLoadingEoQtyBatchGet(Long tenantId, List<String> eoIdList);

    /**
     * containerLimitObjectBatchQuery-获取指定批量容器下的装载对象
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtContLoadDtlVO21> containerLimitObjectBatchQuery(Long tenantId, List<MtContLoadDtlVO> condition);
}
