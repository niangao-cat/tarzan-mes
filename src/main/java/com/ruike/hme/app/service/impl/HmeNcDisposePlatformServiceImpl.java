package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeNcDisposePlatformService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenStatus;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtGenStatusRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.actual.domain.entity.MtNcIncident;
import tarzan.actual.domain.entity.MtNcRecord;
import tarzan.actual.domain.entity.MtWkcShift;
import tarzan.actual.domain.repository.MtNcIncidentRepository;
import tarzan.actual.domain.repository.MtNcRecordRepository;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.actual.infra.mapper.MtNcRecordMapper;
import tarzan.dispatch.domain.entity.MtOperationWkcDispatchRel;
import tarzan.dispatch.domain.repository.MtOperationWkcDispatchRelRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO5;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.method.domain.entity.*;
import tarzan.method.domain.repository.*;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.*;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtEoRouter;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtEoRouterRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;

/**
 * ??????????????????????????????????????????
 *
 * @author: chaonan.hu@hand-china.com 2020-06-30 09:49:52
 **/
@Service
public class HmeNcDisposePlatformServiceImpl implements HmeNcDisposePlatformService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeNcDisposePlatformRepository hmeNcDisposePlatformRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtUserRepository mtUserRepository;
    @Autowired
    private HmeNcDisposePlatformMapper hmeNcDisposePlatformMapper;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtOperationWkcDispatchRelRepository mtOperationWkcDispatchRelRepository;
    @Autowired
    private MtEoRouterRepository mtEoRouterRepository;
    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;
    @Autowired
    private MtRouterOperationRepository mtRouterOperationRepository;
    @Autowired
    private MtOperationRepository mtOperationRepository;
    @Autowired
    private HmeNcComponentSnTempRepository hmeNcComponentSnTempRepository;
    @Autowired
    private HmeNcComponentTempRepository hmeNcComponentTempRepository;
    @Autowired
    private MtNcRecordRepository mtNcRecordRepository;
    @Autowired
    private MtModAreaRepository mtModAreaRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtNcCodeRepository mtNcCodeRepository;
    @Autowired
    private MtGenStatusRepository mtGenStatusRepository;
    @Autowired
    private MtNcGroupRepository mtNcGroupRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private MtNcRecordMapper mtNcRecordMapper;
    @Autowired
    private MtNcIncidentRepository mtNcIncidentRepository;
    @Autowired
    private HmeNcRecordAttrRepository hmeNcRecordAttrRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeNcCheckMapper hmeNcCheckMapper;
    @Autowired
    private HmeNcComponentSnTempMapper hmeNcComponentSnTempMapper;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private HmeEoJobLotMaterialRepository hmeEoJobLotMaterialRepository;
    @Autowired
    private HmeEoJobTimeMaterialRepository hmeEoJobTimeMaterialRepository;
    @Autowired
    private HmeEoJobMaterialRepository hmeEoJobMaterialRepository;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private HmeSignInOutRecordMapper hmeSignInOutRecordMapper;
    @Autowired
    private HmeEoJobSnReWorkMapper hmeEoJobSnReWorkMapper;

    @Override
    public HmeNcDisposePlatformDTO7 ncRecordQuery(Long tenantId, HmeNcDisposePlatformDTO dto, PageRequest pageRequest) {
        //????????????
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "??????", ""));
        }
        if (StringUtils.isEmpty(dto.getMaterialLotCode())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "?????????", ""));
        }
        //????????????????????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        if (mtMaterialLot == null || HmeConstants.ConstantValue.NO.equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("HME_NC_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0009", "HME"));
        }
        //????????????????????????
        //2020-08-25 edit by chaonan.hu for lu.bai
//        Date siteOutDate = hmeNcDisposePlatformMapper.siteOutDateQuery(tenantId, mtMaterialLot.getMaterialLotId());
//        if(siteOutDate == null){
//            throw new MtException("HME_NC_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_NC_0010", "HME"));
//        }
        //2020-11-06 15:23 add by chaonan.hu for lu.bai ???????????????????????????????????????Y  ??????Y??????
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("MF_FLAG");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isEmpty(mtExtendAttrVOS) || !YES.equals(mtExtendAttrVOS.get(0).getAttrValue())) {
            throw new MtException("HME_NC_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0030", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //2020-12-21 add by chaonan.hu for can.wang ????????????????????????????????????Y ???Y??????
//        mtMaterialLotAttrVO2.setAttrName("REWORK_FLAG");
//        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
//        if(CollectionUtils.isNotEmpty(mtExtendAttrVOS) && YES.equals(mtExtendAttrVOS.get(0).getAttrValue())){
//            throw new MtException("HME_NC_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_NC_0049", "HME"));
//        }
        //????????????????????????????????????????????????
        // 2021-03-08 add by sanfeng.zhang for wang.can ????????????????????????
//        List<MtNcRecord> mtNcRecords = mtNcRecordRepository.select(new MtNcRecord() {{
//            setTenantId(tenantId);
//            setMaterialLotId(mtMaterialLot.getMaterialLotId());
//            setNcStatus("OPEN");
//        }});
//        if (CollectionUtils.isNotEmpty(mtNcRecords)) {
//            throw new MtException("HME_NC_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_NC_0011", "HME"));
//        }
        //????????????????????????????????????
        int i = hmeEoJobSnRepository.selectCount(new HmeEoJobSn() {{
            setTenantId(tenantId);
            setMaterialLotId(mtMaterialLot.getMaterialLotId());
            setWorkcellId(dto.getWorkcellId());
        }});
        if (i == 0) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(dto.getWorkcellId());
            throw new MtException("HME_NC_0032", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0032", "HME", mtMaterialLot.getMaterialLotCode(), mtModWorkcell.getWorkcellCode()));
        }
        HmeNcDisposePlatformDTO7 hmeNcDisposePlatformDTO7 = hmeNcDisposePlatformRepository.ncRecordQuery(tenantId, dto, pageRequest);
        return hmeNcDisposePlatformDTO7;
    }

    @Override
    public Page<HmeNcDisposePlatformDTO3> processLov(Long tenantId, HmeNcDisposePlatformDTO4 dto, PageRequest pageRequest) {
        //???????????????
        if (StringUtils.isEmpty(dto.getProdLineId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "??????", ""));
        }
        return hmeNcDisposePlatformRepository.processLov(tenantId, dto, pageRequest);
    }

    @Override
    public Page<HmeNcDisposePlatformDTO6> workcellLov(Long tenantId, HmeNcDisposePlatformDTO5 dto, PageRequest pageRequest) {
        //???????????????
        if (StringUtils.isEmpty(dto.getProcessId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "??????", ""));
        }
        return hmeNcDisposePlatformRepository.workcellLov(tenantId, dto, pageRequest);
    }

    @Override
    public HmeNcDisposePlatformVO getMaterialCode(Long tenantId, String materialLotCode) {
        HmeNcDisposePlatformVO result = new HmeNcDisposePlatformVO();
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(materialLotCode);
        }});
        if (mtMaterialLot != null && StringUtils.isNotBlank(mtMaterialLot.getMaterialId())) {
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
            if (mtMaterial != null && StringUtils.isNotBlank(mtMaterial.getMaterialCode())) {
                result.setMaterialCode(mtMaterial.getMaterialCode());
                result.setMaterialName(mtMaterial.getMaterialName());
            }
        }
        return result;
    }

    @Override
    public MtUserInfo getCurrentUser(Long tenantId) {
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        return mtUserRepository.userPropertyGet(tenantId, userId);
    }

    @Override
    public Page<HmeNcDisposePlatformDTO8> getOtherProcessNcType(String workcellId, String description, PageRequest pageRequest) {
        List<HmeNcDisposePlatformDTO8> processNcCodeTypes = hmeNcDisposePlatformMapper.getProcessNcCodeTypes2(workcellId, WmsConstant.CONSTANT_N);
        List<HmeNcDisposePlatformDTO8> processNcCodeTypeList = new ArrayList<>();
        for (int i = 7; i < processNcCodeTypes.size(); i++) {
            if (StringUtils.isNotEmpty(description)) {
                if (processNcCodeTypes.get(i).getDescription().contains(description)) {
                    processNcCodeTypeList.add(processNcCodeTypes.get(i));
                }
            } else {
                processNcCodeTypeList.add(processNcCodeTypes.get(i));
            }
        }
        //?????????
        int totalPages = 0;
        if (CollectionUtils.isNotEmpty(processNcCodeTypeList)) {
            totalPages = processNcCodeTypeList.size() / pageRequest.getSize();
            if (processNcCodeTypeList.size() % pageRequest.getSize() > 0) {
                totalPages++;
            }
        }
        //????????????????????????
        List<HmeNcDisposePlatformDTO8> processNcCodeTypeList2 = new ArrayList<>();
        int start = 0;
        int end = 0;
        start = pageRequest.getPage() * pageRequest.getSize();
        end = start + pageRequest.getSize();
        end = processNcCodeTypeList.size() < end ? processNcCodeTypeList.size() : end;
        for (int i = start; i < end; i++) {
            processNcCodeTypeList2.add(processNcCodeTypeList.get(i));
        }
        Page<HmeNcDisposePlatformDTO8> resultList = new Page<>();
        resultList.setTotalPages(totalPages);
        resultList.setTotalElements(processNcCodeTypeList.size());
        resultList.setNumberOfElements(processNcCodeTypeList.size());
        resultList.setSize(pageRequest.getSize());
        resultList.setNumber(pageRequest.getPage());
        resultList.setContent(processNcCodeTypeList2);
        return resultList;
    }

    @Override
    public Page<HmeNcDisposePlatformDTO8> getOtherMaterialNcType(String workcellId, String description, PageRequest pageRequest) {
        List<HmeNcDisposePlatformDTO8> materialNcCodeTypes = hmeNcDisposePlatformMapper.getProcessNcCodeTypes2(workcellId, YES);
        List<HmeNcDisposePlatformDTO8> materialNcCodeTypeList = new ArrayList<>();
        for (int i = 7; i < materialNcCodeTypes.size(); i++) {
            if (StringUtils.isNotEmpty(description)) {
                if (materialNcCodeTypes.get(i).getDescription().contains(description)) {
                    materialNcCodeTypeList.add(materialNcCodeTypes.get(i));
                }
            } else {
                materialNcCodeTypeList.add(materialNcCodeTypes.get(i));
            }
        }
        //?????????
        int totalPages = 0;
        if (CollectionUtils.isNotEmpty(materialNcCodeTypeList)) {
            totalPages = materialNcCodeTypeList.size() / pageRequest.getSize();
            if (materialNcCodeTypeList.size() % pageRequest.getSize() > 0) {
                totalPages++;
            }
        }
        //????????????????????????
        List<HmeNcDisposePlatformDTO8> materialNcCodeTypeList2 = new ArrayList<>();
        int start = 0;
        int end = 0;
        start = pageRequest.getPage() * pageRequest.getSize();
        end = start + pageRequest.getSize();
        end = materialNcCodeTypeList.size() < end ? materialNcCodeTypeList.size() : end;
        for (int i = start; i < end; i++) {
            materialNcCodeTypeList2.add(materialNcCodeTypeList.get(i));
        }
        Page<HmeNcDisposePlatformDTO8> resultList = new Page<>();
        resultList.setTotalPages(totalPages);
        resultList.setTotalElements(materialNcCodeTypeList.size());
        resultList.setNumberOfElements(materialNcCodeTypeList.size());
        resultList.setSize(pageRequest.getSize());
        resultList.setNumber(pageRequest.getPage());
        resultList.setContent(materialNcCodeTypeList2);
        return resultList;
    }

    @Override
    public Page<HmeNcDisposePlatformDTO12> getOtherWorkcell(Long tenantId, HmeNcDisposePlatformDTO10 dto, PageRequest pageRequest) {
        //???????????????
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "??????", ""));
        }
        if (StringUtils.isEmpty(dto.getMaterialLotCode())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "?????????", ""));
        }
        Page<HmeNcDisposePlatformDTO12> resultPage = PageHelper.doPage(pageRequest, () -> hmeNcDisposePlatformMapper.getOtherWorkcell(dto));
        return resultPage;
    }

    @Override
    public String processNcTypeCreate(Long tenantId, HmeNcDisposePlatformDTO11 dto) {
        //???????????????
        if (StringUtils.isEmpty(dto.getFlag())) {
            throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_GENERAL_0001", "GENERAL", "????????????/????????????????????????", ""));
        }
        if (!HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
            if (StringUtils.isEmpty(dto.getNcGroupId())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0001", "GENERAL", "???????????????", ""));
            }
            if (StringUtils.isEmpty(dto.getWorkcellId())) {
                throw new MtException("MT_GENERAL_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_GENERAL_0001", "GENERAL", "????????????", ""));
            }
        }
        // 2021-03-03 add by sanfeng.zhang for wang.can ???????????????????????????
        if (CollectionUtils.isEmpty(dto.getNcCodeIdList())) {
            //?????????????????????
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "????????????"));
        }

        //2021-11-09 ???????????????????????????????????? ???EO??????????????? ??????EO????????????EO By ?????? for ?????????
        //????????????????????????,????????????????????????????????????????????????????????????
        String materialLotCode = hmeEoJobSnReWorkMapper.queryReworkMaterialLotCodeByOldCode(tenantId, dto.getMaterialLotCode());
        if (StringUtils.isEmpty(materialLotCode)){
            materialLotCode = dto.getMaterialLotCode();
        }
        //V20211013 modify by penglin.sui for hui.ma ????????????????????????SN?????????EO?????????????????????
        List<MtEo> eoList = mtEoRepository.selectByCondition(Condition.builder(MtEo.class)
                .select(MtEo.FIELD_STATUS)
                .andWhere(Sqls.custom().andEqualTo(MtEo.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(MtEo.FIELD_IDENTIFICATION, materialLotCode))
                .build());
        if(CollectionUtils.isEmpty(eoList)){
            //????????????SN????????????EO
            throw new MtException("HME_EO_JOB_SN_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_005", "HME"));
        }
        if(!HmeConstants.EoStatus.WORKING.equals(eoList.get(0).getStatus())){
            //EO???????????????????????????
            throw new MtException("HME_EO_JOB_SN_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_003", "HME"));
        }

        //????????????????????????????????????????????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        List<MtNcRecord> mtNcRecords = mtNcRecordRepository.select(new MtNcRecord() {{
            setTenantId(tenantId);
            setMaterialLotId(mtMaterialLot.getMaterialLotId());
            setNcStatus("OPEN");
        }});
        if (CollectionUtils.isNotEmpty(mtNcRecords)) {
            throw new MtException("HME_NC_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0011", "HME"));
        }
        //2020-11-06 15:23 add by chaonan.hu for lu.bai ???????????????????????????????????????Y  ??????Y??????
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("MF_FLAG");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isEmpty(mtExtendAttrVOS) || !YES.equals(mtExtendAttrVOS.get(0).getAttrValue())) {
            throw new MtException("HME_NC_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0030", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //2020-12-23 add by chaonan.hu for can.wang ????????????????????????????????????Y ???Y??????
        //2021-01-06 edit by chaonan.hu for can.wang ???????????????????????????????????????
        mtMaterialLotAttrVO2.setAttrName("REWORK_FLAG");
        mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        String reworkFlag = CollectionUtils.isNotEmpty(mtExtendAttrVOS) ? mtExtendAttrVOS.get(0).getAttrValue() : "";
        if (HmeConstants.ProcessStatus.THREE.equals(dto.getFlag())) {
            // ???????????? ????????????????????? ??????????????????????????????
            if (!YES.equals(dto.getReworkRecordFlag())) {
                if (YES.equals(reworkFlag)) {
                    throw new MtException("HME_NC_0049", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0049", "HME"));
                }
            }
            // 2021-02-07 add by sanfeng.zhang for wang.can ??????????????? ?????????????????????????????????Y ?????????
            for (String ncCodeId : dto.getNcCodeIdList()) {
                List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                    setKeyId(ncCodeId);
                    setTableName("mt_nc_code_attr");
                    setAttrName("MDBO_FLAG");
                }});
                String mdboFlag = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
                if (YES.equals(mdboFlag)) {
                    MtNcCode mtNcCode = mtNcCodeRepository.selectByPrimaryKey(ncCodeId);
                    throw new MtException("HME_NC_0076", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0076", "HME", mtNcCode != null ? mtNcCode.getNcCode() : ""));
                }
            }
        }
        // 20211112 add by sanfeng.zhang for peng.zhao ????????????????????? ??????????????? ????????????????????????????????????
        if (!YES.equals(reworkFlag)) {
            this.verifyProcess(tenantId, dto, mtMaterialLot);
        }
        return hmeNcDisposePlatformRepository.processNcTypeCreate(tenantId, dto);
    }

    private void verifyProcess(Long tenantId, HmeNcDisposePlatformDTO11 dto, MtMaterialLot mtMaterialLot) {
        // ????????????
        MtModOrganizationRel mtModOrganizationRel = mtModOrganizationRelRepository.selectOne(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setParentOrganizationType("WORKCELL");
            setOrganizationType("WORKCELL");
            setOrganizationId(dto.getCurrentwWorkcellId());
        }});
        String initiateProcessId = mtModOrganizationRel != null ? mtModOrganizationRel.getParentOrganizationId() : "";
        // ???????????? ??????????????????????????? ????????????????????????
        String currentWorkcellId = hmeNcDisposePlatformMapper.queryCurrentWorkcellId(tenantId, mtMaterialLot.getMaterialLotId());
        MtModOrganizationRel currentProcess = mtModOrganizationRelRepository.selectOne(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setParentOrganizationType("WORKCELL");
            setOrganizationType("WORKCELL");
            setOrganizationId(currentWorkcellId);
        }});
        String currentProcessId = currentProcess != null ? currentProcess.getParentOrganizationId() : "";
        if (!StringUtils.equals(initiateProcessId, currentProcessId)) {
            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(currentProcessId);
            throw new MtException("HME_NC_0087", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0087", "HME", mtModWorkcell != null ? mtModWorkcell.getWorkcellCode() : ""));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeNcDisposePlatformVO2 getMaterialLotId(Long tenantId, HmeNcDisposePlatformDTO14 dto) {
        //??????????????????????????????????????????
        //??????????????????????????? mt_material_lot???????????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getScanMaterialLotCode());
        }});
        if (mtMaterialLot != null) {
            //2020-09-11 19:21 add by chaonan.hu for lu.bai ??????????????????????????????????????????
            if (NO.equals(mtMaterialLot.getEnableFlag())) {
                throw new MtException("MT_INVENTORY_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0025", "INVENTORY"));
            }
            if (!OK.equals(mtMaterialLot.getQualityStatus())) {
                throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
            }
            //???????????????????????????????????????.???????????????????????????????????????????????????????????????
            if (mtMaterialLot.getMaterialId().equals(dto.getMaterialId())) {
                //???????????????????????????
                HmeNcDisposePlatformVO2 result = new HmeNcDisposePlatformVO2();
                result.setScrapQty(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
                return result;
            } else {
                //????????????????????????
                throw new MtException("HME_NC_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0001", "HME"));
            }
        } else {
            //2020-09-10 09:58 edit by chaonan.hu for lu.bai ?????????????????????????????????
            //???????????????????????????
            throw new MtException("HME_NC_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0002", "HME"));
            //????????????????????????????????????????????????????????????
//            List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
//                setTenantId(tenantId);
//                setOrganizationId(dto.getWorkcrellId());
//            }});
//            String siteId = mtModOrganizationRels.get(0).getTopSiteId();
//            //???????????????????????????????????????
//            String attrValue = hmeNcDisposePlatformMapper.getAttrValue(tenantId, dto.getMaterialId(), siteId);
//            if (HmeConstants.ConstantValue.SN.equals(attrValue)) {
//                //?????????SN????????????????????????????????????????????????,???????????????????????????????????????????????????????????????
//                HmeNcComponentSnTemp hmeNcComponentSnTemp = new HmeNcComponentSnTemp();
//                hmeNcComponentSnTemp.setNcComponentTempId(dto.getNcComponentTempId());
//                hmeNcComponentSnTemp.setMaterialLotCode(dto.getScanMaterialLotCode());
//                hmeNcComponentSnTemp.setMaterialLotId(dto.getScanMaterialLotCode());
//                hmeNcComponentSnTempRepository.insertSelective(hmeNcComponentSnTemp);
//                return dto.getScanMaterialLotCode();
//            } else {
//                //????????????SN????????????
//                throw new MtException("HME_NC_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "HME_NC_0002", "HME"));
//            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeNcDisposePlatformDTO27 materialLotScanLineSubmit(Long tenantId, HmeNcDisposePlatformDTO27 dto) {
        //?????????????????????????????????????????????
        for (HmeNcDisposePlatformDTO28 ncDisposePlatformDTO28 : dto.getMaterialLotList()) {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(ncDisposePlatformDTO28.getMaterialLotCode());
            }});
            if (mtMaterialLot.getPrimaryUomQty() < ncDisposePlatformDTO28.getScrapQty().doubleValue()) {
                throw new MtException("HME_NC_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0014", "HME", ncDisposePlatformDTO28.getMaterialLotCode()));
            }
        }

        for (HmeNcDisposePlatformDTO28 ncDisposePlatformDTO28 : dto.getMaterialLotList()) {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(ncDisposePlatformDTO28.getMaterialLotCode());
            }});
            //???????????????????????????
            HmeNcComponentSnTemp hmeNcComponentSnTempDb = hmeNcComponentSnTempRepository.selectOne(new HmeNcComponentSnTemp() {{
                setTenantId(tenantId);
                setNcComponentTempId(dto.getNcComponentTempId());
                setMaterialLotId(mtMaterialLot.getMaterialLotId());
            }});
            if (hmeNcComponentSnTempDb == null) {
                //??????
                HmeNcComponentSnTemp hmeNcComponentSnTemp = new HmeNcComponentSnTemp();
                hmeNcComponentSnTemp.setTenantId(tenantId);
                hmeNcComponentSnTemp.setNcComponentTempId(dto.getNcComponentTempId());
                hmeNcComponentSnTemp.setMaterialLotCode(ncDisposePlatformDTO28.getMaterialLotCode());
                hmeNcComponentSnTemp.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                //2020-09-10 09:58 add by chaonan.hu for lu.bai ?????????????????????????????????????????????????????????
                hmeNcComponentSnTemp.setAttribute1(ncDisposePlatformDTO28.getScrapQty().toString());
                hmeNcComponentSnTempRepository.insertSelective(hmeNcComponentSnTemp);
            } else {
                //??????
                hmeNcComponentSnTempDb.setAttribute1(ncDisposePlatformDTO28.getScrapQty().toString());
                hmeNcComponentSnTempMapper.updateByPrimaryKeySelective(hmeNcComponentSnTempDb);
            }
        }
        return dto;
    }

    @Override
    public HmeNcDisposePlatformVO4 materialLotScan(Long tenantId, HmeNcDisposePlatformDTO15 dto) {
        //?????????????????????????????????????????????
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getScanCode());
        }});
        if (mtMaterialLot != null) {
            //2020-09-11 19:21 add by chaonan.hu for lu.bai ??????????????????????????????????????????
            if (NO.equals(mtMaterialLot.getEnableFlag())) {
                throw new MtException("MT_INVENTORY_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_INVENTORY_0025", "INVENTORY"));
            }
            if (!OK.equals(mtMaterialLot.getQualityStatus())) {
                throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
            }
            //2020-10-05 add by chaonan.hu for lu.bai ?????????????????????????????????MF_FLAG??????Y????????????????????????
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtMaterialLotAttrVO2.setAttrName("MF_FLAG");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && YES.equals(mtExtendAttrVOS.get(0).getAttrValue())) {
                throw new MtException("HME_NC_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0029", "HME"));
            }
            //2020-09-28 add by chaonan.hu for lu.bai ???????????????????????????????????????????????????????????????????????????
            Boolean locatorFlag = false;
            //??????API{subOrganizationRelQuery} ????????????????????????
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(mtMaterialLot.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
            mtModOrganizationVO2.setOrganizationId(dto.getWorkcellId());
            mtModOrganizationVO2.setOrganizationType(MtBaseConstants.ORGANIZATION_TYPE.WORKCELL);
            mtModOrganizationVO2.setQueryType("TOP");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            if (CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)) {
                String lineWorkcellId = mtModOrganizationItemVOS.get(0).getOrganizationId();
                //???????????????????????????????????????
                List<String> areaLocatorIdList = hmeNcDisposePlatformMapper.areaLocatorQuery(tenantId, lineWorkcellId);
                //?????????????????????????????????????????????
                if (CollectionUtils.isNotEmpty(areaLocatorIdList) && StringUtils.isNotEmpty(mtMaterialLot.getLocatorId())) {
                    locatorFlag = areaLocatorIdList.contains(mtMaterialLot.getLocatorId());
                }
            }
            if (!locatorFlag) {
                throw new MtException("HME_NC_0024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0024", "HME"));
            }
//            //2020-09-28 add by chaonan.hu for lu.bai ???????????????????????????
//            List<HmeEoJobLotMaterial> hmeEoJobLotMaterials = hmeEoJobLotMaterialRepository.select(new HmeEoJobLotMaterial() {{
//                setTenantId(tenantId);
//                setMaterialLotId(mtMaterialLot.getMaterialLotId());
//            }});
//            List<HmeEoJobTimeMaterial> hmeEoJobTimeMaterials = hmeEoJobTimeMaterialRepository.select(new HmeEoJobTimeMaterial() {{
//                setTenantId(tenantId);
//                setMaterialLotId(mtMaterialLot.getMaterialLotId());
//            }});
//            List<HmeEoJobMaterial> hmeEoJobMaterials = hmeEoJobMaterialRepository.select(new HmeEoJobMaterial() {{
//                setTenantId(tenantId);
//                setMaterialLotId(mtMaterialLot.getMaterialLotId());
//            }});
//            if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterials) || CollectionUtils.isNotEmpty(hmeEoJobTimeMaterials)
//                    || CollectionUtils.isNotEmpty(hmeEoJobMaterials)) {
//                throw new MtException("HME_NC_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "HME_NC_0025", "HME"));
//            }
            //???????????????????????????????????????????????????????????????????????????material_id?????? mt_material?????????material_code material_name
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtMaterialLot.getMaterialId());
            //??????????????????
            HmeNcDisposePlatformVO4 hmeNcDisposePlatformVO4 = new HmeNcDisposePlatformVO4();
            hmeNcDisposePlatformVO4.setMaterialId(mtMaterial.getMaterialId());
            hmeNcDisposePlatformVO4.setMaterialCode(mtMaterial.getMaterialCode());
            hmeNcDisposePlatformVO4.setMaterialName(mtMaterial.getMaterialName());
            hmeNcDisposePlatformVO4.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            hmeNcDisposePlatformVO4.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            hmeNcDisposePlatformVO4.setLot(mtMaterialLot.getLot());
            hmeNcDisposePlatformVO4.setReleaseQty(BigDecimal.ZERO);
            hmeNcDisposePlatformVO4.setScrapQty(BigDecimal.ZERO);
            hmeNcDisposePlatformVO4.setPrimaryUomQty(new BigDecimal(mtMaterialLot.getPrimaryUomQty()));
            //???????????????
            MtMaterialLot snMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(dto.getSnNumber());
            }});
            BigDecimal waitAuditQty = hmeNcDisposePlatformMapper.waitAuditQtyQuery(tenantId, hmeNcDisposePlatformVO4.getMaterialLotId(), snMaterialLot.getEoId());
            hmeNcDisposePlatformVO4.setWaitAuditQty(waitAuditQty);
            //??????
            hmeNcDisposePlatformVO4.setColor("White");
            // ??????????????????????????????????????????
            // ?????????????????????EO
            MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
                setTenantId(tenantId);
                setIdentification(dto.getSnNumber());
            }});
            if (mtEo == null) {
                throw new MtException("HME_EO_JOB_SN_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_005", "HME"));
            }
            List<String> bomComponentList = hmeNcDisposePlatformMapper.queryBomComponentByEoAndCode(tenantId, mtEo.getEoId(), mtMaterialLot.getMaterialId());
            // ???????????? 1-???eo??????????????? 2-??????
            if (CollectionUtils.isNotEmpty(bomComponentList)) {
                hmeNcDisposePlatformVO4.setAssemblyFlag(ONE);
            } else {
                hmeNcDisposePlatformVO4.setAssemblyFlag(ZERO);
                hmeNcDisposePlatformVO4.setWarnMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_NC_0077", "HME"));
            }
            return hmeNcDisposePlatformVO4;
        } else {
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
    }

    @Override
    public HmeNcDisposePlatformDTO13 materialLotScan2(Long tenantId, HmeNcDisposePlatformDTO16 dto) {
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setOrganizationId(dto.getWorkcellId());
        }});
        String siteId = mtModOrganizationRels.get(0).getTopSiteId();

        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getMaterialId());
        //??????????????????????????????
        HmeNcComponentTemp hmeNcComponentTemp = new HmeNcComponentTemp();
        hmeNcComponentTemp.setTenantId(tenantId);
        hmeNcComponentTemp.setSiteId(siteId);
        hmeNcComponentTemp.setSn(dto.getSnNumber());
        hmeNcComponentTemp.setWorkcellId(dto.getWorkcellId());
        hmeNcComponentTemp.setUserId(userId.toString());
        hmeNcComponentTemp.setMaterialId(mtMaterial.getMaterialId());
        List<HmeNcComponentTemp> hmeNcComponentTemps = hmeNcComponentTempRepository.select(hmeNcComponentTemp);
        if (CollectionUtils.isNotEmpty(hmeNcComponentTemps)) {
            throw new MtException("HME_NC_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0004", "HME", mtMaterial.getMaterialCode()));
        }
        //??????????????????
        HmeNcDisposePlatformDTO13 hmeNcDisposePlatformDTO13 = new HmeNcDisposePlatformDTO13();
        hmeNcDisposePlatformDTO13.setMaterialId(mtMaterial.getMaterialId());
        hmeNcDisposePlatformDTO13.setMaterialCode(mtMaterial.getMaterialCode());
        hmeNcDisposePlatformDTO13.setMaterialName(mtMaterial.getMaterialName());
        hmeNcDisposePlatformDTO13.setMaterialLotId(dto.getScanCode());
        hmeNcDisposePlatformDTO13.setMaterialLotCode(dto.getScanCode());
        return hmeNcDisposePlatformDTO13;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String materialLotScanSubmit(Long tenantId, HmeNcDisposePlatformDTO20 dto) {
        List<HmeNcDisposePlatformDTO21> hmeNcDisposePlatformDTO21List = dto.getHmeNcDisposePlatformDTO21List();
        //?????????????????????????????????????????????
        for (HmeNcDisposePlatformDTO21 hmeNcDisposePlatformDTO21 : hmeNcDisposePlatformDTO21List) {
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
                setTenantId(tenantId);
                setMaterialLotCode(hmeNcDisposePlatformDTO21.getMaterialLotCode());
            }});
            if (mtMaterialLot.getPrimaryUomQty() < hmeNcDisposePlatformDTO21.getScrapQty().doubleValue()) {
                throw new MtException("HME_NC_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0014", "HME", hmeNcDisposePlatformDTO21.getMaterialLotCode()));
            }
        }
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setOrganizationId(dto.getWorkcellId());
        }});
        String siteId = mtModOrganizationRels.get(0).getTopSiteId();
        //??????????????????????????????
        for (HmeNcDisposePlatformDTO21 hmeNcDisposePlatformDTO21 : hmeNcDisposePlatformDTO21List) {
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeNcDisposePlatformDTO21.getMaterialId());
            HmeNcComponentTemp hmeNcComponentTemp = new HmeNcComponentTemp();
            hmeNcComponentTemp.setTenantId(tenantId);
            hmeNcComponentTemp.setSiteId(siteId);
            hmeNcComponentTemp.setSn(hmeNcDisposePlatformDTO21.getSnNumber());
            hmeNcComponentTemp.setWorkcellId(dto.getWorkcellId());
            hmeNcComponentTemp.setUserId(userId.toString());
            hmeNcComponentTemp.setMaterialId(hmeNcDisposePlatformDTO21.getMaterialId());
            List<HmeNcComponentTemp> hmeNcComponentTemps = hmeNcComponentTempRepository.select(hmeNcComponentTemp);
            if (CollectionUtils.isNotEmpty(hmeNcComponentTemps)) {
                throw new MtException("HME_NC_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0004", "HME", mtMaterial.getMaterialCode()));
            }
            hmeNcComponentTempRepository.insertSelective(hmeNcComponentTemp);
            HmeNcComponentSnTemp hmeNcComponentSnTemp = new HmeNcComponentSnTemp();
            hmeNcComponentSnTemp.setTenantId(tenantId);
            hmeNcComponentSnTemp.setNcComponentTempId(hmeNcComponentTemp.getNcComponentTempId());
            hmeNcComponentSnTemp.setMaterialLotId(hmeNcDisposePlatformDTO21.getMaterialLotId());
            hmeNcComponentSnTemp.setMaterialLotCode(hmeNcDisposePlatformDTO21.getMaterialLotCode());

            //2020-09-10 09:58 add by chaonan.hu for lu.bai ?????????????????????????????????????????????????????????
            hmeNcComponentSnTemp.setAttribute1(hmeNcDisposePlatformDTO21.getScrapQty().toString());

            hmeNcComponentSnTempRepository.insertSelective(hmeNcComponentSnTemp);
        }
        return "true";
    }

    @Override
    public HmeNcDisposePlatformDTO18 materialNcTypeCreate(Long tenantId, HmeNcDisposePlatformDTO18 dto) {
        //???????????????
        if (StringUtils.isEmpty(dto.getSnNumber())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "?????????"));
        }
        if (StringUtils.isEmpty(dto.getNcCodeId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "????????????"));
        }
        if (StringUtils.isEmpty(dto.getWorkcellId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????"));
        }
        if (StringUtils.isEmpty(dto.getProcessId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????"));
        }
        if (StringUtils.isEmpty(dto.getFlag())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "????????????/????????????????????????"));
        }
        if (CollectionUtils.isEmpty(dto.getMaterilalList())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????"));
        }
        // 2021-03-03 add by sanfeng.zhang for wang.can ???????????????????????????
        if (CollectionUtils.isEmpty(dto.getChildNcCodeIdList())) {
            //?????????????????????
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "????????????"));
        }
        MtMaterialLot snMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getSnNumber());
        }});
        //2021-04-29 add by sanfeng.zahng for peng.zhao ??????????????????????????????????????????Y  ??????Y??????
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(snMaterialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("MF_FLAG");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isEmpty(mtExtendAttrVOS) || !YES.equals(mtExtendAttrVOS.get(0).getAttrValue())) {
            throw new MtException("HME_NC_0030", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0030", "HME", snMaterialLot.getMaterialLotCode()));
        }
        if (HmeConstants.ProcessStatus.ONE.equals(dto.getFlag())) {
            // 2021-02-07 add by sanfeng.zhang for wang.can ??????????????? ????????????????????????????????????Y ?????????
            // 2021-03-03 add by sanfeng.zhang for wang.can ??????????????? ?????????????????????????????????????????????????????????Y ?????????
            for (String ncCodeId : dto.getChildNcCodeIdList()) {
                List<MtExtendAttrVO> attrVOList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, new MtExtendVO() {{
                    setKeyId(ncCodeId);
                    setTableName("mt_nc_code_attr");
                    setAttrName("MDBO_FLAG");
                }});
                String mdboFlag = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
                if (YES.equals(mdboFlag)) {
                    MtNcCode mtNcCode = mtNcCodeRepository.selectByPrimaryKey(ncCodeId);
                    throw new MtException("HME_NC_0076", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0076", "HME", mtNcCode != null ? mtNcCode.getNcCode() : ""));
                }
            }
        }
        for (HmeNcDisposePlatformVO4 hmeNcDisposePlatformVO4 : dto.getMaterilalList()) {
            if (CollectionUtils.isNotEmpty(hmeNcDisposePlatformVO4.getDetailList())) {
                List<HmeNcDisposePlatformVO5> detailList = hmeNcDisposePlatformVO4.getDetailList();
                for (HmeNcDisposePlatformVO5 hmeNcDisposePlatformVO5 : detailList) {
                    if (Objects.nonNull(hmeNcDisposePlatformVO5.getApplyQty())) {
                        //????????????????????????????????????????????????????????????
                        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeNcDisposePlatformVO5.getMaterialLotId());
                        if (hmeNcDisposePlatformVO5.getReleaseQty().doubleValue() < hmeNcDisposePlatformVO5.getApplyQty().doubleValue()) {
                            throw new MtException("HME_NC_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_NC_0026", "HME", mtMaterialLot.getMaterialLotCode(), "??????"));
                        }
                        //2020-11-26 edit by chaonan.hu for can.wang ????????????????????????????????????????????????????????????????????????
//                        //?????????????????????????????????mt_nc_record??????????????????????????????????????????????????????????????????
//                        long total = hmeNcDisposePlatformMapper.getTotalByMaterialLot(tenantId, mtMaterialLot.getMaterialLotId());
//                        if(total > 0){
//                            throw new MtException("HME_NC_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                    "HME_NC_0028", "HME", mtMaterialLot.getMaterialLotCode()));
//                        }
                        //2020-11-25 edit by chaonan.hu for can.wang ???????????????????????????????????????
//                        //????????????????????????
//                        MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
//                        mtContLoadDtlVO5.setLoadObjectType("MATERIAL_LOT");
//                        mtContLoadDtlVO5.setLoadObjectId(hmeNcDisposePlatformVO5.getMaterialId());
//                        List<String> containerIdList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
//                        if(CollectionUtils.isNotEmpty(containerIdList)){
//                            throw new MtException("HME_NC_0031", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                    "HME_NC_0031", "HME", mtMaterialLot.getMaterialLotCode()));
//                        }
                    }
                }
            }else{
                //????????????????????????????????????????????????????????????
                MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(hmeNcDisposePlatformVO4.getMaterialLotId());
                if (mtMaterialLot.getPrimaryUomQty() < hmeNcDisposePlatformVO4.getApplyQty().doubleValue()) {
                    throw new MtException("HME_NC_0026", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0026", "HME", mtMaterialLot.getMaterialLotCode(), "??????"));
                }
                //2020-11-26 edit by chaonan.hu for can.wang ????????????????????????????????????????????????????????????????????????
//                //?????????????????????????????????mt_nc_record??????????????????????????????????????????????????????????????????
//                long total = hmeNcDisposePlatformMapper.getTotalByMaterialLot(tenantId, mtMaterialLot.getMaterialLotId());
//                if(total > 0){
//                    throw new MtException("HME_NC_0028", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "HME_NC_0028", "HME", mtMaterialLot.getMaterialLotCode()));
//                }
//                //????????????????????????
//                MtContLoadDtlVO5 mtContLoadDtlVO5 = new MtContLoadDtlVO5();
//                mtContLoadDtlVO5.setLoadObjectType("MATERIAL_LOT");
//                mtContLoadDtlVO5.setLoadObjectId(hmeNcDisposePlatformVO4.getMaterialId());
//                List<String> containerIdList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, mtContLoadDtlVO5);
//                if(CollectionUtils.isNotEmpty(containerIdList)){
//                    throw new MtException("HME_NC_0031", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                            "HME_NC_0031", "HME", mtMaterialLot.getMaterialLotCode()));
//                }
            }
        }
        return hmeNcDisposePlatformRepository.materialNcTypeCreate(tenantId, dto);
    }

    @Override
    @ProcessLovValue
    public List<HmeNcDisposePlatformDTO2> ncRecordSingleQuery(Long tenantId, HmeNcDisposePlatformDTO dto) {
        List<HmeNcDisposePlatformDTO2> resultList = new ArrayList<>();
        List<MtModOrganizationRel> mtModOrganizationRels = mtModOrganizationRelRepository.select(new MtModOrganizationRel() {{
            setTenantId(tenantId);
            setOrganizationId(dto.getWorkcellId());
        }});
        String siteId = mtModOrganizationRels.get(0).getTopSiteId();
        //??????????????????????????????????????????????????????
        Date shiftStartTime = null;
        MtModOrganizationVO2 mtModOrganizationVO = new MtModOrganizationVO2();
        mtModOrganizationVO.setTopSiteId(siteId);
        mtModOrganizationVO.setOrganizationType("WORKCELL");
        mtModOrganizationVO.setOrganizationId(dto.getProcessId());
        mtModOrganizationVO.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO.setQueryType("TOP");
        List<MtModOrganizationItemVO> mtModOrganizationItemVOS2 = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO);
        if (CollectionUtils.isNotEmpty(mtModOrganizationItemVOS2)) {
            MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, mtModOrganizationItemVOS2.get(0).getOrganizationId());
            if (mtWkcShiftVO3 != null) {
                MtWkcShift mtWkcShift = mtWkcShiftRepository.selectByPrimaryKey(mtWkcShiftVO3.getWkcShiftId());
                shiftStartTime = mtWkcShift.getShiftStartTime();
            }
        }
        //????????????????????????????????????
        if (shiftStartTime == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            shiftStartTime = calendar.getTime();
        }
        List<MtNcRecord> mtNcRecords = mtNcRecordMapper.mtNcRecordQuery(tenantId, shiftStartTime, dto.getWorkcellId());

        //V20211217 modify by penglin.sui ?????????????????????
        Map<String,String> workcellMap = new HashMap<>();
        Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
        Map<String,MtMaterial> matetialMap = new HashMap<>();
        Map<String,String> materialLotMap = new HashMap<>();
        Map<String,String> workOrderMap = new HashMap<>();
        Map<String , List<HmeNcCheckVO5>> ncListMap = new HashMap<>();
        Map<String , MtNcGroup> ncGroupMap = new HashMap<>();
        Map<String , String> ncIncidentMap = new HashMap<>();
        Map<String , String> statusMap = new HashMap<>();
        Map<String , List<HmeNcRecordAttr>> ncRecordAttrMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(mtNcRecords)){
            //??????
            List<String> workcellIdList = mtNcRecords.stream()
                    .map(MtNcRecord::getWorkcellId)
                    .distinct()
                    .collect(Collectors.toList());
            List<String> rootCauseWorkcellIdList = mtNcRecords.stream()
                    .map(MtNcRecord::getRootCauseWorkcellId)
                    .distinct()
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(rootCauseWorkcellIdList)){
                workcellIdList.addAll(rootCauseWorkcellIdList);
                workcellIdList = workcellIdList.stream().distinct().collect(Collectors.toList());
            }
            if(CollectionUtils.isNotEmpty(workcellIdList)){
                List<MtModWorkcell> workcellList = mtModWorkcellRepository.selectByCondition(Condition.builder(MtModWorkcell.class)
                        .select(MtModWorkcell.FIELD_WORKCELL_ID, MtModWorkcell.FIELD_WORKCELL_NAME).andWhere(Sqls.custom()
                        .andIn(MtModWorkcell.FIELD_WORKCELL_ID, workcellIdList)).build());
                if(CollectionUtils.isNotEmpty(workcellList)){
                    workcellMap = workcellList.stream().collect(Collectors.toMap(MtModWorkcell::getWorkcellId, MtModWorkcell::getWorkcellName));
                }
            }

            //?????????
            List<Long> createByList = mtNcRecords.stream()
                    .map(MtNcRecord::getCreatedBy)
                    .distinct()
                    .collect(Collectors.toList());
            List<Long> userIdList = mtNcRecords.stream()
                    .map(MtNcRecord::getUserId)
                    .distinct()
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(userIdList)){
                createByList.addAll(userIdList);
                createByList = createByList.stream().distinct().collect(Collectors.toList());
            }
            if(CollectionUtils.isNotEmpty(createByList)) {
                userInfoMap = mtUserRepository.userPropertyBatchGetOfRk(tenantId, createByList);
            }

            //??????
            List<String> materialIdList = mtNcRecords.stream()
                    .map(MtNcRecord::getMaterialId)
                    .distinct()
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(materialIdList)){
                List<MtMaterial> materialList = mtMaterialRepository.selectByCondition(Condition.builder(MtMaterial.class)
                        .select(MtMaterial.FIELD_MATERIAL_ID, MtMaterial.FIELD_MATERIAL_CODE, MtMaterial.FIELD_MATERIAL_NAME)
                        .andWhere(Sqls.custom()
                                .andIn(MtMaterial.FIELD_MATERIAL_ID, materialIdList)).build());
                if(CollectionUtils.isNotEmpty(materialList)){
                    matetialMap = materialList.stream().collect(Collectors.toMap(MtMaterial::getMaterialId, t -> t));
                }
            }

            //??????
            List<String> materialLotIdList = mtNcRecords.stream()
                    .map(MtNcRecord::getMaterialLotId)
                    .distinct()
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(materialLotIdList)){
                List<MtMaterialLot> materialLotList = mtMaterialLotRepository.selectByCondition(Condition.builder(MtMaterialLot.class)
                        .select(MtMaterialLot.FIELD_MATERIAL_LOT_ID, MtMaterialLot.FIELD_MATERIAL_LOT_CODE)
                        .andWhere(Sqls.custom()
                                .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_ID, materialLotIdList)).build());
                if(CollectionUtils.isNotEmpty(materialLotList)){
                    materialLotMap = materialLotList.stream().collect(Collectors.toMap(MtMaterialLot::getMaterialLotId, MtMaterialLot::getMaterialLotCode));
                }
            }

            //??????
            List<String> eoIdList = mtNcRecords.stream()
                    .map(MtNcRecord::getEoId)
                    .distinct()
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(eoIdList)){
                List<HmeNcDisposePlatformVO10> workOrderList = hmeNcDisposePlatformMapper.queryWoOfEo(tenantId , eoIdList);
                if(CollectionUtils.isNotEmpty(workOrderList)){
                    workOrderMap = workOrderList.stream().collect(Collectors.toMap(HmeNcDisposePlatformVO10::getEoId, HmeNcDisposePlatformVO10::getWorkOrderNum));
                }
            }

            List<String> ncRecordIdList = mtNcRecords.stream()
                    .map(MtNcRecord::getNcRecordId)
                    .distinct()
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(ncRecordIdList)){
                //??????
                List<HmeNcCheckVO5> ncCheckVO5List = hmeNcCheckMapper.childNcCodeQuery(tenantId , ncRecordIdList);
                if(CollectionUtils.isNotEmpty(ncCheckVO5List)){
                    ncListMap = ncCheckVO5List.stream().collect(Collectors.groupingBy(HmeNcCheckVO5::getNcRecordId));
                }

                //????????????
                List<HmeNcRecordAttr> ncRecordAttrList = hmeNcRecordAttrRepository.selectByCondition(Condition.builder(HmeNcRecordAttr.class)
                        .select(HmeNcRecordAttr.FIELD_PARENT_RECORD_ID, HmeNcRecordAttr.FIELD_PROCESS_METHOD)
                        .andWhere(Sqls.custom()
                                .andEqualTo(HmeNcRecordAttr.FIELD_TENANT_ID , tenantId)
                                .andIn(HmeNcRecordAttr.FIELD_PARENT_RECORD_ID, ncRecordIdList)).build());
                if(CollectionUtils.isNotEmpty(ncRecordAttrList)){
                    ncRecordAttrMap = ncRecordAttrList.stream().collect(Collectors.groupingBy(HmeNcRecordAttr::getParentRecordId));
                }
            }

            //?????????
            List<String> ncCodeIdList = mtNcRecords.stream()
                    .map(MtNcRecord::getNcCodeId)
                    .distinct()
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(ncCodeIdList)){
                List<MtNcGroup> mtNcGroupList = mtNcGroupRepository.selectByCondition(Condition.builder(MtNcGroup.class)
                        .select(MtNcGroup.FIELD_NC_GROUP_ID, MtNcGroup.FIELD_DESCRIPTION, MtNcGroup.FIELD_COMPONENT_REQUIRED)
                        .andWhere(Sqls.custom()
                                .andIn(MtNcGroup.FIELD_NC_GROUP_ID, ncCodeIdList)).build());
                if(CollectionUtils.isNotEmpty(mtNcGroupList)){
                    ncGroupMap = mtNcGroupList.stream().collect(Collectors.toMap(MtNcGroup::getNcGroupId, t -> t));
                }
            }

            //????????????
            List<String> ncIncidentIdList = mtNcRecords.stream()
                    .map(MtNcRecord::getNcIncidentId)
                    .distinct()
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(ncIncidentIdList)){
                List<MtNcIncident> ncIncidentList = mtNcIncidentRepository.selectByCondition(Condition.builder(MtNcIncident.class)
                        .select(MtNcIncident.FIELD_NC_INCIDENT_ID, MtNcIncident.FIELD_INCIDENT_NUMBER)
                        .andWhere(Sqls.custom()
                                .andIn(MtNcIncident.FIELD_NC_INCIDENT_ID, ncIncidentIdList)).build());
                if(CollectionUtils.isNotEmpty(ncIncidentList)){
                    ncIncidentMap = ncIncidentList.stream().collect(Collectors.toMap(MtNcIncident::getNcIncidentId, MtNcIncident::getIncidentNumber));
                }
            }
        }

        //??????
        List<MtGenStatus> genStatusList = mtGenStatusRepository.getGenStatuz(tenantId, "NC_RECORD", "NC_RECORD_STATUS");
        if(CollectionUtils.isNotEmpty(genStatusList)){
            statusMap = genStatusList.stream().collect(Collectors.toMap(MtGenStatus::getStatusCode, MtGenStatus::getDescription));
        }

        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.NC_PROCESS_METHOD", tenantId);
        Map<String , String> lovValueMap = new HashMap<>(lovValueDTOS.size());
        if(CollectionUtils.isNotEmpty(lovValueDTOS)){
            lovValueMap = lovValueDTOS.stream().collect(Collectors.toMap(LovValueDTO::getValue, LovValueDTO::getMeaning));
        }

        for (MtNcRecord mtNcRecordDb : mtNcRecords) {
            //????????????????????????????????????
            HmeNcDisposePlatformDTO2 hmeNcDisposePlatformDTO2 = new HmeNcDisposePlatformDTO2();
            hmeNcDisposePlatformDTO2.setNcRecordId(mtNcRecordDb.getNcRecordId());
            //?????? ??????ROOT_CAUSE_WORKCELL_ID??????API{parentOrganizationRelQuery}
//            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
//            mtModOrganizationVO2.setTopSiteId(mtNcRecordDb.getSiteId());
//            mtModOrganizationVO2.setOrganizationType("WORKCELL");
//            mtModOrganizationVO2.setOrganizationId(mtNcRecordDb.getRootCauseWorkcellId());
//            mtModOrganizationVO2.setParentOrganizationType("AREA");
//            mtModOrganizationVO2.setQueryType("ALL");
//            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
//            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
//                MtModArea mtModArea = mtModAreaRepository.selectByPrimaryKey(mtModOrganizationItemVO.getOrganizationId());
//                if ("CJ".equals(mtModArea.getAreaCategory())) {
//                    hmeNcDisposePlatformDTO2.setWorkShopName(mtModArea.getAreaName());
//                    break;
//                }
//            }
            //?????????
//            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
//            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
//            if (CollectionUtils.isNotEmpty(mtModOrganizationItemVOS)) {
//                MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(mtModOrganizationItemVOS.get(0).getOrganizationId());
//                hmeNcDisposePlatformDTO2.setProdLineName(mtModProductionLine.getDescription());
//            }
            //???????????????
//            mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
//            mtModOrganizationItemVOS = mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
//            for (MtModOrganizationItemVO mtModOrganizationItemVO : mtModOrganizationItemVOS) {
//                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(mtModOrganizationItemVO.getOrganizationId());
//                if ("LINE".equals(mtModWorkcell.getWorkcellType())) {
//                    hmeNcDisposePlatformDTO2.setLineWorkcellName(mtModWorkcell.getWorkcellName());
//                } else if ("PROCESS".equals(mtModWorkcell.getWorkcellType())) {
//                    hmeNcDisposePlatformDTO2.setProcessName(mtModWorkcell.getWorkcellName());
//                }
//            }
            //?????? ??????ROOT_CAUSE_WORKCELL_ID???WORKCELL_TYPE = STATION?????? mt_mod_workcell??????WORKCELL_NAME
//            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(mtNcRecordDb.getWorkcellId());
//            hmeNcDisposePlatformDTO2.setWorkcellName(mtModWorkcell.getWorkcellName());
            hmeNcDisposePlatformDTO2.setWorkcellName(workcellMap.getOrDefault(mtNcRecordDb.getWorkcellId() , ""));
            //?????????
//            MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, mtNcRecordDb.getCreatedBy());
            MtUserInfo mtUserInfo = userInfoMap.getOrDefault(mtNcRecordDb.getCreatedBy() , null);
            if(Objects.nonNull(mtUserInfo)) {
                hmeNcDisposePlatformDTO2.setUserName(mtUserInfo.getRealName());
                //??????
                hmeNcDisposePlatformDTO2.setUserCode(mtUserInfo.getLoginName());
            }
            //???????????? MATERIAL_ID??????mt_material??????material_code
//            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtNcRecordDb.getMaterialId());
            MtMaterial mtMaterial = matetialMap.getOrDefault(mtNcRecordDb.getMaterialId() , null);
            if(Objects.nonNull(mtMaterial)) {
                hmeNcDisposePlatformDTO2.setMaterialCode(mtMaterial.getMaterialCode());
                //????????????
                hmeNcDisposePlatformDTO2.setMaterialName(mtMaterial.getMaterialName());
            }
            //????????? MATERIAL_LOT_ID??????mt_material_lot??????material_lot_code?????????????????????material_Lot_id
//            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(mtNcRecordDb.getMaterialLotId());
            String materialLotCode = materialLotMap.getOrDefault(mtNcRecordDb.getMaterialLotId() , "");
//            if (mtMaterialLot != null && StringUtils.isNotBlank(mtMaterialLot.getMaterialLotCode())) {
//                hmeNcDisposePlatformDTO2.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
//            } else {
//                hmeNcDisposePlatformDTO2.setMaterialLotCode(mtNcRecordDb.getMaterialLotId());
//            }
            hmeNcDisposePlatformDTO2.setMaterialLotCode(StringUtils.isNotBlank(materialLotCode) ? materialLotCode : mtNcRecordDb.getMaterialLotId());
            //????????? ??????eo_id?????????mt_eo?????????work_order_id?????????work_order_id??????mt_work_order??????WORK_ORDER_NUM
//            MtEo mtEo = mtEoRepository.selectByPrimaryKey(mtNcRecordDb.getEoId());
//            if (mtEo != null && StringUtils.isNotBlank(mtEo.getWorkOrderId())) {
//                MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(mtEo.getWorkOrderId());
//                if (mtWorkOrder != null) {
//                    hmeNcDisposePlatformDTO2.setWorkOrderNum(mtWorkOrder.getWorkOrderNum());
//                }
//            }
            hmeNcDisposePlatformDTO2.setWorkOrderNum(workOrderMap.getOrDefault(mtNcRecordDb.getEoId(),""));
            //???????????? ????????????
//            List<MtNcRecord> mtNcRecordList = hmeNcCheckMapper.childNcRecordQuery(tenantId, hmeNcDisposePlatformDTO2.getNcRecordId());
            List<HmeNcCheckVO5> ncCheckVO5List = ncListMap.getOrDefault(hmeNcDisposePlatformDTO2.getNcRecordId() , new ArrayList<>());
            List<String> ncCodeIdList = new ArrayList<>();
            List<String> ncCodeList = new ArrayList<>();
            List<String> ncReasonList = new ArrayList<>();
//            for (MtNcRecord mtNcRecord : mtNcRecordList) {
//                if (StringUtils.isNotEmpty(mtNcRecord.getNcCodeId())) {
//                    //??????NC_CODE_ID?????????mt_nc_code??????nc_code
//                    MtNcCode mtNcCode = mtNcCodeRepository.selectByPrimaryKey(mtNcRecord.getNcCodeId());
//                    ncCodeIdList.add(mtNcCode.getNcCodeId());
//                    ncCodeList.add(mtNcCode.getNcCode());
//                    ncReasonList.add(mtNcCode.getDescription());
//                }
//            }
            if(CollectionUtils.isNotEmpty(ncCheckVO5List)){
                ncCodeIdList = ncCheckVO5List.stream().map(HmeNcCheckVO5::getNcCodeId).collect(Collectors.toList());
                ncCodeList = ncCheckVO5List.stream().map(HmeNcCheckVO5::getNcCode).collect(Collectors.toList());
                ncReasonList = ncCheckVO5List.stream().map(HmeNcCheckVO5::getDescription).collect(Collectors.toList());
            }
            hmeNcDisposePlatformDTO2.setNcCodeIdList(ncCodeIdList);
            hmeNcDisposePlatformDTO2.setNcCodeList(ncCodeList);
            hmeNcDisposePlatformDTO2.setNcReasonList(ncReasonList);
            //???????????????  ????????????
//            MtNcGroup mtNcGroup = mtNcGroupRepository.selectByPrimaryKey(mtNcRecordDb.getNcCodeId());
            MtNcGroup mtNcGroup = ncGroupMap.getOrDefault(mtNcRecordDb.getNcCodeId() , null);
            if (Objects.nonNull(mtNcGroup)) {
                hmeNcDisposePlatformDTO2.setNcGroupDesc(mtNcGroup.getDescription());
                hmeNcDisposePlatformDTO2.setNcType(mtNcGroup.getComponentRequired());
            }
            //????????????
//            MtNcIncident mtNcIncident = mtNcIncidentRepository.selectByPrimaryKey(mtNcRecordDb.getNcIncidentId());
//            hmeNcDisposePlatformDTO2.setNcNumber(mtNcIncident.getIncidentNumber());
            hmeNcDisposePlatformDTO2.setNcNumber(ncIncidentMap.getOrDefault(mtNcRecordDb.getNcIncidentId() , ""));
            //????????????
            hmeNcDisposePlatformDTO2.setDateTime(mtNcRecordDb.getDateTime());
            //????????????
            if (StringUtils.isNotEmpty(mtNcRecordDb.getRootCauseWorkcellId())) {
//                MtModWorkcell mtModWorkcell2 = mtModWorkcellRepository.selectByPrimaryKey(mtNcRecordDb.getRootCauseWorkcellId());
//                hmeNcDisposePlatformDTO2.setResponseWorkcellName(mtModWorkcell2.getWorkcellName());
                hmeNcDisposePlatformDTO2.setResponseWorkcellName(workcellMap.getOrDefault(mtNcRecordDb.getRootCauseWorkcellId() , ""));
            }
            //?????????
//            MtUserInfo mtUserInfo2 = mtUserRepository.userPropertyGet(tenantId, mtNcRecordDb.getUserId());
//            hmeNcDisposePlatformDTO2.setResponseUser(mtUserInfo2.getRealName());
            MtUserInfo responseUserInfo = userInfoMap.getOrDefault(mtNcRecordDb.getUserId() , null);
            if(Objects.nonNull(responseUserInfo)) {
                hmeNcDisposePlatformDTO2.setResponseUser(responseUserInfo.getRealName());
            }
            //??????
            hmeNcDisposePlatformDTO2.setStatus(mtNcRecordDb.getNcStatus());
//            MtGenStatus statusMeaning = mtGenStatusRepository.getGenStatus(tenantId, "NC_RECORD", "NC_RECORD_STATUS", mtNcRecordDb.getNcStatus());
//            hmeNcDisposePlatformDTO2.setStatusMeaning(statusMeaning.getDescription());
            hmeNcDisposePlatformDTO2.setStatusMeaning(statusMap.getOrDefault(mtNcRecordDb.getNcStatus() , ""));
            //????????????
//            HmeNcRecordAttr hmeNcRecordAttr = hmeNcRecordAttrRepository.selectOne(new HmeNcRecordAttr() {{
//                setTenantId(tenantId);
//                setParentRecordId(mtNcRecordDb.getNcRecordId());
//            }});
            List<HmeNcRecordAttr> ncRecordAttrList = ncRecordAttrMap.getOrDefault(mtNcRecordDb.getNcRecordId() , new ArrayList<>());
            if(CollectionUtils.isNotEmpty(ncRecordAttrList)){
                HmeNcRecordAttr hmeNcRecordAttr = ncRecordAttrList.get(0);
                if (Objects.nonNull(hmeNcRecordAttr)) {
                    hmeNcDisposePlatformDTO2.setDisposeMethod(hmeNcRecordAttr.getProcessMethod());
//                    String disposeMethodMeaning = lovAdapter.queryLovMeaning("HME.NC_PROCESS_METHOD", tenantId, hmeNcRecordAttr.getProcessMethod());
                    hmeNcDisposePlatformDTO2.setDisposeMethodMeaning(lovValueMap.getOrDefault(hmeNcRecordAttr.getProcessMethod() , ""));
                }
            }
            resultList.add(hmeNcDisposePlatformDTO2);
        }
        return resultList;
    }

    @Override
    public String materialDataDelete(Long tenantId, List<HmeNcDisposePlatformDTO22> dtoList) {
        //????????????
        for (HmeNcDisposePlatformDTO22 dto : dtoList) {
            if (StringUtils.isEmpty(dto.getNcComponentTempId())) {
                throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "?????????Id"));
            }
            if (StringUtils.isEmpty(dto.getMaterialLotId())) {
                throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0006", "HME", "?????????Id"));
            }
        }
        return hmeNcDisposePlatformRepository.materialDataDelete(tenantId, dtoList);
    }

    @Override
    public List<HmeNcDisposePlatformVO4> materialDataSingleQuery(Long tenantId, HmeNcDisposePlatformDTO30 dto) {
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(dto.getMaterialLotCode());
        }});
        return hmeNcDisposePlatformRepository.materialDataQuery2(tenantId, mtMaterialLot, dto.getProcessId(), dto.getWorkcellId());
    }

    @Override
    public String workcellScan(Long tenantId, HmeNcDisposePlatformDTO5 dto) {
        if(StringUtils.isEmpty(dto.getProcessId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????ID"));
        }
        if(StringUtils.isEmpty(dto.getWorkcellCode())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "????????????"));
        }
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectOne(new MtModWorkcell() {{
            setTenantId(tenantId);
            setWorkcellCode(dto.getWorkcellCode());
        }});
        if(Objects.isNull(mtModWorkcell)){
            //??????????????????????????????
            throw new MtException("HME_NC_0036", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0036", "HME"));
        }else{
            //???????????????????????????Y,??????
            if(!YES.equals(mtModWorkcell.getEnableFlag())){
                throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_001", "HME"));
            }
        }
        MtModWorkcell process = hmeSignInOutRecordMapper.getProcessByWorkcell(tenantId, mtModWorkcell.getWorkcellId());
        if(Objects.isNull(process)){
            //?????????????????????????????????????????????
            throw new MtException("HME_NC_0082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0082", "HME"));
        }else if(!process.getWorkcellId().equals(dto.getProcessId())){
            throw new MtException("HME_NC_0082", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0082", "HME"));
        }
        return mtModWorkcell.getWorkcellId();
    }
}
