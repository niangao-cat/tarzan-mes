package com.ruike.qms.app.service.impl;

import com.ruike.qms.api.dto.QmsTransitionRuleDTO;
import com.ruike.qms.api.dto.QmsTransitionRuleDTO2;
import com.ruike.qms.app.service.QmsTransitionRuleService;
import com.ruike.qms.domain.entity.QmsTransitionRule;
import com.ruike.qms.domain.repository.QmsTransitionRuleRepository;
import com.ruike.qms.infra.mapper.QmsTransitionRuleMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.MtBaseConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.general.api.dto.MtUserOrganizationDTO6;

import java.util.List;


/**
 * 检验水平转移规则表应用服务默认实现
 *
 * @author tong.li05@hand-china.com 2020-05-11 09:54:52
 */
@Service
public class QmsTransitionRuleServiceImpl implements QmsTransitionRuleService {

    @Autowired
    private QmsTransitionRuleMapper transitionRuleMapper;

    @Autowired
    private QmsTransitionRuleRepository qmsTransitionRuleRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    /**
    * @param pageRequest 1
    * @param transitionRule 2
    * @param tenantId 3
    * @return : io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsTransitionRuleDTO>
    * @Description: 检验水平转移基础设置  查询
    * @author: tong.li
    * @date 2020/5/11 14:00
    * @version 1.0
    */
    @Override
    @ProcessLovValue
    public Page<QmsTransitionRuleDTO> query(PageRequest pageRequest, QmsTransitionRule transitionRule, Long tenantId) {
        Page<QmsTransitionRuleDTO> resultList =  PageHelper.doPageAndSort(pageRequest,()->transitionRuleMapper.query(tenantId, transitionRule.getSiteId(),transitionRule.getMaterialId()));
        return resultList;
    }

    /**
    * @param tenantId 1
    * @param transitionRule 2
    * @return : com.ruike.qms.domain.entity.QmsTransitionRule
    * @Description: 检验水平转移基础设置  新建或者更新
    * @author: tong.li
    * @date 2020/5/11 15:52
    * @version 1.0
    */
    @Override
    public QmsTransitionRule createOrUpdate(Long tenantId, QmsTransitionRule transitionRule) {

        //2020/5/15 新增校验  加严不合格限不得大于加严连续批
        if(transitionRule.getNgBatches()>transitionRule.getTightenedBatches()){
            throw new MtException("QMS_MATERIAL_INSP_P0037", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_P0037", "QMS"));
        }

        //判断是新增还是更新
        if(StringUtils.isNotEmpty(transitionRule.getTransitionRuleId())){
            QmsTransitionRule check = new QmsTransitionRule();
            check.setSiteId(transitionRule.getSiteId());
            check.setMaterialId(transitionRule.getMaterialId());
            List<QmsTransitionRule> checkList = transitionRuleMapper.select(check);
            if(CollectionUtils.isNotEmpty(checkList)&&!transitionRule.getTransitionRuleId().equals(checkList.get(0).getTransitionRuleId())) {
                throw new MtException("QMS_MATERIAL_INSP_P0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_P0036", "QMS"));
            }


            QmsTransitionRule version = new QmsTransitionRule();
            version.setTenantId(transitionRule.getTenantId());
            version.setTransitionRuleId(transitionRule.getTransitionRuleId());
            QmsTransitionRule qmsTransitionRule = transitionRuleMapper.selectOne(version);
            transitionRule.setObjectVersionNumber(qmsTransitionRule.getObjectVersionNumber());
            //更新
            transitionRuleMapper.updateByPrimaryKeySelective(transitionRule);
        }else {
            //新增
            //校验 组织+物料在数据库中是否存在
            List<QmsTransitionRuleDTO> checkList = transitionRuleMapper.query(tenantId, transitionRule.getSiteId(), transitionRule.getMaterialId());

            if(CollectionUtils.isNotEmpty(checkList)) {
                throw new MtException("QMS_MATERIAL_INSP_P0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_P0036", "QMS"));
            }
            transitionRule.setTenantId(tenantId);
            int rule = qmsTransitionRuleRepository.insertSelective(transitionRule);
        }

        return transitionRule;
    }

    /**
    * @param tenantId 1
    * @param transitionRuleIdList 2
    * @return : void
    * @Description: 删除
    * @author: tong.li
    * @date 2020/5/13 10:40
    * @version 1.0
    */
    @Override
    public void delete(Long tenantId,  List<String> transitionRuleIdList) {
        for (String transitionRuleId : transitionRuleIdList) {
            QmsTransitionRule rule = new QmsTransitionRule();
            rule.setTransitionRuleId(transitionRuleId);
            qmsTransitionRuleRepository.deleteByPrimaryKey(rule);
        }

    }

    @Override
    public QmsTransitionRuleDTO2 userDefaultSiteForUi(Long tenantId) {
        Long userId = DetailsHelper.getUserDetails().getUserId();
        List<QmsTransitionRuleDTO2> result = transitionRuleMapper.userSiteListForUi(tenantId, userId, MtBaseConstants.YES);

        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        return result.get(0);
    }
}
