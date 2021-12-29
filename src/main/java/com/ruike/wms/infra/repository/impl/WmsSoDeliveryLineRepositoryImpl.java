package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.domain.entity.WmsSoDeliveryContainerNum;
import com.ruike.wms.domain.repository.WmsSoDeliveryContainerNumRepository;
import com.ruike.wms.domain.repository.WmsSoDeliveryLineRepository;
import com.ruike.wms.domain.vo.WmsInstructionWoVO;
import com.ruike.wms.domain.vo.WmsProductPrepareLineVO;
import com.ruike.wms.domain.vo.WmsSoDeliveryLineVO;
import com.ruike.wms.infra.mapper.WmsSoDeliveryLineMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Component;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 发货单行 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/9 20:18
 */
@Component
public class WmsSoDeliveryLineRepositoryImpl implements WmsSoDeliveryLineRepository {
    private final WmsSoDeliveryLineMapper wmsSoDeliveryLineMapper;
    private final WmsSoDeliveryContainerNumRepository wmsSoDeliveryContainerNumRepository;

    public WmsSoDeliveryLineRepositoryImpl(WmsSoDeliveryLineMapper wmsSoDeliveryLineMapper, WmsSoDeliveryContainerNumRepository wmsSoDeliveryContainerNumRepository) {
        this.wmsSoDeliveryLineMapper = wmsSoDeliveryLineMapper;
        this.wmsSoDeliveryContainerNumRepository = wmsSoDeliveryContainerNumRepository;
    }

    @Override
    @ProcessLovValue
    public Page<WmsSoDeliveryLineVO> listByDocId(Long tenantId, PageRequest pageRequest, String instructionDocId) {
        Page<WmsSoDeliveryLineVO> page = PageHelper.doPage(pageRequest, () -> wmsSoDeliveryLineMapper.selectListByDocId(tenantId, instructionDocId));
        if (page.size() > 0) {
            // 补充字段工单号，以及发货箱信息
            List<String> instructionIdList = page.getContent().stream().map(WmsSoDeliveryLineVO::getInstructionId).collect(Collectors.toList());
            List<WmsInstructionWoVO> woList = wmsSoDeliveryLineMapper.selectWoByInstructionIdList(tenantId, instructionIdList);
            Map<String, List<WmsInstructionWoVO>> woMap = woList.stream().collect(Collectors.groupingBy(WmsInstructionWoVO::getInstructionId));
//            List<WmsSoDeliveryContainerNum> containerList = wmsSoDeliveryContainerNumRepository.selectByCondition(Condition.builder(WmsSoDeliveryContainerNum.class).andWhere(Sqls.custom().
//                    andIn(WmsSoDeliveryContainerNum.FIELD_INSTRUCTION_ID, instructionIdList)).build());
//            Map<String, List<WmsSoDeliveryContainerNum>> containerMap = containerList.stream().collect(Collectors.groupingBy(WmsSoDeliveryContainerNum::getInstructionId));
            page.forEach(rec -> {
                if (woMap.containsKey(rec.getInstructionId())) {
                    List<String> woNumList = woMap.get(rec.getInstructionId()).stream().map(WmsInstructionWoVO::getWorkOrderNum).collect(Collectors.toList());
                    rec.setWorkOrderNum(Strings.join(woNumList, '/'));
                }
//                if (containerMap.containsKey(rec.getInstructionId())) {
//                    List<WmsSoDeliveryContainerNum> containers = containerMap.get(rec.getInstructionId());
//                    List<String> containerNumList = containers.stream().map(WmsSoDeliveryContainerNum::getContainerNum).filter(StringUtils::isNotBlank).collect(Collectors.toList());
//                    List<String> sealNumList = containers.stream().map(WmsSoDeliveryContainerNum::getSealNum).filter(StringUtils::isNotBlank).collect(Collectors.toList());
//                    List<String> carNumList = containers.stream().map(WmsSoDeliveryContainerNum::getCarNum).filter(StringUtils::isNotBlank).collect(Collectors.toList());
//                    List<String> licenceNumList = containers.stream().map(WmsSoDeliveryContainerNum::getLicenceNum).filter(StringUtils::isNotBlank).collect(Collectors.toList());
//                    rec.setContainerNum(Strings.join(containerNumList, '/'));
//                    rec.setSealNum(Strings.join(sealNumList, '/'));
//                    rec.setCarNum(Strings.join(carNumList, '/'));
//                    rec.setLicenceNum(Strings.join(licenceNumList, '/'));
//                }
            });
        }
        return page;
    }

    @Override
    public List<WmsProductPrepareLineVO> prepareListGet(Long tenantId, String instructionDocId) {
        List<WmsProductPrepareLineVO> list = wmsSoDeliveryLineMapper.selectPrepareListByDocId(tenantId, instructionDocId);
        list.forEach(rec -> {
            MtModLocator locator = wmsSoDeliveryLineMapper.selectPickUpLocatorByLineId(tenantId, rec.getInstructionId());
            if (Objects.nonNull(locator)) {
                rec.setPickUpLocatorId(locator.getLocatorId());
                rec.setPickUpLocatorCode(locator.getLocatorCode());
            }
            if(StringUtils.isBlank(rec.getSoNum())){
                rec.setSoNum("");
            }
            if(StringUtils.isBlank(rec.getSoLineNum())){
                rec.setSoLineNum("");
            }
        });
        return list;
    }
}
