package tarzan.actual.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.choerodon.mybatis.common.BaseMapper;
import tarzan.actual.domain.entity.MtInstructionActualDetail;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO;
import tarzan.actual.domain.vo.MtInstructionActualDetailVO2;

/**
 * 指令实绩明细表Mapper
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtInstructionActualDetailMapper extends BaseMapper<MtInstructionActualDetail> {

    /**
     * delete by id list
     *
     * @param tenantId
     * @param instructionActualDetailIdList Id List
     * @author benjamin
     * @date 2019-06-21 10:10
     */
    void batchDelete(@Param(value = "tenantId") Long tenantId,
                     @Param(value = "instructionActualDetailIdList") List<String> instructionActualDetailIdList);

    List<MtInstructionActualDetailVO> propertyLimitInstructionActualDetailQuery(
            @Param(value = "tenantId") Long tenantId, @Param(value = "vo") MtInstructionActualDetail detail);

    List<MtInstructionActualDetail> selectByIdsCustom(@Param(value = "tenantId") Long tenantId,
                                                      @Param(value = "actualIds") List<String> actualIds);

    List<MtInstructionActualDetailVO2> selectByMyCondition(@Param(value = "tenantId") Long tenantId,
                                                           @Param(value = "instructionIdList") List<String> instructionIdList);

    List<String> selectMaterialLotIdByActualId(@Param(value = "tenantId") Long tenantId,
                                               @Param(value = "actualIdList") List<String> actualIdList);

    void updateActualQty(@Param(value = "tenantId")Long tenantId,
                         @Param(value = "actualDetailId")String actualDetailId,
                         @Param(value = "actualQty")Double actualQty);

    void updateLocator(@Param(value = "tenantId")Long tenantId,
                       @Param(value = "actualDetailId")String actualDetailId,
                       @Param(value = "toLocatorId")String toLocatorId);

    void updateQty(@Param(value = "tenantId")Long tenantId,
                   @Param(value = "actualQty")Double actualQty,
                   @Param(value = "actualId")String actualId,
                   @Param(value = "materialLotId")String materialLotId);

    void updateLocatorByActualId(@Param(value = "tenantId")Long tenantId,
                                 @Param(value = "locatorId")String locatorId,
                                 @Param(value = "actualId")String actualId,
                                 @Param(value = "materialLotId")String materialLotId);

    void deleteByMaterialLotId(@Param(value = "tenantId")Long tenantId,
                               @Param(value = "actualId")String actualId,
                               @Param(value = "materialLotId")String materialLotId);

    void updateContainer(@Param(value = "tenantId")Long tenantId,
                         @Param(value = "materialLotId")String materialLotId,
                         @Param(value = "actualId")String actualId);

    List<MtInstructionActualDetail> getActualDetailId(@Param(value = "tenantId")Long tenantId,
                                                      @Param(value = "actualId")String actualId);
}
