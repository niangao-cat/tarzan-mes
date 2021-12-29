package com.ruike.itf.infra.util;

import com.ruike.itf.api.dto.DataCollectReturnDTO;
import com.ruike.itf.domain.entity.ItfFacCollectIface;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import net.sf.cglib.beans.BeanCopier;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * description
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/21 3:51 下午
 */
@Component
public class InterfaceUtils {

    /**
     * 构造静态方法
     */
    private static MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    public InterfaceUtils(MtErrorMessageRepository mtErrorMessageRepository) {
        InterfaceUtils.mtErrorMessageRepository = mtErrorMessageRepository;
    }

    private static final String MESSAGE_MODULE = "ITF";

    /**
     * 获取并拼接错误消息
     *
     * @param tenantId    租户ID
     * @param errorFlag   验证表达式结果
     * @param message     消息
     * @param messageCode 消息编码
     * @param args        参数
     * @return java.lang.String
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/7/21 04:05:07
     */
    public static String processErrorMessage(Long tenantId, Boolean errorFlag, String message, String messageCode, String... args) {
        if (errorFlag) {
            String errorMessage = mtErrorMessageRepository.getErrorMessageWithModule(tenantId, messageCode, MESSAGE_MODULE, args);
            return message.concat(errorMessage).concat(";");
        }
        return message;
    }

    /**
     * 接口统一返回格式
     *
     * @param list 接口处理结果数据
     * @return java.util.List<com.ruike.itf.api.dto.DataCollectReturnDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/5 09:18:11
     */
    public static <T> List<DataCollectReturnDTO> getReturnList(List<T> list) {
        List<DataCollectReturnDTO> returnList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return returnList;
        }
        @SuppressWarnings("unchecked")
        Class<T> entityClass = (Class<T>) list.get(0).getClass();
        BeanCopier copier = BeanCopier.create(entityClass, DataCollectReturnDTO.class, false);
        for (T itf : list) {
            DataCollectReturnDTO rtn = new DataCollectReturnDTO();
            copier.copy(itf, rtn, null);
            returnList.add(rtn);
        }
        return returnList;
    }

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
        //V20210716 modify by penglin.sui 传入集合为空，直接返回
        if(CollectionUtils.isEmpty(sqlList)){
            return returnList;
        }
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
}
