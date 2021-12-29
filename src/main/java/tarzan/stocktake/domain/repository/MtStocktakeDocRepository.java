package tarzan.stocktake.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.stocktake.domain.entity.MtStocktakeDoc;
import tarzan.stocktake.domain.vo.*;

/**
 * 盘点单据头表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:35:24
 */
public interface MtStocktakeDocRepository extends BaseRepository<MtStocktakeDoc>, AopProxy<MtStocktakeDocRepository> {


    /**
     * propertyLimitstocktakeDocQuery-根据指定属性查询盘点单据
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/5/16
     */
    List<String> propertyLimitStocktakeDocQuery(Long tenantId, MtStocktakeDocVO dto);

    /**
     * stocktakeDocPropertyGet-获取盘点单属性
     *
     * @param tenantId
     * @param stocktakeId
     * @author guichuan.li
     * @date 2019/5/21
     */
    MtStocktakeDoc stocktakeDocPropertyGet(Long tenantId, String stocktakeId);

    /**
     * stocktakeDocPropertyBatchGet-根据批量盘点单据id获取属性
     *
     * @author benjamin
     * @date 2019-07-23 09:20
     * @param tenantId 租户Id
     * @param stocktakeIdList 盘点单据Id集合
     * @return List
     */
    List<MtStocktakeDoc> stocktakeDocPropertyBatchGet(Long tenantId, List<String> stocktakeIdList);

    /**
     * stocktakeDocCreate-盘点单据新增
     *
     * @author chuang.yang
     * @date 2019/5/16
     * @param tenantId
     * @param dto
     * @return java.lang.String
     */
    String stocktakeDocCreate(Long tenantId, MtStocktakeDocVO1 dto);

    /**
     * stocktakeDocUpdate-盘点单据更新
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/5/21
     */
    void stocktakeDocUpdate(Long tenantId, MtStocktakeDocVO2 dto);

    /**
     * stocktakeDocStatusUpdateVerify-盘点单据状态变更验证
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/5/22
     */
    void stocktakeDocStatusUpdateVerify(Long tenantId, MtStocktakeDocVO3 dto);

    /**
     * stocktakeDocRelease-盘点单据下达
     *
     * @param tenantId
     * @param stocktakeId
     * @author guichuan.li
     * @date 2019/5/22
     */
    void stocktakeDocRelease(Long tenantId, String stocktakeId, String eventRequestId);

    /**
     * stocktakeDocComplete-盘点单据完成
     *
     * @author benjamin
     * @date 2019-07-08 14:54
     * @param tenantId 租户Id
     * @param stocktakeId 盘点单据Id
     */
    void stocktakeDocComplete(Long tenantId, String stocktakeId, String eventRequestId);

    /**
     * stocktakeDocClose-盘点单据关闭
     *
     * @author benjamin
     * @date 2019-07-08 14:54
     * @param tenantId 租户Id
     * @param stocktakeId 盘点单据Id
     */
    void stocktakeDocClose(Long tenantId, String stocktakeId, String eventRequestId);


    /**
     * stocktakeRangeLimitMaterialLotQuery-根据盘点单查询盘点范围下的物料批
     *
     * @author benjamin
     * @date 2019-07-08 10:01
     * @param tenantId 租户Id
     * @param stocktakeId 盘点单据Id
     * @return List
     */
    List<String> stocktakeRangeLimitMaterialLotQuery(Long tenantId, String stocktakeId);

    /**
     * stocktaketAndMaterialLotDifferenceQuery-比较盘点单与物料批的差异
     *
     * @author benjamin
     * @date 2019-07-08 14:07
     * @param tenantId 租户Id
     * @param stocktakeId 盘点单据Id
     * @return List
     */
    List<String> stocktaketAndMaterialLotDifferenceQuery(Long tenantId, String stocktakeId);


    /**
     * stocktakeDocFirstcountComplete-盘点单据初盘完成
     *
     * @param tenantId
     * @param stocktakeId
     * @author guichuan.li
     * @date 2019/7/10
     */
    void stocktakeDocFirstcountComplete(Long tenantId, String stocktakeId, String eventRequestId);

    /**
     * stocktakeDocCountComplete-盘点单据实盘完成
     *
     * @param tenantId
     * @param stocktakeId
     * @author guichuan.li
     * @date 2019/7/10
     */
    void stocktakeDocCountComplete(Long tenantId, String stocktakeId, String eventRequestId);

    /**
     * stocktakeStatusBackout-盘点单据状态撤销
     *
     * @author benjamin
     * @date 2019-07-19 19:55
     * @param tenantId 租户Id
     * @param doc MtStocktakeDoc
     */
    void stocktakeStatusBackout(Long tenantId, MtStocktakeDocVO4 doc);

    /**
     * stocktakeLimitMaterialLotUnlock-盘点物料批启用
     *
     * @author benjamin
     * @date 2019-07-19 20:15
     * @param tenantId 租户Id
     * @param stocktakeId 盘点Id
     */
    void stocktakeLimitMaterialLotUnlock(Long tenantId, String stocktakeId, String eventRequestId);

    /**
     * stocktakeLimitMaterialLotLock-盘点物料批停用
     *
     * @author benjamin
     * @date 2019-07-19 20:51
     * @param tenantId 租户Id
     * @param stocktakeId 盘点Id
     */
    void stocktakeLimitMaterialLotLock(Long tenantId, String stocktakeId, String eventRequestId);
}
