package tarzan.modeling.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.vo.MtModWorkcellVO2;
import tarzan.modeling.domain.vo.MtModWorkcellVO5;

/**
 * 工作单元Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
public interface MtModWorkcellMapper extends BaseMapper<MtModWorkcell> {


    List<MtModWorkcell> selectWorkcells(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "workcellIds") List<String> workcellIds);

    MtModWorkcellVO2 selectWorkcellById(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "workcellId") String workcellId);

    List<MtModWorkcell> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "workcellIds") List<String> workcellIds);

    /**
     * Oracle 空字符串查询处理
     */
    List<MtModWorkcell> selectForEmptyString(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtModWorkcell dto);
    
    /**
     * Oracle 空字符串查询处理
     */
    List<MtModWorkcell> selectForEmptyStringForUi(@Param(value = "tenantId") Long tenantId,
                    @Param(value = "dto") MtModWorkcell dto);

    /**
     * 根据属性获取工作单元信息
     *
     * @param tenantId
     * @param dto
     */
    List<MtModWorkcellVO5> propertyLimitWorkcellPropertyQuery(@Param(value = "tenantId") Long tenantId,
                                                              @Param(value = "dto") MtModWorkcellVO5 dto);

    /**
     * selectByIdsWkcCode-获取
     * @param tenantId
     * @param workcellIds
     * @return
     */
    List<MtModWorkcell> selectByWkcCode(@Param(value = "tenantId") Long tenantId,
                                        @Param(value = "workcellIds") List<String> workcellIds);
}
