package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModProdLineManufacturing;

/**
 * 生产线生产属性Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModProdLineManufacturingMapper extends BaseMapper<MtModProdLineManufacturing> {


    List<MtModProdLineManufacturing> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "prodLineIds") List<String> prodLineIds);
}
