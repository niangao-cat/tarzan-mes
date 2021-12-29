package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeWoSnRel;
import com.ruike.itf.api.dto.ErpReducedSettleRadioUpdateDTO;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.domain.vo.MtCommonExtendVO6;
import lombok.Data;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;

import java.io.*;
import java.util.*;

/**
 * @Classname HmeEoJobSnVO16
 * @Description 视图
 * @Date 2020/11/3 14:53
 * @Author yuchao.wang
 */
@Data
public class HmeEoJobSnVO17 implements Serializable {

    private static final long serialVersionUID = -4007704915481948124L;

    @ApiModelProperty("INV_ONHAND_INCREASE事件ID")
    String onhandIncreaseEventId;

    @ApiModelProperty("HmeWoSnRel需要插入的数据")
    List<HmeWoSnRel> hmeWoSnRelInsertList;

    @ApiModelProperty("需要记录的事务明细表数据")
    List<WmsObjectTransactionRequestVO> objectTransactionRequestList;

    @ApiModelProperty("条码批量更新参数-INV_ONHAND_INCREASE事件")
    List<MtMaterialLotVO20> materialLotOnhandUpdateList;

    @ApiModelProperty("条码扩展属性批量更新参数-INV_ONHAND_INCREASE事件")
    List<MtCommonExtendVO6> materialLotAttrOnhandUpdateList;

    @ApiModelProperty("条码批量更新参数-EO_WKC_STEP_COMPLETE事件")
    List<MtMaterialLotVO20> materialLotCompleteUpdateList;

    @ApiModelProperty("条码扩展属性批量更新参数-EO_WKC_STEP_COMPLETE事件")
    List<MtCommonExtendVO6> materialLotAttrCompleteUpdateList;

    @ApiModelProperty("降级品比例接口请求报文")
    List<ErpReducedSettleRadioUpdateDTO> erpReducedSettleRadioUpdateDTOList;

}