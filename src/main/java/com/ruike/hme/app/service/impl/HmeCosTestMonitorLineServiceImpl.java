package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosTestMonitorRequestOneDTO;
import com.ruike.hme.app.service.HmeCosTestMonitorLineService;
import com.ruike.hme.domain.entity.HmeCosTestMonitorHeader;
import com.ruike.hme.domain.entity.HmeCosTestMonitorLine;
import com.ruike.hme.domain.repository.HmeCosTestMonitorHeaderRepository;
import com.ruike.hme.domain.repository.HmeCosTestMonitorLineRepository;
import com.ruike.hme.domain.vo.HmeCosTestMonitorLineVO;
import com.ruike.hme.domain.vo.HmeCosTestMonitorResponseVO;
import com.ruike.hme.infra.mapper.HmeCosTestMonitorHeaderMapper;
import com.ruike.hme.infra.mapper.HmeCosTestMonitorLineMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.JsonUtils;
import com.ruike.itf.api.dto.ItfCosTestMonitorDTO;
import com.ruike.itf.domain.entity.ItfCosMonitorIface;
import com.ruike.itf.domain.repository.ItfCosMonitorIfaceRepository;
import com.ruike.itf.domain.vo.ItfCosMonitorIfaceVO;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfCosMonitorIfaceMapper;
import com.ruike.itf.utils.SendESBConnect;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseAppService;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * COS测试良率监控行表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-16 14:29:13
 */
@Service
public class HmeCosTestMonitorLineServiceImpl extends BaseAppService implements HmeCosTestMonitorLineService {

    private final HmeCosTestMonitorLineRepository hmeCosTestMonitorLineRepository;
    private final HmeCosTestMonitorLineMapper hmeCosTestMonitorLineMapper;
    private final MtUserClient mtUserClient;
    private final MtMaterialLotMapper mtMaterialLotMapper;
    private final HmeCosTestMonitorHeaderRepository hmeCosTestMonitorHeaderRepository;
    private final MtErrorMessageRepository mtErrorMessageRepository;
    private final MtMaterialRepository mtMaterialRepository;
    private final MtMaterialLotRepository mtMaterialLotRepository;
    private final CustomSequence customSequence;
    private final ItfCosMonitorIfaceRepository itfCosMonitorIfaceRepository;
    private final SendESBConnect sendESBConnect;
    private final ItfCosMonitorIfaceMapper itfCosMonitorIfaceMapper;
    private final HmeCosTestMonitorHeaderMapper hmeCosTestMonitorHeaderMapper;

    @Autowired
    public HmeCosTestMonitorLineServiceImpl(HmeCosTestMonitorLineRepository hmeCosTestMonitorLineRepository,
                                            HmeCosTestMonitorLineMapper hmeCosTestMonitorLineMapper,
                                            MtUserClient mtUserClient,
                                            MtMaterialLotMapper mtMaterialLotMapper,
                                            HmeCosTestMonitorHeaderRepository hmeCosTestMonitorHeaderRepository, MtErrorMessageRepository mtErrorMessageRepository, MtMaterialRepository mtMaterialRepository, MtMaterialLotRepository mtMaterialLotRepository, CustomSequence customSequence, ItfCosMonitorIfaceRepository itfCosMonitorIfaceRepository, SendESBConnect sendESBConnect, ItfCosMonitorIfaceMapper itfCosMonitorIfaceMapper, HmeCosTestMonitorHeaderMapper hmeCosTestMonitorHeaderMapper) {
        this.hmeCosTestMonitorLineRepository = hmeCosTestMonitorLineRepository;
        this.hmeCosTestMonitorLineMapper = hmeCosTestMonitorLineMapper;
        this.mtUserClient = mtUserClient;
        this.mtMaterialLotMapper = mtMaterialLotMapper;
        this.hmeCosTestMonitorHeaderRepository = hmeCosTestMonitorHeaderRepository;
        this.mtErrorMessageRepository = mtErrorMessageRepository;
        this.mtMaterialRepository = mtMaterialRepository;
        this.mtMaterialLotRepository = mtMaterialLotRepository;
        this.customSequence = customSequence;
        this.itfCosMonitorIfaceRepository = itfCosMonitorIfaceRepository;
        this.sendESBConnect = sendESBConnect;
        this.itfCosMonitorIfaceMapper = itfCosMonitorIfaceMapper;
        this.hmeCosTestMonitorHeaderMapper = hmeCosTestMonitorHeaderMapper;
    }

    @Override
    @ProcessLovValue
    public Page<HmeCosTestMonitorLineVO> queryCosMonitorLine(Long tenantId, String cosMonitorHeaderId, PageRequest pageRequest) {
        Page<HmeCosTestMonitorLineVO> page = PageHelper.doPage(pageRequest, () -> hmeCosTestMonitorLineMapper.queryCosMonitorLine(tenantId, cosMonitorHeaderId));
        List<Long> userPassId = new ArrayList<>();
        page.getContent().forEach(hmeCosTestMonitorLineVO -> {
            userPassId.add(hmeCosTestMonitorLineVO.getPassBy());

        });
        List<Long> distinctList = userPassId.stream().distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(distinctList)) {
            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, distinctList);
        }
        for (HmeCosTestMonitorLineVO hmeCosTestMonitorLineVO : page.getContent()) {
            //设置放行人姓名
            hmeCosTestMonitorLineVO.setPassByName(userInfoMap.getOrDefault(hmeCosTestMonitorLineVO.getPassBy(), new MtUserInfo()).getRealName());
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeCosTestMonitorLine> saveCosMonitorLine(Long tenantId, List<HmeCosTestMonitorLineVO> hmeCosTestMonitorLineVOList, String cosMonitorHeaderId) {
        List<HmeCosTestMonitorLine> cosTestMonitorLineList = new ArrayList<>();
        List<String> materialLotCodeList = new ArrayList<>();
        List<String> materialLotIdList = new ArrayList<>();
        hmeCosTestMonitorLineVOList.forEach(testMonitorLineVOList -> {
            materialLotCodeList.add(testMonitorLineVOList.getMaterialLotCode());
        });
        List<String> distinctMaterialLotCodeList = materialLotCodeList.stream().distinct().collect(Collectors.toList());
        //批量获取MaterialLotId
        List<MtMaterialLot> materialLotLoadList = mtMaterialLotMapper.selectByCondition(Condition.builder(MtMaterialLot.class)
                .andWhere(Sqls.custom().andEqualTo(MtMaterialLot.FIELD_TENANT_ID, tenantId)
                        .andIn(MtMaterialLot.FIELD_MATERIAL_LOT_CODE, distinctMaterialLotCodeList)).build());
        if (CollectionUtils.isEmpty(materialLotLoadList)) {
            throw new MtException("HME_COS_TEST_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_TEST_005", "HME"));
        }
        //取到所有的material_lot_id
        materialLotLoadList.forEach(mtMaterialLot -> {
            materialLotIdList.add(mtMaterialLot.getMaterialLotId());
        });
        //在扩展表 查询 CosType 和 Wafer 值
        List<HmeCosTestMonitorLineVO> monitorLineVOList = hmeCosTestMonitorLineMapper.queryCosTypeAndWafer(tenantId, materialLotIdList);
        if (CollectionUtils.isNotEmpty(monitorLineVOList)) {
            //monitorLineVOList 转换成map
            Map<String, HmeCosTestMonitorLineVO> monitorLineVOMap = monitorLineVOList.stream().collect(Collectors.toMap(HmeCosTestMonitorLineVO::getMaterialLotCode, t -> t));
            //获取头
            HmeCosTestMonitorHeader hmeCosTestMonitorHeader = new HmeCosTestMonitorHeader();
            hmeCosTestMonitorHeader.setCosMonitorHeaderId(cosMonitorHeaderId);
            HmeCosTestMonitorHeader cosTestMonitorHeader = hmeCosTestMonitorHeaderRepository.selectOne(hmeCosTestMonitorHeader);
            hmeCosTestMonitorLineVOList.forEach(hmeCosTestMonitorLineVO -> {
                if (!StringUtils.equals(cosTestMonitorHeader.getCosType(), monitorLineVOMap.get(hmeCosTestMonitorLineVO.getMaterialLotCode()).getCosTypeValue())
                        || !StringUtils.equals(cosTestMonitorHeader.getWafer(), monitorLineVOMap.get(hmeCosTestMonitorLineVO.getMaterialLotCode()).getWaferNumValue())) {
                    //若不一致，报错“请填写正确的条码！
                    throw new MtException("HME_COS_TEST_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "HME_COS_TEST_005",
                            "HME"));
                }
                if (StringUtils.equals(cosTestMonitorHeader.getCosType(), monitorLineVOMap.get(hmeCosTestMonitorLineVO.getMaterialLotCode()).getCosTypeValue())
                        && StringUtils.equals(cosTestMonitorHeader.getWafer(), monitorLineVOMap.get(hmeCosTestMonitorLineVO.getMaterialLotCode()).getWaferNumValue())) {
                    //新增数据
                    HmeCosTestMonitorLine hmeCosTestMonitorLine = new HmeCosTestMonitorLine();
                    //头表id
                    hmeCosTestMonitorLine.setCosMonitorHeaderId(cosMonitorHeaderId);
                    //设置租户id
                    hmeCosTestMonitorLine.setTenantId(tenantId);
                    //设置MaterialLotId
                    hmeCosTestMonitorLine.setMaterialLotId(monitorLineVOMap.get(hmeCosTestMonitorLineVO.getMaterialLotCode()).getMaterialLotId());
                    //盒子审核状态
                    hmeCosTestMonitorLine.setLineCheckStatus(cosTestMonitorHeader.getCheckStatus());
                    //盒子状态
                    hmeCosTestMonitorLine.setMaterialLotStatus(cosTestMonitorHeader.getDocStatus());
                    //新增数据
                    hmeCosTestMonitorLineRepository.insertSelective(hmeCosTestMonitorLine);
                    cosTestMonitorLineList.add(hmeCosTestMonitorLine);
                }
            });
        }
        return cosTestMonitorLineList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosTestMonitorResponseVO passMonitorLine(Long tenantId, List<HmeCosTestMonitorLineVO> hmeCosTestMonitorLineVO, String cosMonitorHeaderId) {
        String cid = customSequence.getNextKey("itf_cos_monitor_iface_cid_s");
        HmeCosTestMonitorResponseVO hmeCosTestMonitorResponseVO = new HmeCosTestMonitorResponseVO();
        hmeCosTestMonitorLineVO.forEach(cosTestMonitorLineVO -> {
            //插入接口表数据
            insertIface(tenantId, cosTestMonitorLineVO, cosMonitorHeaderId, cid);
        });
        //组装数据发送到ESB
        OaCOSTestMonitorInsertRestProxy(tenantId, hmeCosTestMonitorLineVO, cosMonitorHeaderId, cid);
        //返回消息
        hmeCosTestMonitorResponseVO.setSuccess("true");
        hmeCosTestMonitorResponseVO.setCode("");
        hmeCosTestMonitorResponseVO.setMessage("成功");
        return hmeCosTestMonitorResponseVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertIface(Long tenantId, HmeCosTestMonitorLineVO hmeCosTestMonitorLineVO, String cosMonitorHeaderId, String cid) {
        HmeCosTestMonitorHeader hmeCosTestMonitorHeader = new HmeCosTestMonitorHeader();
        hmeCosTestMonitorHeader.setCosMonitorHeaderId(cosMonitorHeaderId);
        //查找头数据
        HmeCosTestMonitorHeader cosTestMonitorHeader = hmeCosTestMonitorHeaderRepository.selectOne(hmeCosTestMonitorHeader);
        MtMaterialLot mtmaterialLot = new MtMaterialLot();
        mtmaterialLot.setMaterialLotCode(hmeCosTestMonitorLineVO.getMaterialLotCode());
        MtMaterialLot materialLot = mtMaterialLotRepository.selectOne(mtmaterialLot);
        MtMaterial mtMaterial = new MtMaterial();
        mtMaterial.setMaterialId(materialLot.getMaterialId());
        //获取material_code和material_name
        MtMaterial material = mtMaterialRepository.selectOne(mtMaterial);
        //当前时间
        Date date = CommonUtils.currentTimeGet();
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //插入接口表数据
        ItfCosMonitorIface itfCosMonitorIface = new ItfCosMonitorIface();
        itfCosMonitorIface.setTenantId(tenantId);
        itfCosMonitorIface.setMonitorDocNum(cosTestMonitorHeader.getMonitorDocNum());
        itfCosMonitorIface.setCosType(cosTestMonitorHeader.getCosType());
        itfCosMonitorIface.setReleaseType("MATERIAL_LOT");
        //设置放行批次
        itfCosMonitorIface.setReleaseLot(Long.valueOf(cid));
        itfCosMonitorIface.setDocStatus("WAIT");
        itfCosMonitorIface.setCheckStatus("CHECKING");
        itfCosMonitorIface.setWafer(cosTestMonitorHeader.getWafer());
        itfCosMonitorIface.setTestQty(cosTestMonitorHeader.getTestQty());
        itfCosMonitorIface.setTestPassRate(cosTestMonitorHeader.getTestPassRate());
        itfCosMonitorIface.setPassDate(date);
        itfCosMonitorIface.setPassBy(userId.toString());
        itfCosMonitorIface.setProcessStatus("N");
        itfCosMonitorIface.setMaterialLotCode(hmeCosTestMonitorLineVO.getMaterialLotCode());
        itfCosMonitorIface.setMaterialCode(material.getMaterialCode());
        itfCosMonitorIface.setMaterialName(material.getMaterialName());
        itfCosMonitorIfaceRepository.insertSelective(itfCosMonitorIface);
    }

    @Override
    public void OaCOSTestMonitorInsertRestProxy(Long tenantId, List<HmeCosTestMonitorLineVO> hmeCosTestMonitorLineVOList, String cosMonitorHeaderId, String cid) {
        ItfCosMonitorIface cosMonitorIface = new ItfCosMonitorIface();
        cosMonitorIface.setReleaseLot(Long.valueOf(cid));
        List<ItfCosMonitorIface> cosMonitorIfaceList = itfCosMonitorIfaceRepository.select(cosMonitorIface);
        List<ItfCosMonitorIfaceVO> itfCosMonitorIfaceList = new ArrayList<>();
        cosMonitorIfaceList.forEach(itfCosMonitorIfaces -> {
            HmeCosTestMonitorHeader hmeCosTestMonitorHeader = new HmeCosTestMonitorHeader();
            MtMaterialLot mtmaterialLot = new MtMaterialLot();
            mtmaterialLot.setMaterialLotCode(itfCosMonitorIfaces.getMaterialLotCode());
            MtMaterialLot materialLot = mtMaterialLotRepository.selectOne(mtmaterialLot);
            MtMaterial mtMaterial = new MtMaterial();
            mtMaterial.setMaterialId(materialLot.getMaterialId());
            //获取material_code和material_name
            MtMaterial material = mtMaterialRepository.selectOne(mtMaterial);
            hmeCosTestMonitorHeader.setCosMonitorHeaderId(cosMonitorHeaderId);
            //查找头数据
            HmeCosTestMonitorHeader cosTestMonitorHeader = hmeCosTestMonitorHeaderRepository.selectOne(hmeCosTestMonitorHeader);
            //当前时间
            Date date = CommonUtils.currentTimeGet();
            // 获取当前用户
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            String loginName = hmeCosTestMonitorLineMapper.queryLoginName(userId);
            if(StringUtils.isNotEmpty(loginName)){
                //插入接口表数据
                ItfCosMonitorIfaceVO itfCosMonitorIface = new ItfCosMonitorIfaceVO();

                itfCosMonitorIface.setTenantId(tenantId);
                itfCosMonitorIface.setMonitorDocNum(cosTestMonitorHeader.getMonitorDocNum());
                itfCosMonitorIface.setReleaseType("MATERIAL_LOT");
                //设置放行批次
                itfCosMonitorIface.setReleaseLot(cosMonitorIfaceList.get(0).getReleaseLot());
                //设置主键
                itfCosMonitorIface.setCosMonitorIfaceId(itfCosMonitorIfaces.getCosMonitorIfaceId());
                itfCosMonitorIface.setDocStatus("WAIT");
                itfCosMonitorIface.setCheckStatus("CHECKING");
                itfCosMonitorIface.setWafer(cosTestMonitorHeader.getWafer());
                itfCosMonitorIface.setTestQty(cosTestMonitorHeader.getTestQty());
                itfCosMonitorIface.setCosType(cosTestMonitorHeader.getCosType());
                itfCosMonitorIface.setPassPassRate(cosTestMonitorHeader.getTestPassRate());
                itfCosMonitorIface.setPassDate(date);
                itfCosMonitorIface.setPassBy(loginName);
                itfCosMonitorIface.setProcessStatus("N");
                itfCosMonitorIface.setMaterialLotCode(itfCosMonitorIfaces.getMaterialLotCode());
                itfCosMonitorIface.setMaterialCode(material.getMaterialCode());
                itfCosMonitorIface.setMaterialName(material.getMaterialName());
                itfCosMonitorIfaceList.add(itfCosMonitorIface);
            }
        });
        //将数据发送给ESB
        if (CollectionUtils.isNotEmpty(itfCosMonitorIfaceList)) {
            List<Map<String, String>> fieldsMap = JsonUtils.toStringMap3(itfCosMonitorIfaceList,ItfCosMonitorIfaceVO.class);
            Map<String, Object> returnData = sendESBConnect.sendEsb(fieldsMap, "mesCosInfo",
                    "HmeCosTestMonitorLineServiceImpl.OaCOSTestMonitorInsertRestProxy",
                    ItfConstant.InterfaceCode.ESB_OA_COS_TEST_MONITOR_SYNC);
            String status = returnData.get("status").toString();
            String message = returnData.get("message").toString();
            if ("S".equals(status)) {
                hmeCosTestMonitorLineVOList.forEach(hmeCosTestMonitorLineVO -> {
                    //当前时间
                    Date date = CommonUtils.currentTimeGet();
                    // 获取当前用户
                    CustomUserDetails curUser = DetailsHelper.getUserDetails();
                    Long userId = curUser == null ? -1L : curUser.getUserId();
                    HmeCosTestMonitorLine hmeCosTestMonitorLine = new HmeCosTestMonitorLine();
                    hmeCosTestMonitorLine.setMaterialLotStatus("WAIT");
                    hmeCosTestMonitorLine.setLineCheckStatus("CHECKING");
                    hmeCosTestMonitorLine.setPassBy(userId);
                    hmeCosTestMonitorLine.setCosMonitorLineId(hmeCosTestMonitorLineVO.getCosMonitorLineId());
                    //行表状态更新
                    hmeCosTestMonitorLineMapper.updateByPrimaryKeySelective(hmeCosTestMonitorLine);
                });
                itfCosMonitorIfaceList.forEach(itfCosMonitorIface -> {
                    ItfCosMonitorIface monitorIfaceOne =new ItfCosMonitorIface();
                    monitorIfaceOne.setCosMonitorIfaceId(itfCosMonitorIface.getCosMonitorIfaceId());
                    //更新接口表
                    monitorIfaceOne.setProcessStatus("S");
                    itfCosMonitorIfaceMapper.updateByPrimaryKeySelective(monitorIfaceOne);
                });
            } else {
                //回滚数据
                hmeCosTestMonitorLineVOList.forEach(hmeCosTestMonitorLineVO -> {
                    HmeCosTestMonitorLine hmeCosTestMonitorLine = new HmeCosTestMonitorLine();
                    hmeCosTestMonitorLine.setMaterialLotStatus(hmeCosTestMonitorLineVO.getMaterialLotStatus());
                    hmeCosTestMonitorLine.setLineCheckStatus(hmeCosTestMonitorLineVO.getLineCheckStatus());
                    hmeCosTestMonitorLine.setCosMonitorLineId(hmeCosTestMonitorLineVO.getCosMonitorLineId());
                    hmeCosTestMonitorLine.setTenantId(tenantId);
                    //行表状态更新
                    hmeCosTestMonitorLineMapper.updateByPrimaryKeySelective(hmeCosTestMonitorLine);
                });
                //更新接口表状态
                itfCosMonitorIfaceList.forEach(itfCosMonitorIface -> {
                    ItfCosMonitorIface monitorIfaceOne =new ItfCosMonitorIface();
                    monitorIfaceOne.setCosMonitorIfaceId(itfCosMonitorIface.getCosMonitorIfaceId());
                    monitorIfaceOne.setProcessStatus(status);
                    monitorIfaceOne.setProcessMessage(message);
                    itfCosMonitorIfaceMapper.updateByPrimaryKeySelective(monitorIfaceOne);
                });
            }

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosTestMonitorResponseVO updateCosMonitorStatus(List<ItfCosTestMonitorDTO> itfCosTestMonitorDTOList) {
        //当前时间
        Date date = CommonUtils.currentTimeGet();
        //根据 esb 接口返回数据 更新数据
        itfCosTestMonitorDTOList.forEach(cosMonitor -> {
            ItfCosMonitorIface itfCosMonitorIface = new ItfCosMonitorIface();
            //设置接口表id
            itfCosMonitorIface.setCosMonitorIfaceId(cosMonitor.getCosMonitorIfaceId());
            //查询接口表
            ItfCosMonitorIface cosMonitorIface = itfCosMonitorIfaceRepository.selectOne(itfCosMonitorIface);
            if (Objects.nonNull(cosMonitorIface)) {
                HmeCosTestMonitorHeader cosTestMonitorHeader = new HmeCosTestMonitorHeader();
                cosTestMonitorHeader.setMonitorDocNum(cosMonitorIface.getMonitorDocNum());
                //根据接口表查询的数据取到getMonitorDocNum 单据号
                HmeCosTestMonitorHeader monitorHeader = hmeCosTestMonitorHeaderRepository.selectOne(cosTestMonitorHeader);
                if (Objects.nonNull(monitorHeader)) {
                    //更新头表状态
                    monitorHeader.setDocStatus(cosMonitor.getDocStatus());
                    monitorHeader.setCheckStatus(cosMonitor.getCheckStatus());
                    monitorHeader.setPassDate(date);
                    hmeCosTestMonitorHeaderMapper.updateByPrimaryKeySelective(monitorHeader);
                }
                MtMaterialLot mtMaterialLot = new MtMaterialLot();
                mtMaterialLot.setMaterialLotCode(cosMonitorIface.getMaterialLotCode());
                //根据materialLotCode在 MtMaterialLot 里查询 materialLotId
                MtMaterialLot materialLot = mtMaterialLotRepository.selectOne(mtMaterialLot);
                if (Objects.nonNull(materialLot)) {
                    HmeCosTestMonitorLine hmeCosTestMonitorLine = new HmeCosTestMonitorLine();
                    hmeCosTestMonitorLine.setMaterialLotId(materialLot.getMaterialLotId());
                    HmeCosTestMonitorLine cosTestMonitorLine = hmeCosTestMonitorLineRepository.selectOne(hmeCosTestMonitorLine);
                    //更新行表状态
                    cosTestMonitorLine.setLineCheckStatus(cosMonitor.getCheckStatus());
                    cosTestMonitorLine.setMaterialLotStatus(cosMonitor.getDocStatus());
                    cosTestMonitorLine.setPassDate(date);
                    hmeCosTestMonitorLineMapper.updateByPrimaryKeySelective(cosTestMonitorLine);
                }
            }
        });
        HmeCosTestMonitorResponseVO hmeCosTestMonitorResponseVO = new HmeCosTestMonitorResponseVO();
        hmeCosTestMonitorResponseVO.setSuccess("true");
        hmeCosTestMonitorResponseVO.setCode("");
        hmeCosTestMonitorResponseVO.setMessage("成功");
        return hmeCosTestMonitorResponseVO;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosTestMonitorResponseVO updateLine(Long tenantId, HmeCosTestMonitorRequestOneDTO hmeCosTestMonitorRequestOneDTO) {
        //当前时间
        Date date = CommonUtils.currentTimeGet();
        //根据 esb 接口返回数据 更新数据
        hmeCosTestMonitorRequestOneDTO.getCosMonitorIfaceIdList().forEach(cosMonitorIfaceId -> {
            ItfCosMonitorIface itfCosMonitorIface = new ItfCosMonitorIface();
            //设置接口表id
            itfCosMonitorIface.setCosMonitorIfaceId(cosMonitorIfaceId);
            //查询接口表
            ItfCosMonitorIface cosMonitorIface = itfCosMonitorIfaceRepository.selectOne(itfCosMonitorIface);
            if (Objects.nonNull(cosMonitorIface)) {
                HmeCosTestMonitorHeader cosTestMonitorHeader = new HmeCosTestMonitorHeader();
                cosTestMonitorHeader.setMonitorDocNum(cosMonitorIface.getMonitorDocNum());
                //根据接口表查询的数据取到getMonitorDocNum 单据号
                HmeCosTestMonitorHeader monitorHeader = hmeCosTestMonitorHeaderRepository.selectOne(cosTestMonitorHeader);
                if (Objects.nonNull(monitorHeader)) {
                    //更新头表状态
                    monitorHeader.setDocStatus(hmeCosTestMonitorRequestOneDTO.getDocStatus());
                    monitorHeader.setCheckStatus(hmeCosTestMonitorRequestOneDTO.getCheckStatus());
                    monitorHeader.setPassDate(date);
                    hmeCosTestMonitorHeaderMapper.updateByPrimaryKeySelective(monitorHeader);
                }
                MtMaterialLot mtMaterialLot = new MtMaterialLot();
                mtMaterialLot.setMaterialLotCode(cosMonitorIface.getMaterialLotCode());
                //根据materialLotCode在 MtMaterialLot 里查询 materialLotId
                MtMaterialLot materialLot = mtMaterialLotRepository.selectOne(mtMaterialLot);
                if (Objects.nonNull(materialLot)) {
                    HmeCosTestMonitorLine hmeCosTestMonitorLine = new HmeCosTestMonitorLine();
                    hmeCosTestMonitorLine.setMaterialLotId(materialLot.getMaterialLotId());
                    HmeCosTestMonitorLine cosTestMonitorLine = hmeCosTestMonitorLineRepository.selectOne(hmeCosTestMonitorLine);
                    //更新行表状态
                    cosTestMonitorLine.setLineCheckStatus(hmeCosTestMonitorRequestOneDTO.getCheckStatus());
                    cosTestMonitorLine.setMaterialLotStatus(hmeCosTestMonitorRequestOneDTO.getDocStatus());
                    cosTestMonitorLine.setPassDate(date);
                    hmeCosTestMonitorLineMapper.updateByPrimaryKeySelective(cosTestMonitorLine);
                }
            }
        });
        HmeCosTestMonitorResponseVO hmeCosTestMonitorResponseVO = new HmeCosTestMonitorResponseVO();
        hmeCosTestMonitorResponseVO.setSuccess("true");
        hmeCosTestMonitorResponseVO.setCode("");
        hmeCosTestMonitorResponseVO.setMessage("成功");
        return hmeCosTestMonitorResponseVO;
    }


}
