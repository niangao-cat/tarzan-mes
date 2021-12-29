package com.ruike.hme.app.upload.importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.TagCheckRuleHeader;
import com.ruike.hme.domain.entity.TagCheckRuleLine;
import com.ruike.hme.domain.repository.TagCheckRuleHeaderRepository;
import com.ruike.hme.domain.vo.HmeTagCheckRuleVO;
import com.ruike.hme.infra.mapper.TagCheckRuleHeaderMapper;
import com.ruike.hme.infra.mapper.TagCheckRuleLineMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.domain.entity.WmsItemGroup;
import com.ruike.wms.domain.repository.WmsItemGroupRepository;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.entity.MtTag;
import tarzan.general.domain.repository.MtTagRepository;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据项展示维护导入
 *
 * @author wengang.qiang@hand-china.com 2021/08/27 11:55
 */
@ImportService(templateCode = "HME_TAG_CHECK_RULE_IMPORT")
public class HmeTagCheckRuleImportServiceImpl implements IBatchImportService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtModAreaRepository mtModAreaRepository;
    @Autowired
    private WmsItemGroupRepository itemGroupRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtTagRepository mtTagRepository;
    @Autowired
    private TagCheckRuleHeaderRepository tagCheckRuleHeaderRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;
    @Autowired
    private CustomSequence customSequence;
    @Autowired
    private TagCheckRuleHeaderMapper tagCheckRuleHeaderMapper;
    @Autowired
    private TagCheckRuleLineMapper tagCheckRuleLineMapper;

    @Override
    public Boolean doImport(List<String> data) {
        // 获取租户Id
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long tenantId = curUser == null ? 0L : curUser.getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            List<HmeTagCheckRuleVO> hmeTagCheckRuleVOList = new ArrayList<>();
            for (String vo : data) {
                HmeTagCheckRuleVO hmeTagCheckRuleVO = new HmeTagCheckRuleVO();
                try {
                    //拿到该对象
                    hmeTagCheckRuleVO = objectMapper.readValue(vo, HmeTagCheckRuleVO.class);
                } catch (IOException e) {
                    return false;
                }
                MtModArea modArea = new MtModArea();
                modArea.setAreaCode(hmeTagCheckRuleVO.getBusinessCode());
                modArea.setAreaCategory("SYB");
                //根据code和SYB校验
                MtModArea mtModArea = mtModAreaRepository.selectOne(modArea);
                hmeTagCheckRuleVO.setBusinessId(mtModArea.getAreaId());

                if ("COMPONENT_DATA".equals(hmeTagCheckRuleVO.getType())) {
                    if (StringUtils.isBlank(hmeTagCheckRuleVO.getItemGroupCode())) {
                        //【请填写物料组编码】
                        throw new MtException("HME_TAG_CHECK_005", (mtErrorMessageService.getErrorMessageWithModule(
                                tenantId, "HME_TAG_CHECK_005", "HME"
                        )));
                    }
                    //校验物料编码
                    WmsItemGroup wmsItemGroup = new WmsItemGroup();
                    wmsItemGroup.setItemGroupCode(hmeTagCheckRuleVO.getItemGroupCode());
                    WmsItemGroup itemGroup = itemGroupRepository.selectOne(wmsItemGroup);
                    hmeTagCheckRuleVO.setItemGroupId(itemGroup.getItemGroupId());
                }

                //当前工序
                if (StringUtils.isNotBlank(hmeTagCheckRuleVO.getWorkcellName())) {
                    MtModWorkcell mtModWorkcell = new MtModWorkcell();
                    mtModWorkcell.setWorkcellCode(hmeTagCheckRuleVO.getWorkcellName());
                    MtModWorkcell modWorkcell = mtModWorkcellRepository.selectOne(mtModWorkcell);
                    hmeTagCheckRuleVO.setWorkcellId(modWorkcell.getWorkcellId());
                }

                //来源工序
                if (StringUtils.isNotBlank(hmeTagCheckRuleVO.getSourceWorkcellName())) {
                    MtModWorkcell mtModWorkcell = new MtModWorkcell();
                    mtModWorkcell.setWorkcellCode(hmeTagCheckRuleVO.getSourceWorkcellName());
                    MtModWorkcell workcell = mtModWorkcellRepository.selectOne(mtModWorkcell);
                    hmeTagCheckRuleVO.setSourceWorkcellId(workcell.getWorkcellId());
                }
                //数据项
                if (StringUtils.isNotBlank(hmeTagCheckRuleVO.getTagCode())) {
                    MtTag mtTag = new MtTag();
                    mtTag.setTagCode(hmeTagCheckRuleVO.getTagCode());
                    MtTag tag = mtTagRepository.selectOne(mtTag);
                    hmeTagCheckRuleVO.setTagId(tag.getTagId());
                }
                hmeTagCheckRuleVOList.add(hmeTagCheckRuleVO);
            }
            this.handleData(tenantId, hmeTagCheckRuleVOList);
        }
        return true;
    }

    private void handleData(Long tenantId, List<HmeTagCheckRuleVO> hmeTagCheckRuleVOList) {
        if (CollectionUtils.isEmpty(hmeTagCheckRuleVOList)) {
            return;
        }
        List<String> lineIds = customSequence.getNextKeys("hme_tag_check_rule_line_s", hmeTagCheckRuleVOList.size());
        String lineCid = customSequence.getNextKey("hme_tag_check_rule_line_cid_s");
        //当前时间
        Date date = CommonUtils.currentTimeGet();
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();

        List<TagCheckRuleHeader> tagCheckRuleHeadersList = new ArrayList<>();
        List<TagCheckRuleLine> tagCheckRuleLinesList = new ArrayList<>();
        int lineIndex = 0;
        Map<Object, List<HmeTagCheckRuleVO>> recordMap = new HashMap<>();
        Map<Object, List<HmeTagCheckRuleVO>> groupHeaderMap = hmeTagCheckRuleVOList.stream().collect(Collectors.groupingBy(vo -> this.spliceStr(vo)));
        for (Map.Entry<Object, List<HmeTagCheckRuleVO>> groupHeaderEntry : groupHeaderMap.entrySet()) {
            List<HmeTagCheckRuleVO> value = groupHeaderEntry.getValue();
            //对分组后的数据再进行分组，用于校验excel表中数据是否重复
            Map<Object, List<HmeTagCheckRuleVO>> groupByLineMap = value.stream().collect(Collectors.groupingBy(e -> this.spliceByline(e)));
            if (groupByLineMap.size() != value.size()) {
                groupByLineMap.forEach((lineKey, lineValues) -> {
                    if (lineValues.size() > 1) {
                        //当前SN数据类型下,事业部【${1】当前工序【${1】的来源工序【${1】与数据项【${1】有重复,请检查!
                        if (StringUtils.equals("SN_DATA", lineValues.get(0).getType())) {
                            throw new MtException("HME_TAG_CHECK_014", (mtErrorMessageService.getErrorMessageWithModule(
                                    tenantId, "HME_TAG_CHECK_014", "HME", lineValues.get(0).getBusinessCode(),
                                    lineValues.get(0).getWorkcellName(), lineValues.get(0).getSourceWorkcellName(),
                                    lineValues.get(0).getTagCode()
                            )));
                        }
                        //组件数据类型下,事业部【${1】物料组编码【${1】的来源工序【${1】与数据项【${1】有重复,请检查!
                        if (StringUtils.equals("COMPONENT_DATA", lineValues.get(0).getType())) {
                            if (StringUtils.isNotBlank(lineValues.get(0).getWorkcellId())) {
                                throw new MtException("HME_TAG_CHECK_016", (mtErrorMessageService.getErrorMessageWithModule(
                                        tenantId, "HME_TAG_CHECK_016", "HME", lineValues.get(0).getBusinessCode(),
                                        lineValues.get(0).getItemGroupCode(), lineValues.get(0).getWorkcellName(),
                                        lineValues.get(0).getSourceWorkcellName(),
                                        lineValues.get(0).getTagCode()
                                )));
                            } else {
                                throw new MtException("HME_TAG_CHECK_015", (mtErrorMessageService.getErrorMessageWithModule(
                                        tenantId, "HME_TAG_CHECK_015", "HME", lineValues.get(0).getBusinessCode(),
                                        lineValues.get(0).getItemGroupCode(), lineValues.get(0).getSourceWorkcellName(),
                                        lineValues.get(0).getTagCode()
                                )));
                            }
                        }
                    }
                });
            }
            TagCheckRuleHeader header = new TagCheckRuleHeader();
            header.setBusinessId(value.get(0).getBusinessId());
            header.setTenantId(tenantId);
            header.setType(value.get(0).getType());
            header.setItemGroupId(value.get(0).getItemGroupId());
            header.setWorkcellId(value.get(0).getWorkcellId());
            //校验，用于区分HmeTagCheckRuleVO是否存在，存在，根据头id插入行数据
            TagCheckRuleHeader checkRuleHeader = tagCheckRuleHeaderMapper.queryCheckRuleHeader(tenantId, header);
            //只需要插入行数据
            if (Objects.nonNull(checkRuleHeader)) {
                for (HmeTagCheckRuleVO item : value
                ) {
                    //对行表数据设置
                    TagCheckRuleLine tagCheckRuleLine = new TagCheckRuleLine();
                    tagCheckRuleLine.setHeaderId(checkRuleHeader.getHeaderId());
                    //对行表数据设置是否有效
                    tagCheckRuleLine.setEnableFlag("Y");
                    tagCheckRuleLine.setTenantId(tenantId);
                    tagCheckRuleLine.setLineId(lineIds.get(lineIndex++));
                    tagCheckRuleLine.setCreatedBy(userId);
                    tagCheckRuleLine.setTagId(item.getTagId());
                    tagCheckRuleLine.setSourceWorkcellId(item.getSourceWorkcellId());
                    tagCheckRuleLine.setObjectVersionNumber(1L);
                    tagCheckRuleLine.setCreationDate(date);
                    tagCheckRuleLine.setLastUpdateDate(date);
                    tagCheckRuleLine.setLastUpdatedBy(userId);
                    tagCheckRuleLine.setCid(Long.parseLong(lineCid));
                    tagCheckRuleLinesList.add(tagCheckRuleLine);
                }
            } else {
                recordMap.put(groupHeaderEntry.getKey(), groupHeaderEntry.getValue());
            }
        }
        //新增头数据和行数据
        if (MapUtils.isNotEmpty(recordMap)) {
            int index = 0;
            // 新增头和行
            List<String> headerIds = customSequence.getNextKeys("hme_tag_check_rule_header_s", recordMap.size());
            String cid = customSequence.getNextKey("hme_tag_check_rule_header_cid_s");
            for (Map.Entry<Object, List<HmeTagCheckRuleVO>> record : recordMap.entrySet()) {
                List<HmeTagCheckRuleVO> valueList = record.getValue();
                TagCheckRuleLine tagCheckRuleLine = null;
                TagCheckRuleHeader header = new TagCheckRuleHeader();
                header.setTenantId(tenantId);
                header.setHeaderId(headerIds.get(index));
                header.setRuleCode(valueList.get(0).getRuleCode());
                header.setRuleDescription(valueList.get(0).getRuleDescription());
                header.setItemGroupId(valueList.get(0).getItemGroupId());
                header.setEnableFlag("Y");
                header.setType(valueList.get(0).getType());
                header.setWorkcellId(valueList.get(0).getWorkcellId());
                header.setCreatedBy(userId);
                header.setBusinessId(valueList.get(0).getBusinessId());
                header.setObjectVersionNumber(1L);
                header.setCreationDate(date);
                header.setLastUpdateDate(date);
                header.setLastUpdatedBy(userId);
                header.setCid(Long.parseLong(cid));
                tagCheckRuleHeadersList.add(header);
                for (HmeTagCheckRuleVO item : valueList
                ) {
                    tagCheckRuleLine = new TagCheckRuleLine();
                    tagCheckRuleLine.setTenantId(tenantId);
                    tagCheckRuleLine.setEnableFlag("Y");
                    tagCheckRuleLine.setHeaderId(headerIds.get(index));
                    tagCheckRuleLine.setTagId(item.getTagId());
                    tagCheckRuleLine.setLineId(lineIds.get(lineIndex++));
                    tagCheckRuleLine.setSourceWorkcellId(item.getSourceWorkcellId());
                    tagCheckRuleLine.setObjectVersionNumber(1L);
                    tagCheckRuleLine.setCreationDate(date);
                    tagCheckRuleLine.setCreatedBy(userId);
                    tagCheckRuleLine.setLastUpdateDate(date);
                    tagCheckRuleLine.setLastUpdatedBy(userId);
                    tagCheckRuleLine.setCid(Long.parseLong(lineCid));
                    tagCheckRuleLinesList.add(tagCheckRuleLine);
                }
                index++;
            }
        }
        //批量插入头数据
        if (CollectionUtils.isNotEmpty(tagCheckRuleHeadersList)) {
            tagCheckRuleHeaderMapper.myBatchInsert(tagCheckRuleHeadersList);
        }
        //批量插入行数据
        if (CollectionUtils.isNotEmpty(tagCheckRuleLinesList)) {
            tagCheckRuleLineMapper.myBatchInsert(tagCheckRuleLinesList);
        }
    }

    private String spliceStr(HmeTagCheckRuleVO vo) {
        //用business_id+type+item_group_id+workcell_id构成一个新对象
        StringBuffer sb = new StringBuffer();
        sb.append(vo.getBusinessId()).
                append(vo.getType()).
                append(vo.getItemGroupId()).
                append(vo.getWorkcellId());
        return sb.toString();
    }

    private String spliceByline(HmeTagCheckRuleVO vo) {
        //来源工序和数据项构成新对象
        StringBuffer sb = new StringBuffer();
        sb.append(vo.getSourceWorkcellId()).
                append(vo.getTagId());
        return sb.toString();
    }

}
