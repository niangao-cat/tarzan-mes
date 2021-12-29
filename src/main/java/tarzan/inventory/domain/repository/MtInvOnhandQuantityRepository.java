package tarzan.inventory.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.inventory.domain.vo.*;

/**
 * 库存量资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
public interface MtInvOnhandQuantityRepository
                extends BaseRepository<MtInvOnhandQuantity>, AopProxy<MtInvOnhandQuantityRepository> {

    /**
     * organizationSumOnhandQtyGet-获取特定组织对象下的汇总库存量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    Double organizationSumOnhandQtyGet(Long tenantId, MtInvOnhandQuantityVO7 dto);

    /**
     * organizationDetailOnhandQtyQuery-获取特定组织对象下的明细库存量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtInvOnhandQuantity> organizationDetailOnhandQtyQuery(Long tenantId, MtInvOnhandQuantityVO7 dto);

    /**
     * propertyLimitDetailOnhandQtyQuery-根据属性获取明细库存现有量
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtInvOnhandQuantity> propertyLimitDetailOnhandQtyQuery(Long tenantId, MtInvOnhandQuantityVO condition);

    /**
     * propertyLimitSumOnhandQtyGet-根据属性获取汇总库存现有量
     *
     * @param tenantId
     * @param condition
     * @return
     */
    Double propertyLimitSumOnhandQtyGet(Long tenantId, MtInvOnhandQuantityVO condition);

    /**
     * propertyLimitDetailOnhandQtyBatchQuery-批量根据属性获取明细库存现有量
     *
     * @param tenantId
     * @param condition
     */
    List<MtInvOnhandQuantity> propertyLimitDetailOnhandQtyBatchQuery(Long tenantId, MtInvOnhandQuantityVO1 condition);

    /**
     * onhandQtyUpdateVerify-校验更新物料库存可行性
     *
     * @param tenantId
     * @param dto
     * @return
     */
    MtInvOnhandQuantityVO2 onhandQtyUpdateVerify(Long tenantId, MtInvOnhandQuantityVO8 dto);

    /**
     * onhandQtyUpdate-更新物料库存现有量
     *
     * @param tenantId
     * @param dto
     */
    void onhandQtyUpdate(Long tenantId, MtInvOnhandQuantityVO9 dto);

    /**
     * propertyLimitSumAvailableOnhandQtyGet-根据属性获取汇总可使用现有量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    Double propertyLimitSumAvailableOnhandQtyGet(Long tenantId, MtInvOnhandQuantityVO dto);

    /**
     * propertyLimitDetailAvailableOnhandQtyQuery-根据属性获取明细可使用现有量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtInvOnhandQuantityVO6> propertyLimitDetailAvailableOnhandQtyQuery(Long tenantId, MtInvOnhandQuantityVO dto);

    /**
     * onhandQtyUpdateProcess-校验并更新物料库存量
     * 
     * @param tenantId
     * @param dto
     */
    void onhandQtyUpdateProcess(Long tenantId, MtInvOnhandQuantityVO9 dto);


    /**
     * onhandQtyUpdateBatchProcess-批量校验并更新物料库存
     * 
     * @param tenantId
     * @param dto
     */
    void onhandQtyUpdateBatchProcess(Long tenantId, MtInvOnhandQuantityVO16 dto);

    /**
     * onhandQtyUpdateBatchVerify-批量校验更新物料库存可行性
     * 
     * @param tenantId
     * @param dto
     * @return List<MtInvOnhandQuantityVO17>
     */
    List<MtInvOnhandQuantityVO17> onhandQtyUpdateBatchVerify(Long tenantId, MtInvOnhandQuantityVO14 dto);

    /**
     * onhandQtyBatchUpdate-批量更新物料库存现有量
     * 
     * @param tenantId
     * @param dto
     */
    void onhandQtyBatchUpdate(Long tenantId, MtInvOnhandQuantityVO16 dto);

    /**
     * propertyLimitSumAvailableOnhandQtyBatchGet-根据属性批量获取汇总可使用现有量
     *
     * @param tenantId
     * @param vos
     * @author guichuan.li
     * @date 2020/09/03
     */
    List<MtInvOnhandQuantityVO12> propertyLimitSumAvailableOnhandQtyBatchGet(Long tenantId,
                    List<MtInvOnhandQuantityVO> vos);
}
