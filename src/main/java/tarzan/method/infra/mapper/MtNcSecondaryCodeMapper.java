package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtNcSecondaryCodeDTO;
import tarzan.method.domain.entity.MtNcSecondaryCode;

/**
 * 次级不良代码Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
public interface MtNcSecondaryCodeMapper extends BaseMapper<MtNcSecondaryCode> {

    List<MtNcSecondaryCodeDTO> querySecondaryCodeListForUi(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "ncObjectId") String ncObjectId, @Param(value = "ncObjectType") String ncObjectType);
}
