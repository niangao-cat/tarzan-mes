package com.ruike.hme.infra.repository.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.ruike.hme.domain.entity.HmeEoJobSnLotMaterial;
import com.ruike.hme.domain.entity.HmeVirtualNum;
import com.ruike.hme.domain.repository.HmeEoJobSnLotMaterialRepository;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeVirtualNumRepository;
import com.ruike.hme.domain.vo.HmePrepareMaterialVO;
import com.ruike.hme.infra.constant.HmeConstants;

import com.ruike.hme.infra.mapper.HmeEoJobSnLotMaterialMapper;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;

import com.ruike.hme.domain.vo.HmeEoJobMaterialVO;
import com.ruike.hme.domain.vo.HmeEoJobSnVO2;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import tarzan.iface.domain.entity.MtMaterialBasic;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import com.ruike.hme.domain.entity.HmeEoJobLotMaterial;
import com.ruike.hme.domain.repository.HmeEoJobLotMaterialRepository;
import com.ruike.hme.domain.vo.HmeEoJobLotMaterialVO;
import com.ruike.hme.infra.mapper.HmeEoJobLotMaterialMapper;
import tarzan.method.domain.repository.MtBomComponentRepository;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;
import tarzan.order.domain.vo.MtEoVO19;
import tarzan.order.domain.vo.MtEoVO20;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.order.domain.vo.MtWorkOrderVO7;
import tarzan.order.domain.vo.MtWorkOrderVO8;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;

/**
 * 工序作业平台-投料 资源库实现
 *
 * @author liyuan.lv@hand-china.com 2020-03-21 17:55:01
 */
@Slf4j
@Component
public class HmeEoJobLotMaterialRepositoryImpl extends BaseRepositoryImpl<HmeEoJobLotMaterial> implements HmeEoJobLotMaterialRepository {
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private HmeEoJobLotMaterialMapper hmeEoJobLotMaterialMapper;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingsMapper;
    @Autowired
    private MtBomComponentRepository mtBomComponentRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private HmeEoJobSnLotMaterialRepository hmeEoJobSnLotMaterialRepository;
    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;
    @Autowired
    private HmeEoJobSnLotMaterialMapper hmeEoJobSnLotMaterialMapper;

    @Autowired
    private HmeVirtualNumRepository hmeVirtualNumRepository;

    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    /**
     * 拆分数据
     *
     * @param sqlList  源数据
     * @param splitNum 拆分数量
     * @return 拆分数据
     * @author jiangling.zheng@hand-china.com 2020/7/30 17:01
     */
    public static <T> List<List<T>> splitSqlList(List<T> sqlList, int splitNum) {

        List<List<T>> returnList = new ArrayList<>();
        if (sqlList.size() <= splitNum) {
            returnList.add(sqlList);
        } else {
            int splitCount = sqlList.size() / splitNum;
            int splitRest = sqlList.size() % splitNum;

            for (int i = 0; i < splitCount; i++) {
                returnList.add(sqlList.subList(i * splitNum, (i + 1) * splitNum));
            }

            if (splitRest > 0) {
                returnList.add(sqlList.subList(splitCount * splitNum, sqlList.size()));
            }
        }
        return returnList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobLotMaterialVO initLotMaterial(Long tenantId,
                                                 String bomComponentId,
                                                 String materialId,
                                                 BigDecimal componentQty,
                                                 HmeEoJobSnVO2 dto) {
        log.info("<====== HmeEoJobLotMaterialRepositoryImpl.initLotMaterial tenantId=[{}],bomComponentId=[{}],materialId=[{}]" +
                ",componentQty=[{}],dto=[{}]", tenantId, bomComponentId, materialId, componentQty, dto);
        HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
        hmeEoJobLotMaterial.setTenantId(tenantId);
        hmeEoJobLotMaterial.setWorkcellId(dto.getWorkcellId());
        hmeEoJobLotMaterial.setMaterialId(materialId);

        HmeEoJobLotMaterial existLotMaterial = this.selectOne(hmeEoJobLotMaterial);

        if (Objects.isNull(existLotMaterial)) {
            hmeEoJobLotMaterial.setSnMaterialId(dto.getSnMaterialId());
            // 初始化为未投料
            hmeEoJobLotMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
            hmeEoJobLotMaterial.setReleaseQty(componentQty.multiply(Objects.isNull(dto.getPrepareQty()) ? BigDecimal.ONE : dto.getPrepareQty()));
            hmeEoJobLotMaterial.setBydMaterialId(dto.getBydMaterialId());
            self().insertSelective(hmeEoJobLotMaterial);
        } else {
            hmeEoJobLotMaterial = existLotMaterial;
            //V20200831 modify by penglin.sui for jiao.chen 已存在，更新release_qty
            hmeEoJobLotMaterial.setReleaseQty(componentQty.multiply(Objects.isNull(dto.getPrepareQty()) ? BigDecimal.ONE : dto.getPrepareQty()));
            hmeEoJobLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobLotMaterial);
        }
        HmeEoJobLotMaterialVO jobLotMaterialVO = new HmeEoJobLotMaterialVO();
        BeanUtils.copyProperties(hmeEoJobLotMaterial, jobLotMaterialVO);
        // 物料属性信息
        MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, hmeEoJobLotMaterial.getMaterialId());
        jobLotMaterialVO.setMaterialCode(mtMaterialVO.getMaterialCode());
        jobLotMaterialVO.setMaterialName(mtMaterialVO.getMaterialName());
        jobLotMaterialVO.setPrimaryUomCode(mtMaterialVO.getPrimaryUomCode());
        jobLotMaterialVO.setWkcMatchedFlag(HmeConstants.ConstantValue.NO);
        if(componentQty.compareTo(BigDecimal.ZERO) > 0) {
            jobLotMaterialVO.setWkcMatchedFlag(YES);
        }
        //V20200912 modify by penglin.sui for lu.bai 条码ID不为空才查询
        if(StringUtils.isNotBlank(jobLotMaterialVO.getMaterialLotId())) {
            MtMaterialLot mtMaterialLotPara = new MtMaterialLot();
            mtMaterialLotPara.setTenantId(tenantId);
            mtMaterialLotPara.setMaterialLotId(jobLotMaterialVO.getMaterialLotId());
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(mtMaterialLotPara);
            if (Objects.nonNull(mtMaterialLot)) {
                jobLotMaterialVO.setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
            }
        }
        jobLotMaterialVO.setRemainQty(Objects.isNull(jobLotMaterialVO.getRemainQty()) ? BigDecimal.ZERO : jobLotMaterialVO.getRemainQty());
        jobLotMaterialVO.setReleaseQty(BigDecimal.ZERO);
        jobLotMaterialVO.setComponentQty(Objects.isNull(componentQty) ? 0D : componentQty.doubleValue());

        //获取组件版本
        MtExtendSettings lineAttribute7Attr = new MtExtendSettings();
        lineAttribute7Attr.setAttrName("lineAttribute7");
        List<MtExtendAttrVO> mtBomExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentId,
                Collections.singletonList(lineAttribute7Attr));
        if (CollectionUtils.isNotEmpty(mtBomExtendAttrVOList)) {
            if(StringUtils.isNotBlank(mtBomExtendAttrVOList.get(0).getAttrValue())) {
                jobLotMaterialVO.setBomComponentVersion(mtBomExtendAttrVOList.get(0).getAttrValue());
            }
        }

        return jobLotMaterialVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobLotMaterialVO> initLotMaterialList(Long tenantId,
                                                           String materialId,
                                                           BigDecimal componentQty,
                                                           HmeEoJobSnVO2 dto) {
        log.info("<====== HmeEoJobLotMaterialRepositoryImpl.initLotMaterialList tenantId=[{}],materialId=[{}]" +
                ",componentQty=[{}],dto=[{}]", tenantId, materialId, componentQty, dto);

        //获取用户信息
        Long userId = -1L;
        if(!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())){
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        List<HmeEoJobLotMaterialVO> jobLotMaterialVOList = new ArrayList<HmeEoJobLotMaterialVO>();
        HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
        hmeEoJobLotMaterial.setTenantId(tenantId);
        hmeEoJobLotMaterial.setWorkcellId(dto.getWorkcellId());
        hmeEoJobLotMaterial.setMaterialId(materialId);
        long startDate = System.currentTimeMillis();
        List<HmeEoJobLotMaterial> existLotMaterialList = this.select(hmeEoJobLotMaterial);
        log.info("=================================>initLotMaterialList-existLotMaterialList总耗时："+(System.currentTimeMillis() - startDate)+ "ms");
        BigDecimal releaseQty = componentQty.multiply(Objects.isNull(dto.getPrepareQty()) ? BigDecimal.ONE : dto.getPrepareQty());
        if(CollectionUtils.isNotEmpty(existLotMaterialList)) {
            for (HmeEoJobLotMaterial hmeEoJobLotMaterial2 : existLotMaterialList
            ) {
                //V20200831 modify by penglin.sui for jiao.chen 已存在，更新release_qty
                hmeEoJobLotMaterial2.setReleaseQty(releaseQty);
                HmeEoJobLotMaterialVO jobLotMaterialVO = new HmeEoJobLotMaterialVO();
                BeanUtils.copyProperties(hmeEoJobLotMaterial2, jobLotMaterialVO);
                jobLotMaterialVOList.add(jobLotMaterialVO);
            }
            //V20201023 modify by penglin.sui 批量更新
            if (CollectionUtils.isNotEmpty(jobLotMaterialVOList)) {
                List<List<HmeEoJobLotMaterialVO>> splitSqlList = splitSqlList(jobLotMaterialVOList, 200);
                startDate = System.currentTimeMillis();
                for (List<HmeEoJobLotMaterialVO> domains : splitSqlList) {
                    hmeEoJobLotMaterialMapper.batchUpdateLotMaterial(userId,domains);
                }
                log.info("=================================>initLotMaterialList-批量更新总耗时："+(System.currentTimeMillis() - startDate)+ "ms");
            }
        }else {
            hmeEoJobLotMaterial.setSnMaterialId(dto.getSnMaterialId());
            // 初始化为未投料
            hmeEoJobLotMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
            hmeEoJobLotMaterial.setReleaseQty(releaseQty);
            hmeEoJobLotMaterial.setBydMaterialId(dto.getBydMaterialId());
            startDate = System.currentTimeMillis();
            self().insertSelective(hmeEoJobLotMaterial);
            log.info("=================================>initLotMaterialList-初始化为未投料总耗时："+(System.currentTimeMillis() - startDate)+ "ms");
            HmeEoJobLotMaterialVO jobLotMaterialVO = new HmeEoJobLotMaterialVO();
            BeanUtils.copyProperties(hmeEoJobLotMaterial, jobLotMaterialVO);
            jobLotMaterialVOList.add(jobLotMaterialVO);
        }

        // 物料属性信息
        startDate = System.currentTimeMillis();
        MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, materialId);
        log.info("=================================>initLotMaterialList-物料属性信息总耗时："+(System.currentTimeMillis() - startDate)+ "ms");
        //V20201023 modify by penglin.sui 批量查询物料批
        List<String> materialLotIdList = jobLotMaterialVOList.stream().map(HmeEoJobLotMaterialVO::getMaterialLotId).distinct().collect(Collectors.toList());
        List<MtMaterialLot> mtMaterialLotList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(materialLotIdList)){
            startDate = System.currentTimeMillis();
            mtMaterialLotList = hmeEoJobLotMaterialMapper.queryMaterialLot(tenantId,materialLotIdList);
            log.info("=================================>initLotMaterialList-queryMaterialLot总耗时："+(System.currentTimeMillis() - startDate)+ "ms");
        }

        List<MtWorkOrderVO8> woComponentList = new ArrayList<>();
        List<MtEoVO20> mtEoVO20List = new ArrayList<>();
        List<String> bomComponentIdList = new ArrayList<>();
        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            if (StringUtils.isNotBlank(dto.getOperationId())) {
                MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
                mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
                mtWorkOrderVO7.setOperationId(dto.getOperationId());
                mtWorkOrderVO7.setRouterStepId(dto.getEoStepId());
                startDate = System.currentTimeMillis();
                woComponentList = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
                log.info("=================================>initLotMaterialList-woComponentQtyQuery总耗时："+(System.currentTimeMillis() - startDate)+ "ms");
                if(CollectionUtils.isNotEmpty(woComponentList)){
                    bomComponentIdList = woComponentList.stream().map(MtWorkOrderVO8::getBomComponentId).collect(Collectors.toList());
                }
            }
        }else {
            // 非预装平台需匹配wkc组件清单物料
            if (StringUtils.isNotBlank(dto.getOperationId())) {
                MtEoVO19 mtEoVO19 = new MtEoVO19();
                mtEoVO19.setEoId(dto.getEoId());
                mtEoVO19.setOperationId(dto.getOperationId());
                mtEoVO19.setRouterStepId(dto.getEoStepId());
                // 获取当前wkc工艺对应的组件清单
                startDate = System.currentTimeMillis();
                mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
                log.info("=================================>initLotMaterialList-eoComponentQtyQuery总耗时："+(System.currentTimeMillis() - startDate)+ "ms");
                if(CollectionUtils.isNotEmpty(mtEoVO20List)){
                    bomComponentIdList = mtEoVO20List.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
                }
            }
        }
        List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(bomComponentIdList)){
            List<String> attrNameList = new ArrayList<>();
            attrNameList.add("lineAttribute7");
            startDate = System.currentTimeMillis();
            mtExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_bom_component_attr","BOM_COMPONENT_ID",bomComponentIdList,attrNameList);
            log.info("=================================>initLotMaterialList-queryExtendAttr总耗时："+(System.currentTimeMillis() - startDate)+ "ms");
        }
        for (HmeEoJobLotMaterialVO jobLotMaterialVO:jobLotMaterialVOList
             ) {
            jobLotMaterialVO.setMaterialCode(mtMaterialVO.getMaterialCode());
            jobLotMaterialVO.setMaterialName(mtMaterialVO.getMaterialName());
            jobLotMaterialVO.setPrimaryUomCode(mtMaterialVO.getPrimaryUomCode());
            //V20200912 modify by penglin.sui for lu.bai 条码ID不为空才查询
            if(StringUtils.isNotBlank(jobLotMaterialVO.getMaterialLotId())) {
                MtMaterialLot mtMaterialLotPara = new MtMaterialLot();
                mtMaterialLotPara.setTenantId(tenantId);
                mtMaterialLotPara.setMaterialLotId(jobLotMaterialVO.getMaterialLotId());
                List<MtMaterialLot> mtMaterialLotList2 = mtMaterialLotList.stream().filter(item -> item.getMaterialLotId().equals(jobLotMaterialVO.getMaterialLotId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(mtMaterialLotList2)){
                    MtMaterialLot mtMaterialLot = mtMaterialLotList2.get(0);
                    jobLotMaterialVO.setMaterialLotCode(mtMaterialLotList2.get(0).getMaterialLotCode());
                }
            }
            jobLotMaterialVO.setWkcMatchedFlag(HmeConstants.ConstantValue.NO);
            jobLotMaterialVO.setReleaseQtyChangeFlag(YES);
            String bomComponentId = "";
            if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                if (CollectionUtils.isNotEmpty(woComponentList)) {
                    Optional<MtWorkOrderVO8> componentOptional = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(jobLotMaterialVO.getMaterialId())).findFirst();
                    if (componentOptional.isPresent()) {
                        //数量大于0时标识为N modify by yuchao.wang for lu.bai at 2020.11.10
                        if (!YES.equals(dto.getReworkFlag()) && componentOptional.get().getPerQty() > 0) {
                            jobLotMaterialVO.setReleaseQtyChangeFlag(HmeConstants.ConstantValue.NO);
                        }
                        if (BigDecimal.valueOf(componentOptional.get().getPerQty()).compareTo(BigDecimal.ZERO) > 0) {
                            jobLotMaterialVO.setWkcMatchedFlag(YES);
                        }
                        bomComponentId = componentOptional.get().getBomComponentId();
                    }
                }
            } else {
                // 非预装平台需匹配wkc组件清单物料
                if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
                    Optional<MtEoVO20> componentOptional = mtEoVO20List.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(jobLotMaterialVO.getMaterialId())).findFirst();
                    if (componentOptional.isPresent()) {
                        //数量大于0时标识为N modify by yuchao.wang for lu.bai at 2020.11.10
                        if (!YES.equals(dto.getReworkFlag()) && componentOptional.get().getPreQty() > 0) {
                            jobLotMaterialVO.setReleaseQtyChangeFlag(HmeConstants.ConstantValue.NO);
                        }
                        if (BigDecimal.valueOf(componentOptional.get().getPreQty()).compareTo(BigDecimal.ZERO) > 0) {
                            jobLotMaterialVO.setWkcMatchedFlag(YES);
                        }
                        bomComponentId = componentOptional.get().getBomComponentId();
                    }
                }
            }

            //获取组件版本
            if(StringUtils.isNotBlank(bomComponentId) && CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                String bomComponentId2 = bomComponentId;
                List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(bomComponentId2)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                    if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                        jobLotMaterialVO.setBomComponentVersion(mtExtendAttrVO1List2.get(0).getAttrValue());
                    }
                }
            }
        }

        return jobLotMaterialVOList;
    }

    /**
     *
     * @Description 初始化批次物料投料数据
     *
     * @author yuchao.wang
     * @date 2021/1/2 23:35
     * @param tenantId     租户ID
     * @param materialId   物料ID
     * @param componentQty 组件数量
     * @param dto          数据
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initLotMaterialWithoutQuery(Long tenantId, String materialId, BigDecimal componentQty, HmeEoJobSnVO2 dto) {
        log.info("<====== HmeEoJobLotMaterialRepositoryImpl.initLotMaterialWithoutQuery tenantId=[{}],materialId=[{}]" +
                ",componentQty=[{}],dto=[{}]", tenantId, materialId, componentQty, dto);

        //获取用户信息
        Long userId = -1L;
        if (!Objects.isNull(DetailsHelper.getUserDetails())
                && !Objects.isNull(DetailsHelper.getUserDetails().getUserId())) {
            userId = DetailsHelper.getUserDetails().getUserId();
        }

        HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
        hmeEoJobLotMaterial.setTenantId(tenantId);
        hmeEoJobLotMaterial.setWorkcellId(dto.getWorkcellId());
        hmeEoJobLotMaterial.setMaterialId(materialId);
        List<HmeEoJobLotMaterial> existLotMaterialList = this.select(hmeEoJobLotMaterial);

        BigDecimal releaseQty = componentQty.multiply(Objects.isNull(dto.getPrepareQty()) ? BigDecimal.ONE : dto.getPrepareQty());

        if (CollectionUtils.isNotEmpty(existLotMaterialList)) {
            List<HmeEoJobLotMaterialVO> insertList = new ArrayList<HmeEoJobLotMaterialVO>();
            for (HmeEoJobLotMaterial hmeEoJobLotMaterial2 : existLotMaterialList) {
                //V20200831 modify by penglin.sui for jiao.chen 已存在，更新release_qty
                hmeEoJobLotMaterial2.setReleaseQty(releaseQty);
                HmeEoJobLotMaterialVO jobLotMaterialVO = new HmeEoJobLotMaterialVO();
                BeanUtils.copyProperties(hmeEoJobLotMaterial2, jobLotMaterialVO);
                insertList.add(jobLotMaterialVO);
            }

            //V20201023 modify by penglin.sui 批量更新
            if (CollectionUtils.isNotEmpty(insertList)) {
                List<List<HmeEoJobLotMaterialVO>> splitSqlList = splitSqlList(insertList, 200);
                for (List<HmeEoJobLotMaterialVO> domains : splitSqlList) {
                    hmeEoJobLotMaterialMapper.batchUpdateLotMaterial(userId, domains);
                }
            }
        } else {
            hmeEoJobLotMaterial.setSnMaterialId(dto.getSnMaterialId());
            // 初始化为未投料
            hmeEoJobLotMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
            hmeEoJobLotMaterial.setReleaseQty(releaseQty);
            hmeEoJobLotMaterial.setBydMaterialId(dto.getBydMaterialId());
            self().insertSelective(hmeEoJobLotMaterial);
        }
    }

    @Override
    public List<HmeEoJobLotMaterialVO> matchedJobLotMaterialQuery(Long tenantId,
                                                                  HmeEoJobMaterialVO dto,
                                                                  List<HmeEoJobLotMaterial> lotMaterialList) {
        log.info("<====== HmeEoJobLotMaterialRepositoryImpl.matchedJobLotMaterialQuery tenantId=[{}],dto=[{}]" +
                ",lotMaterialList=[{}]", tenantId, dto, lotMaterialList);
        List<HmeEoJobLotMaterialVO> resultVOList = new ArrayList<>();
        List<HmeEoJobLotMaterial> matchedMaterialList = new ArrayList<>(16);
        List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList  = new ArrayList<>(16);
        Map<String, Long> sortedMap = new HashMap<String, Long>();
        if (CollectionUtils.isNotEmpty(lotMaterialList)) {
            hmeEoJobLotMaterialList = lotMaterialList;
        } else {
            HmeEoJobLotMaterial jobLotMaterialParam = new HmeEoJobLotMaterial();
            jobLotMaterialParam.setTenantId(tenantId);
            jobLotMaterialParam.setWorkcellId(dto.getWorkcellId());
            if (YES.equals(dto.getIsWorkcellQuery())) {
                hmeEoJobLotMaterialList = hmeEoJobLotMaterialMapper.queryEoJobLotMaterialOfNotNullMaterialLot(tenantId, jobLotMaterialParam);
            } else {
                hmeEoJobLotMaterialList = hmeEoJobLotMaterialMapper.queryEoJobLotMaterial(tenantId, jobLotMaterialParam);
            }
        }

        //预先查出组件清单 modify by yuchao.wang at 2020.9.23
        List<MtWorkOrderVO8> woComponentList = new ArrayList<MtWorkOrderVO8>();
        List<MtEoVO20> mtEoVO20List = new ArrayList<MtEoVO20>();
        List<String> bomComponentIdList = new ArrayList<>();
        if (!YES.equals(dto.getIsWorkcellQuery())) {
            if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
                mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
                mtWorkOrderVO7.setOperationId(dto.getOperationId());
                woComponentList = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
                if (CollectionUtils.isNotEmpty(woComponentList)) {
                    bomComponentIdList = woComponentList.stream().map(MtWorkOrderVO8::getBomComponentId).collect(Collectors.toList());
                }
            } else {
                if (StringUtils.isNotBlank(dto.getOperationId())) {
                    MtEoVO19 mtEoVO19 = new MtEoVO19();
                    mtEoVO19.setEoId(dto.getEoId());
                    mtEoVO19.setOperationId(dto.getOperationId());
                    mtEoVO19.setRouterStepId(dto.getEoStepId());
                    mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
                    if(CollectionUtils.isNotEmpty(mtEoVO20List)){
                        bomComponentIdList = mtEoVO20List.stream().map(MtEoVO20::getBomComponentId).collect(Collectors.toList());
                    }
                }
            }
        }

        HmeEoJobMaterialVO hmeEoJobMaterialVO = new HmeEoJobMaterialVO();
        hmeEoJobMaterialVO.setJobType(dto.getJobType());
        hmeEoJobMaterialVO.setEoId(dto.getEoId());
        hmeEoJobMaterialVO.setOperationId(dto.getOperationId());
        hmeEoJobMaterialVO.setWorkOrderId(dto.getWorkOrderId());

        if (CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList)) {
            //预装作业平台要筛选物料 add by yuchao.wang for lu.bai at 2020.9.17
            if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType()) && StringUtils.isNotBlank(dto.getMaterialId())) {
                MtMaterial material = mtMaterialRepository.materialPropertyGet(tenantId, dto.getMaterialId());
                if (Objects.nonNull(material)) {
                    List<HmePrepareMaterialVO> prepareMaterialVOList = hmeEoJobSnMapper.prepareEoJobLotMaterialQuery(tenantId,
                            dto.getSiteId(), dto.getWorkOrderId(), material.getMaterialCode());
                    if (CollectionUtils.isNotEmpty(prepareMaterialVOList)) {
                        List<String> materialIdList = new ArrayList<>();
                        prepareMaterialVOList.forEach(item -> {
                            sortedMap.put(item.getMaterialId(), item.getLineNumber());
                            materialIdList.add(item.getMaterialId());
                        });

                        //V20200930 modify by pengli.sui for lu.bai 替代料物料也要显示
                        List<HmeEoJobLotMaterial> hmeEoJobLotMaterialList2 = hmeEoJobLotMaterialList.stream().filter(item -> !materialIdList.contains(item.getMaterialId())).collect(Collectors.toList());
                        if(CollectionUtils.isNotEmpty(hmeEoJobLotMaterialList2)){
                            for (HmeEoJobLotMaterial hmeEoJobLotMaterial:hmeEoJobLotMaterialList2
                            ) {
                                if(hmeEoJobSnLotMaterialRepository.checkSubstituteRelExists(tenantId,material.getMaterialId(),hmeEoJobMaterialVO,woComponentList,mtEoVO20List)){
                                    materialIdList.add(hmeEoJobLotMaterial.getMaterialId());
                                }
                            }
                        }

                        matchedMaterialList = hmeEoJobLotMaterialList.stream().filter(item -> materialIdList.contains(item.getMaterialId())).collect(Collectors.toList());
                    }
                }
            } else {
                matchedMaterialList = hmeEoJobLotMaterialList;

                if(CollectionUtils.isNotEmpty(mtEoVO20List)){
                    for (MtEoVO20 mtEoVO20:mtEoVO20List
                    ) {
                        sortedMap.put(mtEoVO20.getMaterialId(), mtEoVO20.getSequence());
                    }
                }
            }
        }

        if (CollectionUtils.isNotEmpty(matchedMaterialList)) {
            List<String> itemGroupList = new ArrayList<>();
            List<LovValueDTO> poTypeLov = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP",tenantId);
            if (CollectionUtils.isNotEmpty(poTypeLov)) {
                itemGroupList = poTypeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
            }
            String isInComponentFlag = HmeConstants.ConstantValue.NO;
            //批量查询remainQty、releaseQty add by yuchao.wang for lu.bai at 2020.9.14
            List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterials = hmeEoJobSnLotMaterialRepository.selectByCondition(Condition.builder(HmeEoJobSnLotMaterial.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobSnLotMaterial.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobSnLotMaterial.FIELD_JOB_ID, dto.getJobId())).build());
            Map<String, BigDecimal> releaseQtyMap = new HashMap<>();
            if(CollectionUtils.isNotEmpty(hmeEoJobSnLotMaterials)){

                //V20201003 modify by penglin.sui for lu.bai 排除退料的数据
                List<HmeEoJobSnLotMaterial> hmeEoJobSnLotMaterials2 = hmeEoJobSnLotMaterials.stream().filter(item -> item.getReleaseQty().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());

                hmeEoJobSnLotMaterials2.forEach(item -> {
                    String key = item.getMaterialId() + "-" + StringUtils.trimToEmpty(item.getProductionVersion());
                    if(releaseQtyMap.containsKey(key)){
                        releaseQtyMap.put(key, releaseQtyMap.get(key).add(item.getReleaseQty()));
                    } else {
                        releaseQtyMap.put(key, item.getReleaseQty());
                    }
                });
            }

            //V20201023 modify by penglin.sui 批量获取物料属性信息
            List<String> materialIdList = matchedMaterialList.stream().map(HmeEoJobLotMaterial::getMaterialId).distinct().collect(Collectors.toList());
            List<MtMaterialVO> mtMaterialVOList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(materialIdList)) {
                mtMaterialVOList = hmeEoJobSnLotMaterialMapper.selectMaterialProperty(tenantId, materialIdList);
            }

            //V20201023 modify by penglin.sui 批量获取物料批信息
            List<String> materialLotIdList = matchedMaterialList.stream().map(HmeEoJobLotMaterial::getMaterialLotId).distinct().collect(Collectors.toList());
            List<MtMaterialLot> mtMaterialLotList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(materialLotIdList)) {
                mtMaterialLotList = hmeEoJobLotMaterialMapper.queryMaterialLot(tenantId, materialLotIdList);
            }

            //批量查询是否为COS类型条码 add by penglin.sui for jiao.chen at 2020.10.23
            List<MtMaterialBasic> mtMaterialBasicList = hmeEoJobLotMaterialMapper.queryCosMaterialItemGroups(tenantId, materialIdList, dto.getSiteId());

            List<MtExtendAttrVO1> mtExtendAttrVO1List = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(bomComponentIdList)){
                List<String> attrNameList = new ArrayList<>();
                attrNameList.add("lineAttribute7");
                mtExtendAttrVO1List = hmeEoJobSnMapper.queryExtendAttr(tenantId,"mt_bom_component_attr","BOM_COMPONENT_ID",bomComponentIdList,attrNameList);
            }

            // 获取其他返回前端数据
            for (HmeEoJobLotMaterial material : matchedMaterialList) {

                //工位扫描时不校验替代料 modify by yuchao.wang for lu.bai at 2020.12.15
                if (!YES.equals(dto.getIsWorkcellQuery())) {
                    //投料数量为0时，校验是否替代料，替代料才显示
                    if (Objects.isNull(material.getReleaseQty()) || material.getReleaseQty().compareTo(BigDecimal.ZERO) == 0) {
                        if (!hmeEoJobSnLotMaterialRepository.checkSubstituteRelExists(tenantId, material.getMaterialId(), hmeEoJobMaterialVO, woComponentList, mtEoVO20List)) {
                            continue;
                        }
                    }
                }

                isInComponentFlag = HmeConstants.ConstantValue.NO;
                HmeEoJobLotMaterialVO jobLotMaterialVO = new HmeEoJobLotMaterialVO();
                BeanUtils.copyProperties(material, jobLotMaterialVO);
                jobLotMaterialVO.setComponentQty(0.0);
                jobLotMaterialVO.setRemainQty(BigDecimal.ZERO);
                jobLotMaterialVO.setCosMaterialLotFlag(HmeConstants.ConstantValue.NO);

                // 物料属性信息
                if(CollectionUtils.isNotEmpty(mtMaterialVOList)){
                    List<MtMaterialVO> mtMaterialVOList2 = mtMaterialVOList.stream().filter(item -> material.getMaterialId().equals(item.getMaterialId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(mtMaterialVOList2)) {
                        jobLotMaterialVO.setMaterialCode(mtMaterialVOList2.get(0).getMaterialCode());
                        jobLotMaterialVO.setMaterialName(mtMaterialVOList2.get(0).getMaterialName());
                        jobLotMaterialVO.setPrimaryUomCode(mtMaterialVOList2.get(0).getPrimaryUomCode());
                    }
                }

                if (StringUtils.isNotBlank(material.getMaterialLotId()) && CollectionUtils.isNotEmpty(mtMaterialLotList)) {
                    List<MtMaterialLot> mtMaterialLotList2 = mtMaterialLotList.stream().filter(item -> material.getMaterialLotId().equals(item.getMaterialLotId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(mtMaterialLotList2)) {
                        jobLotMaterialVO.setMaterialLotCode(mtMaterialLotList2.get(0).getMaterialLotCode());
                        jobLotMaterialVO.setRemainQty(Objects.isNull(mtMaterialLotList2.get(0).getPrimaryUomQty()) ? BigDecimal.ZERO : new BigDecimal(mtMaterialLotList2.get(0).getPrimaryUomQty()));
                    }
                }
                jobLotMaterialVO.setReleaseQtyChangeFlag(YES);
                jobLotMaterialVO.setWkcMatchedFlag(HmeConstants.ConstantValue.NO);

                //查询是否为COS类型条码 add by yuchao.wang for jiao.chen at 2020.9.30
                if(CollectionUtils.isNotEmpty(mtMaterialBasicList)){
                    List<MtMaterialBasic> mtMaterialBasicList2 = mtMaterialBasicList.stream().filter(item -> material.getMaterialId().equals(item.getMaterialId())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(mtMaterialBasicList2)) {
                        String itemGroup = mtMaterialBasicList2.get(0).getItemGroup();
                        if (CollectionUtils.isNotEmpty(itemGroupList) && itemGroupList.contains(itemGroup)) {
                            jobLotMaterialVO.setCosMaterialLotFlag(YES);
                        }
                    }
                }

                String bomComponentId = "";
                BigDecimal sumQty = BigDecimal.ZERO;
                if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                    if (CollectionUtils.isNotEmpty(woComponentList)) {
                        //预装根据WO获取组件需求数量 add by yuchao.wang for lu.bai at 2020.9.14
                        List<MtWorkOrderVO8> componentQtyList = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(jobLotMaterialVO.getMaterialId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(componentQtyList)) {
                            //数量大于0时标识为N modify by yuchao.wang for lu.bai at 2020.11.10
                            sumQty = BigDecimal.valueOf(componentQtyList.stream().mapToDouble(MtWorkOrderVO8::getPerQty).sum());
                            if (!YES.equals(dto.getReworkFlag()) && sumQty.compareTo(BigDecimal.ZERO) > 0) {
                                jobLotMaterialVO.setReleaseQtyChangeFlag(HmeConstants.ConstantValue.NO);
                            }
                            jobLotMaterialVO.setComponentQty((sumQty.multiply(dto.getPrepareQty())).doubleValue());
                            if(sumQty.compareTo(BigDecimal.ZERO) > 0) {
                                jobLotMaterialVO.setWkcMatchedFlag(YES);
                            }
                            isInComponentFlag = YES;
                            bomComponentId = componentQtyList.get(0).getBomComponentId();
                        }
                    }
                } else {
                    // 非预装平台需匹配wkc组件清单物料
                    if (StringUtils.isNotBlank(dto.getOperationId())) {
                        if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
                            //非预装根据EO获取组件需求数量 add by yuchao.wang for lu.bai at 2020.9.14
                            List<MtEoVO20> componentQtyList = mtEoVO20List.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(jobLotMaterialVO.getMaterialId())).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(componentQtyList)) {
                                //数量大于0时标识为N modify by yuchao.wang for lu.bai at 2020.11.10
                                sumQty = BigDecimal.valueOf(componentQtyList.stream().mapToDouble(MtEoVO20::getComponentQty).sum());
                                if (!YES.equals(dto.getReworkFlag()) && sumQty.compareTo(BigDecimal.ZERO) > 0) {
                                    jobLotMaterialVO.setReleaseQtyChangeFlag(HmeConstants.ConstantValue.NO);
                                }
                                jobLotMaterialVO.setComponentQty(sumQty.doubleValue());
                                if(sumQty.compareTo(BigDecimal.ZERO) > 0) {
                                    jobLotMaterialVO.setWkcMatchedFlag(YES);
                                }
                                isInComponentFlag = YES;
                                bomComponentId = componentQtyList.get(0).getBomComponentId();
                            }
                        }
                    }
                }
                if (StringUtils.isNotBlank(bomComponentId)){
                    if (hmeEoJobSnRepository.checkVirtualComponent(tenantId, bomComponentId) &&
                            !HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
                        continue;
                    }
                }
                //如果是工位查询不去筛选显示所有物料
                if (!YES.equals(dto.getIsWorkcellQuery())) {

                    //V20200929 modify by penglin.sui for lu.bai 不在组件清单中且不是替代料的不显示
                    if (HmeConstants.ConstantValue.NO.equals(isInComponentFlag) && !hmeEoJobSnLotMaterialRepository.checkSubstituteRelExists(tenantId, material.getMaterialId(), dto, woComponentList, mtEoVO20List)) {
                        continue;
                    }
                }
                String releaseQtyKey = jobLotMaterialVO.getMaterialId() + "-" + StringUtils.trimToEmpty(jobLotMaterialVO.getProductionVersion());
                jobLotMaterialVO.setReleaseQty(releaseQtyMap.getOrDefault(releaseQtyKey, BigDecimal.ZERO));
                jobLotMaterialVO.setLineNumber(sortedMap.getOrDefault(jobLotMaterialVO.getMaterialId(), 99999L));

                //获取组件版本
                if(StringUtils.isNotBlank(bomComponentId) && CollectionUtils.isNotEmpty(mtExtendAttrVO1List)) {
                    String bomComponentId2 = bomComponentId;
                    List<MtExtendAttrVO1> mtExtendAttrVO1List2 = mtExtendAttrVO1List.stream().filter(item -> item.getKeyId().equals(bomComponentId2)).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(mtExtendAttrVO1List2)) {
                        if (StringUtils.isNotBlank(mtExtendAttrVO1List2.get(0).getAttrValue())) {
                            jobLotMaterialVO.setBomComponentVersion(mtExtendAttrVO1List2.get(0).getAttrValue());
                        }
                    }
                }
                resultVOList.add(jobLotMaterialVO);
            }

            //按照LineNumber升序排序 modify by yuchao.wang for lu.bai at 2020.9.25
            if (CollectionUtils.isNotEmpty(resultVOList)) {
                resultVOList = resultVOList.stream().sorted(
                        Comparator.comparing(HmeEoJobLotMaterialVO::getLineNumber)).collect(Collectors.toList());
            }
        } else {
            return new ArrayList<HmeEoJobLotMaterialVO>();
        }
        return CollectionUtils.isNotEmpty(resultVOList) ? resultVOList : new ArrayList<HmeEoJobLotMaterialVO>();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobLotMaterialVO> releaseScan(Long tenantId, HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobLotMaterialRepositoryImpl.releaseScan tenantId=[{}],dto=[{}]", tenantId, dto);
        MtMaterialLotVO3 materialLotParam = new MtMaterialLotVO3();
        materialLotParam.setMaterialLotCode(dto.getMaterialLotCode());
        List<String> materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotParam);
        if (CollectionUtils.isNotEmpty(materialLotIds)) {
            dto.setMaterialLotId(materialLotIds.get(0));
        } else {
            // 当前条码无效, 请确认
            throw new MtException("HME_EO_JOB_SN_050", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_050", "HME"));
        }

        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, dto.getMaterialLotId());

        //V20201016 modify by penglin.sui for lu.bai 新增物料类型校验
        String materialType = hmeEoJobSnRepository.getMaterialType(tenantId,dto.getSiteId(),mtMaterialLot.getMaterialId());
        if(!HmeConstants.MaterialTypeCode.LOT.equals(materialType)){
            //物料生产类型【${1}】与当前扫描框的物料类型不符,请检查!
            throw new MtException("HME_EO_JOB_SN_116", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_116", "HME", materialType));
        }

        //V20201008 modify by penglin.sui for lu.bai 条码不能绑定多个工位
        String workcellCode = hmeEoJobLotMaterialMapper.queryHaveBindWorkcell(tenantId,dto.getMaterialLotId(),dto.getWorkcellId());

        if(StringUtils.isNotBlank(workcellCode)){
            // 当前条码已与工位【${1}】绑定
            throw new MtException("HME_EO_JOB_SN_110", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_110", "HME",workcellCode ));
        }

        //V20200829 modify by penglin.sui for jiao.chen 判断条码是否绑定当前工位
        int count = hmeEoJobLotMaterialMapper.selectCountByCondition(Condition.builder(HmeEoJobLotMaterial.class)
                .andWhere(Sqls.custom()
                        .andEqualTo(HmeEoJobLotMaterial.FIELD_TENANT_ID, tenantId)
                        .andEqualTo(HmeEoJobLotMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                        .andEqualTo(HmeEoJobLotMaterial.FIELD_MATERIAL_ID, mtMaterialLot.getMaterialId())
                        .andEqualTo(HmeEoJobLotMaterial.FIELD_MATERIAL_LOT_ID, dto.getMaterialLotId()))
                .build());
        if(count > 0){
            List<HmeEoJobLotMaterialVO> hmeEoJobLotMaterialVOList = new ArrayList<HmeEoJobLotMaterialVO>();
            hmeEoJobLotMaterialVOList.add(new HmeEoJobLotMaterialVO(){{
                setMaterialLotId(mtMaterialLot.getMaterialLotId());
                setMaterialLotCode(mtMaterialLot.getMaterialLotCode());
                setDeleteFlag(YES);
                setWorkcellId(dto.getWorkcellId());
                setMaterialId(mtMaterialLot.getMaterialId());
            }});
            return hmeEoJobLotMaterialVOList;
        }
        List<MtWorkOrderVO8> woComponentList = new ArrayList<MtWorkOrderVO8>();
        List<MtEoVO20> mtEoVO20List = new ArrayList<MtEoVO20>();
        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            if (StringUtils.isNotBlank(dto.getOperationId())) {
                MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
                mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
                mtWorkOrderVO7.setOperationId(dto.getOperationId());
                woComponentList = mtWorkOrderRepository.woComponentQtyQuery(tenantId, mtWorkOrderVO7);
//                if (CollectionUtils.isNotEmpty(woComponentList)) {
//                    Optional<MtWorkOrderVO8> componentOptional = woComponentList.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(mtMaterialLot.getMaterialId())).findFirst();
//                    if (!componentOptional.isPresent()) {
//                        // 条码号【${1}】不为OK状态,请核实所录入条码
//                        throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "HME_EO_JOB_SN_073", "HME"));
//                    }
//                }
            }
        } else {
            // 非预装平台需匹配wkc组件清单物料
            if (StringUtils.isNotBlank(dto.getOperationId())) {
                MtEoVO19 mtEoVO19 = new MtEoVO19();
                mtEoVO19.setEoId(dto.getEoId());
                mtEoVO19.setOperationId(dto.getOperationId());
                mtEoVO19.setRouterStepId(dto.getEoStepId());
                // 获取当前wkc工艺对应的组件清单
                mtEoVO20List = mtEoRepository.eoComponentQtyQuery(tenantId, mtEoVO19);
//                if (CollectionUtils.isNotEmpty(mtEoVO20List)) {
//                    Optional<MtEoVO20> componentOptional = mtEoVO20List.stream().filter(mtEoVO20 -> mtEoVO20.getMaterialId().equals(mtMaterialLot.getMaterialId())).findFirst();
//                    if (!componentOptional.isPresent()) {
//                        // 条码号【${1}】不为OK状态,请核实所录入条码
//                        throw new MtException("HME_EO_JOB_SN_073", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                                "HME_EO_JOB_SN_073", "HME"));
//                    }
//                }
            }
        }

        //V20200829 modify by penglin.sui for jiao.chen 条码质量状态为OK
        if(!HmeConstants.ConstantValue.OK.equals(mtMaterialLot.getQualityStatus())){
            // 条码号【${1}】不为OK状态,请核实所录入条码
            throw new MtException("HME_CHIP_TRANSFER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_003", "HME", mtMaterialLot.getMaterialLotCode()));
        }

        HmeEoJobLotMaterial param = new HmeEoJobLotMaterial();
        param.setTenantId(tenantId);
        param.setWorkcellId(dto.getWorkcellId());
        param.setMaterialId(mtMaterialLot.getMaterialId());
        List<HmeEoJobLotMaterial> scanMaterialList = hmeEoJobLotMaterialMapper.select(param);

        if (CollectionUtils.isEmpty(scanMaterialList)) {
            // 当前条码无法匹配投料物料,请确认
//            throw new MtException("HME_EO_JOB_SN_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
//                    "HME_EO_JOB_SN_015", "HME"));
            //V20200925 modify by penglin.sui for lu.bai 判断物料是否满足替代关系
            if(!hmeEoJobSnLotMaterialRepository.checkSubstituteRelExists(tenantId,mtMaterialLot.getMaterialId(),dto,woComponentList,mtEoVO20List)
                    && !StringUtils.equals(dto.getPfType(), HmeConstants.PfType.REWORK)){
                //所扫描条码物料与装配清单不匹配且非可替代物料,请检查
                throw new MtException("HME_EO_JOB_SN_078", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_078", "HME"));
            }
        }

        // 校验条码是否有效
        if (!YES.equals(mtMaterialLot.getEnableFlag())) {
            //条码不为有效条码.${1}
            throw new MtException("MT_MATERIAL_LOT_0015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_MATERIAL_LOT_0015", "MATERIAL_LOT", "【API:materialLotEnableValidate】"));
        }

        //如果是预装直接从前台拿woId modify by yuchao.wang for fang.pang at 2020.9.14
        //如果是预装查WO组件，否则查EO组件 modify by yuchao.wang for lu.bai at 2020.9.16
        Map<String, Double> componentQtyMap = new HashMap<>();
        Map<String, String> bomComponentIdMap = new HashMap<>();
        if(!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            MtEo mtEo = mtEoRepository.eoPropertyGet(tenantId, dto.getEoId());
            if (Objects.isNull(mtEo)) {
                // 不存在此SN号对应的EO
                throw new MtException("HME_EO_JOB_SN_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_005", "HME"));
            }
            dto.setWorkOrderId(mtEo.getWorkOrderId());

            // 查询匹配当前工位下wkc工艺组件清单匹配的批次物料
            MtEoVO19 mtEoVO19 = new MtEoVO19();
            mtEoVO19.setEoId(dto.getEoId());
            mtEoVO19.setOperationId(dto.getOperationId());
            mtEoVO19.setRouterStepId(dto.getEoStepId());
            if(CollectionUtils.isNotEmpty(mtEoVO20List)){
                mtEoVO20List.forEach(item -> {
                    componentQtyMap.put(item.getMaterialId(), item.getComponentQty());
                    bomComponentIdMap.put(item.getMaterialId(), item.getBomComponentId());
                });
            }

            //首序作业平台 COS芯片类型的条码要校验虚拟号工单 add by yuchao.wang for jiao.chen at 2020.9.30
            if (HmeConstants.JobType.SINGLE_PROCESS.equals(dto.getJobType()) && StringUtils.isNotBlank(mtMaterialLot.getMaterialId())) {
                List<LovValueDTO> poTypeLov = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP",tenantId);
                if (CollectionUtils.isNotEmpty(poTypeLov)) {
                    List<String> itemGroupList = poTypeLov.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
                    String itemGroup = hmeEoJobLotMaterialMapper.queryCosMaterialItemGroup(tenantId, mtMaterialLot.getMaterialId(), dto.getSiteId());
                    if (CollectionUtils.isNotEmpty(itemGroupList) && itemGroupList.contains(itemGroup)) {
                        HmeVirtualNum hmeVirtualNum = hmeVirtualNumRepository.queryMaxVirtualNumWoId(tenantId, mtMaterialLot.getMaterialLotId());
                        if (Objects.isNull(hmeVirtualNum) || StringUtils.isBlank(hmeVirtualNum.getVirtualId())) {
                            // 条码内的芯片未完成COS芯片挑选作业，请先完成芯片挑选，再执行芯片投料
                            throw new MtException("HME_EO_JOB_SN_096", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_096", "HME"));
                        } else if (StringUtils.isNotBlank(hmeVirtualNum.getWorkOrderId()) && !hmeVirtualNum.getWorkOrderId().equals(mtEo.getWorkOrderId())) {
                            MtWorkOrder socWo = mtWorkOrderRepository.woPropertyGet(tenantId, hmeVirtualNum.getWorkOrderId());
                            if (Objects.nonNull(socWo) && StringUtils.isNotBlank(socWo.getWorkOrderNum())) {
                                hmeVirtualNum.setWorkOrderId(socWo.getWorkOrderNum());
                            }
                            // 条码内的芯片，已指定工单{}，不可投入当前工单
                            throw new MtException("HME_EO_JOB_SN_097", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_EO_JOB_SN_097", "HME", hmeVirtualNum.getWorkOrderId()));
                        }
                    }
                }
            }
        } else {
            MtWorkOrderVO7 mtWorkOrderVO7 = new MtWorkOrderVO7();
            mtWorkOrderVO7.setWorkOrderId(dto.getWorkOrderId());
            mtWorkOrderVO7.setOperationId(dto.getOperationId());
            if(CollectionUtils.isNotEmpty(woComponentList)){
                woComponentList.forEach(item -> {
                    componentQtyMap.put(item.getMaterialId(), item.getPerQty() * dto.getPrepareQty().doubleValue());
                    bomComponentIdMap.put(item.getMaterialId(), item.getBomComponentId());
                });
            }
        }

        //库位校验
        hmeEoJobSnLotMaterialRepository.CheckLocator(tenantId, mtMaterialLot.getLocatorId(), dto.getWorkcellId());

        //单位校验
        hmeEoJobSnLotMaterialRepository.CheckUom(tenantId,mtMaterialLot.getPrimaryUomId(),mtMaterialLot.getMaterialId());

        String bomComponentId = bomComponentIdMap.getOrDefault(mtMaterialLot.getMaterialId(), "");

        //销售订单校验
        hmeEoJobSnLotMaterialRepository.CheckSoNum(tenantId,dto.getWorkOrderId(),dto.getMaterialLotId(),bomComponentId);

        //在制品校验
        hmeEoJobSnLotMaterialRepository.CheckMaterialAttr(tenantId,dto.getMaterialLotId());

        //获取条码扩展表信息
        MtExtendSettings productionVersionAttr = new MtExtendSettings();
        productionVersionAttr.setAttrName("MATERIAL_VERSION");
        List<MtExtendAttrVO> mtExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                "mt_material_lot_attr", "MATERIAL_LOT_ID", dto.getMaterialLotId(),
                Collections.singletonList(productionVersionAttr));
        String productionVersion = "";
        if (CollectionUtils.isNotEmpty(mtExtendAttrVOList)) {
            productionVersion = mtExtendAttrVOList.get(0).getAttrValue();
        }

        // 如果是计划外投料，需要先插批次物料表
        //预装没有计划外投料 modify by yuchao.wang for lu.bai at 2020.9.22
//        if (!HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())
//                && StringUtils.isNotBlank(dto.getBydMaterialId()) && StringUtils.isBlank(dto.getJobMaterialId())) {
//            HmeEoJobSnVO2 initParam = new HmeEoJobSnVO2();
//            initParam.setWorkcellId(dto.getWorkcellId());
//            initParam.setJobId(dto.getJobId());
//            initParam.setEoId(dto.getEoId());
//            initParam.setSnMaterialId(dto.getSnMaterialId());
//            initParam.setBydMaterialId(dto.getBydMaterialId());
//            initParam.setMaterialLotId(mtMaterialLot.getMaterialLotId());
//            initParam.setLocatorId(mtMaterialLot.getLocatorId());
//            initParam.setLotCode(mtMaterialLot.getLot());
//            initParam.setProductionVersion(productionVersion);
//            if (!bomComponentIdMap.isEmpty()) {
//                String virtualFlag = HmeConstants.ConstantValue.NO;
//                //匹配当前工位下wkc工艺组件清单匹配的批次物料
//                if(bomComponentIdMap.containsKey(mtMaterialLot.getMaterialId())) {
//                    //获取BOM组件扩展表信息
//                    MtExtendSettings virtualFlagAttr = new MtExtendSettings();
//                    virtualFlagAttr.setAttrName("lineAttribute8");
//                    List<MtExtendAttrVO> virtualFlagAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
//                            "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentIdMap.get(mtMaterialLot.getMaterialId()),
//                            Collections.singletonList(virtualFlagAttr));
//                    if (CollectionUtils.isNotEmpty(virtualFlagAttrVOList)) {
//                        virtualFlag = virtualFlagAttrVOList.get(0).getAttrValue();
//                    }
//                }
//                initParam.setVirtualFlag(StringUtils.isBlank(virtualFlag) ? HmeConstants.ConstantValue.NO : virtualFlag);
//                initLotMaterial(tenantId, mtMaterialLot.getMaterialId(), dto.getReleaseQty(), initParam);
//            }
//        }

        //V20200831 modify by penglin.sui for jiao.chen 判断工位 + 物料 +条码是否存在，不存在则新增，存在则更新数量
        HmeEoJobLotMaterial hmeEoJobLotMaterialPara = new HmeEoJobLotMaterial();
        hmeEoJobLotMaterialPara.setTenantId(tenantId);
        hmeEoJobLotMaterialPara.setWorkcellId(dto.getWorkcellId());
        hmeEoJobLotMaterialPara.setMaterialId(mtMaterialLot.getMaterialId());
        hmeEoJobLotMaterialPara.setMaterialLotId(mtMaterialLot.getMaterialLotId());
        HmeEoJobLotMaterial existLotMaterial = this.selectOne(hmeEoJobLotMaterialPara);
        HmeEoJobLotMaterial hmeEoJobLotMaterial = new HmeEoJobLotMaterial();
        String virtualFlag = HmeConstants.ConstantValue.NO;
        if (StringUtils.isNotBlank(bomComponentId)) {
            //获取BOM组件扩展表信息
            MtExtendSettings virtualFlagAttr = new MtExtendSettings();
            virtualFlagAttr.setAttrName("lineAttribute8");
            List<MtExtendAttrVO> virtualFlagAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentId,
                    Collections.singletonList(virtualFlagAttr));
            if (CollectionUtils.isNotEmpty(virtualFlagAttrVOList)) {
                virtualFlag = virtualFlagAttrVOList.get(0).getAttrValue();
            }
        }

        if(Objects.isNull(existLotMaterial)){
            //工位 + 物料 + 条码不存在，再判断 工位 + 物料 + 条码为空是否存在
            List<HmeEoJobLotMaterial> existLotMaterialList = hmeEoJobLotMaterialMapper.queryEoJobLotMaterial2(tenantId,dto.getWorkcellId(),mtMaterialLot.getMaterialId());
            if(CollectionUtils.isNotEmpty(existLotMaterialList)){
                //更新material_lot_id 、release_qty
                BeanUtils.copyProperties(existLotMaterialList.get(0),hmeEoJobLotMaterial);
                hmeEoJobLotMaterial.setMaterialLotId(dto.getMaterialLotId());
                //匹配当前工位下wkc工艺组件清单匹配的批次物料
                hmeEoJobLotMaterial.setReleaseQty(new BigDecimal(componentQtyMap.getOrDefault(mtMaterialLot.getMaterialId(), 0.0)));
                hmeEoJobLotMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobLotMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobLotMaterial.setProductionVersion(productionVersion);
                hmeEoJobLotMaterial.setVirtualFlag(virtualFlag);
                hmeEoJobLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobLotMaterial);
            }else {

                //新增数据
                hmeEoJobLotMaterial.setReleaseQty(new BigDecimal(componentQtyMap.getOrDefault(mtMaterialLot.getMaterialId(), 0.0)));
                hmeEoJobLotMaterial.setTenantId(tenantId);
                hmeEoJobLotMaterial.setWorkcellId(dto.getWorkcellId());
                hmeEoJobLotMaterial.setMaterialId(mtMaterialLot.getMaterialId());
                hmeEoJobLotMaterial.setSnMaterialId(dto.getSnMaterialId());
                // 初始化为未投料
                hmeEoJobLotMaterial.setIsReleased(HmeConstants.ConstantValue.ZERO);
                hmeEoJobLotMaterial.setBydMaterialId(dto.getBydMaterialId());
                hmeEoJobLotMaterial.setMaterialLotId(dto.getMaterialLotId());
                hmeEoJobLotMaterial.setLocatorId(mtMaterialLot.getLocatorId());
                hmeEoJobLotMaterial.setLotCode(mtMaterialLot.getLot());
                hmeEoJobLotMaterial.setVirtualFlag(virtualFlag);
                hmeEoJobLotMaterial.setProductionVersion(productionVersion);
                self().insertSelective(hmeEoJobLotMaterial);
            }
        }else{
            //更新release_qty
            BeanUtils.copyProperties(existLotMaterial,hmeEoJobLotMaterial);
            hmeEoJobLotMaterial.setReleaseQty(new BigDecimal(componentQtyMap.getOrDefault(mtMaterialLot.getMaterialId(), 0.0)));
            hmeEoJobLotMaterial.setLocatorId(mtMaterialLot.getLocatorId());
            hmeEoJobLotMaterial.setLotCode(mtMaterialLot.getLot());
            hmeEoJobLotMaterial.setProductionVersion(productionVersion);
            hmeEoJobLotMaterial.setVirtualFlag(virtualFlag);
            if(componentQtyMap.containsKey(mtMaterialLot.getMaterialId())){
                hmeEoJobLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobLotMaterial);
            }
        }

        if(StringUtils.isNotBlank(bomComponentId)) {
            //获取组件扩展表信息
            MtExtendSettings lineAttribute7Attr = new MtExtendSettings();
            lineAttribute7Attr.setAttrName("lineAttribute7");
            List<MtExtendAttrVO> mtBomExtendAttrVOList = mtExtendSettingsMapper.attrPropertyQuery(tenantId,
                    "mt_bom_component_attr", "BOM_COMPONENT_ID", bomComponentId,
                    Collections.singletonList(lineAttribute7Attr));
            String bomVersion = "";
            if (CollectionUtils.isNotEmpty(mtBomExtendAttrVOList)) {
                bomVersion = mtBomExtendAttrVOList.get(0).getAttrValue();
            }
            if(StringUtils.isNotBlank(bomVersion)) {
                if (!productionVersion.equals(bomVersion)) {
                    //条码物料版本【${1}】与组件需求版本【${2}】不一致
                    throw new MtException("HME_EO_JOB_SN_065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EO_JOB_SN_065", "HME", productionVersion, bomVersion));
                }
            }
        }

//        HmeEoJobLotMaterial jobLotParam = new HmeEoJobLotMaterial();
//        jobLotParam.setTenantId(tenantId);
//        jobLotParam.setWorkcellId(dto.getWorkcellId());
        //List<HmeEoJobLotMaterial> currentMaterialList = hmeEoJobLotMaterialMapper.select(jobLotParam);

//        currentMaterialList.forEach(jobLotMaterial -> {
//            jobLotMaterial.setMaterialLotId(dto.getMaterialLotId());
//            hmeEoJobLotMaterialMapper.updateByPrimaryKeySelective(jobLotMaterial);
//        });

        //预装查询时把materialId替换成snMaterialId,用于筛选虚拟组件下的物料
        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            dto.setMaterialId(dto.getSnMaterialId());
        }
        return matchedJobLotMaterialQuery(tenantId, dto, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobLotMaterialVO> deleteLotMaterial(Long tenantId, HmeEoJobMaterialVO dto) {
        log.info("<====== HmeEoJobLotMaterialRepositoryImpl.deleteLotMaterial tenantId=[{}],dto=[{}]", tenantId, dto);
        if(StringUtils.isBlank(dto.getWorkcellId())){
            //工位不能为空!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotId())){
            //扫描条码为空,请确认
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }

        HmeEoJobLotMaterial hmeEoJobLotMaterialPara = new HmeEoJobLotMaterial();
        hmeEoJobLotMaterialPara.setTenantId(tenantId);
        hmeEoJobLotMaterialPara.setWorkcellId(dto.getWorkcellId());
        hmeEoJobLotMaterialPara.setMaterialId(dto.getMaterialId());
        hmeEoJobLotMaterialPara.setMaterialLotId(dto.getMaterialLotId());
        HmeEoJobLotMaterial hmeEoJobLotMaterial = self().selectOne(hmeEoJobLotMaterialPara);
        if(Objects.nonNull(hmeEoJobLotMaterial)){
            //V20200910 modify by penglin.sui for jioa.chen 校验是否存在多笔工位 + 物料关系 , 多笔则删除当前工位+ 物料 + 条码数据，否则 清空条码字段
            List<HmeEoJobLotMaterial> existLotMaterialList = this.selectByCondition(Condition.builder(HmeEoJobLotMaterial.class)
                    .andWhere(Sqls.custom().andEqualTo(HmeEoJobLotMaterial.FIELD_TENANT_ID, tenantId)
                            .andEqualTo(HmeEoJobLotMaterial.FIELD_WORKCELL_ID, dto.getWorkcellId())
                            .andEqualTo(HmeEoJobLotMaterial.FIELD_MATERIAL_ID, dto.getMaterialId()))
                    .build());
            if(existLotMaterialList.size() > 1){
                hmeEoJobLotMaterialMapper.deleteByPrimaryKey(hmeEoJobLotMaterial.getJobMaterialId());
            }else {
                HmeEoJobLotMaterial hmeEoJobLotMaterialUpdate = new HmeEoJobLotMaterial();
                hmeEoJobLotMaterialUpdate.setJobMaterialId(hmeEoJobLotMaterial.getJobMaterialId());
                hmeEoJobLotMaterialUpdate.setMaterialLotId("");
                hmeEoJobLotMaterialUpdate.setIsReleased(HmeConstants.ConstantValue.ZERO);
                hmeEoJobLotMaterialUpdate.setCostQty(BigDecimal.ZERO);
                hmeEoJobLotMaterialUpdate.setLocatorId("");
                hmeEoJobLotMaterialUpdate.setLotCode("");
                hmeEoJobLotMaterialMapper.updateByPrimaryKeySelective(hmeEoJobLotMaterialUpdate);
            }
        }

        //预装查询时把materialId替换成snMaterialId,用于筛选虚拟组件下的物料 modify by yuchao.wang at 2020.9.24
        if (HmeConstants.JobType.PREPARE_PROCESS.equals(dto.getJobType())) {
            dto.setMaterialId(dto.getSnMaterialId());
        }
        //如果jobId为空则为工位解绑 modify by yuchao.wang for lu.bai at 2020.12.15
        if (StringUtils.isBlank(dto.getJobId())) {
            dto.setIsWorkcellQuery(YES);
        }
        return matchedJobLotMaterialQuery(tenantId, dto, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobLotMaterialVO updateIsReleased(Long tenantId, HmeEoJobLotMaterialVO dto) {
        log.info("<====== HmeEoJobLotMaterialRepositoryImpl.updateIsReleased tenantId=[{}],dto=[{}]", tenantId, dto);
        if(StringUtils.isBlank(dto.getWorkcellId())){
            //工位不能为空!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotId())){
            //扫描条码为空,请确认
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }
        HmeEoJobLotMaterial updateHmeEoJobLotMaterial = new HmeEoJobLotMaterial();
        updateHmeEoJobLotMaterial.setJobMaterialId(dto.getJobMaterialId());
        updateHmeEoJobLotMaterial.setIsReleased(dto.getIsReleased());
        hmeEoJobLotMaterialMapper.updateByPrimaryKeySelective(updateHmeEoJobLotMaterial);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeEoJobLotMaterialVO> batchUpdateIsReleased(Long tenantId, List<HmeEoJobLotMaterialVO> dtoList) {
        if(CollectionUtils.isNotEmpty(dtoList)){
            for (HmeEoJobLotMaterialVO dto : dtoList
                 ) {
                updateIsReleased(tenantId, dto);
            }
        }
        return dtoList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobLotMaterialVO updateReleaseQty(Long tenantId, HmeEoJobLotMaterialVO dto) {
        if(StringUtils.isBlank(dto.getWorkcellId())){
            //工位不能为空!
            throw new MtException("HME_PRO_REPORT_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_PRO_REPORT_002", "HME"));
        }
        if(StringUtils.isBlank(dto.getMaterialLotId())){
            //扫描条码为空,请确认
            throw new MtException("HME_EO_JOB_TIME_SN_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_TIME_SN_004", "HME"));
        }

        HmeEoJobLotMaterial updateHmeEoJobLotMaterial = new HmeEoJobLotMaterial();
        updateHmeEoJobLotMaterial.setJobMaterialId(dto.getJobMaterialId());
        updateHmeEoJobLotMaterial.setReleaseQty(dto.getReleaseQty());
        hmeEoJobLotMaterialMapper.updateByPrimaryKeySelective(updateHmeEoJobLotMaterial);
        return dto;
    }
}
