package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosTestMonitorHeaderDTO;
import com.ruike.hme.app.service.HmeCosTestMonitorHeaderService;
import com.ruike.hme.domain.entity.HmeCosTestMonitorHeader;
import com.ruike.hme.domain.repository.HmeCosTestMonitorHeaderRepository;
import com.ruike.hme.domain.vo.HmeCosTestMonitorHeaderVO;
import com.ruike.hme.domain.vo.HmeCosTestMonitorResponseVO;
import com.ruike.hme.infra.mapper.HmeCosTestMonitorHeaderMapper;
import com.ruike.hme.infra.mapper.HmeCosTestMonitorLineMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.JsonUtils;
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
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * COS测试良率监控头表应用服务默认实现
 *
 * @author wengang.qiang@hand-china.com 2021-09-16 14:29:12
 */
@Service
public class HmeCosTestMonitorHeaderServiceImpl extends BaseAppService implements HmeCosTestMonitorHeaderService {

    private final HmeCosTestMonitorHeaderMapper hmeCosTestMonitorHeaderMapper;
    private final MtUserClient mtUserClient;
    private final CustomSequence customSequence;
    private final SendESBConnect sendESBConnect;
    private final ItfCosMonitorIfaceRepository itfCosMonitorIfaceRepository;
    private final HmeCosTestMonitorHeaderRepository hmeCosTestMonitorHeaderRepository;
    private final ItfCosMonitorIfaceMapper itfCosMonitorIfaceMapper;
    private final HmeCosTestMonitorLineMapper hmeCosTestMonitorLineMapper;


    public HmeCosTestMonitorHeaderServiceImpl(HmeCosTestMonitorHeaderMapper hmeCosTestMonitorHeaderMapper,
                                              MtUserClient mtUserClient, CustomSequence customSequence, SendESBConnect sendESBConnect, ItfCosMonitorIfaceRepository itfCosMonitorIfaceRepository, HmeCosTestMonitorHeaderRepository hmeCosTestMonitorHeaderRepository, ItfCosMonitorIfaceMapper itfCosMonitorIfaceMapper, HmeCosTestMonitorLineMapper hmeCosTestMonitorLineMapper) {
        this.hmeCosTestMonitorHeaderMapper = hmeCosTestMonitorHeaderMapper;
        this.mtUserClient = mtUserClient;
        this.customSequence = customSequence;
        this.sendESBConnect = sendESBConnect;
        this.itfCosMonitorIfaceRepository = itfCosMonitorIfaceRepository;
        this.hmeCosTestMonitorHeaderRepository = hmeCosTestMonitorHeaderRepository;
        this.itfCosMonitorIfaceMapper = itfCosMonitorIfaceMapper;
        this.hmeCosTestMonitorLineMapper = hmeCosTestMonitorLineMapper;
    }

    @Override
    @ProcessLovValue
    public Page<HmeCosTestMonitorHeaderVO> queryCosTestMonitorHeader(Long tenantId, HmeCosTestMonitorHeaderDTO dto, PageRequest pageRequest) {
        Page<HmeCosTestMonitorHeaderVO> page = PageHelper.doPage(pageRequest, () -> hmeCosTestMonitorHeaderMapper.queryCosTestMonitorHeader(tenantId, dto));
        List<Long> userId = new ArrayList<>();
        page.getContent().forEach(hmeCosTestMonitorHeaderVO -> {
            //获取放行人id
            userId.add(hmeCosTestMonitorHeaderVO.getPassBy());
        });
        List<Long> distinctList = userId.stream().distinct().collect(Collectors.toList());
        Map<Long, MtUserInfo> userInfoMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(distinctList)) {
            userInfoMap = mtUserClient.userInfoBatchGet(tenantId, distinctList);
        }
        for (HmeCosTestMonitorHeaderVO hmeCosTestMonitorHeaderVO : page.getContent()) {
            //拿到放行人姓名
            hmeCosTestMonitorHeaderVO.setPassByName(userInfoMap.getOrDefault(hmeCosTestMonitorHeaderVO.getPassBy(), new MtUserInfo()).getRealName());
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeCosTestMonitorResponseVO passWafer(Long tenantId, List<HmeCosTestMonitorHeaderVO> hmeCosTestMonitorHeaderVOList) {
        String cid = customSequence.getNextKey("itf_cos_monitor_iface_cid_s");
        HmeCosTestMonitorResponseVO hmeCosTestMonitorResponseVO = new HmeCosTestMonitorResponseVO();
        //当前时间
        Date date = CommonUtils.currentTimeGet();
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        hmeCosTestMonitorHeaderVOList.forEach(hmeCosTestMonitorHeaderVO -> {
            //数据插入到接口表
            insertIface(tenantId, hmeCosTestMonitorHeaderVO, cid);
        });
        //发送数据给ESB
        OaCOSTestMonitorInsertRestProxy(tenantId, hmeCosTestMonitorHeaderVOList, cid);
        hmeCosTestMonitorResponseVO.setSuccess("true");
        hmeCosTestMonitorResponseVO.setCode("");
        hmeCosTestMonitorResponseVO.setMessage("成功");
        return hmeCosTestMonitorResponseVO;

    }

    @Override
    public void OaCOSTestMonitorInsertRestProxy(Long tenantId, List<HmeCosTestMonitorHeaderVO> hmeCosTestMonitorHeaderVOList, String cid) {
        List<ItfCosMonitorIfaceVO> cosMonitorIfaceList = new ArrayList<>();
        ItfCosMonitorIface cosMonitorIface = new ItfCosMonitorIface();
        cosMonitorIface.setReleaseLot(Long.valueOf(cid));
        ItfCosMonitorIface monitorIface = itfCosMonitorIfaceRepository.selectOne(cosMonitorIface);
        //当前时间
        Date date = CommonUtils.currentTimeGet();
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        hmeCosTestMonitorHeaderVOList.forEach(hmeCosTestMonitorHeaderVO -> {
            List<HmeCosTestMonitorHeaderVO> testMonitorHeaderVOList = hmeCosTestMonitorHeaderMapper.queryAttrByWafer(tenantId, hmeCosTestMonitorHeaderVO.getWafer());
            HmeCosTestMonitorHeaderVO headerVO = new HmeCosTestMonitorHeaderVO();
            if (CollectionUtils.isNotEmpty(testMonitorHeaderVOList)) {
                //获取最近时间的一条数据
                headerVO = testMonitorHeaderVOList.stream().max(Comparator.comparing(HmeCosTestMonitorHeaderVO::getCreationDate)).get();
            }
            //根据扩展名 WORK_ORDER_ID 和 MATERIAL_LOT_ID 去查询扩展表 去查 attr_value值
            String workOrderId = hmeCosTestMonitorHeaderMapper.queryAttrValue(tenantId, headerVO.getMaterialLotId());
            //查询 material_code 和 material_name
            HmeCosTestMonitorHeaderVO testMonitorHeaderVO = hmeCosTestMonitorHeaderMapper.queryCodeAndName(tenantId, workOrderId);
            if (Objects.nonNull(testMonitorHeaderVO)) {
                //查询登录人姓名
                String loginName = hmeCosTestMonitorLineMapper.queryLoginName(userId);
                if(StringUtils.isNotEmpty(loginName)){
                    //插入接口表数据
                    ItfCosMonitorIfaceVO itfCosMonitorIface = new ItfCosMonitorIfaceVO();
                    itfCosMonitorIface.setTenantId(tenantId);
                    itfCosMonitorIface.setMonitorDocNum(hmeCosTestMonitorHeaderVO.getMonitorDocNum());
                    itfCosMonitorIface.setReleaseType("WAFER");
                    //设置放行批次
                    itfCosMonitorIface.setReleaseLot(Long.valueOf(cid));
                    itfCosMonitorIface.setDocStatus("WAIT");
                    itfCosMonitorIface.setCheckStatus("CHECKING");
                    itfCosMonitorIface.setCosMonitorIfaceId(monitorIface.getCosMonitorIfaceId());
                    itfCosMonitorIface.setMaterialCode(testMonitorHeaderVO.getMaterialCode());
                    itfCosMonitorIface.setCosType(hmeCosTestMonitorHeaderVO.getCosType());
                    itfCosMonitorIface.setMaterialName(testMonitorHeaderVO.getMaterialName());
                    itfCosMonitorIface.setWafer(hmeCosTestMonitorHeaderVO.getWafer());
                    itfCosMonitorIface.setTestQty(hmeCosTestMonitorHeaderVO.getTestQty());
                    itfCosMonitorIface.setPassPassRate(hmeCosTestMonitorHeaderVO.getTestPassRate());
                    itfCosMonitorIface.setPassDate(date);
                    itfCosMonitorIface.setPassBy(loginName);
                    itfCosMonitorIface.setMaterialLotCode("");
                    itfCosMonitorIface.setProcessStatus("N");
                    cosMonitorIfaceList.add(itfCosMonitorIface);
                }
            }
        });
        //将数据发送给ESB
        List<Map<String, String>> fieldsMap = JsonUtils.toStringMap3(cosMonitorIfaceList,ItfCosMonitorIfaceVO.class);
        Map<String, Object> returnData = sendESBConnect.sendEsb(fieldsMap, "mesCosInfo",
                "HmeCosTestMonitorHeaderServiceImpl.OaCOSTestMonitorInsertRestProxy",
                ItfConstant.InterfaceCode.ESB_OA_COS_TEST_MONITOR_SYNC);
        String status = returnData.get("status").toString();
        String message = returnData.get("message").toString();
        if ("S".equals(status)) {
            hmeCosTestMonitorHeaderVOList.forEach(hmeCosTestMonitorHeaderVO -> {
                HmeCosTestMonitorHeader hmeCosTestMonitorHeader = new HmeCosTestMonitorHeader();
                hmeCosTestMonitorHeader.setDocStatus("WAIT");
                hmeCosTestMonitorHeader.setCheckStatus("CHECKING");
                //设置放行人
                hmeCosTestMonitorHeader.setPassBy(userId);
                hmeCosTestMonitorHeader.setTenantId(tenantId);
                //设置主键
                hmeCosTestMonitorHeader.setCosMonitorHeaderId(hmeCosTestMonitorHeaderVO.getCosMonitorHeaderId());
                //更新头表 单据状态 审核状态
                hmeCosTestMonitorHeaderMapper.updateByPrimaryKeySelective(hmeCosTestMonitorHeader);
            });
            //更新接口表状态
            cosMonitorIfaceList.forEach(itfCosMonitorIface -> {
                ItfCosMonitorIface monitorIfaceOne=new ItfCosMonitorIface();
                monitorIfaceOne.setCosMonitorIfaceId(itfCosMonitorIface.getCosMonitorIfaceId());
                monitorIfaceOne.setProcessStatus("S");
                itfCosMonitorIfaceMapper.updateByPrimaryKeySelective(monitorIfaceOne);
            });
        } else {
            hmeCosTestMonitorHeaderVOList.forEach(hmeCosTestMonitorHeaderVO -> {
                //回滚
                HmeCosTestMonitorHeader hmeCosTestMonitorHeader = new HmeCosTestMonitorHeader();
                hmeCosTestMonitorHeader.setDocStatus(hmeCosTestMonitorHeaderVO.getDocStatus());
                hmeCosTestMonitorHeader.setCheckStatus(hmeCosTestMonitorHeaderVO.getCheckStatus());
                hmeCosTestMonitorHeader.setTenantId(tenantId);
                //设置主键
                hmeCosTestMonitorHeader.setCosMonitorHeaderId(hmeCosTestMonitorHeaderVO.getCosMonitorHeaderId());
                //更新头表 单据状态 审核状态
                hmeCosTestMonitorHeaderMapper.updateByPrimaryKeySelective(hmeCosTestMonitorHeader);
            });
            //更新接口表状态
            cosMonitorIfaceList.forEach(itfCosMonitorIface -> {
                ItfCosMonitorIface monitorIfaceOne=new ItfCosMonitorIface();
                monitorIfaceOne.setCosMonitorIfaceId(itfCosMonitorIface.getCosMonitorIfaceId());
                monitorIfaceOne.setProcessStatus(status);
                monitorIfaceOne.setProcessMessage(message);
                itfCosMonitorIfaceMapper.updateByPrimaryKeySelective(monitorIfaceOne);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItfCosMonitorIface insertIface(Long tenantId, HmeCosTestMonitorHeaderVO hmeCosTestMonitorHeaderVO, String cid) {
        //当前时间
        Date date = CommonUtils.currentTimeGet();
        // 获取当前用户
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        List<HmeCosTestMonitorHeaderVO> testMonitorHeaderVOList = hmeCosTestMonitorHeaderMapper.queryAttrByWafer(tenantId, hmeCosTestMonitorHeaderVO.getWafer());
        HmeCosTestMonitorHeaderVO headerVO = new HmeCosTestMonitorHeaderVO();
        if (CollectionUtils.isNotEmpty(testMonitorHeaderVOList)) {
            //获取最近时间的一条数据
            headerVO = testMonitorHeaderVOList.stream().max(Comparator.comparing(HmeCosTestMonitorHeaderVO::getCreationDate)).get();
        }
        //根据扩展名 WORK_ORDER_ID 和 MATERIAL_LOT_ID 去查询扩展表 去查 attr_value值
        String workOrderId = hmeCosTestMonitorHeaderMapper.queryAttrValue(tenantId, headerVO.getMaterialLotId());
        //查询 material_code 和 material_name
        HmeCosTestMonitorHeaderVO testMonitorHeaderVO = hmeCosTestMonitorHeaderMapper.queryCodeAndName(tenantId, workOrderId);
        ItfCosMonitorIface itfCosMonitorIface = new ItfCosMonitorIface();
        if (Objects.nonNull(testMonitorHeaderVO)) {
            //插入接口表数据
            itfCosMonitorIface.setTenantId(tenantId);
            itfCosMonitorIface.setMonitorDocNum(hmeCosTestMonitorHeaderVO.getMonitorDocNum());
            itfCosMonitorIface.setReleaseType("WAFER");
            //设置放行批次
            itfCosMonitorIface.setReleaseLot(Long.valueOf(cid));
            itfCosMonitorIface.setDocStatus("WAIT");
            itfCosMonitorIface.setCheckStatus("CHECKING");
            itfCosMonitorIface.setCosType(hmeCosTestMonitorHeaderVO.getCosType());
            itfCosMonitorIface.setMaterialCode(testMonitorHeaderVO.getMaterialCode());
            itfCosMonitorIface.setMaterialName(testMonitorHeaderVO.getMaterialName());
            itfCosMonitorIface.setWafer(hmeCosTestMonitorHeaderVO.getWafer());
            itfCosMonitorIface.setTestQty(hmeCosTestMonitorHeaderVO.getTestQty());
            itfCosMonitorIface.setTestPassRate(hmeCosTestMonitorHeaderVO.getTestPassRate());
            itfCosMonitorIface.setPassDate(date);
            itfCosMonitorIface.setPassBy(userId.toString());
            itfCosMonitorIface.setMaterialLotCode("");
            itfCosMonitorIface.setProcessStatus("N");
            itfCosMonitorIfaceRepository.insertSelective(itfCosMonitorIface);
        }
        return itfCosMonitorIface;
    }
}
