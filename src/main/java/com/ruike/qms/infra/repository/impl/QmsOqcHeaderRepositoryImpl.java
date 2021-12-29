package com.ruike.qms.infra.repository.impl;

import com.ruike.qms.infra.mapper.QmsOqcHeaderMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.qms.domain.entity.QmsOqcHeader;
import com.ruike.qms.domain.repository.QmsOqcHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 出库检头表 资源库实现
 *
 * @author yuchao.wang@hand-china.com 2020-08-28 14:18:10
 */
@Component
public class QmsOqcHeaderRepositoryImpl extends BaseRepositoryImpl<QmsOqcHeader> implements QmsOqcHeaderRepository {

    @Autowired
    private QmsOqcHeaderMapper qmsOqcHeaderMapper;

    /**
     *
     * @Description 查询是否有进行中的检验单
     *
     * @author yuchao.wang
     * @date 2020/8/29 11:22
     * @param tenantId 租户ID
     * @param materialLotCode 物料批条码
     * @return boolean
     *
     */
    @Override
    public boolean checkProcessing(Long tenantId, String materialLotCode) {
        Integer hasNcLoadFlag = qmsOqcHeaderMapper.checkProcessing(tenantId, materialLotCode);
        return !Objects.isNull(hasNcLoadFlag) && 1 == hasNcLoadFlag;
    }
}
