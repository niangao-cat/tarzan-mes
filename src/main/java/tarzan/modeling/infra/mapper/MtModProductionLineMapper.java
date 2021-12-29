package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.vo.MtModProductionLineVO2;

/**
 * 生产线Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
public interface MtModProductionLineMapper extends BaseMapper<MtModProductionLine> {

    MtModProductionLineVO2 selectProdLineById(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtModProductionLineVO2 dto);

    List<MtModProductionLine> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "prodLineIds") List<String> prodLineIds);

    /**
     * Oracle 空字符串查询处理
     */
    List<MtModProductionLine> selectForEmptyString(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtModProductionLine dto);

    List<MtModProductionLine> selectByCodesCustom(@Param(value = "tenantId") Long tenantId,
                                                  @Param(value = "prodLineCodes") String prodLineCodes);

}
