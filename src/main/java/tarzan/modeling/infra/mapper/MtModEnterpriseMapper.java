package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModEnterprise;

/**
 * 企业Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModEnterpriseMapper extends BaseMapper<MtModEnterprise> {

    List<MtModEnterprise> selectByIdsCustom(@Param("tenantId") Long tenantId,
                    @Param("enterPriseIds") List<String> modEnterPriseIds);
}
