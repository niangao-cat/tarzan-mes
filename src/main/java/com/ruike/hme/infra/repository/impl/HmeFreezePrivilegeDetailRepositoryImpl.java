package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeFreezePrivilegeDetailQueryDTO;
import com.ruike.hme.domain.entity.HmeFreezePrivilegeDetail;
import com.ruike.hme.domain.repository.HmeFreezePrivilegeDetailRepository;
import com.ruike.hme.domain.vo.HmeFreezePrivilegeDetailObjectVO;
import com.ruike.hme.domain.vo.HmeFreezePrivilegeDetailVO;
import com.ruike.hme.infra.mapper.HmeFreezePrivilegeDetailMapper;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.FreezePrivilegeObjectType.PROD_LINE;
import static com.ruike.hme.infra.constant.HmeConstants.FreezePrivilegeObjectType.WAREHOUSE;

/**
 * 条码冻结权限明细 资源库实现
 *
 * @author yonghui.zhu@hand-china.com 2021-02-26 17:41:20
 */
@Component
public class HmeFreezePrivilegeDetailRepositoryImpl extends BaseRepositoryImpl<HmeFreezePrivilegeDetail> implements HmeFreezePrivilegeDetailRepository {
    private final HmeFreezePrivilegeDetailMapper mapper;
    private final MtModLocatorRepository locatorRepository;
    private final MtModProductionLineRepository productionLineRepository;

    public HmeFreezePrivilegeDetailRepositoryImpl(HmeFreezePrivilegeDetailMapper mapper, MtModLocatorRepository locatorRepository, MtModProductionLineRepository productionLineRepository) {
        this.mapper = mapper;
        this.locatorRepository = locatorRepository;
        this.productionLineRepository = productionLineRepository;
    }

    @Override
    public Page<HmeFreezePrivilegeDetailVO> pageByCondition(Long tenantId, HmeFreezePrivilegeDetailQueryDTO dto, PageRequest pageRequest) {
        HmeFreezePrivilegeDetail condition = HmeFreezePrivilegeDetailQueryDTO.toEntity(dto);
        List<HmeFreezePrivilegeDetail> entityList = mapper.select(condition);
        List<HmeFreezePrivilegeDetailVO> list = entityList.stream().map(HmeFreezePrivilegeDetailVO::toRepresentation).collect(Collectors.toList());
        Page<HmeFreezePrivilegeDetailVO> page = WmsCommonUtils.pagedList(pageRequest.getPage(), pageRequest.getSize(), list);
        if (CollectionUtils.isNotEmpty(page.getContent())) {
            representationCompletion(dto.getDetailObjectType(), page.getContent());
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public int batchSave(List<HmeFreezePrivilegeDetail> list) {
        list = list.stream().filter(r -> StringUtils.isBlank(r.getDetailId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        this.saveValidation(list);
        return this.batchInsert(list).size();
    }

    private void saveValidation(List<HmeFreezePrivilegeDetail> list) {
        // 比较传入的数据是否有重复
        Set<String> detailSet = list.stream().map(r -> r.getDetailObjectType() + "-" + r.getDetailObjectId()).collect(Collectors.toSet());
        if (detailSet.size() != list.size()) {
            throw new CommonException("不能维护重复的详情数据");
        }
        List<HmeFreezePrivilegeDetail> dbList = mapper.select(new HmeFreezePrivilegeDetail() {{
            setPrivilegeId(list.get(0).getPrivilegeId());
        }});
        // 比较新增的是否在数据库中已存在
        Set<String> newDetailSet = list.stream().filter(r -> StringUtils.isBlank(r.getDetailId())).map(r -> r.getDetailObjectType() + "-" + r.getDetailObjectId()).collect(Collectors.toSet());
        Set<String> dbDetailSet = dbList.stream().map(r -> r.getDetailObjectType() + "-" + r.getDetailObjectId()).collect(Collectors.toSet());
        // 若新增的数据在数据库中存在，则remove之后会比之前长度小
        int origSize = newDetailSet.size();
        newDetailSet.removeAll(dbDetailSet);
        if (newDetailSet.size() < origSize) {
            throw new CommonException("不能维护重复的详情数据");
        }
    }

    private void representationCompletion(String detailObjectType, List<HmeFreezePrivilegeDetailVO> list) {
        Map<String, HmeFreezePrivilegeDetailObjectVO> objectMap = new HashMap<>(list.size());
        Set<String> objectIds = list.stream().filter(r -> detailObjectType.equals(r.getDetailObjectType())).map(HmeFreezePrivilegeDetailVO::getDetailObjectId).collect(Collectors.toSet());
        switch (detailObjectType) {
            case WAREHOUSE:
                List<MtModLocator> warehouseList = locatorRepository.selectByIds(Strings.join(objectIds, ','));
                objectMap = warehouseList.stream().collect(Collectors.toMap(MtModLocator::getLocatorId, r -> new HmeFreezePrivilegeDetailObjectVO(detailObjectType, r.getLocatorId(), r.getLocatorCode(), r.getLocatorName())));
                break;
            case PROD_LINE:
                List<MtModProductionLine> productionLineList = productionLineRepository.selectByIds(Strings.join(objectIds, ','));
                objectMap = productionLineList.stream().collect(Collectors.toMap(MtModProductionLine::getProdLineId, r -> new HmeFreezePrivilegeDetailObjectVO(detailObjectType, r.getProdLineId(), r.getProdLineCode(), r.getProdLineName())));
                break;
            default:
                break;
        }

        // 完善字段
        Map<String, HmeFreezePrivilegeDetailObjectVO> finalObjectMap = objectMap;
        AtomicInteger seqGen = new AtomicInteger(0);
        list.forEach(detail -> {
            if (finalObjectMap.containsKey(detail.getDetailObjectId())) {
                detail.representationDisplayField(finalObjectMap.get(detail.getDetailObjectId()));
            }
            detail.setSequenceNum(seqGen.incrementAndGet());
        });
    }
}
