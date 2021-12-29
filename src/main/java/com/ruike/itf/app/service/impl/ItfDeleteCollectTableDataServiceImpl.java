package com.ruike.itf.app.service.impl;

import com.ruike.itf.app.service.ItfDeleteCollectTableDataService;
import com.ruike.itf.domain.vo.ItfDeleteTableDataVO;
import com.ruike.itf.infra.mapper.ItfDeleteTableDataMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.AopProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备数据采集接口表数据删除应用服务
 * 保留半个月的数据以及历史的错误数据
 *
 * @author penglin.sui@hand-china.com 2021-07-24 11:54
 */
@Service
public class ItfDeleteCollectTableDataServiceImpl implements ItfDeleteCollectTableDataService, AopProxy<ItfDeleteCollectTableDataServiceImpl> {

    @Autowired
    private LovAdapter lovAdapter;
    
    @Autowired
    private ItfDeleteTableDataMapper itfDeleteTableDataMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void invokeDel(Long tenantId) {
        // 获取值集配置的表名
        List<LovValueDTO> tableList = lovAdapter.queryLovValue("ITF.DELETE_COLLECT_TABLE_DATA", tenantId);
        if(CollectionUtils.isEmpty(tableList)){
            return;
        }

        // 获取值集配置的公用查询条件
        List<LovValueDTO> commonConditioinList = lovAdapter.queryLovValue("ITF.DELETE_COLLECT_TABLE_DATA_COMMON_CONDITION", tenantId);

        // 获取值集配置的单表的查询条件
        List<LovValueDTO> tableConditionList = lovAdapter.queryLovValue("ITF.DELETE_COLLECT_TABLE_DATA_CONDITION", tenantId);
        if(CollectionUtils.isEmpty(commonConditioinList)
            && CollectionUtils.isEmpty(tableConditionList)){
            return;
        }

        Map<String , List<LovValueDTO>> tableConditionMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(tableConditionList)){
            tableConditionMap = tableConditionList.stream().collect(Collectors.groupingBy(LovValueDTO::getParentValue));
        }

        //循环查询接口表数据
        Map<String , ItfDeleteTableDataVO> resultMap = new HashMap<>(tableList.size());
        StringBuilder sqls = new StringBuilder();
        for (LovValueDTO table : tableList
             ) {
            if(StringUtils.isBlank(table.getTag())){
                continue;
            }

            List<LovValueDTO> subTableConditionList = tableConditionMap.getOrDefault(table.getValue() , new ArrayList<>());
            if(CollectionUtils.isEmpty(subTableConditionList)){
                //没有单独的查询条件，使用共用的查询条件
                subTableConditionList.addAll(commonConditioinList);
            }
            sqls.append("SELECT ");
            sqls.append(table.getTag());
            sqls.append(" FROM ");
            sqls.append(table.getValue());
            sqls.append(" WHERE TENANT_ID = " );
            sqls.append(tenantId);
            for (LovValueDTO tableCondition : subTableConditionList
                 ) {
                sqls.append(" AND " + tableCondition.getMeaning());
            }
            
            List<String> keyIdList = itfDeleteTableDataMapper.selectTableData(sqls.toString());
            sqls.setLength(0);
            if(CollectionUtils.isEmpty(keyIdList)){
                continue;
            }
            ItfDeleteTableDataVO itfDeleteTableDataVO = new ItfDeleteTableDataVO(table.getTag() , keyIdList);
            resultMap.put(table.getValue() , itfDeleteTableDataVO);
        }

        //按主键分批次删除数据
        sqls.setLength(0);
        for (Map.Entry<String, ItfDeleteTableDataVO> entry : resultMap.entrySet()) {
            List<List<String>> splitKeyIdList = InterfaceUtils.splitSqlList(entry.getValue().getKeyIdList(), 5000);
            for (List<String> keyIdList : splitKeyIdList) {
                sqls.append("DELETE FROM ");
                sqls.append(entry.getKey());
                sqls.append(" WHERE ");
                sqls.append(entry.getValue().getKeyIdName());
                sqls.append(" IN (");
                keyIdList.forEach(keyId ->{
                    sqls.append("'");
                    sqls.append(keyId);
                    sqls.append("'");
                    sqls.append(",");
                });
                //删除最后的 “,”
                sqls.deleteCharAt(sqls.length()-1);
                sqls.append(")");
                itfDeleteTableDataMapper.deleteTableData(sqls.toString());
                sqls.setLength(0);
            }
        }
    }
}
