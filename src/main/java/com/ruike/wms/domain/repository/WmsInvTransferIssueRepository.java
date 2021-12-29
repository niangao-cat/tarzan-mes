package com.ruike.wms.domain.repository;

import com.ruike.qms.domain.repository.QmsMaterialInspExemptRepository;
import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.WmsInvTransferObjectTrxVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import org.hzero.core.base.AopProxy;

import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-04-27 10:16
 */
public interface WmsInvTransferIssueRepository extends AopProxy<WmsInvTransferIssueRepository> {

    /**
     * @Description 库存调拨单据头数据查询
     * @param tenantId
     * @param docBarCode
     * @return WmsInvTransferDTO
     * @Date 2020-04-27 10:16
     * @author jiangling.zheng@hand-china.com
     */
    WmsInvTransferDTO docHeaderQuery(Long tenantId, String docBarCode);

    /**
     * 库存调拨单据头数据查询
     * @param tenantId
     * @param sourceDocId
     * @param instructionId
     * @param type
     * @return WmsInvTransferDTO2
     * @Date 2020-04-27 10:16
     * @author jiangling.zheng@hand-china.com
     */
    List<WmsInvTransferDTO2> docLineQuery(Long tenantId, String sourceDocId, String instructionId, String type);

    /**
     * 条码类型查询
     * @param tenantId
     * @param barCode
     * @return WmsCostCtrMaterialDTO6
     * @Date 2020-04-27 10:16
     * @author jiangling.zheng@hand-china.com
     */
    WmsCostCtrMaterialDTO6 materialLotInfoQuery(Long tenantId, String barCode);

    /**
     * 条码查询
     * @param tenantId
     * @param dto
     * @return List<WmsCostCtrMaterialDTO3>
     * @Date 2020-04-27 10:16
     * @author jiangling.zheng@hand-china.com
     * @return
     */
    List<WmsCostCtrMaterialDTO3> containerOrMaterialLotQuery(Long tenantId, String type, WmsInvTransferDTO3 dto);

    /**
     * 明细查询
     * @author jiangling.zheng@hand-china.com 2020-04-27 09:50:00
     * @param tenantId
     * @param dto
     * @return
     */
    WmsInvTransferDTO7 docDetailQuery(Long tenantId, String type, WmsInvTransferDTO6 dto);

    void addObjectTransaction(WmsInvTransferObjectTrxVO dto,
                              WmsCostCtrMaterialDTO3 lotDto,
                              List<WmsObjectTransactionRequestVO> objectTransactionList);
}
