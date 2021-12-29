package tarzan.inventory.infra.mapper;

import java.util.List;

import com.ruike.wms.domain.vo.WmsSoTransferReturnVO;
import org.apache.ibatis.annotations.Param;

import com.ruike.wms.domain.vo.WmsMaterialLotVO;
import com.ruike.wms.domain.vo.WmsMaterialLotVO2;
import io.choerodon.mybatis.common.BaseMapper;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.vo.MtMaterialLotVO21;
import tarzan.inventory.domain.vo.MtMaterialLotVO22;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;
import tarzan.inventory.domain.vo.MtMaterialLotVO4;

/**
 * 物料批，提供实物管理简易装载物料的结构，记录包括物料、位置、状态、数量、所有者、预留对象等数据Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:05:08
 */
public interface MtMaterialLotMapper extends BaseMapper<MtMaterialLot> {
    List<MtMaterialLot> selectByMaterialLotId(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "materialLotIds") List<String> materialLotIds);

    List<MtMaterialLot> selectByMaterialLotIdForUpdate(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "materialLotIds") List<String> materialLotIds);

    List<MtMaterialLot> selectByPropertyLimit(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "dto") MtMaterialLotVO3 dto);

    Long selectByMaterialLotCode(@Param(value = "tenantId") Long tenantId,
                                 @Param(value = "materialLotCode") String materialLotCode);

    MtMaterialLotVO4 selectLimitQty(@Param(value = "tenantId") Long tenantId,
                                    @Param(value = "materialLotId") String materialLotId);

    Long selectByMaterialLotCodes(@Param(value = "tenantId") Long tenantId,
                                  @Param(value = "materialLotCodes") List<String> materialLotCodes);

    Long selectByIdentifications(@Param(value = "tenantId") Long tenantId,
                                 @Param(value = "identifications") List<String> identifications);

    List<MtMaterialLotVO22> selectCondition(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "dto") MtMaterialLotVO21 dto);

    List<String> selectLotIdByTopContainerId(@Param(value = "tenantId") Long tenantId,
                                             @Param(value = "topContainerId") String topContainerId);

    List<MtMaterialLot> selectForMaterialLotCodes(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "materialLotCodes") List<String> materialLotCodes);

    List<MtMaterialLot> selectByTopContainerIds(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "topContainerIds") List<String> topContainerIds);

    List<MtMaterialLot> selectListByEoId(@Param(value = "tenantId") Long tenantId, @Param(value = "eoId") String eoId);

    MtMaterialLot selectForUpdate(@Param(value = "tenantId") Long tenantId, @Param(value = "materialLotId") String materialLotId);

    String selectAttrValue(@Param(value = "tenantId")Long tenantId,
                           @Param(value = "attrName")String attrName,
                           @Param(value = "materialLotId")String materialLotId);

    List<WmsSoTransferReturnVO> selectLocator(@Param(value = "tenantId")Long tenantId,
                                              @Param(value = "materialLotIdList")List<String> materialLotIdList);
}
