package tarzan.inventory.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.inventory.domain.entity.MtInvOnhandHold;
import tarzan.inventory.domain.vo.*;

/**
 * 库存保留量资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
public interface MtInvOnhandHoldRepository
                extends BaseRepository<MtInvOnhandHold>, AopProxy<MtInvOnhandHoldRepository> {
    /**
     * onhandReserveGet-获取库存保留明细
     *
     * @param tenantId
     * @param onhandHoldId
     * @return
     */
    MtInvOnhandHold onhandReserveGet(Long tenantId, String onhandHoldId);

    /**
     * onhandReserveBatchGet-批量获取库存保留明细
     *
     * @param tenantId
     * @param onhandHoldIds
     * @return
     */
    List<MtInvOnhandHold> onhandReserveBatchGet(Long tenantId, List<String> onhandHoldIds);

    /**
     * propertyLimitOnhandReserveQuery-根据属性获取库存保留ID
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitOnhandReserveQuery(Long tenantId, MtInvOnhandHoldVO3 dto);

    /**
     * onhandReserveCreateVerify-校验新增物料库存保留可行性
     *
     * @param tenantId
     * @param dto
     */
    void onhandReserveCreateVerify(Long tenantId, MtInvOnhandHoldVO3 dto);

    /**
     * onhandReserveCreate-新增库存保留
     *
     * @param tenantId
     * @param dto
     */
    void onhandReserveCreate(Long tenantId, MtInvOnhandHoldVO2 dto);

    /**
     * onhandReserveReleaseVerify-校验释放库存保留可行性
     *
     * @param tenantId
     * @param dto
     */
    void onhandReserveReleaseVerify(Long tenantId, MtInvOnhandHoldVO4 dto);

    /**
     * onhandReserveRelease-释放库存保留
     *
     * @param tenantId
     * @param dto
     */
    void onhandReserveRelease(Long tenantId, MtInvOnhandHoldVO5 dto);

    /**
     * onhandReserveAvailableVerify-校验保留库存可用性
     *
     * @param tenantId
     * @param dto
     */
    void onhandReserveAvailableVerify(Long tenantId, MtInvOnhandHoldVO dto);

    /**
     * onhandReserveUse-使用保留库存
     *
     * @param tenantId
     * @param dto
     */
    void onhandReserveUse(Long tenantId, MtInvOnhandHoldVO6 dto);

    /**
     * onhandReserveUseProcess-校验并使用预留库存
     * 
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/3/27
     */
    void onhandReserveUseProcess(Long tenantId, MtInvOnhandHoldVO7 dto);

    /**
     * onhandReserveCreateProcess-校验并新增库存预留
     * 
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/3/27
     */
    void onhandReserveCreateProcess(Long tenantId, MtInvOnhandHoldVO8 dto);

    /**
     * onhandReserveReleaseProcess-释放库存预留
     * 
     * @param tenantId
     * @param dto
     * @author sen.luo
     * @date 2019/3/27
     */
    void onhandReserveReleaseProcess(Long tenantId, MtInvOnhandHoldVO9 dto);

    /**
     * onhandReserveAvailableBatchVerify-批量校验更新物料库存可行性
     *
     * @author chuang.yang
     * @date 2021/4/22
     */
    List<MtInvOnhandHoldVO1> onhandReserveAvailableBatchVerify(Long tenantId, List<MtInvOnhandHoldVO> dtos);

    /**
     * onhandReserveUseBatchProcess-批量校验预留库存并使用
     *
     * @author chuang.yang
     * @date 2021/4/22
     */
    void onhandReserveUseBatchProcess(Long tenantId, MtInvOnhandHoldVO18 mtInvOnhandHoldVO18);

    /**
     * onhandReserveBatchUse-使用保留库存批量
     * 
     * @author chuang.yang
     * @date 2021/4/22
     */
    void onhandReserveBatchUse(Long tenantId, MtInvOnhandHoldVO18 mtInvOnhandHoldVO18);

}
