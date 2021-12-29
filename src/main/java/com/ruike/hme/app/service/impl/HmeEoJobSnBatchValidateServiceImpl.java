package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO2;
import com.ruike.hme.api.dto.HmeEoJobSnBatchDTO4;
import com.ruike.hme.app.service.HmeEoJobSnBatchValidateService;
import com.ruike.hme.app.service.HmeEoJobSnCommonService;
import com.ruike.hme.domain.entity.*;
import com.ruike.hme.domain.repository.HmeEoJobSnLotMaterialRepository;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.*;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.actual.domain.entity.MtWorkOrderComponentActual;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO28;
import tarzan.inventory.domain.vo.MtMaterialLotVO29;
import tarzan.inventory.domain.vo.MtMaterialLotVO30;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.repository.MtRouterStepRepository;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.vo.MtEoAttrVO2;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HmeEoJobSnBatchValidateServiceImpl implements HmeEoJobSnBatchValidateService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private HmeEoJobSnCommonService hmeEoJobSnCommonService;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;
    @Autowired
    private HmeEoJobMaterialMapper hmeEoJobMaterialMapper;
    @Autowired
    private HmeEoJobLotMaterialMapper hmeEoJobLotMaterialMapper;
    @Autowired
    private HmeEoJobTimeMaterialMapper hmeEoJobTimeMaterialMapper;
    @Autowired
    private HmeEoJobSnBatchMapper hmeEoJobSnBatchMapper;
    @Autowired
    private MtRouterStepRepository mtRouterStepRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private HmeMaterialLotLabCodeMapper hmeMaterialLotLabCodeMapper;
    @Autowired
    private HmeSnLabCodeMapper hmeSnLabCodeMapper;
    @Autowired
    private MtExtendSettingsRepository extendSettingsRepository;
    @Autowired
    private MtEoRepository mtEoRepository;

    private static String symbol = "#";

    private static String fetchGroupKey(String str1, String str2) {
        return str1 + symbol + str2;
    }

    /**
     * 批量工序作业平台-根据扫描的数据获取所有需要进站的SN
     *
     * @author penglin.sui@hand-china.com 2020-11-12 16:38:10
     */
    private HmeEoJobSnBatchVO getInSiteSn(Long tenantId, HmeEoJobSnVO3 dto){
        HmeEoJobSnBatchVO hmeEoJobSnBatchVO = new HmeEoJobSnBatchVO();
        List<HmeEoJobSnVO3> snLineList = new ArrayList<>(16);
        //判断当前扫描的为容器还是物料批
        MtMaterialLotVO30 materialLotVo30 = new MtMaterialLotVO30();
        materialLotVo30.setCode(dto.getSnNum());
        materialLotVo30.setAllLevelFlag(HmeConstants.ConstantValue.YES);
        MtMaterialLotVO29 isContainerVO = mtMaterialLotRepository.codeOrIdentificationLimitObjectGet(tenantId, materialLotVo30);
        hmeEoJobSnBatchVO.setCodeType(isContainerVO.getCodeType());
        Map<String,HmeEoBomVO> eoBomMap = new HashMap<>();
        if (HmeConstants.LoadTypeCode.CONTAINER.equals(isContainerVO.getCodeType())) {
            //当前扫描的为容器
            log.info("<================HmeEoJobSnBatchServiceImpl.getInSiteSn-当前扫描的为容器================>");
            hmeEoJobSnBatchVO.setIsContainer(true);
            //V20210708 modify by penglin.sui for peng.zhao 添加容器为空校验
            if(CollectionUtils.isEmpty(isContainerVO.getLoadingObjectlList())){
                //该容器【${1}】下无条码,请检查!
                throw new MtException("HME_EO_JOB_SN_206", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_206", "HME", dto.getSnNum()));
            }
            List<MtMaterialLotVO28> materialLotVOList = isContainerVO.getLoadingObjectlList();
            List<String> eoMaterialLotIds = materialLotVOList.stream().map(MtMaterialLotVO28::getLoadObjectId).distinct()
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(eoMaterialLotIds)) {
                List<MtMaterialLot> materialLotList = mtMaterialLotRepository.materialLotPropertyBatchGet(tenantId, eoMaterialLotIds);

                HmeEoJobSnBatchVO2 hmeEoJobSnBatchVO2 = new HmeEoJobSnBatchVO2();
                hmeEoJobSnBatchVO2.setEnableFlag(HmeConstants.ConstantValue.YES);
                hmeEoJobSnBatchVO2.setSiteId(dto.getSiteId());
                List<String> materialLotCodeList = new ArrayList<>();

                for (MtMaterialLot mtMaterialLot : materialLotList) {

                    //V20210703 modify by penglin.sui for peng.zhao 出站校验有盘点标识的SN不可进站
                    if(StringUtils.isNotBlank(mtMaterialLot.getStocktakeFlag()) &&
                            HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())){
                        //此SN【${1}】正在盘点,不可进站
                        throw new MtException("HME_EO_JOB_SN_205", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_205", "HME", mtMaterialLot.getMaterialLotCode()));
                    }

                    // 20210721 modify by sanfeng.zhang for xenxin.zhang 出站校验冻结标识为Y的SN不可进站
                    if (HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag())) {
                        //【此SN【${1}】正在冻结,不可进站】
                        throw new MtException("HME_EO_JOB_SN_208", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_208", "HME", mtMaterialLot.getMaterialLotCode()));
                    }

                    HmeEoJobSnVO3 snLine = new HmeEoJobSnVO3();
                    BeanUtils.copyProperties(dto, snLine);
                    // 覆盖dto上的扫描条码为容器内的条码
                    snLine.setSnNum(mtMaterialLot.getMaterialLotCode());
                    snLine.setEoId(mtMaterialLot.getEoId());
                    snLine.setMaterialLotId(mtMaterialLot.getMaterialLotId());
                    snLine.setCodeType(isContainerVO.getCodeType());
                    snLineList.add(snLine);

                    materialLotCodeList.add(mtMaterialLot.getMaterialLotCode());
                }

                // 20210908 add by sanfeng.zhang for hui.gu 判断SN在该工序是否已拦截
                hmeEoJobSnCommonService.interceptValidate(tenantId, dto.getWorkcellId(), materialLotList);

                hmeEoJobSnBatchVO2.setMaterialLotCodeList(materialLotCodeList);
                List<HmeEoJobSnVO5> snVOList = hmeEoJobSnMapper.queryMaterialByLot(tenantId, hmeEoJobSnBatchVO2);
                if(CollectionUtils.isEmpty(snVOList) || (CollectionUtils.isNotEmpty(snVOList) && snVOList.size() != eoMaterialLotIds.size())){
                    //所扫描条码不存在或不为有效状态,请核实!
                    throw new MtException("HME_NC_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_NC_0009", "HME"));
                }
                List<String> eoIdList = snVOList.stream().map(HmeEoJobSnVO5::getEoId).distinct().collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(eoIdList)){
                    List<HmeEoBomVO> hmeEoBomVOList = hmeEoJobSnMapper.selectEoBom(tenantId,eoIdList);
                    if(CollectionUtils.isNotEmpty(hmeEoBomVOList)){
                        eoBomMap = hmeEoBomVOList.stream().collect(Collectors.toMap(HmeEoBomVO::getEoId, t -> t));;
                    }
                }
                Map<String, HmeEoBomVO> finalEoBomMap = eoBomMap;
                snVOList.forEach(item -> {
                    HmeEoBomVO hmeEoBomVO = finalEoBomMap.getOrDefault(item.getEoId(),null);
                    if(Objects.nonNull(hmeEoBomVO)) {
                        item.setBomId(hmeEoBomVO.getBomId());
                        item.setBomName(hmeEoBomVO.getBomName());
                    }
                });

                hmeEoJobSnBatchVO.setSnLineList(snLineList);
                hmeEoJobSnBatchVO.setSnVOList(snVOList);
            }
        } else {
            //当前扫描的为物料批
            log.info("<================HmeEoJobSnBatchServiceImpl.getInSiteSn-当前扫描的为物料批================>");
            hmeEoJobSnBatchVO.setIsContainer(false);

            HmeEoJobSnBatchVO2 hmeEoJobSnBatchVO2 = new HmeEoJobSnBatchVO2();
            hmeEoJobSnBatchVO2.setEnableFlag(HmeConstants.ConstantValue.YES);
            hmeEoJobSnBatchVO2.setSiteId(dto.getSiteId());
            List<String> materialLotCodeList = new ArrayList<>();
            materialLotCodeList.add(dto.getSnNum());
            hmeEoJobSnBatchVO2.setMaterialLotCodeList(materialLotCodeList);
            List<HmeEoJobSnVO5> snVOList = hmeEoJobSnMapper.queryMaterialByLot(tenantId, hmeEoJobSnBatchVO2);
            if(CollectionUtils.isEmpty(snVOList) || (CollectionUtils.isNotEmpty(snVOList) && snVOList.size() != HmeConstants.ConstantValue.ONE)){
                //所扫描条码不存在或不为有效状态,请核实!
                throw new MtException("HME_NC_0009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_NC_0009", "HME"));
            }

            dto.setEoId(snVOList.get(0).getEoId());
            List<String> eoIdList = snVOList.stream().map(HmeEoJobSnVO5::getEoId).distinct().collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(eoIdList)){
                List<HmeEoBomVO> hmeEoBomVOList = hmeEoJobSnMapper.selectEoBom(tenantId,eoIdList);
                if(CollectionUtils.isNotEmpty(hmeEoBomVOList)){
                    eoBomMap = hmeEoBomVOList.stream().collect(Collectors.toMap(HmeEoBomVO::getEoId, t -> t));;
                }
            }
            Map<String, HmeEoBomVO> finalEoBomMap = eoBomMap;
            snVOList.forEach(item -> {

                //V20210703 modify by penglin.sui for peng.zhao 出站校验有盘点标识的SN不可出站
                if(StringUtils.isNotBlank(item.getStocktakeFlag()) &&
                        HmeConstants.ConstantValue.YES.equals(item.getStocktakeFlag())){
                    //此SN【${1}】正在盘点,不可出站
                    throw new MtException("HME_EO_JOB_SN_204", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_204", "HME", item.getMaterialLotCode()));
                }
                // 20210721 modify by sanfeng.zhang for xenxin.zhang 出站校验冻结标识为Y的SN不可进站
                if (HmeConstants.ConstantValue.YES.equals(item.getFreezeFlag())) {
                    //【此SN【${1}】正在冻结,不可进站】
                    throw new MtException("HME_EO_JOB_SN_208", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_208", "HME", item.getMaterialLotCode()));
                }

                // 20210908 add by sanfeng.zhang for hui.gu 判断SN在该工序是否已拦截
                hmeEoJobSnCommonService.interceptValidate(tenantId, dto.getWorkcellId(), Collections.singletonList(new MtMaterialLot(){{
                    setMaterialLotCode(item.getMaterialLotCode());
                    setMaterialLotId(item.getMaterialLotId());
                    setLot(item.getLot());
                }}));

                HmeEoBomVO hmeEoBomVO = finalEoBomMap.getOrDefault(item.getEoId(),null);
                if(Objects.nonNull(hmeEoBomVO)) {
                    item.setBomId(hmeEoBomVO.getBomId());
                    item.setBomName(hmeEoBomVO.getBomName());
                }
            });
            dto.setMaterialLotId(snVOList.get(0).getMaterialLotId());
            HmeEoJobSnVO3 snLine = new HmeEoJobSnVO3();
            BeanUtils.copyProperties(dto, snLine);
            snLine.setCodeType(isContainerVO.getCodeType());
            snLineList.add(snLine);
            hmeEoJobSnBatchVO.setSnLineList(snLineList);
            hmeEoJobSnBatchVO.setSnVOList(snVOList);
        }

        return hmeEoJobSnBatchVO;
    }

    @Override
    public HmeEoJobSnBatchVO inSiteScanValidate(Long tenantId, HmeEoJobSnVO3 dto) {
        // 进站条码不能为空
        if (StringUtils.isBlank(dto.getSnNum())) {
            //扫描条码为空,请确认
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        long startDate = System.currentTimeMillis();

        //获取需进站的SN
        HmeEoJobSnBatchVO hmeEoJobSnBatchVO = this.getInSiteSn(tenantId,dto);
        log.info("=================================>批量工序作业平台-进站校验-获取需进站的SN总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //获取所有EOID
        List<String> eoIdList = hmeEoJobSnBatchVO.getSnLineList().stream().map(HmeEoJobSnVO3::getEoId).distinct().collect(Collectors.toList());
        //获取所有物料批ID
        List<String> materialLotIdList = hmeEoJobSnBatchVO.getSnLineList().stream().map(HmeEoJobSnVO3::getMaterialLotId).collect(Collectors.toList());
        //EOID和物料批ID关系
        Map<String,String> eoIdAndMaterialLotIdRelMap = hmeEoJobSnBatchVO.getSnLineList().stream().collect(Collectors.toMap(HmeEoJobSnVO3::getEoId,HmeEoJobSnVO3::getMaterialLotId));
        //物料批编码和物料批ID关系
        Map<String,String> materialLotMap = hmeEoJobSnBatchVO.getSnLineList().stream().collect(Collectors.toMap(HmeEoJobSnVO3::getMaterialLotId,HmeEoJobSnVO3::getSnNum));
        //获取条码扩展属性
        List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(materialLotIdList)) {
            startDate = System.currentTimeMillis();
            mtExtendAttrVO1List = extendSettingsRepository.attrPropertyBatchQuery(tenantId, new MtExtendVO1("mt_material_lot_attr", materialLotIdList, "REWORK_FLAG" , "MF_FLAG"));
            log.info("=================================>批量工序作业平台-进站校验-获取条码扩展属性总耗时：" + (System.currentTimeMillis() - startDate) + "毫秒");
        }
        if(CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
            List<MtExtendAttrVO1> reworkFlagList = mtExtendAttrVO1List.stream().filter(item -> "REWORK_FLAG".equals(item.getAttrName())).collect(Collectors.toList());
            //返修校验
            if(CollectionUtils.isNotEmpty(reworkFlagList)){
                reworkFlagList.forEach(reworkFlag ->{
                    if (HmeConstants.ConstantValue.YES.equals(reworkFlag.getAttrValue())) {
                        // 批量工序作业平台暂不支持返修进站,请使用单件工序作业平台进行返修
                        throw new MtException("HME_EO_JOB_SN_121",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_SN_121", "HME"));
                    }
                });
            }

            //V20211013 modify by penglin.sui for 增加SN必须是在制品的校验
            List<MtExtendAttrVO1> mfFlagList = mtExtendAttrVO1List.stream().filter(item -> "MF_FLAG".equals(item.getAttrName())).collect(Collectors.toList());
            if(mfFlagList.size() != (mtExtendAttrVO1List.size() - reworkFlagList.size())){
                //条码[${1}]不为在制品,不允许进站!
                throw new MtException("HME_EO_JOB_SN_253", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_253", "HME", ""));
            }
            mfFlagList.forEach(mfFlag ->{
                if (!HmeConstants.ConstantValue.YES.equals(mfFlag.getAttrValue())) {
                    //条码[${1}]不为在制品,不允许进站!
                    throw new MtException("HME_EO_JOB_SN_253", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_253", "HME", materialLotMap.getOrDefault(mfFlag.getKeyId() , "")));
                }
            });
        }

        //获取当前步骤
        startDate = System.currentTimeMillis();
        Map<String, List<HmeRouterStepVO3>> currentStepMap = hmeEoJobSnCommonService
                .batchQueryCurrentRouterStep(tenantId, eoIdList, dto.getOperationIdList());
        log.info("=================================>批量工序作业平台-进站校验-获取当前步骤总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        List<String> routerStepIdList = new ArrayList<>();
        List<String> operationIdList = new ArrayList<>();
        List<HmeRouterStepVO3> labCodeOrRemarkIsNullStepList = new ArrayList<>();
        for (List<HmeRouterStepVO3> item:currentStepMap.values()
        ) {
            //记录实验代码或者备注为空的数据
            for (HmeRouterStepVO3 hmeRouterStepVO3:item
            ) {
                routerStepIdList.add(hmeRouterStepVO3.getRouterStepId());
                operationIdList.add(hmeRouterStepVO3.getOperationId());

                if(StringUtils.isBlank(hmeRouterStepVO3.getLabCode()) || StringUtils.isBlank(hmeRouterStepVO3.getRouterStepRemark())){
                    labCodeOrRemarkIsNullStepList.add(hmeRouterStepVO3);
                }
            }
        }
        if(currentStepMap.size() == 0 || (currentStepMap.size() > 0 && currentStepMap.size() != eoIdList.size())){
            // 当前工艺无匹配的工艺步骤,请检查工序与工艺关系/工艺路线基础数据
            throw new MtException("HME_EO_JOB_SN_028", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_028", "HME"));
        }

        //V20210320 modify by penglin.sui for hui.ma 若当前步骤未找到实验代码，则查询SN工艺试验代码表
        if(CollectionUtils.isNotEmpty(labCodeOrRemarkIsNullStepList)){
            List<String> labCodeOrRemarkIsNullOperationIdList = labCodeOrRemarkIsNullStepList.stream().map(HmeRouterStepVO3::getOperationId)
                    .collect(Collectors.toList());
            startDate = System.currentTimeMillis();
            List<HmeSnLabCode> hmeSnLabCodeList = hmeSnLabCodeMapper.selectByCondition(Condition.builder(HmeSnLabCode.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeSnLabCode.FIELD_TENANT_ID, tenantId)
                            .andIn(HmeSnLabCode.FIELD_OPERATION_ID,labCodeOrRemarkIsNullOperationIdList)
                            .andIn(HmeSnLabCode.FIELD_MATERIAL_LOT_ID,materialLotIdList)
                            .andEqualTo(HmeSnLabCode.FIELD_ENABLED_FLAG,HmeConstants.ConstantValue.YES))
                    .build());
            log.info("=================================>批量工序作业平台-进站校验-查询SN工艺试验代码总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            if(CollectionUtils.isNotEmpty(hmeSnLabCodeList)){
                Map<String,HmeSnLabCode> snLabCodeMap = hmeSnLabCodeList.stream().collect(Collectors.toMap(item -> item.getTenantId() + symbol + fetchGroupKey(item.getOperationId(),item.getMaterialLotId()), t -> t));
                for (List<HmeRouterStepVO3> item:currentStepMap.values()
                ) {
                    //更新实验代码或者备注为空的数据
                    for (HmeRouterStepVO3 hmeRouterStepVO3:item
                    ) {
                        if(StringUtils.isBlank(hmeRouterStepVO3.getLabCode()) || StringUtils.isBlank(hmeRouterStepVO3.getRouterStepRemark())){
                            HmeSnLabCode hmeSnLabCode = snLabCodeMap.getOrDefault(tenantId + symbol + fetchGroupKey(hmeRouterStepVO3.getOperationId(),eoIdAndMaterialLotIdRelMap.getOrDefault(hmeRouterStepVO3.getEoId(),"")) , null);
                            if(Objects.nonNull(hmeSnLabCode)) {
                                if(StringUtils.isBlank(hmeRouterStepVO3.getLabCode())) {
                                    hmeRouterStepVO3.setLabCode(hmeSnLabCode.getLabCode());
                                }
                                if(StringUtils.isBlank(hmeRouterStepVO3.getRouterStepRemark())) {
                                    hmeRouterStepVO3.setRouterStepRemark(hmeSnLabCode.getRemark());
                                }
                            }
                        }
                    }
                }
            }
        }

        routerStepIdList = routerStepIdList.stream().distinct().collect(Collectors.toList());
        operationIdList = operationIdList.stream().distinct().collect(Collectors.toList());
        startDate = System.currentTimeMillis();
        List<HmeEoJobSn> hmeEoJobSnList = hmeEoJobSnMapper.selectByCondition(Condition.builder(HmeEoJobSn.class)
                .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                        .andIn(HmeEoJobSn.FIELD_OPERATION_ID,operationIdList)
                        .andIn(HmeEoJobSn.FIELD_EO_ID, eoIdList)
                        .andIn(HmeEoJobSn.FIELD_EO_STEP_ID,routerStepIdList)
                        .andEqualTo(HmeEoJobSn.FIELD_REWORK_FLAG,HmeConstants.ConstantValue.NO)
                        .andIn(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, materialLotIdList))
                .build());
        log.info("=================================>批量工序作业平台-进站校验-hmeEoJobSnMapper.selectByCondition总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //进站校验，不允许重复进站
        if(CollectionUtils.isNotEmpty(hmeEoJobSnList)) {
            //只要找到一个条码就先报这个条码的报错 modify by yuchao.wang for tianyang.xie at 2020.12.29
            Map<String, List<HmeEoJobSn>> exitSnMap = hmeEoJobSnList.stream().collect(Collectors
                    .groupingBy(item -> item.getEoId() + ',' + item.getMaterialLotId() + ',' + item.getOperationId() + ',' + item.getEoStepId()));

            hmeEoJobSnBatchVO.getSnLineList().forEach(item -> {
                HmeRouterStepVO3 currentStep = currentStepMap.get(item.getEoId()).get(0);
                String key = item.getEoId() + ',' + item.getMaterialLotId() + ',' + currentStep.getOperationId() + ',' + currentStep.getRouterStepId();
                if (exitSnMap.containsKey(key) && CollectionUtils.isNotEmpty(exitSnMap.get(key))) {
                    HmeEoJobSn exitSn = exitSnMap.get(key).get(0);

                    //校验工位是否一致
                    String workcellId = exitSn.getWorkcellId();
                    if (!dto.getWorkcellId().equals(workcellId)) {
                        String workcellCode = "";
                        if (StringUtils.isNotBlank(workcellId)) {
                            MtModWorkcell mtModWorkcell = mtModWorkcellRepository.workcellBasicPropertyGet(tenantId, workcellId);
                            workcellCode = mtModWorkcell.getWorkcellCode();
                        }
                        //SN【${1}】已在【${2}】工位进站,不允许重复进站
                        throw new MtException("HME_EO_JOB_SN_159", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_159", "HME", item.getSnNum(), workcellCode));
                    }

                    //校验平台是否一致
                    String jobType = exitSn.getJobType();
                    if (!dto.getJobType().equals(jobType)) {
                        String jobTypeDesc = "";
                        switch(jobType){
                            case HmeConstants.JobType.SINGLE_PROCESS:
                                jobTypeDesc = HmeConstants.JobTypeDesc.SINGLE_PROCESS;
                                break;
                            case HmeConstants.JobType.TIME_PROCESS:
                                jobTypeDesc = HmeConstants.JobTypeDesc.TIME_PROCESS;
                                break;
                            case HmeConstants.JobType.PREPARE_PROCESS:
                                jobTypeDesc = HmeConstants.JobTypeDesc.PREPARE_PROCESS;
                                break;
                            case HmeConstants.JobType.COS_COMPLETED:
                                jobTypeDesc = HmeConstants.JobTypeDesc.COS_COMPLETED;
                                break;
                            case HmeConstants.JobType.PACKAGE_PROCESS_PDA:
                                jobTypeDesc = HmeConstants.JobTypeDesc.PACKAGE_PROCESS_PDA;
                                break;
                            case HmeConstants.JobType.REPAIR_PROCESS:
                                jobTypeDesc = HmeConstants.JobTypeDesc.REPAIR_PROCESS;
                                break;
                            default:
                                break;
                        }

                        //SN【${1}】已在【${2}】平台进站,不允许重复进站
                        throw new MtException("HME_EO_JOB_SN_160", mtErrorMessageRepository
                                .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_160", "HME", item.getSnNum(), jobTypeDesc));
                    }

                    // SN【${1}】已进站,请检查
                    throw new MtException("HME_EO_JOB_SN_122", mtErrorMessageRepository
                            .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_122", "HME", item.getSnNum()));
                }
            });
        }

        hmeEoJobSnBatchVO.getSnVOList().stream().forEach(snVO ->{
            // 验证EO状态
            if (!HmeConstants.EoStatus.WORKING.equals(snVO.getStatus())
                    || (!HmeConstants.EoStatus.RELEASED.equals(snVO.getLastEoStatus())
                    && !HmeConstants.EoStatus.HOLD.equals(snVO.getLastEoStatus()))) {
                //EO状态必须为运行状态
                throw new MtException("HME_EO_JOB_SN_003", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_003", "HME"));
            }

            //验证质量状态
            if (!HmeConstants.ConstantValue.OK.equals(snVO.getQualityStatus())) {
                // SN已判定不良，请判定处置方法后执行工序作业
                throw new MtException("HME_EO_JOB_SN_029", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_029", "HME"));
            }
        });

        //V20210525 modify by penglin.sui for peng.zhao 进站校验工位绑定设备
        for (String operationId : operationIdList
             ) {
            hmeEoJobSnCommonService.workcellBindEquipmentValidate(tenantId, operationId, dto.getWorkcellId());
        }

        //批量查询最近一步工艺步骤
        startDate = System.currentTimeMillis();
        Map<String, HmeRouterStepVO> nearStepMap = hmeEoJobSnCommonService.batchQueryRouterStepByEoIds(tenantId, HmeConstants.StepType.NEAR_STEP, eoIdList);
        log.info("=================================>批量工序作业平台-进站校验-批量查询最近一步工艺步骤总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //批量查询最近一步工艺步骤
        startDate = System.currentTimeMillis();
        Map<String, HmeRouterStepVO> normalStepMap = hmeEoJobSnCommonService.batchQueryRouterStepByEoIds(tenantId, HmeConstants.StepType.NORMAL_STEP, eoIdList);
        log.info("=================================>批量工序作业平台-进站校验-批量查询最近一步工艺步骤2总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //批量查询下一步骤
        List<String> normalRouterStepIdList = new ArrayList<>();
        normalStepMap.values().stream().forEach(item -> {
            normalRouterStepIdList.add(item.getRouterStepId());
        });
        startDate = System.currentTimeMillis();
        Map<String, String> nextStepMap = hmeEoJobSnCommonService.batchQueryNextStep(tenantId,normalRouterStepIdList);
        log.info("=================================>批量工序作业平台-进站校验-批量查询下一步骤总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //批量查询工单类型
        startDate = System.currentTimeMillis();
        Map<String,HmeEoJobSnBatchVO3> workOrderTypeMap = hmeEoJobSnCommonService.batchQtyAfterSalesWorkOrder(tenantId,eoIdList);
        log.info("=================================>批量工序作业平台-进站校验-批量查询工单类型总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        //查询售后工单类型LOV
        List<LovValueDTO> woTypeLovs = lovAdapter.queryLovValue("HME.NOT_CHECK_JUMP_WOTYPE", tenantId);
        List<String> lovValueList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(woTypeLovs)) {
            lovValueList = woTypeLovs.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        }

        List<String> finalLovValueList = lovValueList;
        hmeEoJobSnBatchVO.getSnLineList().stream().forEach(snLine -> {
            HmeRouterStepVO3 currentStep = currentStepMap.get(snLine.getEoId()).get(0);
            snLine.setEoStepId(currentStep.getRouterStepId());
            snLine.setOperationId(currentStep.getOperationId());
            snLine.setReworkFlag(HmeConstants.ConstantValue.NO);

            //非入口步骤
            if(!HmeConstants.ConstantValue.YES.equals(currentStep.getEntryStepFlag())) {
                //判断是否有最近最近步骤
                HmeRouterStepVO nearStepVO = nearStepMap.get(snLine.getEoId());
                if(Objects.isNull(nearStepVO)){
                    // 当前为非入口步骤, 且根据EO没有找到最近加工步骤
                    throw new MtException("HME_EO_JOB_SN_046",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_046", "HME"));
                }

                HmeEoJobSnBatchVO3 workOrderType = workOrderTypeMap.getOrDefault(snLine.getEoId(),null);
                boolean checkFlag = true;
                if(Objects.nonNull(workOrderType) && CollectionUtils.isNotEmpty(finalLovValueList)){
                    if(finalLovValueList.contains(workOrderType.getWorkOrderType())){
                        checkFlag = false;
                    }
                }
                if(checkFlag && nearStepVO.getCompletedQty().compareTo(BigDecimal.ZERO) == 0){
                    // 请先完成工序步骤【${1}】的出站操作, 再执行本道工序步骤进站
                    throw new MtException("HME_EO_JOB_SN_031",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_031", "HME",
                                    nearStepVO.getStepName()));
                }

                if (!snLine.getEoStepId().equals(nextStepMap.get(normalStepMap.get(snLine.getEoId()).getRouterStepId()))) {
                    // 本道工序步骤不为工序步骤{最近加工步骤描述}的下一步骤
                    throw new MtException("HME_EO_JOB_SN_032",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_032", "HME",
                                    nearStepVO.getStepName()));
                }
            }
        });

        hmeEoJobSnBatchVO.setNearStepMap(nearStepMap);
        hmeEoJobSnBatchVO.setCurrentStepMap(currentStepMap);
        return hmeEoJobSnBatchVO;
    }

    @Override
    public HmeEoJobSnBatchVO12 releaseValidate(Long tenantId, HmeEoJobSnBatchDTO4 dto) {
        HmeEoJobSnBatchVO12 resultVO = new HmeEoJobSnBatchVO12();
        if(CollectionUtils.isEmpty(dto.getComponentList())){
            //请先勾选要投料的条码
            throw new MtException("HME_EO_JOB_SN_129", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_129", "HME"));
        }

        //校验物料是否重复
        List<String> materialIdList = dto.getComponentList().stream().map(HmeEoJobSnBatchVO4::getMaterialId).collect(Collectors.toList());
        List<String> materialCodeList = new ArrayList<>();
        for (HmeEoJobSnBatchVO4 item:dto.getComponentList()
             ) {
            long count = materialIdList.stream().filter(item2 -> item2.equals(item.getMaterialId())).count();
            if(count > 1 && !materialCodeList.contains(item.getMaterialCode())){
                materialCodeList.add(item.getMaterialCode());
            }
        }
        if(CollectionUtils.isNotEmpty(materialCodeList)){
            //投料界面物料${1}重复,请检查BOM!
            throw new MtException("HME_EO_JOB_SN_157", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_157", "HME", StringUtils.join(materialCodeList.toArray(), ",")));
        }

        List<HmeEoJobSnBatchVO4> hmeEoJobSnBatchVO4List = dto.getComponentList().stream().filter(item -> HmeConstants.ConstantValue.ONE.equals(item.getIsReleased()))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(hmeEoJobSnBatchVO4List)){
            //请先勾选要投料的条码
            throw new MtException("HME_EO_JOB_SN_129", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_129", "HME"));
        }

        dto.getSnLineListDto().getDtoList().forEach(item ->{
            if(StringUtils.isBlank(item.getOperationId())){
                //当前工序WKC没有维护工艺
                throw new MtException("HME_EO_JOB_SN_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_013", "HME"));
            }
            if(StringUtils.isBlank(item.getEoStepId())){
                // SN对应的工步为空,请检查!
                throw new MtException("HME_EO_JOB_SN_146", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_146", "HME"));
            }
        });

        //校验EO对应的组件行，只能投最多2种物料
//        List<HmeEoJobSnBatchVO4> normalComponentList = dto.getComponentList().stream().filter(item -> !HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag())
//                && !HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag())).collect(Collectors.toList());
//        if(CollectionUtils.isNotEmpty(normalComponentList)) {
//            List<HmeEoJobSnBatchVO4> hmeEoJobSnBatchVO4List2 = normalComponentList.stream().filter(item -> item.getReleasedQty().compareTo(BigDecimal.ZERO) > 0
//                    || (item.getReleasedQty().compareTo(BigDecimal.ZERO) <= 0 && HmeConstants.ConstantValue.ONE.equals(item.getIsReleased()) && item.getWillReleaseQty().compareTo(BigDecimal.ZERO) > 0)
//            ).collect(Collectors.toList());
//            Map<String, List<HmeEoJobSnBatchVO4>> componentReleaseMap = new HashMap<>();
//            if (CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO4List2)) {
//                componentReleaseMap = hmeEoJobSnBatchVO4List2.stream().collect(Collectors.groupingBy(e -> e.getComponentMaterialId()));
//                componentReleaseMap.entrySet().forEach(entry -> {
//                    if (entry.getValue().size() > 2) {
//                        //对于组件物料及其替代料不允许投料超过两种,请检查
//                        throw new MtException("HME_EO_JOB_SN_130", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "HME_EO_JOB_SN_130", "HME"));
//                    }
//                });
//            }
//        }
        //组件清单中的组件
        List<HmeEoJobSnBatchVO4> componentList = dto.getComponentList().stream().filter(item -> StringUtils.isNotBlank(item.getBomComponentId())).collect(Collectors.toList());
        List<String> bomComponentIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(componentList)) {
            bomComponentIdList = componentList.stream().map(HmeEoJobSnBatchVO4::getBomComponentId).collect(Collectors.toList());
        }

        //全局替代料
        List<HmeEoJobSnBatchVO4> substituteList = dto.getComponentList().stream().filter(item -> StringUtils.isBlank(item.getBomComponentId())).collect(Collectors.toList());
        List<String> substituteMaterialIdList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(substituteList)) {
            substituteMaterialIdList = componentList.stream().map(HmeEoJobSnBatchVO4::getBomComponentId).collect(Collectors.toList());
        }

        //查询BOM扩展属性
        Map<String,String> bomExtendAttrMap = new HashMap<>();
        Map<String,MtExtendAttrVO1> bomExtendAttrMap2 = new HashMap<>();
        if(CollectionUtils.isNotEmpty(bomComponentIdList)) {
            List<String> bomAttrNameList = new ArrayList<>();
            bomAttrNameList.add(HmeConstants.BomComponentExtendAttr.SAP_REQUIREMENT_QTY);
            bomAttrNameList.add(HmeConstants.BomComponentExtendAttr.SPECIAL_INVENTORY_FLAG);
            List<MtExtendAttrVO1> bomExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_bom_component_attr", "BOM_COMPONENT_ID"
                    , bomComponentIdList, bomAttrNameList);
            if (CollectionUtils.isNotEmpty(bomExtendAttrVO1List)) {
                bomExtendAttrMap = bomExtendAttrVO1List.stream().collect(Collectors.toMap(item -> fetchGroupKey(item.getKeyId(),item.getAttrName()), MtExtendAttrVO1::getAttrValue));
                bomExtendAttrMap2 = bomExtendAttrVO1List.stream().collect(Collectors.toMap(item -> fetchGroupKey(item.getKeyId(),item.getAttrName()), t -> t));
            }
        }
        long startDate = System.currentTimeMillis();
        //查询工单组件实绩
        List<MtWorkOrderComponentActual> mtWorkOrderComponentActualList = new ArrayList<>();
        Map<String,BigDecimal> workOrderComponentActualMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(bomComponentIdList)) {
            startDate = System.currentTimeMillis();
            mtWorkOrderComponentActualList = hmeEoJobSnBatchMapper.selectWorkOrderComponentActual(tenantId,dto.getSnLineListDto().getDtoList().get(0).getWorkOrderId(),new ArrayList<>(),bomComponentIdList);
            log.info("=================================>批量工序作业平台-投料校验-查询工单组件实绩总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            if(CollectionUtils.isNotEmpty(mtWorkOrderComponentActualList)) {
                workOrderComponentActualMap = mtWorkOrderComponentActualList.stream().collect(Collectors.toMap(MtWorkOrderComponentActual::getBomComponentId, item -> BigDecimal.valueOf(item.getAssembleQty()).add(BigDecimal.valueOf(item.getScrappedQty()))));
            }
        }
        List<MtWorkOrderComponentActual> mtWorkOrderSubstituteActualList = new ArrayList<>();
        Map<String,BigDecimal> workOrderSubstituteActualMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(substituteMaterialIdList)) {
            startDate = System.currentTimeMillis();
            mtWorkOrderSubstituteActualList = hmeEoJobSnBatchMapper.selectWorkOrderComponentActual(tenantId,dto.getSnLineListDto().getDtoList().get(0).getWorkOrderId(),substituteMaterialIdList,new ArrayList<>());
            log.info("=================================>批量工序作业平台-投料校验-查询工单组件实绩2总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            if(CollectionUtils.isNotEmpty(mtWorkOrderSubstituteActualList)) {
                workOrderSubstituteActualMap = mtWorkOrderSubstituteActualList.stream().collect(Collectors.toMap(MtWorkOrderComponentActual::getMaterialId, item -> BigDecimal.valueOf(item.getAssembleQty()).add(BigDecimal.valueOf(item.getScrappedQty()))));
            }
        }

        //超量校验
        Map<String,List<HmeEoJobSnBatchVO4>> componentOverReleaseMap = dto.getComponentList().stream().collect(Collectors.groupingBy(e -> e.getComponentMaterialId()));
        Map<String, BigDecimal> finalWorkOrderComponentActualMap = workOrderComponentActualMap;
        Map<String, BigDecimal> finalWorkOrderSubstituteActualMap = workOrderSubstituteActualMap;
        Map<String, String> finalBomExtendAttrMap = bomExtendAttrMap;
        componentOverReleaseMap.entrySet().forEach(entry -> {
            BigDecimal willReleaseQtySum = entry.getValue().stream().map(HmeEoJobSnBatchVO4::getWillReleaseQty).reduce(BigDecimal.ZERO,BigDecimal::add);
            BigDecimal releasedQtySum = entry.getValue().stream().map(HmeEoJobSnBatchVO4::getReleasedQty).reduce(BigDecimal.ZERO,BigDecimal::add);
            List<HmeEoJobSnBatchVO4> componentList2 = entry.getValue().stream().filter(item -> item.getComponentMaterialId().equals(item.getMaterialId())).collect(Collectors.toList());
            BigDecimal requirementQtySum = componentList2.get(0).getRequirementQty();
            if(willReleaseQtySum.add(releasedQtySum).compareTo(requirementQtySum) > 0){
                log.info("<============将投 + 已投 < 需求==========>" + willReleaseQtySum + "-" + releasedQtySum + "-" + requirementQtySum);
                //物料${1}投料数量大于可投数量,请检查
                throw new MtException("HME_EO_JOB_SN_131", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_131", "HME", componentList2.get(0).getMaterialCode()));
            }

//            BigDecimal woReleasedScrappedQtySum = BigDecimal.ZERO;
//            BigDecimal sapRequirementQtySum = BigDecimal.ZERO;
//            for (HmeEoJobSnBatchVO4 item:entry.getValue()
//            ) {
//                if(StringUtils.isNotBlank(item.getBomComponentId())) {
//                    woReleasedScrappedQtySum = woReleasedScrappedQtySum.add(finalWorkOrderComponentActualMap.getOrDefault(item.getBomComponentId(),BigDecimal.ZERO));
//                    //SAP需求数量：取主料的数量
//                    if(item.getComponentMaterialId().equals(item.getMaterialId())) {
//                        sapRequirementQtySum = sapRequirementQtySum.add(new BigDecimal(finalBomExtendAttrMap.getOrDefault(fetchGroupKey(item.getBomComponentId(), HmeConstants.BomComponentExtendAttr.SAP_REQUIREMENT_QTY), "0")));
//                    }
//                }else{
//                    woReleasedScrappedQtySum = woReleasedScrappedQtySum.add(finalWorkOrderSubstituteActualMap.getOrDefault(item.getMaterialId(),BigDecimal.ZERO));
//                }
//            }
//            if(willReleaseQtySum.add(woReleasedScrappedQtySum).compareTo(sapRequirementQtySum) > 0){
//                log.info("<============将投 + 报废 < SAP总需求需求==========>" + willReleaseQtySum + "-" + woReleasedScrappedQtySum + "-" + sapRequirementQtySum);
//                //物料${1}投料数量大于可投数量,请检查
//                throw new MtException("HME_EO_JOB_SN_131", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                        "HME_EO_JOB_SN_131", "HME", componentList2.get(0).getMaterialCode()));
//            }
        });
        List<String> snLineBomIdList = dto.getSnLineListDto().getDtoList().stream().map(HmeEoJobSnVO::getBomId).distinct().collect(Collectors.toList());
        //销售订单、BOM校验
        List<String> finalMaterialLotIdList = new ArrayList<>();
        List<String> finalMaterialLotIdList2 = new ArrayList<>();
        StringBuilder freezeMaterialLotCodes = new StringBuilder();
        StringBuilder stockStakeMaterialLotCodes = new StringBuilder();
        dto.getComponentList().forEach(item -> {
            List<HmeEoJobSnBatchVO6> releaseMaterialLotList;
            if(CollectionUtils.isNotEmpty(item.getMaterialLotList())) {
                releaseMaterialLotList = item.getMaterialLotList().stream().filter(item2 -> HmeConstants.ConstantValue.ONE.equals(item2.getIsReleased()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(releaseMaterialLotList)) {
                    for (HmeEoJobSnBatchVO6 hmeEoJobSnBatchVO6:releaseMaterialLotList
                         ) {

                        if(!finalMaterialLotIdList.contains(hmeEoJobSnBatchVO6.getMaterialLotId())){
                            finalMaterialLotIdList.add(hmeEoJobSnBatchVO6.getMaterialLotId());
                        }

                        if(HmeConstants.ConstantValue.ONE.equals(hmeEoJobSnBatchVO6.getIsReleased()) && !finalMaterialLotIdList2.contains(hmeEoJobSnBatchVO6.getMaterialLotId())){
                            finalMaterialLotIdList2.add(hmeEoJobSnBatchVO6.getMaterialLotId());
                        }

                        if(HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO6.getFreezeFlag())){
                            if(freezeMaterialLotCodes.length() == 0){
                                freezeMaterialLotCodes.append(hmeEoJobSnBatchVO6.getMaterialLotCode());
                            }else{
                                freezeMaterialLotCodes.append("," + hmeEoJobSnBatchVO6.getMaterialLotCode());
                            }
                        }
                        if(HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO6.getStocktakeFlag())){
                            if(stockStakeMaterialLotCodes.length() == 0){
                                stockStakeMaterialLotCodes.append(hmeEoJobSnBatchVO6.getMaterialLotCode());
                            }else{
                                stockStakeMaterialLotCodes.append("," + hmeEoJobSnBatchVO6.getMaterialLotCode());
                            }
                        }
                    }
                    //V20201222 modify by penglin.sui for hui.ma 新增盘点冻结标识校验
                    if(freezeMaterialLotCodes.length() > 0){
                        // 物料批${1}已被冻结,不可对物料批进行操作!
                        throw new MtException("MT_MATERIAL_TFR_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_TFR_0005", "HME", freezeMaterialLotCodes.toString()));
                    }
                    //V20201222 modify by penglin.sui for hui.ma 新增盘点停用标识校验
                    if(stockStakeMaterialLotCodes.length() > 0){
                        // 物料批${1}正在被盘点,不可对物料批进行操作!
                        throw new MtException("MT_MATERIAL_TFR_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_MATERIAL_TFR_0006", "HME", stockStakeMaterialLotCodes.toString()));
                    }
                }
            }

            //BOM校验
            if(StringUtils.isNotBlank(item.getBomId())) {
                if (!snLineBomIdList.contains(item.getBomId())) {
                    //EO与物料对应的BOM不一致,请检查!
                    throw new MtException("HME_EO_JOB_SN_145", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_145", "HME"));
                }
            }
        });
        Map<String, String> materialLotExtendAttrMap = new HashMap<>();
        List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList = new ArrayList<>();
        List<HmeMaterialLotLabCode> releaseMaterialLotLabCodeList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(finalMaterialLotIdList)) {
            List<String> materialLotIdList = finalMaterialLotIdList;
            //查询条码扩展属性
            materialLotIdList = materialLotIdList.stream().distinct().collect(Collectors.toList());
            List<String> materialLotAttrNameList = new ArrayList<>();
            materialLotAttrNameList.add(HmeConstants.ExtendAttr.SO_NUM);
            materialLotAttrNameList.add(HmeConstants.ExtendAttr.SO_LINE_NUM);
            materialLotAttrNameList.add(HmeConstants.ExtendAttr.SAP_ACCOUNT_FLAG);
            materialLotAttrNameList.add(HmeConstants.ExtendAttr.MATERIAL_VERSION);
            materialLotAttrNameList.add(HmeConstants.ExtendAttr.LAB_CODE);
            startDate = System.currentTimeMillis();
            List<MtExtendAttrVO1> materialLotExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_material_lot_attr", "MATERIAL_LOT_ID"
                    , materialLotIdList, materialLotAttrNameList);
            log.info("=================================>批量工序作业平台-投料校验-查询条码扩展属性总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            if (CollectionUtils.isNotEmpty(materialLotExtendAttrVO1List)) {
                materialLotExtendAttrMap = materialLotExtendAttrVO1List.stream().collect(Collectors.toMap(item -> fetchGroupKey(item.getKeyId(),item.getAttrName()), MtExtendAttrVO1::getAttrValue));
            }

            //V20210125 modify by penglin.sui for hui.ma 校验实验代码是否一致
            //查询实验代码
            if(CollectionUtils.isNotEmpty(finalMaterialLotIdList2)) {
                List<String> snMaterialLotIdList = dto.getSnLineListDto().getDtoList().stream().map(HmeEoJobSnVO::getMaterialLotId).distinct().collect(Collectors.toList());
                List<String> eoStepIdList = dto.getSnLineListDto().getDtoList().stream().map(HmeEoJobSnVO::getEoStepId).distinct().collect(Collectors.toList());
                //查询进站SN实验代码
                hmeMaterialLotLabCodeList = hmeMaterialLotLabCodeMapper.batchSelectLabCode2(tenantId, snMaterialLotIdList, eoStepIdList,null);
                List<String> materialLotIdList2 = finalMaterialLotIdList2.stream().distinct().collect(Collectors.toList());
                for (String materialLotId : materialLotIdList2
                ) {
                    String labCode = materialLotExtendAttrMap.get(fetchGroupKey(materialLotId, HmeConstants.ExtendAttr.LAB_CODE));
                    if (StringUtils.isNotBlank(labCode)) {
                        for (HmeEoJobSnVO hmeEoJobSnVO:dto.getSnLineListDto().getDtoList()
                             ) {
                            List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList1 = hmeMaterialLotLabCodeList.stream().filter(item -> item.getMaterialLotId().equals(hmeEoJobSnVO.getMaterialLotId())
                                    && item.getRouterStepId().equals(hmeEoJobSnVO.getEoStepId())).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(hmeMaterialLotLabCodeList1)) {
                                List<HmeMaterialLotLabCode> hmeMaterialLotLabCodeList2 = hmeMaterialLotLabCodeList1.stream().filter(item -> labCode.equals(item.getLabCode()))
                                        .collect(Collectors.toList());
                                if (CollectionUtils.isEmpty(hmeMaterialLotLabCodeList2)) {
                                    //条码的实验代码【${1}】与工序的实验代码【${2}】不一致,请检查
                                    List<String> labCodeList = hmeMaterialLotLabCodeList1.stream().map(HmeMaterialLotLabCode::getLabCode).distinct().collect(Collectors.toList());
                                    throw new MtException("HME_EO_JOB_SN_173", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_EO_JOB_SN_173", "HME", labCode, StringUtils.join(labCodeList,",")));
                                }
                            }
                        }
                    }
                }

                //查询投料条码的实验代码
                releaseMaterialLotLabCodeList = hmeMaterialLotLabCodeMapper.batchSelectLabCode2(tenantId, finalMaterialLotIdList2, eoStepIdList,null);
            }
        }

        //查询工单扩展属性
        List<String> workOrderIdList = new ArrayList<>();
        workOrderIdList.add(dto.getSnLineListDto().getDtoList().get(0).getWorkOrderId());
        List<String> woAttrNameList = new ArrayList<>();
        woAttrNameList.add("attribute1");
        woAttrNameList.add("attribute7");
        List<MtExtendAttrVO1> woExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_work_order_attr", "WORK_ORDER_ID"
                , workOrderIdList, woAttrNameList);
        Map<String, String> woExtendAttrMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(woExtendAttrVO1List)) {
            woExtendAttrMap = woExtendAttrVO1List.stream().collect(Collectors.toMap(MtExtendAttrVO1::getAttrName, MtExtendAttrVO1::getAttrValue));
        }
        String woSoNum = woExtendAttrMap.getOrDefault("attribute1","");
        String woSoLineNum = woExtendAttrMap.getOrDefault("attribute7","");

        Map<String, MtExtendAttrVO1> finalBomExtendAttrMap2 = bomExtendAttrMap2;
        Map<String, String> finalMaterialLotExtendAttrMap = materialLotExtendAttrMap;
        finalBomExtendAttrMap2.forEach((k, v) -> {
            if("E".equals(v.getAttrValue())){
                log.info("<==========v.keyID=========>" + v.getKeyId());
                log.info("<==========hmeEoJobSnBatchVO4List.size=========>" + hmeEoJobSnBatchVO4List.size());
                List<HmeEoJobSnBatchVO4> currComponentList = hmeEoJobSnBatchVO4List.stream().filter(item -> StringUtils.isNotBlank(item.getBomComponentId())
                        && item.getBomComponentId().equals(v.getKeyId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(currComponentList)){
                    currComponentList.forEach(item -> {
                        if(CollectionUtils.isNotEmpty(item.getMaterialLotList())){
                            List<HmeEoJobSnBatchVO6> releasedMaterialLotList = item.getMaterialLotList().stream().filter(item2 -> HmeConstants.ConstantValue.ONE.equals(item2.getIsReleased()))
                                    .collect(Collectors.toList());
                            if(CollectionUtils.isNotEmpty(releasedMaterialLotList)) {
                                releasedMaterialLotList.forEach(item2 -> {
                                    String materialLotSoNum = finalMaterialLotExtendAttrMap.getOrDefault(fetchGroupKey(item2.getMaterialLotId(), "SO_NUM"), "");
                                    String materialLotSoLineNum = finalMaterialLotExtendAttrMap.getOrDefault(fetchGroupKey(item2.getMaterialLotId(), "SO_LINE_NUM"), "");
                                    if (!(woSoNum + "-" + woSoLineNum).equals(materialLotSoNum + "-" + materialLotSoLineNum)) {
                                        //工单销售订单【${1}】与条码销售订单【${2}】不匹配,请检查
                                        throw new MtException("HME_EO_JOB_SN_112", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "HME_EO_JOB_SN_112", "HME", woSoNum + "-" + woSoLineNum, materialLotSoNum + "-" + materialLotSoLineNum));
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        //升级校验
        List<HmeEoJobSnBatchVO4> upgradeComponentList = dto.getComponentList().stream().filter(item -> HmeConstants.ConstantValue.YES.equals(item.getUpgradeFlag()))
                .collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(upgradeComponentList)){
            if(upgradeComponentList.size() > 1) {
                MtRouterStep currentRouterStep =
                        mtRouterStepRepository.routerStepGet(tenantId, dto.getSnLineListDto().getDtoList().get(0).getEoStepId());
                MtMaterial mtMaterial = mtMaterialRepository.materialPropertyGet(tenantId,upgradeComponentList.get(1).getMaterialId());
                // 当前工艺步骤【${1}】SN升级标志属性的物料应有且只有1个,请检查物料【${2}】的扩展属性
                throw new MtException("HME_EO_JOB_SN_027",
                        mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EO_JOB_SN_027", "HME", currentRouterStep.getStepName(),
                                mtMaterial.getMaterialName()));
            }
        }

        //V20210219 modify by penglin.sui for hui.ma 校验预装组件和当前所需装配虚拟件组件的一致性
        Map<String, List<HmeEoJobSnBatchVO6>> virtualComponentMaterialLotMap = new HashMap<>();
        Map<String, String> virtualJobMap = new HashMap<>();
        Map<String, HmeEoJobSn> virtualEoJobSnMap = new HashMap<>();
        //虚拟件
        List<HmeEoJobSnBatchVO4> virtualList = dto.getComponentList().stream().filter(item ->
                HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag()) && HmeConstants.ConstantValue.ONE.equals(item.getIsReleased())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(virtualList)) {
            //虚拟件 && 虚拟件组件
            List<HmeEoJobSnBatchVO4> virtualComponentList = dto.getComponentList().stream().filter(item -> (
                    (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(item.getVirtualFlag()) && HmeConstants.ConstantValue.ONE.equals(item.getIsReleased()))
                            || HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()))).collect(Collectors.toList());
            HmeEoJobSnBatchVO16 hmeEoJobSnBatchVO16 = hmeEoJobSnCommonService.selectVirtualComponent(tenantId, virtualComponentList);
            if (MapUtils.isNotEmpty(hmeEoJobSnBatchVO16.getVirtualJobMap())) {
                virtualJobMap = hmeEoJobSnBatchVO16.getVirtualJobMap();
            }
            if (MapUtils.isNotEmpty(hmeEoJobSnBatchVO16.getVirtualComponentMaterialLotMap())) {
                virtualComponentMaterialLotMap = hmeEoJobSnBatchVO16.getVirtualComponentMaterialLotMap();
            }else{
                //虚拟件下未找到组件
                throw new MtException("HME_EO_JOB_SN_066", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_066", "HME"));
            }

            if (MapUtils.isNotEmpty(hmeEoJobSnBatchVO16.getVirtualEoJobSnMap())) {
                virtualEoJobSnMap = hmeEoJobSnBatchVO16.getVirtualEoJobSnMap();
            }

            //虚拟件组件
            List<HmeEoJobSnBatchVO4> virtualComponentList2 = dto.getComponentList().stream().filter(item -> HmeConstants.VirtualFlag.VIRTUAL_COMPONENT_FLAG.equals(item.getVirtualComponentFlag()))
                    .collect(Collectors.toList());

            for (HmeEoJobSnBatchVO4 virtual : virtualList
            ) {
                List<HmeEoJobSnBatchVO4> currVirtualComponentList = virtualComponentList2.stream().filter(item -> item.getTopVirtualMaterialCode().equals(virtual.getMaterialCode()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isEmpty(currVirtualComponentList)){
                    //虚拟件下未找到组件
                    throw new MtException("HME_EO_JOB_SN_066", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_066", "HME"));
                }

                List<String> eoVirtualComponentMaterialIdList = currVirtualComponentList.stream().map(HmeEoJobSnBatchVO4::getMaterialId).distinct()
                        .collect(Collectors.toList());

                List<String> virtualComponentMaterialIdList = new ArrayList<>();
                for(List<HmeEoJobSnBatchVO6> hmeEoJobSnBatchVO6List: virtualComponentMaterialLotMap.values()){
                    List<HmeEoJobSnBatchVO6> hmeEoJobSnBatchVO6List2 = hmeEoJobSnBatchVO6List.stream().filter(item -> item.getTopVirtualMaterialCode().equals(virtual.getMaterialCode()))
                            .collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(hmeEoJobSnBatchVO6List2)){
                        virtualComponentMaterialIdList.addAll(hmeEoJobSnBatchVO6List2.stream().map(HmeEoJobSnBatchVO6::getMaterialId).distinct().collect(Collectors.toList()));
                    }
                }

                if(CollectionUtils.isEmpty(virtualComponentMaterialIdList)){
                    //虚拟件条码下的组件物料与EO下的虚拟件组件不一致,请检查!
                    throw new MtException("HME_EO_JOB_SN_186", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_186", "HME"));
                }

                virtualComponentMaterialIdList = virtualComponentMaterialIdList.stream().distinct().collect(Collectors.toList());

                if(eoVirtualComponentMaterialIdList.size() != virtualComponentMaterialIdList.size()){
                    //虚拟件条码下的组件物料与EO下的虚拟件组件不一致,请检查!
                    throw new MtException("HME_EO_JOB_SN_186", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_186", "HME"));
                }

                if(!eoVirtualComponentMaterialIdList.containsAll(virtualComponentMaterialIdList)){
                    //虚拟件条码下的组件物料与EO下的虚拟件组件不一致,请检查!
                    throw new MtException("HME_EO_JOB_SN_186", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_186", "HME"));
                }
            }
        }

        resultVO.setMaterialLotAttrMap(materialLotExtendAttrMap);
        resultVO.setVirtualJobMap(virtualJobMap);
        resultVO.setVirtualEoJobSnMap(virtualEoJobSnMap);
        resultVO.setVirtualComponentMaterialLotMap(virtualComponentMaterialLotMap);
        resultVO.setHmeMaterialLotLabCodeList(hmeMaterialLotLabCodeList);
        resultVO.setReleaseMaterialLotLabCodeList(releaseMaterialLotLabCodeList);
        return resultVO;
    }

    @Override
    public HmeEoJobSnBatchVO8 releaseScanValidate(Long tenantId, HmeEoJobSnBatchDTO2 dto) {
        HmeEoJobSnBatchVO8 hmeEoJobSnBatchVO8 = new HmeEoJobSnBatchVO8();
        if(CollectionUtils.isEmpty(dto.getSnLineList())){
            //请先选择SN条码
            throw new MtException("HME_EO_JOB_SN_127", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_127", "HME"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotCode())){
            // 扫描条码为空,请确认
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        MtMaterialLot mtMaterialLotPara = new MtMaterialLot();
        mtMaterialLotPara.setTenantId(tenantId);
        mtMaterialLotPara.setMaterialLotCode(dto.getMaterialLotCode());
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLotPara);
        hmeEoJobSnBatchVO8.setMtMaterialLot(mtMaterialLot);
        if(Objects.isNull(mtMaterialLot)){
            //扫描条码不存在
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        if(!HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getEnableFlag())){
            //条码${1}已失效,请检查!
            throw new MtException("HME_WO_INPUT_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_WO_INPUT_0004", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        if(!HmeConstants.ConstantValue.OK.equals(mtMaterialLot.getQualityStatus())){
            // 条码号【${1}】不为OK状态,请核实所录入条码
            throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //V20201222 modify by penglin.sui for hui.ma 新增盘点冻结标识校验
        if(HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag())){
            // 物料批${1}已被冻结,不可对物料批进行操作!
            throw new MtException("MT_MATERIAL_TFR_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0005", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //V20201222 modify by penglin.sui for hui.ma 新增盘点停用标识校验
        if(HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())){
            // 物料批${1}正在被盘点,不可对物料批进行操作!
            throw new MtException("MT_MATERIAL_TFR_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0006", "HME", mtMaterialLot.getMaterialLotCode()));
        }
        //查询物料类型
        String materialType = hmeEoJobSnRepository.getMaterialType(tenantId,dto.getSiteId(),mtMaterialLot.getMaterialId());

        List<HmeEoJobSnBatchVO4> componentList = dto.getComponentList().stream().filter(item -> item.getMaterialId().equals(mtMaterialLot.getMaterialId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(componentList)){
            // 请先查询投料组件清单
            throw new MtException("HME_EO_JOB_SN_128", mtErrorMessageRepository
                    .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_128", "HME"));
        }
        hmeEoJobSnBatchVO8.setComponent(componentList.get(0));
        hmeEoJobSnBatchVO8.setMaterialType(materialType);
        if(HmeConstants.MaterialTypeCode.SN.equals(materialType)){

            if(dto.getSnLineList().size() != HmeConstants.ConstantValue.ONE){
                //选中多个SN时不能对序列号物料投料
                throw new MtException("HME_EO_JOB_SN_126", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_126", "HME"));
            }

            if(BigDecimal.ONE.compareTo(BigDecimal.valueOf(mtMaterialLot.getPrimaryUomQty())) != 0){
                //序列号物料条码数量必须是1
                throw new MtException("HME_EO_JOB_SN_125", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_125", "HME"));
            }

            if (dto.getSnLineList().get(0).getSnNum().equals(dto.getMaterialLotCode())) {
                // 升级的序列号编码不可与进站序列号一致，请检查录入数据
                throw new MtException("HME_EO_JOB_SN_047", mtErrorMessageRepository
                        .getErrorMessageWithModule(tenantId, "HME_EO_JOB_SN_047", "HME"));
            }

            List<HmeEoJobSnBatchVO9> materialLotCodeList = hmeEoJobMaterialMapper.selectMaterialLotBindMaterialLot2(tenantId,dto.getMaterialLotCode());
            if(CollectionUtils.isNotEmpty(materialLotCodeList)){
                List<HmeEoJobSnBatchVO9> existJobMaterialList2 = materialLotCodeList.stream().filter(item -> !item.getJobId().equals(dto.getSnLineList().get(0).getJobId()))
                        .collect(Collectors.toList());
                hmeEoJobSnBatchVO8.setDeleteFlag(HmeConstants.ConstantValue.YES);
                String jobMaterialId = null;
                String currBindSnNum = null;
                Date creationDate = null;
                if(CollectionUtils.isNotEmpty(existJobMaterialList2)){
                    jobMaterialId = existJobMaterialList2.get(0).getJobMaterialId();
                    creationDate = existJobMaterialList2.get(0).getCreationDate();
                    currBindSnNum = existJobMaterialList2.get(0).getMaterialLotCode();
                }else {
                    jobMaterialId = materialLotCodeList.get(0).getJobMaterialId();
                    creationDate = materialLotCodeList.get(0).getCreationDate();
                    currBindSnNum = materialLotCodeList.get(0).getMaterialLotCode();
                }
                hmeEoJobSnBatchVO8.setJobMaterialId(jobMaterialId);
                hmeEoJobSnBatchVO8.setCreationDate(creationDate);
                hmeEoJobSnBatchVO8.getComponent().setCurrBindSnNum(currBindSnNum);
            }else {
                HmeEoJobMaterial notBindJobMaterial = hmeEoJobMaterialMapper.selectNotBindJobMaterial(tenantId,dto.getSnLineList().get(0).getJobId(),mtMaterialLot.getMaterialId());
                if (Objects.nonNull(notBindJobMaterial)) {
                    hmeEoJobSnBatchVO8.setJobMaterialId(notBindJobMaterial.getJobMaterialId());
                    hmeEoJobSnBatchVO8.setCreationDate(notBindJobMaterial.getCreationDate());
                }
            }
        }else if(HmeConstants.MaterialTypeCode.LOT.equals(materialType)){
            List<HmeEoJobSnBatchVO10> workcellCodeList = hmeEoJobLotMaterialMapper.queryHaveBindWorkcell2(tenantId,mtMaterialLot.getMaterialLotId());
            if(CollectionUtils.isNotEmpty(workcellCodeList)){
                List<HmeEoJobSnBatchVO10> existWorkcellList2 = workcellCodeList.stream().filter(item -> !item.getWorkcellId().equals(dto.getWorkcellId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(existWorkcellList2)){
                    //当前条码已与工位【${1}】绑定
                    List<String> workcellCodeList2 = existWorkcellList2.stream().map(HmeEoJobSnBatchVO10::getWorkcellCode).collect(Collectors.toList());
                    throw new MtException("HME_EO_JOB_SN_110", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_110", "HME",String.join(",", workcellCodeList2) ));
                }
                hmeEoJobSnBatchVO8.setDeleteFlag(HmeConstants.ConstantValue.YES);
                hmeEoJobSnBatchVO8.setJobMaterialId(workcellCodeList.get(0).getJobMaterialId());
                hmeEoJobSnBatchVO8.setCreationDate(workcellCodeList.get(0).getCreationDate());
            }else{
                HmeEoJobLotMaterial notBindJobLotMaterial = hmeEoJobLotMaterialMapper.selectNotBindJobMaterial(tenantId,dto.getWorkcellId(),mtMaterialLot.getMaterialId());
                if (Objects.nonNull(notBindJobLotMaterial)) {
                    hmeEoJobSnBatchVO8.setJobMaterialId(notBindJobLotMaterial.getJobMaterialId());
                    hmeEoJobSnBatchVO8.setCreationDate(notBindJobLotMaterial.getCreationDate());
                }
            }
        }else if(HmeConstants.MaterialTypeCode.TIME.equals(materialType)){
            List<HmeEoJobSnBatchVO10> workcellCodeList = hmeEoJobTimeMaterialMapper.queryHaveBindWorkcell2(tenantId,mtMaterialLot.getMaterialLotId());
            if(CollectionUtils.isNotEmpty(workcellCodeList)){
                List<HmeEoJobSnBatchVO10> existWorkcellList2 = workcellCodeList.stream().filter(item -> !item.getWorkcellId().equals(dto.getWorkcellId()))
                        .collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(existWorkcellList2)){
                    //当前条码已与工位【${1}】绑定
                    List<String> workcellCodeList2 = existWorkcellList2.stream().map(HmeEoJobSnBatchVO10::getWorkcellCode).collect(Collectors.toList());
                    throw new MtException("HME_EO_JOB_SN_110", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_110", "HME",String.join(",", workcellCodeList2) ));
                }
                hmeEoJobSnBatchVO8.setDeleteFlag(HmeConstants.ConstantValue.YES);
                hmeEoJobSnBatchVO8.setJobMaterialId(workcellCodeList.get(0).getJobMaterialId());
                hmeEoJobSnBatchVO8.setCreationDate(workcellCodeList.get(0).getCreationDate());
            }else{
                HmeEoJobTimeMaterial notBindJobTimeMaterial = hmeEoJobTimeMaterialMapper.selectNotBindJobMaterial(tenantId,dto.getWorkcellId(),mtMaterialLot.getMaterialId());
                if (Objects.nonNull(notBindJobTimeMaterial)) {
                    hmeEoJobSnBatchVO8.setJobMaterialId(notBindJobTimeMaterial.getJobMaterialId());
                    hmeEoJobSnBatchVO8.setCreationDate(notBindJobTimeMaterial.getCreationDate());
                }
            }
        }
        //如果走删除逻辑，直接返回
        if(StringUtils.isNotBlank(hmeEoJobSnBatchVO8.getDeleteFlag())){
            if(HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO8.getDeleteFlag())){
                return hmeEoJobSnBatchVO8;
            }
        }

        //工位登陆时不允许绑定
        if(dto.getSnLineList().size() == 0 && !HmeConstants.ConstantValue.YES.equals(hmeEoJobSnBatchVO8.getDeleteFlag())){
            //请扫描${1}
            throw new MtException("HME_LOGISTICS_INFO_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOGISTICS_INFO_0002", "HME","SN"));
        }

        //库位校验
        hmeEoJobSnLotMaterialRepository.CheckLocator(tenantId, mtMaterialLot.getLocatorId(), dto.getWorkcellId());

        //V20210219 modify by penglin.sui for hui.ma 虚拟件-校验条码是否是预装条码
        if(HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(componentList.get(0).getVirtualFlag())){
            int count = hmeEoJobSnMapper.selectCountByCondition(Condition.builder(HmeEoJobSn.class)
                            .andWhere(Sqls.custom().andEqualTo(HmeEoJobSn.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSn.FIELD_MATERIAL_LOT_ID, mtMaterialLot.getMaterialLotId())
                            .andEqualTo(HmeEoJobSn.FIELD_JOB_TYPE, HmeConstants.JobType.PREPARE_PROCESS)).build());
            if(count != 1){
                //组件物料是虚拟件,投料条码必须是预装条码,请检查!
                throw new MtException("HME_EO_JOB_SN_185", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_185", "HME"));
            }
        }

        //查询BOM扩展属性
        List<String> bomComponentIdList = new ArrayList<>();
        bomComponentIdList.add(componentList.get(0).getBomComponentId());
        List<String> bomAttrNameList = new ArrayList<>();
        bomAttrNameList.add("lineAttribute11");
        bomAttrNameList.add("lineAttribute7");
        bomAttrNameList.add("lineAttribute8");
        List<MtExtendAttrVO1> bomExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_bom_component_attr","BOM_COMPONENT_ID"
                ,bomComponentIdList,bomAttrNameList);
        Map<String,String> bomExtendAttrMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(bomExtendAttrVO1List)){
            bomExtendAttrMap = bomExtendAttrVO1List.stream().collect(Collectors.toMap(MtExtendAttrVO1::getAttrName,MtExtendAttrVO1::getAttrValue));
        }

        //查询条码扩展属性
        List<String> materialLotIdList = new ArrayList<>();
        materialLotIdList.add(mtMaterialLot.getMaterialLotId());
        List<String> materialLotAttrNameList = new ArrayList<>();
        materialLotAttrNameList.add("MATERIAL_VERSION");
        materialLotAttrNameList.add("SO_NUM");
        materialLotAttrNameList.add("SO_LINE_NUM");
        materialLotAttrNameList.add("MF_FLAG");
        materialLotAttrNameList.add("DEADLINE_DATE");
        List<MtExtendAttrVO1> materialLotExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_material_lot_attr","MATERIAL_LOT_ID"
                ,materialLotIdList,materialLotAttrNameList);
        Map<String,String> materialLotExtendAttrMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(materialLotExtendAttrVO1List)){
            materialLotExtendAttrMap = materialLotExtendAttrVO1List.stream().collect(Collectors.toMap(MtExtendAttrVO1::getAttrName,MtExtendAttrVO1::getAttrValue));
        }

        //查询工单扩展属性
        List<String> workOrderIdList = new ArrayList<>();
        workOrderIdList.add(dto.getSnLineList().get(0).getWorkOrderId());
        List<String> woAttrNameList = new ArrayList<>();
        woAttrNameList.add("attribute1");
        woAttrNameList.add("attribute7");
        List<MtExtendAttrVO1> woExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_work_order_attr","WORK_ORDER_ID"
                ,workOrderIdList,woAttrNameList);
        Map<String,String> woExtendAttrMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(woExtendAttrVO1List)){
            woExtendAttrMap = woExtendAttrVO1List.stream().collect(Collectors.toMap(MtExtendAttrVO1::getAttrName,MtExtendAttrVO1::getAttrValue));
        }

        String specialInvFlag = bomExtendAttrMap.getOrDefault("lineAttribute11","");

        if("E".equals(specialInvFlag)){
            //销售订单校验
            String woSoNum = woExtendAttrMap.getOrDefault("attribute1","");
            String woSoLineNum = woExtendAttrMap.getOrDefault("attribute7","");
            String materialLotSoNum = materialLotExtendAttrMap.getOrDefault("SO_NUM","");
            String materialLotSoLineNum = materialLotExtendAttrMap.getOrDefault("SO_LINE_NUM","");
            if(!(woSoNum + "-" + woSoLineNum).equals(materialLotSoNum + "-" + materialLotSoLineNum)){
                //工单销售订单【${1}】与条码销售订单【${2}】不匹配,请检查
                throw new MtException("HME_EO_JOB_SN_112", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_112", "HME", woSoNum + "-" + woSoLineNum,materialLotSoNum + "-" + materialLotSoLineNum));
            }
        }

        //在制品校验
        if(HmeConstants.ConstantValue.YES.equals(materialLotExtendAttrMap.getOrDefault("MF_FLAG",""))){
            //当前物料仍为在制品,尚未加工完成,无法进行投料
            throw new MtException("HME_EO_JOB_SN_117", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_117", "HME"));
        }

        //获取条码扩展表信息
        String productionVersion = materialLotExtendAttrMap.getOrDefault("MATERIAL_VERSION","");

        //获取组件扩展表信息
        String bomVersion = bomExtendAttrMap.getOrDefault("lineAttribute7","");

        if(StringUtils.isNotBlank(bomVersion)) {
            if (!productionVersion.equals(bomVersion)) {
                //条码物料版本【${1}】与组件需求版本【${2}】不一致
                throw new MtException("HME_EO_JOB_SN_065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_065", "HME", productionVersion, bomVersion));
            }
        }

        hmeEoJobSnBatchVO8.setProductionVersion(productionVersion);
        hmeEoJobSnBatchVO8.setVirtualFlag(bomExtendAttrMap.getOrDefault("lineAttribute8","N"));
        hmeEoJobSnBatchVO8.setDeadLineDate(materialLotExtendAttrMap.getOrDefault("DEADLINE_DATE",""));
        return hmeEoJobSnBatchVO8;
    }

    @Override
    public HmeEoJobSnBatchVO20 releaseBackValidate(Long tenantId, HmeEoJobSnVO9 dto) {
        if (StringUtils.isBlank(dto.getBackMaterialLotCode())) {
            //扫描条码为空,请确认
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        if (Objects.isNull(dto.getBackQty())) {
            //退料数量不能为空
            throw new MtException("HME_EO_JOB_SN_067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_067", "HME"));
        }
        if (Objects.nonNull(dto.getIsScrap()) && HmeConstants.ConstantValue.ONE.equals(dto.getIsScrap())) {
            //报废不可进行退料
            throw new MtException("HME_EO_JOB_SN_155", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_155", "HME"));
        }
        if (dto.getBackQty().compareTo(BigDecimal.ZERO) <= 0) {
            //数量不能为空且只能为正数,请检查
            throw new MtException("WMS_OUTSOURCE_PLATFORM_0008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_OUTSOURCE_PLATFORM_0008", "WMS"));
        }
        if(!HmeConstants.ConstantValue.YES.equals(dto.getProdLineEnableFlag())){
            //SN对应工单未分配产线或分配产线已失效,请检查!
            throw new MtException("HME_EO_JOB_SN_142", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_142", "HME"));
        }
        //校验当前选中数据的投料量是否满足要退回数量
        if (dto.getBackQty().compareTo(dto.getReleaseQty()) > 0) {
            //物料【${1}】的退料数量不可大于已投料数量
            throw new MtException("HME_EO_JOB_SN_069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_069", "HME", dto.getMaterialCode()));
        }
        HmeEoJobSnBatchVO20 resultVO = new HmeEoJobSnBatchVO20();
        //获取退料条码
        SecurityTokenHelper.close();
        HmeEoJobSnBatchVO21 backMaterialLot = hmeEoJobSnBatchMapper.selectBackMaterialLot(tenantId,dto.getBackMaterialLotCode());
        if (Objects.isNull(backMaterialLot)) {
            //扫描条码不存在
            throw new MtException("HME_NC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0003", "HME"));
        }
        //V20201222 modify by penglin.sui for hui.ma 新增盘点冻结标识校验
        if(HmeConstants.ConstantValue.YES.equals(backMaterialLot.getFreezeFlag())){
            // 物料批${1}已被冻结,不可对物料批进行操作!
            throw new MtException("MT_MATERIAL_TFR_0005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0005", "HME", backMaterialLot.getMaterialLotCode()));
        }
        //V20201222 modify by penglin.sui for hui.ma 新增盘点停用标识校验
        if(HmeConstants.ConstantValue.YES.equals(backMaterialLot.getStocktakeFlag())){
            // 物料批${1}正在被盘点,不可对物料批进行操作!
            throw new MtException("MT_MATERIAL_TFR_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_TFR_0006", "HME", backMaterialLot.getMaterialLotCode()));
        }
        //校验选中数据与扫描条码的物料一致
        if (!dto.getMaterialId().equals(backMaterialLot.getMaterialId()) && !HmeConstants.ConstantValue.YES.equals(dto.getUpgradeFlag())) {
            //所退物料与扫描条码物料不一致,请核实
            throw new MtException("HME_EO_JOB_SN_102", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_102", "HME"));
        }
        //校验库位一致
        if (!dto.getLocatorId().equals(backMaterialLot.getLocatorId())) {
            //所退物料所在库位与扫描条码所在库位不一致,请核实
            throw new MtException("HME_EO_JOB_SN_104", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_104", "HME"));
        }
        //校验站点一致
        if (!dto.getSiteId().equals(backMaterialLot.getSiteId())) {
            //所退物料所属站点与扫描条码站点不一致,请核实
            throw new MtException("HME_EO_JOB_SN_106", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_106", "HME"));
        }
        //校验批次一致
        if (!dto.getLotCode().equals(backMaterialLot.getLot())) {
            //所退条码所属批次与扫描条码批次不一致,请核实
            throw new MtException("HME_EO_JOB_SN_118", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_118", "HME"));
        }
        //查询条码扩展属性
        List<String> materialLotAttrNameList = new ArrayList<>();
        materialLotAttrNameList.add(HmeConstants.ExtendAttr.SO_NUM);
        materialLotAttrNameList.add(HmeConstants.ExtendAttr.SO_LINE_NUM);
        materialLotAttrNameList.add(HmeConstants.ExtendAttr.SAP_ACCOUNT_FLAG);
        materialLotAttrNameList.add(HmeConstants.ExtendAttr.MATERIAL_VERSION);
        Map<String, String> materialLotExtendAttrMap = new HashMap<>(materialLotAttrNameList.size());
        List<String> materialLotIdList = new ArrayList<>(1);
        materialLotIdList.add(backMaterialLot.getMaterialLotId());
        SecurityTokenHelper.close();
        List<MtExtendAttrVO1> materialLotExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId, "mt_material_lot_attr", "MATERIAL_LOT_ID"
                , materialLotIdList, materialLotAttrNameList);
        if (CollectionUtils.isNotEmpty(materialLotExtendAttrVO1List)) {
            materialLotExtendAttrMap = materialLotExtendAttrVO1List.stream().collect(Collectors.toMap(item -> fetchGroupKey(item.getKeyId(),item.getAttrName()), MtExtendAttrVO1::getAttrValue));
        }
        String productionVersion = materialLotExtendAttrMap.getOrDefault(fetchGroupKey(backMaterialLot.getMaterialLotId(),HmeConstants.ExtendAttr.MATERIAL_VERSION),"");
        //校验物料版本一致
        if (StringUtils.isNotBlank(dto.getProductionVersion()) || StringUtils.isNotBlank(productionVersion)) {
            if (!dto.getProductionVersion().equals(productionVersion)) {
                //所退物料的版本与扫描条码的版本不一致,请核实
                throw new MtException("HME_EO_JOB_SN_103", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_103", "HME"));
            }
        }
        //校验输入数量不能大于已投料数量
        BigDecimal releaseQtySum = BigDecimal.ZERO;
        if (HmeConstants.MaterialTypeCode.SN.equals(dto.getMaterialType())) {
            //序列号物料
            releaseQtySum = hmeEoJobMaterialMapper.selectReleaseQtySum(tenantId, dto);
        } else if (HmeConstants.MaterialTypeCode.LOT.equals(dto.getMaterialType()) || HmeConstants.MaterialTypeCode.TIME.equals(dto.getMaterialType())) {
            //批次/时效物料
            if (HmeConstants.VirtualFlag.VIRTUAL_FLAG.equals(dto.getVirtualFlag())) {
                //虚拟件
                releaseQtySum = hmeEoJobSnLotMaterialMapper.selectVirtualReleaseQtySum(tenantId, dto);
            } else {
                //非虚拟件
                releaseQtySum = hmeEoJobSnLotMaterialMapper.selectReleaseQtySum(tenantId, dto);
            }
        }
        if (Objects.isNull(releaseQtySum)) {
            //物料【${1}】的退料数量不可大于已投料数量
            throw new MtException("HME_EO_JOB_SN_069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_069", "HME", dto.getMaterialCode()));
        }
        if (dto.getBackQty().compareTo(releaseQtySum) > 0) {
            //物料【${1}】的退料数量不可大于已投料数量
            throw new MtException("HME_EO_JOB_SN_069", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_069", "HME", dto.getMaterialCode()));
        }
        if (HmeConstants.ConstantValue.YES.equals(dto.getUpgradeFlag())) {
            if (StringUtils.equals(dto.getReworkSourceFlag(), HmeConstants.ConstantValue.YES)) {
                // 为Y 则返修作业校验 当前SN与eo扩展REWORK_MATERIAL_LOT是否一致
                MtEo mtEo = mtEoRepository.selectOne(new MtEo() {{
                    setTenantId(tenantId);
                    setIdentification(dto.getIdentification());
                }});
                List<MtExtendAttrVO> attrVOList = mtEoRepository.eoLimitAttrQuery(tenantId, new MtEoAttrVO2() {{
                    setEoId(mtEo.getEoId());
                    setAttrName("REWORK_MATERIAL_LOT");
                }});
                String reworkMaterialLot = CollectionUtils.isNotEmpty(attrVOList) ? attrVOList.get(0).getAttrValue() : "";
                if (StringCommonUtils.equalsIgnoreBlank(reworkMaterialLot, backMaterialLot.getMaterialLotCode())) {
                    throw new MtException("HME_EO_JOB_SN_193", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_193", "HME", reworkMaterialLot));
                }
            } else {
                if (StringCommonUtils.equalsIgnoreBlank(dto.getIdentification(), backMaterialLot.getMaterialLotCode())) {
                    //当前EO 的 identification 不能与所退条码一致
                    throw new MtException("HME_EO_JOB_SN_147", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_147", "HME"));
                }
            }
        }
        //set返回结果
        resultVO.setBackMaterialLot(backMaterialLot);
        resultVO.setMaterialLotAttrMap(materialLotExtendAttrMap);
        return resultVO;
    }
}
