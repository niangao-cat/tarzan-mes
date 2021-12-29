package com.ruike.itf.app.assembler;

import com.ruike.hme.domain.entity.HmeQuantityAnalyzeDoc;
import com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine;
import com.ruike.hme.infra.util.BeanCopierUtil;
import com.ruike.itf.domain.entity.ItfQualityAnalyzeIface;
import com.ruike.itf.domain.entity.ItfQualityAnalyzeLineIface;
import com.ruike.itf.domain.vo.QualityAnalyzeIfaceVO;
import com.ruike.itf.domain.vo.QualityAnalyzeLineIfaceVO;
import io.choerodon.core.exception.CommonException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine.DYNAMIC_COLUMNS;
import static com.ruike.hme.domain.entity.HmeQuantityAnalyzeLine.TEST_COLUMNS;

/**
 * <p>
 * 质量文件解析 转换器
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/6 16:45
 */
@Component
public class QualityAnalyzeIfaceAssembler {

    public HmeQuantityAnalyzeDoc lineIfaceToDoc(Long tenantId, QualityAnalyzeLineIfaceVO ifaceLine) {
        HmeQuantityAnalyzeDoc doc = new HmeQuantityAnalyzeDoc();
        doc.setMaterialLotId(ifaceLine.getMaterialLotId());
        doc.setMaterialId(ifaceLine.getMaterialId());
        doc.setQuantity(ifaceLine.getQuantity());
        doc.setQaType(ifaceLine.getType());
        doc.setTenantId(tenantId);
        return doc;
    }

    public List<HmeQuantityAnalyzeLine> lineIfaceToDocLine(Long tenantId, String qaDocId, QualityAnalyzeLineIfaceVO ifaceLine, Map<String, String> fieldMap) {
        List<HmeQuantityAnalyzeLine> list = new ArrayList<>();
        for (int i = 1; i <= DYNAMIC_COLUMNS; i++) {
            Field field;
            try {
                field = ifaceLine.getClass().getDeclaredField(i <= TEST_COLUMNS ? "test" + i : "attribute" + (i - TEST_COLUMNS));
            } catch (NoSuchFieldException e) {
                throw new CommonException("无法读取字段！");
            }
            field.setAccessible(true);
            String result;
            try {
                result = Optional.ofNullable(field.get(ifaceLine)).orElse("").toString();
            } catch (IllegalAccessException e) {
                throw new CommonException("读取数据失败！");
            }
            if (StringUtils.isNotBlank(result)) {
                HmeQuantityAnalyzeLine line = new HmeQuantityAnalyzeLine();
                line.setTenantId(tenantId);
                line.setQaDocId(qaDocId);
                line.setTagCode("TQ" + String.format("%02d", i));
                line.setTagDescription(fieldMap.getOrDefault(field.getName().toUpperCase(), field.getName()));
                line.setResult(result);
                list.add(line);
            }
        }
        return list;
    }

    public ItfQualityAnalyzeIface ifaceToEntity(Long tenantId, QualityAnalyzeIfaceVO iface) {
        ItfQualityAnalyzeIface entity = new ItfQualityAnalyzeIface();
        BeanCopierUtil.copy(iface, entity);
        entity.setTenantId(tenantId);
        return entity;
    }

    public ItfQualityAnalyzeLineIface lineIfaceToEntity(Long tenantId, String interfaceId, QualityAnalyzeLineIfaceVO ifaceLine) {
        ItfQualityAnalyzeLineIface entity = new ItfQualityAnalyzeLineIface();
        BeanCopierUtil.copy(ifaceLine, entity);
        entity.setTenantId(tenantId);
        entity.setInterfaceId(interfaceId);
        return entity;
    }

}
