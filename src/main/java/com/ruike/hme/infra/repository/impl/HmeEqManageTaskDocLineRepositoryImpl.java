package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeEqTaskDocLineQueryDTO;
import com.ruike.hme.api.dto.HmeEqTaskDocQueryDTO;
import com.ruike.hme.domain.entity.HmeEqManageTaskDoc;
import com.ruike.hme.domain.entity.HmeEqManageTaskDocLineHis;
import com.ruike.hme.domain.repository.HmeEqManageTaskDocLineHisRepository;
import com.ruike.hme.domain.repository.HmeEqManageTaskDocRepository;
import com.ruike.hme.domain.vo.HmeEqTaskDocAndLineExportVO;
import com.ruike.hme.domain.vo.HmeEqTaskHisVO;
import com.ruike.hme.domain.vo.HmeMonthlyPlanVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEqManageTaskDocLineHisMapper;
import com.ruike.hme.infra.mapper.HmeEqManageTaskDocLineMapper;
import com.ruike.hme.infra.mapper.HmeEqManageTaskDocMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.spi.copy.Copier;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.hme.domain.entity.HmeEqManageTaskDocLine;
import com.ruike.hme.domain.repository.HmeEqManageTaskDocLineRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static io.tarzan.common.domain.util.MtBaseConstants.QUALITY_STATUS.NG;
import static io.tarzan.common.domain.util.MtBaseConstants.QUALITY_STATUS.OK;

/**
 * 设备管理任务单行表 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-06-16 16:06:10
 */
@Component
public class HmeEqManageTaskDocLineRepositoryImpl extends BaseRepositoryImpl<HmeEqManageTaskDocLine> implements HmeEqManageTaskDocLineRepository {

    @Autowired
    private HmeEqManageTaskDocLineMapper hmeEqManageTaskDocLineMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private HmeEqManageTaskDocMapper hmeEqManageTaskDocMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private HmeEqManageTaskDocLineHisRepository hmeEqManageTaskDocLineHisRepository;

    @Autowired
    private HmeEqManageTaskDocRepository hmeEqManageTaskDocRepository;

    @Override
    @ProcessLovValue
    public List<HmeEqTaskDocLineQueryDTO> queryTaskDocLineList(Long tenantId, String taskDocId) {
        List<HmeEqTaskDocLineQueryDTO> result = hmeEqManageTaskDocLineMapper.queryTaskDocLineList(tenantId, taskDocId);
        result.forEach(item -> {
            MtGenTypeVO2 queryType = new MtGenTypeVO2();
            queryType.setTypeGroup("TAG_VALUE_TYPE");
            queryType.setModule("GENERAL");
            queryType.setTypeCode(item.getValueType());
            List<MtGenType> types = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
            if(CollectionUtils.isNotEmpty(types)){
                item.setValueTypeDesc(types.get(0).getDescription());
                MtUserInfo userInfo = userClient.userInfoGet(tenantId, item.getResponsible());
                item.setResponsibleName(userInfo != null ? userInfo.getRealName() : "");
            }
            MtUserInfo mtUserInfo = userClient.userInfoGet(tenantId, item.getCheckBy());
            item.setCheckByName(mtUserInfo != null ? mtUserInfo.getRealName() : "");
        });
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEqTaskDocLineQueryDTO updateEqCheckDoc(Long tenantId, HmeEqTaskDocLineQueryDTO dto) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? 1L : curUser.getUserId();
        HmeEqManageTaskDocLine hmeEqManageTaskDocLine = new HmeEqManageTaskDocLine();
        hmeEqManageTaskDocLine.setTenantId(tenantId);
        hmeEqManageTaskDocLine.setTaskDocId(dto.getTaskDocId());
        hmeEqManageTaskDocLine.setManageTagId(dto.getManageTagId());
        hmeEqManageTaskDocLine.setTaskDocLineId(dto.getTaskDocLineId());
        hmeEqManageTaskDocLine.setCheckBy(userId);
        hmeEqManageTaskDocLine.setCheckDate(new Date());
        hmeEqManageTaskDocLine.setWkcId(dto.getWkcId());
        hmeEqManageTaskDocLine.setCheckValue(dto.getCheckValue());
        //计算结果
        switch (dto.getValueType()){
            case HmeConstants.ValueType.TEXT :
                if(StringUtils.isNotBlank(dto.getCheckValue())){
                    hmeEqManageTaskDocLine.setResult(HmeConstants.ConstantValue.OK);
                }else {
                    hmeEqManageTaskDocLine.setResult("");
                }
                break;
            case HmeConstants.ValueType.DECISION_VALUE:
                if(StringUtils.equals(HmeConstants.ConstantValue.OK, dto.getCheckValue())){
                    hmeEqManageTaskDocLine.setResult(HmeConstants.ConstantValue.OK);
                }else if(StringUtils.equals(HmeConstants.ConstantValue.NG, dto.getCheckValue())){
                    hmeEqManageTaskDocLine.setResult(HmeConstants.ConstantValue.NG);
                }else {
                    hmeEqManageTaskDocLine.setResult("");
                }
                break;
            case HmeConstants.ValueType.VALUE:
                hmeEqManageTaskDocLine.setResult("");
                if(StringUtils.isNotBlank(dto.getCheckValue())){
                    if (dto.getMinimumValue() != null && dto.getMaximalValue() != null) {
                        if (BigDecimal.valueOf(Double.valueOf(dto.getCheckValue())).compareTo(dto.getMinimumValue()) > 0 && BigDecimal.valueOf(Double.valueOf(dto.getCheckValue())).compareTo(dto.getMaximalValue()) <= 0) {
                            hmeEqManageTaskDocLine.setResult(HmeConstants.ConstantValue.OK);
                        }
                    }
                }
                break;
        }
        hmeEqManageTaskDocLineMapper.updateByPrimaryKeySelective(hmeEqManageTaskDocLine);

        MtEventCreateVO mtEventCreateVO = new MtEventCreateVO();
        mtEventCreateVO.setEventTypeCode("HME_TASKDOC_LINE_MODIFED");
        String eventId = mtEventRepository.eventCreate(tenantId, mtEventCreateVO);
        // 新增行操作记录历史
        HmeEqManageTaskDocLineHis docLineHis = new HmeEqManageTaskDocLineHis();
        BeanUtils.copyProperties(hmeEqManageTaskDocLine, docLineHis);
        docLineHis.setEventId(eventId);
        hmeEqManageTaskDocLineHisRepository.insertSelective(docLineHis);

        //查看行表里面的result是不是都是OK，有一项不是则为NG
        HmeEqManageTaskDoc hmeEqManageTaskDoc = hmeEqManageTaskDocMapper.selectByPrimaryKey(hmeEqManageTaskDocLine.getTaskDocId());

        List<HmeEqManageTaskDocLine> hmeEqManageTaskDocLineList = hmeEqManageTaskDocLineMapper.select(new HmeEqManageTaskDocLine() {{
            setTaskDocId(hmeEqManageTaskDocLine.getTaskDocId());
        }});
        List<HmeEqManageTaskDocLine> lineList = hmeEqManageTaskDocLineList.stream().filter(line -> StringUtils.equals(line.getResult(), HmeConstants.ConstantValue.NG)).collect(Collectors.toList());
        // 查询行的检验结果是否都有值  有值则更改头状态为完成
        List<HmeEqManageTaskDocLine> lineResultList = hmeEqManageTaskDocLineList.stream().filter(line -> StringUtils.isBlank(line.getResult())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(hmeEqManageTaskDocLineList)) {
            if (CollectionUtils.isEmpty(lineResultList)) {
                hmeEqManageTaskDoc.setDocStatus(HmeConstants.ConstantValue.COMPLETED);
            }
        }
        if (CollectionUtils.isEmpty(lineList)) {
            hmeEqManageTaskDoc.setCheckResult(OK);
        } else {
            hmeEqManageTaskDoc.setCheckResult(NG);
        }
        hmeEqManageTaskDocMapper.updateByPrimaryKeySelective(hmeEqManageTaskDoc);
        dto.setResult(hmeEqManageTaskDocLine.getResult());
        return dto;
    }

    @Override
    @ProcessLovValue
    public Page<HmeEqTaskHisVO> taskHistoryListQuery(Long tenantId, String taskDocLineId, PageRequest pageRequest) {
        Page<HmeEqTaskHisVO> pageObj = PageHelper.doPage(pageRequest, () -> hmeEqManageTaskDocLineMapper.taskHistoryListQuery(tenantId, taskDocLineId));
        for (HmeEqTaskHisVO hmeEqTaskHisVO : pageObj.getContent()) {
            if (StringUtils.isNotBlank(hmeEqTaskHisVO.getCheckBy())) {
                MtUserInfo mtUserInfo = userClient.userInfoGet(tenantId, Long.valueOf(hmeEqTaskHisVO.getCheckBy()));
                hmeEqTaskHisVO.setCheckByName(mtUserInfo != null ? mtUserInfo.getRealName() : "");
            }
            if (StringUtils.isNotBlank(hmeEqTaskHisVO.getLastUpdateBy())) {
                MtUserInfo mtUserInfo = userClient.userInfoGet(tenantId, Long.valueOf(hmeEqTaskHisVO.getLastUpdateBy()));
                hmeEqTaskHisVO.setLastUpdateByName(mtUserInfo != null ? mtUserInfo.getRealName() : "");
            }
        }
        return pageObj;
    }

    @Override
    @ProcessLovValue
    public List<HmeEqTaskDocAndLineExportVO> queryTaskDocListAndTaskDocLineList(Long tenantId, HmeEqTaskDocQueryDTO dto) {
        List<HmeEqTaskDocQueryDTO> eqTaskDocQueryDTOList = hmeEqManageTaskDocRepository.queryExportTaskDocList(tenantId, dto);
        if (CollectionUtils.isEmpty(eqTaskDocQueryDTOList)) {
            return null;
        }
        List<String> taskDocIds = eqTaskDocQueryDTOList.stream().map(HmeEqTaskDocQueryDTO::getTaskDocId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(taskDocIds)) {
            return null;
        }
        //查询设备管理任务单行表
        List<HmeEqTaskDocLineQueryDTO> eqTaskDocLineQueryDTOList = hmeEqManageTaskDocLineMapper.queryTaskDocLineListBatchGet(tenantId, taskDocIds);
        eqTaskDocLineQueryDTOList.forEach(item -> {
            MtGenTypeVO2 queryType = new MtGenTypeVO2();
            queryType.setTypeGroup("TAG_VALUE_TYPE");
            queryType.setModule("GENERAL");
            queryType.setTypeCode(item.getValueType());
            List<MtGenType> types = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
            if(CollectionUtils.isNotEmpty(types)){
                item.setValueTypeDesc(types.get(0).getDescription());
                MtUserInfo userInfo = userClient.userInfoGet(tenantId, item.getResponsible());
                item.setResponsibleName(userInfo != null ? userInfo.getRealName() : "");
            }
            MtUserInfo mtUserInfo = userClient.userInfoGet(tenantId, item.getCheckBy());
            item.setCheckByName(mtUserInfo != null ? mtUserInfo.getRealName() : "");

        });
        // 通过 taskDocId 分组
        Map<String, List<HmeEqTaskDocLineQueryDTO>> eqTaskDocLineGroupMap = eqTaskDocLineQueryDTOList.stream().collect(Collectors.groupingBy(HmeEqTaskDocLineQueryDTO::getTaskDocId));
        //对分组后的数据进行组内排序
        for (String taskDocId : eqTaskDocLineGroupMap.keySet()) {
            List<HmeEqTaskDocLineQueryDTO> sortedTaskLineList = eqTaskDocLineGroupMap.get(taskDocId).stream().sorted(Comparator.comparing(HmeEqTaskDocLineQueryDTO::getSerialNumber)).collect(Collectors.toList());
            eqTaskDocLineGroupMap.put(taskDocId, sortedTaskLineList);
        }
        List<HmeEqTaskDocAndLineExportVO> resultVO = new ArrayList<>();
        BeanCopier copier = BeanCopier.create(HmeEqTaskDocQueryDTO.class, HmeEqTaskDocAndLineExportVO.class, false);
        for (HmeEqTaskDocQueryDTO taskDoc : eqTaskDocQueryDTOList) {
            List<HmeEqTaskDocLineQueryDTO> taskDocLineList = eqTaskDocLineGroupMap.get(taskDoc.getTaskDocId());
            if (CollectionUtils.isNotEmpty(taskDocLineList)) {
                taskDocLineList.forEach(taskDocLine -> {
                    HmeEqTaskDocAndLineExportVO taskDocAndLine = new HmeEqTaskDocAndLineExportVO();
                    BeanUtils.copyProperties(taskDoc, taskDocAndLine);
                    BeanUtils.copyProperties(taskDocLine, taskDocAndLine);
                    taskDocAndLine.setItemCheckBy(taskDocLine.getCheckBy());
                    taskDocAndLine.setItemCheckDate(taskDocLine.getCheckDate());
                    taskDocAndLine.setItemRemark(taskDocLine.getRemark());
                    taskDocAndLine.setItemCheckByName(taskDocLine.getCheckByName());
                    resultVO.add(taskDocAndLine);
                });
            } else {
                HmeEqTaskDocAndLineExportVO taskDocAndLine = new HmeEqTaskDocAndLineExportVO();
                BeanUtils.copyProperties(taskDoc, taskDocAndLine);
                resultVO.add(taskDocAndLine);
            }
        }
        return resultVO;
    }

}
