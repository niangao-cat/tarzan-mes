package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.vo.HmeCosFunctionSelectionVO;
import com.ruike.hme.infra.mapper.HmeCosFunctionSelectionMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeCosFunctionSelection;
import com.ruike.hme.domain.repository.HmeCosFunctionSelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 筛选芯片性能表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-08-19 09:37:16
 */
@Component
public class HmeCosFunctionSelectionRepositoryImpl extends BaseRepositoryImpl<HmeCosFunctionSelection> implements HmeCosFunctionSelectionRepository {

    @Autowired
    private HmeCosFunctionSelectionMapper hmeCosFunctionSelectionMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void preSelectionFunctionDataJob(Long tenantId) {
        List<String> loadSequenceList = new ArrayList<>();
        String runMeaning = lovAdapter.queryLovMeaning("HME.COS_SELECTION_FUNCTION_JOB", tenantId, "RUN");
        if("Y".equals(runMeaning)){
            //从临时表中取出LoadSequence
            loadSequenceList = hmeCosFunctionSelectionMapper.getLoadSequence(tenantId);
        }else {
            //查询筛选芯片性能表最大更新时间，无则给默认值当前时间减两个月
            Date maxUpdateDate = hmeCosFunctionSelectionMapper.getFunctionSelectionMaxUpdateDate(tenantId);
            //查询芯片性能表中最后更新时间大于job表最大更新时间的load_sequence，并去重
            loadSequenceList = hmeCosFunctionSelectionMapper.getLoadSequenceByUpdateDate(tenantId, maxUpdateDate);
        }
        if (CollectionUtils.isNotEmpty(loadSequenceList)) {
            loadSequenceList = loadSequenceList.stream().distinct().collect(Collectors.toList());
            //分批根据loadSequence查询其对应的电流点
            List<List<String>> splitLoadSequenceList = CommonUtils.splitSqlList(loadSequenceList, 1000);
            List<HmeCosFunctionSelectionVO> loadSequenceCurrentList = new ArrayList<>();
            for (List<String> split : splitLoadSequenceList) {
                loadSequenceCurrentList.addAll(hmeCosFunctionSelectionMapper.loadSequenceCurrentQuery(tenantId, split));
            }
            if (CollectionUtils.isNotEmpty(loadSequenceCurrentList)) {
                //将电流点拆分
                List<HmeCosFunctionSelectionVO> finalLoadSequenceCurrentList = new ArrayList<>();
                for (HmeCosFunctionSelectionVO loadSequenceCurrent : loadSequenceCurrentList) {
                    List<String> currentList = Arrays.asList(loadSequenceCurrent.getCurrent().split(","));
                    for (String current : currentList) {
                        HmeCosFunctionSelectionVO finalLoadSequence = new HmeCosFunctionSelectionVO();
                        finalLoadSequence.setLoadSequence(loadSequenceCurrent.getLoadSequence());
                        finalLoadSequence.setCurrent(current);
                        finalLoadSequenceCurrentList.add(finalLoadSequence);
                    }
                }
                //根据loadSequence和电流点查询性能数据
                List<List<HmeCosFunctionSelectionVO>> splitLoadSequenceCurrentList = CommonUtils.splitSqlList(finalLoadSequenceCurrentList, 1000);
                List<HmeCosFunctionSelection> hmeCosFunctionSelectionList = new ArrayList<>();
                for (List<HmeCosFunctionSelectionVO> split : splitLoadSequenceCurrentList) {
                    hmeCosFunctionSelectionList.addAll(hmeCosFunctionSelectionMapper.cosFunctionQueryByLoadSequenceCurrent(tenantId, split));
                }
                if (CollectionUtils.isNotEmpty(hmeCosFunctionSelectionList)) {
                    List<String> sqlList = new ArrayList<>();
                    for (HmeCosFunctionSelection hmeCosFunctionSelection : hmeCosFunctionSelectionList) {
                        sqlList.addAll(customDbRepository.getInsertSql(hmeCosFunctionSelection));
                    }
                    List<String> cosFunctionId = hmeCosFunctionSelectionList.stream().map(HmeCosFunctionSelection::getCosFunctionId).distinct().collect(Collectors.toList());
                    //根据cosFunctionId在job表中是否存在来区分哪些数据是新增的，哪些数据是更新的
                    //（因getFullUpdateSql方法存在bug,一是将一个字段有值改为无值时无法实现，二是更新时间永远只用数据库当前时间，不用我指定的时间，故只能先删除再全部新增）
                    List<String> deleteCosFunctionIdList = new ArrayList<>();
                    List<List<String>> splitCosFunctionIdList = CommonUtils.splitSqlList(cosFunctionId, 3000);
                    for (List<String> split:splitCosFunctionIdList) {
                        deleteCosFunctionIdList.addAll(hmeCosFunctionSelectionMapper.getDeleteCosFunctionId(tenantId, split));
                    }
                    if(CollectionUtils.isNotEmpty(deleteCosFunctionIdList)){
                        List<List<String>> splitList = CommonUtils.splitSqlList(deleteCosFunctionIdList, 3000);
                        for (List<String> split:splitList) {
                            hmeCosFunctionSelectionMapper.deleteCosFunctionSelection(split);
                        }
                    }
                    List<List<String>> splitSqlList = CommonUtils.splitSqlList(sqlList, 3000);
                    for (List<String> split : splitSqlList) {
                        jdbcTemplate.batchUpdate(split.toArray(new String[split.size()]));
                    }
                }
            }
        }
    }
}
