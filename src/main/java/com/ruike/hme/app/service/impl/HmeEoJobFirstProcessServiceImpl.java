package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeEoJobSnSingleDTO;
import com.ruike.hme.api.dto.HmeEoLovQueryDTO;
import com.ruike.hme.api.dto.HmeMaterialLotLovQueryDTO;
import com.ruike.hme.api.dto.HmeWoLovQueryDTO;
import com.ruike.hme.app.service.HmeEoJobFirstProcessService;
import com.ruike.hme.app.service.HmeEoJobSnBatchService;
import com.ruike.hme.app.service.HmeEoJobSnSingleInService;
import com.ruike.hme.app.service.HmeEoJobSnSingleService;
import com.ruike.hme.domain.repository.HmeEoJobMaterialRepository;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeVirtualNumRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeMaterialLotNcLoadMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotHisRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO10;
import tarzan.inventory.domain.vo.MtContLoadDtlVO4;
import tarzan.inventory.domain.vo.MtContainerVO13;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.vo.MtEoVO;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.*;

/**
 * @Classname HmeEoJobFirstProcessServiceImpl
 * @Description 首序作业平台
 * @Date 2020/9/1 9:42
 * @Author yuchao.wang
 */
@Service
@Slf4j
public class HmeEoJobFirstProcessServiceImpl implements HmeEoJobFirstProcessService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Autowired
    private MtMaterialLotHisRepository mtMaterialLotHisRepository;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Autowired
    private MtUserOrganizationRepository userOrganizationRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeEoJobMaterialRepository repository;

    @Autowired
    private HmeVirtualNumRepository hmeVirtualNumRepository;

    @Autowired
    private HmeMaterialLotNcLoadMapper hmeMaterialLotNcLoadMapper;

    @Autowired
    private HmeEoJobSnSingleInService hmeEoJobSnSingleInService;

    @Autowired
    private HmeEoJobSnSingleService hmeEoJobSnSingleService;

    @Autowired
    private HmeEoJobSnBatchService hmeEoJobSnBatchService;

    /**
     *
     * @Description 首道序作业平台SN条码扫描
     *
     * @author yuchao.wang
     * @date 2020/9/1 10:31
     * @param tenantId 租户ID
     * @param dto 参数
     * @return com.ruike.hme.domain.vo.HmeEoJobSnVO
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEoJobSnVO inSiteScan(Long tenantId, HmeEoJobSnVO3 dto) {
        //校验非空
        if(StringUtils.isEmpty(dto.getSnNum())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "sn条码"));
        }

        //新增首序校验和为数校验
        MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
        mtExtendVO1.setTableName("mt_operation_attr");
        mtExtendVO1.setKeyIdList(dto.getOperationIdList());
        List<MtExtendVO5> mExtendVO5s=new ArrayList<>();
        MtExtendVO5 extend5 = new MtExtendVO5();
        extend5.setAttrName("AUTO_NUMBER");
        mExtendVO5s.add(extend5);
        MtExtendVO5 extend51 = new MtExtendVO5();
        extend51.setAttrName("DIGIT_LIMIT");
        mExtendVO5s.add(extend51);
        mtExtendVO1.setAttrs(mExtendVO5s);
        long startDate = System.currentTimeMillis();
        List<MtExtendAttrVO1> extendAttrList = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId, mtExtendVO1);
        log.info("=================================>首序工序作业平台-进站-新增首序校验和为数校验总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        if (CollectionUtils.isNotEmpty(extendAttrList)) {
            for (MtExtendAttrVO1 extendAttr : extendAttrList) {
                // 首序编号生成
                if ("AUTO_NUMBER".equals(extendAttr.getAttrName())) {
                    if(HmeConstants.ConstantValue.YES.equals(extendAttr.getAttrValue()))
                    {
                        MtExtendVO vo = new MtExtendVO() {{
                            setAttrName("FIRST_PART");
                            setKeyId(dto.getMaterialId());
                            setTableName("mt_material_attr");
                        }};
                        List<MtExtendAttrVO> attrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, vo);
                        if (CollectionUtils.isNotEmpty(attrList)&&Strings.isNotBlank(attrList.get(0).getAttrValue())) {
                            if(!dto.getSnNum().startsWith(attrList.get(0).getAttrValue()))
                            {
                                throw new MtException("HME_EO_JOB_FIRST_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                        "HME_EO_JOB_FIRST_001", "HME"));
                            }
                        }
                    }
                }
                // 位数限制
                if ("DIGIT_LIMIT".equals(extendAttr.getAttrName())) {
                   if(Strings.isNotBlank(extendAttr.getAttrValue()))
                   {
                       if(dto.getSnNum().length()!=Integer.parseInt(extendAttr.getAttrValue()))
                       {
                           throw new MtException("HME_EO_JOB_FIRST_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                   "HME_EO_JOB_FIRST_002", "HME",extendAttr.getAttrValue()));
                       }
                   }
                }
            }
        }
        //如果SN存在物料批中则直接走原有逻辑
        if (!hasSn(tenantId, dto.getSnNum())) {
            if(StringUtils.isEmpty(dto.getWorkOrderId())){
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "工单ID"));
            }
            if(StringUtils.isEmpty(dto.getSiteId())){
                throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "QMS_MATERIAL_INSP_0020", "QMS", "站点ID"));
            }

            //查询EO信息
            HmeEoVO hmeEoVO = hmeEoJobSnRepository.defaultEoQueryForFirst(tenantId, dto);
            if (Objects.isNull(hmeEoVO) || StringUtils.isBlank(hmeEoVO.getEoId())) {
                throw new MtException("HME_EO_JOB_SN_111", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_111", "HME"));
            }
            dto.setEoId(hmeEoVO.getEoId());

            //查询目前物料批
            MtMaterialLot materialLot = new MtMaterialLot();
            materialLot.setTenantId(tenantId);
            materialLot.setMaterialLotCode(hmeEoVO.getIdentification());
            materialLot = mtMaterialLotMapper.selectOne(materialLot);
            if (Objects.isNull(materialLot) || StringUtils.isBlank(materialLot.getMaterialLotId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "物料批信息"));
            }

            //获取事件ID
            String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
                setEventTypeCode("FIRST_STEP_IN");
            }});

            //更新物料批条码
            MtMaterialLot update = new MtMaterialLot();
            update.setMaterialLotId(materialLot.getMaterialLotId());
            update.setMaterialLotCode(dto.getSnNum());
            update.setIdentification(dto.getSnNum());
            startDate = System.currentTimeMillis();
            mtMaterialLotMapper.updateByPrimaryKeySelective(update);
            log.info("=================================>首序工序作业平台-进站-更新物料批条码总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
            //记录物料批历史
            MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
            BeanUtils.copyProperties(materialLot, mtMaterialLotHis);
            mtMaterialLotHis.setEventId(eventId);
            mtMaterialLotHis.setMaterialLotCode(update.getMaterialLotCode());
            mtMaterialLotHis.setIdentification(update.getIdentification());
            startDate = System.currentTimeMillis();
            mtMaterialLotHisRepository.insertSelective(mtMaterialLotHis);
            log.info("=================================>首序工序作业平台-进站-记录物料批历史总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");

            //更新物料物料批扩展属性
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            MtExtendVO5 statusAttr = new MtExtendVO5();
            statusAttr.setAttrName("FIRST_STEP_IN_FLAG");
            statusAttr.setAttrValue(WmsConstant.ConstantValue.YES);
            mtExtendVO5List.add(statusAttr);
            startDate = System.currentTimeMillis();
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr",
                    materialLot.getMaterialLotId(), eventId, mtExtendVO5List);
            log.info("=================================>首序工序作业平台-进站-更新物料物料批扩展属性总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");

            //更新EO
            MtEoVO mtEoVO = new MtEoVO();
            mtEoVO.setEventId(eventId);
            mtEoVO.setTenantId(tenantId);
            mtEoVO.setEoId(dto.getEoId());
            mtEoVO.setIdentification(dto.getSnNum());
            startDate = System.currentTimeMillis();
            mtEoRepository.eoUpdate(tenantId, mtEoVO, HmeConstants.ConstantValue.NO);
            log.info("=================================>首序工序作业平台-进站-更新EO总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        }

        //调用原有工序作业平台扫描sn uri[/hme-eo-job-sn/in-site-scan]
        startDate = System.currentTimeMillis();
//        HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobSnRepository.inSiteScan(tenantId, dto);

        //V20210615 modify by penglin.sui for hui.ma 直接调用新单件平台进站
        HmeEoJobSnVO hmeEoJobSnVO = hmeEoJobSnSingleInService.inSiteScan(tenantId, dto);
        log.info("=================================>首序工序作业平台-进站-调用原有工序作业平台扫描sn总耗时："+(System.currentTimeMillis() - startDate) + "毫秒");
        return hmeEoJobSnVO;
    }

    /**
     *
     * @Description 序列物料投料扫描
     *
     * @author yuchao.wang
     * @date 2020/9/1 20:17
     * @param tenantId 租户ID
     * @param dto 参数
     * @return com.ruike.hme.domain.vo.HmeEoJobMaterialVO
     *
     */
    @Override
    public List<HmeEoJobMaterialVO> releaseScan(Long tenantId, HmeEoJobMaterialVO dto) {
        //校验非空
        if(StringUtils.isEmpty(dto.getMaterialLotCode())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "物料批条码"));
        }

        //如果物料批不存在则提示报错--这里先不校验，保持原有逻辑
        /*if (!hasSn(tenantId, dto.getMaterialLotCode())) {
            //尝试查询容器是否存在
            MtContainerVO13 mtContainerVO13 = new MtContainerVO13();
            mtContainerVO13.setContainerCode(dto.getMaterialLotCode());
            List<String> containerIds = mtContainerRepository.propertyLimitContainerQuery(tenantId, mtContainerVO13);

            //容器存在报错选择容器内的物料，容器也不存在说明扫的条码不存在
            if(CollectionUtils.isEmpty(containerIds) || StringUtils.isBlank(containerIds.get(0))){
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "物料批信息"));
            } else {
                throw new MtException("Exception", "请选择容器装载的物料批");
            }
        }*/

        //调用原有序列物料投料扫描逻辑 uri[/hme-eo-job-material/release-scan]
        return repository.releaseScan(tenantId, dto);
    }

    /**
     *
     * @Description 查询工单LOV
     *
     * @author yuchao.wang
     * @date 2020/9/1 13:52
     * @param tenantId 租户ID
     * @param dto 参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeWorkOrderVO>
     *
     */
    @Override
    public Page<HmeWorkOrderVO> workOrderQuery(Long tenantId, HmeWoLovQueryDTO dto, PageRequest pageRequest) {
        //校验非空
        if(StringUtils.isEmpty(dto.getWorkcellId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工位ID"));
        }

        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = (Objects.isNull(curUser) || Objects.isNull(curUser.getUserId())) ? -1L : curUser.getUserId();

        // 获取用户默认站点
        MtUserOrganization userOrganization = new MtUserOrganization();
        userOrganization.setUserId(userId);
        userOrganization.setOrganizationType("SITE");
        MtUserOrganization defaultSite =
                userOrganizationRepository.userDefaultOrganizationGet(tenantId, userOrganization);
        if (Objects.isNull(defaultSite) || StringUtils.isBlank(defaultSite.getOrganizationId())) {
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "用户默认站点"));
        }

        //获取工段
        MtModOrganizationVO2 processParam = new MtModOrganizationVO2();
        processParam.setTopSiteId(defaultSite.getOrganizationId());
        processParam.setOrganizationId(dto.getWorkcellId());
        processParam.setOrganizationType("WORKCELL");
        processParam.setParentOrganizationType("WORKCELL");
        processParam.setQueryType("TOP");
        List<MtModOrganizationItemVO> processList =
                mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, processParam);
        if(CollectionUtils.isEmpty(processList) || Objects.isNull(processList.get(0))){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "工段信息"));
        }

        //查询产线ID
        MtModOrganizationVO2 prodLineParam = new MtModOrganizationVO2();
        prodLineParam.setTopSiteId(defaultSite.getOrganizationId());
        prodLineParam.setOrganizationId(processList.get(0).getOrganizationId());
        prodLineParam.setOrganizationType("WORKCELL");
        prodLineParam.setParentOrganizationType("PROD_LINE");
        prodLineParam.setQueryType("TOP");
        List<MtModOrganizationItemVO> prodLineList =
                mtModOrganizationRelRepository.parentOrganizationRelQuery(tenantId, prodLineParam);
        if(CollectionUtils.isEmpty(prodLineList) || Objects.isNull(prodLineList.get(0))){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "产线信息"));
        }
        dto.setProductionLineId(prodLineList.get(0).getOrganizationId());

        return PageHelper.doPage(pageRequest, () -> hmeEoJobSnRepository.workOrderQueryForFirst(tenantId, dto));
    }

    /**
     *
     * @Description 查询SN号是否在物料批中
     *
     * @author yuchao.wang
     * @date 2020/9/1 15:48
     * @param tenantId 租户ID
     * @param barcode 条码
     * @return tarzan.inventory.domain.entity.MtMaterialLot
     *
     */
    @Override
    public MtMaterialLot queryMaterialLot(Long tenantId, String barcode) {
        //查询目前物料批
        MtMaterialLot materialLot = new MtMaterialLot();
        materialLot.setTenantId(tenantId);
        materialLot.setMaterialLotCode(barcode);
        materialLot = mtMaterialLotMapper.selectOne(materialLot);
        return Objects.isNull(materialLot) ? new MtMaterialLot() : materialLot;
    }

    /**
     *
     * @Description 查询条码是否为容器条码
     *
     * @author yuchao.wang
     * @date 2020/9/4 17:34
     * @param tenantId 租户ID
     * @param barcode 条码
     * @return tarzan.inventory.domain.entity.MtContainer
     *
     */
    @Override
    public MtContainer queryContainer(Long tenantId, String barcode) {
        MtContainer mtContainer = new MtContainer();
        MtContainerVO13 mtContainerVO13 = new MtContainerVO13();
        mtContainerVO13.setContainerCode(barcode);
        List<String> containerIds = mtContainerRepository.propertyLimitContainerQuery(tenantId, mtContainerVO13);
        if(CollectionUtils.isNotEmpty(containerIds) && StringUtils.isNotBlank(containerIds.get(0))){
            mtContainer.setContainerId(containerIds.get(0));
            mtContainer.setContainerCode(barcode);
        }
        return mtContainer;
    }

    /**
     *
     * @Description EO查询LOV
     *
     * @author yuchao.wang
     * @date 2020/9/1 16:29
     * @param tenantId 租户ID
     * @param dto 参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEoVO>
     *
     */
    @Override
    public Page<HmeEoVO> eoQuery(Long tenantId, HmeEoLovQueryDTO dto, PageRequest pageRequest) {
        //校验非空
        if(StringUtils.isEmpty(dto.getWorkOrderId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工单ID"));
        }
        if(StringUtils.isEmpty(dto.getSiteId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "站点ID"));
        }

        return PageHelper.doPage(pageRequest, () -> hmeEoJobSnRepository.eoQueryForFirst(tenantId, dto));
    }

    /**
     *
     * @Description 物料批查询LOV
     *
     * @author yuchao.wang
     * @date 2020/9/1 19:34
     * @param tenantId 租户ID
     * @param dto 参数
     * @param pageRequest 分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeMaterialLotVO2>
     *
     */
    @Override
    public Page<HmeMaterialLotVO2> materialLotLovQuery(Long tenantId, HmeMaterialLotLovQueryDTO dto, PageRequest pageRequest) {
        //校验非空
        if(StringUtils.isEmpty(dto.getBarode())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "容器编码"));
        }

        //查询容器ID
        MtContainerVO13 mtContainerVO13 = new MtContainerVO13();
        mtContainerVO13.setContainerCode(dto.getBarode());
        List<String> containerIds = mtContainerRepository.propertyLimitContainerQuery(tenantId, mtContainerVO13);
        if(CollectionUtils.isEmpty(containerIds) || StringUtils.isBlank(containerIds.get(0))){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "容器信息"));
        }

        //查询容器下所有的物料批
        MtContLoadDtlVO10 contLoadDtlParam = new MtContLoadDtlVO10();
        contLoadDtlParam.setContainerId(containerIds.get(0));
        contLoadDtlParam.setAllLevelFlag(HmeConstants.ConstantValue.YES);
        List<MtContLoadDtlVO4> mtContLoadDtlVo1List = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId, contLoadDtlParam);
        if (CollectionUtils.isEmpty(mtContLoadDtlVo1List)) {
            PageInfo pageInfo = new PageInfo(pageRequest.getPage(), pageRequest.getSize());
            return new Page<HmeMaterialLotVO2>(new ArrayList<HmeMaterialLotVO2>(), pageInfo, 0);
        }

        //获取物料批ID集合
        List<String> materialLotIds = mtContLoadDtlVo1List.stream().filter(t -> t.getMaterialLotId() != null)
                .map(MtContLoadDtlVO4::getMaterialLotId).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(materialLotIds)) {
            PageInfo pageInfo = new PageInfo(pageRequest.getPage(), pageRequest.getSize());
            return new Page<HmeMaterialLotVO2>(new ArrayList<HmeMaterialLotVO2>(), pageInfo, 0);
        }

        //查询所有物料批信息
        return PageHelper.doPage(pageRequest, () -> hmeEoJobSnRepository.materialLotLovQueryForFirst(tenantId, materialLotIds, dto));
    }

    /**
     *
     * @Description 查询条码下的虚拟号
     *
     * @author yuchao.wang
     * @date 2020/9/29 21:38
     * @param tenantId 租户ID
     * @param materialLotCodeList 条码号
     * @return java.util.List<com.ruike.hme.domain.vo.HmeVirtualNumVO>
     *
     */
    @Override
    public List<HmeVirtualNumVO> virtualNumQuery(Long tenantId, List<String> materialLotCodeList) {
        if (CollectionUtils.isEmpty(materialLotCodeList)) {
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "物料编码"));
        }

        List<HmeVirtualNumVO> resultList = new ArrayList<>();
        List<HmeVirtualNumVO> hmeVirtualNumVOList = hmeVirtualNumRepository.queryVirtualNumByBarcode(tenantId, materialLotCodeList);
        if (CollectionUtils.isNotEmpty(hmeVirtualNumVOList)) {
            List<String> virtualIdList = hmeVirtualNumVOList.stream().map(HmeVirtualNumVO::getVirtualId).distinct().collect(Collectors.toList());
            //查询报废数
            List<HmeMaterialLotNcLoadVO4> hmeMaterialLotNcLoadVO4List = hmeMaterialLotNcLoadMapper.queryNcLoadByVirtualId(tenantId,virtualIdList);
            Map<String,List<HmeMaterialLotNcLoadVO4>> hmeMaterialLotNcLoadVO4Map = new HashMap<>();
            if(CollectionUtils.isNotEmpty(hmeMaterialLotNcLoadVO4List)){
                hmeMaterialLotNcLoadVO4Map = hmeMaterialLotNcLoadVO4List.stream().collect(Collectors.groupingBy(e -> e.getVirtualId()));
            }

            for (HmeVirtualNumVO hmeVirtualNumVO : hmeVirtualNumVOList) {
                if (CollectionUtils.isNotEmpty(hmeVirtualNumVO.getDetailList())) {
                    List<HmeSelectionDetailsVO> detailsList = hmeVirtualNumVO.getDetailList().stream().sorted(Comparator.comparing(HmeSelectionDetailsVO::getNewLoadRow)
                            .thenComparing(HmeSelectionDetailsVO::getNewLoadColumn)).collect(Collectors.toList());

                    //获取起止位置
                    HmeSelectionDetailsVO startLocation = detailsList.get(0);
                    HmeSelectionDetailsVO endLocation = detailsList.get(detailsList.size() - 1);
                    if (Objects.nonNull(startLocation) && Objects.nonNull(endLocation)) {
                        String location = String.valueOf((char) (64 + (Objects.isNull(startLocation.getNewLoadRow()) ? 1 : startLocation.getNewLoadRow()))) +
                                startLocation.getNewLoadColumn() + "-" +
                                (char) (64 + (Objects.isNull(endLocation.getNewLoadRow()) ? 1 : endLocation.getNewLoadRow())) +
                                endLocation.getNewLoadColumn();
                        hmeVirtualNumVO.setChipLocation(location);
                    }

                    List<HmeSelectionDetailsVO> detailsList2 = hmeVirtualNumVO.getDetailList().stream().sorted(Comparator.comparing(HmeSelectionDetailsVO::getNewLoad))
                            .collect(Collectors.toList());
                    hmeVirtualNumVO.setHotSinkCode(detailsList2.get(0).getHotSinkCode());
                }

                //修改数量
                List<HmeMaterialLotNcLoadVO4> hmeMaterialLotNcLoadVO4List1 = hmeMaterialLotNcLoadVO4Map.getOrDefault(hmeVirtualNumVO.getVirtualId() , new ArrayList<>());
                if(Objects.nonNull(hmeVirtualNumVO.getQuantity())){
                    hmeVirtualNumVO.setQuantity((BigDecimal.valueOf(hmeVirtualNumVO.getQuantity())
                            .subtract(BigDecimal.valueOf(hmeMaterialLotNcLoadVO4List1.size()))).longValue());
                }
            }
            resultList = hmeVirtualNumVOList.stream().sorted(Comparator.comparing(HmeVirtualNumVO::getEnableFlag, Comparator.reverseOrder())
                    .thenComparing(HmeVirtualNumVO::getChipLocation)).collect(Collectors.toList());
        }
        return resultList;
    }

    @Override
    public String firstPartQuery(Long tenantId, String materialId) {
      String firstPart=null;
        MtExtendVO vo = new MtExtendVO() {{
            setAttrName("FIRST_PART");
            setKeyId(materialId);
            setTableName("mt_material_attr");
        }};
        List<MtExtendAttrVO> attrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, vo);
        if (CollectionUtils.isNotEmpty(attrList)) {
            firstPart=attrList.get(0).getAttrValue();
        }
        return firstPart;
    }

    @Override
    public List<HmeEoJobSnBatchVO4> release(Long tenantId, HmeEoJobSnSingleDTO dto) {
        dto.setIsFirstProcess(HmeConstants.ConstantValue.YES);
        return hmeEoJobSnSingleService.release(tenantId , dto);
    }

    @Override
    public HmeEoJobSnVO9 releaseBack(Long tenantId, HmeEoJobSnVO9 dto) {
        dto.setIsFirstProcess(HmeConstants.ConstantValue.YES);
        return hmeEoJobSnBatchService.releaseBack(tenantId , dto);
    }

    /**
     *
     * @Description 查询SN号是否在物料批中 true:存在 false:不存在
     *
     * @author yuchao.wang
     * @date 2020/9/1 15:58
     * @param tenantId 租户ID
     * @param snNum SN号
     * @return boolean
     *
     */
    private boolean hasSn(Long tenantId, String snNum){
        //查询目前物料批
        MtMaterialLot materialLot = new MtMaterialLot();
        materialLot.setTenantId(tenantId);
        materialLot.setMaterialLotCode(snNum);
        materialLot = mtMaterialLotMapper.selectOne(materialLot);
        return !Objects.isNull(materialLot) && StringUtils.isNotBlank(materialLot.getMaterialLotId());
    }
}