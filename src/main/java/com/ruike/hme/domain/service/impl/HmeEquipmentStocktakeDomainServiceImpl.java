package com.ruike.hme.domain.service.impl;

import com.ruike.hme.api.dto.command.HmeEquipmentStocktakeDocCreateCommand;
import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeActualRepresentation;
import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation;
import com.ruike.hme.app.assembler.HmeEquipmentStocktakeActualAssembler;
import com.ruike.hme.app.assembler.HmeEquipmentStocktakeDocAssembler;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeActual;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeDoc;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.repository.HmeEquipmentStocktakeActualRepository;
import com.ruike.hme.domain.repository.HmeEquipmentStocktakeDocRepository;
import com.ruike.hme.domain.service.HmeEquipmentStocktakeDomainService;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;

/**
 * <p>
 * 设备盘点 领域服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 11:13
 */
@Service
@Slf4j
public class HmeEquipmentStocktakeDomainServiceImpl implements HmeEquipmentStocktakeDomainService {
    private final HmeEquipmentStocktakeDocRepository stocktakeDocRepository;
    private final HmeEquipmentStocktakeActualRepository stocktakeActualRepository;
    private final HmeEquipmentStocktakeDocAssembler docAssembler;
    private final HmeEquipmentStocktakeActualAssembler actualAssembler;
    private final HmeEquipmentRepository equipmentRepository;
    private final MtCustomDbRepository mtCustomDbRepository;
    private final JdbcTemplate jdbcTemplate;

    public HmeEquipmentStocktakeDomainServiceImpl(HmeEquipmentStocktakeDocRepository stocktakeDocRepository, HmeEquipmentStocktakeActualRepository stocktakeActualRepository, HmeEquipmentStocktakeDocAssembler docAssembler, HmeEquipmentStocktakeActualAssembler actualAssembler, HmeEquipmentRepository equipmentRepository, MtCustomDbRepository mtCustomDbRepository, JdbcTemplate jdbcTemplate) {
        this.stocktakeDocRepository = stocktakeDocRepository;
        this.stocktakeActualRepository = stocktakeActualRepository;
        this.docAssembler = docAssembler;
        this.actualAssembler = actualAssembler;
        this.equipmentRepository = equipmentRepository;
        this.mtCustomDbRepository = mtCustomDbRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEquipmentStocktakeDocRepresentation createDoc(HmeEquipmentStocktakeDocCreateCommand command) {
        Long startTime = System.currentTimeMillis();
        log.info("HmeEquipmentStocktakeDomainServiceImpl.createDoc  start time:{}", new Date());
        command.validObject(stocktakeDocRepository, stocktakeActualRepository, equipmentRepository);
        Long endTime = System.currentTimeMillis();
        log.info("HmeEquipmentStocktakeDomainServiceImpl.createDoc  validObject time 耗时:{}毫秒", (endTime-startTime));
        if (command.getIgnoreFlag()) {
            return new HmeEquipmentStocktakeDocRepresentation();
        }
        // 保存单据头
        HmeEquipmentStocktakeDoc entity = docAssembler.createCommandToEntity(command);
        stocktakeDocRepository.save(entity);
        // 插入行
        List<HmeEquipmentStocktakeActual> actualList = command.getEquipmentList().stream().map(rec -> actualAssembler.equipmentToEntity(entity.getStocktakeId(), rec)).collect(Collectors.toList());
        this.batchInsetEquipmentStocktakeActual(actualList);
        return stocktakeDocRepository.byId(entity.getStocktakeId(), entity.getTenantId());
    }

    private void batchInsetEquipmentStocktakeActual(List<HmeEquipmentStocktakeActual> actualList) {
        if (CollectionUtils.isNotEmpty(actualList)) {
            List<String> actualIds = mtCustomDbRepository.getNextKeys("hme_equipment_stocktake_actual_s", actualList.size());
            List<String> actualCids = mtCustomDbRepository.getNextKeys("hme_equipment_stocktake_actual_cid_s", actualList.size());
            Integer indexNum = 0;
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            Date currentDate = CommonUtils.currentTimeGet();
            List<String> sqlList = new ArrayList<>();
            for (HmeEquipmentStocktakeActual hmeEquipmentStocktakeActual : actualList) {
                hmeEquipmentStocktakeActual.setStocktakeActualId(actualIds.get(indexNum));
                hmeEquipmentStocktakeActual.setCid(Long.valueOf(actualCids.get(indexNum++)));
                hmeEquipmentStocktakeActual.setCreatedBy(userId);
                hmeEquipmentStocktakeActual.setCreationDate(currentDate);
                hmeEquipmentStocktakeActual.setLastUpdatedBy(userId);
                hmeEquipmentStocktakeActual.setLastUpdateDate(currentDate);

                sqlList.addAll(mtCustomDbRepository.getInsertSql(hmeEquipmentStocktakeActual));
            }
            if (CollectionUtils.isNotEmpty(sqlList)) {
                List<List<String>> commitSqlList = CommonUtils.splitSqlList(sqlList, 1000);
                for (List<String> commitSql : commitSqlList) {
                    jdbcTemplate.batchUpdate(commitSql.toArray(new String[commitSql.size()]));
                }
            }
        }
    }

    @Override
    public Boolean completeValid(String stocktakeId) {
        List<HmeEquipmentStocktakeActualRepresentation> list = stocktakeActualRepository.list(stocktakeId);
        return list.stream().noneMatch(rec -> NO.equals(rec.getStocktakeFlag()));
    }
}
