package com.ruike.hme.infra.mapper;

import java.util.List;

import com.ruike.hme.domain.entity.HmeMaterialLotLabCode;
import com.ruike.hme.domain.vo.HmeMaterialLotLabCodeVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 条码实验代码表Mapper
 *
 * @author penglin.sui@hand-china.com 2021-01-25 14:23:29
 */
public interface HmeMaterialLotLabCodeMapper extends BaseMapper<HmeMaterialLotLabCode> {
    /**
     *
     * @Description 查询实验代码数量
     *
     * @author penglin.sui
     * @date 2021/1/25 16:45
     * @param tenantId 租户ID
     * @param dto 参数
     * @return java.lang.Integer
     *
     */
    HmeMaterialLotLabCode selectLabCode2(@Param("tenantId") Long tenantId, @Param("dto") HmeMaterialLotLabCode dto);

    /**
     *
     * @Description 查询实验代码数量
     *
     * @author penglin.sui
     * @date 2021/1/25 16:45
     * @param tenantId 租户ID
     * @param materialLotIdList 条码ID
     * @param eoStepIdList 步骤ID
     * @param labCode 实验代码
     * @return java.lang.Integer
     *
     */
    List<HmeMaterialLotLabCode> batchSelectLabCode2(@Param("tenantId") Long tenantId,
                                                    @Param("materialLotIdList") List<String> materialLotIdList,
                                                    @Param("eoStepIdList") List<String> eoStepIdList,
                                                    @Param("labCode") String labCode);

    /**
     * 获取实验代码
     *
     * @param tenantId
     * @param materialLotId
     * @return WmsMaterialLotLabCodeVO
     * @author li.zhang13@hand-china.com 2021/01/25 13:40
     */
    List<HmeMaterialLotLabCodeVO> selectLabCode(@Param("tenantId") Long tenantId, @Param("materialLotId")String materialLotId);

    /**
     * 根据实验代码获取物料批ID
     *
     * @param tenantId
     * @param labCode
     * @return HmeMaterialLotLabCodeVO2
     * @author li.zhang13@hand-china.com 2021/01/25 13:40
     */
    List<String> selectMaterial(@Param("tenantId") Long tenantId,@Param("labCode") String labCode);
    /**
     *
     * @Description 查询实验代码数量
     *
     * @author penglin.sui
     * @date 2021/1/25 16:45
     * @param tenantId 租户ID
     * @param routerStepId 参数
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLabCode>
     *
     */
    List<HmeMaterialLotLabCode> batchSelectLabCode(@Param("tenantId") Long tenantId,
                                                   @Param("routerStepId") String routerStepId,
                                                   @Param("materialLotIdlist") List<String> materialLotIdList);
}
