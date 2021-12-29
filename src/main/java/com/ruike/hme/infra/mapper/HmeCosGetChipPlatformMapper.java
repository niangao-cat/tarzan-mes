package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.HmeCosGetChipMaterialLotListDTO;
import com.ruike.hme.api.dto.HmeCosGetChipMaterialLotListResponseDTO;
import com.ruike.hme.api.dto.HmeCosGetChipNcListDTO;
import com.ruike.hme.domain.vo.HmeCosGetChipPlatformVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * COS取片表Mapper
 *
 * @author chaonan.hu@hand-china.com 2020-11-03 13:58:30
 */
public interface HmeCosGetChipPlatformMapper {

    /**
     * 投入条码列表查询
     *
     * @param tenantId 租户Id
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/3 14:02:46
     * @return java.util.List<com.ruike.hme.api.dto.HmeCosGetChipMaterialLotListResponseDTO>
     */
    List<HmeCosGetChipMaterialLotListResponseDTO> queryInputMaterialLotList(@Param("tenantId") Long tenantId,
                                                                            @Param("dto") HmeCosGetChipMaterialLotListDTO dto);

    /**
     * 查询条码的芯片不良列表
     * 
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/11/3 15:22:44
     * @return java.util.List<com.ruike.hme.api.dto.HmeCosGetChipNcListDTO>
     */
    List<HmeCosGetChipNcListDTO> queryNcList(@Param("tenantId") Long tenantId,
                                             @Param("materialLotId") String materialLotId);

    /**
     * 根据woId查询工序及工艺之间的关联关系
     *
     * @param tenantId 租户ID
     * @param workOrderId woId
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/12/1 11:09:46
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosGetChipPlatformVO>
     */
    List<HmeCosGetChipPlatformVO> routerStepAndOperationQuery(@Param("tenantId") Long tenantId,
                                                              @Param("workOrderId") String workOrderId);

    /**
     * 根据物料批ID查询芯片上的实验代码
     *
     * @param tenantId 租户ID
     * @param materialLotId 物料批ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/11/8 10:42:37
     * @return java.util.List<java.lang.String>
     */
    List<String> chipLabCodeQuery(@Param("tenantId") Long tenantId, @Param("materialLotId") String materialLotId);
}
