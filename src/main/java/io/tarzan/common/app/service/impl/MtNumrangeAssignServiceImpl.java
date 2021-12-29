package io.tarzan.common.app.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.api.dto.MtNumrangeAssignDTO2;
import io.tarzan.common.app.service.MtNumrangeAssignService;
import io.tarzan.common.domain.entity.MtNumrangeAssign;
import io.tarzan.common.domain.entity.MtNumrangeAssignHis;
import io.tarzan.common.domain.entity.MtNumrangeObjectColumn;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtNumrangeAssignHisRepository;
import io.tarzan.common.domain.repository.MtNumrangeAssignRepository;
import io.tarzan.common.domain.repository.MtNumrangeObjectColumnRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO6;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO3;
import io.tarzan.common.domain.vo.MtNumrangeAssignVO4;
import io.tarzan.common.infra.mapper.MtGenTypeMapper;
import io.tarzan.common.infra.mapper.MtNumrangeAssignMapper;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 号码段分配表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:32:43
 */
@Service
public class MtNumrangeAssignServiceImpl extends BaseServiceImpl<MtNumrangeAssign> implements MtNumrangeAssignService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private MtNumrangeAssignRepository mtNumrangeAssignRepository;

    @Autowired
    private MtNumrangeAssignHisRepository mtNumrangeAssignHisRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtNumrangeAssignMapper mtNumrangeAssignMapper;

    @Autowired
    private MtGenTypeMapper mtGenTypeMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MtNumrangeObjectColumnRepository mtNumrangeObjectColumnRepository;

    @Override
    public Page<MtNumrangeAssignVO> listNumrangeAssignForUi(Long tenantId, MtNumrangeAssignVO3 condition,
                                                            PageRequest pageRequest) {
        if (StringUtils.isEmpty(condition.getObjectId())) {
            return new Page<MtNumrangeAssignVO>();
        }
        Page<MtNumrangeAssignVO> mtNumrangeAssignVOS = PageHelper.doPage(pageRequest,
                        () -> mtNumrangeAssignMapper.selectByConditionForUi(tenantId, condition));
        mtNumrangeAssignVOS.forEach(vo -> {
            if (StringUtils.isNotEmpty(vo.getSiteId())) {
                MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, vo.getSiteId());
                if (mtModSite != null) {
                    vo.setSite(mtModSite.getSiteCode());
                    vo.setSiteDesc(mtModSite.getSiteName());
                }
            }
        });
        return mtNumrangeAssignVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveNumrangeAssignForUi(Long tenantId, MtNumrangeAssignDTO2 dto) {

        // 新增事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("NUMRANGE_ASSIGN");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        // 唯一性校验
        MtNumrangeAssign oldAssign = new MtNumrangeAssign();
        oldAssign.setTenantId(tenantId);
        oldAssign.setObjectId(dto.getObjectId());
        oldAssign.setObjectTypeCode(dto.getObjectTypeCode());
        oldAssign.setSiteId(dto.getSiteId());
        oldAssign = mtNumrangeAssignRepository.selectOne(oldAssign);
        if (oldAssign != null) {
            if (StringUtils.isEmpty(dto.getNumrangeAssignId())
                            || !dto.getNumrangeAssignId().equals(oldAssign.getNumrangeAssignId())) {
                throw new MtException("MT_GENERAL_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_GENERAL_0016", "GENERAL"));
            }
        }

        // 保存号码段分配
        MtNumrangeAssign mtNumrangeAssign = new MtNumrangeAssign();
        BeanUtils.copyProperties(dto, mtNumrangeAssign);
        mtNumrangeAssign.setTenantId(tenantId);
        if (StringUtils.isNotEmpty(mtNumrangeAssign.getNumrangeAssignId())) {
            updateByPrimaryKey(mtNumrangeAssign);
        } else {
            insertSelective(mtNumrangeAssign);
        }

        // 保存号码段分配历史表
        MtNumrangeAssignHis mtNumrangeAssignHis = new MtNumrangeAssignHis();
        BeanUtils.copyProperties(mtNumrangeAssign, mtNumrangeAssignHis);
        mtNumrangeAssignHis.setTenantId(tenantId);
        mtNumrangeAssignHis.setEventId(eventId);
        mtNumrangeAssignHisRepository.insertSelective(mtNumrangeAssignHis);

        return mtNumrangeAssign.getNumrangeAssignId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRemoveNumrangeAssignForUi(Long tenantId, List<MtNumrangeAssignDTO2> list) {
        // 新增事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("NUMRANGE_ASSIGN");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        List<String> sqlList = new ArrayList<String>();

        // 获取批量删除语句
        list.forEach(dto -> {
            MtNumrangeAssign mtNumrangeAssign = new MtNumrangeAssign();
            BeanUtils.copyProperties(dto, mtNumrangeAssign);
            mtNumrangeAssign.setTenantId(tenantId);
            mtNumrangeAssign.setNumrangeAssignId(dto.getNumrangeAssignId());
            sqlList.addAll(customDbRepository.getDeleteSql(mtNumrangeAssign));

            // 生成历史表信息
            MtNumrangeAssignHis mtNumrangeAssignHis = new MtNumrangeAssignHis();
            BeanUtils.copyProperties(dto, mtNumrangeAssignHis);
            mtNumrangeAssignHis.setTenantId(tenantId);
            mtNumrangeAssignHis.setNumrangeAssignHisId(customDbRepository.getNextKey("mt_numrange_assign_his_s"));
            mtNumrangeAssignHis.setCid(Long.valueOf(customDbRepository.getNextKey("mt_numrange_assign_his_cid_s")));
            mtNumrangeAssignHis.setEventId(eventId);
            mtNumrangeAssignHis.setTenantId(tenantId);
            sqlList.addAll(customDbRepository.getInsertSql(mtNumrangeAssignHis));
        });

        jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
    }

    @Override
    public Page<MtGenTypeVO6> queryObjectTypeLovForUi(final Long tenantId, MtNumrangeAssignVO4 condition,
                                                      PageRequest pageRequest) {

        Page<MtGenTypeVO6> resultPage = new Page<MtGenTypeVO6>();
        if (StringUtils.isNotEmpty(condition.getObjectId())) {
            MtNumrangeObjectColumn mtNumrangeObjectColumn = new MtNumrangeObjectColumn();
            mtNumrangeObjectColumn.setObjectId(condition.getObjectId());

            List<MtNumrangeObjectColumn> mtNumrangeObjectColumns = mtNumrangeObjectColumnRepository
                            .propertyLimitNumrangeObjectColumnQuery(tenantId, mtNumrangeObjectColumn);
            if (CollectionUtils.isNotEmpty(mtNumrangeObjectColumns)) {
                MtNumrangeObjectColumn objectColumn = new MtNumrangeObjectColumn();

                // 找出两个字段都有的值，如果没有则是空
                List<MtNumrangeObjectColumn> collect = mtNumrangeObjectColumns.stream().filter(
                                t -> StringUtils.isNotEmpty(t.getModule()) && StringUtils.isNotEmpty(t.getTypeGroup()))
                                .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)) {
                    objectColumn = collect.get(0);
                }

                MtGenTypeVO6 mtGenTypeVO6 = new MtGenTypeVO6();
                mtGenTypeVO6.setModule(objectColumn.getModule());
                mtGenTypeVO6.setTypeGroup(objectColumn.getTypeGroup());
                mtGenTypeVO6.setTypeCode(condition.getTypeCode());
                mtGenTypeVO6.setDescription(condition.getDescription());
                resultPage = PageHelper.doPage(pageRequest,
                                () -> mtGenTypeMapper.selectByConditionForLov(tenantId, mtGenTypeVO6));
            }
        }
        return resultPage;
    }

    @Override
    public MtNumrangeAssignVO detailNumrangeAssignForUi(Long tenantId, String numrangeAssignId) {
        MtNumrangeAssignVO mtNumrangeAssignVO = mtNumrangeAssignMapper.selectByIdCustom(tenantId, numrangeAssignId);
        if (StringUtils.isNotEmpty(mtNumrangeAssignVO.getSiteId())) {
            MtModSite mtModSite = mtModSiteRepository.siteBasicPropertyGet(tenantId, mtNumrangeAssignVO.getSiteId());
            if (mtModSite != null) {
                mtNumrangeAssignVO.setSite(mtModSite.getSiteCode());
                mtNumrangeAssignVO.setSiteDesc(mtModSite.getSiteName());
            }
        }
        return mtNumrangeAssignVO;
    }

}
