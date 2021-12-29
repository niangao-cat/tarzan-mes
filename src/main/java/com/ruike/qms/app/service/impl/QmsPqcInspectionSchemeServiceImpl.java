package com.ruike.qms.app.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import com.ruike.qms.api.dto.QmsPqcInspectionSchemeDTO;
import com.ruike.qms.app.service.QmsPqcInspectionSchemeService;
import com.ruike.qms.domain.entity.QmsPqcGroupRel;
import com.ruike.qms.domain.entity.QmsPqcInspectionContent;
import com.ruike.qms.domain.entity.QmsPqcInspectionScheme;
import com.ruike.qms.domain.repository.QmsPqcGroupRelRepository;
import com.ruike.qms.domain.repository.QmsPqcInspectionContentRepository;
import com.ruike.qms.domain.repository.QmsPqcInspectionSchemeRepository;
import com.ruike.qms.infra.mapper.QmsPqcGroupRelMapper;
import com.ruike.qms.infra.mapper.QmsPqcInspectionContentMapper;
import com.ruike.qms.infra.mapper.QmsPqcInspectionSchemeMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 巡检检验计划应用服务默认实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-12 16:41:12
 */
@Service
public class QmsPqcInspectionSchemeServiceImpl implements QmsPqcInspectionSchemeService {

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private QmsPqcGroupRelRepository qmsPqcGroupRelRepository;

    @Autowired
    private QmsPqcInspectionContentRepository qmsPqcInspectionContentRepository;

    @Autowired
    private QmsPqcInspectionSchemeMapper qmsPqcInspectionSchemeMapper;

    @Autowired
    private QmsPqcInspectionSchemeRepository qmsPqcInspectionSchemeRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private QmsPqcGroupRelMapper qmsPqcGroupRelMapper;

    @Autowired
    private QmsPqcInspectionContentMapper qmsPqcInspectionContentMapper;
    // 获取当前用户
    CustomUserDetails curUser = DetailsHelper.getUserDetails();
    Long userId = curUser == null ? -1L : curUser.getUserId();

    /***
     * @Description: 获取当前时间
     * @author: penglin.sui
     * @date 2020/11/10 9:26
     * @return : Date
     * @version 1.0
     */
    public static Date currentTimeGet() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(currentTime.format(formatter), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String copy(Long tenantId, QmsPqcInspectionSchemeDTO dto) {
        // 校验来源巡检计划与目标巡检计划是否相同
        StringBuilder fromPlan = new StringBuilder(dto.getSiteId());
        fromPlan.append(dto.getMaterialId());
        StringBuilder toPlan = new StringBuilder(dto.getSiteIdTo());
        toPlan.append(dto.getMaterialIdTo());
        if (fromPlan.toString().equals(toPlan.toString())) {
            throw new MtException("QMS_MATERIAL_INSP_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0035", "QMS"));
        }
        // 校验目标巡检计划是否存在
        QmsPqcInspectionScheme qmsPqcInspectionSchemeQuery = new QmsPqcInspectionScheme();
        qmsPqcInspectionSchemeQuery.setSiteId(dto.getSiteIdTo());
        qmsPqcInspectionSchemeQuery.setMaterialId(dto.getMaterialIdTo());
        qmsPqcInspectionSchemeQuery.setInspectionType(dto.getInspectionType());
        int count = qmsPqcInspectionSchemeRepository.selectCount(qmsPqcInspectionSchemeQuery);
        if (count > 0) {
            throw new MtException("QMS_MATERIAL_INSP_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0036", "QMS"));
        }
        // 依照 dto 中的参数从数据库查询数据并修改插入表中
        QmsPqcInspectionScheme qmsPqcInspectionScheme = new QmsPqcInspectionScheme();
        qmsPqcInspectionScheme.setSiteId(dto.getSiteId());
        qmsPqcInspectionScheme.setMaterialId(dto.getMaterialId());
        qmsPqcInspectionScheme.setInspectionType(dto.getInspectionType());
        qmsPqcInspectionScheme = qmsPqcInspectionSchemeMapper.queryOnePqcInspectionScheme(tenantId,qmsPqcInspectionScheme);
        String inspectionSchemeIdQuery = qmsPqcInspectionScheme.getInspectionSchemeId();
        // 跟新从数据库中查出的数据
        qmsPqcInspectionScheme.setSiteId(dto.getSiteIdTo());
        qmsPqcInspectionScheme.setMaterialId(dto.getMaterialIdTo());
        // 主键ID规则
        String inspectionSchemeId = customDbRepository.getNextKey("qms_pqc_inspection_scheme_s");
        qmsPqcInspectionScheme.setInspectionSchemeId(inspectionSchemeId);
        // cid规则
        Long pqcInspectionSchemeCid = Long.parseLong(customDbRepository.getNextKey("qms_pqc_inspection_scheme_cid_s"));
        qmsPqcInspectionScheme.setCid(pqcInspectionSchemeCid);
        qmsPqcInspectionScheme.setCreatedBy(userId);
        qmsPqcInspectionScheme.setCreationDate(currentTimeGet());
        qmsPqcInspectionScheme.setLastUpdatedBy(userId);
        qmsPqcInspectionScheme.setLastUpdateDate(currentTimeGet());
        qmsPqcInspectionScheme.setObjectVersionNumber(1L);
        // 插入 qms_pqc_inspection_shceme 表中
        qmsPqcInspectionSchemeMapper.insert(qmsPqcInspectionScheme);

        // 依据上面查出的数据获取inspectionSchemeId 查询 qms_pqc_group_rel 表中的数据
        QmsPqcGroupRel pqcGroupRelquery = new QmsPqcGroupRel();
        pqcGroupRelquery.setSchemeId(inspectionSchemeIdQuery);
        pqcGroupRelquery.setTenantId(tenantId);
        List<QmsPqcGroupRel> qmsPqcGroupRelList= qmsPqcGroupRelRepository.select(pqcGroupRelquery);
        for (QmsPqcGroupRel item : qmsPqcGroupRelList) {
            item.setTenantId(tenantId);
            String pqcGroupRelId = customDbRepository.getNextKey("qms_pqc_group_rel_s");
            item.setPqcGroupRelId(pqcGroupRelId);
            item.setSchemeId(inspectionSchemeId);
            Long pqcGroupRelCid = Long.parseLong(customDbRepository.getNextKey("qms_pqc_group_rel_cid_s"));
            item.setCid(pqcGroupRelCid);
            item.setCreatedBy(userId);
            item.setCreationDate(currentTimeGet());
            item.setLastUpdatedBy(userId);
            item.setLastUpdateDate(currentTimeGet());
            item.setObjectVersionNumber(1L);
            qmsPqcGroupRelMapper.insert(item);

            // 依据上面查出的数据获取inspectionSchemeId 、TagGroupId 查询  qms_pqc_inspection_content 表中的数据
            List<QmsPqcInspectionContent> qmsPqcInspectionContentList = qmsPqcInspectionContentRepository.selectByCondition(
                    Condition.builder(QmsPqcInspectionContent.class)
                            .andWhere(Sqls.custom()
                                    .andEqualTo(QmsPqcInspectionContent.FIELD_SCHEME_ID, inspectionSchemeIdQuery)
                                    .andEqualTo(QmsPqcInspectionContent.FIELD_TAG_GROUP_ID, item.getTagGroupId()))

                            .build());
            for (QmsPqcInspectionContent it: qmsPqcInspectionContentList) {
                it.setTenantId(tenantId);
                String pqcInspectionContentId = customDbRepository.getNextKey("qms_pqc_inspection_content_s");
                it.setPqcInspectionContentId(pqcInspectionContentId);
                it.setSchemeId(inspectionSchemeId);
                Long pqcInspectionContentCid = Long.parseLong(customDbRepository.getNextKey("qms_pqc_inspection_content_cid_s"));
                it.setCid(pqcInspectionContentCid);
                it.setCreatedBy(userId);
                it.setCreationDate(currentTimeGet());
                it.setLastUpdatedBy(userId);
                it.setLastUpdateDate(currentTimeGet());
                it.setObjectVersionNumber(1L);
                qmsPqcInspectionContentMapper.insert(it);
            }
        }
        return "sucess";
    }
}
