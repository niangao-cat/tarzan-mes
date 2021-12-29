package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.repository.HmeBatchLoadContainerRepository;
import com.ruike.hme.domain.vo.HmeLoadContainerVO;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeBatchLoadContainerMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.*;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
* @Classname HmeBatchLoadContainerRepositoryImpl
* @Description 批量完工装箱 资源库实现
* @Date  2020/6/5 9:42
* @Created by Deng xu
*/
@Component
public class HmeBatchLoadContainerRepositoryImpl implements HmeBatchLoadContainerRepository {

    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    private HmeBatchLoadContainerMapper mapper;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private MtEventRepository mtEventRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    /**
    * @Description: 批量完工装箱-扫描外箱条码
    * @author: Deng Xu
    * @date 2020/6/5 10:18
    * @param tenantId 租户ID
    * @param outerContainerCode 外箱条码
    * @return : com.ruike.hme.domain.vo.HmeLoadContainerVO
    * @version 1.0
    */
    @Override
    public HmeLoadContainerVO scanOuterContainer(Long tenantId, String outerContainerCode) {
        if( StringUtils.isEmpty(outerContainerCode)){
            throw new MtException("HME_LOAD_CONTAINER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_001", "HME"));
        }
        HmeLoadContainerVO outerContainer = new HmeLoadContainerVO();
        //根据租户和外箱条码查询外箱信息
        MtContainer queryContainer = new MtContainer();
        queryContainer.setTenantId(tenantId);
        queryContainer.setContainerCode(outerContainerCode);
        queryContainer = mtContainerRepository.selectOne(queryContainer);
        //判断当前外箱条码是否存在,否则报错：当前容器条码不存在
        if(null ==queryContainer || StringUtils.isEmpty(queryContainer.getContainerId())){
            throw new MtException("HME_LOAD_CONTAINER_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_001", "HME"));
        }
        //校验容器可用性,容器状态需要为可下达
        if( !HmeConstants.StatusCode.CANRELEASE.equals(queryContainer.getStatus())){
            throw new MtException("HME_LOAD_CONTAINER_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_002", "HME",outerContainerCode));
        }

        //容器货位
        List<String> locatorCodeList = mapper.queryLocatorCodeByContainerId(tenantId, queryContainer.getContainerId());
        outerContainer.setLocatorCode(CollectionUtils.isNotEmpty(locatorCodeList) ? locatorCodeList.get(0) : "");

        outerContainer.setCodeType("CONTAINER");
        outerContainer.setOuterContainerId(queryContainer.getContainerId());
        outerContainer.setOuterContainerCode(queryContainer.getContainerCode());
        /*判断当前外箱容器条码是否已经装载了物料批*/
        //调用API【containerLimitObjectQuery】获取容器内所有已装载对象列表
        MtContLoadDtlVO mtConLoadDetailVO = new MtContLoadDtlVO();
        mtConLoadDetailVO.setContainerId(queryContainer.getContainerId());
        List<MtContLoadDtlVO6> loadDetails =
                mtContainerLoadDetailRepository.containerLimitObjectQuery(tenantId, mtConLoadDetailVO);
        List<HmeLoadContainerVO> loadMaterialLotList= new ArrayList<>();
        //未装载对象
        if(CollectionUtils.isEmpty(loadDetails)){
            outerContainer.setLoadFlag(HmeConstants.ConstantValue.NO);
            outerContainer.setContainerDtlList(loadMaterialLotList);
            return outerContainer;
        }
        outerContainer.setLoadFlag(HmeConstants.ConstantValue.YES);
        Double barCodeQty = 0.0;
        List<String> materialLotIdList = new ArrayList<>();
        List<HmeLoadContainerVO> containerVOList = new ArrayList<>();
        for (MtContLoadDtlVO6 e : loadDetails) {
            HmeLoadContainerVO hmeLoadContainerVO = new HmeLoadContainerVO();
            hmeLoadContainerVO.setCodeType(e.getLoadObjectType());
            if(StringUtils.equals(e.getLoadObjectType(),HmeConstants.LoadTypeCode.CONTAINER)){
                MtContainer mtContainer = mtContainerRepository.selectByPrimaryKey(e.getLoadObjectId());
                hmeLoadContainerVO.setContainerName(mtContainer.getContainerName());
                hmeLoadContainerVO.setContainerCode(mtContainer.getContainerCode());
                hmeLoadContainerVO.setContainerId(mtContainer.getContainerId());
                hmeLoadContainerVO.setScanFlag("N");
                //查询容器下物料批
                List<HmeLoadContainerVO> loadMaterialLot = this.getLoadMaterialLot(tenantId, mtContainer.getContainerId(), mtContainer.getContainerCode());
                for (HmeLoadContainerVO loadContainerVO : loadMaterialLot) {
                    barCodeQty += loadContainerVO.getPrimaryUomQty();
                    //调用materialLotLimitAttrQuery-获取物料批扩展属性
                    MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                    mtMaterialLotAttrVO2.setMaterialLotId(loadContainerVO.getMaterialLotId());
                    //物料版本
                    mtMaterialLotAttrVO2.setAttrName("MATERIAL_VERSION");
                    List<MtExtendAttrVO> versionList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                    if(CollectionUtils.isNotEmpty(versionList)){
                        loadContainerVO.setMaterialVersion(versionList.get(0).getAttrValue());
                    }
                    //在制标识
                    mtMaterialLotAttrVO2.setAttrName("MF_FLAG");
                    List<MtExtendAttrVO> flagList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                    if(CollectionUtils.isNotEmpty(flagList)){
                        loadContainerVO.setMfFlag(flagList.get(0).getAttrValue());
                    }
                }

                hmeLoadContainerVO.setContainerDtlList(loadMaterialLot);

                containerVOList.add(hmeLoadContainerVO);
            }else if (StringUtils.equals(e.getLoadObjectType(),HmeConstants.LoadTypeCode.MATERIAL_LOT)){
               materialLotIdList.add(e.getLoadObjectId());
            }
        };
        if(CollectionUtils.isNotEmpty(materialLotIdList)){
            List<HmeLoadContainerVO> hmeLoadContainerVOList = mapper.queryOuterContainerLoadDetail(outerContainerCode, materialLotIdList);
            for (HmeLoadContainerVO e : hmeLoadContainerVOList) {
                barCodeQty += e.getPrimaryUomQty();
                //调用materialLotLimitAttrQuery-获取物料批扩展属性
                MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
                mtMaterialLotAttrVO2.setMaterialLotId(e.getMaterialLotId());
                //物料版本
                mtMaterialLotAttrVO2.setAttrName("MATERIAL_VERSION");
                List<MtExtendAttrVO> versionList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                if(CollectionUtils.isNotEmpty(versionList)){
                    e.setMaterialVersion(versionList.get(0).getAttrValue());
                }
                //在制标识
                mtMaterialLotAttrVO2.setAttrName("MF_FLAG");
                List<MtExtendAttrVO> flagList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
                if(CollectionUtils.isNotEmpty(flagList)){
                    e.setMfFlag(flagList.get(0).getAttrValue());
                }

                e.setCodeType("MATERIAL_LOT");
                e.setScanFlag("N");
            };
            containerVOList.addAll(hmeLoadContainerVOList);
        }
        if(CollectionUtils.isNotEmpty(containerVOList)){
            for(HmeLoadContainerVO hmeLoadContainerVO:containerVOList){
                if("CHECKED".equals(hmeLoadContainerVO.getMaterialLotStatus())){
                    throw new MtException("Exception","当前容器下条码"+hmeLoadContainerVO.getMaterialLotCode()+"状态为已入库,不允许装箱！");
                }
            }
        }
        outerContainer.setContainerDtlList(containerVOList);
        outerContainer.setScanQty(Double.valueOf(loadDetails.size()));
        outerContainer.setBarCodeQty(barCodeQty);
        return outerContainer;
    }

    /**
    * @Description: 批量完工装箱-扫描待装箱条码
    * @author: Deng Xu
    * @date 2020/6/5 14:41
    * @param tenantId 租户ID
    * @param loadContainerVO 外箱条码ID、待装箱条码code
    * @return : com.ruike.hme.domain.vo.HmeLoadContainerVO
    * @version 1.0
    */
    @Override
    public HmeLoadContainerVO scanContainer(Long tenantId, HmeLoadContainerVO loadContainerVO) {
        //校验参数：获取外箱条码和待装箱条码失败,请重试
        if(StringUtils.isEmpty(loadContainerVO.getOuterContainerId()) || StringUtils.isEmpty(loadContainerVO.getContainerCode())){
            throw new MtException("HME_LOAD_CONTAINER_014", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_014", "HME"));
        }
        //根据ID查询外箱信息
        MtContainer queryOutContainer = new MtContainer();
        queryOutContainer.setTenantId(tenantId);
        queryOutContainer.setContainerId(loadContainerVO.getOuterContainerId());
        queryOutContainer = mtContainerRepository.selectByPrimaryKey(queryOutContainer);
        List<HmeLoadContainerVO> loadedList = new ArrayList<>();
        /*判断当前外箱容器条码是否已经装载了物料批*/
        //调用API【containerLimitObjectQuery】获取容器内所有已装载对象列表
        MtContLoadDtlVO mtConLoadDetailVO = new MtContLoadDtlVO();
        mtConLoadDetailVO.setContainerId(loadContainerVO.getOuterContainerId());
        List<MtContLoadDtlVO6> loadDetails =
                mtContainerLoadDetailRepository.containerLimitObjectQuery(tenantId, mtConLoadDetailVO);
        //未装载对象
        if(CollectionUtils.isNotEmpty(loadDetails)){
            //如果已经装载了对象，需要加载物料批信息
            loadedList = this.getLoadMaterialLot(tenantId,loadContainerVO.getOuterContainerId(),"");
        }
        HmeLoadContainerVO returnVo = new HmeLoadContainerVO();
        //首先判定当前扫描的SN是不是物料批条码
        MtMaterialLot queryMaterialLot = new MtMaterialLot();
        queryMaterialLot.setTenantId(tenantId);
        queryMaterialLot.setMaterialLotCode(loadContainerVO.getContainerCode());
        queryMaterialLot = mtMaterialLotRepository.selectOne(queryMaterialLot);
        List<HmeLoadContainerVO> loadMaterialLotList = new ArrayList<>();
        //当前条码类型
        String codeType = "";
        String containerId ="";
        String containerCode ="";
        String locatorId = "";
        String loadObjectId = "";
        //如果可以查找到，则表示当前扫描的SN为物料批条码
        if(null!=queryMaterialLot && StringUtils.isNotEmpty(queryMaterialLot.getMaterialLotId())){
            returnVo.setCodeType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            returnVo.setMaterialLotId(queryMaterialLot.getMaterialLotId());
            returnVo.setMaterialLotCode(queryMaterialLot.getMaterialLotCode());
            //获取该物料批信息，此时显示最外层外箱条码
            List<String> materialLotIdList = new ArrayList<>();
            materialLotIdList.add(queryMaterialLot.getMaterialLotId());
            loadMaterialLotList = mapper.queryOuterContainerLoadDetail("" , materialLotIdList);
            codeType=HmeConstants.LoadTypeCode.MATERIAL_LOT;
            locatorId = queryMaterialLot.getLocatorId();
            loadObjectId = queryMaterialLot.getMaterialLotId();
        }
        //如果不是物料批条码，则判断当前扫描的SN是不是容器条码
        else{
            MtContainer queryContainer = new MtContainer();
            queryContainer.setTenantId(tenantId);
            queryContainer.setContainerCode(loadContainerVO.getContainerCode());
            queryContainer= mtContainerRepository.selectOne(queryContainer);
            //如果可以查找到，则表示当前扫描的SN为容器条码
            if(null!=queryContainer && StringUtils.isNotEmpty(queryContainer.getContainerId())){
                containerId = queryContainer.getContainerId();
                containerCode = queryContainer.getContainerCode();
                returnVo.setCodeType(HmeConstants.LoadTypeCode.CONTAINER);
                returnVo.setContainerId(queryContainer.getContainerId());
                returnVo.setContainerCode(queryContainer.getContainerCode());
                //通过容器条码获取装载当前容器下所有的物料批
                loadMaterialLotList =this.getLoadMaterialLot(tenantId,queryContainer.getContainerId(),loadContainerVO.getContainerCode());
                codeType=HmeConstants.LoadTypeCode.CONTAINER;
                locatorId = queryContainer.getLocatorId();
                loadObjectId = queryContainer.getContainerId();
            }
            //如果没有查找到，则表示当前扫描的SN既不是物料批条码也不是容器条码,报错：当前编码不存在有效装箱信息
            else{
                throw new MtException("HME_LOAD_CONTAINER_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LOAD_CONTAINER_006", "HME"));
            }
        }
        //更新空容器货位
        if(StringUtils.isBlank(queryOutContainer.getTopContainerId()) && StringUtils.equals(loadContainerVO.getLoadFlag(), HmeConstants.ConstantValue.NO)){
            MtModLocator mtModLocator = mtModLocatorRepository.selectByPrimaryKey(locatorId);
            if(mtModLocator != null && !StringUtils.equals(loadContainerVO.getLocatorCode(), mtModLocator.getLocatorCode())){
                //生成事件
                MtEventCreateVO eventCreateVO2 = new MtEventCreateVO();
                eventCreateVO2.setEventTypeCode("CONTAINER_UPDATE");
                String eventId = mtEventRepository.eventCreate(tenantId, eventCreateVO2);

                //更新容器货位
                MtContainerVO12 dto1 = new MtContainerVO12();
                dto1.setContainerId(queryOutContainer.getContainerId());
                dto1.setLocatorId(locatorId);
                dto1.setEventId(eventId);
                mtContainerRepository.containerUpdate(tenantId, dto1, HmeConstants.ConstantValue.NO);

                returnVo.setLocatorCode(mtModLocator.getLocatorCode());
            }
        }

        // 容器装载验证
        MtContainerVO9 mtContainerVo9 = new MtContainerVO9();
        mtContainerVo9.setContainerId(loadContainerVO.getOuterContainerId());
        mtContainerVo9.setLoadObjectType(codeType);
        mtContainerVo9.setLoadObjectId(loadObjectId);
        mtContainerRepository.containerLoadVerify(tenantId, mtContainerVo9);

        //循环判断已装载物料批，并返回是否已装载标识
        Map<String, List<HmeLoadContainerVO>> groupLoadedList = new HashMap<String, List<HmeLoadContainerVO>>(16);
        if(CollectionUtils.isNotEmpty(loadedList)) {
            groupLoadedList = loadedList.stream().collect(Collectors.groupingBy(item -> item.getMaterialLotId()));
        }
        Double scanQty = 0.0;
        //校验每个物料批
        for(HmeLoadContainerVO loadContainer :loadMaterialLotList){
            //报错：当前条码存在已失效物料信息不允许装箱
            if(HmeConstants.ConstantValue.NO.equals(loadContainer.getEnableFlag())){
                throw new MtException("HME_LOAD_CONTAINER_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LOAD_CONTAINER_007", "HME"));
            }
            //报错：当前条码存在冻结物料不允许装箱
            if(HmeConstants.ConstantValue.YES.equals(loadContainer.getFreezeFlag())){
                throw new MtException("HME_LOAD_CONTAINER_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LOAD_CONTAINER_008", "HME"));
            }
            //报错：当前条码对应物料正在盘点中不允许装箱
            if (HmeConstants.ConstantValue.YES.equals(loadContainer.getStocktakeFlag())) {
                throw new MtException("HME_LOAD_CONTAINER_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LOAD_CONTAINER_009", "HME"));
            }
            //报错：当前条码存在不合格物料不允许装箱
            if (HmeConstants.ConstantValue.NG.equals(loadContainer.getQualityStatus())) {
                throw new MtException("HME_LOAD_CONTAINER_010", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LOAD_CONTAINER_010", "HME"));
            }
            //2020/12/07 add by sanfeng.zhang for zhouyiwei 报错：当前条码已扫描不允许装箱
            if (HmeConstants.ConstantValue.SCANNED.equals(loadContainer.getMaterialLotStatus())) {
                throw new MtException("HME_LOAD_CONTAINER_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LOAD_CONTAINER_016", "HME", loadContainer.getMaterialLotCode()));
            }
            //当前条码状态已复核，不允许装箱
            if ("CHECKED".equals(loadContainer.getMaterialLotStatus())) {
                throw new MtException("Exception","当前条码"+loadContainer.getMaterialLotCode()+"状态为已入库,不允许装箱！");
            }
            //获取扫描数量总和
            scanQty += loadContainer.getPrimaryUomQty();
            loadContainer.setCodeType(codeType);
            loadContainer.setOuterContainerId(loadContainerVO.getOuterContainerId());
            loadContainer.setOuterContainerCode(queryOutContainer.getContainerCode());
            loadContainer.setContainerId(containerId);
            loadContainer.setContainerCode(containerCode);
            //循环判断已装载物料批，并返回是否已装载标识
            if (groupLoadedList.containsKey(loadContainer.getMaterialLotId())) {
                loadContainer.setLoadFlag(HmeConstants.ConstantValue.YES);
            }
            loadContainer.setScanFlag("Y");

            //调用materialLotLimitAttrQuery-获取物料批扩展属性
            //物料版本
            MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
            mtMaterialLotAttrVO2.setMaterialLotId(loadContainer.getMaterialLotId());
            mtMaterialLotAttrVO2.setAttrName("MATERIAL_VERSION");
            List<MtExtendAttrVO> versionList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
            if(CollectionUtils.isNotEmpty(versionList)){
                loadContainer.setMaterialVersion(versionList.get(0).getAttrValue());
            }
        }
        returnVo.setContainerDtlList(loadMaterialLotList);
        returnVo.setScanQty(scanQty);
        returnVo.setScanFlag("Y");
        return returnVo;
    }

    /**
    * @Description: 通过容器ID获取装载当前容器下所有的物料批
    * @author: Deng Xu
    * @date 2020/6/5 14:18
    * @param tenantId 租户ID
    * @param scanContainerId 扫描的容器ID
    * @param scanContainerCode 扫描的容器编码（主要用于报错信息）
    * @return : java.util.List<com.ruike.hme.domain.vo.HmeLoadContainerVO>
    * @version 1.0
    */
    private  List<HmeLoadContainerVO> getLoadMaterialLot(Long tenantId , String scanContainerId , String scanContainerCode ){
        //调用API【containerLimitMaterialLotQuery】通过容器条码获取装载当前容器下所有的物料批
        MtContLoadDtlVO10 contLoadDtlVO = new MtContLoadDtlVO10();
        contLoadDtlVO.setContainerId(scanContainerId);
        contLoadDtlVO.setAllLevelFlag(HmeConstants.ConstantValue.YES);
        List<MtContLoadDtlVO4> loadDetailList = mtContainerLoadDetailRepository.containerLimitMaterialLotQuery(tenantId,
                contLoadDtlVO);
        if(CollectionUtils.isEmpty(loadDetailList)){
            return Collections.EMPTY_LIST;
        }
        //可能获取到多个物料批：【materialLotId】
        if(CollectionUtils.isEmpty(loadDetailList)){
            throw new MtException("HME_LOAD_CONTAINER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_003", "HME"));
        }
        //获取物料批ID集合
        List<String> materialLotIdList =loadDetailList.stream().map(MtContLoadDtlVO4::getMaterialLotId).distinct().collect(toList());
        if(CollectionUtils.isEmpty(materialLotIdList)){
            throw new MtException("HME_LOAD_CONTAINER_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_003", "HME"));
        }
        //可能获取到多个物料批ID，根据物料批ID获取物料批明细
        return mapper.queryOuterContainerLoadDetail(scanContainerCode,materialLotIdList);
    }

    /**
    * @Description: 批量完工装箱-卸载容器/物料批
    * @author: Deng Xu
    * @date 2020/6/5 11:37
    * @param tenantId 租户ID
    * @param loadContainer 待装箱条码信息VO
    * @return : void
    * @version 1.0
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unloadContainer(Long tenantId, HmeLoadContainerVO loadContainer) {
        if(CollectionUtils.isEmpty(loadContainer.getContainerDtlList())){
            throw new MtException("HME_LOAD_CONTAINER_015", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_015", "HME"));
        }
        for(HmeLoadContainerVO  loadContainerVO: loadContainer.getContainerDtlList()){
            //如果是容器类型
            if(HmeConstants.LoadTypeCode.CONTAINER.equals(loadContainerVO.getCodeType())){
                //当未传容器ID时报错：暂无需要卸载容器或物料批
                if (StringUtils.isEmpty(loadContainerVO.getContainerId())) {
                    throw new MtException("HME_LOAD_CONTAINER_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LOAD_CONTAINER_005", "HME"));
                }
                //查询容器下条码 校验条码状态
                List<HmeLoadContainerVO> loadMaterialLotList = this.getLoadMaterialLot(tenantId, loadContainerVO.getContainerId(), loadContainerVO.getContainerCode());
                Optional<HmeLoadContainerVO> scannedFirst = loadMaterialLotList.stream().filter(dto -> StringUtils.equals(dto.getMaterialLotStatus(), HmeConstants.ConstantValue.SCANNED)).findFirst();
                if (scannedFirst.isPresent()) {
                    throw new MtException("HME_LOAD_CONTAINER_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LOAD_CONTAINER_016", "HME", scannedFirst.get().getMaterialLotCode()));
                }

                MtContainerVO25 mtContainerVo25 = new MtContainerVO25();
                mtContainerVo25.setContainerId(loadContainer.getOuterContainerId());
                mtContainerVo25.setLoadObjectId(loadContainerVO.getContainerId());
                mtContainerVo25.setLoadObjectType(HmeConstants.LoadTypeCode.CONTAINER);
                mtContainerRepository.containerUnload(tenantId, mtContainerVo25);
            }
            //如果为物料批类型
            else if(HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(loadContainerVO.getCodeType())) {
                //当未传物料批ID时报错：暂无需要卸载容器或物料批
                if (StringUtils.isEmpty(loadContainerVO.getMaterialLotId())) {
                    throw new MtException("HME_LOAD_CONTAINER_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LOAD_CONTAINER_005", "HME"));
                }
                //查询容器下条码 校验条码状态
                List<HmeLoadContainerVO> loadMaterialLotList = mapper.queryOuterContainerLoadDetail("", Collections.singletonList(loadContainerVO.getMaterialLotId()));
                Optional<HmeLoadContainerVO> scannedFirst = loadMaterialLotList.stream().filter(dto -> StringUtils.equals(dto.getMaterialLotStatus(), HmeConstants.ConstantValue.SCANNED)).findFirst();
                if (scannedFirst.isPresent()) {
                    throw new MtException("HME_LOAD_CONTAINER_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LOAD_CONTAINER_016", "HME", scannedFirst.get().getMaterialLotCode()));
                }

                MtContainerVO25 mtContainerVo25 = new MtContainerVO25();
                mtContainerVo25.setContainerId(loadContainer.getOuterContainerId());
                mtContainerVo25.setLoadObjectId(loadContainerVO.getMaterialLotId());
                mtContainerVo25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                mtContainerRepository.containerUnload(tenantId, mtContainerVo25);
            }
            else{
                throw new MtException("HME_LOAD_CONTAINER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LOAD_CONTAINER_013", "HME"));
            }
        }
    }

    /**
    * @Description: 批量完工装箱-容器/物料批装箱
    * @author: Deng Xu
    * @date 2020/6/5 16:09
    * @param tenantId 租户ID
    * @param loadContainerVO 待装箱容器/物料批、外箱ID
    * @return : void
    * @version 1.0
    */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void loadContainer(Long tenantId , HmeLoadContainerVO loadContainerVO) {
        //校验外箱条码：请输入或扫描外箱条码
        if(StringUtils.isEmpty(loadContainerVO.getOuterContainerId())){
            throw new MtException("HME_LOAD_CONTAINER_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_011", "HME"));
        }
        //校验待装箱条码：请输入或扫描待装箱条码
        if(CollectionUtils.isEmpty(loadContainerVO.getContainerDtlList())){
            throw new MtException("HME_LOAD_CONTAINER_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_LOAD_CONTAINER_012", "HME"));
        }
        String outerContainerId = loadContainerVO.getOuterContainerId();
        //根据容器ID和物料批ID去重
        List<HmeLoadContainerVO> containerDetailList = loadContainerVO.getContainerDtlList()
                .stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                -> new TreeSet<>(Comparator.comparing(o -> o.getContainerId() + ";" + o.getMaterialLotId()))), ArrayList::new));
        //每一条待装箱条码进行处理
        List<MtContainerVO9> containerVO9List = new ArrayList<>();

        MtContainerVO30 mtContainerVO30 = new MtContainerVO30();
        List<MtContainerVO31> mtContainerVO31List = new ArrayList<>();
        for(HmeLoadContainerVO containerDetail: containerDetailList){
            if(StringUtils.equals(containerDetail.getScanFlag(), "Y")){
                //获取每一条的装箱条码的类型，获取不到时报错：获取待装箱条码类型失败
                if(StringUtils.isEmpty(containerDetail.getCodeType())){
                    throw new MtException("HME_LOAD_CONTAINER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LOAD_CONTAINER_013", "HME"));
                }
                //如果是容器条码
                String loadObjectType = "";
                String loadContainerId = "";
                if(HmeConstants.LoadTypeCode.CONTAINER.equals(containerDetail.getCodeType())){
                    if(StringUtils.isEmpty(containerDetail.getContainerId())){
                        throw new MtException("HME_LOAD_CONTAINER_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_LOAD_CONTAINER_012", "HME"));
                    }
                    loadObjectType = HmeConstants.LoadTypeCode.CONTAINER;
                    loadContainerId = containerDetail.getContainerId();
                }
                //如果是物料批条码
                else if(HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(containerDetail.getCodeType())){
                    if(StringUtils.isEmpty(containerDetail.getMaterialLotId())){
                        throw new MtException("HME_LOAD_CONTAINER_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_LOAD_CONTAINER_012", "HME"));
                    }
                    loadObjectType = HmeConstants.LoadTypeCode.MATERIAL_LOT;
                    loadContainerId = containerDetail.getMaterialLotId();
                }
                else{
                    throw new MtException("HME_LOAD_CONTAINER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LOAD_CONTAINER_013", "HME"));
                }

                MtContainerVO9 mtContainerVo9 = new MtContainerVO9();
                mtContainerVo9.setContainerId(outerContainerId);
                mtContainerVo9.setLoadObjectType(loadObjectType);
                mtContainerVo9.setLoadObjectId(loadContainerId);
                containerVO9List.add(mtContainerVo9);

                MtContainerVO31 containerVO31 = new MtContainerVO31();
                containerVO31.setContainerId(outerContainerId);
                containerVO31.setLoadObjectType(loadObjectType);
                containerVO31.setLoadObjectId(loadContainerId);
                mtContainerVO31List.add(containerVO31);
            }
        }
        mtContainerVO30.setContainerLoadList(mtContainerVO31List);
        //批量装箱
        this.batchLoadContainerOrMaterialLot(tenantId, containerVO9List, mtContainerVO30);
    }

    @Override
    public HmeLoadContainerVO verifyContainer(Long tenantId, HmeLoadContainerVO loadContainerVO) {
        loadContainerVO.setVerifyFlag(HmeConstants.ConstantValue.YES);
        if(StringUtils.isNotBlank(loadContainerVO.getOuterContainerId())) {
            MtMaterialLot queryMaterialLot = new MtMaterialLot();
            queryMaterialLot.setTenantId(tenantId);
            queryMaterialLot.setMaterialLotCode(loadContainerVO.getContainerCode());
            queryMaterialLot = mtMaterialLotRepository.selectOne(queryMaterialLot);

            if (null != queryMaterialLot && StringUtils.isNotEmpty(queryMaterialLot.getMaterialLotId())) {
                //物料批条码
                loadContainerVO.setCodeType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                loadContainerVO.setMaterialLotId(queryMaterialLot.getMaterialLotId());
                loadContainerVO.setMaterialLotCode(queryMaterialLot.getMaterialLotCode());

                //调用获取指定对象所在的容器API【objectLimitLoadingContainerQuery】
                MtContLoadDtlVO5 contLoadDtlVo5 = new MtContLoadDtlVO5();
                contLoadDtlVo5.setLoadObjectId(queryMaterialLot.getMaterialLotId());
                contLoadDtlVo5.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                contLoadDtlVo5.setTopLevelFlag(HmeConstants.ConstantValue.NO);
                List<String> containerIdList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, contLoadDtlVo5);
                if (CollectionUtils.isNotEmpty(containerIdList)) {
                    Boolean flag = false;
                    String originalContainerId = "";
                    for (String containerId : containerIdList) {
                        if (!StringUtils.equals(containerId, loadContainerVO.getOuterContainerId())) {
                            flag = true;
                            originalContainerId = containerId;
                        }
                    }

                    if (flag) {
                        loadContainerVO.setVerifyFlag(HmeConstants.ConstantValue.NO);

                        MtContainer mtContainer = mtContainerRepository.selectByPrimaryKey(originalContainerId);
                        loadContainerVO.setOrgContainerId(originalContainerId);
                        loadContainerVO.setOuterContainerCode(mtContainer != null ? mtContainer.getContainerCode() : "");
                    }
                }
            } else {
                MtContainer queryContainer = new MtContainer();
                queryContainer.setTenantId(tenantId);
                queryContainer.setContainerCode(loadContainerVO.getContainerCode());
                queryContainer = mtContainerRepository.selectOne(queryContainer);

                if (null != queryContainer && StringUtils.isNotEmpty(queryContainer.getContainerId())) {
                    //容器条码
                    loadContainerVO.setCodeType(HmeConstants.LoadTypeCode.CONTAINER);
                    loadContainerVO.setContainerId(queryContainer.getContainerId());
                    loadContainerVO.setContainerCode(queryContainer.getContainerCode());

                    //调用获取指定对象所在的容器API【objectLimitLoadingContainerQuery】
                    MtContLoadDtlVO5 contLoadDtlVo5 = new MtContLoadDtlVO5();
                    contLoadDtlVo5.setLoadObjectId(queryContainer.getContainerId());
                    contLoadDtlVo5.setLoadObjectType(HmeConstants.LoadTypeCode.CONTAINER);
                    contLoadDtlVo5.setTopLevelFlag(HmeConstants.ConstantValue.NO);
                    List<String> containerIdList = mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, contLoadDtlVo5);

                    if (CollectionUtils.isNotEmpty(containerIdList)) {
                        Boolean flag = false;
                        String originalContainerId = "";
                        for (String containerId : containerIdList) {
                            if (!StringUtils.equals(containerId, loadContainerVO.getOuterContainerId())) {
                                flag = true;
                                originalContainerId = containerId;
                            }
                        }

                        if (flag) {
                            loadContainerVO.setVerifyFlag(HmeConstants.ConstantValue.NO);

                            MtContainer mtContainer = mtContainerRepository.selectByPrimaryKey(originalContainerId);
                            loadContainerVO.setOrgContainerId(originalContainerId);
                            loadContainerVO.setOuterContainerCode(mtContainer != null ? mtContainer.getContainerCode() : "");
                        }
                    }
                }else {
                    throw new MtException("HME_LOAD_CONTAINER_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_LOAD_CONTAINER_006", "HME"));
                }
            }
        }
        return loadContainerVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeLoadContainerVO unloadOriginalContainer(Long tenantId, HmeLoadContainerVO loadContainerVO) {
        String loadObjectType = "";
        String loadContainerId = "";
        if(HmeConstants.LoadTypeCode.CONTAINER.equals(loadContainerVO.getCodeType())) {
            loadObjectType = HmeConstants.LoadTypeCode.CONTAINER;
            loadContainerId = loadContainerVO.getContainerId();
            //查询容器下条码 校验条码状态
            List<HmeLoadContainerVO> loadMaterialLotList = this.getLoadMaterialLot(tenantId, loadContainerId, loadContainerVO.getContainerCode());
            Optional<HmeLoadContainerVO> scannedFirst = loadMaterialLotList.stream().filter(dto -> StringUtils.equals(dto.getMaterialLotStatus(), HmeConstants.ConstantValue.SCANNED)).findFirst();
            if (scannedFirst.isPresent()) {
                throw new MtException("HME_LOAD_CONTAINER_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LOAD_CONTAINER_016", "HME", scannedFirst.get().getMaterialLotCode()));
            }
        }else if(HmeConstants.LoadTypeCode.MATERIAL_LOT.equals(loadContainerVO.getCodeType())) {
            loadObjectType = HmeConstants.LoadTypeCode.MATERIAL_LOT;
            loadContainerId = loadContainerVO.getMaterialLotId();
            //查询容器下条码 校验条码状态
            List<HmeLoadContainerVO> loadMaterialLotList = mapper.queryOuterContainerLoadDetail("", Collections.singletonList(loadContainerId));
            Optional<HmeLoadContainerVO> scannedFirst = loadMaterialLotList.stream().filter(dto -> StringUtils.equals(dto.getMaterialLotStatus(), HmeConstants.ConstantValue.SCANNED)).findFirst();
            if (scannedFirst.isPresent()) {
                throw new MtException("HME_LOAD_CONTAINER_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_LOAD_CONTAINER_016", "HME", scannedFirst.get().getMaterialLotCode()));
            }
        }

        MtContainerVO25 mtContainerVo25 = new MtContainerVO25();
        mtContainerVo25.setContainerId(loadContainerVO.getOrgContainerId());
        mtContainerVo25.setLoadObjectId(loadContainerId);
        mtContainerVo25.setLoadObjectType(loadObjectType);
        mtContainerRepository.containerUnload(tenantId, mtContainerVo25);

        // 容器装载验证
        MtContainerVO9 mtContainerVo9 = new MtContainerVO9();
        mtContainerVo9.setContainerId(loadContainerVO.getOuterContainerId());
        mtContainerVo9.setLoadObjectType(loadObjectType);
        mtContainerVo9.setLoadObjectId(loadContainerId);
        mtContainerRepository.containerLoadVerify(tenantId, mtContainerVo9);
        return loadContainerVO;
    }


    /**
    * @Description: 进行容器/物料批装载
    * @author: Deng Xu
    * @date 2020/6/5 17:16
    * @param tenantId 租户ID
    * @param outerContainerId 外箱ID
    * @param loadContainerId 待装载对象ID
    * @param loadObjectType 装载对象类型（MATERIAL_LOT/CONTAINER）
    * @return : void
    * @version 1.0
    */
    private void loadContainerOrMaterialLot( Long tenantId ,String outerContainerId , String loadContainerId , String loadObjectType ) {
        //先调用容器装载验证API【containerLoadVerify】
        MtContainerVO9 mtContainerVo9 = new MtContainerVO9();
        mtContainerVo9.setContainerId(outerContainerId);
        mtContainerVo9.setLoadObjectType(loadObjectType);
        mtContainerVo9.setLoadObjectId(loadContainerId);
        mtContainerRepository.containerLoadVerify(tenantId, mtContainerVo9);
        //验证通过后再调用容器装载API【containerLoad】
        MtContainerVO24 mtContainerVo24 = new MtContainerVO24();
        mtContainerVo24.setContainerId(outerContainerId);
        mtContainerVo24.setLoadObjectType(loadObjectType);
        mtContainerVo24.setLoadObjectId(loadContainerId);
        mtContainerRepository.containerLoad(tenantId, mtContainerVo24);
    }

    /**
     * 批量装箱
     *
     * @param tenantId          租户id
     * @param containerVO9List  校验VO
     * @param containerVO30     装载实体
     * @author sanfeng.zhang@hand-china.com 2020/8/27 1:14
     * @return void
     */
    private void batchLoadContainerOrMaterialLot( Long tenantId , List<MtContainerVO9> containerVO9List, MtContainerVO30 containerVO30) {
        //先调用容器装载验证API【containerLoadVerify】
        for (MtContainerVO9 mtContainerVo9 : containerVO9List) {
            mtContainerRepository.containerLoadVerify(tenantId, mtContainerVo9);
        }

        //验证通过后再调用容器装载API【containerBatchLoad】
        if(containerVO30 != null){
            mtContainerRepository.containerBatchLoad(tenantId, containerVO30);
        }
    }
}
