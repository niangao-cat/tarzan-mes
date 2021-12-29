package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.HmeFreezeCosLoadRepresentationDTO;
import com.ruike.hme.api.dto.HmeFreezeDocLineSnQueryDTO;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeFreezeDocLine;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeFreezeDocLineRepository;
import com.ruike.hme.domain.vo.HmeFreezeDocJobVO;
import com.ruike.hme.domain.vo.HmeFreezeDocLineSnUnfreezeVO;
import com.ruike.hme.domain.vo.HmeFreezeDocLineVO;
import com.ruike.hme.infra.mapper.HmeFreezeDocLineMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.sys.CustomSequence;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 条码冻结单行 资源库实现
 *
 * @author yonghui.zhu@hand-china.com 2021-02-22 15:44:41
 */
@Component
public class HmeFreezeDocLineRepositoryImpl extends BaseRepositoryImpl<HmeFreezeDocLine> implements HmeFreezeDocLineRepository {
    private final HmeFreezeDocLineMapper mapper;
    private final HmeEoJobSnRepository eoJobSnRepository;
    private final CustomSequence customSequence;

    public HmeFreezeDocLineRepositoryImpl(HmeFreezeDocLineMapper mapper, HmeEoJobSnRepository eoJobSnRepository, CustomSequence customSequence) {
        this.mapper = mapper;
        this.eoJobSnRepository = eoJobSnRepository;
        this.customSequence = customSequence;
    }

    @Override
    @ProcessLovValue
    public List<HmeFreezeDocLineVO> listGet(Long tenantId, String freezeDocId) {
        List<HmeFreezeDocLineVO> list = mapper.selectRepresentationList(tenantId, new HmeFreezeDocLineSnQueryDTO.Builder().freezeDocId(freezeDocId).build());
        return getLines(tenantId, list);
    }

    @Override
    public List<HmeFreezeDocLine> byDocId(Long tenantId, String freezeDocId) {
        HmeFreezeDocLine condition = new HmeFreezeDocLine();
        condition.setFreezeDocId(freezeDocId);
        return this.select(condition);
    }

    @Override
    @ProcessLovValue
    public Page<HmeFreezeDocLineVO> byCondition(Long tenantId, HmeFreezeDocLineSnQueryDTO dto, PageRequest pageRequest) {
        Page<HmeFreezeDocLineVO> resultPage = PageHelper.doPage(pageRequest, ()->mapper.selectRepresentationList(tenantId, dto));
        if(CollectionUtils.isNotEmpty(resultPage.getContent())){
            List<HmeFreezeDocLineVO> list = resultPage.getContent();
            getLines(tenantId, list);
        }
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(HmeFreezeDocLine line) {
        if (StringUtils.isBlank(line.getFreezeDocLineId())) {
            this.insertSelective(line);
        } else {
            mapper.updateByPrimaryKeySelective(line);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public int batchSave(List<HmeFreezeDocLine> lineList) {
        int splitNum = 500;
        List<HmeFreezeDocLine> insertList = lineList.stream().filter(r -> StringUtils.isBlank(r.getFreezeDocLineId())).collect(Collectors.toList());
        List<HmeFreezeDocLine> updateList = lineList.stream().filter(r -> StringUtils.isNotBlank(r.getFreezeDocLineId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<String> idList = customSequence.getNextKeys("hme_freeze_doc_line_s", insertList.size());
            List<String> cidList = customSequence.getNextKeys("hme_freeze_doc_line_cid_s", insertList.size());
            Iterator<String> idGen = idList.iterator(), cidGen = cidList.iterator();
            Date nowDate = DateUtil.date();
            Long userId = DetailsHelper.getUserDetails().getUserId();
            insertList.forEach(r -> {
                r.setFreezeDocLineId(idGen.next());
                r.setCid(Long.valueOf(cidGen.next()));
                r.setCreatedBy(userId);
                r.setCreationDate(nowDate);
                r.setLastUpdatedBy(userId);
                r.setLastUpdateDate(nowDate);
                r.setObjectVersionNumber(1L);
            });
        }
        List<List<HmeFreezeDocLine>> splitInsertList = CommonUtils.splitSqlList(insertList, splitNum);
        List<List<HmeFreezeDocLine>> splitUpdateList = CommonUtils.splitSqlList(updateList, splitNum);
        return (CollectionUtils.isNotEmpty(insertList) ? splitInsertList.stream().mapToInt(mapper::batchInsert).sum() : 0) + (CollectionUtils.isNotEmpty(updateList) ? splitUpdateList.stream().mapToInt(mapper::batchUpdate).sum() : 0);
    }

    @Override
    public List<HmeFreezeDocLineSnUnfreezeVO> unfreezeSnGet(Long tenantId, List<String> materialLotIds) {
        return mapper.selectSnUnfreezeList(tenantId, materialLotIds);
    }

    @Override
    @ProcessLovValue
    public Page<HmeFreezeCosLoadRepresentationDTO> cosLoadGet(Long tenantId,
                                                              String materialLotId,
                                                              PageRequest pageRequest) {
        Page<HmeFreezeCosLoadRepresentationDTO> page = PageHelper.doPage(pageRequest, () -> mapper.selectCostLoadList(tenantId, materialLotId));

        if (CollectionUtils.isNotEmpty(page.getContent())) {
            cosLoadCompletion(page.getContent());
        }
        return page;
    }

    private void cosLoadCompletion(List<HmeFreezeCosLoadRepresentationDTO> list) {
        AtomicInteger seqGen = new AtomicInteger(0);
        list.forEach(rec -> {
            rec.setPosition(CommonUtils.numberToUpperLetter(rec.getLoadColumn().intValue()) + rec.getLoadRow());
            rec.setSequenceNum(seqGen.incrementAndGet());
        });
    }

    private List<HmeFreezeDocLineVO> getLines(Long tenantId, List<HmeFreezeDocLineVO> list) {
        Set<String> materialLotIdSet = list.stream().map(HmeFreezeDocLineVO::getMaterialLotId).collect(Collectors.toSet());
        // 查询sn下所有的数据
        List<HmeEoJobSn> snList = eoJobSnRepository.selectByCondition(Condition.builder(HmeEoJobSn.class).andWhere(Sqls.custom().andIn(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLotIdSet)).orderBy(HmeEoJobSn.FIELD_MATERIAL_LOT_ID).orderByDesc(HmeEoJobSn.FIELD_CREATION_DATE).build());
        if (CollectionUtils.isNotEmpty(snList)) {
            Map<String, String> snJobMap = snList.stream().collect(Collectors.groupingBy(HmeEoJobSn::getMaterialLotId, Collectors.collectingAndThen(Collectors.toList(), values -> values.get(0).getJobId())));
            List<HmeFreezeDocJobVO> jobList = mapper.selectJobInfoList(tenantId, snJobMap.values());
            Map<String, HmeFreezeDocJobVO> jobMap = jobList.stream().collect(Collectors.toMap(HmeFreezeDocJobVO::getJobId, a -> a, (t, s) -> t));
            list.forEach(rec -> {
                if (snJobMap.containsKey(rec.getMaterialLotId())) {
                    String jobId = snJobMap.get(rec.getMaterialLotId());
                    if(StringUtils.isNotBlank(jobId)){
                        HmeFreezeDocJobVO hmeFreezeDocJobVO = jobMap.get(jobId);
                        if(Objects.nonNull(hmeFreezeDocJobVO)){
                            rec.propertiesCompletion(hmeFreezeDocJobVO);
                        }
                    }
                }
            });
        }
        AtomicInteger seqGen = new AtomicInteger(0);
        list.forEach(rec -> rec.setSequenceNum(seqGen.incrementAndGet()));
        return list;
    }
}
