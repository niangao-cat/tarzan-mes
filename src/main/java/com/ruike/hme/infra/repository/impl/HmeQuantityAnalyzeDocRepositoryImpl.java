package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeQuantityAnalyzeDocDTO;
import com.ruike.hme.api.dto.query.QualityAnalyzeQuery;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeDoc;
import com.ruike.hme.domain.repository.HmeQuantityAnalyzeDocRepository;
import com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO;
import com.ruike.hme.domain.vo.HmeQuantityAnalyzeDocVO2;
import com.ruike.hme.domain.vo.MaterialLotWorkOrderVO;
import com.ruike.hme.domain.vo.QualityAnalyzeRepresentationLineVO;
import com.ruike.hme.infra.mapper.HmeQuantityAnalyzeDocMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 质量文件头表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-01-19 11:10:06
 */
@Component
public class HmeQuantityAnalyzeDocRepositoryImpl extends BaseRepositoryImpl<HmeQuantityAnalyzeDoc> implements HmeQuantityAnalyzeDocRepository {

    @Autowired
    private HmeQuantityAnalyzeDocMapper hmeQuantityAnalyzeDocMapper;

    @Override
    @Deprecated
    public Page<HmeQuantityAnalyzeDocVO> quantityAnalyzeDocQuery(Long tenantId, HmeQuantityAnalyzeDocDTO dto, PageRequest pageRequest) {
        Page<HmeQuantityAnalyzeDocVO> resultPage = PageHelper.doPage(pageRequest, () -> hmeQuantityAnalyzeDocMapper.quantityAnalyzeDocQuery(tenantId, dto));
        for (HmeQuantityAnalyzeDocVO hmeQuantityAnalyzeDocVO : resultPage) {
            List<String> woList = hmeQuantityAnalyzeDocMapper.getWoByMaterialLot(tenantId, hmeQuantityAnalyzeDocVO.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(woList)) {
                String woNum = StringUtils.join(woList, ";");
                hmeQuantityAnalyzeDocVO.setWorkOrderNum(woNum);
            }
        }
        return resultPage;
    }

    @Override
    public Page<HmeQuantityAnalyzeDocVO2> quantityAnalyzeLineQuery(Long tenantId, String docId, PageRequest pageRequest) {
        Page<HmeQuantityAnalyzeDocVO2> resultPage = PageHelper.doPage(pageRequest, () -> hmeQuantityAnalyzeDocMapper.quantityAnalyzeLineQuery(tenantId, docId));
        return resultPage;
    }

    @Override
    @ProcessLovValue
    public Page<QualityAnalyzeRepresentationLineVO> pagedList(Long tenantId, QualityAnalyzeQuery query, PageRequest pageRequest) {
        Page<QualityAnalyzeRepresentationLineVO> page = PageHelper.doPage(pageRequest, () -> hmeQuantityAnalyzeDocMapper.selectList(tenantId, query));
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            AtomicInteger seqGen = new AtomicInteger(pageRequest.getPage() * pageRequest.getSize());
            List<MaterialLotWorkOrderVO> materialLotWorkOrders = hmeQuantityAnalyzeDocMapper.getMaterialLotWorkOrder(tenantId, page.getContent().stream().map(QualityAnalyzeRepresentationLineVO::getMaterialLotId).collect(Collectors.toList()));
            Map<String, String> materialLotWorkOrderMap = materialLotWorkOrders.stream().collect(Collectors.groupingBy(MaterialLotWorkOrderVO::getMaterialLotId, Collectors.mapping(MaterialLotWorkOrderVO::getWorkOrderNum, Collectors.joining(";"))));
            page.getContent().forEach(rec -> {
                rec.setSeqNum(seqGen.incrementAndGet());
                rec.setWorkOrderNum(materialLotWorkOrderMap.getOrDefault(rec.getMaterialLotId(), ""));
            });
        }
        return page;
    }
}
