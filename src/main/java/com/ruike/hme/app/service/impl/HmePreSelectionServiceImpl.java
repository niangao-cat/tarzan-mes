package com.ruike.hme.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeObjectRecordLockService;
import com.ruike.hme.app.service.HmePreSelectionService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.*;
import com.ruike.hme.domain.vo.HmePreSelectionVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.infra.mapper.ItfCosaCollectIfaceMapper;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.vo.WmsLocatorTransferVO;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.*;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.CollectorsUtil;
import io.tarzan.common.domain.util.MtBaseConstants;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.vo.MtEoVO14;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.*;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * ??????????????????????????????????????????
 *
 * @author wenzhang.yu@hand-china.com 2020-08-18 15:00:33
 */
@Service
@Slf4j
public class HmePreSelectionServiceImpl implements HmePreSelectionService {

    @Autowired
    private HmePreSelectionRepository hmePreSelectionRepository;

    @Autowired
    private HmePreSelectionMapper hmePreSelectionMapper;
    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @Autowired
    private HmeSelectionDetailsRepository hmeSelectionDetailsRepository;

    @Autowired
    private HmeCosRuleLogicRepository hmeCosRuleLogicRepository;

    @Autowired
    private HmeCosRuleTypeMapper hmeCosRuleTypeMapper;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;

    @Autowired
    private HmeSelectionDetailsMapper hmeSelectionDetailsMapper;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private HmeVirtualNumRepository hmeVirtualNumRepository;

    @Autowired
    private HmeVirtualNumMapper hmeVirtualNumMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private ProfileClient profileClient;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private HmeObjectRecordLockService hmeObjectRecordLockService;

    @Autowired
    private HmeObjectRecordLockRepository hmeObjectRecordLockRepository;

    @Autowired
    private HmeCosRuleHeadRepository hmeCosRuleHeadRepository;

    @Autowired
    private MtEventRequestRepository mtEventRequestRepository;

    @Autowired
    private MtUserRepository mtUserRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private WmsObjectTransactionRepository wmsObjectTransactionRepository;

    @Autowired
    private HmeCosFunctionRepository hmeCosFunctionRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private ItfCosaCollectIfaceMapper itfCosaCollectIfaceMapper;

    @Autowired
    private MtCustomDbRepository customDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmePreSelectionReturnDTO>
     * @description ????????????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 19:54
     **/
    @Override
    public Page<HmePreSelectionReturnDTO> workOrderQuery(Long tenantId, HmePreSelectionDTO dto, PageRequest pageRequest) {
        List<LovValueDTO> List = lovAdapter.queryLovValue("HME.COS_PICK_PRODUCTION_LINE", tenantId);
        List<String> collect = List.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        dto.setProdLineCodeList(collect);
        return PageHelper.doPage(pageRequest, () -> hmePreSelectionRepository.workOrderQuery(tenantId, dto));
    }

    /**
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.api.dto.HmePreSelectionReturnDTO2
     * @description ??????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 19:55
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmePreSelectionReturnDTO2 confirm(Long tenantId, HmePreSelectionDTO2 dto) {

        //??????????????????
        HmePreSelectionReturnDTO2 hmePreSelectionReturnDTO2 = new HmePreSelectionReturnDTO2();
        List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, dto.getMaterialLotIdList());
        try {
            for (MtMaterialLot temp :
                    mtMaterialLots) {
                HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
                hmeObjectRecordLockDTO.setFunctionName("?????????");
                hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
                hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
                hmeObjectRecordLockDTO.setObjectRecordId(temp.getMaterialLotId());
                hmeObjectRecordLockDTO.setObjectRecordCode(temp.getMaterialLotCode());
                HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
                //??????
                hmeObjectRecordLockRepository.lock(hmeObjectRecordLock);
            }
            //??????????????????
            MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
            mtNumrange.setObjectCode("PICK_BATCH");
            String preSelectionLot = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange).getNumber();

            //??????????????????
            HmeCosRuleLogic hmeCosRuleLogic = new HmeCosRuleLogic();
            hmeCosRuleLogic.setCosRuleId(dto.getCosRuleId());
            List<HmeCosRuleLogic> hmeCosRuleLogic1 = hmeCosRuleLogicRepository.select(hmeCosRuleLogic);
            //??????????????????????????????
            List<HmeCosRuleLogic> limitRule = hmeCosRuleLogic1.stream().filter(t -> "C".equals(t.getCountType()) || "D".equals(t.getCountType())).collect(Collectors.toList());

            //??????????????????
            List<HmeCosRuleTypeDTO1> hmeCosRuleTypeDTO1 = hmeCosRuleTypeMapper.selectRule(dto.getCosRuleId());

            //?????????????????????????????????
            List<HmePreSelectionDTO4> hmePreSelectionDTO4s = getAllCos(tenantId, dto.getMaterialLotIdList(), limitRule, hmeCosRuleTypeDTO1);
            if (CollectionUtils.isEmpty(hmePreSelectionDTO4s)) {
                throw new MtException("HME_SELECT_003", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_SELECT_003", "HME"));
            }
            HmeCosRuleHead hmeCosRuleHead = hmeCosRuleHeadRepository.selectByPrimaryKey(dto.getCosRuleId());
            //????????????????????????
            HmePreSelection hmePreSelection = new HmePreSelection();
            hmePreSelection.setTenantId(tenantId);
            hmePreSelection.setSiteId(dto.getSiteId());
            hmePreSelection.setPreSelectionLot(preSelectionLot);
            hmePreSelection.setStatus("NEW");
            if (dto.getIsBind().equals('Y')) {
                hmePreSelection.setWorkOrderId(dto.getWorkOrderId());
            }
            hmePreSelection.setAttribute1(hmeCosRuleHead.getCosRuleCode());
            hmePreSelection.setAttribute2(hmeCosRuleHead.getProductType());
            hmePreSelection.setAttribute3(hmeCosRuleHead.getMaterialId());
            hmePreSelectionRepository.insertSelective(hmePreSelection);

            //??????????????????????????????
            String s = PreSelection(tenantId, hmePreSelection, hmeCosRuleLogic1, hmeCosRuleTypeDTO1, hmePreSelectionDTO4s, dto);
            if (Long.valueOf(s) > 0) {
                hmePreSelection.setSetsNum(Long.valueOf(s));
                hmePreSelectionMapper.updateByPrimaryKeySelective(hmePreSelection);
                hmePreSelectionReturnDTO2.setNum(s);
                hmePreSelectionReturnDTO2.setPreSelectionLot(preSelectionLot);
            } else {
                hmePreSelectionReturnDTO2.setNum(s);
                hmePreSelectionMapper.deleteByPrimaryKey(hmePreSelection.getPreSelectionId());
            }
        } catch (Exception e) {
            throw new CommonException(e);
        } finally {
            for (MtMaterialLot temp :
                    mtMaterialLots) {
                HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
                hmeObjectRecordLockDTO.setFunctionName("?????????");
                hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
                hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
                hmeObjectRecordLockDTO.setObjectRecordId(temp.getMaterialLotId());
                hmeObjectRecordLockDTO.setObjectRecordCode(temp.getMaterialLotCode());
                HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
                //??????
                hmeObjectRecordLockRepository.releaseLock(hmeObjectRecordLock, YES);
            }
        }
        return hmePreSelectionReturnDTO2;

    }

    /**
     * @param tenantId
     * @param materialLotIdList
     * @param limitRule
     * @param hmeCosRuleTypeDTO1
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionDTO4>
     * @description ??????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/10/9 13:55
     */
    private List<HmePreSelectionDTO4> getAllCos(Long tenantId, List<String> materialLotIdList, List<HmeCosRuleLogic> limitRule, List<HmeCosRuleTypeDTO1> hmeCosRuleTypeDTO1) {
        //?????????????????????
        List<HmePreSelectionDTO4> hmePreSelectionDTO4s = hmePreSelectionMapper.queryMaterialLot(tenantId, materialLotIdList, hmeCosRuleTypeDTO1.get(0), limitRule);
        return hmePreSelectionDTO4s;
    }

    /**
     * @param tenantId
     * @param selectLot
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO3>
     * @description ??????????????????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 19:56
     **/
    @Override
    @ProcessLovValue
    public List<HmePreSelectionReturnDTO3> selectLot(Long tenantId, String selectLot) {
        return hmePreSelectionMapper.selectLot(tenantId, selectLot);
    }

    /**
     * @param tenantId
     * @param materialLotCode
     * @param selectLot
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO4>
     * @description ???????????????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 20:00
     **/
    @Override
    public List<HmePreSelectionReturnDTO4> materialLot(Long tenantId, String materialLotCode, String selectLot) {
        Integer a = hmePreSelectionMapper.selectmaterialLot(tenantId, materialLotCode, selectLot);
        if (a == 0) {
            throw new MtException("HME_SELECT_002", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_SELECT_002", "HME"));
        }
        List<HmePreSelectionReturnDTO4> hmePreSelectionReturnDTO4s = hmePreSelectionMapper.materialLot(tenantId, materialLotCode);

        //????????????????????????????????? modify by yuchao.wang for zhenyong.ban at 2020.10.13
        return getReturnDto(hmePreSelectionReturnDTO4s);
    }

    /**
     * @param dtoList ?????????
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO4>
     * @Description ?????????????????????????????????
     * @author yuchao.wang
     * @date 2020/10/13 22:25
     */
    private List<HmePreSelectionReturnDTO4> getReturnDto(List<HmePreSelectionReturnDTO4> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return new ArrayList<HmePreSelectionReturnDTO4>();
        }

        //?????????????????????????????????
        for (HmePreSelectionReturnDTO4 dto4 : dtoList) {
            //??????????????????????????????
            if (StringUtils.isNotBlank(dto4.getOldLoad())) {
                String[] split = dto4.getOldLoad().split(",");
                if (split.length != 2) {
                    continue;
                }
                dto4.setDisplayOldLoad((char) (64 + Integer.parseInt(split[0])) + "," + split[1]);
            }

            //??????????????????????????????
            if (StringUtils.isNotBlank(dto4.getNewLoad())) {
                String[] split = dto4.getNewLoad().split(",");
                if (split.length != 2) {
                    continue;
                }
                dto4.setDisplayNewLoad((char) (64 + Integer.parseInt(split[0])) + "," + split[1]);
            }
        }
        return dtoList;
    }

    /**
     * @param tenantId
     * @param materialLotCode
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO4>
     * @description ???????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 20:01
     **/
    @Override
    public List<HmePreSelectionReturnDTO4> tomaterialLot(Long tenantId, String materialLotCode) {
        //????????????
        if (StringUtils.isEmpty(materialLotCode)) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????"));
        }

        //????????????????????????
        List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.materialLotByCodeBatchGet(tenantId, Collections.singletonList(materialLotCode));
        if (CollectionUtils.isEmpty(mtMaterialLots) || Objects.isNull(mtMaterialLots.get(0)) || StringUtils.isBlank(mtMaterialLots.get(0).getMaterialLotId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        List<HmePreSelectionReturnDTO4> hmePreSelectionReturnDTO4s = new ArrayList<>();
        List<HmePreSelectionReturnDTO4> hmePreSelectionReturnDTO4s1 = hmePreSelectionMapper.tomaterialLot(tenantId, mtMaterialLots.get(0).getMaterialLotId());
        if (CollectionUtils.isNotEmpty(hmePreSelectionReturnDTO4s1)) {
            List<String> virtualNums = hmePreSelectionReturnDTO4s1.stream().map(HmePreSelectionReturnDTO4::getVirtualNum).distinct().collect(Collectors.toList());
            int a = 1;
            int b = 1;
            for (String temp :
                    virtualNums) {
                List<HmePreSelectionReturnDTO4> hmePreSelectionReturnDTO4s2 = hmePreSelectionMapper.getOrderBy(temp);
                for (int i = 0; i < hmePreSelectionReturnDTO4s2.size(); i++) {
                    int c = i / 8;
                    int d = i % 8;
                    HmePreSelectionReturnDTO4 hmePreSelectionReturnDTO4 = hmePreSelectionReturnDTO4s2.get(i);
                    hmePreSelectionReturnDTO4.setNewLoad((a + c) + "," + (d + b));
                    hmePreSelectionReturnDTO4.setVirtualNum(temp);
                    if (hmePreSelectionReturnDTO4s1.stream().filter(t -> t.getOldMaterialLotId().equals(hmePreSelectionReturnDTO4.getOldMaterialLotId()) && t.getOldLoad().equals(hmePreSelectionReturnDTO4.getOldLoad())).count() > 0) {
                        hmePreSelectionReturnDTO4.setIsTrue("Y");
                    }
                    hmePreSelectionReturnDTO4s.add(hmePreSelectionReturnDTO4);
                }
                if (hmePreSelectionReturnDTO4s2.size() % 8 > 0) {
                    a = hmePreSelectionReturnDTO4s2.size() / 8 + a + 1;
                } else {
                    a = hmePreSelectionReturnDTO4s2.size() / 8 + a;

                }

            }
        }

        //????????????????????????????????? modify by yuchao.wang for zhenyong.ban at 2020.10.13
        return getReturnDto(hmePreSelectionReturnDTO4s);
    }

    /**
     * @param tenantId
     * @param selectLot
     * @return java.util.List<com.ruike.hme.domain.entity.HmeSelectionDetails>
     * @description ??????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 20:04
     **/
    @Override
    public List<HmeSelectionDetails> selectDetails(Long tenantId, String selectLot) {
        List<HmeSelectionDetails> result = new ArrayList<>();
        HmePreSelection hmePreSelection = new HmePreSelection();
        hmePreSelection.setPreSelectionLot(selectLot);
        HmePreSelection hmePreSelection1 = hmePreSelectionRepository.selectOne(hmePreSelection);
        if (ObjectUtil.isEmpty(hmePreSelection1)) {
            return result;
        }
        HmeSelectionDetails hmeSelectionDetail = new HmeSelectionDetails();
        hmeSelectionDetail.setPreSelectionId(hmePreSelection1.getPreSelectionId());

        result = hmeSelectionDetailsRepository.select(hmeSelectionDetail);

        //????????????????????????????????? add by yuchao.wang for zhenyong.ban at 2020.10.13
        if (CollectionUtils.isNotEmpty(result)) {
            for (HmeSelectionDetails details : result) {
                //??????????????????????????????
                if (StringUtils.isNotBlank(details.getOldLoad())) {
                    String[] split = details.getOldLoad().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    details.setOldLoad((char) (64 + Integer.parseInt(split[0])) + "," + split[1]);
                }

                //??????????????????????????????
                if (StringUtils.isNotBlank(details.getNewLoad())) {
                    String[] split = details.getNewLoad().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    details.setNewLoad((char) (64 + Integer.parseInt(split[0])) + "," + split[1]);
                }
            }
        }
        return result;
    }

    /**
     * @param tenantId
     * @param dtos
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO4>
     * @description ???????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/8/27 20:02
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmePreSelectionReturnDTO4> load(Long tenantId, List<HmePreSelectionDTO3> dtos) {

        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("COS_CHIP_SELECT_IN");
        // ????????????
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        MtEventCreateVO eventCreateVO1 = new MtEventCreateVO();
        eventCreateVO1.setEventTypeCode("COS_CHIP_SELECT_OUT");
        // ????????????
        String eventId1 = mtEventRepository.eventCreate(tenantId, eventCreateVO1);

        //????????????????????????
        for (HmePreSelectionDTO3 dto :
                dtos) {
            HmeSelectionDetails hmeSelectionDetails = hmeSelectionDetailsRepository.selectByPrimaryKey(dto.getSelectionDetailsId());
            if (!ObjectUtils.isEmpty(hmeSelectionDetails.getNewMaterialLotId())) {
                throw new MtException("HME_COS_009", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_COS_009", "HME"));
            }

        }
        List<HmePreSelectionReturnDTO4> hmePreSelectionReturnDTO4sReryrn = new ArrayList<>();
        //????????????????????????
        MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
        materialLotVo3.setMaterialLotCode(dtos.get(0).getNewMaterialLotCode());
        List<String> materialLotIds =
                mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotIds.get(0));
        MtMaterialLot oldmtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, dtos.get(0).getOldMaterialLotId());

        if (YES.equals(mtMaterialLot.getEnableFlag())) {
            if (!oldmtMaterialLot.getMaterialId().equals(mtMaterialLot.getMaterialId())) {
                throw new MtException("HME_CHIP_TRANSFER_023", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_CHIP_TRANSFER_023", "HME"));
            }
            if (!oldmtMaterialLot.getLot().equals(mtMaterialLot.getLot())) {
                throw new MtException("HME_COS_004", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_COS_004", "HME"));
            }
        } else {
            //???????????? ??????lot
            List<MtExtendSettings> mtExtendSettingss = new ArrayList<>();
            MtExtendSettings mtExtendSettings = new MtExtendSettings();
            mtExtendSettings.setAttrName("CONTAINER_TYPE");
            mtExtendSettingss.add(mtExtendSettings);
            MtExtendSettings mtExtendSettings1 = new MtExtendSettings();
            mtExtendSettings1.setAttrName("LOCATION_ROW");
            mtExtendSettingss.add(mtExtendSettings1);
            MtExtendSettings mtExtendSettings2 = new MtExtendSettings();
            mtExtendSettings2.setAttrName("LOCATION_COLUMN");
            mtExtendSettingss.add(mtExtendSettings2);
            MtExtendSettings mtExtendSettings3 = new MtExtendSettings();
            mtExtendSettings3.setAttrName("CHIP_NUM");
            mtExtendSettingss.add(mtExtendSettings3);
            MtExtendSettings mtExtendSettings4 = new MtExtendSettings();
            mtExtendSettings4.setAttrName("MF_FLAG");
            mtExtendSettingss.add(mtExtendSettings4);
            MtExtendSettings mtExtendSettings5 = new MtExtendSettings();
            mtExtendSettings5.setAttrName("STATUS");
            mtExtendSettingss.add(mtExtendSettings5);
            List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                    "mt_material_lot_attr", "MATERIAL_LOT_ID", oldmtMaterialLot.getMaterialLotId(), mtExtendSettingss);
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                mtExtendAttrVOList.forEach(item -> {
                    List<String> name = mtExtendSettingss.stream().map(MtExtendSettings::getAttrName).collect(Collectors.toList());
                    if (name.contains(item.getAttrName())) {
                        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                        mtExtendVO5.setAttrName(item.getAttrName());
                        mtExtendVO5.setAttrValue(item.getAttrValue());
                        mtExtendVO5List.add(mtExtendVO5);
                    }
                });
            }


            MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
            mtExtendVO10.setKeyId(mtMaterialLot.getMaterialLotId());
            mtExtendVO10.setEventId(eventId);
            mtExtendVO10.setAttrs(mtExtendVO5List);
            mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);
        }


        //?????????????????????
        List<HmeVirtualNum> hmeVirtualNums = hmeVirtualNumRepository.selectByCondition(Condition.builder(HmeVirtualNum.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmeVirtualNum.FIELD_VIRTUAL_NUM, dtos.get(0).getVirtualNum()))
                .build());
        if (CollectionUtils.isNotEmpty(hmeVirtualNums)) {
            if (StringUtils.isEmpty(hmeVirtualNums.get(0).getMaterialLotId())) {
                HmeVirtualNum hmeVirtualNum = hmeVirtualNums.get(0);
                hmeVirtualNum.setMaterialLotId(materialLotIds.get(0));
                if (StringUtils.isNotBlank(mtMaterialLot.getLocatorId())) {
                    hmeVirtualNum.setLocatorId(mtMaterialLot.getLocatorId());
                    MtModLocator mtModLocator = hmePreSelectionMapper.getWarehouse(tenantId, mtMaterialLot.getLocatorId());
                    if (!ObjectUtils.isEmpty(mtModLocator)) {
                        hmeVirtualNum.setWarehouseId(mtModLocator.getLocatorId());
                    }
                }
                hmeVirtualNumMapper.updateByPrimaryKeySelective(hmeVirtualNum);
            } else {
                if (!hmeVirtualNums.get(0).getMaterialLotId().equals(materialLotIds.get(0))) {
                    throw new MtException("HME_SELECT_001", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_SELECT_001", "HME", hmeVirtualNums.get(0).getVirtualNum()));
                }

            }
        }

        //??????????????????????????????
        List<HmePreSelectionReturnDTO4> hmePreSelectionReturnDTO4s = hmePreSelectionMapper.getOrderBy(dtos.get(0).getVirtualNum());

        //???????????????????????????
        HmeSelectionDetails hmeSelectionDetails = new HmeSelectionDetails();
        hmeSelectionDetails.setNewMaterialLotId(materialLotIds.get(0));
        List<HmeSelectionDetails> select2 = hmeSelectionDetailsRepository.select(hmeSelectionDetails);
        List<HmeSelectionDetails> collect = select2.stream().filter(t -> t.getVirtualNum().equals(dtos.get(0).getVirtualNum())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            int a = 0;
            //??????????????????
            for (int i = 0; i < hmePreSelectionReturnDTO4s.size(); i++) {
                if (hmePreSelectionReturnDTO4s.get(i).getOldLoad().equals(collect.get(0).getOldLoad()) && hmePreSelectionReturnDTO4s.get(i).getOldMaterialLotId().equals(collect.get(0).getOldMaterialLotId())) {
                    a = i;
                    break;
                }
            }
            int b = a / 8;
            int c = Integer.valueOf(collect.get(0).getNewLoad().substring(0, 1)) - b;

            for (HmePreSelectionDTO3 dto :
                    dtos) {
                int num = 0;
                for (int i = 0; i < hmePreSelectionReturnDTO4s.size(); i++) {
                    if (hmePreSelectionReturnDTO4s.get(i).getOldLoad().equals(dto.getOldLoad()) && hmePreSelectionReturnDTO4s.get(i).getOldMaterialLotId().equals(dto.getOldMaterialLotId())) {
                        num = i;
                        break;
                    }
                }
                int num1 = num / 8 + c;//?????????
                int num2 = num % 8 + 1;//?????????
                //???????????????
                HmeSelectionDetails hmeSelectionDetails1 = new HmeSelectionDetails();
                hmeSelectionDetails1.setSelectionDetailsId(dto.getSelectionDetailsId());
                hmeSelectionDetails1.setNewMaterialLotId(materialLotIds.get(0));
                hmeSelectionDetails1.setNewLoad(num1 + "," + num2);
                hmeSelectionDetailsMapper.updateByPrimaryKeySelective(hmeSelectionDetails1);
                //???????????????
                HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                hmeMaterialLotLoad.setLoadSequence(dto.getLoadSequence());
                HmeMaterialLotLoad select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad).get(0);
                select.setSourceLoadColumn(select.getLoadColumn());
                select.setSourceLoadRow(select.getLoadRow());
                select.setSourceMaterialLotId(select.getMaterialLotId());
                select.setMaterialLotId(materialLotIds.get(0));
                select.setLoadRow((long) num1);
                select.setLoadColumn((long) num2);
                hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(select);
                HmePreSelectionReturnDTO4 hmePreSelectionReturnDTO4 = new HmePreSelectionReturnDTO4();
                hmePreSelectionReturnDTO4.setOldLoad(dto.getOldLoad());


                hmePreSelectionReturnDTO4.setOldMaterialLotId(dto.getOldMaterialLotId());
                hmePreSelectionReturnDTO4.setIsTrue("Y");
                hmePreSelectionReturnDTO4.setNewLoad(num1 + "," + num2);
                hmePreSelectionReturnDTO4sReryrn.add(hmePreSelectionReturnDTO4);
            }

        } else {
            //???????????????
            List<String> collect1 = select2.stream().map(t -> t.getVirtualNum()).distinct().collect(Collectors.toList());
            int a = 1;
            int b = 1;
            for (String virtualNum :
                    collect1) {
                int size = hmePreSelectionMapper.getOrderBy(virtualNum).size();
                if (size % 8 > 0) {
                    a = a + 1 + size / 8;
                } else {
                    a = a + size / 8;
                }
            }
            for (int i = 0; i < hmePreSelectionReturnDTO4s.size(); i++) {
                int c = i / 8;
                int d = i % 8;
                HmePreSelectionReturnDTO4 hmePreSelectionReturnDTO4 = hmePreSelectionReturnDTO4s.get(i);
                hmePreSelectionReturnDTO4.setNewLoad((a + c) + "," + (d + b));
                for (HmePreSelectionDTO3 dto :
                        dtos) {
                    if (hmePreSelectionReturnDTO4s.get(i).getOldLoad().equals(dto.getOldLoad()) && hmePreSelectionReturnDTO4s.get(i).getOldMaterialLotId().equals(dto.getOldMaterialLotId())) {
                        HmeSelectionDetails hmeSelectionDetails1 = new HmeSelectionDetails();
                        hmeSelectionDetails1.setSelectionDetailsId(dto.getSelectionDetailsId());
                        hmeSelectionDetails1.setNewMaterialLotId(materialLotIds.get(0));
                        hmeSelectionDetails1.setNewLoad((a + c) + "," + (d + b));
                        hmeSelectionDetailsMapper.updateByPrimaryKeySelective(hmeSelectionDetails1);
                        //???????????????
                        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                        hmeMaterialLotLoad.setLoadSequence(dto.getLoadSequence());
                        HmeMaterialLotLoad select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad).get(0);
                        select.setSourceLoadColumn(select.getLoadColumn());
                        select.setSourceLoadRow(select.getLoadRow());
                        select.setSourceMaterialLotId(select.getMaterialLotId());
                        select.setMaterialLotId(materialLotIds.get(0));
                        select.setLoadRow((long) (a + c));
                        select.setLoadColumn((long) (b + d));
                        hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(select);

                        hmePreSelectionReturnDTO4.setIsTrue("Y");
                    }
                }
                hmePreSelectionReturnDTO4sReryrn.add(hmePreSelectionReturnDTO4);
            }
        }
        //??????????????????
        if (NO.equals(mtMaterialLot.getEnableFlag())) {
            String lot = profileClient.getProfileValueByOptions("HME_COS_MATERIAL_LOT_LOT");
            if (StringUtils.isEmpty(lot)) {
                throw new MtException("HME_COS_006", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_COS_006", "HME"));
            }
            MtMaterialLotVO2 mtLotUpdate = new MtMaterialLotVO2();
            mtLotUpdate.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtLotUpdate.setPrimaryUomQty((double) dtos.size());
            mtLotUpdate.setMaterialId(oldmtMaterialLot.getMaterialId());
            mtLotUpdate.setEnableFlag("Y");
            mtLotUpdate.setLot(lot);
            mtLotUpdate.setQualityStatus("OK");
            mtLotUpdate.setLocatorId(oldmtMaterialLot.getLocatorId());
            mtLotUpdate.setSupplierId(oldmtMaterialLot.getSupplierId());
            mtLotUpdate.setSupplierSiteId(oldmtMaterialLot.getSupplierSiteId());
            mtLotUpdate.setEventId(eventId);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotUpdate, HmeConstants.ConstantValue.NO);
        } else {
            MtMaterialLotVO2 mtLotUpdate = new MtMaterialLotVO2();
            mtLotUpdate.setMaterialLotId(mtMaterialLot.getMaterialLotId());
            mtLotUpdate.setPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() + (double) dtos.size());
            mtLotUpdate.setEventId(eventId);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotUpdate, HmeConstants.ConstantValue.NO);
        }
        //????????????????????????
        MtMaterialLotVO2 mtLotUpdate1 = new MtMaterialLotVO2();
        mtLotUpdate1.setMaterialLotId(oldmtMaterialLot.getMaterialLotId());
        mtLotUpdate1.setPrimaryUomQty(oldmtMaterialLot.getPrimaryUomQty() - (double) dtos.size());
        if (new BigDecimal(mtLotUpdate1.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) == 0) {
            mtLotUpdate1.setEnableFlag("N");
        }
        mtLotUpdate1.setMaterialId(oldmtMaterialLot.getMaterialId());
        mtLotUpdate1.setEventId(eventId1);
        mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotUpdate1, HmeConstants.ConstantValue.NO);

        //????????????????????????????????? modify by yuchao.wang for zhenyong.ban at 2020.10.13
        return getReturnDto(hmePreSelectionReturnDTO4sReryrn);
    }

    @Override
    @ProcessLovValue
    public List<HmeCosRuleLogicDTO> selectRule(Long tenantId, String ruleId) {
        return hmePreSelectionMapper.selectRule(tenantId, ruleId);
    }

    @Override
    public List<HmePreSelectionReturnDTO5> getMateriallot(Long tenantId, String locatorCode) {

        return hmePreSelectionMapper.getMateriallot(tenantId, locatorCode);
    }

    /**
     * @param tenantId
     * @param materialLotCode
     * @return com.ruike.hme.api.dto.HmePreSelectionReturnDTO5
     * @description ??????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/2 14:29
     **/
    @Override
    public HmePreSelectionReturnDTO5 materialLotQuery(Long tenantId, String materialLotCode) {
        //??????????????????
        if(StringUtils.isBlank(materialLotCode)){
            throw new MtException("HME_SELECT_013", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "HME_SELECT_013", HmeConstants.ConstantValue.HME));
        }
        //??????
        MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
        materialLotVo3.setMaterialLotCode(materialLotCode);
        List<String> materialLotIds =
                mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
        // ???????????????
        if (CollectionUtils.isEmpty(materialLotIds)) {
            throw new MtException("MT_MATERIAL_TFR_0002", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "MT_MATERIAL_TFR_0002", HmeConstants.ConstantValue.HME, materialLotCode));
        }
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotIds.get(0));
        // ???????????????
        if (!YES.equals(mtMaterialLot.getEnableFlag())) {
            throw new MtException("HME_COS_022", mtErrorMessageRepository.getErrorMessageWithModule(
                    tenantId, "HME_COS_022", HmeConstants.ConstantValue.HME, materialLotCode));
        }
        //???????????????
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("MF_FLAG");
        List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && YES.equals(mtExtendAttrVOS.get(0).getAttrValue())) {
            throw new MtException("HME_NC_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0029", "HME"));
        }
        HmePreSelectionReturnDTO5 hmePreSelectionReturnDTO5 = hmePreSelectionMapper.materialLotQuery(tenantId, materialLotCode);
        // 20211115 modify by sanfeng.zhang ???????????? wafer????????? ???????????????????????????
        if (hmePreSelectionReturnDTO5 != null && StringUtils.isNotBlank(hmePreSelectionReturnDTO5.getWafer())) {
            //2021-09-22 10:34 edit by chaonan.hu for peng.zhao ??????????????????????????????
            HmePreSelectionDTO10 hmePreSelectionDTO10 = hmePreSelectionMapper.cosTestMonitorHeaderQueryByCosTypeWafer(tenantId, hmePreSelectionReturnDTO5.getCosType(), hmePreSelectionReturnDTO5.getWafer());
            if(Objects.nonNull(hmePreSelectionDTO10)){
                if("UNKNOWN".equals(hmePreSelectionDTO10.getDocStatus())){
                    //????????????????????????????????????UNKNOWN????????????WAFER???${1}????????????????????????????????????????????????
                    throw new MtException("HME_SELECT_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SELECT_012", "HME", hmePreSelectionReturnDTO5.getWafer()));
                }
                if("NG".equals(hmePreSelectionDTO10.getDocStatus()) || "WAIT".equals(hmePreSelectionDTO10.getDocStatus())
                        || "NO_PASS".equals(hmePreSelectionDTO10.getDocStatus())){
                    //????????????????????????????????????NG???WAIT???NO_PASS????????????COS??????????????????????????????????????????ID?????????????????????
                    String materialLotStatus = hmePreSelectionMapper.getCosTestMonitorLineMaterialLotStatus(tenantId, hmePreSelectionDTO10.getCosMonitorHeaderId(), hmePreSelectionReturnDTO5.getMaterialLotId());
                    if(StringUtils.isBlank(materialLotStatus) || !OK.equals(materialLotStatus)){
                        //????????????????????????????????????????????????OK????????????WAFER???${1}?????????????????????????????????????????????
                        throw new MtException("HME_SELECT_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_SELECT_009", "HME", hmePreSelectionReturnDTO5.getWafer()));
                    }
                }
            }else{
                //?????????wafer??????hme_cos_test_select_cancle?????????????????????WAFER???${1}????????????????????????
                Long testSelectCancelCount = hmePreSelectionMapper.testSelectCancelCountQueryByWafer(tenantId, hmePreSelectionReturnDTO5.getWafer());
                if(testSelectCancelCount == 0){
                    throw new MtException("HME_SELECT_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_SELECT_014", "HME", hmePreSelectionReturnDTO5.getWafer()));
                }
            }
        }

        Long cosNum = hmePreSelectionMapper.selectCosNumByMaterialLotCode(tenantId, materialLotCode);
        hmePreSelectionReturnDTO5.setCosNum(cosNum);

        //?????????????????????
        BigDecimal primaryUomQty = StringUtils.isNotBlank(hmePreSelectionReturnDTO5.getPrimaryUomQty()) ? BigDecimal.valueOf(Double.valueOf(hmePreSelectionReturnDTO5.getPrimaryUomQty())) : BigDecimal.ZERO;
        BigDecimal notPreparaNum = primaryUomQty.subtract(BigDecimal.valueOf(hmePreSelectionReturnDTO5.getCosNum()));
        hmePreSelectionReturnDTO5.setNotPreparaNum(notPreparaNum.longValue());

        return hmePreSelectionReturnDTO5;
    }

    @Override
    public List<HmePreSelectionReturnDTO5> materialLotReQuery(Long tenantId, List<String> materialLotCodeList) {
        List<HmePreSelectionReturnDTO5> hmePreSelectionReturnDTO5List = new ArrayList<>();
        if(CollectionUtils.isEmpty(materialLotCodeList)) {
            return hmePreSelectionReturnDTO5List;
        }
        hmePreSelectionReturnDTO5List = hmePreSelectionMapper.materialLotBatchQuery(tenantId, materialLotCodeList);
        List<HmePreSelectionReturnDTO5> cosNumList = hmePreSelectionMapper.selectBatchCosNumByMaterialLotCode(tenantId,materialLotCodeList);
        Map<String,HmePreSelectionReturnDTO5> cosNumMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(cosNumList)){
            cosNumMap = cosNumList.stream().collect(Collectors.toMap(HmePreSelectionReturnDTO5::getMaterialLotId,c->c));
        }
        //?????????????????????
        Map<String, HmePreSelectionReturnDTO5> finalCosNumMap = cosNumMap;
        hmePreSelectionReturnDTO5List.forEach(e->{
            HmePreSelectionReturnDTO5 dto5 = finalCosNumMap.get(e.getMaterialLotId());
            e.setCosNum(dto5 == null ? 0L : dto5.getCosNum());

            BigDecimal primaryUomQty = StringUtils.isNotBlank(e.getPrimaryUomQty()) ? BigDecimal.valueOf(Double.valueOf(e.getPrimaryUomQty())) : BigDecimal.ZERO;
            BigDecimal notPreparaNum = primaryUomQty.subtract(BigDecimal.valueOf(e.getCosNum()));
            e.setNotPreparaNum(notPreparaNum.longValue());
        });
        return hmePreSelectionReturnDTO5List;
    }

    /**
     * @param tenantId
     * @param selectLot
     * @param pageRequest
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO3>
     * @description ??????????????????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/2 15:06
     */
    @Override
    @ProcessLovValue
    public Page<HmePreSelectionReturnDTO3> selectLotQuery(Long tenantId, String selectLot, PageRequest pageRequest) {
        Page<HmePreSelectionReturnDTO3> hmePreSelectionReturnDTO3s = PageHelper.doPage(pageRequest, () -> hmePreSelectionMapper.selectLotQuery(tenantId, selectLot));
        if (CollectionUtils.isNotEmpty(hmePreSelectionReturnDTO3s)) {
            //int i = 1;
            long count = hmePreSelectionReturnDTO3s.stream().filter(t -> t.getVirtualNum().equals(hmePreSelectionReturnDTO3s.get(0).getVirtualNum())).count();
            //long j = count;
            //String virtualNum = hmePreSelectionReturnDTO3s.get(0).getVirtualNum();

            //?????????????????????????????????????????????????????????????????????
            List<HmePreSelectionReturnDTO10> hmePreSelectionReturnDTO10List = hmePreSelectionMapper.selectCosRuleLogicBySelectLot(tenantId, selectLot);
            for (HmePreSelectionReturnDTO10 hmePreSelectionReturnDTO10:hmePreSelectionReturnDTO10List) {
                String collectionItemDesc = lovAdapter.queryLovMeaning("HME.COS_FUNCTION", tenantId, hmePreSelectionReturnDTO10.getCollectionItem());
                hmePreSelectionReturnDTO10.setCollectionItemDesc(collectionItemDesc);
            }
            List<String> loadSequenceList = hmePreSelectionReturnDTO3s.stream().map(HmePreSelectionReturnDTO3::getLoadSequence).distinct().collect(Collectors.toList());
            Map<String, List<HmeCosFunction>> hmeCosFunctionMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(loadSequenceList)) {
                List<HmeCosFunction> hmeCosFunctionList = hmeCosFunctionRepository.selectByCondition(Condition.builder(HmeCosFunction.class).andWhere(Sqls.custom()
                        .andEqualTo(HmeCosFunction.FIELD_TENANT_ID, tenantId)
                        .andIn(HmeCosFunction.FIELD_LOAD_SEQUENCE, loadSequenceList)).build());
                if (CollectionUtils.isNotEmpty(hmeCosFunctionList)) {
                    hmeCosFunctionMap = hmeCosFunctionList.stream().collect(Collectors.groupingBy(cf -> cf.getLoadSequence() + "_" + cf.getCurrent()));
                }
            }

            for (HmePreSelectionReturnDTO3 temp : hmePreSelectionReturnDTO3s) {
                if (StringUtils.isNotBlank(temp.getOldLoad())) {
                    String[] split = temp.getOldLoad().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    temp.setOldLoad((char) (64 + Integer.parseInt(split[0])) + split[1]);
                }
                if (StringUtils.isNotBlank(temp.getNewLoad())) {
                    String[] split = temp.getNewLoad().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    temp.setNewLoad((char) (64 + Integer.parseInt(split[0])) + split[1]);
                }
                //if (virtualNum.equals(temp.getVirtualNum())) {
                //    temp.setWays("COS" + String.format("%02d", j));
                //    temp.setDeviceNumber(String.valueOf(i));
                //    j--;
                //} else {
                //    j = count;
                //    i++;
                //    virtualNum = temp.getVirtualNum();
                //    temp.setWays("COS" + String.format("%02d", j));
                //    temp.setDeviceNumber(String.valueOf(i));
                //    j--;
                //}

                //???????????????????????????
                List<HmePreSelectionReturnDTO9> functionList = new ArrayList<>();
                for (HmePreSelectionReturnDTO10 hmePreSelectionReturnDTO10:hmePreSelectionReturnDTO10List) {
                    HmePreSelectionReturnDTO9 function = new HmePreSelectionReturnDTO9();
                    function.setTitle(hmePreSelectionReturnDTO10.getCollectionItemDesc());
                    String mapKey = temp.getLoadSequence() + "_" + hmePreSelectionReturnDTO10.getCurrent();
                    List<HmeCosFunction> hmeCosFunctionList = hmeCosFunctionMap.get(mapKey);
                    if(CollectionUtils.isNotEmpty(hmeCosFunctionList)){
                        String functionValue = getFunction(tenantId, hmeCosFunctionList.get(0), hmePreSelectionReturnDTO10.getCollectionItem());
                        function.setFunction(functionValue);
                    }else{
                        function.setFunction(null);
                    }
                    functionList.add(function);
                }
                temp.setFunctionList(functionList);
            }
        }
        return hmePreSelectionReturnDTO3s;

    }

    /**
     * @param tenantId
     * @param dtos
     * @return void
     * @description ??????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/2 15:42
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void loadNew(Long tenantId, List<HmePreSelectionDTO6> dtos) {

        //????????????????????????
        for (HmePreSelectionDTO6 dto :
                dtos) {
            HmeSelectionDetails hmeSelectionDetails = hmeSelectionDetailsRepository.selectByPrimaryKey(dto.getSelectionDetailsId());
            if (!ObjectUtils.isEmpty(hmeSelectionDetails.getNewMaterialLotId())) {
                throw new MtException("HME_COS_009", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_COS_009", "HME"));
            }
        }
        HmePreSelection hmePreSelection = hmePreSelectionMapper.selectbyDetails(dtos.get(0).getSelectionDetailsId());
        List<HmeCosRuleHead> hmeCosRuleHeads = hmeCosRuleHeadRepository.selectByCondition(Condition.builder(HmeCosRuleHead.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmeCosRuleHead.FIELD_COS_RULE_CODE, hmePreSelection.getAttribute1()))
                .build());
        if (dtos.size() % hmeCosRuleHeads.get(0).getCosNum() > 0) {
            throw new MtException("HME_COS_025 ", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_025 ", "HME"));
        }
        List<HmePreSelectionReturnDTO4> hmePreSelectionReturnDTO4sReryrn = new ArrayList<>();
        //???????????????????????????
        MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
        materialLotVo3.setMaterialLotCode(dtos.get(0).getNewMaterialLotCode());
        List<String> newMaterialLotIds =
                mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
        if (CollectionUtils.isEmpty(newMaterialLotIds)) {
            throw new MtException("HME_COS_022", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_022", "HME", dtos.get(0).getNewMaterialLotCode()));
        }
        MtMaterialLot newMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, newMaterialLotIds.get(0));
        //???????????????
        if (!ObjectUtils.isEmpty(newMaterialLot)) {
            if (HmeConstants.ConstantValue.YES.equals(newMaterialLot.getEnableFlag())) {
                //???????????????
                MtMaterialLotAttrVO2 mtMaterialLotAttrVO21 = new MtMaterialLotAttrVO2();
                mtMaterialLotAttrVO21.setMaterialLotId(newMaterialLotIds.get(0));
                mtMaterialLotAttrVO21.setAttrName("MF_FLAG");
                List<MtExtendAttrVO> mtExtendAttrVOS1 = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO21);
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOS1) && YES.equals(mtExtendAttrVOS1.get(0).getAttrValue())) {
                    throw new MtException("HME_NC_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0029", "HME"));
                }
            }
        } else {
            throw new MtException("HME_COS_022", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_022", "HME", dtos.get(0).getNewMaterialLotCode()));
        }
        //2021-07-06 15:03 add by chaonan.hu for peng.zhao ????????????????????????????????????????????????Y?????????
        String usedFlagAttr = hmePreSelectionMapper.getSingleAttrValueByMaterialLotId(tenantId, newMaterialLot.getMaterialLotId(), "USED_FLAG");
        if(YES.equals(usedFlagAttr)){
            throw new MtException("HME_COS_063", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_063", "HME", dtos.get(0).getNewMaterialLotCode()));
        }
        //2021-05-16 14:34 add by chaonan.hu for zhenyong.ban ????????????????????????????????????????????????????????????????????????
        List<String> targetSelectionLotList = itfCosaCollectIfaceMapper.targerSelectionLotQuery(tenantId, newMaterialLot.getMaterialLotId());
        if(CollectionUtils.isNotEmpty(targetSelectionLotList)){
            targetSelectionLotList = targetSelectionLotList.stream().distinct().collect(Collectors.toList());
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if(targetSelectionLotList.size() != 1){
                throw new MtException("HME_COS_061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_061", "HME"));
            }
            if(Objects.isNull(hmePreSelection) && StringUtils.isEmpty(hmePreSelection.getAttribute1())){
                throw new MtException("HME_COS_061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_061", "HME"));
            }
            if(!hmePreSelection.getAttribute1().equals(targetSelectionLotList.get(0))){
                throw new MtException("HME_COS_061", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_061", "HME"));
            }
        }

        //??????????????????
        List<String> oldMaterialLotIds = dtos.stream().map(HmePreSelectionDTO6::getOldMaterialLotId).distinct().collect(Collectors.toList());
        List<MtMaterialLot> oldmtMaterialLots = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, oldMaterialLotIds);

        //2021-11-16 By ?????? for ????????? ?????????????????????????????????????????????????????????
        oldmtMaterialLots.forEach(e->{
            if(!e.getPrimaryUomId().equals(newMaterialLot.getPrimaryUomId())){
                throw new MtException("HME_COS_065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_COS_065", "HME",e.getIdentification()));
            }
        });

        //???????????????
        List<String> virtualNumList = dtos.stream().map(HmePreSelectionDTO6::getVirtualNum).distinct().collect(Collectors.toList());
        for (String virtualNum :
                virtualNumList) {
            //?????????????????????
            List<HmeVirtualNum> hmeVirtualNums = hmeVirtualNumRepository.selectByCondition(Condition.builder(HmeVirtualNum.class)
                    .andWhere(Sqls.custom()
                            .andEqualTo(HmeVirtualNum.FIELD_VIRTUAL_NUM, virtualNum))
                    .build());
            if (CollectionUtils.isNotEmpty(hmeVirtualNums)) {
                if (StringUtils.isEmpty(hmeVirtualNums.get(0).getMaterialLotId())) {
                    HmeVirtualNum hmeVirtualNum = hmeVirtualNums.get(0);
                    hmeVirtualNum.setMaterialLotId(newMaterialLotIds.get(0));
                    if (StringUtils.isNotBlank(newMaterialLot.getLocatorId())) {
                        hmeVirtualNum.setLocatorId(newMaterialLot.getLocatorId());
                        MtModLocator mtModLocator = hmePreSelectionMapper.getWarehouse(tenantId, newMaterialLot.getLocatorId());
                        if (!ObjectUtils.isEmpty(mtModLocator)) {
                            hmeVirtualNum.setWarehouseId(mtModLocator.getLocatorId());
                        }
                    }
                    hmeVirtualNumMapper.updateByPrimaryKeySelective(hmeVirtualNum);
                } else {
                    if (!hmeVirtualNums.get(0).getMaterialLotId().equals(newMaterialLotIds.get(0))) {
                        throw new MtException("HME_SELECT_001", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_SELECT_001", "HME", hmeVirtualNums.get(0).getVirtualNum()));
                    }

                }
            }
        }

        //???????????????????????????
        HmeSelectionDetails hmeSelectionDetails = new HmeSelectionDetails();
        hmeSelectionDetails.setNewMaterialLotId(newMaterialLotIds.get(0));
        List<HmeSelectionDetails> select2 = hmeSelectionDetailsRepository.select(hmeSelectionDetails);
        List<String> collect1 = select2.stream().map(t -> t.getVirtualNum()).distinct().collect(Collectors.toList());
        int a = 1;
        int b = 1;
        for (String virtualNum :
                collect1) {
            int size = hmePreSelectionMapper.getOrderBy(virtualNum).size();
            if (size % 8 > 0) {
                a = a + 1 + size / 8;
            } else {
                a = a + size / 8;
            }
        }
        for (String virtualNum :
                virtualNumList) {
            //??????????????????????????????
            List<HmePreSelectionDTO6> list = dtos.stream().filter(t -> t.getVirtualNum().equals(virtualNum)).sorted(Comparator.comparing(HmePreSelectionDTO6::getWays)).collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                int c = i / 8;
                int d = i % 8;
                HmeSelectionDetails hmeSelectionDetails1 = new HmeSelectionDetails();
                hmeSelectionDetails1.setSelectionDetailsId(list.get(i).getSelectionDetailsId());
                hmeSelectionDetails1.setNewMaterialLotId(newMaterialLotIds.get(0));
                hmeSelectionDetails1.setNewLoad((a + c) + "," + (d + b));
                hmeSelectionDetails1.setAttribute1("LOADED");
                hmeSelectionDetailsMapper.updateByPrimaryKeySelective(hmeSelectionDetails1);
                HmeSelectionDetails hmeSelectionDetails2 = hmeSelectionDetailsMapper.selectByPrimaryKey(hmeSelectionDetails1);
                //???????????????
                HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                hmeMaterialLotLoad.setLoadSequence(hmeSelectionDetails2.getLoadSequence());
                HmeMaterialLotLoad select = hmeMaterialLotLoadRepository.select(hmeMaterialLotLoad).get(0);
                select.setSourceLoadColumn(select.getLoadColumn());
                select.setSourceLoadRow(select.getLoadRow());
                select.setSourceMaterialLotId(select.getMaterialLotId());
                select.setMaterialLotId(newMaterialLotIds.get(0));
                select.setLoadRow((long) (a + c));
                select.setLoadColumn((long) (b + d));
                //2021-04-21 17:09 add by chaonan.hu for kang.wang ??????material_lot_id+load_row+load_column???????????????
                int count = hmeMaterialLotLoadRepository.selectCount(new HmeMaterialLotLoad() {{
                    setTenantId(tenantId);
                    setMaterialLotId(select.getMaterialLotId());
                    setLoadRow(select.getLoadRow());
                    setLoadColumn(select.getLoadColumn());
                }});
                if(count > 0){
                    throw new MtException("HME_SELECT_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_SELECT_007", "HME"));
                }
                hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(select);
            }
            if (list.size() % 8 > 0) {
                a = a + 1 + list.size() / 8;
            } else {
                a = a + list.size() / 8;
            }
        }

        //????????????
        String receiptRequestId = mtEventRequestRepository.eventRequestCreate(tenantId, "PRODUCT_RECEIPT_CREATE");
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode("COS_CHIP_SELECT_IN");
        // ????????????
        eventCreateVO.setEventRequestId(receiptRequestId);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        MtEventCreateVO eventCreateVO1 = new MtEventCreateVO();

        eventCreateVO1.setEventTypeCode("COS_CHIP_SELECT_OUT");
        eventCreateVO1.setEventRequestId(receiptRequestId);
        String eventId1 = mtEventRepository.eventCreate(tenantId, eventCreateVO1);

        List<MtMaterialLotVO20> mtMaterialLotVO20List = new ArrayList<>();
        List<MtMaterialLotVO20> sourceMaterialLotVO20List = new ArrayList<>();
        List<MtCommonExtendVO7> mtCommonExtendVO7List = new ArrayList<>();
        long cosNumSum = 0;
        for (MtMaterialLot oldmtMaterialLot :
                oldmtMaterialLots) {
            // ???????????????
            if (!YES.equals(oldmtMaterialLot.getEnableFlag())) {
                throw new MtException("HME_COS_022", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "HME_COS_022", HmeConstants.ConstantValue.HME, oldmtMaterialLot.getMaterialLotCode()));
            }
            //???????????????
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(oldmtMaterialLot.getMaterialLotId());
            mtMaterialLotAttrVO2.setAttrName("MF_FLAG");
            List<MtExtendAttrVO> mtExtendAttrVOS = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if (CollectionUtils.isNotEmpty(mtExtendAttrVOS) && "Y".equals(mtExtendAttrVOS.get(0).getAttrValue())) {
                throw new MtException("HME_NC_0029", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0029", "HME"));
            }
            //????????????????????????
            MtMaterialLot newMaterialLotTemp = mtMaterialLotRepository.materialLotPropertyGet(tenantId, newMaterialLotIds.get(0));
            long cosNum = dtos.stream().filter(t -> t.getOldMaterialLotId().equals(oldmtMaterialLot.getMaterialLotId())).count();
            cosNumSum += cosNum;

            if (YES.equals(newMaterialLotTemp.getEnableFlag())) {
                if (!oldmtMaterialLot.getMaterialId().equals(newMaterialLotTemp.getMaterialId())) {
                    throw new MtException("HME_CHIP_TRANSFER_023", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_CHIP_TRANSFER_023", "HME"));
                }
                if (!oldmtMaterialLot.getLocatorId().equals(newMaterialLotTemp.getLocatorId())) {
                    throw new MtException("HME_COS_062", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_COS_062", "HME"));
                }
                if (!oldmtMaterialLot.getLot().equals(newMaterialLotTemp.getLot())) {
                    throw new MtException("HME_COS_004", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_COS_004", "HME"));
                }
                List<LovValueDTO> typeLov = lovAdapter.queryLovValue("HME.CONTAINER_CAPACITY", tenantId);
                if((newMaterialLotTemp.getPrimaryUomQty() + (double) cosNumSum) > Integer.parseInt(typeLov.get(0).getValue())){
                    throw new MtException("MT_MATERIAL_LOT_0043", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0043", "MATERIAL_LOT",typeLov.get(0).getValue()));
                }
                typeLov.get(0).getValue();
                //2021-04-22 16:15 edit by chaonan.hu for kang.wang ???????????????????????????????????????
                MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
                mtCommonExtendVO7.setKeyId(newMaterialLotIds.get(0));
                List<MtCommonExtendVO4> mtCommonExtendVO4List = new ArrayList<>();
                MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
                mtCommonExtendVO4.setAttrName("ORIGINAL_ID");
                mtCommonExtendVO4.setAttrValue(oldmtMaterialLot.getMaterialLotId());
                mtCommonExtendVO4List.add(mtCommonExtendVO4);
                mtCommonExtendVO7.setAttrs(mtCommonExtendVO4List);
                mtCommonExtendVO7List.add(mtCommonExtendVO7);

//                List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
//                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
//                mtExtendVO5.setAttrName("ORIGINAL_ID");
//                mtExtendVO5.setAttrValue(oldmtMaterialLot.getMaterialLotId());
//                mtExtendVO5List.add(mtExtendVO5);
//
//                MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
//                mtExtendVO10.setKeyId(newMaterialLotIds.get(0));
//                mtExtendVO10.setEventId(eventId);
//                mtExtendVO10.setAttrs(mtExtendVO5List);
//                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);

                //2021-04-22 16:15 edit by chaonan.hu for kang.wang ???????????????????????????
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(newMaterialLotTemp.getMaterialLotId());
                mtMaterialLotVO20.setPrimaryUomQty(newMaterialLotTemp.getPrimaryUomQty() + (double) cosNum);
                mtMaterialLotVO20List.add(mtMaterialLotVO20);

//                MtMaterialLotVO2 mtLotUpdate = new MtMaterialLotVO2();
//                mtLotUpdate.setMaterialLotId(newMaterialLotTemp.getMaterialLotId());
//                mtLotUpdate.setPrimaryUomQty(newMaterialLotTemp.getPrimaryUomQty() + (double) cosNum);
//                mtLotUpdate.setEventId(eventId);
//                mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotUpdate, HmeConstants.ConstantValue.NO);
            } else {
                List<LovValueDTO> typeLov = lovAdapter.queryLovValue("HME.CONTAINER_CAPACITY", tenantId);
                if(cosNumSum > Integer.parseInt(typeLov.get(0).getValue())){
                    throw new MtException("MT_MATERIAL_LOT_0043", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "MT_MATERIAL_LOT_0043", "MATERIAL_LOT",typeLov.get(0).getValue()));
                }
                //???????????? ??????lot
                List<MtExtendSettings> mtExtendSettingss = new ArrayList<>();
                MtExtendSettings mtExtendSettings = new MtExtendSettings();
                mtExtendSettings.setAttrName("CONTAINER_TYPE");
                mtExtendSettingss.add(mtExtendSettings);
                MtExtendSettings mtExtendSettings1 = new MtExtendSettings();
                mtExtendSettings1.setAttrName("LOCATION_ROW");
                mtExtendSettingss.add(mtExtendSettings1);
                MtExtendSettings mtExtendSettings2 = new MtExtendSettings();
                mtExtendSettings2.setAttrName("LOCATION_COLUMN");
                mtExtendSettingss.add(mtExtendSettings2);
                MtExtendSettings mtExtendSettings3 = new MtExtendSettings();
                mtExtendSettings3.setAttrName("CHIP_NUM");
                mtExtendSettingss.add(mtExtendSettings3);
                MtExtendSettings mtExtendSettings4 = new MtExtendSettings();
                mtExtendSettings4.setAttrName("MF_FLAG");
                mtExtendSettingss.add(mtExtendSettings4);
                MtExtendSettings mtExtendSettings5 = new MtExtendSettings();
                mtExtendSettings5.setAttrName("STATUS");
                mtExtendSettingss.add(mtExtendSettings5);
                MtExtendSettings mtExtendSettings6 = new MtExtendSettings();
                mtExtendSettings6.setAttrName("COS_TYPE");
                mtExtendSettingss.add(mtExtendSettings6);
                List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                        "mt_material_lot_attr", "MATERIAL_LOT_ID", oldmtMaterialLots.get(0).getMaterialLotId(), mtExtendSettingss);

                //2021-04-22 16:15 edit by chaonan.hu for kang.wang ???????????????????????????????????????
                MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
                mtCommonExtendVO7.setKeyId(newMaterialLotIds.get(0));
                List<MtCommonExtendVO4> mtCommonExtendVO4List = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
                    mtExtendAttrVOList.forEach(item -> {
                        List<String> name = mtExtendSettingss.stream().map(MtExtendSettings::getAttrName).collect(Collectors.toList());
                        if (name.contains(item.getAttrName())) {
                            MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
                            mtCommonExtendVO4.setAttrName(item.getAttrName());
                            mtCommonExtendVO4.setAttrValue(item.getAttrValue());
                            mtCommonExtendVO4List.add(mtCommonExtendVO4);
                        }
                    });
                }
                MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
                mtCommonExtendVO4.setAttrName("ORIGINAL_ID");
                mtCommonExtendVO4.setAttrValue(oldmtMaterialLot.getMaterialLotId());
                mtCommonExtendVO4List.add(mtCommonExtendVO4);
                mtCommonExtendVO7.setAttrs(mtCommonExtendVO4List);
                mtCommonExtendVO7List.add(mtCommonExtendVO7);

//                List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
//                if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
//                    mtExtendAttrVOList.forEach(item -> {
//                        List<String> name = mtExtendSettingss.stream().map(MtExtendSettings::getAttrName).collect(Collectors.toList());
//                        if (name.contains(item.getAttrName())) {
//                            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
//                            mtExtendVO5.setAttrName(item.getAttrName());
//                            mtExtendVO5.setAttrValue(item.getAttrValue());
//                            mtExtendVO5List.add(mtExtendVO5);
//                        }
//                    });
//                }
//                MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
//                mtExtendVO5.setAttrName("ORIGINAL_ID");
//                mtExtendVO5.setAttrValue(oldmtMaterialLot.getMaterialLotId());
//                mtExtendVO5List.add(mtExtendVO5);
//
//                MtExtendVO10 mtExtendVO10 = new MtExtendVO10();
//                mtExtendVO10.setKeyId(newMaterialLotIds.get(0));
//                mtExtendVO10.setEventId(eventId);
//                mtExtendVO10.setAttrs(mtExtendVO5List);
//                mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, mtExtendVO10);

                String lot = profileClient.getProfileValueByOptions("HME_COS_MATERIAL_LOT_LOT");
                if (StringUtils.isEmpty(lot)) {
                    throw new MtException("HME_COS_006", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_COS_006", "HME"));
                }
                //2021-04-22 16:15 edit by chaonan.hu for kang.wang ???????????????????????????
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(newMaterialLotTemp.getMaterialLotId());
                mtMaterialLotVO20.setPrimaryUomQty((double) cosNum);
                mtMaterialLotVO20.setMaterialId(oldmtMaterialLot.getMaterialId());
                mtMaterialLotVO20.setEnableFlag("Y");
                mtMaterialLotVO20.setLot(lot);
                mtMaterialLotVO20.setQualityStatus("OK");
                mtMaterialLotVO20.setLocatorId(oldmtMaterialLot.getLocatorId());
                mtMaterialLotVO20.setSupplierId(oldmtMaterialLot.getSupplierId());
                mtMaterialLotVO20.setSupplierSiteId(oldmtMaterialLot.getSupplierSiteId());
                mtMaterialLotVO20.setPrimaryUomId(oldmtMaterialLot.getPrimaryUomId());
                mtMaterialLotVO20List.add(mtMaterialLotVO20);

//                MtMaterialLotVO2 mtLotUpdate = new MtMaterialLotVO2();
//                mtLotUpdate.setMaterialLotId(newMaterialLotTemp.getMaterialLotId());
//                mtLotUpdate.setPrimaryUomQty((double) cosNum);
//                mtLotUpdate.setMaterialId(oldmtMaterialLot.getMaterialId());
//                mtLotUpdate.setEnableFlag("Y");
//                mtLotUpdate.setLot(lot);
//                mtLotUpdate.setQualityStatus("OK");
//                mtLotUpdate.setLocatorId(oldmtMaterialLot.getLocatorId());
//                mtLotUpdate.setSupplierId(oldmtMaterialLot.getSupplierId());
//                mtLotUpdate.setSupplierSiteId(oldmtMaterialLot.getSupplierSiteId());
//                mtLotUpdate.setEventId(eventId);
//                mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotUpdate, HmeConstants.ConstantValue.NO);
            }

            //2021-04-22 16:15 edit by chaonan.hu for kang.wang ?????????????????????????????????
            MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
            mtMaterialLotVO20.setMaterialLotId(oldmtMaterialLot.getMaterialLotId());
            mtMaterialLotVO20.setTrxPrimaryUomQty(new BigDecimal(cosNum * -1).doubleValue());
            mtMaterialLotVO20.setPrimaryUomQty((new BigDecimal(oldmtMaterialLot.getPrimaryUomQty()).subtract(new BigDecimal(cosNum))).doubleValue());
            if (new BigDecimal(mtMaterialLotVO20.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) == 0) {
                mtMaterialLotVO20.setEnableFlag("N");
            }
            mtMaterialLotVO20.setMaterialId(oldmtMaterialLot.getMaterialId());
            sourceMaterialLotVO20List.add(mtMaterialLotVO20);

//            //????????????????????????
//            MtMaterialLotVO2 mtLotUpdate1 = new MtMaterialLotVO2();
//            mtLotUpdate1.setMaterialLotId(oldmtMaterialLot.getMaterialLotId());
//            mtLotUpdate1.setPrimaryUomQty((new BigDecimal(oldmtMaterialLot.getPrimaryUomQty()).subtract(new BigDecimal(cosNum))).doubleValue());
//            if (new BigDecimal(mtLotUpdate1.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) == 0) {
//                mtLotUpdate1.setEnableFlag("N");
//            }
//            mtLotUpdate1.setMaterialId(oldmtMaterialLot.getMaterialId());
//            mtLotUpdate1.setEventId(eventId1);
//            mtMaterialLotRepository.materialLotUpdate(tenantId, mtLotUpdate1, HmeConstants.ConstantValue.NO);
        }
        //???????????????????????? ??????????????????????????????????????????????????????
        if(CollectionUtils.isNotEmpty(mtMaterialLotVO20List)){
            String materialLotId = mtMaterialLotVO20List.get(0).getMaterialLotId();
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
            if(YES.equals(mtMaterialLot.getEnableFlag())){
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                mtMaterialLotVO20.setPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() + (double) cosNumSum);
                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, Collections.singletonList(mtMaterialLotVO20), eventId, NO);
            }else{
                MtMaterialLotVO20 mtMaterialLotVO20 = mtMaterialLotVO20List.get(mtMaterialLotVO20List.size() - 1);
                mtMaterialLotVO20.setPrimaryUomQty((double) cosNumSum);
                mtMaterialLotRepository.materialLotBatchUpdate(tenantId, Collections.singletonList(mtMaterialLotVO20), eventId, NO);
            }
        }
        List<String> unBindingMaterialLotIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(sourceMaterialLotVO20List)){
            //??????????????????????????????
            List<MtMaterialLotVO20> sourceMaterialLotVO20s = new ArrayList<>();
            Map<String, List<MtMaterialLotVO20>> sourceMaterialLotMap = sourceMaterialLotVO20List.stream().collect(Collectors.groupingBy(MtMaterialLotVO20::getMaterialLotId));
            for(Map.Entry<String, List<MtMaterialLotVO20>> entry:sourceMaterialLotMap.entrySet()){
                List<MtMaterialLotVO20> value = entry.getValue();
                if(value.size() > 1){
                    BigDecimal qty = value.stream().collect(CollectorsUtil
                            .summingBigDecimal(item -> BigDecimal.valueOf(item.getTrxPrimaryUomQty())));
                    MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                    mtMaterialLotVO20.setMaterialLotId(entry.getKey());
                    MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(entry.getKey());
                    //?????????qty??????????????????????????????
                    mtMaterialLotVO20.setPrimaryUomQty(mtMaterialLot.getPrimaryUomQty() + qty.doubleValue());
                    mtMaterialLotVO20.setMaterialId(value.get(value.size() - 1).getMaterialId());
                    if (new BigDecimal(mtMaterialLotVO20.getPrimaryUomQty()).compareTo(BigDecimal.ZERO) == 0) {
                        mtMaterialLotVO20.setEnableFlag("N");
                        unBindingMaterialLotIdList.add(mtMaterialLotVO20.getMaterialLotId());
                    }
                    sourceMaterialLotVO20s.add(mtMaterialLotVO20);
                }else{
                    value.get(0).setTrxPrimaryUomQty(null);
                    if (new BigDecimal(value.get(0).getPrimaryUomQty()).compareTo(BigDecimal.ZERO) == 0) {
                        unBindingMaterialLotIdList.add(value.get(0).getMaterialLotId());
                    }
                    sourceMaterialLotVO20s.addAll(value);
                }
            }
            mtMaterialLotRepository.materialLotBatchUpdate(tenantId, sourceMaterialLotVO20s, eventId1, NO);
        }
        //2021-06-28 10:03 add by chaonan.hu for peng.zhao ?????????????????????????????????
        if(CollectionUtils.isNotEmpty(unBindingMaterialLotIdList)){
            hmePreSelectionRepository.materialLotBatchUnBindingContainer(tenantId, unBindingMaterialLotIdList);
        }
        //?????????????????????????????????
        if(CollectionUtils.isNotEmpty(mtCommonExtendVO7List)){
            mtCommonExtendVO7List = Collections.singletonList(mtCommonExtendVO7List.get(mtCommonExtendVO7List.size() - 1));
            mtExtendSettingsRepository.attrPropertyBatchUpdateNew(tenantId, "mt_material_lot_attr", eventId, mtCommonExtendVO7List);
        }

        List<HmeSelectionDetails> hmeSelectionDetails1 = hmeSelectionDetailsMapper.selectByCondition(Condition.builder(HmeSelectionDetails.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmeSelectionDetails.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeSelectionDetails.FIELD_PRE_SELECTION_ID, hmePreSelection.getPreSelectionId()))
                .build());
        long loaded = hmeSelectionDetails1.stream().filter(t -> t.getAttribute1().equals("LOADED")).count();
        if (loaded == hmeSelectionDetails1.size()) {
            hmePreSelection.setStatus("LOADED");
        } else {
            hmePreSelection.setStatus("LOADING");
        }
        hmePreSelectionMapper.updateByPrimaryKeySelective(hmePreSelection);
    }

    /**
     * @param tenantId
     * @param containerCode
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO5>
     * @description ??????????????????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/3 15:08
     **/
    @Override
    public List<HmePreSelectionReturnDTO5> materialLotQueryByContainer(Long tenantId, String containerCode) {
        return hmePreSelectionMapper.materialLotQueryByContainer(tenantId, containerCode);
    }

    /**
     * @param tenantId
     * @param productTypeMeaning
     * @param ruleCode
     * @param statusMeaning
     * @param materialCode
     * @param materialName
     * @param pageRequest
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO6>
     * @description ??????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/2 16:10
     **/
    @Override
    @ProcessLovValue
    public Page<HmePreSelectionReturnDTO6> selectLotQueryAll(Long tenantId, String productTypeMeaning, String ruleCode, String statusMeaning, String materialCode, String materialName, PageRequest pageRequest) {
        Page<HmePreSelectionReturnDTO6> result = new Page<HmePreSelectionReturnDTO6>();
        String productTyperesult = null;
        String statusresult = null;

        if (StringUtils.isNotBlank(productTypeMeaning)) {
            List<LovValueDTO> List = lovAdapter.queryLovValue("HME_PRODUCT_TYPE", tenantId);
            if (CollectionUtils.isNotEmpty(List)) {
                List<LovValueDTO> collect = List.stream().filter(t -> t.getMeaning().equals(productTypeMeaning)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect) && StringUtils.isNotBlank(collect.get(0).getValue())) {
                    productTyperesult = collect.get(0).getValue();
                } else {
                    return result;
                }
            } else {
                return result;
            }
        }
        if (StringUtils.isNotBlank(statusMeaning)) {
            List<LovValueDTO> List = lovAdapter.queryLovValue("HME.SELECT_STATUS", tenantId);
            if (CollectionUtils.isNotEmpty(List)) {
                List<LovValueDTO> collect = List.stream().filter(t -> t.getMeaning().equals(statusMeaning)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect) && StringUtils.isNotBlank(collect.get(0).getValue())) {
                    statusresult = collect.get(0).getValue();
                } else {
                    return result;
                }
            } else {
                return result;
            }
        }
        String finalProductTyperesult = productTyperesult;
        String finalStatusresult = statusresult;
        result =PageHelper.doPage(pageRequest, () -> hmePreSelectionMapper.selectLotQueryAll(tenantId, finalProductTyperesult, ruleCode, finalStatusresult, materialCode, materialName));
        return result;
    }

    /**
     * @param tenantId
     * @param containerCode
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO7>
     * @description ???????????????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/2 17:34
     **/
    @Override
    public List<HmePreSelectionReturnDTO7> selectLotQueryElse(Long tenantId, String containerCode) {
        return hmePreSelectionMapper.selectLotQueryElse(tenantId, containerCode);
    }

    @Override
    public List<HmePreSelectionReturnDTO8> selectLotInformation(Long tenantId, String selectLot) {
        List<HmePreSelectionReturnDTO8> hmePreSelectionReturnDTO8s = hmePreSelectionMapper.selectLotInformation(tenantId, selectLot);
        if (CollectionUtils.isNotEmpty(hmePreSelectionReturnDTO8s)) {
            long j = 1l;
            String virtualNum = hmePreSelectionReturnDTO8s.get(0).getVirtualNum();
            String finalVirtualNum = virtualNum;
            List<HmePreSelectionReturnDTO8> collect = hmePreSelectionReturnDTO8s.stream().filter(t -> t.getVirtualNum().equals(finalVirtualNum)).collect(Collectors.toList());
            BigDecimal reduce = collect.stream().map(HmePreSelectionReturnDTO8::getA02).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal divide = collect.stream().map(HmePreSelectionReturnDTO8::getA04).reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(collect.size()));
            for (HmePreSelectionReturnDTO8 temp :
                    hmePreSelectionReturnDTO8s) {
                //??????
                MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, temp.getCreatedBy());
                temp.setUserName(mtUserInfo.getRealName());

                temp.setStatus("??????");

                //????????????
                if (StringUtils.isNotBlank(temp.getOldLoad())) {
                    String[] split = temp.getOldLoad().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    temp.setLoad((char) (64 + Integer.parseInt(split[0])) + split[1]);
                }

                //?????????
                if (virtualNum.equals(temp.getVirtualNum())) {
                    temp.setWays("COS" + String.format("%02d", j));
                    j++;
                    //?????????
                    temp.setAvga04(divide);
                    //??????
                    temp.setSuna02(reduce);
                } else {
                    virtualNum = temp.getVirtualNum();
                    j = 1l;
                    temp.setWays("COS" + String.format("%02d", j));
                    List<HmePreSelectionReturnDTO8> HmePreSelectionReturnDTO8Temp = hmePreSelectionReturnDTO8s.stream().filter(t -> t.getVirtualNum().equals(finalVirtualNum)).collect(Collectors.toList());
                    reduce = HmePreSelectionReturnDTO8Temp.stream().map(HmePreSelectionReturnDTO8::getA02).reduce(BigDecimal.ZERO, BigDecimal::add);
                    divide = HmePreSelectionReturnDTO8Temp.stream().map(HmePreSelectionReturnDTO8::getA04).reduce(BigDecimal.ZERO, BigDecimal::add).divide(new BigDecimal(collect.size()));
                    temp.setAvga04(divide);
                    temp.setSuna02(reduce);

                }
            }
        }
        return hmePreSelectionReturnDTO8s;
    }

    @Transactional(rollbackFor = Exception.class)
    public String PreSelection(Long tenantId, HmePreSelection hmePreSelection, List<HmeCosRuleLogic> hmeCosRuleLogic, List<HmeCosRuleTypeDTO1> hmeCosRuleTypeDTO1, List<HmePreSelectionDTO4> hmePreSelectionDTO4s, HmePreSelectionDTO2 dto) {
        //??????userId
        Long userId = -1L;
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        int resultMessage = 0;
        //??????????????????????????????
        List<HmeCosRuleLogic> limitRule = hmeCosRuleLogic.stream().filter(t -> "A".equals(t.getCountType()) || "B".equals(t.getCountType())).collect(Collectors.toList());

        //??????????????????????????????????????????????????????
        List<HmeCosRuleTypeDTO1> cosTypeList = new ArrayList<>();
        List<HmeCosRuleLogic> powerRule = hmeCosRuleLogic.stream().filter(t -> "B".equals(t.getCountType()) && t.getCollectionItem() == "A02" && ">=".equals(t.getRangeType())).collect(Collectors.toList());
        boolean isFlag = false;
        if (StringUtils.isEmpty(hmeCosRuleTypeDTO1.get(0).getPowerSinglePoint())) {
            isFlag = true;
            cosTypeList = getPowerNum(powerRule, hmePreSelectionDTO4s, hmeCosRuleTypeDTO1.get(0));
            log.info("<====== ??????{}", cosTypeList);
        } else {
            isFlag = false;
            cosTypeList.addAll(hmeCosRuleTypeDTO1);
        }

        List<HmeCosFunction> hmeCosFunctions = hmePreSelectionMapper.selectFunction(dto.getMaterialLotIdList());

        boolean filstflag = false;
        do {
            List<HmePreSelectionDTO5> countList = new ArrayList<>();
            List<HmePreSelectionDTO4> otherList = new ArrayList<>();
            //??????????????????????????????
            long sum1 = hmeCosRuleTypeDTO1.stream().mapToLong(HmeCosRuleTypeDTO1::getCosCount).summaryStatistics().getSum();

            if (filstflag) {
                for (int i = cosTypeList.size() - 1; i >= 0; i--) {
                    if (cosTypeList.get(i).getCosCount() > 0) {
                        HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO11 = cosTypeList.get(i);
                        hmeCosRuleTypeDTO11.setCosCount(hmeCosRuleTypeDTO11.getCosCount() - 1);
                        cosTypeList.set(i, hmeCosRuleTypeDTO11);
                        HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO12 = cosTypeList.get(i - 1);
                        hmeCosRuleTypeDTO12.setCosCount(hmeCosRuleTypeDTO12.getCosCount() + 1);
                        cosTypeList.set(i - 1, hmeCosRuleTypeDTO12);
                        break;
                    }
                }
            }
            for (HmeCosRuleLogic powerRuleTemp :
                    powerRule) {
                if (cosTypeList.size() == 1) {
                    HmePreSelectionDTO5 hmePreSelectionDTO5 = new HmePreSelectionDTO5();
                    hmePreSelectionDTO5.setCurrent(powerRuleTemp.getCurrent());
                    hmePreSelectionDTO5.setPowerSinglePoint(cosTypeList.get(0).getPowerSinglePoint());
                    hmePreSelectionDTO5.setPower(Double.valueOf(powerRuleTemp.getRuleValue()));
                    countList.add(hmePreSelectionDTO5);
                } else {
                    List<HmePreSelectionDTO4> hmePreSelectionDTO4Temp = hmePreSelectionDTO4s.stream().filter(t -> t.getCurrent().equals(powerRule.get(0).getCurrent())).collect(Collectors.toList());
                    Map<String, Double> powerAvg = hmePreSelectionDTO4Temp.stream()
                            .collect(Collectors.groupingBy(HmePreSelectionDTO4::getA01,
                                    Collectors.averagingDouble(HmePreSelectionDTO4::getA02)));
                    for (HmeCosRuleTypeDTO1 cosTypeTemp :
                            cosTypeList) {
                        HmePreSelectionDTO5 hmePreSelectionDTO5 = new HmePreSelectionDTO5();
                        hmePreSelectionDTO5.setCurrent(powerRuleTemp.getCurrent());
                        hmePreSelectionDTO5.setPowerSinglePoint(cosTypeTemp.getPowerSinglePoint());
                        hmePreSelectionDTO5.setPower(powerAvg.get(cosTypeTemp.getPowerSinglePoint()) * cosTypeTemp.getCosCount());
                        countList.add(hmePreSelectionDTO5);
                    }
                }
            }
            filstflag = true;
            //??????????????????????????????
            List<HmeCosRuleTypeDTO1> limitRuleResult = cosTypeList.stream().filter(t -> !t.getCosCount().equals(0L)).collect(Collectors.toList());
            log.info("<====== ??????????????????????????????{}", limitRuleResult);

            //????????????????????????????????????
            Set<String> powerSinglePoints = limitRuleResult.stream()
                    .map(HmeCosRuleTypeDTO1::getPowerSinglePoint)
                    .collect(Collectors.toSet());
            List<HmePreSelectionDTO4> hmePreSelectionDTO4Power = hmePreSelectionDTO4s.stream().filter(t -> powerSinglePoints.contains(t.getA01())).collect(Collectors.toList());
            ArrayList<HmePreSelectionDTO4> hmePreSelectionDTO4Last = hmePreSelectionDTO4Power.stream().collect(
                    collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getLoadSequence()))),
                            ArrayList::new));

            int num = 0;
            int all = 1000000;
            if (StringUtils.isNotBlank(dto.getSetsNum()) && Integer.parseInt(dto.getSetsNum()) == resultMessage) {
                break;
            }
            for (int i = 0; i < all; i++) {
                List<HmePreSelectionDTO4> result = new ArrayList<>();
                HmeVirtualNum hmeVirtualNum = new HmeVirtualNum();

                List<String> collect10 = limitRule.stream().map(HmeCosRuleLogic::getRuleValue).collect(Collectors.toList());
                List<HmeCosRuleTypeDTO1> select11 = new ArrayList<>();
                for (HmeCosRuleTypeDTO1 temp :
                        limitRuleResult) {
                    HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO11 = new HmeCosRuleTypeDTO1();
                    BeanUtils.copyProperties(temp, hmeCosRuleTypeDTO11);
                    select11.add(hmeCosRuleTypeDTO11);
                }

                List<HmePreSelectionDTO5> countListTemp = new ArrayList<>();
                for (HmePreSelectionDTO5 temp :
                        countList) {
                    HmePreSelectionDTO5 hmePreSelectionDTO5 = new HmePreSelectionDTO5();
                    BeanUtils.copyProperties(temp, hmePreSelectionDTO5);
                    countListTemp.add(hmePreSelectionDTO5);
                }
                long sum = hmeCosRuleTypeDTO1.stream().mapToLong(HmeCosRuleTypeDTO1::getCosCount).summaryStatistics().getSum();

                //???????????????
                Map<String, Long> mapByMaterialLotCode = hmePreSelectionDTO4Last.stream().collect(Collectors.groupingBy(
                        HmePreSelectionDTO4::getMaterialLotCode, Collectors.counting()
                ));
                //??????
                LinkedHashMap<String, String> mapGroupBy = mapByMaterialLotCode.entrySet().stream().sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getKey, (e1, e2) -> e2, LinkedHashMap::new));

                //????????????
                for (String key : mapGroupBy.keySet()) {
                    String materialLotCode = mapGroupBy.get(key);

                    //????????????????????????
                    List<HmePreSelectionDTO4> mePreSelectionDTO4Bylot = hmePreSelectionDTO4Last.stream().filter(t -> t.getMaterialLotCode().equals(materialLotCode)).collect(Collectors.toList());

                    //??????????????????????????????
                    List<HmePreSelectionDTO4> mePreSelectionDTO4GroupBy = mePreSelectionDTO4Bylot.stream().sorted(Comparator.comparing(HmePreSelectionDTO4::getLoadRow).thenComparing(HmePreSelectionDTO4::getLoadColumn)).collect(Collectors.toList());

                    //?????????????????????
                    for (HmePreSelectionDTO4 temp :
                            mePreSelectionDTO4GroupBy) {
                        if (otherList.contains(temp)) {
                            break;
                        }
                        boolean flag = true;
                        List<String> collect10Temp = new ArrayList<>();

                        //????????????????????????????????????
                        List<HmeCosRuleTypeDTO1> collect7 = select11.stream().filter(t -> t.getCosType().equals(temp.getCosType()) && t.getMaterialId().equals(temp.getMaterialId()) && t.getPowerSinglePoint().equals(temp.getA01())).collect(Collectors.toList());

                        if (CollectionUtils.isNotEmpty(collect7) && collect7.get(0).getCosCount() != 0) {
                            //?????????????????????
                            List<HmeCosFunction> hmeCosFunctionAll = hmeCosFunctions.stream().filter(t -> t.getLoadSequence().equals(temp.getLoadSequence())).collect(Collectors.toList());
                            //????????????
                            for (int j = 0; j < limitRule.size(); j++) {

                                //???????????????????????????
                                String current = limitRule.get(j).getCurrent();
                                List<HmeCosFunction> hmeCosFunction = hmeCosFunctionAll.stream().filter(t -> t.getCurrent().equals(current)).collect(Collectors.toList());
                                //??????
                                if (CollectionUtils.isNotEmpty(hmeCosFunction)) {
                                    if ("A".equals(limitRule.get(j).getCountType())) {
                                        //??????
                                        if (CollectionUtils.isNotEmpty(result)) {
                                            double a = 0;
                                            if ("A07".equals(limitRule.get(j).getCollectionItem())) {
                                                if (hmeCosFunction.get(0).getA07() == null) {
                                                    flag = false;
                                                    break;
                                                } else {
                                                    a = hmeCosFunction.get(0).getA07().doubleValue();
                                                }
                                            } else if ("A02".equals(limitRule.get(j).getCollectionItem())) {
                                                if (hmeCosFunction.get(0).getA02() == null) {
                                                    flag = false;
                                                    break;
                                                } else {
                                                    a = hmeCosFunction.get(0).getA02().doubleValue();
                                                }
                                            } else if ("A04".equals(limitRule.get(j).getCollectionItem())) {
                                                if (hmeCosFunction.get(0).getA04() == null) {
                                                    flag = false;
                                                    break;
                                                } else {
                                                    a = hmeCosFunction.get(0).getA04().doubleValue();
                                                }
                                            } else if ("A05".equals(limitRule.get(j).getCollectionItem())) {
                                                if (hmeCosFunction.get(0).getA05() == null) {
                                                    flag = false;
                                                    break;
                                                } else {
                                                    a = hmeCosFunction.get(0).getA05().doubleValue();
                                                }
                                            } else if ("A06".equals(limitRule.get(j).getCollectionItem())) {
                                                if (hmeCosFunction.get(0).getA06() == null) {
                                                    flag = false;
                                                    break;
                                                } else {
                                                    a = hmeCosFunction.get(0).getA06().doubleValue();
                                                }
                                            }
                                            log.info(collect10.get(j));
                                            double b = Double.parseDouble(collect10.get(j).split(",")[0]);
                                            double c = Double.parseDouble(collect10.get(j).split(",")[1]);

                                            if (">=".equals(limitRule.get(j).getRangeType())) {
                                                if (a > b) {
                                                    if (a - c >= Double.parseDouble(limitRule.get(j).getRuleValue())) {
                                                        collect10Temp.add(a + "," + c);
                                                    } else {
                                                        flag = false;
                                                        break;
                                                    }
                                                } else if (a < c) {
                                                    if (b - a >= Double.parseDouble(limitRule.get(j).getRuleValue())) {
                                                        collect10Temp.add(b + "," + a);
                                                    } else {
                                                        flag = false;
                                                        break;
                                                    }
                                                } else {
                                                    collect10Temp.add(b + "," + c);
                                                }
                                            } else {
                                                if (a > b) {
                                                    if (a - c <= Double.parseDouble(limitRule.get(j).getRuleValue())) {
                                                        collect10Temp.add(a + "," + c);
                                                    } else {
                                                        flag = false;
                                                        break;
                                                    }
                                                } else if (a < c) {
                                                    if ((b - a) <= Double.parseDouble(limitRule.get(j).getRuleValue())) {
                                                        collect10Temp.add(b + "," + a);
                                                    } else {
                                                        flag = false;
                                                        break;
                                                    }
                                                } else {
                                                    collect10Temp.add(b + "," + c);
                                                }
                                            }
                                        } else {
                                            double a = 0;
                                            if ("A07".equals(limitRule.get(j).getCollectionItem())) {
                                                if (hmeCosFunction.get(0).getA07() == null) {
                                                    flag = false;
                                                    break;
                                                } else {
                                                    a = hmeCosFunction.get(0).getA07().doubleValue();
                                                }
                                            } else if ("A02".equals(limitRule.get(j).getCollectionItem())) {
                                                if (hmeCosFunction.get(0).getA02() == null) {
                                                    flag = false;
                                                    break;
                                                } else {
                                                    a = hmeCosFunction.get(0).getA02().doubleValue();
                                                }
                                            } else if ("A04".equals(limitRule.get(j).getCollectionItem())) {
                                                if (hmeCosFunction.get(0).getA04() == null) {
                                                    flag = false;
                                                    break;
                                                } else {
                                                    a = hmeCosFunction.get(0).getA04().doubleValue();
                                                }
                                            } else if ("A05".equals(limitRule.get(j).getCollectionItem())) {
                                                if (hmeCosFunction.get(0).getA05() == null) {
                                                    flag = false;
                                                    break;
                                                } else {
                                                    a = hmeCosFunction.get(0).getA05().doubleValue();
                                                }
                                            } else if ("A06".equals(limitRule.get(j).getCollectionItem())) {
                                                if (hmeCosFunction.get(0).getA06() == null) {
                                                    flag = false;
                                                    break;
                                                } else {
                                                    a = hmeCosFunction.get(0).getA06().doubleValue();
                                                }
                                            }
                                            collect10Temp.add(a + "," + a);
                                        }
                                    } else {
                                        //??????
                                        double a = 0;
                                        if ("A07".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA07() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA07().doubleValue();
                                            }
                                        } else if ("A02".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA02() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA02().doubleValue();
                                            }
                                        } else if ("A04".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA04() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA04().doubleValue();
                                            }
                                        } else if ("A05".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA05() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA05().doubleValue();
                                            }
                                        } else if ("A06".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA06() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA06().doubleValue();
                                            }
                                        }

                                        if (isFlag && "A02".equals(limitRule.get(j).getCollectionItem()) && ">=".equals(limitRule.get(j).getRangeType())) {
                                            String current1 = limitRule.get(j).getCurrent();
                                            List<HmePreSelectionDTO5> collect = countListTemp.stream().filter(t -> t.getCurrent().equals(current1)
                                                    && t.getPowerSinglePoint().equals(temp.getA01())).collect(Collectors.toList());
                                            HmePreSelectionDTO5 hmePreSelectionDTO5 = collect.get(0);
                                            double b = collect.get(0).getPower();
                                            double c = b / collect7.get(0).getCosCount();
                                            if (a >= c) {
                                                int iTemp = countListTemp.indexOf(collect.get(0));
                                                hmePreSelectionDTO5.setPower(b - a);
                                                countListTemp.set(iTemp, hmePreSelectionDTO5);
                                                collect10Temp.add(String.valueOf(b - a));
                                            } else {
                                                flag = false;
                                                break;
                                            }
                                        } else {
                                            double b = Double.parseDouble(collect10.get(j));
                                            double c = b / sum;
                                            if (">=".equals(limitRule.get(j).getRangeType())) {
                                                if (a >= c) {
                                                    collect10Temp.add(String.valueOf(b - a));
                                                } else {
                                                    flag = false;
                                                    break;
                                                }
                                            } else {
                                                if (c >= a) {
                                                    collect10Temp.add(String.valueOf(b - a));
                                                } else {
                                                    flag = false;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    flag = false;
                                    break;
                                }
                            }
                            //??????-1 ??????????????????-1
                            if (flag) {
                                collect10.clear();
                                collect10.addAll(collect10Temp);
                                sum = sum - 1;
                                HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO11 = collect7.get(0);
                                hmeCosRuleTypeDTO11.setCosCount(hmeCosRuleTypeDTO11.getCosCount() - 1);
                                int j = select11.indexOf(collect7.get(0));
                                select11.set(j, hmeCosRuleTypeDTO11);
                                result.add(temp);
                            }
                        }
                        if (sum == 0) {
                            break;
                        }
                    }
                    if (sum == 0) {
                        break;
                    }
                }
                if (sum == 0) {
                    String virtualNum;
                    if (dto.getIsBind().equals('Y')) {
                        //??????????????????EO???????????????
                        MtEoVO14 mtEoVO14 = new MtEoVO14();
                        mtEoVO14.setEoCount("1");
                        mtEoVO14.setTotalQty(Double.valueOf(dto.getCosNum()));
                        mtEoVO14.setWorkOrderId(dto.getWorkOrderId());
                        List<String> eoids = mtEoRepository.woLimitEoBatchCreate(tenantId, mtEoVO14);
                        virtualNum = eoids.get(0);
                    } else {
                        MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
                        mtNumrange.setObjectCode("PICK_NUM");
                        virtualNum = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange).getNumber();
                    }
                    List<HmeSelectionDetails> insertList = new ArrayList<>();

                    for (HmePreSelectionDTO4 temp :
                            result) {
                        HmeSelectionDetails hmeSelectionDetails = new HmeSelectionDetails();
                        hmeSelectionDetails.setPreSelectionId(hmePreSelection.getPreSelectionId());
                        hmeSelectionDetails.setTenantId(tenantId);
                        hmeSelectionDetails.setSiteId(dto.getSiteId());
                        hmeSelectionDetails.setVirtualNum(virtualNum);
                        hmeSelectionDetails.setOldMaterialLotId(temp.getMaterialLotId());
                        hmeSelectionDetails.setOldLoad(temp.getLoadRow() + "," + temp.getLoadColumn());
                        hmeSelectionDetails.setMaterialId(temp.getMaterialId());
                        hmeSelectionDetails.setLoadSequence(temp.getLoadSequence());
                        hmeSelectionDetails.setCosType(temp.getCosType());
                        hmeSelectionDetails.setPower(temp.getA01());
                        hmeSelectionDetails.setAttribute1("NEW");
                        insertList.add(hmeSelectionDetails);
                        hmePreSelectionDTO4Last.removeIf(t -> t.getLoadSequence().equals(temp.getLoadSequence()));
                        hmePreSelectionDTO4s.removeIf(t -> t.getLoadSequence().equals(temp.getLoadSequence()));
                        //?????????????????????
                        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                        hmeMaterialLotLoad.setLoadSequence(temp.getLoadSequence());
                        HmeMaterialLotLoad hmeMaterialLotLoad1 = hmeMaterialLotLoadMapper.selectOne(hmeMaterialLotLoad);
                        hmeMaterialLotLoad1.setStatus("Y");
                        hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(hmeMaterialLotLoad1);
                    }
                    num = num + 1;
                    hmeSelectionDetailsRepository.batchInsertSelective(insertList);
                    hmeVirtualNum.setTenantId(tenantId);
                    hmeVirtualNum.setBindFlag(dto.getIsBind());
                    hmeVirtualNum.setVirtualNum(virtualNum);
                    hmeVirtualNum.setMaterialId(dto.getMaterialId());
                    hmeVirtualNum.setProductCode(dto.getProductType());
                    hmeVirtualNum.setWorkOrderId(dto.getWorkOrderId());
                    hmeVirtualNum.setQuantity(hmeCosRuleTypeDTO1.stream().mapToLong(HmeCosRuleTypeDTO1::getCosCount).summaryStatistics().getSum());

                    hmeVirtualNum.setSelectBy(userId.toString());
                    hmeVirtualNum.setSelectDate(new Date());
                    hmeVirtualNum.setSelectWorkcellId(dto.getWorkcellId());
                    hmeVirtualNum.setEnableFlag("Y");
                    hmeVirtualNumRepository.insertSelective(hmeVirtualNum);
                    if (StringUtils.isNotBlank(dto.getSetsNum()) && Integer.parseInt(dto.getSetsNum()) == (resultMessage + num)) {
                        break;
                    }
                } else if (sum == hmeCosRuleTypeDTO1.stream().mapToLong(HmeCosRuleTypeDTO1::getCosCount).summaryStatistics().getSum()) {
                    break;
                } else {
                    otherList.addAll(result);
                }
            }
            resultMessage += num;
        } while (isFlag && cosTypeList.get(0).getCosCount() < hmeCosRuleTypeDTO1.get(0).getCosCount());

        if (StringUtils.isEmpty(dto.getSetsNum()) || Integer.parseInt(dto.getSetsNum()) > resultMessage) {
            int result = PreSelection1(tenantId, hmePreSelection, limitRule, hmeCosRuleTypeDTO1, hmePreSelectionDTO4s, dto, resultMessage);
            resultMessage += result;
        }


        return String.valueOf(resultMessage);
    }

    @Transactional(rollbackFor = Exception.class)
    public int PreSelection1(Long tenantId, HmePreSelection hmePreSelection, List<HmeCosRuleLogic> hmeCosRuleLogic, List<HmeCosRuleTypeDTO1> hmeCosRuleTypeDTO1, List<HmePreSelectionDTO4> hmePreSelectionDTO4s, HmePreSelectionDTO2 dto, int num1) {
        Long userId = Long.valueOf(-1L);
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        //???????????????????????????
        List<HmeCosFunction> hmeCosFunctions = hmePreSelectionMapper.selectFunction(dto.getMaterialLotIdList());

        //??????
        ArrayList<HmePreSelectionDTO4> hmePreSelectionDTO4Last = hmePreSelectionDTO4s.stream().collect(
                collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getLoadSequence()))),
                        ArrayList::new));
        //????????????
        int num = 0;
        for (int k = 0; k < 1000000; k++) {
            List<HmePreSelectionDTO4> result = new ArrayList<>();
            HmeVirtualNum hmeVirtualNum = new HmeVirtualNum();

            //?????????????????????
            long sum = hmeCosRuleTypeDTO1.stream().mapToLong(HmeCosRuleTypeDTO1::getCosCount).summaryStatistics().getSum();
            List<String> collect10 = hmeCosRuleLogic.stream().map(HmeCosRuleLogic::getRuleValue).collect(Collectors.toList());
            //?????????????????????
            Map<String, Long> mapByMaterialLotCode = hmePreSelectionDTO4Last.stream().collect(Collectors.groupingBy(
                    HmePreSelectionDTO4::getMaterialLotCode, Collectors.counting()
            ));
            //??????
            LinkedHashMap<String, String> mapGroupBy = mapByMaterialLotCode.entrySet().stream().sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getKey, (e1, e2) -> e2, LinkedHashMap::new));


            //????????????
            for (String key : mapGroupBy.keySet()) {
                String materialLotCode = mapGroupBy.get(key);
                //????????????????????????
                List<HmePreSelectionDTO4> mePreSelectionDTO4Bylot = hmePreSelectionDTO4Last.stream().filter(t -> t.getMaterialLotCode().equals(materialLotCode)).collect(Collectors.toList());
                //??????????????????????????????
                List<HmePreSelectionDTO4> mePreSelectionDTO4GroupBy = mePreSelectionDTO4Bylot.stream().sorted(Comparator.comparing(HmePreSelectionDTO4::getLoadRow).thenComparing(HmePreSelectionDTO4::getLoadColumn)).collect(Collectors.toList());

                //?????????????????????
                for (HmePreSelectionDTO4 temp :
                        mePreSelectionDTO4GroupBy) {
                    List<String> newList = new ArrayList<>();
                    boolean flag = true;
                    //?????????????????????
                    List<HmeCosFunction> hmeCosFunctionAll = hmeCosFunctions.stream().filter(t -> t.getLoadSequence().equals(temp.getLoadSequence())).collect(Collectors.toList());
                    //????????????
                    for (int j = 0; j < hmeCosRuleLogic.size(); j++) {
                        //???????????????????????????
                        String current = hmeCosRuleLogic.get(j).getCurrent();
                        //??????????????????????????????????????????
                        List<HmeCosFunction> hmeCosFunction = hmeCosFunctionAll.stream().filter(t -> t.getCurrent().equals(current)).collect(Collectors.toList());

                        //??????
                        if (CollectionUtils.isNotEmpty(hmeCosFunction)) {
                            if ("A".equals(hmeCosRuleLogic.get(j).getCountType())) {
                                //??????
                                double a = 0;
                                if ("A07".equals(hmeCosRuleLogic.get(j).getCollectionItem())) {
                                    if (hmeCosFunction.get(0).getA07() == null) {
                                        flag = false;
                                        break;
                                    } else {
                                        a = hmeCosFunction.get(0).getA07().doubleValue();
                                    }
                                } else if ("A02".equals(hmeCosRuleLogic.get(j).getCollectionItem())) {
                                    if (hmeCosFunction.get(0).getA02() == null) {
                                        flag = false;
                                        break;
                                    } else {
                                        a = hmeCosFunction.get(0).getA02().doubleValue();
                                    }
                                } else if ("A04".equals(hmeCosRuleLogic.get(j).getCollectionItem())) {
                                    if (hmeCosFunction.get(0).getA04() == null) {
                                        flag = false;
                                        break;
                                    } else {
                                        a = hmeCosFunction.get(0).getA04().doubleValue();
                                    }
                                } else if ("A05".equals(hmeCosRuleLogic.get(j).getCollectionItem())) {
                                    if (hmeCosFunction.get(0).getA05() == null) {
                                        flag = false;
                                        break;
                                    } else {
                                        a = hmeCosFunction.get(0).getA05().doubleValue();
                                    }
                                } else if ("A06".equals(hmeCosRuleLogic.get(j).getCollectionItem())) {
                                    if (hmeCosFunction.get(0).getA06() == null) {
                                        flag = false;
                                        break;
                                    } else {
                                        a = hmeCosFunction.get(0).getA06().doubleValue();
                                    }
                                }
                                if (CollectionUtils.isNotEmpty(result)) {
                                    double b = Double.parseDouble(collect10.get(j).split(",")[0]);
                                    double c = Double.parseDouble(collect10.get(j).split(",")[1]);

                                    if (">=".equals(hmeCosRuleLogic.get(j).getRangeType())) {
                                        if (a > b) {
                                            if (a - c >= Double.parseDouble(hmeCosRuleLogic.get(j).getRuleValue())) {
                                                collect10.set(j, a + "," + c);
                                            } else {
                                                flag = false;
                                                break;
                                            }
                                        } else if (a < c) {
                                            if (b - a >= Double.parseDouble(hmeCosRuleLogic.get(j).getRuleValue())) {
                                                collect10.set(j, b + "," + c);
                                            } else {
                                                flag = false;
                                                break;
                                            }
                                        }
                                    } else {
                                        if (a > b) {
                                            if (a - c <= Double.parseDouble(hmeCosRuleLogic.get(j).getRuleValue())) {
                                                collect10.set(j, a + "," + c);

                                            } else {
                                                flag = false;
                                                break;
                                            }
                                        } else if (a < c) {
                                            if (b - a <= Double.parseDouble(hmeCosRuleLogic.get(j).getRuleValue())) {
                                                collect10.set(j, b + "," + c);
                                            } else {
                                                flag = false;
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    collect10.set(j, a + "," + a);
                                }

                            } else {
                                //??????
                                double a = 0;
                                if ("A07".equals(hmeCosRuleLogic.get(j).getCollectionItem())) {
                                    if (hmeCosFunction.get(0).getA07() == null) {
                                        flag = false;
                                        break;
                                    } else {
                                        a = hmeCosFunction.get(0).getA07().doubleValue();
                                    }
                                } else if ("A02".equals(hmeCosRuleLogic.get(j).getCollectionItem())) {
                                    if (hmeCosFunction.get(0).getA02() == null) {
                                        flag = false;
                                        break;
                                    } else {
                                        a = hmeCosFunction.get(0).getA02().doubleValue();
                                    }
                                } else if ("A04".equals(hmeCosRuleLogic.get(j).getCollectionItem())) {
                                    if (hmeCosFunction.get(0).getA04() == null) {
                                        flag = false;
                                        break;
                                    } else {
                                        a = hmeCosFunction.get(0).getA04().doubleValue();
                                    }
                                } else if ("A05".equals(hmeCosRuleLogic.get(j).getCollectionItem())) {
                                    if (hmeCosFunction.get(0).getA05() == null) {
                                        flag = false;
                                        break;
                                    } else {
                                        a = hmeCosFunction.get(0).getA05().doubleValue();
                                    }
                                } else if ("A06".equals(hmeCosRuleLogic.get(j).getCollectionItem())) {
                                    if (hmeCosFunction.get(0).getA06() == null) {
                                        flag = false;
                                        break;
                                    } else {
                                        a = hmeCosFunction.get(0).getA06().doubleValue();
                                    }
                                }

                                double b = Double.parseDouble(collect10.get(j));
                                double c = b / sum;
                                if (">=".equals(hmeCosRuleLogic.get(j).getRangeType())) {
                                    if (a >= c) {
                                        collect10.set(j, String.valueOf(b - a));
                                    } else {
                                        flag = false;
                                        break;
                                    }
                                } else {
                                    if (c >= a) {
                                        collect10.set(j, String.valueOf(b - a));
                                    } else {
                                        flag = false;
                                        break;
                                    }
                                }
                            }
                        } else {
                            flag = false;
                            break;
                        }
                    }
                    //??????-1 ??????????????????-1
                    if (flag) {
                        sum = sum - 1;
                        result.add(temp);
                    }
                    if (sum == 0) {
                        break;
                    }
                }
                if (sum == 0) {
                    break;
                }
            }
            if (sum == 0) {
                String virtualNum;
                if (dto.getIsBind().equals('Y')) {
                    //??????????????????EO???????????????
                    MtEoVO14 mtEoVO14 = new MtEoVO14();
                    mtEoVO14.setEoCount("1");
                    mtEoVO14.setTotalQty(Double.valueOf(dto.getCosNum()));
                    mtEoVO14.setWorkOrderId(dto.getWorkOrderId());
                    List<String> eoids = mtEoRepository.woLimitEoBatchCreate(tenantId, mtEoVO14);
                    virtualNum = eoids.get(0);
                } else {
                    MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
                    mtNumrange.setObjectCode("PICK_NUM");
                    virtualNum = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange).getNumber();
                }
                List<HmeSelectionDetails> insertList = new ArrayList<>();

                for (HmePreSelectionDTO4 temp :
                        result) {
                    HmeSelectionDetails hmeSelectionDetails = new HmeSelectionDetails();
                    hmeSelectionDetails.setPreSelectionId(hmePreSelection.getPreSelectionId());
                    hmeSelectionDetails.setTenantId(tenantId);
                    hmeSelectionDetails.setSiteId(dto.getSiteId());
                    hmeSelectionDetails.setVirtualNum(virtualNum);
                    hmeSelectionDetails.setOldMaterialLotId(temp.getMaterialLotId());
                    hmeSelectionDetails.setOldLoad(temp.getLoadRow() + "," + temp.getLoadColumn());
                    hmeSelectionDetails.setMaterialId(temp.getMaterialId());
                    hmeSelectionDetails.setLoadSequence(temp.getLoadSequence());
                    hmeSelectionDetails.setCosType(temp.getCosType());
                    hmeSelectionDetails.setPower(temp.getA01());
                    hmeSelectionDetails.setAttribute1("NEW");
                    insertList.add(hmeSelectionDetails);
                    hmePreSelectionDTO4Last.removeIf(t -> t.getLoadSequence().equals(temp.getLoadSequence()));
                    hmePreSelectionDTO4s.removeIf(t -> t.getLoadSequence().equals(temp.getLoadSequence()));
                    //?????????????????????
                    HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                    hmeMaterialLotLoad.setLoadSequence(temp.getLoadSequence());
                    HmeMaterialLotLoad hmeMaterialLotLoad1 = hmeMaterialLotLoadMapper.selectOne(hmeMaterialLotLoad);
                    hmeMaterialLotLoad1.setStatus("Y");
                    hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(hmeMaterialLotLoad1);
                }
                num = num + 1;
                hmeSelectionDetailsRepository.batchInsertSelective(insertList);
                hmeVirtualNum.setTenantId(tenantId);
                hmeVirtualNum.setBindFlag(dto.getIsBind());
                hmeVirtualNum.setVirtualNum(virtualNum);
                hmeVirtualNum.setMaterialId(dto.getMaterialId());
                hmeVirtualNum.setProductCode(dto.getProductType());
                hmeVirtualNum.setWorkOrderId(dto.getWorkOrderId());
                hmeVirtualNum.setQuantity(hmeCosRuleTypeDTO1.stream().mapToLong(HmeCosRuleTypeDTO1::getCosCount).summaryStatistics().getSum());

                hmeVirtualNum.setSelectBy(userId.toString());
                hmeVirtualNum.setSelectDate(new Date());
                hmeVirtualNum.setSelectWorkcellId(dto.getWorkcellId());
                hmeVirtualNum.setEnableFlag("Y");
                hmeVirtualNumRepository.insertSelective(hmeVirtualNum);
                if (StringUtils.isNotBlank(dto.getSetsNum()) && Integer.parseInt(dto.getSetsNum()) == num + num1) {
                    break;
                }
            } else if (sum == hmeCosRuleTypeDTO1.stream().mapToLong(HmeCosRuleTypeDTO1::getCosCount).summaryStatistics().getSum()) {
                break;
            } else {
                for (HmePreSelectionDTO4 temp :
                        result) {
                    hmePreSelectionDTO4Last.removeIf(t -> t.getLoadSequence().equals(temp.getLoadSequence()));
                }
            }

        }
        return num;
    }


    /**
     * @param powerRule
     * @param hmePreSelectionDTO4sAll
     * @param hmeCosRuleTypeDTO1
     * @return java.util.List<com.ruike.hme.api.dto.HmeCosRuleTypeDTO1>
     * @description ?????????????????????????????????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/10/10 9:43
     **/
    private List<HmeCosRuleTypeDTO1> getPowerNum(List<HmeCosRuleLogic> powerRule, List<HmePreSelectionDTO4> hmePreSelectionDTO4sAll, HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO1) {
        List<HmeCosRuleTypeDTO1> result = new ArrayList<>();
        List<HmePreSelectionDTO4> hmePreSelectionDTO4s = hmePreSelectionDTO4sAll.stream().filter(t -> t.getCurrent().equals(powerRule.get(0).getCurrent())).collect(Collectors.toList());
        //?????????????????????
        Long cosCount = hmeCosRuleTypeDTO1.getCosCount();
        //?????????????????????
        Map<String, Double> powerCount = hmePreSelectionDTO4s.stream()
                .collect(Collectors.groupingBy(HmePreSelectionDTO4::getA01,
                        Collectors.summingDouble(HmePreSelectionDTO4::getA02)));
        Map<String, Double> powerAvg = hmePreSelectionDTO4s.stream()
                .collect(Collectors.groupingBy(HmePreSelectionDTO4::getA01,
                        Collectors.averagingDouble(HmePreSelectionDTO4::getA02)));
        double powersum1 = hmePreSelectionDTO4s.stream().mapToDouble(HmePreSelectionDTO4::getA02).sum();
        double powersum = Double.parseDouble(powerRule.get(0).getRuleValue());
        double powersumnew = 0d;

        //???????????????????????????
        for (String key :
                powerCount.keySet()) {
            HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO11 = new HmeCosRuleTypeDTO1();
            Double power = powerCount.get(key);
            int v = 0;
            double v1 = cosCount * power / powersum1;
            if (v1 < 1) {
                v = (int) Math.ceil(v1);
            } else {
                v = (int) Math.floor(v1);
            }
            hmeCosRuleTypeDTO11.setMaterialId(hmeCosRuleTypeDTO1.getMaterialId());
            hmeCosRuleTypeDTO11.setCosType(hmeCosRuleTypeDTO1.getCosType());
            hmeCosRuleTypeDTO11.setPowerSinglePoint(key);
            hmeCosRuleTypeDTO11.setCosCount((long) v);
            result.add(hmeCosRuleTypeDTO11);
            powersumnew += powerAvg.get(key) * v;
        }
        result = result.stream().sorted(Comparator.comparing(HmeCosRuleTypeDTO1::getPowerSinglePoint)).collect(Collectors.toList());

        //??????????????????????????????
        result = getStartResult(result, cosCount, powersumnew, powersum, powerAvg);

        //???????????????????????????
        double count = 0d;
        for (HmeCosRuleTypeDTO1 temp :
                result) {
            count += powerAvg.get(temp.getPowerSinglePoint()) * temp.getCosCount();
        }
        while (count > powersum * 1.1) {
            if (result.get(result.size() - 1).getCosCount().equals(cosCount)) {
                break;
            }
            double v = count - powersum;
            for (int i = 0; i < result.size(); i++) {
                if (v > powerAvg.get(result.get(i).getPowerSinglePoint())) {
                    if (result.get(i).getCosCount() > 0) {
                        HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO11 = result.get(i);
                        hmeCosRuleTypeDTO11.setCosCount(hmeCosRuleTypeDTO11.getCosCount() - 1);
                        result.set(i, hmeCosRuleTypeDTO11);
                        HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO12 = result.get(result.size() - 1);
                        hmeCosRuleTypeDTO12.setCosCount(hmeCosRuleTypeDTO12.getCosCount() + 1);
                        result.set(result.size() - 1, hmeCosRuleTypeDTO12);
                        count = count + powerAvg.get(result.get(result.size() - 1).getPowerSinglePoint()) - powerAvg.get(result.get(i).getPowerSinglePoint());
                        break;
                    }
                }
            }
        }
        while (count < powersum) {
            if (result.get(0).getCosCount().equals(cosCount)) {
                break;
            }
            //????????????
            for (int i = result.size() - 1; i >= 0; i--) {
                if (result.get(i).getCosCount() > 0) {
                    HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO11 = result.get(i);
                    hmeCosRuleTypeDTO11.setCosCount(hmeCosRuleTypeDTO11.getCosCount() - 1);
                    result.set(i, hmeCosRuleTypeDTO11);
                    HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO12 = result.get(i - 1);
                    hmeCosRuleTypeDTO12.setCosCount(hmeCosRuleTypeDTO12.getCosCount() + 1);
                    result.set(i - 1, hmeCosRuleTypeDTO12);
                    count = count + powerAvg.get(result.get(i - 1).getPowerSinglePoint()) - powerAvg.get(result.get(i).getPowerSinglePoint());
                    break;
                }
            }
        }
        //??????????????????????????????????????????
        if (powerRule.size() > 1) {
            for (int i = 1; i < powerRule.size(); i++) {
                String current = powerRule.get(i).getCurrent();
                List<HmePreSelectionDTO4> hmePreSelectionDTO4Temp = hmePreSelectionDTO4sAll.stream().filter(t -> t.getCurrent().equals(current)).collect(Collectors.toList());
                Map<String, Double> powerAvgTemp = hmePreSelectionDTO4Temp.stream()
                        .collect(Collectors.groupingBy(HmePreSelectionDTO4::getA01,
                                Collectors.averagingDouble(HmePreSelectionDTO4::getA02)));
                double powersumnewTemp = 0d;
                for (HmeCosRuleTypeDTO1 resultTemp :
                        result) {
                    powersumnewTemp += powerAvgTemp.get(resultTemp.getPowerSinglePoint()) * resultTemp.getCosCount();
                }
                while (powersumnewTemp < Double.parseDouble(powerRule.get(i).getRuleValue())) {
                    if (result.get(0).getCosCount().equals(cosCount)) {
                        break;
                    }
                    //????????????
                    for (int j = result.size() - 1; j >= 0; j--) {
                        if (result.get(j).getCosCount() > 0) {
                            HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO11 = result.get(j);
                            hmeCosRuleTypeDTO11.setCosCount(hmeCosRuleTypeDTO11.getCosCount() - 1);
                            result.set(j, hmeCosRuleTypeDTO11);
                            HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO12 = result.get(j - 1);
                            hmeCosRuleTypeDTO12.setCosCount(hmeCosRuleTypeDTO12.getCosCount() + 1);
                            result.set(j - 1, hmeCosRuleTypeDTO12);
                            powersumnewTemp = powersumnewTemp + powerAvgTemp.get(result.get(j - 1).getPowerSinglePoint()) - powerAvgTemp.get(result.get(j).getPowerSinglePoint());
                            break;
                        }
                    }
                }

            }
        }

        return result;
    }

    /**
     * @param list
     * @param cosCount
     * @param powersumnew
     * @param powersum
     * @param powerAvg
     * @return java.util.List<com.ruike.hme.api.dto.HmeCosRuleTypeDTO1>
     * @description ??????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/10/10 17:03
     **/
    private List<HmeCosRuleTypeDTO1> getStartResult(List<HmeCosRuleTypeDTO1> list, Long cosCount, double powersumnew,
                                                    double powersum, Map<String, Double> powerAvg) {
        //??????????????????
        Long cosCountNew = list.stream().mapToLong(HmeCosRuleTypeDTO1::getCosCount).sum();
        //????????????????????????
        if (cosCount >= cosCountNew) {
            if (powersumnew > powersum) {
                long l = cosCount - cosCountNew;
                HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO11 = list.get(list.size() - 1);
                hmeCosRuleTypeDTO11.setCosCount(hmeCosRuleTypeDTO11.getCosCount() + l);
                list.set(list.size() - 1, hmeCosRuleTypeDTO11);
            } else {
                long l = cosCount - cosCountNew;
                for (int i = 1; i <= l; i++) {
                    HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO11 = list.get(0);
                    hmeCosRuleTypeDTO11.setCosCount(hmeCosRuleTypeDTO11.getCosCount() + 1);
                    powersumnew += powerAvg.get(hmeCosRuleTypeDTO11.getPowerSinglePoint());
                    if (powersumnew > powersum) {
                        list.set(0, hmeCosRuleTypeDTO11);
                        HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO12 = list.get(list.size() - 1);
                        hmeCosRuleTypeDTO12.setCosCount(hmeCosRuleTypeDTO12.getCosCount() + l - i);
                        list.set(list.size() - 1, hmeCosRuleTypeDTO12);
                        break;
                    } else {
                        list.set(0, hmeCosRuleTypeDTO11);
                    }
                }
            }
        } else {    //????????????????????????
            for (int i = list.size() - 1; i >= 0; i--) {
                long l = cosCountNew - cosCount;
                if (l == 0) {
                    break;
                }
                if (list.get(i).getCosCount() > l) {
                    HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO11 = list.get(i);
                    hmeCosRuleTypeDTO11.setCosCount(hmeCosRuleTypeDTO11.getCosCount() - l);
                    list.set(i, hmeCosRuleTypeDTO11);
                    cosCountNew -= l;
                } else {

                    HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO11 = list.get(i);
                    l = l - hmeCosRuleTypeDTO11.getCosCount();
                    hmeCosRuleTypeDTO11.setCosCount(hmeCosRuleTypeDTO11.getCosCount() - l);
                    list.set(i, hmeCosRuleTypeDTO11);
                    cosCountNew -= list.get(i).getCosCount();
                }

            }
        }
        return list;
    }

    /**
     * @param tenantId
     * @param hmePreSelection
     * @param hmeCosRuleLogic
     * @param hmeCosRuleTypeDTO1
     * @param hmePreSelectionDTO4s
     * @param dto
     * @return java.lang.String
     * @description ??????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/23 19:19
     **/
    @Transactional(rollbackFor = Exception.class)
    public String PreSelectionNew(Long tenantId, HmePreSelection hmePreSelection, List<HmeCosRuleLogic> hmeCosRuleLogic, List<HmeCosRuleTypeDTO1> hmeCosRuleTypeDTO1, List<HmePreSelectionDTO4> hmePreSelectionDTO4s, HmePreSelectionDTO2 dto) {
        //??????userId
        Long userId = -1L;
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        int resultMessage = 0;
        //??????????????????????????????
        List<HmeCosRuleLogic> limitRule = hmeCosRuleLogic.stream().filter(t -> "A".equals(t.getCountType()) || "B".equals(t.getCountType())).collect(Collectors.toList());

        //??????????????????????????????????????????????????????
        List<HmeCosRuleTypeDTO1> cosTypeList = new ArrayList<>();
        List<HmeCosRuleLogic> powerRule = hmeCosRuleLogic.stream().filter(t -> "B".equals(t.getCountType()) && "A02".equals(t.getCollectionItem()) && ">=".equals(t.getRangeType())).collect(Collectors.toList());
        boolean isFlag = false;
        if (StringUtils.isEmpty(hmeCosRuleTypeDTO1.get(0).getPowerSinglePoint())) {
            isFlag = true;
            cosTypeList = getPowerNum(powerRule, hmePreSelectionDTO4s, hmeCosRuleTypeDTO1.get(0));
            log.info("<====== ??????{}", cosTypeList);
        } else {
            isFlag = false;
            cosTypeList.addAll(hmeCosRuleTypeDTO1);
        }

        List<HmeCosFunction> hmeCosFunctions = hmePreSelectionMapper.selectFunction(dto.getMaterialLotIdList());

        Double power = 0d;

        boolean filstflag = false;
        do {
            List<HmePreSelectionDTO5> countList = new ArrayList<>();
            List<HmePreSelectionDTO4> otherList = new ArrayList<>();

            if (filstflag) {
                for (int i = cosTypeList.size() - 1; i >= 0; i--) {
                    if (cosTypeList.get(i).getCosCount() > 0) {
                        HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO11 = cosTypeList.get(i);
                        hmeCosRuleTypeDTO11.setCosCount(hmeCosRuleTypeDTO11.getCosCount() - 1);
                        cosTypeList.set(i, hmeCosRuleTypeDTO11);
                        HmeCosRuleTypeDTO1 hmeCosRuleTypeDTO12 = cosTypeList.get(i - 1);
                        hmeCosRuleTypeDTO12.setCosCount(hmeCosRuleTypeDTO12.getCosCount() + 1);
                        cosTypeList.set(i - 1, hmeCosRuleTypeDTO12);
                        break;
                    }
                }
            }

            if (cosTypeList.size() == 1) {
                HmePreSelectionDTO5 hmePreSelectionDTO5 = new HmePreSelectionDTO5();
                hmePreSelectionDTO5.setCurrent(powerRule.get(0).getCurrent());
                hmePreSelectionDTO5.setPowerSinglePoint(cosTypeList.get(0).getPowerSinglePoint());
                hmePreSelectionDTO5.setPower(Double.valueOf(powerRule.get(0).getRuleValue()));
                countList.add(hmePreSelectionDTO5);
                power = Double.valueOf(powerRule.get(0).getRuleValue());
            } else {
                List<HmePreSelectionDTO4> hmePreSelectionDTO4Temp = hmePreSelectionDTO4s.stream().filter(t -> t.getCurrent().equals(powerRule.get(0).getCurrent())).collect(Collectors.toList());
                Map<String, Double> powerAvg = hmePreSelectionDTO4Temp.stream()
                        .collect(Collectors.groupingBy(HmePreSelectionDTO4::getA01,
                                Collectors.averagingDouble(HmePreSelectionDTO4::getA02)));
                for (HmeCosRuleTypeDTO1 cosTypeTemp :
                        cosTypeList) {
                    HmePreSelectionDTO5 hmePreSelectionDTO5 = new HmePreSelectionDTO5();
                    hmePreSelectionDTO5.setCurrent(powerRule.get(0).getCurrent());
                    hmePreSelectionDTO5.setPowerSinglePoint(cosTypeTemp.getPowerSinglePoint());
                    hmePreSelectionDTO5.setPower(powerAvg.get(cosTypeTemp.getPowerSinglePoint()) * cosTypeTemp.getCosCount());
                    countList.add(hmePreSelectionDTO5);
                    power += hmePreSelectionDTO5.getPower();
                }
            }
            filstflag = true;
            //??????????????????????????????
            List<HmeCosRuleTypeDTO1> limitRuleResult = cosTypeList.stream().filter(t -> !t.getCosCount().equals(0L)).collect(Collectors.toList());
            log.info("<====== ??????????????????????????????{}", limitRuleResult);

            //????????????????????????????????????
            Set<String> powerSinglePoints = limitRuleResult.stream()
                    .map(HmeCosRuleTypeDTO1::getPowerSinglePoint)
                    .collect(Collectors.toSet());
            List<HmePreSelectionDTO4> hmePreSelectionDTO4Power = hmePreSelectionDTO4s.stream().filter(t -> powerSinglePoints.contains(t.getA01())).collect(Collectors.toList());
            //??????
            ArrayList<HmePreSelectionDTO4> hmePreSelectionDTO4Last = hmePreSelectionDTO4Power.stream().collect(
                    collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getLoadSequence()))),
                            ArrayList::new));

            int num = 0;
            int all = 1000000;
            if (StringUtils.isNotBlank(dto.getSetsNum()) && Integer.parseInt(dto.getSetsNum()) == resultMessage) {
                break;
            }
            for (int i = 0; i < all; i++) {
                List<HmePreSelectionDTO4> result = new ArrayList<>();
                HmeVirtualNum hmeVirtualNum = new HmeVirtualNum();
                //??????????????????????????????
                long sum = hmeCosRuleTypeDTO1.stream().mapToLong(HmeCosRuleTypeDTO1::getCosCount).summaryStatistics().getSum();

                List<String> collect10 = limitRule.stream().map(HmeCosRuleLogic::getRuleValue).collect(Collectors.toList());

                Double powerSum = 0d;

                //????????????
                for (HmeCosRuleTypeDTO1 hmeCosRuleType :
                        limitRuleResult) {
                    //?????????????????????????????????
                    Double powerTemp = countList.stream().filter(t -> t.getPowerSinglePoint().equals(hmeCosRuleType.getPowerSinglePoint())).collect(Collectors.toList()).get(0).getPower() - (Double.valueOf(powerRule.get(0).getRuleValue()) - powerSum);
                    //???????????????????????????
                    List<HmePreSelectionDTO4> cosList = hmePreSelectionDTO4Last.stream().filter(t -> t.getA01().equals(hmeCosRuleType.getPowerSinglePoint())).collect(Collectors.toList());
                    //
                    int typeNum = 0;
                    //????????????
                    for (HmePreSelectionDTO4 cosTemp :
                            cosList) {
                        boolean flag = true;
                        List<String> collect10Temp = new ArrayList<>();
                        //?????????????????????
                        List<HmeCosFunction> hmeCosFunctionAll = hmeCosFunctions.stream().filter(t -> t.getLoadSequence().equals(cosTemp.getLoadSequence())).collect(Collectors.toList());
                        //?????????????????????
                        Double rulePower = 0D;

                        //????????????
                        for (int j = 0; j < limitRule.size(); j++) {

                            //???????????????????????????
                            String current = limitRule.get(j).getCurrent();
                            List<HmeCosFunction> hmeCosFunction = hmeCosFunctionAll.stream().filter(t -> t.getCurrent().equals(current)).collect(Collectors.toList());
                            //??????
                            if (CollectionUtils.isNotEmpty(hmeCosFunction)) {
                                if ("A".equals(limitRule.get(j).getCountType())) {
                                    //??????
                                    if (CollectionUtils.isNotEmpty(result)) {
                                        double a = 0;
                                        if ("A07".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA07() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA07().doubleValue();
                                            }
                                        } else if ("A02".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA02() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA02().doubleValue();
                                            }
                                        } else if ("A04".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA04() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA04().doubleValue();
                                            }
                                        } else if ("A05".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA05() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA05().doubleValue();
                                            }
                                        } else if ("A06".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA06() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA06().doubleValue();
                                            }
                                        }
                                        double b = Double.parseDouble(collect10.get(j).split(",")[0]);
                                        double c = Double.parseDouble(collect10.get(j).split(",")[1]);

                                        if (">=".equals(limitRule.get(j).getRangeType())) {
                                            if (a > b) {
                                                if (a - c >= Double.parseDouble(limitRule.get(j).getRuleValue())) {
                                                    collect10Temp.add(a + "," + c);
                                                } else {
                                                    flag = false;
                                                    break;
                                                }
                                            } else if (a < c) {
                                                if (b - a >= Double.parseDouble(limitRule.get(j).getRuleValue())) {
                                                    collect10Temp.add(b + "," + a);
                                                } else {
                                                    flag = false;
                                                    break;
                                                }
                                            } else {
                                                collect10Temp.add(b + "," + c);
                                            }
                                        } else {
                                            if (a > b) {
                                                if (a - c <= Double.parseDouble(limitRule.get(j).getRuleValue())) {
                                                    collect10Temp.add(a + "," + c);
                                                } else {
                                                    flag = false;
                                                    break;
                                                }
                                            } else if (a < c) {
                                                if ((b - a) <= Double.parseDouble(limitRule.get(j).getRuleValue())) {
                                                    collect10Temp.add(b + "," + a);
                                                } else {
                                                    flag = false;
                                                    break;
                                                }
                                            } else {
                                                collect10Temp.add(b + "," + c);
                                            }
                                        }
                                    } else {
                                        double a = 0;
                                        if ("A07".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA07() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA07().doubleValue();
                                            }
                                        } else if ("A02".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA02() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA02().doubleValue();
                                            }
                                        } else if ("A04".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA04() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA04().doubleValue();
                                            }
                                        } else if ("A05".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA05() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA05().doubleValue();
                                            }
                                        } else if ("A06".equals(limitRule.get(j).getCollectionItem())) {
                                            if (hmeCosFunction.get(0).getA06() == null) {
                                                flag = false;
                                                break;
                                            } else {
                                                a = hmeCosFunction.get(0).getA06().doubleValue();
                                            }
                                        }
                                        collect10Temp.add(a + "," + a);
                                    }
                                } else {
                                    //??????
                                    double a = 0;
                                    if ("A07".equals(limitRule.get(j).getCollectionItem())) {
                                        if (hmeCosFunction.get(0).getA07() == null) {
                                            flag = false;
                                            break;
                                        } else {
                                            a = hmeCosFunction.get(0).getA07().doubleValue();
                                        }
                                    } else if ("A02".equals(limitRule.get(j).getCollectionItem())) {
                                        if (hmeCosFunction.get(0).getA02() == null) {
                                            flag = false;
                                            break;
                                        } else {
                                            a = hmeCosFunction.get(0).getA02().doubleValue();
                                        }
                                    } else if ("A04".equals(limitRule.get(j).getCollectionItem())) {
                                        if (hmeCosFunction.get(0).getA04() == null) {
                                            flag = false;
                                            break;
                                        } else {
                                            a = hmeCosFunction.get(0).getA04().doubleValue();
                                        }
                                    } else if ("A05".equals(limitRule.get(j).getCollectionItem())) {
                                        if (hmeCosFunction.get(0).getA05() == null) {
                                            flag = false;
                                            break;
                                        } else {
                                            a = hmeCosFunction.get(0).getA05().doubleValue();
                                        }
                                    } else if ("A06".equals(limitRule.get(j).getCollectionItem())) {
                                        if (hmeCosFunction.get(0).getA06() == null) {
                                            flag = false;
                                            break;
                                        } else {
                                            a = hmeCosFunction.get(0).getA06().doubleValue();
                                        }
                                    }

                                    if (isFlag && "A02".equals(limitRule.get(j).getCollectionItem()) && ">=".equals(limitRule.get(j).getRangeType())) {
                                        double c = powerTemp / hmeCosRuleType.getCosCount() - typeNum;
                                        if (a >= c) {
                                            rulePower = a;
                                            collect10Temp.add(String.valueOf(powerTemp - a));
                                        } else {
                                            flag = false;
                                            break;
                                        }
                                    } else {
                                        double b = Double.parseDouble(collect10.get(j));
                                        double c = b / sum;
                                        if (">=".equals(limitRule.get(j).getRangeType())) {
                                            if (a >= c) {
                                                collect10Temp.add(String.valueOf(b - a));
                                            } else {
                                                flag = false;
                                                break;
                                            }
                                        } else {
                                            if (c >= a) {
                                                collect10Temp.add(String.valueOf(b - a));
                                            } else {
                                                flag = false;
                                                break;
                                            }
                                        }
                                    }
                                }
                            } else {
                                flag = false;
                                break;
                            }
                        }
                        //??????-1 ??????????????????-1
                        if (flag) {
                            typeNum += 1;
                            collect10.clear();
                            collect10.addAll(collect10Temp);
                            sum -= 1;
                            result.add(cosTemp);
                            powerSum += rulePower;
                        }
                        if (typeNum == hmeCosRuleType.getCosCount()) {
                            break;
                        }
                    }
                    if (typeNum < hmeCosRuleType.getCosCount()) {
                        break;
                    }
                }
                if (sum == 0) {
                    String virtualNum;
                    if (dto.getIsBind().equals('Y')) {
                        //??????????????????EO???????????????
                        MtEoVO14 mtEoVO14 = new MtEoVO14();
                        mtEoVO14.setEoCount("1");
                        mtEoVO14.setTotalQty(Double.valueOf(dto.getCosNum()));
                        mtEoVO14.setWorkOrderId(dto.getWorkOrderId());
                        List<String> eoids = mtEoRepository.woLimitEoBatchCreate(tenantId, mtEoVO14);
                        virtualNum = eoids.get(0);
                    } else {
                        MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
                        mtNumrange.setObjectCode("PICK_NUM");
                        virtualNum = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange).getNumber();
                    }
                    List<HmeSelectionDetails> insertList = new ArrayList<>();

                    for (HmePreSelectionDTO4 temp :
                            result) {
                        HmeSelectionDetails hmeSelectionDetails = new HmeSelectionDetails();
                        hmeSelectionDetails.setPreSelectionId(hmePreSelection.getPreSelectionId());
                        hmeSelectionDetails.setTenantId(tenantId);
                        hmeSelectionDetails.setSiteId(dto.getSiteId());
                        hmeSelectionDetails.setVirtualNum(virtualNum);
                        hmeSelectionDetails.setOldMaterialLotId(temp.getMaterialLotId());
                        hmeSelectionDetails.setOldLoad(temp.getLoadRow() + "," + temp.getLoadColumn());
                        hmeSelectionDetails.setMaterialId(temp.getMaterialId());
                        hmeSelectionDetails.setLoadSequence(temp.getLoadSequence());
                        hmeSelectionDetails.setCosType(temp.getCosType());
                        hmeSelectionDetails.setPower(temp.getA01());
                        hmeSelectionDetails.setAttribute1("NEW");
                        insertList.add(hmeSelectionDetails);
                        hmePreSelectionDTO4Last.removeIf(t -> t.getLoadSequence().equals(temp.getLoadSequence()));
                        hmePreSelectionDTO4s.removeIf(t -> t.getLoadSequence().equals(temp.getLoadSequence()));
                        //?????????????????????
                        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                        hmeMaterialLotLoad.setLoadSequence(temp.getLoadSequence());
                        HmeMaterialLotLoad hmeMaterialLotLoad1 = hmeMaterialLotLoadMapper.selectOne(hmeMaterialLotLoad);
                        hmeMaterialLotLoad1.setStatus("Y");
                        hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(hmeMaterialLotLoad1);
                    }
                    num = num + 1;
                    hmeSelectionDetailsRepository.batchInsertSelective(insertList);
                    hmeVirtualNum.setTenantId(tenantId);
                    hmeVirtualNum.setBindFlag(dto.getIsBind());
                    hmeVirtualNum.setVirtualNum(virtualNum);
                    hmeVirtualNum.setMaterialId(dto.getMaterialId());
                    hmeVirtualNum.setProductCode(dto.getProductType());
                    hmeVirtualNum.setWorkOrderId(dto.getWorkOrderId());
                    hmeVirtualNum.setQuantity(hmeCosRuleTypeDTO1.stream().mapToLong(HmeCosRuleTypeDTO1::getCosCount).summaryStatistics().getSum());

                    hmeVirtualNum.setSelectBy(userId.toString());
                    hmeVirtualNum.setSelectDate(new Date());
                    hmeVirtualNum.setSelectWorkcellId(dto.getWorkcellId());
                    hmeVirtualNum.setEnableFlag("Y");
                    hmeVirtualNumRepository.insertSelective(hmeVirtualNum);
                    if (StringUtils.isNotBlank(dto.getSetsNum()) && Integer.parseInt(dto.getSetsNum()) == (resultMessage + num)) {
                        break;
                    }
                } else if (sum == hmeCosRuleTypeDTO1.stream().mapToLong(HmeCosRuleTypeDTO1::getCosCount).summaryStatistics().getSum()) {
                    break;
                } else {
                    otherList.addAll(result);
                }
            }
            resultMessage += num;
        } while (isFlag && cosTypeList.get(0).getCosCount() < hmeCosRuleTypeDTO1.get(0).getCosCount());

        if (StringUtils.isEmpty(dto.getSetsNum()) || Integer.parseInt(dto.getSetsNum()) > resultMessage) {
            int result = PreSelection1(tenantId, hmePreSelection, limitRule, hmeCosRuleTypeDTO1, hmePreSelectionDTO4s, dto, resultMessage);
            resultMessage += result;
        }

        return String.valueOf(resultMessage);
    }

    /**
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.api.dto.HmePreSelectionReturnDTO2
     * @description ??????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/30 14:01
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmePreSelectionReturnDTO2 confirmNew(Long tenantId, HmePreSelectionDTO1 dto) {
        log.info("<====hmePreSelectionService-confirmNew.start:??????????????????");
        long startDate = System.currentTimeMillis();
        //?????????
        HmePreSelectionReturnDTO2 hmePreSelectionReturnDTO2 = new HmePreSelectionReturnDTO2();
        //?????????????????????
        HmeCosRuleHead hmeCosRuleHead = hmeCosRuleHeadRepository.selectByPrimaryKey(dto.getCosRuleId());
        //???????????????????????????????????????
        HmePreSelection hmePreSelection = new HmePreSelection();
        if (StringUtils.isNotBlank(dto.getSelectLot())) {
            List<HmePreSelection> hmePreSelections = hmePreSelectionRepository.selectByCondition(Condition.builder(HmePreSelection.class)
                    .andWhere(Sqls.custom().andEqualTo(HmePreSelection.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmePreSelection.FIELD_SITE_ID, dto.getSiteId())
                            .andEqualTo(HmePreSelection.FIELD_PRE_SELECTION_LOT, dto.getSelectLot())).build());
            //???????????????????????????
            if (CollectionUtils.isEmpty(hmePreSelections)) {
                throw new MtException("HME_SELECT_004", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_SELECT_004", "HME", dto.getSelectLot()));
            } else {
                //???????????????????????????
                if (!hmePreSelections.get(0).getAttribute1().equals(hmeCosRuleHead.getCosRuleCode())) {
                    throw new MtException("HME_SELECT_005", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_SELECT_005", "HME"));
                } else {
                    hmePreSelection = hmePreSelections.get(0);
                }
            }
        }
        //2021-05-18 10:17 edit by chaonan.hu for zhenyong.ban ?????????????????????,??????????????????
        long startDateWorkcell = System.currentTimeMillis();
        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(dto.getWorkcellId());
        if(mtModWorkcell.getWorkcellCode().length() < 6){
            //????????????????????????6?????????
            throw new MtException("HME_COS_058", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_058", "HME"));
        }
        String subWorkcellCode = mtModWorkcell.getWorkcellCode().substring(mtModWorkcell.getWorkcellCode().length() - 6);
        Pattern p = Pattern.compile("[0-9a-zA-Z]{1,}");
        Matcher m = p.matcher(subWorkcellCode);
        if(!m.matches()){
            //??????????????????6???????????????????????????????????????????????????
            throw new MtException("HME_COS_058", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_COS_058", "HME"));
        }
        long endDate2 = System.currentTimeMillis();
        log.info("<====hmePreSelectionService-confirmNew:??????????????????????????????{}??????", (endDate2 - startDateWorkcell));
        log.info("<====hmePreSelectionService-confirmNew:???????????????????????????{}??????", (endDate2 - startDate));
        //????????????????????????
        List<MtMaterialLot> mtMaterialLots = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, dto.getMaterialLotIdList());
        long endDate3 = System.currentTimeMillis();
        log.info("<====hmePreSelectionService-confirmNew:???????????????{}???????????????,????????????{}??????", dto.getMaterialLotIdList().size(), (endDate3 - endDate2));
        //??????????????????
        for (MtMaterialLot temp :
                mtMaterialLots) {
            HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
            hmeObjectRecordLockDTO.setFunctionName("?????????");
            hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
            hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
            hmeObjectRecordLockDTO.setObjectRecordId(temp.getMaterialLotId());
            hmeObjectRecordLockDTO.setObjectRecordCode(temp.getMaterialLotCode());
            HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
            //??????
            hmeObjectRecordLockRepository.lock(hmeObjectRecordLock);
        }
        long endDate4 = System.currentTimeMillis();
        log.info("<====hmePreSelectionService-confirmNew:????????????{}???????????????,????????????{}??????", mtMaterialLots.size(), (endDate4 - endDate3));
        try {

            //???????????????????????????
            List<HmeCosRuleLogic> hmeCosRuleLogic = hmeCosRuleLogicRepository.selectByCondition(Condition.builder(HmeCosRuleLogic.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeCosRuleLogic.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeCosRuleLogic.FIELD_SITE_ID, dto.getSiteId())
                            .andEqualTo(HmeCosRuleLogic.FIELD_COS_RULE_ID, dto.getCosRuleId())).build());
            //??????????????????????????????
            List<HmeCosRuleLogic> limitRule = hmeCosRuleLogic.stream().filter(t -> t.getCountType().equals("C") || t.getCountType().equals("D")).collect(Collectors.toList());
            //2021-06-21 edit by chaonan.hu for hui.ma
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????,???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            Map<String, List<HmeCosRuleLogic>> limitRuleMap = limitRule.stream().collect(Collectors.groupingBy(t -> {
                return t.getCountType() + "," + t.getCurrent() + "," + t.getCollectionItem();
            }));
            List<HmePreSelectionDTO8> hmePreSelectionDTO8List = new ArrayList<>();
            for (Map.Entry<String, List<HmeCosRuleLogic>> entry:limitRuleMap.entrySet()) {
                HmePreSelectionDTO8 hmePreSelectionDTO8 = new HmePreSelectionDTO8();
                hmePreSelectionDTO8.setCountType(entry.getValue().get(0).getCountType());
                hmePreSelectionDTO8.setCurrent(entry.getValue().get(0).getCurrent());
                hmePreSelectionDTO8.setCollectionItem(entry.getValue().get(0).getCollectionItem());
                List<HmePreSelectionDTO9> hmePreSelectionDTO9List = new ArrayList<>();
                for (HmeCosRuleLogic cosRuleLogic:entry.getValue()) {
                    HmePreSelectionDTO9 hmePreSelectionDTO9 = new HmePreSelectionDTO9();
                    hmePreSelectionDTO9.setRangeType(cosRuleLogic.getRangeType());
                    hmePreSelectionDTO9.setRuleValue(cosRuleLogic.getRuleValue());
                    hmePreSelectionDTO9List.add(hmePreSelectionDTO9);
                }
                hmePreSelectionDTO8.setHmePreSelectionDTO9List(hmePreSelectionDTO9List);
                hmePreSelectionDTO8List.add(hmePreSelectionDTO8);
            }
            long endDate5 = System.currentTimeMillis();
            log.info("<====hmePreSelectionService-confirmNew:????????????????????????????????????,????????????{}??????", (endDate5 - endDate4));

            //??????????????????
            List<HmeCosRuleTypeDTO1> hmeCosRuleTypeDTO1 = hmeCosRuleTypeMapper.selectRule(dto.getCosRuleId());
            long endDate6 = System.currentTimeMillis();
            log.info("<====hmePreSelectionService-confirmNew:???????????????????????????,????????????{}??????", (endDate6 - endDate5));

            //??????????????????????????????????????????
            List<HmePreSelectionDTO4> hmePreSelectionDTO4s = new ArrayList<>();
            //?????????HME.COS_SELECTION_FUNCTION_JOB?????????ORIGINAL?????????????????????Y??????cos_function????????????????????????cos_function_selection????????????
            String originalMeaning = lovAdapter.queryLovMeaning("HME.COS_SELECTION_FUNCTION_JOB", tenantId, "ORIGINAL");
            if(YES.equals(originalMeaning)){
                hmePreSelectionDTO4s = hmePreSelectionMapper.queryMaterialLotNew(tenantId, dto.getMaterialLotIdList(), hmeCosRuleTypeDTO1.get(0), hmePreSelectionDTO8List);
            }else {
                hmePreSelectionDTO4s = hmePreSelectionMapper.queryMaterialLotNew2(tenantId, dto.getMaterialLotIdList(), hmeCosRuleTypeDTO1.get(0), hmePreSelectionDTO8List);
            }
            long endDate7 = System.currentTimeMillis();
            log.info("<====hmePreSelectionService-confirmNew:???????????????????????????????????????????????????,????????????{}??????", (endDate7 - endDate6));

            if (StringUtils.isEmpty(dto.getSelectLot())) {
                //?????????????????? 2021/09/15 10:30 edit by chaonan.hu for peng.zhao ???????????????????????????API,???????????????????????????????????????????????????????????????
                MtNumrangeVO9 mtNumrangeVO9 = new MtNumrangeVO9();
                mtNumrangeVO9.setObjectCode("PICK_BATCH");
                MtNumrangeVO11 mtNumrangeVO11 = new MtNumrangeVO11();
                mtNumrangeVO11.setSequence(0L);
                List<String> valList = new ArrayList<>();
                valList.add(subWorkcellCode);
                mtNumrangeVO11.setIncomingValue(valList);
                mtNumrangeVO9.setIncomingValueList(Collections.singletonList(mtNumrangeVO11));
                mtNumrangeVO9.setObjectNumFlag("Y");
                mtNumrangeVO9.setNumQty(1L);
                MtNumrangeVO8 mtNumrangeVO8 = mtNumrangeRepository.numrangeBatchGenerate(tenantId, mtNumrangeVO9);
                String preSelectionLot = mtNumrangeVO8.getNumberList().get(0);
                long endDate8 = System.currentTimeMillis();
                log.info("<====hmePreSelectionService-confirmNew:???????????????????????????,????????????{}??????", (endDate8 - endDate7));

                //????????????
                hmePreSelection.setTenantId(tenantId);
                hmePreSelection.setSiteId(dto.getSiteId());
                hmePreSelection.setPreSelectionLot(preSelectionLot);
                hmePreSelection.setStatus("NEW");
                hmePreSelection.setSetsNum(0l);
                if (dto.getIsBind().equals('Y')) {
                    hmePreSelection.setWorkOrderId(dto.getWorkOrderId());
                }
                hmePreSelection.setAttribute1(hmeCosRuleHead.getCosRuleCode());
                hmePreSelection.setAttribute2(hmeCosRuleHead.getProductType());
                hmePreSelection.setAttribute3(hmeCosRuleHead.getMaterialId());
                hmePreSelectionRepository.insertSelective(hmePreSelection);
                long endDate9 = System.currentTimeMillis();
                log.info("<====hmePreSelectionService-confirmNew:?????????????????????????????????,????????????{}??????", (endDate9 - endDate8));
            }

            long startDate10 = System.currentTimeMillis();
            String s = preSelectionNew(tenantId, hmePreSelection, hmeCosRuleLogic, hmeCosRuleTypeDTO1, dto, hmePreSelectionDTO4s, startDate);
            long endDate10 = System.currentTimeMillis();
            log.info("<====hmePreSelectionService-confirmNew:???????????????????????????,????????????{}??????", (endDate10 - startDate10));

            //???????????????s???
            if (Long.valueOf(s) > 0) {
                if (StringUtils.isNotBlank(dto.getSelectLot())) {
                    //?????????????????????????????????????????????
                    hmePreSelection.setSetsNum(hmePreSelection.getSetsNum() + Long.parseLong(s));

                } else {
                    //?????????????????????
                    hmePreSelection.setSetsNum(Long.valueOf(s));
                }
                hmePreSelectionMapper.updateByPrimaryKeySelective(hmePreSelection);
                long endDate11 = System.currentTimeMillis();
                log.info("<====hmePreSelectionService-confirmNew:?????????????????????????????????,????????????{}??????", (endDate11 - endDate10));
                hmePreSelectionReturnDTO2.setCosNum(hmeCosRuleTypeDTO1.get(0).getCosCount()*Long.parseLong(s));
                hmePreSelectionReturnDTO2.setNum(s);
                hmePreSelectionReturnDTO2.setPreSelectionLot(hmePreSelection.getPreSelectionLot());
            } else {
                if (StringUtils.isEmpty(dto.getSelectLot())) {
                    hmePreSelectionReturnDTO2.setCosNum(0l);
                    hmePreSelectionReturnDTO2.setNum(s);
                    hmePreSelectionMapper.deleteByPrimaryKey(hmePreSelection.getPreSelectionId());
                    long endDate11 = System.currentTimeMillis();
                    log.info("<====hmePreSelectionService-confirmNew:?????????????????????????????????,????????????{}??????", (endDate11 - endDate10));
                }
            }
        } catch (Exception e) {
            log.info("?????????????????????" + e.getMessage());
            throw new CommonException(e);
        } finally {
            long startDate12 = System.currentTimeMillis();
            //??????
            for (MtMaterialLot temp :
                    mtMaterialLots) {
                HmeObjectRecordLockDTO hmeObjectRecordLockDTO = new HmeObjectRecordLockDTO();
                hmeObjectRecordLockDTO.setFunctionName("?????????");
                hmeObjectRecordLockDTO.setDeviceCode(HmeConstants.PlatformType.PC);
                hmeObjectRecordLockDTO.setObjectType(HmeConstants.LockObjectType.MATERIAL_LOT);
                hmeObjectRecordLockDTO.setObjectRecordId(temp.getMaterialLotId());
                hmeObjectRecordLockDTO.setObjectRecordCode(temp.getMaterialLotCode());
                HmeObjectRecordLock hmeObjectRecordLock = hmeObjectRecordLockService.getRecordLock(tenantId, hmeObjectRecordLockDTO);
                //??????
                hmeObjectRecordLockRepository.releaseLock(hmeObjectRecordLock, HmeConstants.ConstantValue.YES);
            }
            long endDate12 = System.currentTimeMillis();
            log.info("<====hmePreSelectionService-confirmNew:???????????????{}?????????,????????????{}??????", mtMaterialLots.size(), (endDate12 - startDate12));
        }
        long endDate = System.currentTimeMillis();
        log.info("<====hmePreSelectionService-confirmNew.end:?????????????????????{}??????", (endDate - startDate));
        return hmePreSelectionReturnDTO2;
    }

    @Override
    public String qtyQueryByContainer(Long tenantId, String containerCode) {
        return hmePreSelectionMapper.qtyQueryByContainer(tenantId, containerCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(Long tenantId, HmePreSelectionDTO7 dto) {
        //??????????????????????????????
        String eventRequestCreateId = mtEventRequestRepository.eventRequestCreate(tenantId, "LOCATOR_TRANSFER_REQUISTION");

        //????????????????????????
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventRequestId(eventRequestCreateId);
        eventCreateVO.setEventTypeCode("LOCATOR_TRANSFER");
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

        //??????????????????id
        List<String> parentLocatorIds = mtModLocatorRepository.parentLocatorQuery(tenantId, dto.getLocatorId(), "FIRST");
        if (CollectionUtils.isEmpty(parentLocatorIds)) {
            throw new MtException("WMS_LOCATOR_TRANSFER_0006",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "WMS_LOCATOR_TRANSFER_0006", "WMS"));
        }
        String parentLocatorId = parentLocatorIds.get(0);
        //????????????????????????
        MtModLocator parentLocator = new MtModLocator();
        parentLocator.setLocatorId(parentLocatorId);
        parentLocator = mtModLocatorRepository.selectByPrimaryKey(parentLocator);
        //????????????????????????
        MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
        materialLotVo3.setMaterialLotCode(dto.getMaterialLotCode());
        List<String> materialLotIds =
                mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
        if(CollectionUtils.isEmpty(materialLotIds))
        {
            throw new MtException("HME_LOAD_CONTAINER_001",
                    mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_LOAD_CONTAINER_001", "HME"));

        }
        WmsLocatorTransferVO wmsLocatorTransferVO = hmePreSelectionMapper.selectMaterialLotInfo(tenantId, materialLotIds.get(0));

        List<WmsObjectTransactionRequestVO> objectTransactionRequestList = new ArrayList<>();
        WmsObjectTransactionRequestVO transactionRequestDTO = new WmsObjectTransactionRequestVO();
        transactionRequestDTO.setEventId(eventId);
        transactionRequestDTO.setMaterialLotId(wmsLocatorTransferVO.getMaterialLotId());
        transactionRequestDTO.setMaterialId(wmsLocatorTransferVO.getMaterialId());
        transactionRequestDTO.setMaterialCode(wmsLocatorTransferVO.getMaterialCode());
        transactionRequestDTO.setTransactionQty(BigDecimal.valueOf(Optional.ofNullable(wmsLocatorTransferVO.getPrimaryUomQty()).orElse(0D)));
        transactionRequestDTO.setLotNumber(wmsLocatorTransferVO.getLot());
        transactionRequestDTO.setTransactionUom(wmsLocatorTransferVO.getPrimaryUomCode());
        transactionRequestDTO.setPlantId(wmsLocatorTransferVO.getSiteId());
        transactionRequestDTO.setWarehouseId(wmsLocatorTransferVO.getWarehouseId());
        transactionRequestDTO.setLocatorId(wmsLocatorTransferVO.getLocatorId());
        transactionRequestDTO.setTransferPlantId(wmsLocatorTransferVO.getSiteId());
        transactionRequestDTO.setTransferWarehouseId(parentLocator.getLocatorId());
        transactionRequestDTO.setTransferLocatorId(dto.getLocatorId());
        transactionRequestDTO.setSupplierCode(wmsLocatorTransferVO.getSupplierCode());
        transactionRequestDTO.setSupplierSiteCode(wmsLocatorTransferVO.getSupplierSiteCode());
        transactionRequestDTO.setMergeFlag(MtBaseConstants.NO);
        transactionRequestDTO.setTransactionTime(new Date());
        transactionRequestDTO.setBarcode(wmsLocatorTransferVO.getMaterialLotCode());
        transactionRequestDTO.setTransactionTypeCode("WMS_LOCATOR_TRAN");
        transactionRequestDTO.setTransactionReasonCode("??????????????????????????????");

        MtMaterialLotVO9 lotDto = new MtMaterialLotVO9();
        lotDto.setMaterialLotId(wmsLocatorTransferVO.getMaterialLotId());
        lotDto.setTargetSiteId(wmsLocatorTransferVO.getSiteId());
        lotDto.setTargetLocatorId(dto.getLocatorId());
        lotDto.setEventRequestId(eventRequestCreateId);
        mtMaterialLotRepository.materialLotTransfer(tenantId, lotDto);

        objectTransactionRequestList.add(transactionRequestDTO);

        //????????????????????????
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, objectTransactionRequestList);
    }

    @Override
    @ProcessLovValue
    public Page<HmePreSelectionReturnDTO12> recallDataQuery(Long tenantId, HmePreSelectionReturnDTO11 dto, PageRequest pageRequest) {
        Page<HmePreSelectionReturnDTO12> resultPage = PageHelper.doPage(pageRequest, () -> hmePreSelectionMapper.recallDataQuery(tenantId, dto));
        if(CollectionUtils.isNotEmpty(resultPage)){
            int i = 1;
            //long count = resultPage.stream().filter(t -> t.getVirtualNum().equals(resultPage.get(0).getVirtualNum())).count();
            //long j = count;
            String virtualNum = resultPage.get(0).getVirtualNum();
            for (HmePreSelectionReturnDTO12 temp :
                    resultPage) {
                if (StringUtils.isNotBlank(temp.getOldLoad())) {
                    String[] split = temp.getOldLoad().split(",");
                    if (split.length != 2) {
                        continue;
                    }
                    temp.setOldLoad((char) (64 + Integer.parseInt(split[0])) + split[1]);
                }
                if (StringUtils.isNotBlank(temp.getTargetLoad())) {
                    String[] split1 = temp.getTargetLoad().split(",");
                    if (split1.length != 2) {
                        continue;
                    }
                    temp.setTargetLoad((char) (64 + Integer.parseInt(split1[0])) + split1[1]);
                }
                if (virtualNum.equals(temp.getVirtualNum())) {
                    //temp.setWays("COS" + String.format("%02d", j));
                    temp.setDeviceNumber(String.valueOf(i));
                    //j--;
                } else {
                    //j = count;
                    i++;
                    virtualNum = temp.getVirtualNum();
                    //temp.setWays("COS" + String.format("%02d", j));
                    temp.setDeviceNumber(String.valueOf(i));
                    //j--;
                }
            }
        }
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmePreSelectionReturnDTO13 recallData(Long tenantId, HmePreSelectionReturnDTO13 dto) {
        if(CollectionUtils.isEmpty(dto.getVirtualNumList())){
            throw new MtException("HME_NC_0006",mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "?????????"));
        }
        List<String> virtualNumList = dto.getVirtualNumList().stream().distinct().collect(Collectors.toList());
        List<HmePreSelectionVO> gpDelRecordList = new ArrayList<>();
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        Date now = new Date();
        for (String virtualNum:virtualNumList) {
            //?????????hme_pre_selection???sets_num, ???????????????????????????????????????1, ???????????????0?????????status?????????CLOSED
            String preSelectionId = hmePreSelectionMapper.getPreSelectionIdByVirtualNum(tenantId, virtualNum);
            HmePreSelection hmePreSelection = hmePreSelectionRepository.selectByPrimaryKey(preSelectionId);
            hmePreSelection.setSetsNum(hmePreSelection.getSetsNum() - 1);
            if(hmePreSelection.getSetsNum() <= 0){
                hmePreSelection.setStatus("CLOSED");
            }
            //?????????hme_material_lot_load???status??????
            List<HmeMaterialLotLoad> hmeMaterialLotLoads = hmePreSelectionMapper.selectMaterialLotLoadByVirtualNum(tenantId, virtualNum);
            for (HmeMaterialLotLoad hmeMaterialLotLoad:hmeMaterialLotLoads) {
                hmeMaterialLotLoad.setStatus(null);
                hmeMaterialLotLoadMapper.updateByPrimaryKey(hmeMaterialLotLoad);
            }
            //?????????hme_selection_details?????????
            List<HmeSelectionDetails> hmeSelectionDetails = hmeSelectionDetailsRepository.select(new HmeSelectionDetails() {{
                setTenantId(tenantId);
                setVirtualNum(virtualNum);
            }});
            if(CollectionUtils.isNotEmpty(hmeSelectionDetails)){
                hmeSelectionDetailsRepository.batchDeleteByPrimaryKey(hmeSelectionDetails);
                //2021-11-02 14:26 add by chaonan.hu for penglin.sui ??????????????????????????????GP??????
                for (HmeSelectionDetails hmeSelectionDetail:hmeSelectionDetails) {
                    HmePreSelectionVO hmePreSelectionVO = new HmePreSelectionVO();
                    hmePreSelectionVO.setSchemaName("tarzan_mes");
                    hmePreSelectionVO.setTableName("hme_selection_details");
                    hmePreSelectionVO.setTableKeyId(hmeSelectionDetail.getSelectionDetailsId());
                    hmePreSelectionVO.setObjectVersionNumber(1L);
                    hmePreSelectionVO.setCreationDate(now);
                    hmePreSelectionVO.setCreatedBy(userId);
                    hmePreSelectionVO.setLastUpdateDate(now);
                    hmePreSelectionVO.setLastUpdatedBy(userId);
                    gpDelRecordList.add(hmePreSelectionVO);
                }
            }
            //?????????hme_virtual_num?????????
            HmeVirtualNum hmeVirtualNum = hmeVirtualNumRepository.selectOne(new HmeVirtualNum() {{
                setTenantId(tenantId);
                setVirtualNum(virtualNum);
            }});
            if(Objects.nonNull(hmeVirtualNum)){
                hmeVirtualNumRepository.deleteByPrimaryKey(hmeVirtualNum);
                HmePreSelectionVO hmePreSelectionVO = new HmePreSelectionVO();
                hmePreSelectionVO.setSchemaName("tarzan_mes");
                hmePreSelectionVO.setTableName("hme_virtual_num");
                hmePreSelectionVO.setTableKeyId(hmeVirtualNum.getVirtualId());
                hmePreSelectionVO.setObjectVersionNumber(1L);
                hmePreSelectionVO.setCreationDate(now);
                hmePreSelectionVO.setCreatedBy(userId);
                hmePreSelectionVO.setLastUpdateDate(now);
                hmePreSelectionVO.setLastUpdatedBy(userId);
                gpDelRecordList.add(hmePreSelectionVO);
            }
            //2021-04-06 11:32 edit by chaonan.hu for zhenyong.ban ?????????hme_pre_selection???status
            List<HmeSelectionDetails> hmeSelectionDetailsList = hmeSelectionDetailsRepository.select(new HmeSelectionDetails() {{
                setTenantId(tenantId);
                setPreSelectionId(preSelectionId);
            }});
            if(CollectionUtils.isNotEmpty(hmeSelectionDetailsList)){
                //??????????????????????????????ATTRIBUTE1??????NEW????????????????????????NEW
                long newCount = hmeSelectionDetailsList.stream().filter(item -> "NEW".equals(item.getAttribute1())).count();
                if(newCount == hmeSelectionDetailsList.size()){
                    hmePreSelection.setStatus("NEW");
                }else if(newCount == 0){
                    //??????????????????????????????ATTRIBUTE1??????LOADED????????????????????????LOADED
                    long loadedCount = hmeSelectionDetailsList.stream().filter(item -> "LOADED".equals(item.getAttribute1())).count();
                    if(loadedCount == hmeSelectionDetailsList.size()){
                        hmePreSelection.setStatus("LOADED");
                    }
                }
            }
            hmePreSelectionMapper.updateByPrimaryKeySelective(hmePreSelection);
        }
        if(CollectionUtils.isNotEmpty(gpDelRecordList)){
            List<String> ids = customDbRepository.getNextKeys("rk_gp_del_record_s", gpDelRecordList.size());
            List<String> cids = customDbRepository.getNextKeys("rk_gp_del_record_cid_s", gpDelRecordList.size());
            int i = 0;
            for (HmePreSelectionVO hmePreSelectionVO:gpDelRecordList) {
                hmePreSelectionVO.setDelRecordId(ids.get(i));
                hmePreSelectionVO.setCid(Long.valueOf(cids.get(i)));
                i++;
            }
            hmePreSelectionMapper.gpDelRecordBatchInsert(tenantId, gpDelRecordList);
        }
        return dto;
    }

    /**
     * @param tenantId
     * @param hmePreSelection
     * @param hmeCosRuleLogic
     * @param hmeCosRuleTypeDTO1
     * @param dto
     * @param hmePreSelectionDTO4s
     * @return java.lang.String
     * @description ????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/30 15:00
     **/
    @Transactional(rollbackFor = Exception.class)
    public String SelectionNew(Long tenantId, HmePreSelection hmePreSelection, List<HmeCosRuleLogic> hmeCosRuleLogic, List<HmeCosRuleTypeDTO1> hmeCosRuleTypeDTO1, HmePreSelectionDTO1 dto, List<HmePreSelectionDTO4> hmePreSelectionDTO4s) {
        //??????userId
        Long userId = -1L;
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        int num = 0;

        //??????????????????
        List<HmeCosFunction> hmeCosFunctions = hmePreSelectionMapper.selectFunction(dto.getMaterialLotIdList());

        //????????????????????????
        List<HmeCosRuleLogic> limitRule = hmeCosRuleLogic.stream().filter(t ->
                t.getCollectionItem().equals("A04") && t.getCountType().equals("A") && t.getRangeType().equals("<=")).collect(Collectors.toList());

        //?????????????????????
        List<HmePreSelectionDTO4> collect = hmePreSelectionDTO4s.stream().filter(t -> t.getCurrent().equals(limitRule.get(0).getCurrent()) && !Double.isNaN(t.getA04())).collect(Collectors.toList());
        //????????????????????????
        Map<String, List<HmePreSelectionDTO4>> sortedDatas = collect.stream()
                .sorted(Comparator.comparing(HmePreSelectionDTO4::getA04, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.groupingBy(HmePreSelectionDTO4::getMaterialLotId, LinkedHashMap::new, Collectors.toList()));
        //????????????
        for (Map.Entry<String, List<HmePreSelectionDTO4>> elem : sortedDatas.entrySet()) {
            //?????????????????????
            for (HmePreSelectionDTO4 p : elem.getValue()) {
                //???????????????????????????
                List<HmePreSelectionDTO4> hmePreSelectionDTO4ByOrder = getOrder(sortedDatas, p, limitRule.get(0), collect);
                do {
                    if (hmePreSelectionDTO4ByOrder.size() > hmeCosRuleTypeDTO1.get(0).getCosCount()) {
                        //???????????????????????????
                        List<HmePreSelectionDTO4> result = getPortfolio(hmePreSelectionDTO4ByOrder, hmeCosRuleTypeDTO1.get(0).getCosCount(), hmeCosRuleLogic, hmeCosFunctions);
                        if (CollectionUtils.isNotEmpty(result)) {
                            String virtualNum;
                            if (dto.getIsBind().equals('Y')) {
                                //??????????????????EO???????????????
                                MtEoVO14 mtEoVO14 = new MtEoVO14();
                                mtEoVO14.setEoCount("1");
                                mtEoVO14.setTotalQty(Double.valueOf(dto.getCosNum()));
                                mtEoVO14.setWorkOrderId(dto.getWorkOrderId());
                                List<String> eoids = mtEoRepository.woLimitEoBatchCreate(tenantId, mtEoVO14);
                                virtualNum = eoids.get(0);
                            } else {
                                MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
                                mtNumrange.setObjectCode("PICK_NUM");
                                virtualNum = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange).getNumber();
                            }
                            List<HmeSelectionDetails> insertList = new ArrayList<>();
                            for (HmePreSelectionDTO4 temp :
                                    result) {
                                HmeSelectionDetails hmeSelectionDetails = new HmeSelectionDetails();
                                hmeSelectionDetails.setPreSelectionId(hmePreSelection.getPreSelectionId());
                                hmeSelectionDetails.setTenantId(tenantId);
                                hmeSelectionDetails.setSiteId(dto.getSiteId());
                                hmeSelectionDetails.setVirtualNum(virtualNum);
                                hmeSelectionDetails.setOldMaterialLotId(temp.getMaterialLotId());
                                hmeSelectionDetails.setOldLoad(temp.getLoadRow() + "," + temp.getLoadColumn());
                                hmeSelectionDetails.setMaterialId(temp.getMaterialId());
                                hmeSelectionDetails.setLoadSequence(temp.getLoadSequence());
                                hmeSelectionDetails.setCosType(temp.getCosType());
                                hmeSelectionDetails.setPower(temp.getA01());
                                hmeSelectionDetails.setAttribute1("NEW");
                                insertList.add(hmeSelectionDetails);
                                hmePreSelectionDTO4s.removeIf(t -> t.getLoadSequence().equals(temp.getLoadSequence()));
                                collect.removeIf(t -> t.getLoadSequence().equals(temp.getLoadSequence()));
                                hmePreSelectionDTO4ByOrder.removeIf(t -> t.getLoadSequence().equals(temp.getLoadSequence()));
                                //?????????????????????
                                HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
                                hmeMaterialLotLoad.setLoadSequence(temp.getLoadSequence());
                                HmeMaterialLotLoad hmeMaterialLotLoad1 = hmeMaterialLotLoadMapper.selectOne(hmeMaterialLotLoad);
                                hmeMaterialLotLoad1.setStatus("Y");
                                hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(hmeMaterialLotLoad1);
                            }
                            hmeSelectionDetailsRepository.batchInsertSelective(insertList);
                            HmeVirtualNum hmeVirtualNum = new HmeVirtualNum();

                            hmeVirtualNum.setTenantId(tenantId);
                            hmeVirtualNum.setBindFlag(dto.getIsBind());
                            hmeVirtualNum.setVirtualNum(virtualNum);
                            hmeVirtualNum.setMaterialId(dto.getMaterialId());
                            hmeVirtualNum.setProductCode(dto.getProductType());
                            hmeVirtualNum.setWorkOrderId(dto.getWorkOrderId());
                            hmeVirtualNum.setQuantity(hmeCosRuleTypeDTO1.stream().mapToLong(HmeCosRuleTypeDTO1::getCosCount).summaryStatistics().getSum());

                            hmeVirtualNum.setSelectBy(userId.toString());
                            hmeVirtualNum.setSelectDate(new Date());
                            hmeVirtualNum.setSelectWorkcellId(dto.getWorkcellId());
                            hmeVirtualNum.setEnableFlag("Y");
                            hmeVirtualNumRepository.insertSelective(hmeVirtualNum);
                            num++;
                            if (StringUtils.isNotBlank(dto.getSetsNum()) && Integer.parseInt(dto.getSetsNum()) == num) {
                                break;
                            }
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                while (1 == 1);
                if (StringUtils.isNotBlank(dto.getSetsNum()) && Integer.parseInt(dto.getSetsNum()) == num) {
                    break;
                }
            }
            if (StringUtils.isNotBlank(dto.getSetsNum()) && Integer.parseInt(dto.getSetsNum()) == num) {
                break;
            }
        }

        return String.valueOf(num);
    }


    /**
     * @param sortedDatas
     * @param p
     * @param limitRule
     * @param collect
     * @return java.util.List<com.ruike.hme.api.dto.HmePreSelectionDTO4>
     * @description ??????????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/30 16:26
     **/
    private List<HmePreSelectionDTO4> getOrder(Map<String, List<HmePreSelectionDTO4>> sortedDatas, HmePreSelectionDTO4 p, HmeCosRuleLogic limitRule, List<HmePreSelectionDTO4> collect) {
        List<HmePreSelectionDTO4> resultList = new ArrayList<>();
        //??????????????????
        List<HmePreSelectionDTO4> collect1 = collect.stream()
                .sorted(Comparator.comparing(HmePreSelectionDTO4::getA04, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
        if (collect1.contains(p)) {
            List<HmePreSelectionDTO4> hmePreSelectionDTO4s = collect1.subList(collect1.indexOf(p), collect1.size());
            for (Map.Entry<String, List<HmePreSelectionDTO4>> elem : sortedDatas.entrySet()) {
                List<HmePreSelectionDTO4> collect2 = hmePreSelectionDTO4s.stream().filter(t -> t.getMaterialLotId().equals(elem.getKey()) && t.getA04() > p.getA04() - Double.parseDouble(limitRule.getRuleValue())).collect(Collectors.toList());
                resultList.addAll(collect2);
            }
        }
        return resultList;
    }


    private List<HmePreSelectionDTO4> getPortfolio(List<HmePreSelectionDTO4> hmePreSelectionDTO4s, Long cosCount, List<HmeCosRuleLogic> hmeCosRuleLogic, List<HmeCosFunction> hmeCosFunctions) {
        List<HmePreSelectionDTO4> result = new ArrayList<>();
        //??????????????????
        int m = hmePreSelectionDTO4s.size();
        //??????????????????
        List<HmeCosRuleLogic> hmeCosRule = hmeCosRuleLogic.stream().filter(t -> t.getCountType().equals("A") || t.getCountType().equals("B")).collect(Collectors.toList());
        //??????????????????????????????
        Boolean allFlag = true;
        //???????????????????????????
        for (int x = 0; x < cosCount; x++) {
            result.add(hmePreSelectionDTO4s.get(x));
        }
        //???????????????????????????
        List<HmeCosFunction> thisHmeCosFunction = hmeCosFunctions.stream().filter(t -> result.stream().map(HmePreSelectionDTO4::getLoadSequence).collect(Collectors.toList()).contains(t.getLoadSequence())).collect(Collectors.toList());

        for (HmeCosRuleLogic hmeCosRuleTemp :
                hmeCosRule) {
            //??????????????????????????????
            List<HmeCosFunction> collect = thisHmeCosFunction.stream().filter(t -> t.getCurrent().equals(hmeCosRuleTemp.getCurrent())).collect(Collectors.toList());

            if (hmeCosRuleTemp.getCountType().equals("A")) {
                //??????
                BigDecimal subtract = new BigDecimal(0);
                if (hmeCosRuleTemp.getCollectionItem().equals("A02")) {
                    List<HmeCosFunction> collect1 = collect.stream()
                            .sorted(Comparator.comparing(HmeCosFunction::getA02, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
                    subtract = collect1.get(0).getA02().subtract(collect1.get(collect1.size() - 1).getA02());
                } else if (hmeCosRuleTemp.getCollectionItem().equals("A04")) {
                    List<HmeCosFunction> collect1 = collect.stream()
                            .sorted(Comparator.comparing(HmeCosFunction::getA04, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
                    subtract = collect1.get(0).getA04().subtract(collect1.get(collect1.size() - 1).getA04());
                } else if (hmeCosRuleTemp.getCollectionItem().equals("A05")) {
                    List<HmeCosFunction> collect1 = collect.stream()
                            .sorted(Comparator.comparing(HmeCosFunction::getA05, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
                    subtract = collect1.get(0).getA05().subtract(collect1.get(collect1.size() - 1).getA05());
                } else if (hmeCosRuleTemp.getCollectionItem().equals("A06")) {
                    List<HmeCosFunction> collect1 = collect.stream()
                            .sorted(Comparator.comparing(HmeCosFunction::getA06, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
                    subtract = collect1.get(0).getA06().subtract(collect1.get(collect1.size() - 1).getA06());
                } else if (hmeCosRuleTemp.getCollectionItem().equals("A07")) {
                    List<HmeCosFunction> collect1 = collect.stream()
                            .sorted(Comparator.comparing(HmeCosFunction::getA07, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
                    subtract = collect1.get(0).getA07().subtract(collect1.get(collect1.size() - 1).getA07());
                }
                if (hmeCosRuleTemp.getRangeType().equals(">=")) {
                    if (subtract.compareTo(new BigDecimal(hmeCosRuleTemp.getRuleValue())) < 0) {
                        allFlag = false;
                        break;
                    }
                } else {
                    if (subtract.compareTo(new BigDecimal(hmeCosRuleTemp.getRuleValue())) > 0) {
                        allFlag = false;
                        break;
                    }
                }
            } else {
                BigDecimal subtract = new BigDecimal(0);
                if (hmeCosRuleTemp.getCollectionItem().equals("A02")) {
                    subtract = collect.stream()
                            .map(HmeCosFunction::getA02).reduce(BigDecimal.ZERO, BigDecimal::add);
                } else if (hmeCosRuleTemp.getCollectionItem().equals("A04")) {
                    subtract = collect.stream()
                            .map(HmeCosFunction::getA04).reduce(BigDecimal.ZERO, BigDecimal::add);
                } else if (hmeCosRuleTemp.getCollectionItem().equals("A05")) {
                    subtract = collect.stream()
                            .map(HmeCosFunction::getA05).reduce(BigDecimal.ZERO, BigDecimal::add);
                } else if (hmeCosRuleTemp.getCollectionItem().equals("A06")) {
                    subtract = collect.stream()
                            .map(HmeCosFunction::getA06).reduce(BigDecimal.ZERO, BigDecimal::add);
                } else if (hmeCosRuleTemp.getCollectionItem().equals("A07")) {
                    subtract = collect.stream()
                            .map(HmeCosFunction::getA07).reduce(BigDecimal.ZERO, BigDecimal::add);
                }
                if (hmeCosRuleTemp.getRangeType().equals(">=")) {
                    if (subtract.compareTo(new BigDecimal(hmeCosRuleTemp.getRuleValue())) < 0) {
                        allFlag = false;
                        break;
                    }
                } else {
                    if (subtract.compareTo(new BigDecimal(hmeCosRuleTemp.getRuleValue())) > 0) {
                        allFlag = false;
                        break;
                    }
                }
            }
        }
        if (allFlag) {
            return result;
        }
        result.clear();

        int[] s = new int[m];    //??????????????????
        boolean flag = true;     //????????????
        int k = 1;               //???????????????????????????????????????

        for (int i = 0; i < m; i++)   //???????????????????????????
        {
            if (i < cosCount)
                s[i] = 1;
            else
                s[i] = 0;
        }
        do {
            flag = false;                        //??????FLAG
            for (int i = m - 1; i > 0; i--) {

                if (s[i] == 0 && s[i - 1] == 1)        //10??????01
                {
                    s[i - 1] = 0;
                    s[i] = 1;
                    flag = true;                 //?????????????????????flag?????????0???????????????????????????1???????????????????????????????????????DO??????

                    if (i < m - 1) {
                        int a = 0;
                        for (int j = i + 1; j < m; j++)       //10??????01????????????1????????????
                        {
                            if (s[j] == 1)        //10??????01
                            {
                                s[j] = 0;
                                a++;
                            }
                        }
                        for (int j = 1; j <= a; j++)       //10??????01????????????1????????????
                        {
                            s[i + j] = 1;

                        }
                    }
                    for (int kk = 0; kk < m; kk++)    //???????????????????????????????????????????????????
                    {
                        if (s[kk] == 1) {
                            result.add(hmePreSelectionDTO4s.get(kk));
                        }
                    }
                    allFlag = true;
                    for (HmeCosRuleLogic hmeCosRuleTemp :
                            hmeCosRule) {
                        //??????????????????????????????
                        List<HmeCosFunction> thisHmeCosFunctiontemp = hmeCosFunctions.stream().filter(t -> result.stream().map(HmePreSelectionDTO4::getLoadSequence).collect(Collectors.toList()).contains(t.getLoadSequence())).collect(Collectors.toList());

                        List<HmeCosFunction> collect = thisHmeCosFunctiontemp.stream().filter(t -> t.getCurrent().equals(hmeCosRuleTemp.getCurrent())).collect(Collectors.toList());

                        if (hmeCosRuleTemp.getCountType().equals("A")) {
                            //??????
                            BigDecimal subtract = new BigDecimal(0);
                            if (hmeCosRuleTemp.getCollectionItem().equals("A02")) {
                                List<HmeCosFunction> collect1 = collect.stream()
                                        .sorted(Comparator.comparing(HmeCosFunction::getA02, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
                                subtract = collect1.get(0).getA02().subtract(collect1.get(collect1.size() - 1).getA02());
                            } else if (hmeCosRuleTemp.getCollectionItem().equals("A04")) {
                                List<HmeCosFunction> collect1 = collect.stream()
                                        .sorted(Comparator.comparing(HmeCosFunction::getA04, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
                                subtract = collect1.get(0).getA04().subtract(collect1.get(collect1.size() - 1).getA04());
                            } else if (hmeCosRuleTemp.getCollectionItem().equals("A05")) {
                                List<HmeCosFunction> collect1 = collect.stream()
                                        .sorted(Comparator.comparing(HmeCosFunction::getA05, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
                                subtract = collect1.get(0).getA05().subtract(collect1.get(collect1.size() - 1).getA05());
                            } else if (hmeCosRuleTemp.getCollectionItem().equals("A06")) {
                                List<HmeCosFunction> collect1 = collect.stream()
                                        .sorted(Comparator.comparing(HmeCosFunction::getA06, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
                                subtract = collect1.get(0).getA06().subtract(collect1.get(collect1.size() - 1).getA06());
                            } else if (hmeCosRuleTemp.getCollectionItem().equals("A07")) {
                                List<HmeCosFunction> collect1 = collect.stream()
                                        .sorted(Comparator.comparing(HmeCosFunction::getA07, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
                                subtract = collect1.get(0).getA07().subtract(collect1.get(collect1.size() - 1).getA07());
                            }
                            if (hmeCosRuleTemp.getRangeType().equals(">=")) {
                                if (subtract.compareTo(new BigDecimal(hmeCosRuleTemp.getRuleValue())) < 0) {
                                    allFlag = false;
                                    break;
                                }
                            } else {
                                if (subtract.compareTo(new BigDecimal(hmeCosRuleTemp.getRuleValue())) > 0) {
                                    allFlag = false;
                                    break;
                                }
                            }
                        } else {
                            BigDecimal subtract = new BigDecimal(0);
                            if (hmeCosRuleTemp.getCollectionItem().equals("A02")) {
                                subtract = collect.stream()
                                        .map(HmeCosFunction::getA02).reduce(BigDecimal.ZERO, BigDecimal::add);
                            } else if (hmeCosRuleTemp.getCollectionItem().equals("A04")) {
                                subtract = collect.stream()
                                        .map(HmeCosFunction::getA04).reduce(BigDecimal.ZERO, BigDecimal::add);
                            } else if (hmeCosRuleTemp.getCollectionItem().equals("A05")) {
                                subtract = collect.stream()
                                        .map(HmeCosFunction::getA05).reduce(BigDecimal.ZERO, BigDecimal::add);
                            } else if (hmeCosRuleTemp.getCollectionItem().equals("A06")) {
                                subtract = collect.stream()
                                        .map(HmeCosFunction::getA06).reduce(BigDecimal.ZERO, BigDecimal::add);
                            } else if (hmeCosRuleTemp.getCollectionItem().equals("A07")) {
                                subtract = collect.stream()
                                        .map(HmeCosFunction::getA07).reduce(BigDecimal.ZERO, BigDecimal::add);
                            }
                            if (hmeCosRuleTemp.getRangeType().equals(">=")) {
                                if (subtract.compareTo(new BigDecimal(hmeCosRuleTemp.getRuleValue())) < 0) {
                                    allFlag = false;
                                    break;
                                }
                            } else {
                                if (subtract.compareTo(new BigDecimal(hmeCosRuleTemp.getRuleValue())) > 0) {
                                    allFlag = false;
                                    break;
                                }
                            }
                        }

                    }
                    if (allFlag) {
                        return result;
                    }
                    result.clear();
                    i = m;                       //??????????????????10????????????????????????FOR??????????????????i????????????m
                    k++;                       //rs???????????????????????????????????????????????????????????????
                }
            }
        } while (flag == true);

        return result;
    }

    /**
     * @param tenantId
     * @param hmePreSelection
     * @param hmeCosRuleLogic
     * @param hmeCosRuleTypeDTO1
     * @param dto
     * @param hmePreSelectionDTO4s
     * @return java.lang.String
     * @description ????????????
     * @author wenzhang.yu@hand-china.com
     * @date 2020/11/30 15:00
     **/
    @Transactional(rollbackFor = Exception.class)
    public String preSelectionNew(Long tenantId, HmePreSelection hmePreSelection, List<HmeCosRuleLogic> hmeCosRuleLogic,
                                  List<HmeCosRuleTypeDTO1> hmeCosRuleTypeDTO1, HmePreSelectionDTO1 dto, List<HmePreSelectionDTO4> hmePreSelectionDTO4s, long selectionStartDate) {
        log.info("<====hmePreSelectionService-preSelectionNew.start:?????????????????????");
        long startDate = System.currentTimeMillis();
        //??????userId
        Long userId = -1L;
        if (DetailsHelper.getUserDetails() != null && DetailsHelper.getUserDetails().getUserId() != null) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }
        int num = 0;

        //??????????????????
        long startDate2 = System.currentTimeMillis();
        List<HmeCosFunction> hmeCosFunctions = hmePreSelectionMapper.selectFunction(dto.getMaterialLotIdList());
        long endDate2 = System.currentTimeMillis();
        log.info("<====hmePreSelectionService-preSelectionNew:??????????????????????????????,????????????{}??????", (endDate2 - startDate2));

        //????????????????????????
        List<HmeCosRuleLogic> limitRule = hmeCosRuleLogic.stream().filter(t ->
                t.getCollectionItem().equals("A04") && t.getCountType().equals("A") && t.getRangeType().equals("<=")).collect(Collectors.toList());
        long endDate3 = System.currentTimeMillis();
        log.info("<====hmePreSelectionService-preSelectionNew:????????????????????????????????????,????????????{}??????", (endDate3 - endDate2));

        //?????????????????????
        List<HmePreSelectionDTO4> limitRuleBy = hmePreSelectionDTO4s.stream()
                .filter(t -> t.getCurrent().equals(limitRule.get(0).getCurrent()) && !Double.isNaN(t.getA04()))
                .collect(Collectors.toList());
        long endDate4 = System.currentTimeMillis();
        log.info("<====hmePreSelectionService-preSelectionNew:?????????????????????????????????,????????????{}??????", (endDate4 - endDate3));

        //????????????????????????
        List<HmePreSelectionDTO4> hmePreSelectionDTO4ByOrder = limitRuleBy.stream()
                .sorted(Comparator.comparing(HmePreSelectionDTO4::getA04, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
        long endDate5 = System.currentTimeMillis();
        log.info("<====hmePreSelectionService-preSelectionNew:???????????????????????????????????????{}???,????????????{}??????", hmePreSelectionDTO4ByOrder.size(), (endDate5 - endDate4));

        int total = 1;
        List<HmeSelectionDetails> insertSelectionDetailsList = new ArrayList<>();
        List<String> loadSequenceList = new ArrayList<>();
        List<HmeVirtualNum> insertVirtualNumList = new ArrayList<>();
        //2021-09-17 10:18 edit by chaonan.hu for peng.zhao ??????????????????????????? virtualNumOrder????????????????????????
        int virtualNumOrder = -1;
        for (HmePreSelectionDTO4 hmePreSelectionDTO4Temp :
                hmePreSelectionDTO4ByOrder) {
            long startDate6 = System.currentTimeMillis();
            log.info("<====hmePreSelectionService-preSelectionNew:???????????????????????????{}???hmePreSelectionDTO4Temp", total);
            //????????????????????????????????????
            if(!limitRuleBy.contains(hmePreSelectionDTO4Temp))
            {
                continue;
            }
            List<HmePreSelectionDTO4> hmePreSelectionDTO4s1 = hmePreSelectionDTO4ByOrder.subList(hmePreSelectionDTO4ByOrder.indexOf(hmePreSelectionDTO4Temp), hmePreSelectionDTO4ByOrder.size());
            List<HmePreSelectionDTO4> collect = hmePreSelectionDTO4s1.stream().filter(t -> limitRuleBy.contains(t)).collect(Collectors.toList());
            List<HmePreSelectionDTO4> HmePreSelectionDTO4List = collect.stream()
                    .filter(t -> t.getA04() >= hmePreSelectionDTO4Temp.getA04() - Double.parseDouble(limitRule.get(0).getRuleValue()))
                    .collect(Collectors.toList());
            long endDate7 = System.currentTimeMillis();
            log.info("<====hmePreSelectionService-preSelectionNew:????????????????????????????????????????????????,????????????{}??????", (endDate7 - startDate6));

            int i = 1;
            long startDate9 = System.currentTimeMillis();
            do {
                long startDate8 = System.currentTimeMillis();
                log.info("<====hmePreSelectionService-preSelectionNew:???????????????????????????{}???hmePreSelectionDTO4Temp???{}?????????", total, i);
                //2021-06-21 11:45 add by chaonan.hu for peng.zhao ??????????????????????????????????????????4???30????????????????????????
                if(startDate8 - selectionStartDate >= 270000){
                    throw new MtException("HME_SELECT_008", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_SELECT_008", "HME"));
                }
                if (HmePreSelectionDTO4List.size() >= hmeCosRuleTypeDTO1.get(0).getCosCount()) {
                    //???????????????????????????
                    List<HmePreSelectionDTO4> result = getPortfolioNew(HmePreSelectionDTO4List, hmeCosRuleTypeDTO1.get(0).getCosCount(), hmeCosRuleLogic, hmeCosFunctions);
                    long endDate10 = System.currentTimeMillis();
                    log.info("<====???????????????????????????{}???hmePreSelectionDTO4Temp???{}?????????,getPortfolioNew??????{}??????", total, i, (endDate10 - startDate8));

                    if (CollectionUtils.isNotEmpty(result)) {
                        result = result.stream().sorted(Comparator.comparing(HmePreSelectionDTO4::getMaterialLotCode).
                                thenComparing(HmePreSelectionDTO4::getLoadRow).
                                thenComparing(HmePreSelectionDTO4::getLoadColumn)).collect(Collectors.toList());
//                        String virtualNum;
//                        if (dto.getIsBind().equals('Y')) {
//                            //??????????????????EO???????????????
//                            MtEoVO14 mtEoVO14 = new MtEoVO14();
//                            mtEoVO14.setEoCount("1");
//                            mtEoVO14.setTotalQty(Double.valueOf(dto.getCosNum()));
//                            mtEoVO14.setWorkOrderId(dto.getWorkOrderId());
//                            List<String> eoids = mtEoRepository.woLimitEoBatchCreate(tenantId, mtEoVO14);
//                            virtualNum = eoids.get(0);
//                            long endDate11 = System.currentTimeMillis();
//                            log.info("<====???????????????????????????{}???hmePreSelectionDTO4Temp???{}?????????,??????????????????EO?????????????????????{}??????", total, i, (endDate11 - endDate10));
//                        } else {
//                            MtNumrangeVO2 mtNumrange = new MtNumrangeVO2();
//                            mtNumrange.setObjectCode("PICK_NUM");
//                            virtualNum = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrange).getNumber();
//                            long endDate11 = System.currentTimeMillis();
//                            log.info("<====???????????????????????????{}???hmePreSelectionDTO4Temp???{}?????????,??????????????????????????????{}??????", total, i, (endDate11 - endDate10));
//                        }
                        virtualNumOrder++;
                        List<HmeSelectionDetails> insertList = new ArrayList<>();
                        int cosNum=1;
                        long startDate12 = System.currentTimeMillis();
                        for (HmePreSelectionDTO4 temp :
                                result) {
                            HmeSelectionDetails hmeSelectionDetails = new HmeSelectionDetails();
                            hmeSelectionDetails.setPreSelectionId(hmePreSelection.getPreSelectionId());
                            hmeSelectionDetails.setTenantId(tenantId);
                            hmeSelectionDetails.setSiteId(dto.getSiteId());
                            hmeSelectionDetails.setVirtualNumOrder(virtualNumOrder);
//                            hmeSelectionDetails.setVirtualNum(virtualNum);
                            hmeSelectionDetails.setOldMaterialLotId(temp.getMaterialLotId());
                            hmeSelectionDetails.setOldLoad(temp.getLoadRow() + "," + temp.getLoadColumn());
                            hmeSelectionDetails.setMaterialId(temp.getMaterialId());
                            hmeSelectionDetails.setLoadSequence(temp.getLoadSequence());
                            hmeSelectionDetails.setCosType(temp.getCosType());
                            hmeSelectionDetails.setPower(temp.getA01());
                            hmeSelectionDetails.setAttribute1("NEW");
                            hmeSelectionDetails.setAttribute2("COS"+String.format("%02d", cosNum));
                            insertSelectionDetailsList.add(hmeSelectionDetails);
                            insertList.add(hmeSelectionDetails);
                            limitRuleBy.removeIf(t -> t.getLoadSequence().equals(temp.getLoadSequence()));
                            HmePreSelectionDTO4List.removeIf(t -> t.getLoadSequence().equals(temp.getLoadSequence()));
                            //?????????????????????
                            loadSequenceList.add(temp.getLoadSequence());
//                            HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
//                            hmeMaterialLotLoad.setLoadSequence(temp.getLoadSequence());
//                            HmeMaterialLotLoad hmeMaterialLotLoad1 = hmeMaterialLotLoadMapper.selectOne(hmeMaterialLotLoad);
//                            hmeMaterialLotLoad1.setStatus("Y");
//                            hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(hmeMaterialLotLoad1);
                            cosNum++;
                        }
                        //2021-04-21 16:38 add by chaonan.hu for kang.wang ?????????????????????pre_selection_id+old_material_lot_id+old_load???????????????
                        if(CollectionUtils.isNotEmpty(insertList)){
                            Map<String, List<HmeSelectionDetails>> insertMap = insertList.stream().collect(Collectors.groupingBy(item -> {
                                return item.getPreSelectionId() + "," + item.getOldMaterialLotId() + "," + item.getOldLoad();
                            }));
                            if(insertList.size() != insertMap.size()){
                                throw new MtException("HME_SELECT_006", mtErrorMessageRepository
                                        .getErrorMessageWithModule(tenantId, "HME_SELECT_006", "HME"));
                            }
//                            hmeSelectionDetailsRepository.batchInsertSelective(insertList);
                        }
                        HmeVirtualNum hmeVirtualNum = new HmeVirtualNum();

                        hmeVirtualNum.setTenantId(tenantId);
                        hmeVirtualNum.setBindFlag(dto.getIsBind());
                        hmeVirtualNum.setVirtualNumOrder(virtualNumOrder);
//                        hmeVirtualNum.setVirtualNum(virtualNum);
                        hmeVirtualNum.setMaterialId(dto.getMaterialId());
                        hmeVirtualNum.setProductCode(dto.getProductType());
                        hmeVirtualNum.setWorkOrderId(dto.getWorkOrderId());
                        hmeVirtualNum.setQuantity(hmeCosRuleTypeDTO1.stream().mapToLong(HmeCosRuleTypeDTO1::getCosCount).summaryStatistics().getSum());

                        hmeVirtualNum.setSelectBy(userId.toString());
                        hmeVirtualNum.setSelectDate(new Date());
                        hmeVirtualNum.setSelectWorkcellId(dto.getWorkcellId());
                        hmeVirtualNum.setEnableFlag("Y");
                        insertVirtualNumList.add(hmeVirtualNum);
//                        hmeVirtualNumRepository.insertSelective(hmeVirtualNum);
                        long endDate12 = System.currentTimeMillis();
                        log.info("<====???????????????????????????{}???hmePreSelectionDTO4Temp???{}?????????,?????????????????????????????????????????????{}??????", total, i, (endDate12 - startDate12));
                        num++;
                        if (StringUtils.isNotBlank(dto.getSetsNum()) && Integer.parseInt(dto.getSetsNum()) == num) {
                            long endDate8 = System.currentTimeMillis();
                            log.info("<====hmePreSelectionService-preSelectionNew:???????????????????????????{}???hmePreSelectionDTO4Temp???{}?????????,????????????{}??????", total, i, (endDate8 - startDate8));
                            i++;
                            break;
                        }
                    } else {
                        long endDate8 = System.currentTimeMillis();
                        log.info("<====hmePreSelectionService-preSelectionNew:???????????????????????????{}???hmePreSelectionDTO4Temp???{}?????????,????????????{}??????", total, i, (endDate8 - startDate8));
                        i++;
                        break;
                    }
                } else {
                    long endDate8 = System.currentTimeMillis();
                    log.info("<====hmePreSelectionService-preSelectionNew:???????????????????????????{}???hmePreSelectionDTO4Temp???{}?????????,????????????{}??????", total, i, (endDate8 - startDate8));
                    i++;
                    break;
                }
                i++;
            }while (1==1);
            long endDate9 = System.currentTimeMillis();
            log.info("<====hmePreSelectionService-preSelectionNew:???????????????????????????{}???hmePreSelectionDTO4Temp?????????{}???, ????????????{}??????", total, i, (endDate9 - startDate9));
            if (StringUtils.isNotBlank(dto.getSetsNum()) && Integer.parseInt(dto.getSetsNum()) == num) {
                long endDate6 = System.currentTimeMillis();
                log.info("<====hmePreSelectionService-preSelectionNew:???????????????????????????{}???hmePreSelectionDTO4Temp,????????????{}??????", total, (endDate6 - startDate6));
                total++;
                break;
            }
            long endDate6 = System.currentTimeMillis();
            log.info("<====hmePreSelectionService-preSelectionNew:???????????????????????????{}???hmePreSelectionDTO4Temp,????????????{}??????", total, (endDate6 - startDate6));
            total++;
        }
        long endDate = System.currentTimeMillis();
        log.info("<====hmePreSelectionService-preSelectionNew.end:????????????????????????{}???,????????????{}??????", hmePreSelectionDTO4ByOrder.size(), (endDate - endDate5));
        //2021-09-17 10:33 edit by chaonan.hu for peng.zhao ???????????????????????????
        List<String> virtualNumList = new ArrayList<>();
        if(virtualNumOrder > -1){
            MtNumrangeVO9 mtNumrangeVO9 = new MtNumrangeVO9();
            mtNumrangeVO9.setObjectCode("PICK_NUM");
            mtNumrangeVO9.setObjectNumFlag("Y");
            //??????virtualNumOrder??????0????????????????????????????????????1
            Long numQty = Long.valueOf(virtualNumOrder + 1);
            mtNumrangeVO9.setNumQty(numQty);
            MtNumrangeVO8 mtNumrangeVO8 = mtNumrangeRepository.numrangeBatchGenerate(tenantId, mtNumrangeVO9);
            virtualNumList = mtNumrangeVO8.getNumberList();
            log.info("<====hmePreSelectionService-preSelectionNew:??????????????????????????????{}??????", (System.currentTimeMillis() - endDate));
        }
        //2021-08-27 15:23 edit by chaonan.hu for peng.zhao ????????????????????????????????????????????????????????????????????????????????????
        Date date = new Date();
        long endDate8 = System.currentTimeMillis();
        List<String> sqlList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(insertSelectionDetailsList)){
            List<String> selectionDetailsIdS = customDbRepository.getNextKeys("hme_selection_details_s", insertSelectionDetailsList.size());
            List<String> selectionDetailsCidS = customDbRepository.getNextKeys("hme_selection_details_cid_s", insertSelectionDetailsList.size());
            int i = 0;
            for (HmeSelectionDetails hmeSelectionDetails:insertSelectionDetailsList) {
                hmeSelectionDetails.setSelectionDetailsId(selectionDetailsIdS.get(i));
                hmeSelectionDetails.setVirtualNum(virtualNumList.get(hmeSelectionDetails.getVirtualNumOrder()));
                hmeSelectionDetails.setCid(Long.valueOf(selectionDetailsCidS.get(i)));
                hmeSelectionDetails.setObjectVersionNumber(1L);
                hmeSelectionDetails.setCreatedBy(userId);
                hmeSelectionDetails.setLastUpdatedBy(userId);
                hmeSelectionDetails.setCreationDate(date);
                hmeSelectionDetails.setLastUpdateDate(date);
                sqlList.addAll(customDbRepository.getInsertSql(hmeSelectionDetails));
                i++;
            }
        }
        if(CollectionUtils.isNotEmpty(loadSequenceList)){
            loadSequenceList = loadSequenceList.stream().distinct().collect(Collectors.toList());
            List<HmeMaterialLotLoad> updateMaterialLotLoadList = new ArrayList<>();
            List<List<String>> splitLoadSequenceList = CommonUtils.splitSqlList(loadSequenceList, 1000);
            for (List<String> splitLoadSequence:splitLoadSequenceList) {
                updateMaterialLotLoadList.addAll(hmeMaterialLotLoadRepository.selectByCondition(Condition.builder(HmeMaterialLotLoad.class)
                                                .andWhere(Sqls.custom()
                                                        .andIn(HmeMaterialLotLoad.FIELD_LOAD_SEQUENCE, splitLoadSequence))
                                                .build()));
            }
            if(CollectionUtils.isNotEmpty(updateMaterialLotLoadList)){
                List<String> materialLotLoadCidS = customDbRepository.getNextKeys("hme_material_lot_load_cid_s", updateMaterialLotLoadList.size());
                int i = 0;
                for (HmeMaterialLotLoad hmeMaterialLotLoad:updateMaterialLotLoadList) {
                    hmeMaterialLotLoad.setStatus("Y");
                    hmeMaterialLotLoad.setCid(Long.valueOf(materialLotLoadCidS.get(i)));
                    hmeMaterialLotLoad.setLastUpdatedBy(userId);
                    hmeMaterialLotLoad.setLastUpdateDate(date);
                    sqlList.addAll(customDbRepository.getUpdateSql(hmeMaterialLotLoad));
                    i++;
                }
            }
        }
        if(CollectionUtils.isNotEmpty(insertVirtualNumList)){
            List<String> virtualNumIdS = customDbRepository.getNextKeys("hme_virtual_num_s", insertVirtualNumList.size());
            List<String> virtualNumCidS = customDbRepository.getNextKeys("hme_virtual_num_cid_s", insertVirtualNumList.size());
            int i = 0;
            for (HmeVirtualNum hmeVirtualNum: insertVirtualNumList) {
                hmeVirtualNum.setVirtualId(virtualNumIdS.get(i));
                hmeVirtualNum.setVirtualNum(virtualNumList.get(hmeVirtualNum.getVirtualNumOrder()));
                hmeVirtualNum.setCid(Long.valueOf(virtualNumCidS.get(i)));
                hmeVirtualNum.setObjectVersionNumber(1L);
                hmeVirtualNum.setCreatedBy(userId);
                hmeVirtualNum.setLastUpdatedBy(userId);
                hmeVirtualNum.setCreationDate(date);
                hmeVirtualNum.setLastUpdateDate(date);
                sqlList.addAll(customDbRepository.getInsertSql(hmeVirtualNum));
                i++;
            }
        }
        if(CollectionUtils.isNotEmpty(sqlList)){
            List<List<String>> splitSqlList = CommonUtils.splitSqlList(sqlList, 1000);
            for (List<String> splitSql:splitSqlList) {
                jdbcTemplate.batchUpdate(splitSql.toArray(new String[splitSql.size()]));
            }
        }
        long endDate7 = System.currentTimeMillis();
        log.info("<====hmePreSelectionService-preSelectionNew:???????????????????????????{}??????", (endDate7 - endDate8));
        log.info("<====hmePreSelectionService-preSelectionNew.end:??????????????????,????????????{}??????", (endDate7 - startDate));
        return String.valueOf(num);
    }


    private List<HmePreSelectionDTO4> getPortfolioNew(List<HmePreSelectionDTO4> hmePreSelectionDTO4List, Long cosCount, List<HmeCosRuleLogic> hmeCosRuleLogic, List<HmeCosFunction> hmeCosFunctions) {

        List<HmePreSelectionDTO4> result = new ArrayList<>();
        //?????????????????????????????????
        List<HmeCosRuleLogic> limitRuleByPower = hmeCosRuleLogic.stream().filter(t ->
                t.getCollectionItem().equals("A02") && t.getCountType().equals("B") && t.getRangeType().equals(">=")).collect(Collectors.toList());

        //????????????
        BigDecimal divide = new BigDecimal(limitRuleByPower.get(0).getRuleValue()).divide(new BigDecimal(cosCount),4, BigDecimal.ROUND_HALF_UP);
//        log.info("??????"+divide);
        //V20211221 modify by penglin.sui ?????????List??????MAP
        Map<String,List<HmeCosFunction>> hmeCosFunctionMap =
                hmeCosFunctions.stream().collect(Collectors.groupingBy(e -> e.getCurrent() + "#" + e.getLoadSequence()));
        hmePreSelectionDTO4List.forEach(t -> {
//            List<HmeCosFunction> collect = hmeCosFunctions.stream().filter(temp -> temp.getLoadSequence().equals(t.getLoadSequence()) && temp.getCurrent().equals(limitRuleByPower.get(0).getCurrent())).collect(Collectors.toList());
            List<HmeCosFunction> collect = hmeCosFunctionMap.getOrDefault(limitRuleByPower.get(0).getCurrent() + "#" + t.getLoadSequence() , new ArrayList<>());
            if(CollectionUtils.isNotEmpty(collect)) {
                t.setPowerDif(collect.get(0).getA02().subtract(divide));
            }
        });
        //?????????????????????????????????
        List<HmePreSelectionDTO4> collect = new ArrayList<>();
        for (HmePreSelectionDTO4 hmePreSelectionDTO4 : hmePreSelectionDTO4List) {
            collect.add(hmePreSelectionDTO4);
        }
        collect.sort(Comparator.comparing(HmePreSelectionDTO4::getPowerDif));
//        log.info("???????????????"+collect);
        BigDecimal dif = BigDecimal.ZERO;
        do {
            if (dif.compareTo(BigDecimal.ZERO) < 0) {
                BigDecimal finalDif = dif;
                List<HmePreSelectionDTO4> collect1 = collect.stream().filter(t -> t.getPowerDif().compareTo(finalDif.abs()) >= 0).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect1)) {
                    dif=dif.add(collect1.get(0).getPowerDif());
                    result.add(collect1.get(0));
                    collect.remove(collect1.get(0));
                } else {
                    dif=dif.add(collect.get(collect.size() - 1).getPowerDif());
                    result.add(collect.get(collect.size() - 1));
                    collect.remove(collect.get(collect.size() - 1));
                }
            } else {
                dif=dif.add(collect.get(0).getPowerDif());
                result.add(collect.get(0));
                collect.remove(collect.get(0));
            }
        } while (result.size() < cosCount);
        //??????????????????????????????
        //??????????????????
        List<HmeCosRuleLogic> hmeCosRule = hmeCosRuleLogic.stream().filter(t -> t.getCountType().equals("B")).collect(Collectors.toList());

        boolean allFlag = false;

        //V20211221 modify by penglin.sui ?????????List??????MAP
//        Map<String,List<HmeCosFunction>> hmeCosFunctionMap2 =
//                hmeCosFunctions.stream().collect(Collectors.groupingBy(e -> e.getCurrent()));

        do {
            allFlag = false;
            //???????????????
            List<HmeCosRuleLogic> hmeCosRuleByPower = hmeCosRule.stream().filter(t -> t.getCollectionItem().equals("A02")).collect(Collectors.toList());
            for (HmeCosRuleLogic hmeCosRuleByPowerTemp :
                    hmeCosRuleByPower) {
                //??????????????????????????????
//                List<HmeCosFunction> hmeCosFunctiontemp =hmeCosFunctions.stream().filter(t -> t.getCurrent().equals(hmeCosRuleByPowerTemp.getCurrent())).collect(Collectors.toList());
//                List<HmeCosFunction> hmeCosFunctiontemp = hmeCosFunctionMap2.getOrDefault(hmeCosRuleByPowerTemp.getCurrent() , new ArrayList<>());
//                List<HmeCosFunction> hmeCosFunctionAll = hmeCosFunctiontemp.stream().filter(t -> resultLoadSequenceList.contains(t.getLoadSequence())).collect(Collectors.toList());
//                log.info("????????????"+hmeCosFunctionAll);
                List<HmeCosFunction> hmeCosFunctionAll = new ArrayList<>();
                List<String> resultLoadSequenceList = result.stream().map(HmePreSelectionDTO4::getLoadSequence).collect(Collectors.toList());
                for (String loadSequence : resultLoadSequenceList
                     ) {
                    List<HmeCosFunction> subHmeCosFunctionAll =
                            hmeCosFunctionMap.getOrDefault(hmeCosRuleByPowerTemp.getCurrent() + "#" + loadSequence , new ArrayList<>());
                    if(CollectionUtils.isNotEmpty(subHmeCosFunctionAll)){
                        hmeCosFunctionAll.addAll(subHmeCosFunctionAll);
                    }
                }

                BigDecimal subtract  = hmeCosFunctionAll.stream()
                        .map(HmeCosFunction::getA02).reduce(BigDecimal.ZERO, BigDecimal::add);
                if (hmeCosRuleByPowerTemp.getRangeType().equals(">=")) {
                    if (subtract.compareTo(new BigDecimal(hmeCosRuleByPowerTemp.getRuleValue())) < 0) {
                        HmeCosFunction hmeCosFunction = hmeCosFunctionAll.stream()
                                .sorted(Comparator.comparing(HmeCosFunction::getA02))
                                .collect(Collectors.toList()).get(0);
                        String loadSequence = hmeCosFunction.getLoadSequence();
                        //??????????????????????????????
                        List<HmePreSelectionDTO4> collect1 = result.stream().filter(t -> t.getLoadSequence().equals(loadSequence)).collect(Collectors.toList());
                        result.remove(collect1.get(0));
                        dif=dif.subtract(collect1.get(0).getPowerDif());
                        BigDecimal finalDif1 = dif;
                        List<HmePreSelectionDTO4> collect2 = collect.stream().filter(t -> t.getPowerDif().compareTo(finalDif1.abs()) > 0).collect(Collectors.toList());

                        if (CollectionUtils.isNotEmpty(collect2)) {
                            dif=dif.add(collect2.get(0).getPowerDif());
                            result.add(collect2.get(0));
                            collect.remove(collect2.get(0));
                        } else {
                            if(CollectionUtils.isNotEmpty(collect)) {
                                dif = dif.add(collect.get(collect.size() - 1).getPowerDif());
                                result.add(collect.get(collect.size() - 1));
                                collect.remove(collect.get(collect.size() - 1));
                            }
                            else
                            {
                                result.clear();
                                break;
                            }
                        }
                        allFlag = true;
                        break;
                    }
                } else {
                    if (subtract.compareTo(new BigDecimal(hmeCosRuleByPowerTemp.getRuleValue())) > 0) {
                        HmeCosFunction hmeCosFunction = hmeCosFunctionAll.stream()
                                .sorted(Comparator.comparing(HmeCosFunction::getA02, Comparator.nullsLast(Comparator.reverseOrder())))
                                .collect(Collectors.toList()).get(0);
                        String loadSequence = hmeCosFunction.getLoadSequence();
                        //??????????????????????????????
                        List<HmePreSelectionDTO4> collect1 = result.stream().filter(t -> t.getLoadSequence().equals(loadSequence)).collect(Collectors.toList());
                        result.remove(collect1.get(0));
                        dif=dif.subtract(collect1.get(0).getPowerDif());
                        BigDecimal finalDif1 = dif;
                        List<HmePreSelectionDTO4> collect2 = collect.stream().filter(t -> t.getPowerDif().compareTo(finalDif1.abs()) > 0).collect(Collectors.toList());
                        List<HmeCosFunction> collect4 = hmeCosFunctions.stream()
                                .filter(t -> collect2.stream().map(HmePreSelectionDTO4::getLoadSequence).collect(Collectors.toList()).contains(t.getLoadSequence())
                                        &&t.getCurrent().equals(hmeCosRuleByPowerTemp.getCurrent())
                                        &&t.getA02().compareTo(hmeCosFunction.getA02())<0)
                                .collect(Collectors.toList());
                        List<HmePreSelectionDTO4> collect5 = collect2.stream()
                                .filter(t -> collect4.stream().map(HmeCosFunction::getLoadSequence).collect(Collectors.toList()).contains(t.getLoadColumn())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(collect5)) {
                            dif=dif.add(collect5.get(0).getPowerDif());
                            result.add(collect5.get(0));
                            collect.remove(collect5.get(0));
                        } else {
                            result.clear();
                            break;
                        }
                        allFlag = true;
                        break;
                    }
                }

            }
            if(CollectionUtils.isEmpty(result))
            {
                break;
            }
            if(!allFlag) {
                List<HmeCosRuleLogic> hmeCosRuleByVol = hmeCosRule.stream().filter(t -> t.getCollectionItem().equals("A06")).collect(Collectors.toList());
                for (HmeCosRuleLogic hmeCosRuleByVolTemp :
                        hmeCosRuleByVol) {
                    //??????????????????????????????
//                    List<HmeCosFunction> hmeCosFunctiontemp = hmeCosFunctions.stream().filter(t -> t.getCurrent().equals(hmeCosRuleByVolTemp.getCurrent())).collect(Collectors.toList());
//                    List<HmeCosFunction> hmeCosFunctiontemp = hmeCosFunctionMap2.getOrDefault(hmeCosRuleByVolTemp.getCurrent() , new ArrayList<>());
//                    List<String> resultLoadSequenceList = result.stream().map(HmePreSelectionDTO4::getLoadSequence).collect(Collectors.toList());
//                    List<HmeCosFunction> hmeCosFunctionAll = hmeCosFunctiontemp.stream().filter(t -> result.stream().map(HmePreSelectionDTO4::getLoadSequence).collect(Collectors.toList()).contains(t.getLoadSequence())).collect(Collectors.toList());
//                    log.info("????????????" + hmeCosFunctionAll);

                    List<HmeCosFunction> hmeCosFunctionAll = new ArrayList<>();
                    List<String> resultLoadSequenceList = result.stream().map(HmePreSelectionDTO4::getLoadSequence).collect(Collectors.toList());
                    for (String loadSequence : resultLoadSequenceList
                    ) {
                        List<HmeCosFunction> subHmeCosFunctionAll =
                                hmeCosFunctionMap.getOrDefault(hmeCosRuleByVolTemp.getCurrent() + "#" + loadSequence , new ArrayList<>());
                        if(CollectionUtils.isNotEmpty(subHmeCosFunctionAll)){
                            hmeCosFunctionAll.addAll(subHmeCosFunctionAll);
                        }
                    }

                    BigDecimal subtract = hmeCosFunctionAll.stream()
                            .map(HmeCosFunction::getA06).reduce(BigDecimal.ZERO, BigDecimal::add);
                    if (hmeCosRuleByVolTemp.getRangeType().equals(">=")) {
                        if (subtract.compareTo(new BigDecimal(hmeCosRuleByVolTemp.getRuleValue())) < 0) {
                            HmeCosFunction hmeCosFunction = hmeCosFunctionAll.stream()
                                    .sorted(Comparator.comparing(HmeCosFunction::getA06))
                                    .collect(Collectors.toList()).get(0);
                            String loadSequence = hmeCosFunction.getLoadSequence();
                            //??????????????????????????????
                            List<HmePreSelectionDTO4> collect1 = result.stream().filter(t -> t.getLoadSequence().equals(loadSequence)).collect(Collectors.toList());
                            result.remove(collect1.get(0));
                            dif = dif.subtract(collect1.get(0).getPowerDif());
                            BigDecimal finalDif1 = dif;
                            List<HmePreSelectionDTO4> collect2 = collect.stream().filter(t -> t.getPowerDif().compareTo(finalDif1.abs()) > 0).collect(Collectors.toList());
                            List<HmeCosFunction> collect4 = hmeCosFunctions.stream()
                                    .filter(t -> collect2.stream().map(HmePreSelectionDTO4::getLoadSequence).collect(Collectors.toList()).contains(t.getLoadSequence())
                                            && t.getCurrent().equals(hmeCosRuleByVolTemp.getCurrent())
                                            && t.getA06().compareTo(hmeCosFunction.getA06()) > 0)
                                    .collect(Collectors.toList());
                            List<HmePreSelectionDTO4> collect5 = collect2.stream()
                                    .filter(t -> collect4.stream().map(HmeCosFunction::getLoadSequence).collect(Collectors.toList()).contains(t.getLoadColumn())).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(collect5)) {
                                dif = dif.add(collect5.get(0).getPowerDif());
                                result.add(collect5.get(0));
                                collect.remove(collect5.get(0));
                            } else {
                                result.clear();
                                break;
                            }
                            allFlag = true;
                            break;
                        }
                    } else {
                        if (subtract.compareTo(new BigDecimal(hmeCosRuleByVolTemp.getRuleValue())) > 0) {
                            HmeCosFunction hmeCosFunction = hmeCosFunctionAll.stream()
                                    .sorted(Comparator.comparing(HmeCosFunction::getA06, Comparator.nullsLast(Comparator.reverseOrder())))
                                    .collect(Collectors.toList()).get(0);
                            String loadSequence = hmeCosFunction.getLoadSequence();
                            //??????????????????????????????
                            List<HmePreSelectionDTO4> collect1 = result.stream().filter(t -> t.getLoadSequence().equals(loadSequence)).collect(Collectors.toList());
                            result.remove(collect1.get(0));
                            dif = dif.subtract(collect1.get(0).getPowerDif());
                            BigDecimal finalDif1 = dif;
                            List<HmePreSelectionDTO4> collect2 = collect.stream().filter(t -> t.getPowerDif().compareTo(finalDif1.abs()) > 0).collect(Collectors.toList());
                            List<HmeCosFunction> collect4 = hmeCosFunctions.stream()
                                    .filter(t -> collect2.stream().map(HmePreSelectionDTO4::getLoadSequence).collect(Collectors.toList()).contains(t.getLoadSequence())
                                            && t.getCurrent().equals(hmeCosRuleByVolTemp.getCurrent())
                                            && t.getA06().compareTo(hmeCosFunction.getA06()) < 0)
                                    .collect(Collectors.toList());
                            List<HmePreSelectionDTO4> collect5 = collect2.stream()
                                    .filter(t -> collect4.stream().map(HmeCosFunction::getLoadSequence).collect(Collectors.toList()).contains(t.getLoadColumn())).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(collect5)) {
                                dif = dif.add(collect5.get(0).getPowerDif());
                                result.add(collect5.get(0));
                                collect.remove(collect5.get(0));
                            } else {
                                result.clear();
                                break;
                            }
                            allFlag = true;
                            break;
                        }
                    }

                }
            }
        } while (allFlag);

        return result;
    }

    String getFunction(Long tenantId, HmeCosFunction hmeCosFunction, String collectionItem){
        String resultFunction = null;
        switch (collectionItem){
            case "A01":
                resultFunction = hmeCosFunction.getA01();
                break;
            case "A02":
                resultFunction = Objects.nonNull(hmeCosFunction.getA02())?hmeCosFunction.getA02().toString():null;
                break;
            case "A03":
                resultFunction = hmeCosFunction.getA03();
                break;
            case "A04":
                resultFunction = Objects.nonNull(hmeCosFunction.getA04())?hmeCosFunction.getA04().toString():null;
                break;
            case "A05":
                resultFunction = Objects.nonNull(hmeCosFunction.getA05())?hmeCosFunction.getA05().toString():null;
                break;
            case "A06":
                resultFunction = Objects.nonNull(hmeCosFunction.getA06())?hmeCosFunction.getA06().toString():null;
                break;
            case "A07":
                resultFunction = Objects.nonNull(hmeCosFunction.getA07())?hmeCosFunction.getA07().toString():null;
                break;
            case "A08":
                resultFunction = hmeCosFunction.getA08();
                break;
            case "A09":
                resultFunction = hmeCosFunction.getA09();
                break;
            case "A010":
                resultFunction = Objects.nonNull(hmeCosFunction.getA010())?hmeCosFunction.getA010().toString():null;
                break;
            case "A011":
                resultFunction = Objects.nonNull(hmeCosFunction.getA011())?hmeCosFunction.getA011().toString():null;
                break;
            case "A012":
                resultFunction = Objects.nonNull(hmeCosFunction.getA012())?hmeCosFunction.getA012().toString():null;
                break;
            case "A013":
                resultFunction = Objects.nonNull(hmeCosFunction.getA013())?hmeCosFunction.getA013().toString():null;
                break;
            case "A014":
                resultFunction = Objects.nonNull(hmeCosFunction.getA014())?hmeCosFunction.getA014().toString():null;
                break;
            case "A15":
                resultFunction = Objects.nonNull(hmeCosFunction.getA15())?hmeCosFunction.getA15().toString():null;
                break;
            case "A16":
                resultFunction = Objects.nonNull(hmeCosFunction.getA16())?hmeCosFunction.getA16().toString():null;
                break;
            case "A17":
                resultFunction = Objects.nonNull(hmeCosFunction.getA17())?hmeCosFunction.getA17().toString():null;
                break;
            case "A18":
                resultFunction = Objects.nonNull(hmeCosFunction.getA18())?hmeCosFunction.getA18().toString():null;
                break;
            case "A19":
                resultFunction = Objects.nonNull(hmeCosFunction.getA19())?hmeCosFunction.getA19().toString():null;
                break;
            case "A20":
                resultFunction = Objects.nonNull(hmeCosFunction.getA20())?hmeCosFunction.getA20().toString():null;
                break;
            case "A21":
                resultFunction = Objects.nonNull(hmeCosFunction.getA21())?hmeCosFunction.getA21().toString():null;
                break;
            case "A22":
                resultFunction = Objects.nonNull(hmeCosFunction.getA22())?hmeCosFunction.getA22().toString():null;
                break;
            case "A23":
                resultFunction = Objects.nonNull(hmeCosFunction.getA23())?hmeCosFunction.getA23().toString():null;
                break;
            case "A24":
                resultFunction = hmeCosFunction.getA24();
                break;
            case "A25":
                resultFunction = hmeCosFunction.getA25();
                break;
            case "A26":
                resultFunction = hmeCosFunction.getA26();
                break;
            default:
                break;
        }
        return resultFunction;
    }

}