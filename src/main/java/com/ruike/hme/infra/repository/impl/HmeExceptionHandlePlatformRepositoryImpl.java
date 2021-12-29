package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeExceptionDTO;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeExcWkcRecordMapper;
import com.ruike.hme.infra.mapper.HmeExceptionGroupMapper;
import com.ruike.hme.infra.mapper.HmeExceptionHandlePlatformMapper;
import com.ruike.hme.infra.mapper.HmeExceptionMapper;
import com.ruike.itf.app.service.ItfExceptionWkcRecordService;
import com.ruike.itf.domain.vo.IftSendOAExceptionMsgVO;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.actual.domain.repository.MtWkcShiftRepository;
import tarzan.actual.domain.vo.MtWkcShiftVO3;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.modeling.domain.vo.MtModWorkcellVO1;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname HmeExceptionHandlePlatformRepositoryImpl
 * @Description 异常处理平台
 * @Date 2020/6/22 14:30
 * @Created by Deng xu
 */
@Slf4j
@Component
public class HmeExceptionHandlePlatformRepositoryImpl implements HmeExceptionHandlePlatformRepository {

    @Autowired
    private HmeExceptionHandlePlatformMapper mapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtWkcShiftRepository mtWkcShiftRepository;
    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;
    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private HmeExcWkcRecordRepository hmeExcWkcRecordRepository;
    @Autowired
    private HmeExcWkcRecordHisRepository hmeExcWkcRecordHisRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private HmeExceptionMapper hmeExceptionMapper;
    @Autowired
    private ItfExceptionWkcRecordService itfExceptionWkcRecordService;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private HmeExceptionGroupRepository hmeExceptionGroupRepository;
    @Autowired
    private HmeExcGroupWkcAssignRepository hmeExcGroupWkcAssignRepository;
    @Autowired
    private HmeExcGroupAssignRepository hmeExcGroupAssignRepository;
    @Autowired
    private HmeExcWkcRecordMapper hmeExcWkcRecordMapper;


    /**
     * @param tenantId     租户ID
     * @param workcellCode 工位编码
     * @param siteId       用户默认站点ID
     * @return : java.util.List<com.ruike.hme.domain.vo.HmeExceptionHandlePlatFormVO>
     * @Description: 异常处理平台-主界面查询
     * @author: Deng Xu
     * @date 2020/6/22 15:51
     * @version 1.0
     */
    @Override
    public List<HmeExceptionHandlePlatFormVO> listExceptionForUi(Long tenantId, String workcellCode, String siteId) {
        //判断工位编码是否传入:当前工位无效,请检查工位条码
        if (StringUtils.isEmpty(workcellCode)) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_001", "HME"));
        }
        //判断用户默认站点ID是否传入:获取用户默认站点失败，请检查
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_013", "HME"));
        }
        //根据工位编码获取工位ID
        MtModWorkcellVO1 mtModWorkcellVO1 = new MtModWorkcellVO1();
        mtModWorkcellVO1.setWorkcellCode(workcellCode);
        mtModWorkcellVO1.setWorkcellType("STATION");
        List<String> workcellIdList = mtModWorkcellRepository.propertyLimitWorkcellQuery(tenantId, mtModWorkcellVO1);
        if (CollectionUtils.isEmpty(workcellIdList) || StringUtils.isEmpty(workcellIdList.get(0))) {
            //当前工位无效,请检查工位条码
            throw new MtException("HME_EO_JOB_SN_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_001", "HME"));
        }
        String workcellId = workcellIdList.get(0);
        //获取当前工位对应的工序
        MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(siteId);
        mtModOrganizationVO2.setOrganizationId(workcellId);
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO2.setQueryType("BOTTOM");
        List<MtModOrganizationItemVO> parentOrganizationRel =
                mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        if (CollectionUtils.isEmpty(parentOrganizationRel) || StringUtils.isEmpty(parentOrganizationRel.get(0).getOrganizationId())) {
            //请先维护Wkc工序工位关系
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_002", "HME"));
        }
        String parentWorkcellId = parentOrganizationRel.get(0).getOrganizationId();
        //获取当前工位对应的工序对应的工段id即工位的父层的父层workcell_id
        mtModOrganizationVO2 = new MtModOrganizationVO2();
        mtModOrganizationVO2.setTopSiteId(siteId);
        mtModOrganizationVO2.setOrganizationId(workcellId);
        mtModOrganizationVO2.setOrganizationType("WORKCELL");
        mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
        mtModOrganizationVO2.setQueryType("TOP");
        List<MtModOrganizationItemVO> topOrganizationRel =
                mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
        if (CollectionUtils.isEmpty(topOrganizationRel) || StringUtils.isEmpty(topOrganizationRel.get(0).getOrganizationId())) {
            //请先维护Wkc工序工位关系
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_002", "HME"));
        }
        String topWorkcellId = topOrganizationRel.get(0).getOrganizationId();
        //根据最顶层workcellId调用API【wkcCurrentShiftQuery】获取工位班次ID
        MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, topWorkcellId);
        String wkcShiftId = mtWkcShiftVO3.getWkcShiftId();
        //查询类型组“EXCEPTION_TYPE”（异常类型，共5类）
        //人员（MAN），设备（EQUIPMENT），物料（MATERIAL），工艺质量（METHOD），环境（ENVIRONMENTS）
        List<MtGenType> exceptionTypeList = this.getGenType(tenantId);
        //对于不同异常类型进行循环查询，共5类，并返回
        List<HmeExceptionHandlePlatFormVO> exceptionVoList = new ArrayList<>();
        for (MtGenType genType : exceptionTypeList) {
            //每一个类别对应一个返回信息
            HmeExceptionHandlePlatFormVO exceptionVo = new HmeExceptionHandlePlatFormVO();
            exceptionVo.setWorkcellId(workcellId);
            exceptionVo.setWorkcellCode(workcellCode);
            exceptionVo.setExceptionType(genType.getTypeCode());
            exceptionVo.setExceptionTypeMeaning(genType.getDescription());
            //先根据租户、异常类型、工位ID、班次工位ID查询未解决异常数量、本班次异常数量和总异常数量
            HmeExceptionHandlePlatFormVO condition = new HmeExceptionHandlePlatFormVO();
            condition.setTenantId(tenantId);
            condition.setExceptionType(genType.getTypeCode());
            condition.setWorkcellId(workcellId);
            condition.setWkcShiftId(wkcShiftId);
            HmeExceptionHandlePlatFormVO quantityResult = mapper.queryUnresolvedExcQty(condition);
            //设置返回未解决异常数量
            exceptionVo.setUnresolvedExcQty(quantityResult.getUnresolvedExcQty());
            //设置返回本班次异常数量
            exceptionVo.setShiftExcQty(quantityResult.getShiftExcQty());
            //设置返回总异常数量
            exceptionVo.setTotalExcQty(quantityResult.getTotalExcQty());
            //查询异常清单内容,关闭状态的只查询两天内的
            List<HmeExceptionRecordQueryVO> exceptionRecordList = mapper.queryExceptionRecordList(condition);
            exceptionVo.setExceptionRecordList(exceptionRecordList);
            //将查询条件的workcellId换为工序ID，查询新建异常时的异常标签集合，将班次、工位、操作者也查出来
            condition.setParentWorkcellId(parentWorkcellId);
            //获取当前用户ID
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            condition.setUserId(userId);
            List<HmeExceptionRecordQueryVO> labelList = mapper.queryExceptionLabelList(condition);
            exceptionVo.setExceptionLabelList(labelList);
            //将结果添加到返回集合
            exceptionVoList.add(exceptionVo);
        }
        return exceptionVoList;
    }

    /**
     * @param tenantId 租户ID
     * @param createVO 创建VO（包含设备编码）
     * @return : com.ruike.hme.domain.vo.HmeExcRecordCreateVO
     * @Description: 异常处理平台-扫描设备编码后校验并返回设备ID
     * @author: Deng Xu
     * @date 2020/6/23 11:20
     * @version 1.0
     */
    @Override
    public HmeExcRecordCreateVO equipmentVerification(Long tenantId, HmeExcRecordCreateVO createVO) {
        //先校验是否扫描设备：请扫描设备
        if (StringUtils.isEmpty(createVO.getEquipmentCode())) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_005", "HME"));
        }
        //校验当前设备编码是否存在
        HmeEquipment queryEquipment = new HmeEquipment();
        queryEquipment.setTenantId(tenantId);
        queryEquipment.setAssetEncoding(createVO.getEquipmentCode());
        queryEquipment = hmeEquipmentRepository.selectOne(queryEquipment);
        //查询不到，则提示“当前设备资产编码不存在”
        if (null == queryEquipment || StringUtils.isEmpty(queryEquipment.getEquipmentId())) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_006", "HME", createVO.getEquipmentCode()));
        }
        //将设备ID返回
        createVO.setEquipmentId(queryEquipment.getEquipmentId());
        return createVO;
    }

    /**
     * @param tenantId 租户ID
     * @param createVO 创建VO（包含物料条码）
     * @return : com.ruike.hme.domain.vo.HmeExcRecordCreateVO
     * @Description: 异常处理平台-扫描物料条码后校验是否存在并返回物料信息
     * @author: Deng Xu
     * @date 2020/6/23 11:34
     * @version 1.0
     */
    @Override
    public HmeExcRecordCreateVO materialLotVerification(Long tenantId, HmeExcRecordCreateVO createVO) {
        //先校验是否扫描物料条码：请扫描物料条码
        if (StringUtils.isEmpty(createVO.getMaterialLotCode())) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_007", "HME"));
        }
        //校验当前物料条码是否存在
        MtMaterialLot queryMaterialLot = new MtMaterialLot();
        queryMaterialLot.setTenantId(tenantId);
        queryMaterialLot.setMaterialLotCode(createVO.getMaterialLotCode());
        queryMaterialLot = mtMaterialLotRepository.selectOne(queryMaterialLot);
        if (null == queryMaterialLot || StringUtils.isEmpty(queryMaterialLot.getMaterialLotId())) {
            //关联不到则，提示“当前物料条码不存在”
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_008", "HME", createVO.getMaterialLotCode()));
        }
        //未查询到物料条码的关联物料
        if (StringUtils.isEmpty(queryMaterialLot.getMaterialId())) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_009", "HME", createVO.getMaterialLotCode()));
        }
        //查找物料信息并返回给前台
        MtMaterial queryMaterial = new MtMaterial();
        queryMaterial.setTenantId(tenantId);
        queryMaterial.setMaterialId(queryMaterialLot.getMaterialId());
        queryMaterial = mtMaterialRepository.selectOne(queryMaterial);
        if (null == queryMaterial || StringUtils.isEmpty(queryMaterial.getMaterialId())) {
            //关联不到则，提示“未查询到物料条码的关联物料”
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_009", "HME", createVO.getMaterialLotCode()));
        }
        createVO.setMaterialLotId(queryMaterialLot.getMaterialLotId());
        createVO.setMaterialId(queryMaterial.getMaterialId());
        createVO.setMaterialCode(queryMaterial.getMaterialCode());
        createVO.setMaterialName(queryMaterial.getMaterialName());
        return createVO;
    }

    /**
     * @param tenantId 租户ID
     * @param createVO 创建VO
     * @return : com.ruike.hme.domain.entity.HmeExcWkcRecord
     * @Description: 异常处理平台-创建异常记录
     * @author: Deng Xu
     * @date 2020/6/23 14:13
     * @version 1.0
     */
    @Override
    public HmeExcWkcRecord createExceptionRecord(Long tenantId, HmeExcRecordCreateVO createVO) {
        //判断工位是否传入,否则报错:获取工位失败,请检查
        if (StringUtils.isEmpty(createVO.getWorkcellId())) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_001", "HME"));
        }
        //判断异常ID是否传入,否则报错:获取异常收集项失败,请检查
        if (StringUtils.isEmpty(createVO.getExceptionId())) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_010", "HME"));
        }
        //判断异常收集组ID是否传入,否则报错:获取异常收集组失败,请检查
        if (StringUtils.isEmpty(createVO.getExceptionGroupId())) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_011", "HME"));
        }
        //判断异常等级是否传入,否则报错:获取异常等级失败,请检查
        if (StringUtils.isEmpty(createVO.getExceptionGroupId())) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_012", "HME"));
        }
        //判断用户默认站点ID是否传入:获取用户默认站点失败，请检查
        if (StringUtils.isEmpty(createVO.getSiteId())) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_013", "HME"));
        }
        // 如果不是WKC类型的则跳过校验
        String wkcShiftId = "";
        if ("WKC".equals(createVO.getInitiationType())) {
            //获取当前工位对应的工序对应的工段id即工位的父层的父层workcell_id
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(createVO.getSiteId());
            mtModOrganizationVO2.setOrganizationId(createVO.getWorkcellId());
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setParentOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("TOP");
            List<MtModOrganizationItemVO> topOrganizationRel =
                    mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            if (CollectionUtils.isEmpty(topOrganizationRel) || StringUtils.isEmpty(topOrganizationRel.get(0).getOrganizationId())) {
                //请先维护Wkc工序工位关系
                throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EXCEPTION_HANDLE_PLATFORM_002", "HME"));
            }

            String topWorkcellId = topOrganizationRel.get(0).getOrganizationId();
            //根据最顶层workcellId调用API【wkcCurrentShiftQuery】获取工位班次ID
            MtWkcShiftVO3 mtWkcShiftVO3 = mtWkcShiftRepository.wkcCurrentShiftQuery(tenantId, topWorkcellId);
            wkcShiftId = mtWkcShiftVO3.getWkcShiftId();
        }
        //开始新增异常记录
        HmeExcWkcRecord createRecord = new HmeExcWkcRecord();
        BeanUtils.copyProperties(createVO, createRecord);
        createRecord.setWkcShiftId(wkcShiftId);
        createRecord.setExceptionStatus(HmeConstants.StatusCode.NEW);
        createRecord.setProdFlag(HmeConstants.ConstantValue.NO);
        createRecord.setAttribute1(createVO.getInitiationType());
        createRecord.setAttribute2(createVO.getWorkcellId());
        createRecord.setWorkcellId("WKC".equals(createVO.getInitiationType()) ? createVO.getWorkcellId() : null);
        hmeExcWkcRecordRepository.insertSelective(createRecord);
        //开始新增异常记录历史表
        HmeExcWkcRecordHis insertHis = new HmeExcWkcRecordHis();
        BeanUtils.copyProperties(createRecord, insertHis);
        hmeExcWkcRecordHisRepository.insertSelective(insertHis);
        //查询工位编码并返回
        MtModWorkcell queryWorkcell = new MtModWorkcell();
        if ("WKC".equals(createVO.getInitiationType())) {
            queryWorkcell.setWorkcellId(createVO.getWorkcellId());
            queryWorkcell = mtModWorkcellRepository.selectByPrimaryKey(queryWorkcell);
            createRecord.setWorkcellCode(queryWorkcell.getWorkcellCode());
        }

        //查询事业部
        Map<String, String> stringMap = queryArea2(tenantId, createRecord.getAttribute2(), createRecord.getAttribute1());
        String areaCode = stringMap.size() > 0 ? stringMap.get("areaCode") : null;
        if(StringUtils.isBlank(areaCode)){
            //当前组织关系维护错误,请检查!
            throw new MtException("HME_NC_0059", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0059", "HME"));
        }

        // 发送消息至ESB
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            HmeExceptionDTO hmeExceptionDTO = new HmeExceptionDTO();
            hmeExceptionDTO.setExceptionId(createVO.getExceptionId());
            List<HmeException> hmeExceptions = hmeExceptionMapper.uiQuery(tenantId, hmeExceptionDTO);
            HmeException hmeException = hmeExceptions.get(0);
            // 组装异常信息
            String exceptionWkcRecordId = createRecord.getExceptionWkcRecordId();
            String exceptionType = hmeException.getExceptionTypeName();
            String exceptionCode = hmeException.getExceptionCode();
            String exceptionName = hmeException.getExceptionName();
            String exceptionRemark = createVO.getExceptionRemark();
            String workcellName = queryWorkcell.getWorkcellName();
            String currentTime = format.format(new Date()).replace(" ", "T");
            String applicant = DetailsHelper.getUserDetails().getUsername();

            IftSendOAExceptionMsgVO iftSendOAExceptionMsgVO = new IftSendOAExceptionMsgVO();
            iftSendOAExceptionMsgVO.setId(this.customDbRepository.getNextKey("batch_cid_s"));
            iftSendOAExceptionMsgVO.setExcWkcRecordId(exceptionWkcRecordId);
            iftSendOAExceptionMsgVO.setExceptionType(exceptionType);
            iftSendOAExceptionMsgVO.setExceptionCode(exceptionCode);
            iftSendOAExceptionMsgVO.setExceptionName(exceptionName);
            iftSendOAExceptionMsgVO.setExceptionRemark(exceptionRemark);
            iftSendOAExceptionMsgVO.setWorkcellName(workcellName);
            iftSendOAExceptionMsgVO.setCurrentTime(currentTime);
            iftSendOAExceptionMsgVO.setCreateTime(currentTime);
            iftSendOAExceptionMsgVO.setApplicant(applicant);
            iftSendOAExceptionMsgVO.setExceptionLevel("-1");
            iftSendOAExceptionMsgVO.setUpgradeTime("-1");
            iftSendOAExceptionMsgVO.setStatus("N");
            iftSendOAExceptionMsgVO.setDepartment(areaCode);

            itfExceptionWkcRecordService.sendExceptionInfoEsb(tenantId, iftSendOAExceptionMsgVO);
        } catch (Exception e) {
            log.error("HmeExceptionHandlePlatformRepositoryImpl.createExceptionRecord.sendExceptionInfoEsb:" + e.getMessage());

        }
        return createRecord;
    }

    /**
     * @param tenantId             租户ID
     * @param exceptionWkcRecordId 异常记录ID
     * @return : com.ruike.hme.domain.vo.HmeExceptionQueryVO
     * @Description: 异常处理平台-异常历史查看
     * @author: Deng Xu
     * @date 2020/6/23 10:09
     * @version 1.0
     */
    @Override
    @ProcessLovValue(targetField = {"", "statusHistoryList"})
    public HmeExceptionRecordQueryVO queryHistoryForUi(Long tenantId, String exceptionWkcRecordId) {
        //获取异常记录失败，请检查
        if (StringUtils.isEmpty(exceptionWkcRecordId)) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_004", "HME"));
        }
        //先根据异常记录ID查询异常明细信息
        HmeExceptionRecordQueryVO returnVo = mapper.queryExceptionRecordDetail(exceptionWkcRecordId);
        if (null == returnVo || StringUtils.isEmpty(returnVo.getExceptionWkcRecordId())) {
            //获取异常记录失败，请检查
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_004", "HME"));
        }
        //根据异常记录ID查询异常状态时间轴列表信息
        List<HmeExceptionStatusHistoryVO> statusHistoryList = mapper.queryStatusHistory(tenantId, exceptionWkcRecordId);
        returnVo.setStatusHistoryList(statusHistoryList);
        return returnVo;
    }

    @Override
    public HmeExcWkcRecord closeExceptionRecord(Long tenantId, HmeExcRecordCreateVO createVO) {
        //更新异常记录行状态
        if (StringUtils.isBlank(createVO.getExceptionWkcRecordId())) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_014", "HME"));
        }
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();

        HmeExcWkcRecord hmeExcWkcRecord = hmeExcWkcRecordRepository.selectByPrimaryKey(createVO.getExceptionWkcRecordId());
        hmeExcWkcRecord.setExceptionStatus("CLOSE");
        hmeExcWkcRecord.setCloseTime(new Date());
        hmeExcWkcRecord.setClosedBy(userDetails != null ? userDetails.getUserId() : null);
        hmeExcWkcRecord.setLastUpdateDate(new Date());
        hmeExcWkcRecord.setLastUpdatedBy(userDetails != null ? userDetails.getUserId() : null);
        hmeExcWkcRecordMapper.updateByPrimaryKeySelective(hmeExcWkcRecord);

        //记录关闭历史
        HmeExcWkcRecordHis insertHis = new HmeExcWkcRecordHis();
        BeanUtils.copyProperties(hmeExcWkcRecord, insertHis);
        hmeExcWkcRecordHisRepository.insertSelective(insertHis);
        //查询工位编码并返回
        MtModWorkcell queryWorkcell = new MtModWorkcell();
        queryWorkcell.setWorkcellId(hmeExcWkcRecord.getWorkcellId());
        queryWorkcell = mtModWorkcellRepository.selectByPrimaryKey(queryWorkcell);
        if(Objects.nonNull(queryWorkcell)) {
            hmeExcWkcRecord.setWorkcellCode(queryWorkcell.getWorkcellCode());
        }
        return hmeExcWkcRecord;
    }

    /**
     * 异常处理平台-根据用户查询有效的区域、车间、产线
     *
     * @param tenantId
     * @param userId
     * @return
     */
    @Override
    public HmeAreaWorkshopProdLineVO areaCJProdLineByUserId(Long tenantId, Long userId) {
        HmeAreaWorkshopProdLineVO result = new HmeAreaWorkshopProdLineVO();
        // 查询区域
        List<HmeAreaInfo> areaInfos = mapper.selectAreaByUserId(tenantId, userId, null);
        // 查询车间
        List<HmeAreaInfo> workshopInfos = mapper.selectWorkshopByUserId(tenantId, userId, null);
        // 查询产线
        List<HmeProdLineInfo> prodLineInfos = mapper.selectProdLineByUserId(tenantId, userId, null);
        result.setAreaInfo(areaInfos);
        result.setWorkshopInfo(workshopInfos);
        result.setProdLineInfo(prodLineInfos);
        return result;
    }

    /**
     * <strong>Title : listExceptionNotLoginForUi</strong><br/>
     * <strong>Description : 异常处理平台-主界面查询-不登陆工位查询 </strong><br/>
     * <strong>Create on : 2021/2/25 4:42 下午</strong><br/>
     *
     * @param tenantId
     * @param siteId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeExceptionHandlePlatFormVO>
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * kejin.liu | 2021年03月04日16:17:10 | 添加明细查询<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    @Override
    public List<HmeExceptionHandlePlatFormVO> listExceptionNotLoginForUi(Long tenantId, String siteId) {
        List<MtGenType> exceptionTypeList = this.getGenType(tenantId);

        HmeExceptionGroup hmeExceptionGroup = new HmeExceptionGroup();
        hmeExceptionGroup.setEnableFlag("Y");
        hmeExceptionGroup.setTenantId(tenantId);
        List<HmeExceptionGroup> hmeExceptionGroups = hmeExceptionGroupRepository.select(hmeExceptionGroup);
        List<String> hmeExceptionGroupIds = hmeExceptionGroups.stream().map(HmeExceptionGroup::getExceptionGroupId).distinct().collect(Collectors.toList());
        HmeExcGroupWkcAssign hmeExcGroupWkcAssign = new HmeExcGroupWkcAssign();
        hmeExcGroupWkcAssign.setTenantId(tenantId);
        hmeExcGroupWkcAssign.setEnableFlag("Y");
        List<HmeExcGroupWkcAssign> hmeExcGroupWkcAssigns = hmeExcGroupWkcAssignRepository.select(hmeExcGroupWkcAssign);
        List<String> wkcExceptionGroupIds = hmeExcGroupWkcAssigns.stream().map(HmeExcGroupWkcAssign::getExceptionGroupId).distinct().collect(Collectors.toList());
        hmeExceptionGroupIds.removeAll(wkcExceptionGroupIds);
        hmeExcGroupWkcAssigns.clear();

        List<HmeExceptionRecordQueryVO> labelList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(hmeExceptionGroupIds)) {
            labelList = mapper.queryExceptionLabelListByGroupIds(tenantId, hmeExceptionGroupIds);
        }

        List<HmeExceptionHandlePlatFormVO> exceptionVoList = new ArrayList<>();
        for (MtGenType genType : exceptionTypeList) {
            //每一个类别对应一个返回信息
            HmeExceptionHandlePlatFormVO exceptionVo = new HmeExceptionHandlePlatFormVO();
            exceptionVo.setExceptionType(genType.getTypeCode());
            exceptionVo.setExceptionTypeMeaning(genType.getDescription());
            //设置返回未解决异常数量
            Long unresolvedExcQty = mapper.selectExceptionUnresolvedExcQty(tenantId,genType.getTypeCode());
            exceptionVo.setUnresolvedExcQty(unresolvedExcQty);
            //设置返回本班次异常数量
            exceptionVo.setShiftExcQty(0l);
            //设置返回总异常数量
            exceptionVo.setTotalExcQty(0l);
            //获取当前用户ID
            List<HmeExceptionRecordQueryVO> collect = labelList.stream().filter(vo -> genType.getTypeCode().equals(vo.getExceptionType())).collect(Collectors.toList());
            exceptionVo.setExceptionLabelList(collect);
            // 查询异常列表
            //获取当前用户ID
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            HmeExceptionHandlePlatFormVO condition = new HmeExceptionHandlePlatFormVO();
            condition.setTenantId(tenantId);
            condition.setExceptionType(genType.getTypeCode());
            condition.setUserId(userId);
            List<HmeExceptionRecordQueryVO> exceptionRecordList = mapper.queryExceptionRecordList(condition);
            exceptionVo.setExceptionRecordList(exceptionRecordList);
            //将结果添加到返回集合
            exceptionVoList.add(exceptionVo);
        }

        return exceptionVoList;
    }

    /**
     * <strong>Title : listExceptionNotLoginHistoryForUi</strong><br/>
     * <strong>Description : 异常处理平台-异常清单查询历史-不登陆工位查询-限制当前用户 </strong><br/>
     * <strong>Create on : 2021/3/4 4:27 下午</strong><br/>
     *
     * @param tenantId
     * @param exceptionWkcRecordId
     * @return com.ruike.hme.domain.vo.HmeExceptionRecordQueryVO
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    @Override
    public HmeExceptionRecordQueryVO listExceptionNotLoginHistoryForUi(Long tenantId, String exceptionWkcRecordId) {
        //获取异常记录失败，请检查
        if (StringUtils.isEmpty(exceptionWkcRecordId)) {
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_004", "HME"));
        }
        //先根据异常记录ID查询异常明细信息
        HmeExceptionRecordQueryVO returnVo = mapper.queryExceptionRecordDetailByexceptionWkcRecordId(exceptionWkcRecordId);

        Map<String, String> stringMap = queryArea(tenantId, returnVo.getAttribute2(), returnVo.getAttribute1());
        returnVo.setWorkcellName(stringMap.size() > 0 ? stringMap.get("areaName") : null);

        //根据异常记录ID查询异常状态时间轴列表信息
        List<HmeExceptionStatusHistoryVO> statusHistoryList = mapper.queryStatusHistory(tenantId, exceptionWkcRecordId);
        returnVo.setStatusHistoryList(statusHistoryList);
        return returnVo;
    }

    /**
     * <strong>Title : queryArea</strong><br/>
     * <strong>Description : 数据匹配 区域-AREA、车间-WORKSHOP、产线-PROD_LINE、工位-WKC </strong><br/>
     * <strong>Create on : 2021/3/4 6:32 下午</strong><br/>
     *
     * @param tenantId
     * @param keyId
     * @param type
     * @return java.lang.String
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    private Map<String, String> queryArea(Long tenantId, String keyId, String type) {
        // 查询组织
        Map<String, String> result = new HashMap<>();
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        // 查询区域
        if ("AREA".equals(type)) {
            List<HmeAreaInfo> areaInfos = mapper.selectAreaByUserId(tenantId, userId, keyId);
            if (CollectionUtils.isNotEmpty(areaInfos)) {
                result.put("areaCode", areaInfos.get(0).getAreaCode());
                result.put("areaName", areaInfos.get(0).getAreaName());
                return result;
            }
        } else if ("WORKSHOP".equals(type)) {
            List<HmeAreaInfo> workshopInfos = mapper.selectWorkshopByUserId(tenantId, userId, keyId);
            if (CollectionUtils.isNotEmpty(workshopInfos)) {
                result.put("areaCode", workshopInfos.get(0).getAreaCode());
                result.put("areaName", workshopInfos.get(0).getAreaName());
                return result;
            }
        } else if ("PROD_LINE".equals(type)) {
            List<HmeProdLineInfo> prodLineInfos = mapper.selectProdLineByUserId(tenantId, userId, keyId);
            if (CollectionUtils.isNotEmpty(prodLineInfos)) {
                result.put("areaCode", prodLineInfos.get(0).getProdLineCode());
                result.put("areaName", prodLineInfos.get(0).getProdLineName());
                return result;
            }
        }
        return null;
    }

    /**
     * <strong>Title : queryArea2</strong><br/>
     * <strong>Description : 数据匹配 区域-AREA、车间-WORKSHOP、产线-PROD_LINE、工位-WKC </strong><br/>
     * <strong>Create on : 2021/3/9 14:08 下午</strong><br/>
     *
     * @param tenantId
     * @param keyId
     * @param type
     * @return java.lang.String
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    private Map<String, String> queryArea2(Long tenantId, String keyId, String type) {
        // 查询组织
        Map<String, String> result = new HashMap<>();
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        // 查询区域
        List<HmeAreaInfo> areaInfos = null;
        if ("AREA".equals(type)) {
            areaInfos = mapper.selectAreaByUserId(tenantId, userId, keyId);
        } else if ("WORKSHOP".equals(type)) {
            areaInfos = mapper.selectAreaByWorkShopId(tenantId, keyId);
        } else if ("PROD_LINE".equals(type)) {
            areaInfos = mapper.selectAreaByProdLineId(tenantId, keyId);
        }else if("WKC".equals(type)){
            areaInfos = mapper.selectAreaByWorkcellId(tenantId,keyId);
        }

        if (CollectionUtils.isNotEmpty(areaInfos)) {
            result.put("areaCode", areaInfos.get(0).getAreaCode());
            result.put("areaName", areaInfos.get(0).getAreaName());
            return result;
        }

        return null;
    }

    private List<MtGenType> getGenType(Long tenantId) {
        MtGenTypeVO2 queryType = new MtGenTypeVO2();
        queryType.setTypeGroup("EXCEPTION_TYPE");
        queryType.setModule("HME");
        List<MtGenType> exceptionTypeList = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);
        if (CollectionUtils.isEmpty(exceptionTypeList)) {
            //类型组“ EXCEPTION_TYPE”未维护，请检查
            throw new MtException("HME_EXCEPTION_HANDLE_PLATFORM_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EXCEPTION_HANDLE_PLATFORM_003", "HME"));
        }
        return exceptionTypeList;
    }

}
