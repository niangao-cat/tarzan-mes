package tarzan.material.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.material.domain.entity.MtPfepManufacturingCategory;
import tarzan.material.domain.vo.MtPfepInventoryVO2;
import tarzan.material.domain.vo.MtPfepManufacturingVO;
import tarzan.material.domain.vo.MtPfepManufacturingVO2;

/**
 * 物料类别生产属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepManufacturingCategoryMapper extends BaseMapper<MtPfepManufacturingCategory> {

    List<MtPfepInventoryVO2> selectByBomId(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "bomId") String bomId);

    List<MtPfepInventoryVO2> selectByRouterId(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "routerId") String routerId);

    List<MtPfepManufacturingVO> selectByMaterialCategroyIdForUi(@Param(value = "tenantId") Long tenantId,
                                                                @Param(value = "materialCategoryId") String materialCategoryId);

    MtPfepManufacturingVO2 selectByIdForUi(@Param(value = "tenantId") Long tenantId,
                                           @Param(value = "kid") String kid);
}
