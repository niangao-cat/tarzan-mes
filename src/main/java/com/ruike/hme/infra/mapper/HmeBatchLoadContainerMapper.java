package com.ruike.hme.infra.mapper;

import com.ruike.hme.domain.vo.HmeLoadContainerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批量完工装箱 Mapper
 *
 * @author Deng xu
 * @date 2020/6/5 9:40
 */
public interface HmeBatchLoadContainerMapper {

    /**
     * 查询外箱装载物料批明细
     *
     * @param outerContainerCode 外箱编码
     * @param materialLotIdList  物料批ID集合
     * @return : java.util.List<com.ruike.hme.domain.vo.HmeLoadContainerVO>
     * @author Deng Xu
     * @date 2020/6/5 11:04
     */
    List<HmeLoadContainerVO> queryOuterContainerLoadDetail(@Param(value = "outerContainerCode") String outerContainerCode,
                                                           @Param(value = "materialLotIdList") List<String> materialLotIdList);

    /**
     * 容器货位
     *
     * @param tenantId          租户id
     * @param containerId       容器id
     * @author sanfeng.zhang@hand-china.com 2020/8/26 18:09
     * @return java.util.List<java.lang.String>
     */
    List<String> queryLocatorCodeByContainerId(@Param("tenantId") Long tenantId, @Param("containerId") String containerId);
}
