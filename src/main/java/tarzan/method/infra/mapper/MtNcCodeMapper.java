package tarzan.method.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.method.api.dto.MtNcCodeDTO2;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.vo.MtNcCodeVO;
import tarzan.method.domain.vo.MtNcCodeVO1;

/**
 * 不良代码数据Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
public interface MtNcCodeMapper extends BaseMapper<MtNcCode> {

    List<MtNcCodeDTO2> queryNcCodeForUi(@Param(value = "tenantId") Long tenantId,
                                        @Param(value = "dto") MtNcCodeDTO2 dto);

    List<MtNcCodeVO1> selectCondition(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtNcCodeVO dto);

    List<MtNcCode> selectByNcCode(@Param(value = "tenantId") Long tenantId,
                                  @Param(value = "ncCodes") List<String> ncCodes);
}
