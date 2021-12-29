package tarzan.inventory.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.inventory.domain.entity.MtContainerLoadDetail;
import tarzan.inventory.domain.vo.MtContLoadDtlVO13;
import tarzan.inventory.domain.vo.MtContLoadDtlVO15;
import tarzan.inventory.domain.vo.MtContLoadDtlVO17;
import tarzan.inventory.domain.vo.MtContLoadDtlVO18;
import tarzan.inventory.domain.vo.MtContLoadDtlVO19;

/**
 * 容器装载明细，记录具体容器装载的执行作业或物料批或其他容器的情况Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:32
 */
public interface MtContainerLoadDetailMapper extends BaseMapper<MtContainerLoadDetail> {

    Long getMaxSequence(@Param(value = "tenantId") Long tenantId, @Param(value = "containerId") String containerId);

    List<MtContainerLoadDetail> selectDeleteUnique(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "dto") MtContainerLoadDetail dto);

    List<MtContainerLoadDetail> selectByLoadObject(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "loadObjectType") String loadObjectType,
                                                   @Param(value = "loadObjectIds") List<String> loadObjectIds);

    List<MtContainerLoadDetail> selectMaterialLotAndContainerByContainer(@Param(value = "tenantId") Long tenantId,
                                                                         @Param(value = "containerId") String containerId);

    List<MtContLoadDtlVO15> getBatchMaxSequence(@Param(value = "tenantId") Long tenantId,
                                                @Param(value = "containerIds") List<String> containerIds);

    List<MtContainerLoadDetail> selectContainerLoadDetailList(@Param(value = "tenantId") Long tenantId,
                                                              @Param(value = "detailList") List<MtContLoadDtlVO13> detailList);

    List<MtContainerLoadDetail> batchGetContainerDetail(@Param(value = "tenantId") Long tenantId,
                                                        @Param(value = "loadObjectType") String loadObjectType,
                                                        @Param(value = "containerIds") List<String> containerIds);

    /**
     * 根据sourceMaterialLotId查找信息
     *
     * @Author peng.yuan
     * @Date 2019/11/25 19:36
     * @param tenantId :
     * @param sourceMaterialLotId :
     * @return java.util.List<tarzan.inventory.domain.vo.MtContLoadDtlVO17>
     */
    List<MtContLoadDtlVO17> selectByMaterialLodIds(@Param(value = "tenantId") Long tenantId,
                                                   @Param(value = "sourceMaterialLotId") List<String> sourceMaterialLotId);

    /**
     * 根据容器id和类型查找
     *
     * @Author peng.yuan
     * @Date 2020/2/5 15:30
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.inventory.domain.vo.MtContLoadDtlVO19>
     */
    List<MtContLoadDtlVO19> selectByIdAndType(@Param(value = "tenantId") Long tenantId,
                                              @Param(value = "dto") MtContLoadDtlVO18 dto);

    /**
     * 根据容器id批量查询
     *
     * @Author peng.yuan
     * @Date 2020/2/6 15:46
     * @param tenantId :
     * @param containerIds :
     * @return java.util.List<tarzan.inventory.domain.entity.MtContainerLoadDetail>
     */
    List<MtContainerLoadDetail> selectByContainerId(@Param(value = "tenantId") Long tenantId,
                                                    @Param(value = "containerIds") List<String> containerIds);
}
