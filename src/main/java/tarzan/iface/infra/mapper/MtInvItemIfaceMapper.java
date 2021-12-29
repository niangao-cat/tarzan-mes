package tarzan.iface.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.iface.domain.entity.MtInvItemIface;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.vo.MtInvIfaceVO;
import tarzan.material.domain.entity.*;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.modeling.domain.entity.MtModLocator;

/**
 * 物料接口表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:38:29
 */
public interface MtInvItemIfaceMapper extends BaseMapper<MtInvItemIface> {

    /***
     * 根据单位Code获取主单位
     *
     */
    List<MtUomVO> getUomList(@Param("tenantId") Long tenantId, @Param(value = "uomCodes") String uomCodes);

    /***
     * 根据itemId和itemCode获取对应数据
     *
     */
    List<MtMaterial> getMaterialList(@Param("tenantId") Long tenantId,
                                     @Param(value = "materialList") String materialList);

    /***
     * 根据plantId和plantCode获取物料站点信息
     *
     */
    List<MtSitePlantReleation> getMateriaSiteReleationList(@Param("tenantId") Long tenantId,
                                                           @Param(value = "materialList") List<String> materialList);

    /***
     * 根据和materialId与siteId获取物料站点信息
     *
     */
    List<MtMaterialSite> getMateriaSitelList(@Param("tenantId") Long tenantId,
                                             @Param(value = "materialList") List<MtMaterialSite> materialList);

    /***
     * 根据和materialSiteId获取mtPfepSchedule数据
     *
     */
    List<MtPfepSchedule> getPfepSchedule(@Param("tenantId") Long tenantId,
                                         @Param(value = "materialSiteList") List<String> materialSiteList);

    /***
     * 根据和materialSiteId获取mtPfepPurchase数据
     *
     */
    List<MtPfepPurchase> getPfepPurchase(@Param("tenantId") Long tenantId,
                                         @Param(value = "materialSiteList") List<String> materialSiteList);

    /**
     * get unprocessedList
     * <p>
     * condition: status is 'N' or 'E'
     *
     * @return List
     * @author benjamin
     * @date 2019-06-25 15:56
     */
    List<MtInvItemIface> getUnprocessedList(@Param(value = "tenantId") Long tenantId);

    /**
     * 根据itemCode获取物料站点信息
     */
    List<MtMaterialSite> getMateriaSitelList1(@Param("tenantId") Long tenantId,
                                              @Param(value = "codeList") String codeList);


    /**
     * 获取MtPfepPurchase数据
     */
    List<MtPfepPurchase> getMtPfepPurchaseList(@Param("tenantId") Long tenantId, @Param(value = "list") String list);

    /**
     * 获取MtPfepSchedule数据
     */
    List<MtPfepSchedule> getMtPfepScheduleList(@Param("tenantId") Long tenantId, @Param(value = "list") String list);

    /**
     * 获取MtPfepInventory数据
     */
    List<MtPfepInventory> getMtPfepInventoryList(@Param("tenantId") Long tenantId, @Param(value = "list") String list);

    /**
     * 获取MtMaterialBasis数据
     */
    List<MtMaterialBasic> getMtMaterialBasisList(@Param("tenantId") Long tenantId, @Param(value = "list") String list);

    /**
     * 获取物料的多语言信息
     */
    List<MtInvIfaceVO> getItemTlList(@Param("tenantId") Long tenantId, @Param(value = "list") String list);

    void lockTable(@Param("tableName") String tableName);

    void unlockTable(@Param("tableName") String tableName);

    List<MtMaterial> selectMaterialByItemCodes(@Param("tenantId") Long tenantId, @Param("itemCodes") String itemCodes);

    List<MtSitePlantReleation> selectSiteByPlantCode(@Param("tenantId") Long tenantId, @Param("plantCodes") String plantCodes);

    List<MtUomVO> selectPrimaryUom(@Param("tenantId") Long tenantId, @Param("primaryUoms") String primaryUoms);

    List<MtModLocator> selectStockLocatorByLocatorCode(@Param("tenantId") Long tenantId, @Param("stockLocatorCodes") String stockLocatorCodes);

    List<MtMaterialSite> selectMaterialSite(@Param("tenantId") Long tenantId, @Param("materialIds") String materialIds);

    void deleteMaterialSiteAttrData(@Param("sql") String sql);

    void updateStatusByBatchId(@Param("batchId") Double batchId, @Param("status") String status, @Param("message") String message);
}
