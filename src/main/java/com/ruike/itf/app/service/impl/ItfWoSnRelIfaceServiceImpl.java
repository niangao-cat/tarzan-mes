package com.ruike.itf.app.service.impl;

import com.ruike.itf.app.service.ItfWoSnRelIfaceService;
import com.ruike.itf.domain.entity.ItfWoSnRelIface;
import com.ruike.itf.domain.repository.ItfWoSnRelIfaceRepository;
import com.ruike.itf.domain.vo.ItfEsbRequestVO;
import com.ruike.itf.domain.vo.ItfEsbResponseVO;
import com.ruike.itf.domain.vo.ItfWoSnRelIfaceVO;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfWoSnRelIfaceMapper;
import com.ruike.itf.infra.util.InterfaceUtils;
import com.ruike.itf.infra.util.JsonUtils;
import com.ruike.itf.utils.Utils;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.interfaces.sdk.dto.RequestPayloadDTO;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.interfaces.sdk.invoke.InterfaceInvokeSdk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tarzan.iface.domain.entity.MtSitePlantReleation;
import tarzan.iface.domain.repository.MtSitePlantReleationRepository;
import tarzan.iface.domain.vo.MtSitePlantReleationVO3;

import java.math.*;
import java.util.*;

/**
 * SAP 工单SN码关系接口应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020/7/21 15:41
 */
@Slf4j
@Service
public class ItfWoSnRelIfaceServiceImpl extends BaseServiceImpl<ItfWoSnRelIface> implements ItfWoSnRelIfaceService {

    private final ItfWoSnRelIfaceRepository itfWoSnRelIfaceRepository;
    private final MtSitePlantReleationRepository mtSitePlantReleationRepository;
    private final MtCustomDbRepository customDbRepository;
    private final ItfWoSnRelIfaceMapper itfWoSnRelIfaceMapper;
    private final InterfaceInvokeSdk interfaceInvokeSdk;

    @Autowired
    public ItfWoSnRelIfaceServiceImpl(ItfWoSnRelIfaceRepository itfWoSnRelIfaceRepository,
                                      MtSitePlantReleationRepository mtSitePlantReleationRepository,
                                      MtCustomDbRepository customDbRepository,
                                      ItfWoSnRelIfaceMapper itfWoSnRelIfaceMapper,
                                      InterfaceInvokeSdk interfaceInvokeSdk) {
        this.itfWoSnRelIfaceRepository = itfWoSnRelIfaceRepository;
        this.mtSitePlantReleationRepository = mtSitePlantReleationRepository;
        this.customDbRepository = customDbRepository;
        this.itfWoSnRelIfaceMapper = itfWoSnRelIfaceMapper;
        this.interfaceInvokeSdk = interfaceInvokeSdk;
    }

    @Value("${hwms.interface.defaultNamespace}")
    private String namespace;

    private static final int SQL_ITEM_COUNT_LIMIT = 1000;

    /**
     *
     * @Description 工单SN码关系同步接口
     *
     * @author yuchao.wang
     * @date 2020/7/30 18:52
     * @param tenantId 租户ID
     *
     */
    @Override
    public void invoke(Long tenantId) {
        //获取用户信息
        Long userId = -1L;
        CustomUserDetails customUserDetails = DetailsHelper.getUserDetails();
        if(Objects.nonNull(customUserDetails)
                && Objects.nonNull(customUserDetails.getUserId())){
            userId = customUserDetails.getUserId();
        }

        //查询所有需要更新的数据
        List<ItfWoSnRelIface> ifaceList = itfWoSnRelIfaceRepository.selectInterfaceData(tenantId);
        if (CollectionUtils.isEmpty(ifaceList)) {
            log.info("<==== ItfWoSnRelIfaceJob requestPayload: 未查询需要同步的数据！");
            return;
        }

        //构造成接口报文主体 筛选需要更新状态的数据
        List<ItfWoSnRelIfaceVO> postList = new ArrayList<>(ifaceList.size());
        List<String> ifaceIdList = new ArrayList<>();
        ifaceList.forEach(iface -> {
            ItfWoSnRelIfaceVO vo = new ItfWoSnRelIfaceVO();
            vo.setAUFNR(iface.getWorkOrderNum());
            vo.setSERNR(iface.getSnNum());
            vo.setWERKS(iface.getPlantCode());
            postList.add(vo);

            if (StringUtils.isNotEmpty(iface.getIfaceId())) {
                ifaceIdList.add(iface.getIfaceId());
            }
        });

        //更新接口表状态为处理中
        if (CollectionUtils.isNotEmpty(ifaceIdList)){
            List<List<String>> splitSqlList = InterfaceUtils.splitSqlList(ifaceIdList, SQL_ITEM_COUNT_LIMIT);
            for (List<String> domains : splitSqlList) {
                itfWoSnRelIfaceMapper.batchUpdateStatus(userId, STATUS_PROCESS, domains);
            }
        }

        //构造接口报文
        ItfEsbRequestVO itfEsbRequestVO = new ItfEsbRequestVO(new Date());
        Map<String, Object> requestInfo = new HashMap<String, Object>();
        requestInfo.put("SERIAL", postList);
        itfEsbRequestVO.setRequestInfo(requestInfo);

        RequestPayloadDTO requestPayload = new RequestPayloadDTO();
        requestPayload.setPayload(JsonUtils.objToJson(itfEsbRequestVO));

        //请求接口
        ResponsePayloadDTO responsePayload = new ResponsePayloadDTO();
        try {
            responsePayload = interfaceInvokeSdk.invoke(namespace,
                    ItfConstant.ServerCode.MES_ESB,
                    ItfConstant.InterfaceCode.ESB_WO_SN_SYNC,
                    requestPayload);
        } catch (Exception e) {
            rollbackIfaceStatus(userId, ifaceList);
            throw e;
        }

        if (Objects.isNull(responsePayload)) {
            throwExceptionWithRollback(userId, ifaceList, "ESB接口调用失败");
        } else if (responsePayload.getStatusCodeValue() != HTTP_STATUS_OK || !HTTP_STATUS_OK.toString().equals(responsePayload.getStatus())) {
            throwExceptionWithRollback(userId, ifaceList, "ESB接口调用失败：" + responsePayload.getMessage());
        }

        //解析返回报文
        ItfEsbResponseVO itfEsbResponseVO = JsonUtils.jsonToObject(responsePayload.getPayload(), ItfEsbResponseVO.class);

        if (Objects.isNull(itfEsbResponseVO)
                || Objects.isNull(itfEsbResponseVO.getEsbInfo()) || Objects.isNull(itfEsbResponseVO.getResultInfo())) {
            if (StringUtils.isEmpty(responsePayload.getPayload())){
                throwExceptionWithRollback(userId, ifaceList, "ESB接口返回结果为空！");
            } else {
                throwExceptionWithRollback(userId, ifaceList, "ESB接口返回报文解析失败！报文内容：" + responsePayload.getPayload());
            }
        }/* 考虑到可能整体返回不为成功但是部分行数据更新成功，这里不进行整体状态判断
         else if (!STATUS_SUCCESS.equals(itfEsbResponseVO.getEsbInfo().getReturnStatus())
                || !ESB_STATUS_SUCCESS.equals(itfEsbResponseVO.getEsbInfo().getReturnCode())) {
            throwExceptionWithRollback(ifaceList, "ESB接口返回失败状态，返回报文：" + itfEsbResponseVO.toString());
        }*/

        //获取SN数据
        List<Map<String, String>> responseInfo = (List<Map<String, String>>) itfEsbResponseVO.getResultInfo().get("RETURN");

        if (CollectionUtils.isEmpty(responseInfo)) {
            throwExceptionWithRollback(userId, ifaceList, "ESB接口返回数据为空！接口返回报文：" + itfEsbResponseVO.toString());
        }

        //批量获取站点工厂关系
        List<String> plantCodeList = new ArrayList<>();
        responseInfo.forEach(item -> {
            if(!plantCodeList.contains(item.get("WERKS"))){
                plantCodeList.add(item.get("WERKS"));
            }
        });
        MtSitePlantReleationVO3 mtSitePlantReleationVO3 = new MtSitePlantReleationVO3();
        mtSitePlantReleationVO3.setPlantCodes(plantCodeList);
        mtSitePlantReleationVO3.setSiteType("MANUFACTURING");
        List<MtSitePlantReleation> relationByPlantAndSiteTypes = mtSitePlantReleationRepository
                .getRelationByPlantAndSiteType(tenantId, mtSitePlantReleationVO3);

        if (CollectionUtils.isEmpty(relationByPlantAndSiteTypes)) {
            throwExceptionWithRollback(userId, ifaceList, "未查询到站点工厂关系数据！");
        }

        Map<String, String> plantAndSiteRelationMap = new HashMap<String, String>();
        relationByPlantAndSiteTypes.forEach(item -> {
            plantAndSiteRelationMap.put(item.getPlantCode(), item.getSiteId());
        });

        //回写接口表
        Date nowDate = Utils.getNowDate();
        List<ItfWoSnRelIface> insertList = new ArrayList<>();
        List<ItfWoSnRelIface> updateList = new ArrayList<>();
        for (Map<String, String> woSnRelIfaceMap : responseInfo) {
            ItfWoSnRelIface woSnRelIface = new ItfWoSnRelIface();
            String workOrderNum = getRealWorkOrderNum(woSnRelIfaceMap.get("AUFNR"));
            String siteId = plantAndSiteRelationMap.get(woSnRelIfaceMap.get("WERKS"));

            //先查询是否在接口表中存在
            woSnRelIface.setSnNum(woSnRelIfaceMap.get("SERNR"));
            woSnRelIface.setWorkOrderNum(workOrderNum);
            woSnRelIface.setSiteId(siteId);
            woSnRelIface.setTenantId(tenantId);
            woSnRelIface = itfWoSnRelIfaceRepository.queryIfaceForReturnWrite(woSnRelIface);

            //判断是新增还是更新
            boolean addFlag = (Objects.isNull(woSnRelIface) || StringUtils.isEmpty(woSnRelIface.getIfaceId()));

            if (addFlag) {
                //执行新增逻辑

                woSnRelIface = new ItfWoSnRelIface();
                String ifaceId = this.customDbRepository.getNextKey("itf_wo_sn_rel_iface_s");
                woSnRelIface.setSnNum(woSnRelIfaceMap.get("SERNR"));
                woSnRelIface.setWorkOrderNum(workOrderNum);
                woSnRelIface.setSiteId(siteId);
                woSnRelIface.setTenantId(tenantId);
                woSnRelIface.setRetryTime(BigDecimal.ZERO);
                woSnRelIface.setIfaceId(ifaceId);
                woSnRelIface.setObjectVersionNumber(1L);
                woSnRelIface.setCreatedBy(userId);
                woSnRelIface.setCreationDate(nowDate);
                woSnRelIface.setLastUpdatedBy(userId);
                woSnRelIface.setLastUpdateDate(nowDate);

                //判断是否执行成功
                if (STATUS_SUCCESS.equals(woSnRelIfaceMap.get("TYPE"))) {
                    woSnRelIface.setStatus(STATUS_SUCCESS);
                } else {
                    woSnRelIface.setStatus(STATUS_ERROR);
                    woSnRelIface.setMessage(woSnRelIfaceMap.get("MESSAGE"));
                }
                insertList.add(woSnRelIface);
            } else {
                //执行更新逻辑,判断是否执行成功
                ItfWoSnRelIface updateRecrd = new ItfWoSnRelIface();
                updateRecrd.setIfaceId(woSnRelIface.getIfaceId());
                updateRecrd.setRetryTime(Objects.isNull(woSnRelIface.getRetryTime()) ? BigDecimal.ONE : woSnRelIface.getRetryTime().add(BigDecimal.ONE));

                if (STATUS_SUCCESS.equals(woSnRelIfaceMap.get("TYPE"))) {
                    updateRecrd.setStatus(STATUS_SUCCESS);
                    updateRecrd.setMessage("");
                } else {
                    updateRecrd.setStatus(STATUS_ERROR);
                    updateRecrd.setMessage(woSnRelIfaceMap.get("MESSAGE"));
                }
                updateList.add(updateRecrd);
            }
        }

        //执行批量新增更新
        if (CollectionUtils.isNotEmpty(insertList)) {
            List<List<ItfWoSnRelIface>> splitSqlList = InterfaceUtils.splitSqlList(insertList, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfWoSnRelIface> domains : splitSqlList) {
                itfWoSnRelIfaceMapper.batchInsert(domains);
            }
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            List<List<ItfWoSnRelIface>> splitSqlList = InterfaceUtils.splitSqlList(updateList, SQL_ITEM_COUNT_LIMIT);
            for (List<ItfWoSnRelIface> domains : splitSqlList) {
                itfWoSnRelIfaceMapper.batchUpdate(userId, domains);
            }
        }
    }

    /**
     *
     * @Description 将补0的wo_num转为MES系统中的wo_num(去掉补位的0)
     *
     * @author yuchao.wang
     * @date 2020/7/30 18:26
     * @param workOrderNum 工单编码
     * @return java.lang.String
     *
     */
    private String getRealWorkOrderNum(String workOrderNum){
        return workOrderNum.replaceAll("^0*", "");
    }

    /**
     *
     * @Description 更新接口表状态为原有状态,用于异常回滚逻辑
     *
     * @author yuchao.wang
     * @date 2020/7/31 11:19
     * @param userId userId
     * @param ifaceList 接口表数据
     * @return void
     *
     */
    private void throwExceptionWithRollback(Long userId, List<ItfWoSnRelIface> ifaceList, String message){
        rollbackIfaceStatus(userId, ifaceList);

        throw new CommonException(message);
    }

    /**
     *
     * @Description 更新接口表状态为原有状态
     *
     * @author yuchao.wang
     * @date 2020/7/31 13:43
     * @param userId userId
     * @param ifaceList 接口表数据
     * @return void
     *
     */
    private void rollbackIfaceStatus(Long userId, List<ItfWoSnRelIface> ifaceList){
        if (CollectionUtils.isNotEmpty(ifaceList)) {
            List<String> errorIfaceIdList = new ArrayList<>();
            List<String> normalIfaceIdList = new ArrayList<>();

            //查找接口表原有状态
            ifaceList.forEach(iface -> {
                if (StringUtils.isNotEmpty(iface.getIfaceId())) {
                    if (STATUS_ERROR.equals(iface.getStatus())) {
                        errorIfaceIdList.add(iface.getIfaceId());
                    } else if (STATUS_NORMAL.equals(iface.getStatus())) {
                        normalIfaceIdList.add(iface.getIfaceId());
                    } else {
                        //正常情况下只会有E/N两种状态，为了提高容错，暂时不删除默认处理逻辑
                        ItfWoSnRelIface updateIfacd = new ItfWoSnRelIface();
                        updateIfacd.setIfaceId(iface.getIfaceId());
                        updateIfacd.setStatus(iface.getStatus());
                        updateIfacd.setObjectVersionNumber(iface.getObjectVersionNumber() + 1L);
                        itfWoSnRelIfaceMapper.updateByPrimaryKeySelective(updateIfacd);
                    }
                }
            });

            //批量更新接口表状态
            if (CollectionUtils.isNotEmpty(errorIfaceIdList)){
                List<List<String>> splitSqlList = InterfaceUtils.splitSqlList(errorIfaceIdList, SQL_ITEM_COUNT_LIMIT);
                for (List<String> domains : splitSqlList) {
                    itfWoSnRelIfaceMapper.batchUpdateStatus(userId, STATUS_ERROR, domains);
                }
            }
            if (CollectionUtils.isNotEmpty(normalIfaceIdList)){
                List<List<String>> splitSqlList = InterfaceUtils.splitSqlList(normalIfaceIdList, SQL_ITEM_COUNT_LIMIT);
                for (List<String> domains : splitSqlList) {
                    itfWoSnRelIfaceMapper.batchUpdateStatus(userId, STATUS_NORMAL, domains);
                }
            }
        }
    }
}
