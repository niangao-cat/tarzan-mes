package com.ruike.wms.app.service.impl;

import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO;
import com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO2;
import com.ruike.wms.api.dto.WmsStockTransferHeadDTO;
import com.ruike.wms.app.service.WmsCheckedWaitGroudingService;
import com.ruike.wms.domain.entity.WmsPutInStorageTask;
import com.ruike.wms.domain.repository.WmsPutInStorageTaskRepository;
import com.ruike.wms.infra.mapper.WmsCheckedWaitGroudingMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtExtendSettings;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import io.tarzan.common.infra.mapper.MtExtendSettingsMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.material.domain.vo.MtUomVO;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author tong.li
 * @Date 2020/4/24 12:49
 * @Version 1.0
 */
@Service
public class WmsCheckedWaitGroudingServiceImpl implements WmsCheckedWaitGroudingService {

    @Autowired
    private WmsCheckedWaitGroudingMapper checkedWaitGroudingMapper;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtSupplierRepository mtSupplierRepository;
    @Autowired
    private MtExtendSettingsMapper mtExtendSettingMapper;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtUomRepository mtUomRepository;




    /**
    * @param tenantId 1
    * @param pageRequest 2
    * @return : java.util.List<com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO>
    * @Description: 已收待上架看板 数据查询
    * @author: tong.li
    * @date 2020/4/24 13:00
    * @version 1.0
    */
    @Override
    public List<WmsCheckedWaitGroudingDTO> queryTaskData(Long tenantId, PageRequest pageRequest) {

        Date now = new Date();
        Page<WmsCheckedWaitGroudingDTO> base = PageHelper.doPageAndSort(pageRequest,
                ()->checkedWaitGroudingMapper.queryPutInStorage(tenantId));
        for (WmsCheckedWaitGroudingDTO wmsCheckedWaitGroudingDTO : base) {
            BigDecimal waitStoragedDays = daysBetween(wmsCheckedWaitGroudingDTO.getCreationDate(),now);
            wmsCheckedWaitGroudingDTO.setWaitStoragedDays(waitStoragedDays);
        }
        return base;
    }

    /**
    * @param tenantId 1
    * @param pageRequest 2
    * @return : java.util.List<com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO2>
    * @Description: 30天物料上架量
    * @author: tong.li
    * @date 2020/4/26 16:19
    * @version 1.0
    */
    @Override
    public List<WmsCheckedWaitGroudingDTO2> materialStoragedNumQuery(Long tenantId, PageRequest pageRequest) {
        List<WmsCheckedWaitGroudingDTO2> resultList = new ArrayList<>();

        int a = 29;
        Date nowDate = new Date();
        for (int i = a; i >= 0; i--) {
            WmsCheckedWaitGroudingDTO2 checkedWaitGroudingDTO2 = new WmsCheckedWaitGroudingDTO2();
            Date dailyDateFrom  = getFromDate(nowDate,i);
            Date dailyDateTo  = getToDate(nowDate,i);
            BigDecimal resultNum = checkedWaitGroudingMapper.materialStoragedNumQuery(tenantId,dailyDateFrom,dailyDateTo);
            checkedWaitGroudingDTO2.setDailyTimeFrom(dailyDateFrom);
            checkedWaitGroudingDTO2.setDailyTimeTo(dailyDateTo);
            checkedWaitGroudingDTO2.setMaterialStoragedNum(resultNum);
            resultList.add(checkedWaitGroudingDTO2);
        }
        return resultList;
    }

    /**
    * @param tenantId 1
    * @param pageRequest 2
    * @return : java.util.List<com.ruike.wms.api.dto.WmsCheckedWaitGroudingDTO2>
    * @Description: 全年收货量和上架时长趋势图数据查询
    * @author: tong.li
    * @date 2020/4/26 16:20
    * @version 1.0
    */
    @Override
    public List<WmsCheckedWaitGroudingDTO2> trendDataQuery(Long tenantId, PageRequest pageRequest) {
        List<WmsCheckedWaitGroudingDTO2> resultList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int monthCount = 12;
        for (int i = 0; i < monthCount; i++) {
            //获取每个月的第一天的00：00：00
            Calendar from = Calendar.getInstance();
            //将年月日设为本年的每个月的1号
            from.set(Calendar.YEAR, year);
            from.set(Calendar.MONTH, i);
            from.set(Calendar.DAY_OF_MONTH, 1);
            //将时分秒置0
            from.set(Calendar.HOUR_OF_DAY, 0);
            from.set(Calendar.MINUTE, 0);
            from.set(Calendar.SECOND, 0);
            Date monthDateFrom =from.getTime();

            //获取每个月的最后一天的23：59：59
            Calendar to = Calendar.getInstance();
            //将年月日设为本年的每个月的最后一天
            to.set(Calendar.YEAR,year);
            to.set(Calendar.MONTH,i);
            to.set(Calendar.DAY_OF_MONTH, to.getActualMaximum(Calendar.DAY_OF_MONTH));
            //将时分秒置23:59:59
            to.set(Calendar.HOUR_OF_DAY, 23);
            to.set(Calendar.MINUTE, 59);
            to.set(Calendar.SECOND, 59);
            Date monthDateTo =to.getTime();

            WmsCheckedWaitGroudingDTO2 checkedWaitGroudingDTO2 = checkedWaitGroudingMapper.trendDataQuery(tenantId, monthDateFrom, monthDateTo);
            checkedWaitGroudingDTO2.setMonthTimeFrom(monthDateFrom);
            checkedWaitGroudingDTO2.setMonthTimeTo(monthDateTo);
            resultList.add(checkedWaitGroudingDTO2);
        }
        return resultList;
    }

    /**
     * @Description 获取当前时间前i天的日期，yyyy-mm-dd 00:00:00格式
     * @param nowDate 当前时间
     * @param day 天数
     * @return
     */
    public Date getFromDate(Date nowDate,int day){
        Calendar now = Calendar.getInstance();
        now.setTime(nowDate);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);

        //将时分秒置0
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        return now.getTime();
    }

    /**
     * @Description 获取当前时间前i天的日期，yyyy-mm-dd 23:59:59格式
     * @param nowDate 当前时间
     * @param day 天数
     * @return
     */
    public Date getToDate(Date nowDate,int day){
        Calendar now = Calendar.getInstance();
        now.setTime(nowDate);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        //将时分秒置0
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        return now.getTime();
    }


    /**
     * @Description 获取两个日期之间相差天数,date2-date1
     * @param date1
     * @param date2
     * @return
     */
    public static BigDecimal daysBetween(Date date1,Date date2){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        Long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        Long time2 = cal.getTimeInMillis();

        double cc = time2.doubleValue() - time1.doubleValue();
        double test = (time2.doubleValue() - time1.doubleValue())/(1000 * 3600 * 24);
        String aaa = String.format("%.2f", test);
        BigDecimal between_days=new BigDecimal(aaa);

        return between_days;
    }

}
