package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeWoInputRecord;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 工单投料记录表Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-10-27 17:41:58
 */
public interface HmeWoInputRecordMapper extends BaseMapper<HmeWoInputRecord> {


    /**
     * 获取工单信息
     *
     * @param tenantId
     * @param workOrderNum
     * @author jiangling.zheng@hand-china.com 2020/10/27 20:38
     * @return com.ruike.hme.api.dto.HmeWoInputRecordDTO
     */

    HmeWoInputRecordDTO workOrderGet(@Param("tenantId") Long tenantId,
                                     @Param("workOrderNum") String workOrderNum);

    /**
     * 获取装配清单信息
     * 
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/10/29 17:24 
     * @return java.util.List<com.ruike.hme.api.dto.HmeWoInputRecordDTO4>
     */
    List<HmeWoInputRecordDTO4> woBomCompInfoQuery(@Param("tenantId") Long tenantId,
                                                  @Param("dto") HmeWoInputRecordDTO5 dto);

    /**
     * 获取投料信息
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/10/29 11:30
     * @return java.util.List<com.ruike.hme.api.dto.HmeWoInputRecordDTO2>
     */
    List<HmeWoInputRecordDTO2> woInputRecordQuery(@Param("tenantId") Long tenantId,
                                                  @Param("dto") HmeWoInputRecordDTO3 dto);

    /**
     *  条码信息获取
     *
     * @param tenantId
     * @param materialLotCode
     * @author jiangling.zheng@hand-china.com 2020/10/29 12:32
     * @return com.ruike.hme.api.dto.HmeWoInputRecordDTO2
     */
    HmeWoInputRecordDTO2 materialLotGet(@Param("tenantId") Long tenantId,
                                        @Param("materialLotCode") String materialLotCode);

    /**
     * 获取工单产线
     *
     * @param tenantId
     * @param workOrderId
     * @author jiangling.zheng@hand-china.com 2020/11/6 17:28
     * @return com.ruike.hme.api.dto.HmeWoInputRecordDTO6
     */
    HmeWoInputRecordDTO6 workOrderNumGet(@Param("tenantId") Long tenantId,
                                         @Param("workOrderId") String workOrderId);

    /**
     * 获取替代料信息
     *
     * @param tenantId
     * @param siteId
     * @param workOrderId
     * @param materialId
     * @author jiangling.zheng@hand-china.com 2020/12/14 16:22
     * @return java.util.List<com.ruike.hme.api.dto.HmeWoInputRecordDTO7>
     */
    List<HmeWoInputRecordDTO7> mainSubstituteMaterialGet(@Param("tenantId") Long tenantId,
                                                         @Param("siteId") String siteId,
                                                         @Param("workOrderId") String workOrderId,
                                                         @Param("materialId") String materialId);

    /**
     * 获取工单替代料信息
     *
     * @param tenantId
     * @param siteId
     * @param workOrderId
     * @param materialIdList
     * @author jiangling.zheng@hand-china.com 2020/12/14 19:33
     * @return java.util.List<com.ruike.hme.api.dto.HmeWoInputRecordDTO7>
     */
    List<HmeWoInputRecordDTO7> mainWoMaterialGet(@Param("tenantId") Long tenantId,
                                                         @Param("siteId") String siteId,
                                                         @Param("workOrderId") String workOrderId,
                                                         @Param("materialIdList") List<String> materialIdList);

    /**
     * 获取全局替代料信息
     *
     * @param tenantId
     * @param siteId
     * @param workOrderId
     * @param materialIdList
     * @author jiangling.zheng@hand-china.com 2020/12/14 19:33
     * @return java.util.List<com.ruike.hme.api.dto.HmeWoInputRecordDTO7>
     */
    List<HmeWoInputRecordDTO7> mainGlobalMaterialGet(@Param("tenantId") Long tenantId,
                                                 @Param("siteId") String siteId,
                                                 @Param("workOrderId") String workOrderId,
                                                 @Param("materialIdList") List<String> materialIdList);

    /**
     * 获取不在装配清单内替代料信息
     *
     * @param tenantId
     * @param siteId
     * @param materialIds
     * @param materialId
     * @author jiangling.zheng@hand-china.com 2020/12/14 20:03
     * @return java.lang.String>
     */
    List<String> substituteMaterialGet(@Param("tenantId") Long tenantId,
                                       @Param("siteId") String siteId,
                                       @Param("materialIds") List<String> materialIds,
                                       @Param("materialId") String materialId);

    /**
     * 获取所有存在替代关系的物料已装配数量
     *
     * @param tenantId
     * @param workOrderId
     * @param bomComponentIds
     * @param materialIds
     * @author jiangling.zheng@hand-china.com 2020/12/14 20:29
     * @return java.math.BigDecimal
     */
    BigDecimal assembleQtyGet(@Param("tenantId") Long tenantId,
                              @Param("workOrderId") String workOrderId,
                              @Param("bomComponentIds") List<String> bomComponentIds,
                              @Param("materialIds") List<String> materialIds);

    /**
     * 获取主料对应工艺
     *
     * @param tenantId
     * @param workOrderId
     * @param bomComponentId
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2021/4/11 23:19
     */
    List<String> queryMainMaterialOperationId(@Param("tenantId") Long tenantId,
                                              @Param("workOrderId") String workOrderId,
                                              @Param("bomComponentId") String bomComponentId);
}
