package com.ruike.qms.domain.repository;

import com.ruike.qms.domain.entity.QmsIqcHeaderHis;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 质检单头历史表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-06 11:43:28
 */
public interface QmsIqcHeaderHisRepository extends BaseRepository<QmsIqcHeaderHis>,AopProxy<QmsIqcHeaderHisRepository> {

    /**
     * 保存质检头历史
     *
     * @param qmsIqcHeaderHis
     * @author sanfeng.zhang@hand-china.com 2020/8/6 14:18
     * @return void
     */
    void createQmsIqcHeaderHis(QmsIqcHeaderHis qmsIqcHeaderHis);

    /**
     * 查询物料批id
     *
     * @param tenantId
     * @param instructionId
     * @author sanfeng.zhang@hand-china.com 2020/8/6 16:09
     * @return java.util.List<java.lang.String>
     */
    List<String> queryMaterialLotIdByLine(Long tenantId,String instructionId);
}
