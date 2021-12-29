package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.TagCheckRuleLineService;
import com.ruike.hme.domain.entity.TagCheckRuleLine;
import com.ruike.hme.domain.repository.TagCheckRuleLineRepository;
import com.ruike.hme.infra.mapper.TagCheckRuleLineMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据项展示规则维护行表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-08-25 16:23:20
 */
@Service
public class TagCheckRuleLineServiceImpl extends BaseAppService implements TagCheckRuleLineService {

    private final TagCheckRuleLineRepository tagCheckRuleLineRepository;
    private final TagCheckRuleLineMapper tagCheckRuleLineMapper;
    private final MtErrorMessageRepository mtErrorMessageRepository;

    public TagCheckRuleLineServiceImpl(TagCheckRuleLineRepository tagCheckRuleLineRepository, TagCheckRuleLineMapper tagCheckRuleLineMapper, MtErrorMessageRepository mtErrorMessageRepository) {
        this.tagCheckRuleLineRepository = tagCheckRuleLineRepository;
        this.tagCheckRuleLineMapper = tagCheckRuleLineMapper;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Long tenantId, TagCheckRuleLine tagCheckRuleLine) {
        TagCheckRuleLine checkRuleLine = new TagCheckRuleLine();
        checkRuleLine.setHeaderId(tagCheckRuleLine.getHeaderId());
        checkRuleLine.setSourceWorkcellId(tagCheckRuleLine.getSourceWorkcellId());
        checkRuleLine.setTagId(tagCheckRuleLine.getTagId());
        //用头id,来源工序，数据项校验
        List<TagCheckRuleLine> ruleLines = tagCheckRuleLineRepository.select(checkRuleLine);
        //修改数据逻辑
        if (StringUtils.isNotBlank(tagCheckRuleLine.getLineId())) {
            if (CollectionUtils.isNotEmpty(ruleLines) && !StringUtils.equals(ruleLines.get(0).getLineId(),
                    tagCheckRuleLine.getLineId())) {
                throw new MtException("HME_TAG_CHECK_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_TAG_CHECK_002", "HME"));
            }
            //更新数据
            tagCheckRuleLineMapper.updateByPrimaryKeySelective(tagCheckRuleLine);
        } else {
            //插入数据
            if (CollectionUtils.isNotEmpty(ruleLines)) {
                throw new MtException("HME_TAG_CHECK_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_TAG_CHECK_002", "HME"));
            }
            tagCheckRuleLine.setTenantId(tenantId);
            tagCheckRuleLineRepository.insertSelective(tagCheckRuleLine);
        }
    }
}
