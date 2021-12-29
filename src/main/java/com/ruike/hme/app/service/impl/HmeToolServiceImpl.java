package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeToolInsertDto;
import com.ruike.hme.app.service.HmeToolService;
import com.ruike.hme.domain.entity.HmeTool;
import com.ruike.hme.domain.entity.HmeToolHis;
import com.ruike.hme.domain.repository.HmeToolHisRepository;
import com.ruike.hme.domain.repository.HmeToolRepository;
import com.ruike.hme.domain.vo.HmeToolCheckVO;
import com.ruike.hme.infra.mapper.HmeToolMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 工装基础数据表应用服务默认实现
 *
 * @author li.zhang13@hand-china.com 2021-01-07 10:06:45
 */
@Service
public class HmeToolServiceImpl implements HmeToolService {

    @Autowired
    private HmeToolRepository hmeToolRepository;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private HmeToolHisRepository hmeToolHisRepository;
    @Autowired
    private HmeToolMapper hmeToolMapper;

    /***
     * @Description: 获取当前时间
     */
    public static Date currentTimeGet() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(currentTime.format(formatter), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /***
     * @Description: 获取当前用户
     */
    CustomUserDetails curUser = DetailsHelper.getUserDetails();
    Long userId = curUser == null ? -1L : curUser.getUserId();


    /**
     * @param tenantId
     * @param hmeToolInsertDtos
     * @Description 新增或更新工装基本数据，同时新增修改历史记录
     * @Author li.zhang13@hand-china.com
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertorupdateSelective(String tenantId, List<HmeToolInsertDto> hmeToolInsertDtos){
        hmeToolInsertDtos.forEach(hmeToolInsertDto -> {
            HmeTool hmeTool = new HmeTool();
            HmeToolHis hmeToolHis = new HmeToolHis();

            //新增工装维护历史记录的基本信息
            String hisids = customDbRepository.getNextKey("hme_tool_his_s");
            Long hiscids = Long.parseLong(customDbRepository.getNextKey("hme_tool_his_cid_s"));
            hmeToolHis.setTenantId(0L);
            hmeToolHis.setToolHisId(hisids);
            hmeToolHis.setCid(hiscids);
            hmeToolHis.setAreaId(hmeToolInsertDto.getAreaId());
            hmeToolHis.setWorkShopId(hmeToolInsertDto.getAreaCode());
            hmeToolHis.setWorkcellId(hmeToolInsertDto.getWorkcellId());
            hmeToolHis.setToolName(hmeToolInsertDto.getToolName());
            hmeToolHis.setBrandName(hmeToolInsertDto.getBrandName());
            hmeToolHis.setSpecification(hmeToolInsertDto.getSpecification());
            hmeToolHis.setUomId(hmeToolInsertDto.getUomId());
            hmeToolHis.setQty(hmeToolInsertDto.getQty());
            hmeToolHis.setRate(hmeToolInsertDto.getRate());
            hmeToolHis.setEnableFlag(hmeToolInsertDto.getEnableFlag());
            hmeToolHis.setCreationDate(currentTimeGet());
            hmeToolHis.setLastUpdateDate(currentTimeGet());
            hmeToolHis.setCreatedBy(userId);
            hmeToolHis.setLastUpdatedBy(userId);

            if(StringUtils.isEmpty(hmeToolInsertDto.getToolId())){
                //主键id为空是新增

                //新增工装维护记录
                String ids = customDbRepository.getNextKey("hme_tool_s");
                Long cids = Long.parseLong(customDbRepository.getNextKey("hme_tool_cid_s"));
                hmeTool.setTenantId(0L);
                hmeTool.setToolId(ids);
                hmeTool.setCid(cids);
                hmeTool.setAreaId(hmeToolInsertDto.getAreaId());
                hmeTool.setWorkShopId(hmeToolInsertDto.getAreaCode());
                hmeTool.setWorkcellId(hmeToolInsertDto.getWorkcellId());
                hmeTool.setToolName(hmeToolInsertDto.getToolName());
                hmeTool.setBrandName(hmeToolInsertDto.getBrandName());
                hmeTool.setSpecification(hmeToolInsertDto.getSpecification());
                hmeTool.setUomId(hmeToolInsertDto.getUomId());
                hmeTool.setQty(hmeToolInsertDto.getQty());
                hmeTool.setRate(hmeToolInsertDto.getRate());
                hmeTool.setEnableFlag(hmeToolInsertDto.getEnableFlag());
                hmeTool.setCreationDate(currentTimeGet());
                hmeTool.setLastUpdateDate(currentTimeGet());
                hmeTool.setCreatedBy(userId);
                hmeTool.setLastUpdatedBy(userId);

                //新增工装修改记录对应的工装ID
                hmeToolHis.setToolId(ids);

                hmeToolHisRepository.insert(hmeToolHis);
                hmeToolRepository.insert(hmeTool);
            }else {
                //不为空则进行更新操作
                //更新工装维护数据
                //获取原数据
                hmeTool = hmeToolRepository.selectByPrimaryKey(hmeToolInsertDto.getToolId());
                //修改数据
                hmeTool.setAreaId(hmeToolInsertDto.getAreaId());
                hmeTool.setWorkShopId(hmeToolInsertDto.getAreaCode());
                hmeTool.setWorkcellId(hmeToolInsertDto.getWorkcellId());
                hmeTool.setBrandName(hmeToolInsertDto.getBrandName());
                hmeTool.setToolName(hmeToolInsertDto.getToolName());
                hmeTool.setSpecification(hmeToolInsertDto.getSpecification());
                hmeTool.setUomId(hmeToolInsertDto.getUomId());
                hmeTool.setQty(hmeToolInsertDto.getQty());
                hmeTool.setRate(hmeToolInsertDto.getRate());
                hmeTool.setEnableFlag(hmeToolInsertDto.getEnableFlag());
                hmeTool.setLastUpdateDate(currentTimeGet());
                hmeTool.setLastUpdatedBy(userId);

                //新增工装修改记录对应的工装ID
                hmeToolHis.setToolId(hmeToolInsertDto.getToolId());

                hmeToolMapper.updateByPrimaryKey(hmeTool);
                hmeToolHisRepository.insert(hmeToolHis);
            }
        });
    }
}
