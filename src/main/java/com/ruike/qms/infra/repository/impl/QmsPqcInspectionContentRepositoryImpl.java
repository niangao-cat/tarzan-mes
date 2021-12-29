package com.ruike.qms.infra.repository.impl;

import com.ruike.qms.infra.mapper.QmsPqcInspectionContentMapper;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.qms.domain.entity.QmsPqcInspectionContent;
import com.ruike.qms.domain.repository.QmsPqcInspectionContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 巡检检验项目表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-12 16:41:11
 */
@Component
public class QmsPqcInspectionContentRepositoryImpl extends BaseRepositoryImpl<QmsPqcInspectionContent> implements QmsPqcInspectionContentRepository {

    @Autowired
    private QmsPqcInspectionContentMapper qmsPqcInspectionContentMapper;

    @Override
    public List<QmsPqcInspectionContent> queryPqcInspectionContent(Long tenantId, QmsPqcInspectionContent pqcInspectionContent) {
        return qmsPqcInspectionContentMapper.queryPqcInspectionContent(tenantId, pqcInspectionContent);
    }
}
