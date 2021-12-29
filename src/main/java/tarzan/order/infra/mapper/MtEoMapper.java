package tarzan.order.infra.mapper;

import com.ruike.hme.domain.vo.HmeEoRepairSnVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.order.api.dto.MtEoBomDTO;
import tarzan.order.api.dto.MtEoBomDTO3;
import tarzan.order.api.dto.MtEoBomDTO4;
import tarzan.order.api.dto.MtEoDTO4;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.vo.*;

import java.util.List;

/**
 * 执行作业【执行作业需求和实绩拆分开】Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoMapper extends BaseMapper<MtEo> {
    List<MtEo> selectEoByWoLimit(@Param(value = "tenantId") Long tenantId, @Param(value = "dtos") MtEoVO2 dtos);

    List<MtEo> selectEoByRouterOrBom(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtEoVO5 dto);

    List<MtEo> selectByIdsCustom(@Param(value = "tenantId") Long tenantId, @Param(value = "eoIds") String eoIds);

    List<MtEo> selectByTime(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtEoVO7 dto);

    List<MtEo> propertyLimitEoPropertyQuery(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "dto") MtEoVO30 dto);

    List<MtEo> selectByNumCustom(@Param(value = "tenantId") Long tenantId,
                                 @Param(value = "eoNums") List<String> eoNums);

    String selectMinNumByEoIds(@Param(value = "tenantId") Long tenantId, @Param(value = "eoIds") List<String> eoIds);

    List<MtWorkOrderVO51> selectByWorkOrder(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "dto") MtWorkOrderVO46 dto);

    List<MtWorkOrderVO51> ncInfoFlagQuery(@Param(value = "tenantId") Long tenantId,
                                          @Param(value = "eoList") List<String> eo_list);

    /**
     * 增加了当前工序的查询条件  更改了返回值及sql修改
     *
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.order.domain.entity.MtEo>
     * @author sanfeng.zhang@hand-china.com 2020/12/24 14:22
     */
    List<MtEo> eoListForUi(@Param(value = "tenantId") Long tenantId, @Param(value = "dto") MtEoDTO4 dto);


    List<MtEoBomDTO3> eoBomStepListForUi(@Param(value = "tenantId") Long tenantId,
                                         @Param(value = "dto") MtEoBomDTO4 dto);

    List<MtEoVO48> queryWoEoCount(@Param(value = "tenantId") Long tenantId,
                                  @Param(value = "workOrderIdsInSql") String workOrderIdsInSql);

    /**
     *
     * @Description 根据主表数据查询mt_eo_component_actual
     *
     * @author yuchao.wang
     * @date 2020/11/10 21:35
     * @return tarzan.order.api.dto.MtEoBomDTO
     *
     */
    MtEoBomDTO queryBomComponentActualForUi(@Param(value = "tenantId") Long tenantId,
                                            @Param(value = "eoId") String eoId,
                                            @Param(value = "bomId") String bomId,
                                            @Param(value = "bomComponentId") String bomComponentId,
                                            @Param(value = "bomComponentType") String bomComponentType,
                                            @Param(value = "materialId") String materialId);

    List<String> checkSubstitute(@Param(value = "tenantId")Long tenantId,@Param(value = "materialId")String materialId, @Param(value = "bomId") String bomId);

    MtEoBomDTO queryBomComponentActualForSubstitute(@Param(value = "tenantId")Long tenantId,
                                                    @Param(value = "eoId")String eoId,
                                                    @Param(value = "bomId")String bomId,
                                                    @Param(value = "materialId")String materialId);

    /**
     * 根据eoId查询扩展属性返修SN
     *
     * @param tenantId 租户ID
     * @param eoIdList eoId集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/24 02:24:38
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEoRepairSnVO>
     */
    List<HmeEoRepairSnVO> eoRepairSnQuery(@Param(value = "tenantId")Long tenantId,
                                          @Param(value = "eoIdList")List<String> eoIdList);
}
