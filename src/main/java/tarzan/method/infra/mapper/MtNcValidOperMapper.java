package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtNcValidOperDTO;
import tarzan.method.domain.entity.MtNcValidOper;

/**
 * 不良代码工艺分配Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
public interface MtNcValidOperMapper extends BaseMapper<MtNcValidOper> {

    List<MtNcValidOperDTO> queryNcValidOperList(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "ncObjectId") String ncObjectId, @Param(value = "ncObjectType") String ncObjectType);
}
