package tarzan.material.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.material.domain.vo.MtUomVO4;
import tarzan.material.domain.vo.MtUomVO5;

/**
 * 单位Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:10:08
 */
public interface MtUomMapper extends BaseMapper<MtUom> {

    List<MtUomVO> selectOneView(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtUomVO dto);

    List<MtUomVO> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "uomIds") List<String> uomIds);

    List<MtUomVO5> selectCondition(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtUomVO4 dto);

    /**
     *
     * @param tenantId
     * @param codes
     * @return
     */
    List<MtUom> selectByUomCode(@Param(value = "tenantId") Long tenantId, @Param(value = "codes") String codes);

    /**
     * 根据单位Id批量获取
     *
     * @param tenantId
     * @param ids
     * @return
     */
    List<MtUom> selectUomBatch(@Param(value = "tenantId") Long tenantId, @Param(value = "ids") List<String> ids);
}
