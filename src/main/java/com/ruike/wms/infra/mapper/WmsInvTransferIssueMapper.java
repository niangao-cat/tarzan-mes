package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsCostCtrMaterialDTO3;
import com.ruike.wms.api.dto.WmsInvTransferDTO;
import com.ruike.wms.api.dto.WmsInvTransferDTO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存调拨发出执行Mapper
 *
 * @author jiangling.zheng@hand-china.com 2020-04-27 09:50
 */
public interface WmsInvTransferIssueMapper {

    /**
     * 单据扫码查询
     * @author jiangling.zheng@hand-china.com 2020-04-27 09:50
     * @param tenantId
     * @param instructionDocNum
     * @return
     */
    WmsInvTransferDTO selectDocCondition(@Param("tenantId") Long tenantId,
                                         @Param("instructionDocNum") String instructionDocNum);

    /**
     * 单据行查询
     * @author jiangling.zheng@hand-china.com 2020-04-27 09:50
     * @param tenantId
     * @param sourceDocId
     * @param instructionId
     * @param type
     * @return
     */
    List<WmsInvTransferDTO2> selectDocLineCondition(@Param("tenantId") Long tenantId,
                                                    @Param("sourceDocId") String sourceDocId,
                                                    @Param("instructionId") String instructionId,
                                                    @Param("type") String type);

    /**
     * 物料批查询
     * @author jiangling.zheng@hand-china.com 2020-04-27 09:50
     * @param tenantId
     * @param materialLotIds
     * @return
     */
    List<WmsCostCtrMaterialDTO3> selectMaterialLotCondition(@Param("tenantId") Long tenantId,
                                                            @Param("materialLotIds") List<String> materialLotIds);

    /**
     * 货位查询
     *
     * @param tenantId
     * @param parentLocatorId
     * @param locatorType
     * @return
     * @author jiangling.zheng@hand-china.com 2020-06-08 16:58
     */
    List<String> selectLocatorId(@Param("tenantId") Long tenantId,
                                 @Param("parentLocatorId") String parentLocatorId,
                                 @Param("locatorType") String locatorType);

    /**
     * 获取条码是否已扫描
     *
     * @param tenantId
     * @param materialLotCode
     * @param sourceDocId
     * @author jiangling.zheng@hand-china.com 2020/10/6 21:38
     * @return int
     */
    int  selectMaterialLotCount(@Param("tenantId") Long tenantId,
                                @Param("materialLotCode") String materialLotCode,
                                @Param("sourceDocId") String sourceDocId);

}
