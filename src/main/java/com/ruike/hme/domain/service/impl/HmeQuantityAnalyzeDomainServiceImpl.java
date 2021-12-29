package com.ruike.hme.domain.service.impl;

import com.ruike.hme.api.dto.query.QualityAnalyzeQuery;
import com.ruike.hme.api.dto.representation.QualityAnalyzeRepresentation;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;
import com.ruike.hme.domain.repository.HmeQuantityAnalyzeDocRepository;
import com.ruike.hme.domain.repository.HmeQuantityAnalyzeLineRepository;
import com.ruike.hme.domain.service.HmeQuantityAnalyzeDomainService;
import com.ruike.hme.domain.vo.QualityAnalyzeRepresentationLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ruike.itf.domain.vo.QualityAnalyzeIfaceVO.TYPE_D;
import static com.ruike.itf.infra.constant.ItfConstant.LovCode.ITF_QUALITY_ANALYZE_D_MAP;
import static com.ruike.itf.infra.constant.ItfConstant.LovCode.ITF_QUALITY_ANALYZE_G_MAP;

/**
 * <p>
 * 质量文件解析 领域服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/8 11:16
 */
@Service
public class HmeQuantityAnalyzeDomainServiceImpl implements HmeQuantityAnalyzeDomainService {
    private final HmeQuantityAnalyzeDocRepository quantityAnalyzeDocRepository;
    private final HmeQuantityAnalyzeLineRepository quantityAnalyzeLineRepository;
    private final LovAdapter lovAdapter;

    public HmeQuantityAnalyzeDomainServiceImpl(HmeQuantityAnalyzeDocRepository quantityAnalyzeDocRepository, HmeQuantityAnalyzeLineRepository quantityAnalyzeLineRepository, LovAdapter lovAdapter) {
        this.quantityAnalyzeDocRepository = quantityAnalyzeDocRepository;
        this.quantityAnalyzeLineRepository = quantityAnalyzeLineRepository;
        this.lovAdapter = lovAdapter;
    }

    @Override
    public QualityAnalyzeRepresentation page(Long tenantId, QualityAnalyzeQuery query, PageRequest pageRequest) {
        List<String> titleList = getDynamicTitleList(tenantId, query);
        Page<QualityAnalyzeRepresentationLineVO> lineList = quantityAnalyzeDocRepository.pagedList(tenantId, query, pageRequest);
        QualityAnalyzeRepresentation representation = new QualityAnalyzeRepresentation(titleList, lineList);
        if (CollectionUtils.isNotEmpty(lineList.getContent())) {
            Map<String, List<HmeQuantityAnalyzeLine>> lineMap = getDocLinesMap(lineList);
            int columnCount = titleList.size();
            lineList.getContent().forEach(doc -> {
                List<HmeQuantityAnalyzeLine> docLines = lineMap.get(doc.getQaDocId());
                doc.setResultList(getResultList(docLines, columnCount));
            });
        }
        return representation;
    }

    private List<String> getResultList(List<HmeQuantityAnalyzeLine> docLines, int columnCount) {
        List<String> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(docLines)) {
            Map<String, String> lineResultMap = docLines.stream().collect(Collectors.toMap(HmeQuantityAnalyzeLine::getTagCode, HmeQuantityAnalyzeLine::getResult, (a, b) -> a));
            for (int index = 1; index <= columnCount; index++) {
                resultList.add(lineResultMap.getOrDefault("TQ" + String.format("%02d", index), ""));
            }
        } else {
            for (int index = 1; index <= columnCount; index++) {
                resultList.add("");
            }
        }

        return resultList;
    }

    private Map<String, List<HmeQuantityAnalyzeLine>> getDocLinesMap(Page<QualityAnalyzeRepresentationLineVO> lineList) {
        List<String> qaDocIds = lineList.getContent().stream().map(QualityAnalyzeRepresentationLineVO::getQaDocId).collect(Collectors.toList());
        List<HmeQuantityAnalyzeLine> lines = quantityAnalyzeLineRepository.selectByCondition(Condition.builder(HmeQuantityAnalyzeLine.class).andWhere(Sqls.custom().andIn(HmeQuantityAnalyzeLine.FIELD_QA_DOC_ID, qaDocIds)).build());
        return lines.stream().collect(Collectors.groupingBy(HmeQuantityAnalyzeLine::getQaDocId));
    }

    private List<String> getDynamicTitleList(Long tenantId, QualityAnalyzeQuery query) {
        List<LovValueDTO> lovValues = lovAdapter.queryLovValue(TYPE_D.equals(query.getQaType()) ? ITF_QUALITY_ANALYZE_D_MAP : ITF_QUALITY_ANALYZE_G_MAP, tenantId);
        return lovValues.stream().sorted(Comparator.comparing(LovValueDTO::getOrderSeq)).map(LovValueDTO::getMeaning).collect(Collectors.toList());
    }
}
