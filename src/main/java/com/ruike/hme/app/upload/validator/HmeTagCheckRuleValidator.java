package com.ruike.hme.app.upload.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.TagCheckRuleHeader;
import com.ruike.hme.domain.entity.TagCheckRuleLine;
import com.ruike.hme.domain.repository.TagCheckRuleHeaderRepository;
import com.ruike.hme.domain.repository.TagCheckRuleLineRepository;
import com.ruike.hme.domain.vo.HmeTagCheckRuleVO;
import com.ruike.wms.domain.entity.WmsItemGroup;
import com.ruike.wms.domain.repository.WmsItemGroupRepository;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 数据项展示维护数据校验
 *
 * @author wengang.qiang@hand-chaina.com 2021/08/27 11:54
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME_TAG_CHECK_RULE_IMPORT")
})
public class HmeTagCheckRuleValidator extends ValidatorHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtModAreaRepository mtModAreaRepository;
    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;
    @Autowired
    private WmsItemGroupRepository itemGroupRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtTagRepository mtTagRepository;
    @Autowired
    private TagCheckRuleHeaderRepository tagCheckRuleHeaderRepository;
    @Autowired
    private TagCheckRuleLineRepository tagCheckRuleLineRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;

    @Override
    public boolean validate(String data) {
        //租户判断
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        if (StringUtils.isNotBlank(data)) {
            HmeTagCheckRuleVO tagCheckRuleVO = null;
            Long userId = null;
            //对应用事业部校验
            try {
                tagCheckRuleVO = objectMapper.readValue(data, HmeTagCheckRuleVO.class);
            } catch (IOException e) {
                //失败
                return false;
            }
            MtModArea modArea = new MtModArea();
            modArea.setAreaCode(tagCheckRuleVO.getBusinessCode());
            modArea.setAreaCategory("SYB");
            //根据code和SYB校验
            MtModArea mtModArea = mtModAreaRepository.selectOne(modArea);
            if (Objects.nonNull(mtModArea)) {
                //拿到用户id
                userId = DetailsHelper.getUserDetails().getUserId();
                MtUserOrganization userOrganization = new MtUserOrganization();
                userOrganization.setUserId(userId);
                userOrganization.setOrganizationId(mtModArea.getAreaId());
                userOrganization.setOrganizationType("AREA");
                userOrganization.setEnableFlag("Y");
                MtUserOrganization mtUserOrganization = mtUserOrganizationRepository.selectOne(userOrganization);
                if (Objects.isNull(mtUserOrganization)) {
                    //校验事业部编码是否存在于登录账号权限内，若不存在，则报错【请输入正确的事业部】
                    getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(
                            tenantId, "HME_TAG_CHECK_003", "HME"
                    ));
                    return false;
                }
            }
            tagCheckRuleVO.setBusinessId(mtModArea.getAreaId());

            //物料组编码校验
            if ("COMPONENT_DATA".equals(tagCheckRuleVO.getType())) {
                if (StringUtils.isBlank(tagCheckRuleVO.getItemGroupCode())) {
                    //【请填写物料组编码】
                    getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "HME_TAG_CHECK_005", "HME"));
                    return false;
                }
                //校验物料编码
                WmsItemGroup wmsItemGroup = new WmsItemGroup();
                wmsItemGroup.setItemGroupCode(tagCheckRuleVO.getItemGroupCode());
                WmsItemGroup itemGroup = itemGroupRepository.selectOne(wmsItemGroup);
                if (Objects.isNull(itemGroup)) {
                    //【请填写正确的物料组编码】
                    getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "HME_TAG_CHECK_004", "HME"));
                    return false;
                }
                tagCheckRuleVO.setItemGroupId(itemGroup.getItemGroupId());
            }else{
                if(StringUtils.isNotBlank(tagCheckRuleVO.getItemGroupCode())){
                    //【当前SN数据类型不可填写物料组！】
                    getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "HME_TAG_CHECK_008","HME"));
                    return false;
                }
            }
            //当前工序
            if (StringUtils.isNotBlank(tagCheckRuleVO.getWorkcellName())) {
                MtModWorkcell mtModWorkcell = new MtModWorkcell();
                mtModWorkcell.setWorkcellCode(tagCheckRuleVO.getWorkcellName());
                MtModWorkcell modWorkcell = mtModWorkcellRepository.selectOne(mtModWorkcell);
                //【请填写正确的当前工序！】
                if (Objects.isNull(modWorkcell)) {
                    getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "HME_TAG_CHECK_009", "HME"));
                    return false;
                }
                tagCheckRuleVO.setWorkcellId(modWorkcell.getWorkcellId());
            }
            //sn情况下当前工序校验
            if("SN_DATA".equals(tagCheckRuleVO.getType())){
                if(StringUtils.isBlank(tagCheckRuleVO.getWorkcellName())){
                    getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "HME_TAG_CHECK_013","HME"));
                    return false;
                }
            }

            //来源工序
            if (StringUtils.isNotBlank(tagCheckRuleVO.getSourceWorkcellName())) {
                MtModWorkcell mtModWorkcell = new MtModWorkcell();
                mtModWorkcell.setWorkcellCode(tagCheckRuleVO.getSourceWorkcellName());
                MtModWorkcell workcell = mtModWorkcellRepository.selectOne(mtModWorkcell);
                if (Objects.isNull(workcell)) {
                    getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "HME_TAG_CHECK_010", "HME"));
                    return false;
                }
                tagCheckRuleVO.setSourceWorkcellId(workcell.getWorkcellId());
            }
            //数据项
            if (StringUtils.isNotBlank(tagCheckRuleVO.getTagCode())) {
                MtTag mtTag = new MtTag();
                mtTag.setTagCode(tagCheckRuleVO.getTagCode());
                MtTag tag = mtTagRepository.selectOne(mtTag);
                if (Objects.isNull(tag)) {
                    //【请填写正确的数据项！】
                    getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "HME_TAG_CHECK_011", "HME"));
                    return false;
                }
                tagCheckRuleVO.setTagId(tag.getTagId());
            }
            return this.handleData(tenantId, tagCheckRuleVO);
        }
        return true;
    }

    private boolean handleData(Long tenantId, HmeTagCheckRuleVO tagCheckRuleVO) {
        TagCheckRuleHeader header = new TagCheckRuleHeader();
        header.setBusinessId(tagCheckRuleVO.getBusinessId());
        header.setTenantId(tenantId);
        header.setType(tagCheckRuleVO.getType());
        header.setItemGroupId(tagCheckRuleVO.getItemGroupId());
        header.setWorkcellId(tagCheckRuleVO.getWorkcellId());
        List<TagCheckRuleHeader> checkRuleHeaderList = tagCheckRuleHeaderRepository.select(header);
        if (CollectionUtils.isNotEmpty(checkRuleHeaderList)) {
            for (TagCheckRuleHeader checkRuleHeader : checkRuleHeaderList) {
                TagCheckRuleLine tagCheckRuleLine = new TagCheckRuleLine();
                tagCheckRuleLine.setTenantId(tenantId);
                tagCheckRuleLine.setHeaderId(checkRuleHeader.getHeaderId());
                tagCheckRuleLine.setSourceWorkcellId(tagCheckRuleVO.getSourceWorkcellId());
                tagCheckRuleLine.setTagId(tagCheckRuleVO.getTagId());
                List<TagCheckRuleLine> ruleLine = tagCheckRuleLineRepository.select(tagCheckRuleLine);
                if (CollectionUtils.isNotEmpty(ruleLine)) {
                    //数据项【${1} 】已存在，请检查
                    getContext().addErrorMsg(mtErrorMessageService.getErrorMessageWithModule(tenantId,
                            "HME_TAG_CHECK_007", "HME", tagCheckRuleVO.getTagCode()));
                    return false;
                }
            }
        }
        return true;
    }
}
