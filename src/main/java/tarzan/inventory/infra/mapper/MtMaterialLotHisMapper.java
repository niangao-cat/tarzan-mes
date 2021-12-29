package tarzan.inventory.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.vo.MtMaterialLotHisVO2;
import tarzan.inventory.domain.vo.MtMaterialLotHisVO3;

/**
 * 物料批历史Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:05:08
 */
public interface MtMaterialLotHisMapper extends BaseMapper<MtMaterialLotHis> {
    List<MtMaterialLotHis> selectByEventIds(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "eventIds") List<String> eventIds);

    List<MtMaterialLotHis> selectMaterialLotEventLimit(@Param(value = "tenantId") Long tenantId,
                                                       @Param(value = "dto") MtMaterialLotHisVO2 dto);

    MtMaterialLotHisVO3 materialLotLatestHisGet(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "materialLotId") String materialLotId);

    List<MtMaterialLotHis> materialLotHisBatchGet(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "hisIds") List<String> hisIds);
}
