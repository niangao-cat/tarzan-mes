package com.ruike.qms.domain.repository;

import com.ruike.hme.domain.vo.HmeChipTransferVO4;
import com.ruike.qms.domain.entity.QmsQcDocMaterialLotRel;
import com.ruike.qms.domain.vo.QmsDocMaterialLotVO;
import com.ruike.qms.domain.vo.QmsDocMaterialLotVO2;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 二次送检条码资源库
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-24 17:28:04
 */
public interface QmsQcDocMaterialLotRelRepository extends BaseRepository<QmsQcDocMaterialLotRel>, AopProxy<QmsQcDocMaterialLotRelRepository> {


    /**
     * 条码扫描
     *
     * @param tenantId
     * @param materialLotCode       条码
     * @param instructionDocNum     送货单
     * @author sanfeng.zhang@hand-china.com 2020/8/25 10:57
     * @return java.lang.String
     */
    QmsDocMaterialLotVO scanMaterialLot(Long tenantId, String materialLotCode, String instructionDocNum);

    /**
     * 删除条码
     *
     * @param tenantId      租户id
     * @param vo4           参数
     * @author sanfeng.zhang@hand-china.com 2020/8/25 17:00
     * @return void
     */
    void deleteMaterialLotCode(Long tenantId, HmeChipTransferVO4 vo4);

    /**
     * 确认
     * 
     * @param tenantId      租户id
     * @param vo2           参数
     * @author sanfeng.zhang@hand-china.com 2020/8/25 17:33 
     * @return void
     */
    void confirmMaterialLotCode(Long tenantId, QmsDocMaterialLotVO2 vo2);

    /**
     * 提交
     * 
     * @param tenantId      租户id
     * @param vo2           参数
     * @author sanfeng.zhang@hand-china.com 2020/8/25 19:10 
     * @return void
     */
    QmsDocMaterialLotVO2 submitMaterialLotCode(Long tenantId, QmsDocMaterialLotVO2 vo2);
}
