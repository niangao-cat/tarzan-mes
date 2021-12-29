package tarzan.actual.infra.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.api.dto.MtEoStepWipDTO;
import tarzan.actual.api.dto.MtEoStepWipDTO2;
import tarzan.actual.domain.entity.MtEoStepWip;
import tarzan.actual.domain.vo.MtEoStepWipVO1;
import tarzan.actual.domain.vo.MtEoStepWipVO6;
import tarzan.actual.domain.vo.MtEoStepWipVO7;

/**
 * 执行作业在制品Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:59:30
 */
public interface MtEoStepWipMapper extends BaseMapper<MtEoStepWip> {

    List<MtEoStepWip> eoWkcAndStepWipBatchGet(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "eoStepActualWkcIds") Map<String, String> eoStepActualWkcIds);

    List<MtEoStepWip> selectByEoId(@Param(value = "tenantId") Long tenantId, @Param(value = "eoId") String eoId);

    List<MtEoStepWipVO6> selectByStepActual(@Param(value = "tenantId") Long tenantId, @Param("dto") MtEoStepWipVO7 dto,
                                            @Param("qtyType") String qtyType);

    /**
     * Oracle 空字符匹配查询
     */
    MtEoStepWip selectForEmptyString(@Param(value = "tenantId") Long tenantId, @Param("dto") MtEoStepWip dto);

    /**
     * 根据sourceEoStepActualIdList查询属性集合
     *
     * @Author peng.yuan
     * @Date 2019/11/25 21:32
     * @param tenantId :
     * @param sourceEoStepActualIdList :
     * @return java.util.List<tarzan.actual.domain.entity.MtEoStepWip>
     */
    List<MtEoStepWip> selectByEoStepActualIdList(@Param(value = "tenantId") Long tenantId,
                                                 @Param(value = "sourceEoStepActualIdList") List<String> sourceEoStepActualIdList);

    List<MtEoStepWip> selectByConditionList(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "list") List<MtEoStepWipVO1> list);

    List<MtEoStepWipDTO2> eoStepWipReportForUi(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "dto") MtEoStepWipDTO dto);

    /**
     * 改造查询空串
     *
     * @Author peng.yuan
     * @Date 2020/2/25 22:03
     * @param mtEoStepWip :
     * @param qtyType :
     * @return java.util.List<tarzan.actual.domain.entity.MtEoStepWip>
     */
    List<MtEoStepWip> selectForEoWkcAndStepWipQuery(@Param(value = "tenantId") Long tenantId,
                                                    @Param("dto") MtEoStepWip mtEoStepWip,@Param("qtyType") String qtyType);
}
