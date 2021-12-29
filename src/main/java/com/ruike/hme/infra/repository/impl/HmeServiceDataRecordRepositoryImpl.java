package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.api.dto.HmeHzeroFileDTO;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.HmeEoJobSnVO4;
import com.ruike.hme.domain.vo.HmeServiceDataRecordVO;
import com.ruike.hme.domain.vo.HmeServiceDataRecordVO2;
import com.ruike.hme.domain.vo.HmeServiceDataRecordVO3;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.feign.HmeHzeroFileFeignClient;
import com.ruike.hme.infra.mapper.HmeServiceDataRecordLineMapper;
import com.ruike.hme.infra.mapper.HmeServiceDataRecordMapper;
import com.ruike.hme.infra.mapper.HmeWorkOrderManagementMapper;
import com.ruike.itf.app.service.ItfLogisticsServiceReceIfaceService;
import com.ruike.itf.domain.entity.ItfLogisticsServiceReceIface;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtTagGroupRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 售后返品信息采集确认表 资源库实现
 *
 * @author sanfeng.zhang@hand-china.com 2020-09-03 15:20:59
 */
@Component
@Slf4j
public class HmeServiceDataRecordRepositoryImpl extends BaseRepositoryImpl<HmeServiceDataRecord> implements HmeServiceDataRecordRepository, AopProxy<HmeServiceDataRecordRepository> {

    @Autowired
    private HmeServiceDataRecordMapper hmeServiceDataRecordMapper;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeServiceReceiveRepository hmeServiceReceiveRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtUserClient userClient;

    @Autowired
    private HmeServiceDataRecordLineRepository hmeServiceDataRecordLineRepository;

    @Autowired
    private HmeServiceDataRecordLineMapper hmeServiceDataRecordLineMapper;

    @Autowired
    private HmeWorkOrderManagementMapper hmeWorkOrderManagementMapper;

    @Autowired
    private MtTagGroupRepository mtTagGroupRepository;

    @Autowired
    private HmeHzeroFileFeignClient hmeHzeroFileFeignClient;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private HmeLogisticsInfoRepository hmeLogisticsInfoRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private HmeServiceReceiveHisRepository hmeServiceReceiveHisRepository;

    @Autowired
    private ItfLogisticsServiceReceIfaceService itfLogisticsServiceReceIfaceService;

    @Override
    public HmeEoJobSnVO4 workcellScan(Long tenantId, HmeEoJobSnDTO hmeEoJobSnDTO) {
        return hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ProcessLovValue
    public HmeServiceDataRecordVO scanRepairCode(Long tenantId, HmeServiceDataRecord record) {

        String defaultSiteId = hmeWorkOrderManagementMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());

        HmeServiceDataRecordVO hmeServiceDataRecordVO = hmeServiceDataRecordMapper.queryRecordInfo(tenantId, defaultSiteId, record.getSnNum());
        if (Objects.isNull(hmeServiceDataRecordVO)) {
            throw new MtException("HME_SERVICE_DATA_RECORD_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SERVICE_DATA_RECORD_001", HmeConstants.ConstantValue.HME, record.getSnNum()));
        }

        //拆箱人
        hmeServiceDataRecordVO.setReceiveByName(StringUtils.isNotBlank(hmeServiceDataRecordVO.getReceiveBy()) ? userClient.userInfoGet(tenantId, Long.valueOf(hmeServiceDataRecordVO.getReceiveBy())).getRealName() : "");

        //签收人
        hmeServiceDataRecordVO.setCreatedByName(StringUtils.isNotBlank(hmeServiceDataRecordVO.getCreatedBy()) ? userClient.userInfoGet(tenantId, Long.valueOf(hmeServiceDataRecordVO.getCreatedBy())).getRealName() : "");

        //录入状态 插入数据
        if (StringUtils.equals(hmeServiceDataRecordVO.getReceiveStatus(), "RECEIVE")) {
            HmeServiceDataRecord serviceDataRecord = new HmeServiceDataRecord();
            serviceDataRecord.setServiceReceiveId(hmeServiceDataRecordVO.getServiceReceiveId());
            serviceDataRecord.setSnNum(record.getSnNum());
            serviceDataRecord.setTenantId(tenantId);
            List<HmeServiceDataRecord> recordList = hmeServiceDataRecordMapper.select(serviceDataRecord);
            if (CollectionUtils.isEmpty(recordList)) {
                //查询采集项
                List<HmeServiceDataRecordVO2> recordVO2List = hmeServiceDataRecordMapper.queryTagInfo(tenantId, record.getOperationId());
                for (HmeServiceDataRecordVO2 hmeServiceDataRecordVO2 : recordVO2List) {
                    //保存头表
                    HmeServiceDataRecord dataRecord = new HmeServiceDataRecord();
                    BeanUtils.copyProperties(record, dataRecord);
                    dataRecord.setTenantId(tenantId);
                    dataRecord.setServiceReceiveId(hmeServiceDataRecordVO.getServiceReceiveId());
                    dataRecord.setLogisticsNumber(hmeServiceDataRecordVO.getLogisticsNumber());
                    dataRecord.setTagGroupId(hmeServiceDataRecordVO2.getTagGroupId());
                    dataRecord.setBusinessType(hmeServiceDataRecordVO2.getBusinessType());
                    self().insertSelective(dataRecord);

                    //保存行
                    if (CollectionUtils.isNotEmpty(hmeServiceDataRecordVO2.getTagList())) {
                        hmeServiceDataRecordVO2.getTagList().forEach(vo -> {
                            HmeServiceDataRecordLine line = new HmeServiceDataRecordLine();
                            line.setServiceDataRecordId(dataRecord.getServiceDataRecordId());
                            line.setTagId(vo.getTagId());
                            line.setTenantId(tenantId);
                            line.setResult(HmeConstants.ConstantValue.NO);
                            hmeServiceDataRecordLineRepository.insertSelective(line);
                        });
                    }
                }
            }
        }

        //组装数据返回
        HmeServiceDataRecord vo = new HmeServiceDataRecord();
        vo.setServiceReceiveId(hmeServiceDataRecordVO.getServiceReceiveId());
        vo.setSnNum(record.getSnNum());
        vo.setTenantId(tenantId);
        List<HmeServiceDataRecord> recordList = hmeServiceDataRecordMapper.select(vo);

        List<HmeServiceDataRecordVO2> dataRecordVO2List = new ArrayList<>();
        recordList.forEach(cord -> {
            HmeServiceDataRecordVO2 recordVO2 = new HmeServiceDataRecordVO2();
            BeanUtils.copyProperties(cord, recordVO2);
            MtTagGroup mtTagGroup = mtTagGroupRepository.selectByPrimaryKey(recordVO2.getTagGroupId());
            recordVO2.setTagGroupDescription(mtTagGroup != null ? mtTagGroup.getTagGroupDescription() : "");
            if (recordVO2.getAttachmentUuid() != null) {
                ResponseEntity<List<HmeHzeroFileDTO>> unitsInfo = hmeHzeroFileFeignClient.getUnitsInfo(tenantId, recordVO2.getAttachmentUuid().toString());
                if (CollectionUtils.isNotEmpty(unitsInfo.getBody())) {
                    recordVO2.setAttachmentName(unitsInfo.getBody().get(0).getFileName());
                }
            }

            //采集项
            List<HmeServiceDataRecordVO3> recordVO3List = hmeServiceDataRecordMapper.queryRecordLineList(tenantId, cord.getServiceDataRecordId());
            recordVO2.setTagIdList(recordVO3List.stream().filter(e -> StringUtils.equals(e.getResult(),HmeConstants.ConstantValue.YES)).map(HmeServiceDataRecordVO3::getTagId).collect(Collectors.toList()));

            recordVO2.setTagDescList(recordVO3List.stream().filter(e -> StringUtils.equals(e.getResult(),HmeConstants.ConstantValue.YES)).map(HmeServiceDataRecordVO3::getTagDescription).collect(Collectors.toList()));
            recordVO2.setTagList(recordVO3List);
            dataRecordVO2List.add(recordVO2);
        });

        hmeServiceDataRecordVO.setRecordList(dataRecordVO2List);
        return hmeServiceDataRecordVO;
    }

    @Override
    public HmeServiceDataRecordVO saveRecord(Long tenantId, HmeServiceDataRecordVO record) {
        record.getRecordList().forEach(vo -> {
            HmeServiceDataRecord dataRecord = hmeServiceDataRecordMapper.selectByPrimaryKey(vo.getServiceDataRecordId());
            dataRecord.setRemark(vo.getRemark());
            dataRecord.setResult(vo.getResult());
            dataRecord.setAttachmentUuid(vo.getAttachmentUuid());
            hmeServiceDataRecordMapper.updateByPrimaryKeySelective(dataRecord);

            //更新行 先更新为默认状态
            hmeServiceDataRecordLineMapper.updateBatchDefaultResult(tenantId, vo.getServiceDataRecordId());
            if (CollectionUtils.isNotEmpty(vo.getTagIdList())) {
                for (String tagId : vo.getTagIdList()) {
                    HmeServiceDataRecordLine line = hmeServiceDataRecordLineRepository.selectOne(new HmeServiceDataRecordLine(){{
                        setTagId(tagId);
                        setServiceDataRecordId(vo.getServiceDataRecordId());
                    }});
                    if(line != null){
                        line.setResult(HmeConstants.ConstantValue.YES);
                        hmeServiceDataRecordLineMapper.updateByPrimaryKeySelective(line);
                    }
                }
            }
        });
        return record;
    }

    @Override
    public HmeServiceDataRecordVO completeRecord(Long tenantId, HmeServiceDataRecordVO record) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        if(StringUtils.isBlank(record.getServiceReceiveId())){
            throw new MtException("HME_SERVICE_DATA_RECORD_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SERVICE_DATA_RECORD_003", HmeConstants.ConstantValue.HME));
        }
        HmeServiceReceive hmeServiceReceive = hmeServiceReceiveRepository.selectByPrimaryKey(record.getServiceReceiveId());
        if(StringUtils.equals(hmeServiceReceive.getReceiveStatus(), "CONFIRM")){
            throw new MtException("HME_SERVICE_DATA_RECORD_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SERVICE_DATA_RECORD_004", HmeConstants.ConstantValue.HME));
        }

        hmeServiceReceive.setReceiveStatus("CONFIRM");
        hmeServiceReceive.setLastUpdateDate(new Date());
        hmeServiceReceive.setLastUpdatedBy(userDetails != null ? userDetails.getUserId() : -1);
        hmeServiceReceiveRepository.updateByPrimaryKeySelective(hmeServiceReceive);

        //创建事件
        MtEventCreateVO eventCreate = new MtEventCreateVO();
        eventCreate.setEventTypeCode("SERVICE_RECEIVE_CONFIRM");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreate);

        //存入历史
        HmeServiceReceiveHis hmeServiceReceiveHis = new HmeServiceReceiveHis();
        BeanUtils.copyProperties(hmeServiceReceive, hmeServiceReceiveHis);
        hmeServiceReceiveHis.setEventId(eventId);
        hmeServiceReceiveHisRepository.insertSelective(hmeServiceReceiveHis);
        return record;
    }
}
