package com.ruike.itf.infra.repository.impl;

import com.alibaba.fastjson.JSON;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.domain.vo.ItfMaterialLotConfirmIfaceVO2;
import com.ruike.itf.domain.vo.ItfMaterialLotConfirmIfaceVO4;
import com.ruike.itf.domain.vo.ItfWcsResponseVO;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfMaterialLotConfirmIfaceMapper;
import com.ruike.itf.infra.util.JsonUtils;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.interfaces.sdk.dto.RequestPayloadDTO;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.interfaces.sdk.invoke.InterfaceInvokeSdk;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.itf.domain.entity.ItfMaterialLotConfirmIface;
import com.ruike.itf.domain.repository.ItfMaterialLotConfirmIfaceRepository;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.repository.MtModLocatorRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 立库入库复核接口表 资源库实现
 *
 * @author chaonan.hu@hand-china.com 2021-07-13 19:40:25
 */
@Component
@Slf4j
public class ItfMaterialLotConfirmIfaceRepositoryImpl extends BaseRepositoryImpl<ItfMaterialLotConfirmIface> implements ItfMaterialLotConfirmIfaceRepository {

    @Autowired
    private ItfMaterialLotConfirmIfaceMapper itfMaterialLotConfirmIfaceMapper;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private InterfaceInvokeSdk interfaceInvokeSdk;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Value("${hwms.interface.defaultNamespace}")
    private String namespace;

    private final Integer HTTP_STATUS_OK = 200;

    @Override
    public String insertMaterialLotConfirmIface(Long tenantId, List<String> materialLotIdList) {
        String batchId = null;
        if(CollectionUtils.isNotEmpty(materialLotIdList)){
            //根据物料批Id查询相关信息，将查询数据插入到接口表中
            List<ItfMaterialLotConfirmIface> itfMaterialLotConfirmIfaceList = itfMaterialLotConfirmIfaceMapper.materialLotConfirmIfaceQuery(tenantId, materialLotIdList);
            //如果是已入库货位的条码，将状态改为change
            for(ItfMaterialLotConfirmIface itfMaterialLotConfirmIface: itfMaterialLotConfirmIfaceList){
                MtModLocator mtModLocator = new MtModLocator();
                mtModLocator.setTenantId(tenantId);
                mtModLocator.setLocatorCode(itfMaterialLotConfirmIface.getLocatorCode());
                List<MtModLocator> mtModLocators = mtModLocatorRepository.select(mtModLocator);
                if(CollectionUtils.isNotEmpty(mtModLocators)){
                    if("AUTO".equals(mtModLocators.get(0).getLocatorType())){
                        itfMaterialLotConfirmIface.setStatus("CHANGE");
                    }
                }
            }

            //如果查到数据，执行新增主逻辑，由于主逻辑是个新事务，要先查询出当前事务最新的数据，再开事务新增接口表 modify by yuchao.wang at 2021.8.7
            if(CollectionUtils.isNotEmpty(itfMaterialLotConfirmIfaceList)) {
                batchId = self().insertMaterialLotConfirmIfaceMain(tenantId, itfMaterialLotConfirmIfaceList);
            }
        }
        return batchId;
    }

    @Override
    public ResponsePayloadDTO sendWcs(Object requestMap, String logName, String itfPath) {
        RequestPayloadDTO requestPayload = new RequestPayloadDTO();
        requestPayload.setPayload(JsonUtils.objToJson(requestMap));
        log.info("<==== " + logName + " requestPayload: {}", requestPayload.toString());

        // 请求接口
        ResponsePayloadDTO responsePayload = new ResponsePayloadDTO();
        try {
            responsePayload = interfaceInvokeSdk.invoke(namespace,
                    ItfConstant.ServerCode.MES_WCS,
                    itfPath,
                    requestPayload);
        } catch (Exception e) {
            //rollbackIfaceStatus(userId, ifaceList);
            throw e;
        }

        if (Objects.isNull(responsePayload)) {
            log.error("<==== " + logName + " Error requestPayload: {}WCS接口调用失败", requestPayload.toString());
            throw new CommonException("<==== " + logName + " Error requestPayload: {}ESB接口调用失败", requestPayload.toString());
        } else if (responsePayload.getStatusCodeValue() != HTTP_STATUS_OK || !HTTP_STATUS_OK.toString().equals(responsePayload.getStatus())) {
            log.error("<==== " + logName + " Error requestPayload: {}WCS接口调用失败,服务器不通", requestPayload.toString());
            throw new CommonException("<==== " + logName + " Error requestPayload: {}WCS接口调用失败,服务器不通", requestPayload.toString());
        } else {
            log.info("<==== " + logName + " Success responsePayload: {}", JSON.toJSON(responsePayload));
        }
        return responsePayload;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void updateIfaceData(Long tenantId, List<String> ifaceIdList, String message, Long userId) {
        itfMaterialLotConfirmIfaceMapper.updateIfaceData(tenantId, ifaceIdList, message, userId);
    }

    @Override
    public ItfWcsResponseVO validateResponsePayload(Long tenantId, ResponsePayloadDTO responsePayload, List<String> ifaceIdList, Long userId) {
        ItfWcsResponseVO itfWcsResponseVO = JsonUtils.jsonToObject(responsePayload.getPayload(), ItfWcsResponseVO.class);
        if (Objects.isNull(itfWcsResponseVO)
                || Objects.isNull(itfWcsResponseVO.getHeader())) {
            if (org.apache.commons.lang.StringUtils.isEmpty(responsePayload.getPayload())) {
                log.error("<==== " + "ItfMaterialLotConfirmIface" + " Success WCS {}接口返回结果为空！", responsePayload);
                this.updateIfaceData(tenantId, ifaceIdList, "WCS接口返回结果为空", userId);
                throw new CommonException("WCS接口返回结果为空！报文内容：" + responsePayload.getPayload());
            } else {
                log.error("WCS接口返回报文解析失败！报文内容：" + responsePayload.getPayload());
                this.updateIfaceData(tenantId, ifaceIdList, "WCS接口返回报文解析失败", userId);
                throw new CommonException("WCS接口返回报文解析失败！报文内容：" + responsePayload.getPayload());
            }
        }
        List<ItfMaterialLotConfirmIfaceVO2> header = itfWcsResponseVO.getHeader();
        if(header.size() != ifaceIdList.size()){
            this.updateIfaceData(tenantId, ifaceIdList,
                    "WCS接口返回结果数据个数不一致,MES:" + ifaceIdList.size() + "WCS:" + header.size(),
                    userId);
            throw new CommonException("WCS接口返回数据个数不一致！报文内容：" + responsePayload.getPayload());
        }
        return itfWcsResponseVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<ItfMaterialLotConfirmIfaceVO4> updateIface(Long tenantId, List<ItfMaterialLotConfirmIfaceVO2> header, String batchId, Long userId,
                                                            List<String> ifaceIdList) {
        List<ItfMaterialLotConfirmIfaceVO4> resultList = new ArrayList<>();
        List<ItfMaterialLotConfirmIface> itfMaterialLotConfirmIfaceList = this.selectByCondition(Condition.builder(ItfMaterialLotConfirmIface.class)
                .andWhere(Sqls.custom()
                        .andIn(ItfMaterialLotConfirmIface.FIELD_IFACE_ID, ifaceIdList))
                .build());
        Date nowDate = new Date();
        List<String> sqlList = new ArrayList<>();
        for (ItfMaterialLotConfirmIface iface:itfMaterialLotConfirmIfaceList) {
            List<ItfMaterialLotConfirmIfaceVO2> materialLotConfirmIfaceVO2List = header.stream().filter(item -> iface.getMaterialLotCode().equals(item.getMaterialLotCode())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(materialLotConfirmIfaceVO2List)){
                iface.setProcessStatus(materialLotConfirmIfaceVO2List.get(0).getMsgCode());
                iface.setProcessDate(nowDate);
                iface.setProcessMessage(materialLotConfirmIfaceVO2List.get(0).getMessage());
                iface.setObjectVersionNumber(iface.getObjectVersionNumber() + 1);
                iface.setLastUpdatedBy(userId);
                iface.setLastUpdateDate(nowDate);
                sqlList.addAll(customDbRepository.getUpdateSql(iface));
                if(!"S".equals(iface.getProcessStatus())){
                    ItfMaterialLotConfirmIfaceVO4 itfMaterialLotConfirmIfaceVO4 = new ItfMaterialLotConfirmIfaceVO4();
                    itfMaterialLotConfirmIfaceVO4.setSuccess(false);
                    itfMaterialLotConfirmIfaceVO4.setBarcode(iface.getMaterialLotCode());
                    itfMaterialLotConfirmIfaceVO4.setErrorMessage(iface.getProcessMessage());
                    resultList.add(itfMaterialLotConfirmIfaceVO4);
                }else {
                    ItfMaterialLotConfirmIfaceVO4 itfMaterialLotConfirmIfaceVO4 = new ItfMaterialLotConfirmIfaceVO4();
                    itfMaterialLotConfirmIfaceVO4.setSuccess(true);
                    itfMaterialLotConfirmIfaceVO4.setBarcode(iface.getMaterialLotCode());
                    itfMaterialLotConfirmIfaceVO4.setErrorMessage(iface.getProcessMessage());
                    resultList.add(itfMaterialLotConfirmIfaceVO4);
                }
            }else {
                ItfMaterialLotConfirmIfaceVO4 itfMaterialLotConfirmIfaceVO4 = new ItfMaterialLotConfirmIfaceVO4();
                itfMaterialLotConfirmIfaceVO4.setBarcode(iface.getMaterialLotCode());
                itfMaterialLotConfirmIfaceVO4.setErrorMessage("WCS接口未返回此条码相关报文");
                resultList.add(itfMaterialLotConfirmIfaceVO4);
            }
        }
        if(CollectionUtils.isNotEmpty(sqlList)){
            this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
        }
        return resultList;
    }

    /**
     *
     * @description 根据物料批ID查询数据，并将数据插入到接口表中
     *
     * @param itfMaterialLotConfirmIfaceList 要插入的主数据
     * @author yuchao.wang@hand-china.com
     * @date 2021/8/7 15:03
     * @return String 批次ID
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public String insertMaterialLotConfirmIfaceMain(Long tenantId, List<ItfMaterialLotConfirmIface> itfMaterialLotConfirmIfaceList) {
        String batchId = null;
        if(CollectionUtils.isNotEmpty(itfMaterialLotConfirmIfaceList)){
            //批量获取主键、cid、batchId、当前时间、当前用户
            List<String> ids = this.customDbRepository.getNextKeys("itf_material_lot_confirm_iface_s", itfMaterialLotConfirmIfaceList.size());
            List<String> cIds = this.customDbRepository.getNextKeys("itf_material_lot_confirm_iface_cid_s", itfMaterialLotConfirmIfaceList.size());
            batchId = this.customDbRepository.getNextKey("itf_material_lot_confirm_iface_cid_s");
            Date nowDate = new Date();
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();

            List<String> sqlList = new ArrayList<>();
            int index = 0;
            for (ItfMaterialLotConfirmIface itfMaterialLotConfirmIface:itfMaterialLotConfirmIfaceList) {
                itfMaterialLotConfirmIface.setTenantId(tenantId);
                itfMaterialLotConfirmIface.setIfaceId(ids.get(index));
                itfMaterialLotConfirmIface.setBatchId(Long.valueOf(batchId));
                itfMaterialLotConfirmIface.setProcessDate(nowDate);
                itfMaterialLotConfirmIface.setProcessStatus("N");
                itfMaterialLotConfirmIface.setCid(Long.valueOf(cIds.get(index)));
                itfMaterialLotConfirmIface.setObjectVersionNumber(1L);
                itfMaterialLotConfirmIface.setCreationDate(nowDate);
                itfMaterialLotConfirmIface.setCreatedBy(userId);
                itfMaterialLotConfirmIface.setLastUpdatedBy(userId);
                itfMaterialLotConfirmIface.setLastUpdateDate(nowDate);
                sqlList.addAll(customDbRepository.getInsertSql(itfMaterialLotConfirmIface));
                index++;
            }
            if(CollectionUtils.isNotEmpty(sqlList)){
                this.jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
            }
        }
        return batchId;
    }
}
