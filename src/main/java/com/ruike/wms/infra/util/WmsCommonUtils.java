package com.ruike.wms.infra.util;

import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

/**
 * WMS 工具类
 *
 * @author jiangling.zheng@hand-china.com 2020-08-05 20:46
 */
@Component
@Slf4j
public class WmsCommonUtils {

    /**
     * 构造静态方法
     */
    private static MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    public WmsCommonUtils(MtErrorMessageRepository mtErrorMessageRepository) {
        WmsCommonUtils.mtErrorMessageRepository = mtErrorMessageRepository;
    }

    /**
     * 表字段
     */
    private static String[] ignoreTableFields = new String[]{AuditDomain.FIELD_OBJECT_VERSION_NUMBER, AuditDomain.FIELD_LAST_UPDATED_BY, AuditDomain.FIELD_CREATED_BY, AuditDomain.FIELD_CREATION_DATE, AuditDomain.FIELD_LAST_UPDATE_DATE};

    public static <T> String[] getNullPropertyNames(T source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static <E, T> void copyPropertiesIgnoreNullAndTableFields(E src, T target) {
        // 对象值转换时屏蔽表字段：为空字段，WHO字段等信息
        Set<String> hashSet = new HashSet<>();
        hashSet.addAll(Arrays.asList(getNullPropertyNames(src)));
        hashSet.addAll(Arrays.asList(ignoreTableFields));
        String[] result = new String[hashSet.size()];
        BeanUtils.copyProperties(src, target, hashSet.toArray(result));
    }

    /**
     * 异常消息统一处理方法
     *
     * @param tenantId    租户
     * @param condition   异常判断条件
     * @param messageCode 消息编码
     * @param module      模块
     * @param args        参数
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/10 11:07:57
     */
    public static void processValidateMessage(Long tenantId, boolean condition, String messageCode, String module, String... args) {
        if (condition) {
            throw new MtException(messageCode, mtErrorMessageRepository.getErrorMessageWithModule(tenantId, messageCode, module, args));
        }
    }

    /**
     * 异常消息统一处理方法
     *
     * @param tenantId    租户
     * @param messageCode 消息编码
     * @param module      模块
     * @param args        参数
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/10 11:07:57
     */
    public static void processValidateMessage(Long tenantId, String messageCode, String module, String... args) {
        throw new MtException(messageCode, mtErrorMessageRepository.getErrorMessageWithModule(tenantId, messageCode, module, args));
    }

    /**
     * 异常消息统一处理方法
     *
     * @param tenantId    租户
     * @param messageCode 消息编码
     * @param module      模块
     * @param args        参数
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/10 11:07:57
     */
    public static String messageContentGet(Long tenantId, String messageCode, String module, String... args) {
        return mtErrorMessageRepository.getErrorMessageWithModule(tenantId, messageCode, module, args);
    }

    /**
     * 拆分数据
     *
     * @param sqlList  源数据
     * @param splitNum 拆分数量
     * @return 拆分数据
     * @author jiangling.zheng@hand-china.com 2020/7/30 17:01
     */
    public static <T> List<List<T>> splitSqlList(List<T> sqlList, Integer splitNum) {
        int defaultNum = 500;
        splitNum = Optional.ofNullable(splitNum).orElse(defaultNum);
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

    /**
     * 设置默认的who字段
     *
     * @param obj 对象
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/11 02:38:12
     */
    public static <T extends AuditDomain> void setDefaultWhoFields(T obj) {
        Date now = new Date();
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        obj.setObjectVersionNumber(1L);
        obj.setCreatedBy(userDetails.getUserId());
        obj.setLastUpdatedBy(userDetails.getUserId());
        obj.setCreationDate(now);
        obj.setLastUpdateDate(now);
    }

    /**
     * 将文件转换成String
     *
     * @param pathStr 文件路径
     * @return byte[]
     * @author penglin.sui@hand-china.com 2020/8/24 01:30
     */
    public static String getStringByFile(String pathStr) {
        File file = new File(pathStr);
        byte[] data = FileUtil.File2byte(file);
        String dataStr = "";
        try {
            dataStr = new String(data, "UTF-8");
        } catch (Exception e) {
            log.error("===========================WmsCommonUtils.getStringByFile error:" + e.getMessage());
            e.printStackTrace();
        }
        return dataStr;
    }

    /**
     * 手动排序分页
     * 默认第一页为0
     *
     * @param nowPage    当前页
     * @param pageSize   每页数量
     * @param data       分页数据
     * @param comparator 排序实现
     * @return <T> Page<T>
     */
    public static <T> Page<T> pagedAndSorList(int nowPage, int pageSize, List<T> data, Comparator<T> comparator) {
        nowPage = nowPage + 1;
        if (Objects.nonNull(comparator)) {
            data.sort(comparator);
        }
        int fromIndex = (nowPage - 1) * pageSize;
        if (CollectionUtils.isEmpty(data) || fromIndex >= data.size() || fromIndex < 0) {
            // 返回空页
            return new Page<>();
        }
        int toIndex = nowPage * pageSize;
        if (toIndex >= data.size()) {
            toIndex = data.size();
        }
        List<T> list = data.subList(fromIndex, toIndex);
        return new Page<>(list, new PageInfo(nowPage - 1, pageSize), data.size());
    }

    /**
     * 手动分页
     * 默认第一页为0
     *
     * @param nowPage  当前页
     * @param pageSize 每页数量
     * @param data     分页数据
     * @return <T> Page<T>
     */
    public static <T> Page<T> pagedList(int nowPage, int pageSize, List<T> data) {
        return pagedAndSorList(nowPage, pageSize, data, null);
    }
}
