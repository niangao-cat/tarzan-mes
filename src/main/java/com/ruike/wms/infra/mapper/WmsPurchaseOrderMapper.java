package com.ruike.wms.infra.mapper;

import com.ruike.wms.api.dto.WmsPurchaseOrderDTO;
import com.ruike.wms.domain.vo.WmsPurchaseOrderVO;
import org.apache.ibatis.annotations.Param;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import tarzan.instruction.domain.entity.MtInstruction;

import java.util.List;

/**
 * @program: tarzan-mes
 * @description: 采购订单关系表mapper
 * @author: han.zhang
 * @create: 2020/04/03 18:51
 */
public interface WmsPurchaseOrderMapper {

    /**
     * @Description 采购订单头
     * @param codition 查询条件
     * @param valueList 值集
     * @return java.util.List<tarzan.inventory.domain.vo.PurchaseOrderVO>
     * @Date 2020-03-19 12:13
     * @Author han.zhang
     */
    List<WmsPurchaseOrderVO> selectPoDataByCondition(WmsPurchaseOrderDTO codition, List<String> valueList);

    /**
     * 查询头下行数据
     *
     * @param sourceInstructionId
     * @author sanfeng.zhang@hand-china.com 2020/9/3 20:50
     * @return java.util.List<tarzan.instruction.domain.entity.MtInstruction>
     */
    List<MtInstruction> queryMtInstructionList(@Param("sourceInstructionId") String sourceInstructionId);

}