package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.WmsContainerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author by admin
 * @Classname ContainerMapper
 * @Description
 * @Date 2019/9/18 11:41
 */
public interface WmsContainerMapper {

    /**
     * @param containerQryDTO
     * @return java.util.List<com.superlighting.hwms.domain.entity.ZMaterialLotE>
     * @Description 条码查询
     * @Date 2019/9/19 10:52
     * @Created by admin
     */
    List<WmsContainerResultDTO> containerHeaderQuery(WmsContainerQryDTO containerQryDTO);

    /**
     * @param containerQryDTO
     * @return java.util.List<com.superlighting.hwms.domain.entity.ZMaterialLotHisE>
     * @Description 条码历史查询
     * @Date 2019/9/19 10:53
     * @Created by admin
     */
    List<WmsContainerLineResultDTO> containerLineQuery(WmsContainerQryDTO containerQryDTO);

    /**
     * @param containerHisQryDTO
     * @return java.util.List<com.superlighting.hwms.domain.entity.ZMaterialLotE>
     * @Description 条码查询
     * @Date 2019/9/19 10:52
     * @Created by admin
     */
    List<WmsContainerHisResultDTO> containerHeaderHis(WmsContainerHisQryDTO containerHisQryDTO);

    /**
     * @param containerHisQryDTO
     * @return java.util.List<com.superlighting.hwms.domain.entity.ZMaterialLotHisE>
     * @Description 条码历史查询
     * @Date 2019/9/19 10:53
     * @Created by admin
     */
    List<WmsContainerLineHisResultDTO> containerLineHis(WmsContainerHisQryDTO containerHisQryDTO);

    /**
     * @param containerId 容器id
     * @param tenantId
     * @return ZContainerInfoResultDTO
     * @Description 根据容器id查询容器信息
     * @Date 2019/10/10 10:53
     * @Created by ljh
     */
    WmsContainerInfoResultDTO containnerCodeQuery(@Param(value = "tenantId") Long tenantId, @Param(value = "containerId") String containerId);

    /**
     * @param materialLotId 物料批次id
     * @param tenantId
     * @return ZContainerDetailResultDTO
     * @Description 根据物料批次id查询物料批次信息
     * @Date 2019/10/10 10:53
     * @Created by ljh
     */
    WmsContainerDetailResultDTO getMaterialLotInfoByLotId(@Param(value = "tenantId") Long tenantId, @Param(value = "materialLotId") String materialLotId);

    /**
     * @param materialLotId
     * @param tenantId
     * @return ZContainerDetailResultDTO
     * @Description 根据物料批次id查询容器信息
     * @Date 2019/10/10 10:53
     * @Created by ljh
     */
    WmsContainerDetailResultDTO getContainerInfoByContainerId(@Param(value = "tenantId") Long tenantId, @Param(value = "materialLotId") String materialLotId);

    /**
     * @param containerId
     * @param tenantId
     * @return ZContainerDetailResultDTO
     * @Description 根据容器id得到容器code
     * @Date 2019/10/10 10:53
     * @Created by ljh
     */
    String getContainerCodeById(@Param(value = "tenantId") Long tenantId, @Param(value = "containerId") String containerId);

    /**
     * @param tenantId
     * @param loadObjectId
     * @return ZContainerDetailResultDTO
     * @Description 得到物料批id、code
     * @Date 2019/10/23 14:11
     * @user
     */
    WmsContainerDetailResultDTO getMaterialLotInfoById(@Param(value = "tenantId") Long tenantId, @Param(value = "loadObjectId") String loadObjectId);

    /**
     * @param containerTypeMaintainRequestDto
     * @return java.util.List<com.ruike.wms.api.controller.dto.WmsContainerTypeMaintainResponseDTO>
     * @Description 查询容器类型
     * @Date 2019/12/27 9:32
     * @Created by 许博思
     */
    List<WmsContainerTypeMaintainResponseDTO> queryType(WmsContainerTypeMaintainRequestDTO containerTypeMaintainRequestDto);

    /**
     * 通过编码查询容器信息
     *
     * @param tenantId      租户
     * @param containerCode 容器编码
     * @return com.ruike.wms.domain.vo.WmsContainerVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/11/16 04:26:10
     */
    WmsContainerVO selectByCode(@Param(value = "tenantId") Long tenantId,
                                @Param(value = "containerCode") String containerCode);
}
