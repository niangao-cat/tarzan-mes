package tarzan.material.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.material.domain.entity.MtPfepManufacturing;
import tarzan.material.domain.vo.MtPfepInventoryVO;
import tarzan.material.domain.vo.MtPfepManufacturingVO;
import tarzan.material.domain.vo.MtPfepManufacturingVO2;

/**
 * 物料生产属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepManufacturingMapper extends BaseMapper<MtPfepManufacturing> {

    List<MtPfepInventoryVO> selectByBomId(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "bomId") String bomId);

    List<MtPfepInventoryVO> selectByRouterId(@Param(value = "tenantId") Long tenantId,
                                             @Param(value = "routerId") String routerId);

    List<MtPfepManufacturingVO> selectByMaterialIdForUi(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "materialId") String materialId);


    MtPfepManufacturingVO2 selectByIdCustomForUi(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "kid") String kid);

    List<MtPfepManufacturing> selectByMaterialSiteId(@Param(value = "tenantId") Long tenantId,
                                                     @Param(value = "materialSiteIds") String materialSiteIds);
}
