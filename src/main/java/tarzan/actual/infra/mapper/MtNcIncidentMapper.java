package tarzan.actual.infra.mapper;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtNcIncident;

/**
 * 不良事故Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:48
 */
public interface MtNcIncidentMapper extends BaseMapper<MtNcIncident> {

    /**
     * 生成编码
     *
     * @param tenantId
     * @return
     */
    String generateIncidentNumber(@Param(value = "tenantId") Long tenantId);
}
