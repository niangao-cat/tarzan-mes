package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.entity.HmeQualification;
import com.ruike.hme.domain.repository.HmeSnBindEoRepository;
import com.ruike.hme.domain.repository.HmeWorkCellDetailsReportRepository;
import com.ruike.hme.domain.vo.HmeEoVO3;
import com.ruike.hme.domain.vo.HmeSnBindEoVO;
import com.ruike.hme.domain.vo.HmeSnBindEoVO2;
import com.ruike.hme.domain.vo.HmeSnBindEoVO3;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeProLineDetailsMapper;
import com.ruike.hme.infra.mapper.HmeSnBindEoMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO19;
import tarzan.inventory.domain.vo.MtMaterialLotVO20;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.method.domain.entity.*;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于工单生成SN绑定EO
 *
 * @author sanfeng.zhang@hand-china.com 2020/07/06 20:36
 */
@Component
@Slf4j
public class HmeSnBindEoRepositoryImpl implements HmeSnBindEoRepository {


    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeSnBindEoMapper hmeSnBindEoMapper;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private HmeWorkCellDetailsReportRepository hmeWorkCellDetailsReportRepository;

    @Autowired
    private HmeProLineDetailsMapper hmeProLineDetailsMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSnBindEo(List<MtEo> mtEoList, MtWorkOrder mtWorkOrder) {
        if(CollectionUtils.isNotEmpty(mtEoList)){
            //批量生成物料批编码
            long startDate = System.currentTimeMillis();
            List<HmeSnBindEoVO> materialLotCodeList = this.batchCreateMaterialLotCode(mtEoList);
            log.info("=================================>工单管理平台-工单下达-handleEoRelated-createSnBindEo-批量生成物料批编码总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            Long tenantId = mtEoList.get(0).getTenantId();

            if (mtWorkOrder == null) {
                throw new MtException("MT_ORDER_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0006", "ORDER", "【API:woLimitEoCreate】"));
            }

            //生成事件
            MtEventCreateVO eventCreateVO = new MtEventCreateVO();
            String eventTypeCode = "EO_MATERIAL_LOT_CREAT";
            eventCreateVO.setEventTypeCode(eventTypeCode);
            eventCreateVO.setLocatorId(mtWorkOrder.getLocatorId());
            String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO);

            List<MtEo> eoList = new ArrayList<>();

            List<MtMaterialLotVO20> materialLotList = new ArrayList<>();
            //工单上的货位
            String locatorId = "";
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(mtWorkOrder.getLocatorId());
            if(mtModLocator == null){
                throw new MtException("MT_ORDER_0172", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ORDER_0172", "ORDER"));
            }
            locatorId = mtModLocator.getLocatorId();
            BeanCopier copier = BeanCopier.create(HmeSnBindEoVO.class, MtEo.class, false);
            for (HmeSnBindEoVO code : materialLotCodeList) {
                //创建物料批
                MtMaterialLotVO20 mtMaterialLotVO20 = new MtMaterialLotVO20();
                mtMaterialLotVO20.setMaterialLotCode(code.getMaterialLotCode());
                mtMaterialLotVO20.setSiteId(mtEoList.get(0).getSiteId());
                mtMaterialLotVO20.setEnableFlag(HmeConstants.ConstantValue.YES);
                mtMaterialLotVO20.setQualityStatus(HmeConstants.ConstantValue.OK);
                mtMaterialLotVO20.setMaterialId(mtWorkOrder.getMaterialId());
                mtMaterialLotVO20.setPrimaryUomId(mtWorkOrder.getUomId());
                mtMaterialLotVO20.setPrimaryUomQty(1.0d);
                mtMaterialLotVO20.setLocatorId(locatorId);
                mtMaterialLotVO20.setLoadTime(new Date());
                mtMaterialLotVO20.setCreateReason("INITIALIZE");
                mtMaterialLotVO20.setInSiteTime(new Date());
                mtMaterialLotVO20.setEoId(code.getEoId());
                mtMaterialLotVO20.setLot("");
                materialLotList.add(mtMaterialLotVO20);

                MtEo mtEo = new MtEo();
                copier.copy(code, mtEo, null);
                // BeanUtils.copyProperties(code, mtEo);
                mtEo.setIdentification(code.getMaterialLotCode());
                eoList.add(mtEo);
            }

            //批量创建物料批
            startDate = System.currentTimeMillis();
            List<MtMaterialLotVO19> mtMaterialLotVO19List = mtMaterialLotRepository.materialLotBatchUpdate(tenantId, materialLotList, eventId, HmeConstants.ConstantValue.NO);
            log.info("=================================>工单管理平台-工单下达-handleEoRelated-createSnBindEo-批量创建物料批总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            List<MtCommonExtendVO6> attrPropertyList = new ArrayList<>();
            List<MtCommonExtendVO7> attrPropertyList2 = new ArrayList<>();
            // 20210812 add by sanfeng.zhang for tianyang.xie 返修工单 创建的条码 返修标识为Y
            // 工单生产版本在值集内的为返修工单
            List<LovValueDTO> lovValue = lovAdapter.queryLovValue("HME.AFWO_PRODUCTION_VERSION", tenantId);
            List<String> productionVersionList = lovValue.stream().map(LovValueDTO::getMeaning).distinct().collect(Collectors.toList());
            Optional<String> firstOpt = productionVersionList.stream().filter(dto -> StringUtils.equals(dto, mtWorkOrder.getProductionVersion())).findFirst();
            for (MtMaterialLotVO19 mtMaterialLotVO19 : mtMaterialLotVO19List) {
                //物料批扩展属性
//                MtCommonExtendVO6 mtCommonExtendVO6 = new MtCommonExtendVO6();
//                mtCommonExtendVO6.setKeyId(mtMaterialLotVO19.getMaterialLotId());
//
//                List<MtCommonExtendVO5> mtCommonExtendVO5s = new ArrayList<>();
//                MtCommonExtendVO5 mtCommonExtendVO5 = new MtCommonExtendVO5();
//                mtCommonExtendVO5.setAttrName("MF_FLAG");
//                mtCommonExtendVO5.setAttrValue(HmeConstants.ConstantValue.YES);
//                mtCommonExtendVO5s.add(mtCommonExtendVO5);
//
//                MtCommonExtendVO5 statusVO = new MtCommonExtendVO5();
//                statusVO.setAttrName("STATUS");
//                statusVO.setAttrValue(HmeConstants.StatusCode.NEW);
//                mtCommonExtendVO5s.add(statusVO);
//
//                mtCommonExtendVO6.setAttrs(mtCommonExtendVO5s);
//
//                attrPropertyList.add(mtCommonExtendVO6);

                //V20210421 modify by penglin.sui for 产品 组装attrPropertyBatchUpdateNew-API参数
                List<MtCommonExtendVO4> mtCommonExtendVO4s = new ArrayList<>();
                MtCommonExtendVO7 mtCommonExtendVO7 = new MtCommonExtendVO7();
                mtCommonExtendVO7.setKeyId(mtMaterialLotVO19.getMaterialLotId());

                MtCommonExtendVO4 mtCommonExtendVO4 = new MtCommonExtendVO4();
                mtCommonExtendVO4.setAttrName("MF_FLAG");
                mtCommonExtendVO4.setAttrValue(HmeConstants.ConstantValue.YES);
                mtCommonExtendVO4s.add(mtCommonExtendVO4);

                MtCommonExtendVO4 statusVO4 = new MtCommonExtendVO4();
                statusVO4.setAttrName("STATUS");
                statusVO4.setAttrValue(HmeConstants.StatusCode.NEW);
                mtCommonExtendVO4s.add(statusVO4);

                if (firstOpt.isPresent()) {
                    MtCommonExtendVO4 reworkFlag = new MtCommonExtendVO4();
                    reworkFlag.setAttrName("REWORK_FLAG");
                    reworkFlag.setAttrValue(HmeConstants.ConstantValue.YES);
                    mtCommonExtendVO4s.add(reworkFlag);
                }
                mtCommonExtendVO7.setAttrs(mtCommonExtendVO4s);
                attrPropertyList2.add(mtCommonExtendVO7);
            }
            //批量更新扩展字段
            startDate = System.currentTimeMillis();
//            mtExtendSettingsRepository.attrPropertyBatchUpdate(tenantId, "mt_material_lot_attr", eventId, attrPropertyList);
            mtExtendSettingsRepository.attrPropertyBatchUpdateNew(tenantId, "mt_material_lot_attr", eventId, attrPropertyList2);
            log.info("=================================>工单管理平台-工单下达-handleEoRelated-createSnBindEo-批量更新扩展字段总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            //批量返写对应的eo
            startDate = System.currentTimeMillis();
            this.batchUpdateMtEo(tenantId, eoList);
            log.info("=================================>工单管理平台-工单下达-handleEoRelated-createSnBindEo-批量返写对应的eo总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }
    }

    private void batchUpdateMtEo(Long tenantId, List<MtEo> eoList){
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = userDetails != null ? userDetails.getUserId() : -1L;
        hmeProLineDetailsMapper.batchUpdateMtEo(tenantId,userId, eoList);
    }

    public List<HmeSnBindEoVO> batchCreateMaterialLotCode(List<MtEo> mtEoList){
        if(CollectionUtils.isEmpty(mtEoList)){
            return Collections.EMPTY_LIST;
        }
        List<HmeSnBindEoVO> hmeSnBindEoVOList = new ArrayList<>();

        List<MtNumrangeVO11> mtNumrangeVO11List = new ArrayList<>();

        mtNumrangeVO11List = this.createRuleCode(mtEoList);

        MtNumrangeVO9 dto = new MtNumrangeVO9();
        // 调用API{ numrangeBatchGenerate}
        dto.setObjectCode("SN_NUM");

        dto.setIncomingValueList(mtNumrangeVO11List);
        dto.setObjectNumFlag(HmeConstants.ConstantValue.YES);
        dto.setNumQty(Long.valueOf(mtEoList.size()));
        long startDate = System.currentTimeMillis();
        MtNumrangeVO8 mtNumrangeVO8 = mtNumrangeRepository.numrangeBatchGenerate(mtEoList.get(0).getTenantId(), dto);
        log.info("=================================>工单管理平台-工单下达-handleEoRelated-createSnBindEo-batchCreateMaterialLotCode-numrangeBatchGenerate总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        List<String> numberList = mtNumrangeVO8.getNumberList();

        //将号码段放入对应的实体
        Integer numIndex = 0;
        BeanCopier copier = BeanCopier.create(MtEo.class, HmeSnBindEoVO.class, false);
        for (MtEo mtEo : mtEoList) {
            HmeSnBindEoVO eoVO = new HmeSnBindEoVO();
            copier.copy(mtEo, eoVO, null);
            // BeanUtils.copyProperties(mtEo, eoVO);
            eoVO.setMaterialLotCode(numberList.get(numIndex));
            hmeSnBindEoVOList.add(eoVO);
            numIndex++;
        }

        return hmeSnBindEoVOList;
    }


    /**
     * 条码 规则：工厂（1位）-产线（1位）-产品类型（4位）-年（2位）-月（1位）-流水号（5位）
     * 生成物料批编码
     *
     * @param mtEoList
     * @return
     * @date 2020/07/06
     * @author sanfeng.zhang
     */
    public List<MtNumrangeVO11> createRuleCode(List<MtEo> mtEoList) {
        List<MtNumrangeVO11> mtNumrangeVO11List = new ArrayList<>();

        Long tenantId = mtEoList.get(0).getTenantId();
        String workOrderId = mtEoList.get(0).getWorkOrderId();

        List<String> siteIdList  = mtEoList.stream().map(MtEo::getSiteId).distinct().collect(Collectors.toList());

        List<String> proLineList  = mtEoList.stream().map(MtEo::getProductionLineId).distinct().collect(Collectors.toList());

        //获取工厂简码
        Map<String, String> siteMap = new HashMap<>();
        List<HmeSnBindEoVO2> siteCodeList = this.modSiteAttrValueGet(tenantId, siteIdList);
        if (CollectionUtils.isNotEmpty(siteCodeList)) {
            siteCodeList.forEach(item -> siteMap.put(item.getSiteId(), item.getSiteCode()));
        }


        //获取生产线简码
        Map<String, String> proLineMap = new HashMap<>();
        List<HmeSnBindEoVO2> proLineInfoList = this.productionLineAttrValueGet(tenantId, proLineList, workOrderId);
        if (CollectionUtils.isNotEmpty(proLineInfoList)) {
            proLineInfoList.forEach(item -> proLineMap.put(item.getProLineId(), item.getProLineCode()));
        }

        //获取产品类型(迭代修改)
        List<MtExtendSettings> attrList = new ArrayList<>();
        MtExtendSettings mtExtendSettings3 = new MtExtendSettings();
        mtExtendSettings3.setAttrName("attribute5");
        attrList.add(mtExtendSettings3);
        MtExtendSettings mtExtendSettings3_2 = new MtExtendSettings();
        mtExtendSettings3_2.setAttrName("attribute15");
        attrList.add(mtExtendSettings3_2);
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingMapper.attrPropertyQuery(tenantId,
                "mt_work_order_attr", "WORK_ORDER_ID", workOrderId, attrList);
        Map<String , String> workOrderAttrMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(mtExtendAttrVOList)){
            workOrderAttrMap = mtExtendAttrVOList.stream().collect(Collectors.toMap(MtExtendAttrVO::getAttrName , MtExtendAttrVO::getAttrValue));
        }
        List<LovValueDTO> productionList = lovAdapter.queryLovValue("HME.NOT_CHECK_PRODUCTION_MANAGER", tenantId);
        MtWorkOrder mtWorkOrder = mtWorkOrderRepository.selectByPrimaryKey(workOrderId);

        Map<String, String> typeMap = new HashMap<>();
        List<HmeSnBindEoVO2> proItemTypeList = this.proItemTypeGet(tenantId, siteIdList, workOrderId);
        if (CollectionUtils.isNotEmpty(proItemTypeList)) {
            proItemTypeList.forEach(item -> typeMap.put(item.getSiteId(), item.getProItemType()));
        }

        //当前年 后两位
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2);
        //月：1-9，A-Z 获取的月比真实少一个月
        String month = this.handleMonth(calendar.get(Calendar.MONTH) + 1);

        Long index = 0L;
        for (MtEo mtEo : mtEoList) {
            StringBuffer codeStr = new StringBuffer();
            //站点
            if (StringUtils.isBlank(mtEo.getSiteId())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(mtEo.getTenantId(),
                        "MT_ORDER_0001", "ORDER", "siteId", "【API:createMaterialLotCode】"));
            }
            String siteAttrValue = "";
            if(siteMap.containsKey(mtEo.getSiteId())){
                siteAttrValue = siteMap.get(mtEo.getSiteId());
            }
            if (StringUtils.isBlank(siteAttrValue)) {
                throw new MtException("HME_SN_BIND_EO_001", mtErrorMessageRepository.getErrorMessageWithModule(mtEo.getTenantId(),
                        "HME_SN_BIND_EO_001", "HME", ""));
            }

            //产线
            if (StringUtils.isBlank(mtEo.getProductionLineId())) {
                throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(mtEo.getTenantId(),
                        "MT_ORDER_0001", "ORDER", "productionLineId", "【API:createMaterialLotCode】"));
            }

            String proLineAttrValue = "";
            if(proLineMap.containsKey(mtEo.getProductionLineId())){
                proLineAttrValue = proLineMap.get(mtEo.getProductionLineId());
            }

            if (StringUtils.isBlank(proLineAttrValue)) {
                throw new MtException("HME_SN_BIND_EO_002", mtErrorMessageRepository.getErrorMessageWithModule(mtEo.getTenantId(),
                        "HME_SN_BIND_EO_002", "HME", ""));
            }

            //类型
            String proItemType = "";
            String itemType = hmeSnBindEoMapper.queryProductionManager(tenantId, mtEo.getSiteId(), mtWorkOrder.getMaterialId());
            String attribute5Value = workOrderAttrMap.getOrDefault("attribute5" , "");
            if(CollectionUtils.isEmpty(mtExtendAttrVOList) || StringUtils.isBlank(attribute5Value)){
                proItemType = StringUtils.isBlank(itemType) ? "SNDD" : itemType;
            }else {
                List<LovValueDTO> collect = productionList.stream().filter(dto -> StringUtils.equals(dto.getValue(), attribute5Value)).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(collect)){
                    //V20210329 add by penglin.sui for fang.pan 新增校验
                    String attribute15Value = workOrderAttrMap.getOrDefault("attribute15" , "");
                    if(StringUtils.isBlank(attribute15Value)) {
                        proItemType = StringUtils.isBlank(itemType) ? "SNDD" : itemType;
                    }else{
                        if(attribute15Value.length() != 4){
                            //工单【${1}】的BOM可选文本必须为四位
                            throw new MtException("HME_SN_BIND_EO_004", mtErrorMessageRepository.getErrorMessageWithModule(mtEo.getTenantId(),
                                    "HME_SN_BIND_EO_004", "HME",mtWorkOrder.getWorkOrderNum()));
                        }
                        for(int i = 0; i< attribute15Value.length(); i++){
                            char c = attribute15Value.charAt(i);
                            if(!Character.isDigit(c) && !Character.isUpperCase(c)){
                                //维护的BOM可选文本只能为大写字母或数字,当前为【${1}】;
                                throw new MtException("HME_SN_BIND_EO_005", mtErrorMessageRepository.getErrorMessageWithModule(mtEo.getTenantId(),
                                        "HME_SN_BIND_EO_005", "HME",attribute15Value));
                            }
                        }
                        proItemType = attribute15Value;
                    }
                }else {
                    if(StringUtils.isBlank(itemType)){
                        throw new MtException("HME_SN_BIND_EO_003", mtErrorMessageRepository.getErrorMessageWithModule(mtEo.getTenantId(),
                                "HME_SN_BIND_EO_003", "HME"));
                    }
                    proItemType = itemType;
                }

            }

            codeStr.append(siteAttrValue)
                    .append(proLineAttrValue)
                    .append(proItemType)
                    .append(year)
                    .append(month);
            String ruleCode = codeStr.toString();

            MtNumrangeVO11 vo11 = new MtNumrangeVO11();
            vo11.setSequence(index);
            List<String> valList = new ArrayList<>();
            valList.add(ruleCode);
            vo11.setIncomingValue(valList);
            mtNumrangeVO11List.add(vo11);

            index++;
        }
        return mtNumrangeVO11List;
    }



    @Override
    public List<HmeSnBindEoVO2> productionLineAttrValueGet(Long tenantId, List<String> productionLineIdList, String workOrderId) {
        //参数校验
        if (CollectionUtils.isEmpty(productionLineIdList)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "productionLineId", "【API:productionLineAttrValueGet】"));
        }

        if (StringUtils.isBlank(workOrderId)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "workOrderId", "【API:productionLineAttrValueGet】"));
        }
        return hmeSnBindEoMapper.productionLineAttrValueGet(tenantId, productionLineIdList, workOrderId);
    }

    @Override
    public List<HmeSnBindEoVO2> modSiteAttrValueGet(Long tenantId, List<String> siteIdList) {
        //参数检验
        if (CollectionUtils.isEmpty(siteIdList)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "siteId", "【API:modSiteAttrValueGet】"));
        }
        return hmeSnBindEoMapper.modSiteAttrValueGet(tenantId, siteIdList);
    }

    @Override
    public List<HmeSnBindEoVO2> proItemTypeGet(Long tenantId, List<String> siteIdList, String workOrderId) {
        if (CollectionUtils.isEmpty(siteIdList)) {
            throw new MtException("MT_ORDER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ORDER_0001", "ORDER", "siteId", "【API:proItemTypeGet】"));
        }
        return hmeSnBindEoMapper.proItemTypeGet(tenantId, siteIdList, workOrderId);
    }

    @Override
    public String eoWorkcellIdDescQuery(Long tenantId, String eoId) {
        return hmeSnBindEoMapper.eoWorkcellIdDescQuery(tenantId, eoId);
    }

    @Override
    public String eoWorkcellIdDescQuery2(Long tenantId, String eoId) {
        return hmeSnBindEoMapper.eoWorkcellIdDescQuery2(tenantId, eoId);
    }

    @Override
    public List<MtBom> queryBomListByEoIds(Long tenantId, List<String> eoIdList) {
        if(CollectionUtils.isEmpty(eoIdList)){
            return Collections.emptyList();
        }
        return hmeSnBindEoMapper.queryBomListByEoIds(tenantId, eoIdList);
    }

    @Override
    public List<MtOperation> queryOperationIdByName(Long tenantId, String operationName) {
        return hmeSnBindEoMapper.queryOperationIdByName(tenantId,operationName);
    }

    @Override
    public List<HmeQualification> queryQualificationByName(Long tenantId, String qualityName) {
        return hmeSnBindEoMapper.queryQualificationByName(tenantId,qualityName);
    }

    @Override
    public List<MtDispositionGroup> queryDispositionGroupIdByDesc(Long tenantId, String dispositionGroup) {
        return hmeSnBindEoMapper.queryDispositionGroupIdByDesc(tenantId,dispositionGroup);
    }

    @Override
    public List<MtNcCode> queryNcCodeInfo(Long tenantId, String ncCodeDesc) {
        return hmeSnBindEoMapper.queryNcCodeInfo(tenantId,ncCodeDesc);
    }

    @Override
    public List<MtNcGroup> queryNcGroupInfo(Long tenantId, String ncGroupDesc) {
        return hmeSnBindEoMapper.queryNcGroupInfo(tenantId,ncGroupDesc);
    }

    @Override
    public MtUom queryMtUomByUomCode(Long tenantId, String uomCode) {
        return hmeSnBindEoMapper.queryMtUomByUomCode(tenantId,uomCode);
    }

    @Override
    public MtMaterial queryOneMaterialByCode(Long tenantId, String materialCode) {
        List<String> codeList = new ArrayList<>();
        codeList.add(materialCode);
        List<MtMaterial> mtMaterialList = mtMaterialRepository.queryMaterialByCode(tenantId, codeList);
        if(CollectionUtils.isNotEmpty(mtMaterialList)){
            return mtMaterialList.get(0);
        }
        return null;
    }

    @Override
    public MtRouter queryOneRouterByName(Long tenantId, String routerName) {
        return hmeSnBindEoMapper.queryOneRouterByName(tenantId,routerName);
    }

    @Override
    public MtRouterStep queryOneRouterStepByName(Long tenantId, String stepName) {
        return null;
    }

    @Override
    public Integer queryOperationComponentCount(Long tenantId, String workOrderId) {
        return hmeSnBindEoMapper.queryOperationComponentCount(tenantId, workOrderId);
    }

    @Override
    public Integer queryBomComponentCount(Long tenantId, String workOrderId) {
        return hmeSnBindEoMapper.queryBomComponentCount(tenantId, workOrderId);
    }


    /**
     * 处理月 1-9，A-Z
     *
     * @param month
     * @return
     * @date 2020/7/6
     * @author sanfeng.zhang
     */
    @Override
    public String handleMonth(int month) {
        String monthStr = "";
        int monthMax = 10;
        if (month < monthMax) {
            return String.valueOf(month);
        }
        switch (month) {
            case 10:
                monthStr = "A";
                break;
            case 11:
                monthStr = "B";
                break;
            case 12:
                monthStr = "C";
                break;
            default:
                break;
        }
        return monthStr;
    }

    @Override
    public List<HmeEoVO3> batchQuerySoNum(Long tenantId, List<String> workOrderIdList) {
        return hmeSnBindEoMapper.batchQuerySoNum(tenantId, workOrderIdList);
    }

    @Override
    public List<String> queryEoIdByProcessId(Long tenantId, String processId, String siteId) {
        return hmeSnBindEoMapper.queryEoIdByProcessId(tenantId, processId, siteId);
    }

    @Override
    public List<HmeSnBindEoVO3> queryBomComponentCountByWorkOrderIds(Long tenantId, List<String> workOrderIdList) {
        return hmeSnBindEoMapper.queryBomComponentCountByWorkOrderIds(tenantId, workOrderIdList);
    }
}
