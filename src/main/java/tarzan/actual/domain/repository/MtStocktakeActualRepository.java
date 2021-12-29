package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtStocktakeActual;
import tarzan.actual.domain.vo.MtStocktakeActualVO;
import tarzan.actual.domain.vo.MtStocktakeActualVO1;
import tarzan.actual.domain.vo.MtStocktakeActualVO2;
import tarzan.actual.domain.vo.MtStocktakeActualVO4;

/**
 * 盘点实绩表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtStocktakeActualRepository
                extends BaseRepository<MtStocktakeActual>, AopProxy<MtStocktakeActualRepository> {

    /**
     * stocktakeActualQuery-查询盘点实绩
     *
     * @param tenantId 租户Id
     * @param mtStocktakeActual MtStocktakeActual
     * @return List
     * @author benjamin
     * @date 2019-07-08 09:37
     */
    List<MtStocktakeActual> stocktakeActualQuery(Long tenantId, MtStocktakeActual mtStocktakeActual);

    /**
     * stocktakeLimitActualPropertyQuery-根据盘点单查询盘点实绩
     *
     * @author benjamin
     * @date 2019-07-29 19:33
     * @param tenantId 租户Id
     * @param stocktakeId 盘点单据Id
     * @return List
     */
    List<MtStocktakeActual> stocktakeLimitActualPropertyQuery(Long tenantId, String stocktakeId);

    /**
     * stocktakeActualUpdate-盘点实绩创建&更新
     *
     * @param tenantId 租户Id
     * @param updateVO 更新&新增VO
     * @author benjamin
     * @date 2019-07-04 15:40
     */
    void stocktakeActualUpdate(Long tenantId, MtStocktakeActualVO updateVO);

    /**
     * stocktaketActualDifferenceCreate-生成盘点实绩差异
     *
     * @param tenantId
     * @param stocktakeId
     * @author guichuan.li
     * @date 2019/7/08
     */
    void stocktaketActualDifferenceCreate(Long tenantId, String stocktakeId, String eventRequestId);


    /**
     * stocktakeFirstcount-初盘
     *
     * update remarks 2019-7-23 benjamin
     * <p>
     * 传入参数-物料批 由List更改为String 修改相应逻辑
     * </p>
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/7/08
     */
    void stocktakeFirstcount(Long tenantId, MtStocktakeActualVO1 dto);

    /**
     * stocktakeRecount-复盘
     *
     * update remarks 2019-7-23 benjamin
     * <p>
     * 传入参数-物料批 由List更改为String 修改相应逻辑
     * </p>
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/7/08
     */
    void stocktakeRecount(Long tenantId, MtStocktakeActualVO1 dto);

    /**
     * stocktakeActualDifferenceAdjust-盘点实绩差异调整
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/7/08
     */
    void stocktakeActualDifferenceAdjust(Long tenantId, MtStocktakeActualVO2 dto);

    /**
     * stocktakeActualDifferenceBatchAdjust-盘点实绩差异批量调整
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/7/09
     */
    void stocktakeActualDifferenceBatchAdjust(Long tenantId, MtStocktakeActualVO4 dto);
}
