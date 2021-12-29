package com.ruike.qms.infra.mapper;

import com.ruike.qms.domain.entity.QmsQcDocMaterialLotRel;
import com.ruike.qms.domain.vo.QmsDocMaterialLotVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tarzan.instruction.domain.entity.MtInstructionDoc;

import java.util.List;

/**
 * 二次送检条码Mapper
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-24 17:28:04
 */
public interface QmsQcDocMaterialLotRelMapper extends BaseMapper<QmsQcDocMaterialLotRel> {

    /**
     *  查询送货单
     *
     * @param tenantId      租户id
     * @param actualId      实绩id
     * @author sanfeng.zhang@hand-china.com 2020/8/25 14:22
     * @return java.util.List<tarzan.instruction.domain.entity.MtInstructionDoc>
     */
    List<MtInstructionDoc> queryInstructionDocInfo(Long tenantId, String actualId);

    /**
     * 获取头信息
     *
     * @param tenantId         租户id
     * @param instructionDocId 头表id
     * @return com.ruike.qms.domain.vo.QmsDocMaterialLotVO
     * @author sanfeng.zhang@hand-china.com 2020/8/25 15:40
     */
    QmsDocMaterialLotVO queryInstructionHeaderInfo(Long tenantId, String instructionDocId);

    /**
     * 获取二次送检条码信息
     *
     * @param tenantId
     * @param iqcHeaderId
     * @return java.util.List<com.ruike.qms.domain.entity.QmsQcDocMaterialLotRel>
     * @author sanfeng.zhang@hand-china.com 2021/2/25 11:01
     */
    List<QmsQcDocMaterialLotRel> querySecondMaterialLot(@Param("tenantId") Long tenantId, @Param("iqcHeaderId") String iqcHeaderId);

}
