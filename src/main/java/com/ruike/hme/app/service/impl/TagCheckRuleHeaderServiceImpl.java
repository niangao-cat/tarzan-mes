package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.TagCheckRuleHeaderDTO;
import com.ruike.hme.app.service.TagCheckRuleHeaderService;
import com.ruike.hme.domain.entity.TagCheckRuleHeader;
import com.ruike.hme.domain.entity.TagCheckRuleLine;
import com.ruike.hme.domain.repository.TagCheckRuleHeaderRepository;
import com.ruike.hme.infra.mapper.TagCheckRuleHeaderMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 数据项展示规则维护头表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-08-25 16:23:18
 */
@Service
public class TagCheckRuleHeaderServiceImpl extends BaseAppService implements TagCheckRuleHeaderService {

    private final TagCheckRuleHeaderMapper checkRuleHeaderMapper;
    private final TagCheckRuleHeaderRepository checkRuleHeaderRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final CustomSequence customSequence;

    public TagCheckRuleHeaderServiceImpl(TagCheckRuleHeaderMapper checkRuleHeaderMapper, TagCheckRuleHeaderRepository checkRuleHeaderRepository, MtErrorMessageRepository mtErrorMessageRepository, CustomSequence customSequence) {
        this.checkRuleHeaderMapper = checkRuleHeaderMapper;
        this.checkRuleHeaderRepository = checkRuleHeaderRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.customSequence = customSequence;
    }


    @Override
    public Page<TagCheckRuleHeader> list(Long tenantId, TagCheckRuleHeaderDTO checkRuleHeaderDTO, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> checkRuleHeaderMapper.query(tenantId, checkRuleHeaderDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Long tenantId, TagCheckRuleHeader tagCheckRuleHeader) {
        TagCheckRuleHeader tagCheckRuleHeaders = new TagCheckRuleHeader();
        tagCheckRuleHeaders.setBusinessId(tagCheckRuleHeader.getBusinessId());
        tagCheckRuleHeaders.setTenantId(tagCheckRuleHeader.getTenantId());
        tagCheckRuleHeaders.setType(tagCheckRuleHeader.getType());
        tagCheckRuleHeaders.setItemGroupId(tagCheckRuleHeader.getItemGroupId());
        tagCheckRuleHeaders.setWorkcellId(tagCheckRuleHeader.getWorkcellId());
        if (StringUtils.isNotBlank(tagCheckRuleHeader.getHeaderId())) {
            //头表修改数据操作
            TagCheckRuleHeader ruleHeaders = checkRuleHeaderMapper.queryCheckRuleHeader(tenantId, tagCheckRuleHeaders);
            if (Objects.nonNull(ruleHeaders) && !StringUtils.equals(ruleHeaders.getHeaderId(), tagCheckRuleHeader.getHeaderId())) {
                //报错
                throw new MtException("HME_TAG_CHECK_022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_TAG_CHECK_022", "HME"));
            }
            //通过序列获取cid,用于批量插入
            String cid = customSequence.getNextKey("hme_tag_check_rule_header_cid_s");
            // 获取当前用户
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            tagCheckRuleHeader.setCid(Long.parseLong(cid));
            tagCheckRuleHeader.setLastUpdatedBy(userId);
            checkRuleHeaderMapper.myBatchUpdate(tenantId, tagCheckRuleHeader);
        } else {
            //头表添加数据操作
            TagCheckRuleHeader ruleHeaders = checkRuleHeaderMapper.queryCheckRuleHeader(tenantId, tagCheckRuleHeaders);
            if (Objects.nonNull(ruleHeaders)) {
                //报错
                throw new MtException("HME_TAG_CHECK_022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_TAG_CHECK_022", "HME"));
            }
            checkRuleHeaderRepository.insertSelective(tagCheckRuleHeader);
        }
    }

    @Override
    public Page<TagCheckRuleLine> queryById(Long tenantId, String headerId, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> checkRuleHeaderMapper.queryById(tenantId, headerId));
    }
}
