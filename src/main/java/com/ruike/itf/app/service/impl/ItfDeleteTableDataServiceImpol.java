package com.ruike.itf.app.service.impl;

import com.ruike.itf.app.service.ItfDeleteTableDataService;
import com.ruike.itf.domain.vo.ItfDeleteTableDataVO;
import com.ruike.itf.infra.mapper.ItfDeleteTableDataMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.AopProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItfDeleteTableDataServiceImpol implements ItfDeleteTableDataService , AopProxy<ItfDeleteTableDataServiceImpol> {

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private ItfDeleteTableDataMapper itfDeleteTableDataMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void invokeDel(Long tenantId) {
        // 获取值集配置的表名
        List<LovValueDTO> tableList = lovAdapter.queryLovValue("ITF.DELETE_TABLE_DATA", tenantId);
        if(CollectionUtils.isEmpty(tableList)){
            return;
        }

        // 获取值集配置的表查询条件
        List<LovValueDTO> tableConditionList = lovAdapter.queryLovValue("ITF.DELETE_TABLE_DATA_CONDITION", tenantId);
        if(CollectionUtils.isEmpty(tableConditionList)){
            return;
        }

        // 获取值集配置的直接删除的表，针对没有建立主键的表
        List<LovValueDTO> directlyDeleteTableNameLovList = lovAdapter.queryLovValue("ITF.DIRECTLY_DELETE_TABLE_DATA", tenantId);
        List<String> directlyDeleteTableNameList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(directlyDeleteTableNameLovList)){
            directlyDeleteTableNameList = directlyDeleteTableNameLovList.stream()
                    .map(LovValueDTO::getValue)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        Map<String,String> tableMap = tableList.stream().collect(Collectors.toMap(LovValueDTO::getValue , LovValueDTO::getMeaning));

        //特殊逻辑，由于ParentValue长度不能超过30，超长的表名需要转换
        String tableName = Strings.EMPTY;
        for (LovValueDTO tableCondition : tableConditionList
             ) {
            tableName = tableMap.getOrDefault(tableCondition.getParentValue() , Strings.EMPTY);
            if(!Strings.EMPTY.equals(tableName)
                    && tableName.length() > 0){
                tableCondition.setParentValue(tableName);
            }
        }

        Map<String , List<LovValueDTO>> tableConditionMap = tableConditionList.stream().collect(Collectors.groupingBy(LovValueDTO::getParentValue));

        //循环查询接口表数据
        Map<String , ItfDeleteTableDataVO> resultMap = new HashMap<>(tableList.size());
        StringBuilder sqls = new StringBuilder();
        List<String> directlyDeleteSqlList = new ArrayList<>();
        for (LovValueDTO table : tableList
        ) {

            List<LovValueDTO> subTableConditionList = tableConditionMap.getOrDefault(table.getMeaning() , new ArrayList<>());
            if(CollectionUtils.isEmpty(subTableConditionList)){
                continue;
            }

            //特殊逻辑：在值集ITF.DIRECTLY_DELETE_TABLE_DATA中的表，直接拼接删除语句
            if(directlyDeleteTableNameList.contains(table.getMeaning())){
                sqls.append("DELETE FROM ");
                sqls.append(table.getMeaning());
                sqls.append(" WHERE TENANT_ID = " );
                sqls.append(tenantId);
                for (LovValueDTO tableCondition : subTableConditionList
                ) {
                    sqls.append(" AND " + tableCondition.getMeaning());
                }
                directlyDeleteSqlList.add(sqls.toString());
                sqls.setLength(0);
                continue;
            }

            if(StringUtils.isBlank(table.getTag())){
                continue;
            }

            sqls.append("SELECT ");
            sqls.append(table.getTag());
            sqls.append(" FROM ");
            sqls.append(table.getMeaning());
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

            ItfDeleteTableDataVO itfDeleteTableDataVO = new ItfDeleteTableDataVO(table.getTag(),keyIdList);
            resultMap.put(table.getMeaning() , itfDeleteTableDataVO);
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
        for (String sql : directlyDeleteSqlList
             ) {
            itfDeleteTableDataMapper.deleteTableData(sql);
        }
    }
}
