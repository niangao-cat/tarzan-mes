package com.ruike.hme.infra.repository.impl;

import cn.hutool.core.date.DateUtil;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO2;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO3;
import com.ruike.hme.api.dto.HmeServiceSplitRecordLineDTO;
import com.ruike.hme.domain.entity.HmeEoJobMaterial;
import com.ruike.hme.domain.entity.HmeRepairWorkOrderCreate;
import com.ruike.hme.domain.entity.HmeServiceReceive;
import com.ruike.hme.domain.entity.HmeServiceReceiveHis;
import com.ruike.hme.domain.entity.HmeServiceSplitRecord;
import com.ruike.hme.domain.repository.HmeServiceReceiveHisRepository;
import com.ruike.hme.domain.repository.HmeServiceReceiveRepository;
import com.ruike.hme.domain.repository.HmeEoJobMaterialRepository;
import com.ruike.hme.domain.repository.HmeServiceSplitRecordRepository;
import com.ruike.hme.domain.service.HmeOrganizationService;
import com.ruike.hme.domain.vo.HmeServiceSplitBomHeaderVO;
import com.ruike.hme.domain.vo.HmeServiceSplitBomLineVO;
import com.ruike.hme.domain.vo.HmeServiceSplitRecordVO;
import com.ruike.hme.domain.vo.HmeServiceSplitReturnCheckVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobMaterialMapper;
import com.ruike.hme.infra.mapper.HmeServiceReceiveMapper;
import com.ruike.hme.infra.mapper.HmeServiceSplitRecordMapper;
import com.ruike.hme.infra.util.BeanCopierUtil;
import com.ruike.itf.app.service.ItfRepairWorkOrderCreateService;
import com.ruike.wms.api.dto.WmsTransactionTypeDTO;
import com.ruike.wms.domain.repository.WmsObjectTransactionRepository;
import com.ruike.wms.domain.repository.WmsTransactionTypeRepository;
import com.ruike.wms.domain.vo.WmsObjectTransactionRequestVO;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.vo.MtExtendVO10;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.boot.platform.profile.ProfileClient;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.iface.domain.repository.MtMaterialBasisRepository;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtMaterialSite;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtMaterialSiteRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.MtBom;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.method.domain.repository.MtBomRepository;
import tarzan.method.domain.vo.MtBomComponentVO7;
import tarzan.method.domain.vo.MtBomVO6;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtWorkOrderVO;
import tarzan.order.domain.vo.MtWorkOrderVO28;
import tarzan.order.infra.mapper.MtWorkOrderMapper;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.ONE;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;
import static com.ruike.hme.infra.constant.HmeConstants.EventType.AF_MATERIAL_LOT_CREATE;

/**
 * 售后返品拆机表 资源库实现
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 14:21:01
 */
@Component
public class HmeServiceSplitRecordRepositoryImpl extends BaseRepositoryImpl<HmeServiceSplitRecord>
        implements HmeServiceSplitRecordRepository {

    private final HmeServiceSplitRecordMapper hmeServiceSplitRecordMapper;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtUserClient userClient;
    private final LovAdapter lovAdapter;
    private final MtCustomDbRepository customDbRepository;
    private final MtEventRepository mtEventRepository;
    private final MtWorkOrderRepository mtWorkOrderRepository;
    private final MtModProductionLineRepository mtModProductionLineRepository;
    private final MtBomComponentRepository mtBomComponentRepository;
    private final MtBomRepository mtBomRepository;
    private final MtWorkOrderMapper mtWorkOrderMapper;
    private final MtModLocatorRepository mtModLocatorRepository;
    private final MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository;
    private final MtMaterialRepository mtMaterialRepository;
    private final WmsTransactionTypeRepository wmsTransactionTypeRepository;
    private final MtUomRepository mtUomRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final ItfRepairWorkOrderCreateService itfRepairWorkOrderCreateService;
    private final MtModSiteRepository mtModSiteRepository;
    private final WmsObjectTransactionRepository wmsObjectTransactionRepository;
    private final ProfileClient profileClient;
    private final MtMaterialBasisRepository mtMaterialBasisRepository;
    private final MtMaterialSiteRepository mtMaterialSiteRepository;
    private final HmeOrganizationService organizationService;
    private final HmeServiceReceiveRepository hmeServiceReceiveRepository;
    private final HmeServiceReceiveMapper hmeServiceReceiveMapper;
    private final HmeServiceReceiveHisRepository hmeServiceReceiveHisRepository;
    private final HmeEoJobMaterialRepository hmeEoJobMaterialRepository;
    private final HmeEoJobMaterialMapper hmeEoJobMaterialMapper;

    public HmeServiceSplitRecordRepositoryImpl(MtWorkOrderRepository mtWorkOrderRepository, HmeServiceSplitRecordMapper hmeServiceSplitRecordMapper, MtErrorMessageRepository mtErrorMessageRepository, MtUserClient userClient, MtModLocatorRepository mtModLocatorRepository, MtMaterialRepository mtMaterialRepository, LovAdapter lovAdapter, MtCustomDbRepository customDbRepository, MtInvOnhandQuantityRepository mtInvOnhandQuantityRepository, MtEventRepository mtEventRepository, MtModProductionLineRepository mtModProductionLineRepository, WmsTransactionTypeRepository wmsTransactionTypeRepository, MtUomRepository mtUomRepository, MtMaterialLotRepository mtMaterialLotRepository, MtBomComponentRepository mtBomComponentRepository, MtBomRepository mtBomRepository, MtWorkOrderMapper mtWorkOrderMapper, ItfRepairWorkOrderCreateService itfRepairWorkOrderCreateService, MtModSiteRepository mtModSiteRepository, WmsObjectTransactionRepository wmsObjectTransactionRepository, ProfileClient profileClient, MtMaterialBasisRepository mtMaterialBasisRepository, MtMaterialSiteRepository mtMaterialSiteRepository, MtUserOrganizationRepository userOrganizationRepository, MtModAreaRepository areaRepository, HmeOrganizationService organizationService, HmeServiceReceiveRepository hmeServiceReceiveRepository, HmeServiceReceiveMapper hmeServiceReceiveMapper, HmeServiceReceiveHisRepository hmeServiceReceiveHisRepository, HmeEoJobMaterialRepository hmeEoJobMaterialRepository, HmeEoJobMaterialMapper hmeEoJobMaterialMapper) {
        this.mtWorkOrderRepository = mtWorkOrderRepository;
        this.hmeServiceSplitRecordMapper = hmeServiceSplitRecordMapper;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.userClient = userClient;
        this.mtModLocatorRepository = mtModLocatorRepository;
        this.mtMaterialRepository = mtMaterialRepository;
        this.lovAdapter = lovAdapter;
        this.customDbRepository = customDbRepository;
        this.mtInvOnhandQuantityRepository = mtInvOnhandQuantityRepository;
        this.mtEventRepository = mtEventRepository;
        this.mtModProductionLineRepository = mtModProductionLineRepository;
        this.wmsTransactionTypeRepository = wmsTransactionTypeRepository;
        this.mtUomRepository = mtUomRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.mtBomComponentRepository = mtBomComponentRepository;
        this.mtBomRepository = mtBomRepository;
        this.mtWorkOrderMapper = mtWorkOrderMapper;
        this.itfRepairWorkOrderCreateService = itfRepairWorkOrderCreateService;
        this.mtModSiteRepository = mtModSiteRepository;
        this.wmsObjectTransactionRepository = wmsObjectTransactionRepository;
        this.profileClient = profileClient;
        this.mtMaterialBasisRepository = mtMaterialBasisRepository;
        this.mtMaterialSiteRepository = mtMaterialSiteRepository;
        this.organizationService = organizationService;
        this.hmeServiceReceiveRepository = hmeServiceReceiveRepository;
        this.hmeServiceReceiveMapper = hmeServiceReceiveMapper;
        this.hmeServiceReceiveHisRepository = hmeServiceReceiveHisRepository;
        this.hmeEoJobMaterialRepository = hmeEoJobMaterialRepository;
        this.hmeEoJobMaterialMapper = hmeEoJobMaterialMapper;
    }

    @Override
    @ProcessLovValue(targetField = {"", "recordLineList", ""})
    public HmeServiceSplitRecordDTO splitRecordBySnQuery(Long tenantId, String snNum) {
        HmeServiceSplitRecordDTO record = hmeServiceSplitRecordMapper.selectSplitRecordBySn(tenantId, snNum);
        if (Objects.isNull(record)) {
            throw new MtException("HME_SPLIT_RECORD_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0002", "HME"));
        }
        // 获取签收人
        if (record.getSignForBy() != null) {
            record.setSignForByName(userClient.userInfoGet(tenantId, record.getSignForBy()).getRealName());
        }
        // 获取拆箱人
        if (record.getReceiveBy() != null) {
            record.setReceiveByName(userClient.userInfoGet(tenantId, record.getReceiveBy()).getRealName());
        }
        List<HmeServiceSplitRecordLineDTO> lineRecordList =
                hmeServiceSplitRecordMapper.selectSplitRecordLine(tenantId, record.getSplitRecordId());
        lineRecordList.forEach(line -> {
            // 获取拆机人
            line.setSplitByName(userClient.userInfoGet(tenantId, line.getSplitBy()).getRealName());
        });
        record.setRecordLineList(lineRecordList);
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeServiceSplitRecordDTO3 splitRecordInsert(Long tenantId, HmeServiceSplitRecordVO vo) {
        // 获取当前用户
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 校验sn是否为整机sn
        HmeServiceSplitRecordDTO2 receive =
                hmeServiceSplitRecordMapper.selectServiceReceive(tenantId, vo.getSnNum(), vo.getSiteId());
        if (Objects.isNull(receive)) {
            throw new MtException("HME_SPLIT_RECORD_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0003", "HME"));
        }
        List<MtModLocator> locators = mtModLocatorRepository.selectByCondition(Condition.builder(MtModLocator.class).where(Sqls.custom().andEqualTo(MtModLocator.FIELD_PARENT_LOCATOR_ID, receive.getLocatorId()).andEqualTo(MtModLocator.FIELD_LOCATOR_TYPE, "DEFAULT_STORAGE").andEqualTo(MtModLocator.FIELD_ENABLE_FLAG, YES).andEqualTo(MtModLocator.FIELD_TENANT_ID, tenantId)).build());
        if (CollectionUtils.isEmpty(locators)) {
            throw new MtException("HME_SPLIT_RECORD_0023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0023", "HME", mtModLocatorRepository.selectByPrimaryKey(receive.getLocatorId()).getLocatorCode()));
        }
        MtModLocator locator = locators.get(0);
        MtMaterialLot newSn = this.splitMaterialLotCreate(tenantId, receive.getMaterialId(), vo, locator.getLocatorId());
        // 校验返回类型是否为【客户机】
        String tag = getLovTag(tenantId, receive.getBackType());
        // 若为客户机，创建工单
        String workOrderNum;
        HmeServiceSplitRecordDTO3 dto = new HmeServiceSplitRecordDTO3();
        dto.setMaterialId(receive.getMaterialId());
        dto.setMaterialCode(receive.getMaterialCode());
        dto.setUomId(receive.getUomId());
        dto.setUomCode(receive.getUomCode());
        dto.setSiteId(vo.getSiteId());
        if (StringUtils.equals(HmeConstants.BackTypeTag.CUSTOMER, tag)) {
            // 获取产线"-1"
            MtModProductionLine prodLine = mtModProductionLineRepository.selectOne(new MtModProductionLine() {
                {
                    setTenantId(tenantId);
                    setProdLineCode(HmeConstants.ConstantValue.STRING_MINUS_ONE);
                }
            });
            if (Objects.isNull(prodLine)) {
                throw new MtException("HME_SPLIT_RECORD_0009", mtErrorMessageRepository.getErrorMessageWithModule(
                        tenantId, "HME_SPLIT_RECORD_0009", "HME", HmeConstants.ConstantValue.STRING_MINUS_ONE));
            }
            dto.setProductionLineId(prodLine.getProdLineId());
            // 获取SAP内部订单失败,请检查或重推SAP返厂实物登记信息
            if (StringUtils.isBlank(receive.getWorkOrderId())) {
                throw new MtException("HME_SPLIT_RECORD_0022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_SPLIT_RECORD_0022", "HME"));
            }
            workOrderNum = this.createWorkOrder(tenantId, vo.getSnNum(), receive.getMaterialId(), vo.getSiteId(), locator.getLocatorId());
        } else {
            // 公司机
            workOrderNum = hmeServiceSplitRecordMapper.selectWorkOrderNum(tenantId, vo.getSnNum());
            if (StringUtils.isBlank(workOrderNum)) {
                throw new MtException("HME_SPLIT_RECORD_0013", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_SPLIT_RECORD_0013", "HME", vo.getSnNum()));
            }
        }
        String splitRecordId = customDbRepository.getNextKey("hme_service_split_record_s");
        Long cid = Long.valueOf(customDbRepository.getNextKey("hme_service_split_record_cid_s"));
        HmeServiceSplitRecord record = new HmeServiceSplitRecord();
        record.setTenantId(tenantId);
        record.setSplitRecordId(splitRecordId);
        record.setTopSplitRecordId(record.getSplitRecordId());
        record.setParentSplitRecordId(record.getSplitRecordId());
        record.setServiceReceiveId(receive.getServiceReceiveId());
        record.setAfterSalesRepairId(receive.getAfterSalesRepairId());
        record.setSiteId(receive.getSiteId());
        record.setMaterialId(receive.getMaterialId());
        record.setSnNum(newSn.getMaterialLotCode());
        record.setInternalOrderNum(receive.getWorkOrderId());
        record.setWorkOrderNum(workOrderNum);
        record.setLocatorId(receive.getLocatorId());
        record.setBackType(receive.getBackType());
        record.setIsRepair(YES);
        record.setIsOnhand(YES);
        record.setSplitStatus(HmeConstants.SplitStatus.WAIT_SPLIT);
        record.setSplitBy(userId);
        record.setSplitTime(new Date());
        record.setWkcShiftId(vo.getWkcShiftId());
        record.setOperationId(vo.getOperationId());
        record.setWorkcellId(vo.getWorkcellId());
        record.setMaterialLotId(newSn.getMaterialLotId());
        record.setCid(cid);
        hmeServiceSplitRecordMapper.insertSelective(record);
        dto.setSplitRecordId(splitRecordId);
        dto.setIssuedLocatorId(locator.getLocatorId());
        dto.setNewMaterialLot(newSn);
        dto.setLotCode(newSn.getLot());
        dto.setInternalOrderNum(receive.getWorkOrderId());
        dto.setWarehouseId(receive.getLocatorId());
        return dto;
    }

    @Override
    public MtMaterialLot splitMaterialLotCreate(Long tenantId, String materialId, HmeServiceSplitRecordVO vo, String locatorId) {
        MtMaterialLot materialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setMaterialLotCode(vo.getSnNum());
        }});
        if (Objects.nonNull(materialLot) && YES.equals(materialLot.getEnableFlag())) {
            throw new MtException("HME_SPLIT_RECORD_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_SPLIT_RECORD_0020", "HME", mtMaterialRepository.selectByPrimaryKey(materialLot.getMaterialId()).getMaterialCode()));
        }
        //查询值集HME.NOT_CREATE_BATCH_NUM
        List<String> createBatchNumList = new ArrayList<>();
        List<LovValueDTO> lovValueDTOList = lovAdapter.queryLovValue("HME.NOT_CREATE_BATCH_NUM", tenantId);
        if (CollectionUtils.isNotEmpty(lovValueDTOList)) {
            createBatchNumList = lovValueDTOList.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        }

        //获取批次编码
        MtMaterialBasic mtMaterialBasic = getMtMaterialBasic(tenantId, materialId, vo.getSiteId());
        // 物料类型
        String itemType = Objects.isNull(mtMaterialBasic) ? "" : mtMaterialBasic.getItemType();
        String lotCode = "";
        if (StringUtils.isBlank(itemType) || !createBatchNumList.contains(itemType)) {
            //查询系统参数 HME_MATERIAL_BATCH_NUM 作为批次
            lotCode = profileClient.getProfileValueByOptions("HME_MATERIAL_BATCH_NUM");
            if (StringUtils.isBlank(lotCode)) {
                //默认批次获取失败,请联系管理员通过【配置维护】功能维护【HME_MATERIAL_BATCH_NUM】
                throw new MtException("HME_EO_JOB_SN_113", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_113", "HME"));
            } else if (lotCode.length() != 10) {
                //默认批次必须为10位,请联系管理员通过【配置维护】功能维护【HME_MATERIAL_BATCH_NUM】
                throw new MtException("HME_EO_JOB_SN_114", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_114", "HME"));
            }
        }
        if (Objects.nonNull(materialLot)) {
            // 20210529 add by sanfeng.zhang for fang.pan 判断物料批ID是否在hme_eo_job_material 中存在IsIssued = 1 的数据
            List<HmeEoJobMaterial> hmeEoJobMaterialList = hmeEoJobMaterialRepository.selectByCondition(Condition.builder(HmeEoJobMaterial.class).andWhere(Sqls.custom()
                    .andEqualTo(HmeEoJobMaterial.FIELD_TENANT_ID, tenantId)
                    .andEqualTo(HmeEoJobMaterial.FIELD_MATERIAL_LOT_ID, materialLot.getMaterialLotId())).build());
            for (HmeEoJobMaterial eoJobMaterial : hmeEoJobMaterialList) {
                // 20210721 modify by sanfeng.zhang for peng.zhao 是否已投标识 可能为空 为空则默认0
                if (eoJobMaterial.getIsIssued() == null) {
                    eoJobMaterial.setIsIssued(0);
                }
                if (eoJobMaterial.getIsIssued().compareTo(ONE) == 0) {
                    // 新建一行退料记录，再更新原记录为负的；
                    HmeEoJobMaterial existsHmeEoJobMaterial = hmeEoJobMaterialRepository.selectByPrimaryKey(eoJobMaterial.getJobMaterialId());
                    if (Objects.nonNull(existsHmeEoJobMaterial)) {
                        HmeEoJobMaterial hmeEoJobMaterial = new HmeEoJobMaterial();
                        hmeEoJobMaterial.setTenantId(tenantId);
                        hmeEoJobMaterial.setJobId(existsHmeEoJobMaterial.getJobId());
                        hmeEoJobMaterial.setWorkcellId(existsHmeEoJobMaterial.getWorkcellId());
                        hmeEoJobMaterial.setSnMaterialId(existsHmeEoJobMaterial.getSnMaterialId());
                        hmeEoJobMaterial.setMaterialId(existsHmeEoJobMaterial.getMaterialId());
                        hmeEoJobMaterial.setMaterialLotId(existsHmeEoJobMaterial.getMaterialLotId());
                        hmeEoJobMaterial.setMaterialLotCode(existsHmeEoJobMaterial.getMaterialLotCode());
                        hmeEoJobMaterial.setReleaseQty(existsHmeEoJobMaterial.getReleaseQty().multiply(new BigDecimal(-1)));
                        hmeEoJobMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                        hmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ZERO);
                        hmeEoJobMaterial.setEoId(existsHmeEoJobMaterial.getEoId());
                        hmeEoJobMaterial.setBomComponentId(existsHmeEoJobMaterial.getBomComponentId());
                        hmeEoJobMaterial.setBydMaterialId(existsHmeEoJobMaterial.getBydMaterialId());
                        hmeEoJobMaterial.setLocatorId(existsHmeEoJobMaterial.getLocatorId());
                        hmeEoJobMaterial.setLotCode(existsHmeEoJobMaterial.getLotCode());
                        hmeEoJobMaterial.setProductionVersion(existsHmeEoJobMaterial.getProductionVersion());
                        hmeEoJobMaterial.setVirtualFlag(existsHmeEoJobMaterial.getVirtualFlag());
                        hmeEoJobMaterial.setParentMaterialLotId(existsHmeEoJobMaterial.getParentMaterialLotId());
                        hmeEoJobMaterial.setRemainQty(existsHmeEoJobMaterial.getRemainQty());
                        hmeEoJobMaterialRepository.insert(hmeEoJobMaterial);

                        //更新关系（记录）表is_issued
                        existsHmeEoJobMaterial.setIsIssued(HmeConstants.ConstantValue.ZERO);
                        if (Objects.nonNull(existsHmeEoJobMaterial.getRemainQty())) {
                            existsHmeEoJobMaterial.setRemainQty(existsHmeEoJobMaterial.getRemainQty().subtract(existsHmeEoJobMaterial.getReleaseQty()));
                        }
                        existsHmeEoJobMaterial.setMaterialLotId(null);
                        existsHmeEoJobMaterial.setMaterialLotCode(null);
                        hmeEoJobMaterialMapper.updateByPrimaryKey(existsHmeEoJobMaterial);
                    }
                }
            }
        }
        // 不存在sn，则创建条码
        if (Objects.isNull(materialLot) || !YES.equals(materialLot.getEnableFlag())) {
            HmeServiceSplitRecordDTO3 materialLotCreate = new HmeServiceSplitRecordDTO3();
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialId);
            materialLotCreate.setEventCode(AF_MATERIAL_LOT_CREATE);
            materialLotCreate.setCreateReason("CUSTOMER");
            materialLotCreate.setMaterialId(materialId);
            materialLotCreate.setSiteId(vo.getSiteId());
            materialLotCreate.setIssuedLocatorId(locatorId);
            materialLotCreate.setUomId(mtMaterial.getPrimaryUomId());
            materialLotCreate.setMaterialLotCode(vo.getSnNum());
            materialLotCreate.setLotCode(lotCode);
            materialLotCreate.setMaterialLotId(Objects.isNull(materialLot) ? null : materialLot.getMaterialLotId());
            //实物返回属性
            materialLotCreate.setBackType(vo.getBackType());
            return self().materialLotCreate(tenantId, materialLotCreate);
        } else {
            return materialLot;
        }
    }

    @Override
    public String createWorkOrder(Long tenantId, String snNum, String materialId, String siteId, String locatorId) {
        MtModLocator locator = hmeServiceSplitRecordMapper.selectParentLocator(tenantId, locatorId);
        if (Objects.isNull(locator)) {
            throw new MtException("HME_SPLIT_RECORD_0019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_SPLIT_RECORD_0019", "HME", mtModLocatorRepository.selectByPrimaryKey(locatorId).getLocatorCode()));
        }
        String productionVersion = productionVersionGet(tenantId);
        MtMaterial material = mtMaterialRepository.selectByPrimaryKey(materialId);
        MtModSite site = mtModSiteRepository.selectByPrimaryKey(siteId);
        MtUom uom = mtUomRepository.selectByPrimaryKey(material.getPrimaryUomId());
        HmeRepairWorkOrderCreate create = new HmeRepairWorkOrderCreate();
        Date startDate = DateUtil.date();
        Date endDate = DateUtils.addDays(startDate, 30);
        create.setMaterialLotCode(snNum);
        create.setMaterialCode(material.getMaterialCode());
        create.setQty(1D);
        create.setPrimaryUomCode(uom.getUomCode());
        create.setSiteCode(site.getSiteCode());
        create.setLocatorCode(locator.getLocatorCode());
        create.setPlanStartTime(startDate);
        create.setPlanEndTime(endDate);
        create.setProductionVersion(productionVersion);
        HmeRepairWorkOrderCreate result = itfRepairWorkOrderCreateService.hmeRepairWorkOrderCreateService(tenantId, create);
        return result.getWorkOrderNum();
    }

    @Override
    public String productionVersionGet(Long tenantId) {
        MtModArea area = organizationService.getUserDefaultArea(tenantId);
        List<LovValueDTO> valueList = lovAdapter.queryLovValue("HME.AFWO_PRODUCTION_VERSION", tenantId).stream().sorted(Comparator.comparing(LovValueDTO::getOrderSeq)).collect(Collectors.toList());
        Optional<LovValueDTO> productionVersionValueOpl = valueList.stream().filter(rec -> area.getAreaCode().equals(rec.getValue())).findFirst();
        if (!productionVersionValueOpl.isPresent()) {
            throw new MtException("HME_SPLIT_RECORD_0025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_SPLIT_RECORD_0025", "HME"));
        }
        return productionVersionValueOpl.get().getMeaning();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerCancel(Long tenantId, String serviceReceiveId) {
        HmeServiceReceive hmeServiceReceive = hmeServiceReceiveRepository.selectByPrimaryKey(serviceReceiveId);
        hmeServiceReceive.setReceiveStatus("CANCEL");
        hmeServiceReceiveMapper.updateByPrimaryKeySelective(hmeServiceReceive);
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("SERVICE_RECEIVE_CANCEL");
        }});
        HmeServiceReceiveHis hmeServiceReceiveHis = new HmeServiceReceiveHis();
        BeanCopierUtil.copy(hmeServiceReceive, hmeServiceReceiveHis);
        hmeServiceReceiveHis.setEventId(eventId);
        hmeServiceReceiveHisRepository.insertSelective(hmeServiceReceiveHis);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeServiceSplitRecord splitRecordLineInsert(Long tenantId, HmeServiceSplitRecordDTO3 dto) {
        HmeServiceSplitRecord record = this.selectByPrimaryKey(dto.getSplitRecordId());
        if (Objects.isNull(record)) {
            throw new MtException("HME_SPLIT_RECORD_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0006", "HME"));
        }
        // 获取当前用户默认站点
        Long userId = DetailsHelper.getUserDetails().getUserId();
        // 拆机行生成
        HmeServiceSplitRecord recordLine = new HmeServiceSplitRecord();
        recordLine.setTenantId(tenantId);
        recordLine.setServiceReceiveId(record.getServiceReceiveId());
        recordLine.setAfterSalesRepairId(record.getAfterSalesRepairId());
        recordLine.setSnNum(dto.getMaterialLotCode());
        recordLine.setMaterialLotId(dto.getMaterialLotId());
        recordLine.setSiteId(record.getSiteId());
        recordLine.setMaterialId(dto.getMaterialId());
        recordLine.setTopSplitRecordId(record.getTopSplitRecordId());
        recordLine.setParentSplitRecordId(record.getSplitRecordId());
        recordLine.setInternalOrderNum(record.getInternalOrderNum());
        recordLine.setWorkOrderNum(dto.getWorkOrderNum());
        recordLine.setLocatorId(record.getLocatorId());
        recordLine.setBackType(record.getBackType());
        recordLine.setIsRepair(record.getIsRepair());
        recordLine.setIsOnhand(record.getIsOnhand());
        recordLine.setSplitStatus(HmeConstants.SplitStatus.WAIT_SPLIT);
        recordLine.setSplitBy(userId);
        recordLine.setSplitTime(new Date());
        recordLine.setWkcShiftId(dto.getWkcShiftId());
        recordLine.setWorkcellId(dto.getWorkcellId());
        recordLine.setOperationId(dto.getOperationId());
        self().insertSelective(recordLine);
        return recordLine;
    }

    @Override
    public HmeServiceSplitRecordDTO3 materialLotGet(Long tenantId, String materialLotCode) {
        return hmeServiceSplitRecordMapper.selectMaterialLot(tenantId, materialLotCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String workOrderUpdate(Long tenantId, HmeServiceSplitRecordDTO3 dto) {
        // 获取物料类型
        String materialCategory = hmeServiceSplitRecordMapper.queryMaterialCategory(tenantId, dto.getMaterialId(),
                dto.getSiteId());
        if (StringUtils.isBlank(materialCategory)) {
            throw new MtException("HME_SPLIT_RECORD_0014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0014", "HME", dto.getMaterialCode()));
        }
        List<LovValueDTO> lovValueList = lovAdapter.queryLovValue(HmeConstants.LovCode.PRODUCTION_CATEGORY, tenantId);
        Optional<LovValueDTO> lovValueDTO =
                lovValueList.stream().filter(t -> materialCategory.equals(t.getValue())).findFirst();
        if (!lovValueDTO.isPresent()) {
            throw new MtException("HME_SPLIT_RECORD_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0015", "HME", dto.getMaterialCode(), materialCategory));
        }
        String routerCode = lovValueDTO.get().getParentValue();
        if (StringUtils.isBlank(routerCode)) {
            throw new MtException("HME_SPLIT_RECORD_0016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0016", "HME", dto.getMaterialCode(), materialCategory));
        }
        // 获取工艺路线
        String routerId = hmeServiceSplitRecordMapper.queryRouterId(tenantId, routerCode, dto.getSiteId());
        if (StringUtils.isBlank(routerId)) {
            throw new MtException("HME_SPLIT_RECORD_0017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0017", "HME", routerCode));
        }
        // 创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(HmeConstants.EventType.WO_CREATE);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        // 工单创建
        MtWorkOrderVO mtWorkOrderVO = new MtWorkOrderVO();
        mtWorkOrderVO.setEventId(eventId);
        mtWorkOrderVO.setMaterialId(dto.getMaterialId());
        mtWorkOrderVO.setProductionLineId(dto.getProductionLineId());
        mtWorkOrderVO.setQty(1D);
        mtWorkOrderVO.setSiteId(dto.getSiteId());
        mtWorkOrderVO.setStatus(HmeConstants.StatusCode.NEW);
        mtWorkOrderVO.setValidateFlag(YES);
        mtWorkOrderVO.setWorkOrderType("MES_RK06");
        mtWorkOrderVO.setRouterId(routerId);
        MtWorkOrderVO28 workOrderVO =
                mtWorkOrderRepository.woUpdate(tenantId, mtWorkOrderVO, HmeConstants.ConstantValue.NO);
        if (Objects.isNull(workOrderVO) || Strings.isBlank(workOrderVO.getWorkOrderId())) {
            throw new MtException("HME_SPLIT_RECORD_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0002", "HME"));
        }
        return workOrderVO.getWorkOrderId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeServiceSplitRecordDTO3 bomUpdate(Long tenantId, HmeServiceSplitRecordDTO3 dto) {
        // 创建生产指令BOM清单
        HmeServiceSplitRecord record = this.selectByPrimaryKey(dto.getSplitRecordId());
        // 获取主件workOrderId
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectOne(new MtWorkOrder() {{
            setTenantId(tenantId);
            setWorkOrderNum(record.getWorkOrderNum());
        }});
        if (Objects.isNull(mtWorkOrder)) {
            throw new MtException("HME_SPLIT_RECORD_0010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0010", "HME"));
        }
        String bomId = Strings.isBlank(mtWorkOrder.getBomId()) ? null : mtWorkOrder.getBomId();
        Long lineNum = 0L;
        String bomStatus = HmeConstants.StatusCode.NEW;
        if (Strings.isNotBlank(bomId)) {
            // 获取BOM状态
            MtBom bom = mtBomRepository.selectByPrimaryKey(bomId);
            if (!Objects.isNull(bom)) {
                bomStatus = bom.getBomStatus();
            }
            // 获取BOM行号
            List<MtBomComponent> bomComps = mtBomComponentRepository.select(new MtBomComponent() {
                {
                    setTenantId(tenantId);
                    setBomId(mtWorkOrder.getBomId());
                }
            });
            // 存在bom行，则取最大行号
            if (CollectionUtils.isNotEmpty(bomComps)) {
                List<Long> lineNums = bomComps.stream().map(MtBomComponent::getLineNumber).collect(Collectors.toList());
                lineNum = Collections.max(lineNums);
            }
        }
        // 获取货位
        List<LovValueDTO> lovValueList = lovAdapter.queryLovValue(HmeConstants.LovCode.AF_ISSUED_LOCATOR, tenantId);
        if (CollectionUtils.isEmpty(lovValueList)) {
            throw new MtException("HME_SPLIT_RECORD_0011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0011", "HME", HmeConstants.LovCode.AF_ISSUED_LOCATOR));
        }
        // 获取最小排序号
        LovValueDTO lovValueDTO = lovValueList.stream().min(Comparator.comparing(LovValueDTO::getOrderSeq))
                .orElse(new LovValueDTO());
        String locatorCode = lovValueDTO.getValue();
        MtModLocator locator = mtModLocatorRepository.selectOne(new MtModLocator() {
            {
                setTenantId(tenantId);
                setLocatorCode(locatorCode);
                setEnableFlag(YES);
            }
        });
        if (Objects.isNull(locator)) {
            throw new MtException("HME_SPLIT_RECORD_0012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0012", "HME", locatorCode));
        }
        dto.setIssuedLocatorId(locator.getLocatorId());
        lineNum = lineNum + 10L;
        Date now = new Date();
        // 创建组件
        MtBomVO6 bomVO = new MtBomVO6();
        bomVO.setBomId(bomId);
        bomVO.setBomName(mtWorkOrder.getWorkOrderNum());
        bomVO.setBomType("WO");
        bomVO.setDateFrom(now);
        bomVO.setBomStatus(bomStatus);
        bomVO.setRevision("MAIN");
        // 组件行
        MtBomComponentVO7 bomComponentVO = new MtBomComponentVO7();
        bomComponentVO.setLineNumber(lineNum);
        bomComponentVO.setMaterialId(dto.getMaterialId());
        bomComponentVO.setBomComponentType("ASSEMBLING");
        bomComponentVO.setDateFrom(now);
        bomComponentVO.setQty(1D);
        bomComponentVO.setAssembleMethod("ISSUE");
        bomComponentVO.setIssuedLocatorId(dto.getIssuedLocatorId());
        List<MtBomComponentVO7> mtBomComponentList = Collections.singletonList(bomComponentVO);
        bomVO.setMtBomComponentList(mtBomComponentList);
        String newBonId = mtBomRepository.bomAllUpdate(tenantId, bomVO);
        // 若生产指令为空，则回写
        if (Strings.isBlank(bomId)) {
            mtWorkOrder.setBomId(newBonId);
            mtWorkOrderMapper.updateByPrimaryKey(mtWorkOrder);
        }
        return dto;
    }

    @Override
    public void onhandQtyUpdateProcess(Long tenantId, HmeServiceSplitRecordDTO3 dto, MtMaterialLot materialLot) {
        HmeServiceSplitRecord record = this.selectByPrimaryKey(dto.getSplitRecordId());
        // 获取返回属性类型
        String tag = getLovTag(tenantId, record.getBackType());
        // 获取工单编号
        if (StringUtils.equals(tag, HmeConstants.BackTypeTag.OWN)) {
            // 获取顶层拆机信息
            HmeServiceSplitRecord topRecord = this.selectByPrimaryKey(record.getTopSplitRecordId());
            // 获取顶层拆机信息的工单num
            MtWorkOrder workOrder = mtWorkOrderRepository.selectOne(new MtWorkOrder() {{
                setTenantId(tenantId);
                setWorkOrderNum(topRecord.getWorkOrderNum());
            }});
            if (Objects.isNull(workOrder)) {
                throw new MtException("HME_SPLIT_RECORD_0018", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_SPLIT_RECORD_0018", "HME"));
            }
            dto.setInternalOrderNum(null);
            dto.setWorkOrderNum(workOrder.getWorkOrderNum());
        } else {
            dto.setInternalOrderNum(record.getInternalOrderNum());
            dto.setWorkOrderNum(null);
        }
        // 获取物料信息
        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(dto.getMaterialId());
        // 获取物料单位信息
        MtUom uom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
        dto.setUomId(uom.getUomId());
        dto.setUomCode(uom.getUomCode());
        // 创建事务
        String transactionCode = dto.getRepairSnFlag() ? HmeConstants.TransactionTypeCode.AF_ZSD005 : (StringUtils.equals(tag, HmeConstants.BackTypeTag.OWN) ? HmeConstants.TransactionTypeCode.HME_WO_ISSUE_R_EXT : HmeConstants.TransactionTypeCode.WMS_INSDID_ORDER_S_R);
        // 创建事件
        String eventTypeCode = dto.getRepairSnFlag() ? HmeConstants.EventType.AF_ZSD005 : (StringUtils.equals(tag, HmeConstants.BackTypeTag.OWN) ? HmeConstants.EventType.UNPLANNED_OUTPUT : HmeConstants.EventType.INTERNAL_ORDER_OUTPUT);
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(eventTypeCode);
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        // 库存变更
        MtInvOnhandQuantityVO9 vo = new MtInvOnhandQuantityVO9();
        vo.setEventId(eventId);
        vo.setSiteId(dto.getSiteId());
        vo.setMaterialId(dto.getMaterialId());
        vo.setLocatorId(dto.getIssuedLocatorId());
        vo.setLotCode(dto.getLotCode());
        vo.setChangeQuantity(1D);
        mtInvOnhandQuantityRepository.onhandQtyUpdateProcess(tenantId, vo);
        // 创建事务
        createObjectTransaction(tenantId, eventId, transactionCode, materialLot, dto);
    }

    @Override
    public HmeServiceSplitBomHeaderVO selectLatestBomByMaterial(Long tenantId, String siteId, String materialId) {
        return hmeServiceSplitRecordMapper.selectLatestBomByMaterial(tenantId, siteId, materialId);
    }

    @Override
    public List<HmeServiceSplitBomLineVO> selectBomLineList(Long tenantId, String siteId, String bomId) {
        return hmeServiceSplitRecordMapper.selectBomLineList(tenantId, siteId, bomId);
    }

    /**
     * 获取mt_material_basic信息
     *
     * @param tenantId
     * @param materialId
     * @return
     */
    private MtMaterialBasic getMtMaterialBasic(Long tenantId, String materialId, String siteId) {
        MtMaterialSite site = new MtMaterialSite();
        site.setTenantId(tenantId);
        site.setMaterialId(materialId);
        site.setSiteId(siteId);
        MtMaterialSite materialSite = mtMaterialSiteRepository.selectOne(site);
        if (Objects.isNull(materialSite)) {
            return new MtMaterialBasic();
        }
        return mtMaterialBasisRepository.selectByPrimaryKey(materialSite.getMaterialSiteId());
    }

    private void createObjectTransaction(Long tenantId, String eventId, String transactionCode,
                                         MtMaterialLot materialLot, HmeServiceSplitRecordDTO3 dto) {
        // 获取货位ID
        MtModLocator loc = mtModLocatorRepository.selectByPrimaryKey(dto.getIssuedLocatorId());
        // 获取事务信息
        WmsTransactionTypeDTO type = wmsTransactionTypeRepository.getTransactionType(tenantId, transactionCode);
        WmsObjectTransactionRequestVO trxVo = new WmsObjectTransactionRequestVO();
        trxVo.setTransactionId(null);
        trxVo.setTransactionTypeCode(transactionCode);
        trxVo.setEventId(eventId);
        trxVo.setMaterialLotId(materialLot.getMaterialLotId());
        trxVo.setPlantId(dto.getSiteId());
        trxVo.setMaterialId(dto.getMaterialId());
        trxVo.setTransactionQty(BigDecimal.ONE);
        trxVo.setLotNumber(materialLot.getLot());
        trxVo.setTransactionUom(dto.getUomCode());
        trxVo.setTransactionTime(new Date());
        trxVo.setWarehouseId(loc.getParentLocatorId());
        trxVo.setLocatorId(dto.getIssuedLocatorId());
        trxVo.setInsideOrder(dto.getInternalOrderNum());
        trxVo.setWorkOrderNum(dto.getWorkOrderNum());
        trxVo.setMoveType(type.getMoveType());
        trxVo.setRemark(dto.getRepairSnFlag() ? "拆机售后大仓平台登记，不传sap" : "售后拆机产出");
        wmsObjectTransactionRepository.objectTransactionSync(tenantId, Collections.singletonList(trxVo));
    }

    /**
     * 获取返回属性对应类型
     *
     * @param tenantId
     * @param backType
     * @return java.lang.String
     * @author jiangling.zheng@hand-china.com 2020/9/15 15:12
     */
    private String getLovTag(Long tenantId, String backType) {
        List<LovValueDTO> lovValueDTOList = lovAdapter.queryLovValue(HmeConstants.LovCode.HME_BACK_TYPE, tenantId);
        if (CollectionUtils.isEmpty(lovValueDTOList)) {
            throw new MtException("HME_SPLIT_RECORD_0007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0007", "HME", HmeConstants.LovCode.HME_BACK_TYPE));
        }
        // 清除不为BackType的记录
        lovValueDTOList.removeIf(item -> !StringUtils.equals(backType, item.getValue()));
        // 关联不到或为空
        if (CollectionUtils.isEmpty(lovValueDTOList) || Strings.isBlank(lovValueDTOList.get(0).getTag())) {
            throw new MtException("HME_SPLIT_RECORD_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_SPLIT_RECORD_0008", "HME", backType));
        }
        return lovValueDTOList.get(0).getTag();
    }

    @Override
    public MtMaterialLot materialLotCreate(Long tenantId, HmeServiceSplitRecordDTO3 dto) {
        // 创建事件
        MtEventCreateVO eventCreateVO = new MtEventCreateVO();
        eventCreateVO.setEventTypeCode(dto.getEventCode());
        String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);
        // 调用api【materialLotUpdate】进行物料批注册
        Date nowDate = new Date();
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        mtMaterialLotVO2.setMaterialLotId(dto.getMaterialLotId());
        mtMaterialLotVO2.setEventId(eventId);
        mtMaterialLotVO2.setSiteId(dto.getSiteId());
        mtMaterialLotVO2.setMaterialLotCode(dto.getMaterialLotCode());
        mtMaterialLotVO2.setEnableFlag(YES);
        mtMaterialLotVO2.setQualityStatus(HmeConstants.ConstantValue.OK);
        mtMaterialLotVO2.setMaterialId(dto.getMaterialId());
        mtMaterialLotVO2.setPrimaryUomId(dto.getUomId());
        mtMaterialLotVO2.setPrimaryUomQty(1D);
        mtMaterialLotVO2.setLocatorId(dto.getIssuedLocatorId());
        mtMaterialLotVO2.setLoadTime(nowDate);
        mtMaterialLotVO2.setCreateReason(dto.getCreateReason());
        mtMaterialLotVO2.setInSiteTime(nowDate);
        mtMaterialLotVO2.setLot(dto.getLotCode());
        MtMaterialLotVO13 vo = mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
        // 扩展字段创建
        List<MtExtendVO5> mtExtends = new ArrayList<>();
        mtExtends.add(new MtExtendVO5() {{
            setAttrName("STATUS");
            setAttrValue(WmsConstant.InstructionStatus.NEW);
        }});
        mtExtends.add(new MtExtendVO5() {{
            setAttrName("AF_FLAG");
            setAttrValue(YES);
        }});
        mtExtends.add(new MtExtendVO5() {{
            setAttrName("REWORK_FLAG");
            setAttrValue(YES);
        }});
        mtExtends.add(new MtExtendVO5() {{
            setAttrName("MF_FLAG");
            setAttrValue("");
        }});
        //客户机拆机出模块不计入SAP账务，其账务标识为 N  Z1：客户保内返回  Z2：客户保外返回
        if ("Z1".equals(dto.getBackType()) || "Z2".equals(dto.getBackType())) {
            mtExtends.add(new MtExtendVO5() {{
                setAttrName("SAP_ACCOUNT_FLAG");
                setAttrValue("N");
            }});
        }
        mtMaterialLotRepository.materialLotAttrPropertyUpdate(tenantId, new MtExtendVO10() {{
            setKeyId(vo.getMaterialLotId());
            setEventId(eventId);
            setAttrs(mtExtends);
        }});
        return mtMaterialLotRepository.selectByPrimaryKey(vo.getMaterialLotId());
    }

    @Override
    public List<HmeServiceSplitReturnCheckVO> returnCheckListGet(Long tenantId, String snNum, String allFlag) {
        return hmeServiceSplitRecordMapper.selectReturnCheckList(tenantId, snNum, allFlag);
    }
}
