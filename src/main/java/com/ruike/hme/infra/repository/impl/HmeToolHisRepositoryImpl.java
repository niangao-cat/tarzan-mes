package com.ruike.hme.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ruike.hme.api.dto.HmeToolHisDTO;
import com.ruike.hme.domain.entity.HmeTool;
import com.ruike.hme.domain.vo.HmeToolHisVO;
import com.ruike.hme.infra.mapper.HmeToolHisMapper;
import com.ruike.hme.infra.mapper.HmeToolMapper;
import com.ruike.hme.infra.util.CommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeToolHis;
import com.ruike.hme.domain.repository.HmeToolHisRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工装基础数据历史表 资源库实现
 *
 * @author li.zhang13@hand-china.com 2021-01-07 10:06:46
 */
@Component
public class HmeToolHisRepositoryImpl extends BaseRepositoryImpl<HmeToolHis> implements HmeToolHisRepository {

    @Autowired
    private HmeToolHisMapper hmeToolHisMapper;

    @Autowired
    private HmeToolMapper hmeToolMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @param tenantId
     * @param hmeToolHisDTO
     * @Description 根据工装ID查询工装修改历史记录
     * @Author li.zhang13@hand-china.com
     */
    @Override
    public Page<HmeToolHisVO> selectHmeToolHis(PageRequest pageRequest, String tenantId, HmeToolHisDTO hmeToolHisDTO) {
        return PageHelper.doPage(pageRequest, () -> hmeToolHisMapper.selectHmeToolHis(tenantId,hmeToolHisDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeToolHis> insertHmeToolHis(Long tenantId, List<HmeTool> dtoList) {
        List<HmeToolHis> resultList = new ArrayList<>();
        if(CollectionUtils.isEmpty(dtoList)){
            return resultList;
        }
        List<String> toolIdList = dtoList.stream().map(HmeTool::getToolId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(toolIdList)){
            return resultList;
        }
        List<HmeTool> hmeToolList = hmeToolMapper.selectByCondition(Condition.builder(HmeTool.class)
                .andWhere(Sqls.custom()
                        .andIn(HmeTool.FIELD_TOOL_ID, toolIdList))
                .build());
        if(CollectionUtils.isNotEmpty(hmeToolList)) {
            List<String> cidS = customDbRepository.getNextKeys("hme_tool_his_cid_s", hmeToolList.size());
            List<String> toolHisIdS = customDbRepository.getNextKeys("hme_tool_his_s", hmeToolList.size());
            int count = 0;
            Date date = CommonUtils.currentTimeGet();
            // 获取当前用户
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            List<String> insertHmeToolHisListSql = new ArrayList<>();
            for (HmeTool hmeTool : hmeToolList
            ) {
                HmeToolHis hmeToolHis = new HmeToolHis();
                hmeToolHis.setToolHisId(toolHisIdS.get(count));
                hmeToolHis.setToolId(hmeTool.getToolId());
                hmeToolHis.setTenantId(hmeTool.getTenantId());
                hmeToolHis.setToolName(hmeTool.getToolName());
                hmeToolHis.setAreaId(hmeTool.getAreaId());
                hmeToolHis.setWorkShopId(hmeTool.getWorkShopId());
                hmeToolHis.setWorkcellId(hmeTool.getWorkcellId());
                hmeToolHis.setBrandName(hmeTool.getBrandName());
                hmeToolHis.setSpecification(hmeTool.getSpecification());
                hmeToolHis.setUomId(hmeTool.getUomId());
                hmeToolHis.setQty(hmeTool.getQty());
                hmeToolHis.setRate(hmeTool.getRate());
                hmeToolHis.setEnableFlag(hmeTool.getEnableFlag());
                hmeToolHis.setCid(Long.valueOf(cidS.get(count)));
                hmeToolHis.setObjectVersionNumber(1L);
                hmeToolHis.setCreationDate(date);
                hmeToolHis.setCreatedBy(userId);
                hmeToolHis.setLastUpdatedBy(userId);
                hmeToolHis.setLastUpdateDate(date);
                hmeToolHis.setAttributeCategory(hmeTool.getAttributeCategory());
                hmeToolHis.setAttribute1(hmeTool.getAttribute1());
                hmeToolHis.setAttribute2(hmeTool.getAttribute2());
                hmeToolHis.setAttribute3(hmeTool.getAttribute3());
                hmeToolHis.setAttribute4(hmeTool.getAttribute4());
                hmeToolHis.setAttribute5(hmeTool.getAttribute5());
                hmeToolHis.setAttribute6(hmeTool.getAttribute6());
                hmeToolHis.setAttribute7(hmeTool.getAttribute7());
                hmeToolHis.setAttribute8(hmeTool.getAttribute8());
                hmeToolHis.setAttribute9(hmeTool.getAttribute9());
                hmeToolHis.setAttribute10(hmeTool.getAttribute10());
                hmeToolHis.setAttribute11(hmeTool.getAttribute11());
                hmeToolHis.setAttribute12(hmeTool.getAttribute12());
                hmeToolHis.setAttribute13(hmeTool.getAttribute13());
                hmeToolHis.setAttribute14(hmeTool.getAttribute14());
                hmeToolHis.setAttribute15(hmeTool.getAttribute15());
                resultList.add(hmeToolHis);
                insertHmeToolHisListSql.addAll(customDbRepository.getInsertSql(hmeToolHis));
                count++;
            }
            if(CollectionUtils.isNotEmpty(insertHmeToolHisListSql)){
                jdbcTemplate.batchUpdate(insertHmeToolHisListSql.toArray(new String[insertHmeToolHisListSql.size()]));
            }
        }
        return resultList;
    }
}
