package io.tarzan.common.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.entity.MtErrorMessage;
import io.tarzan.common.domain.vo.MtErrorMessageVO2;

/**
 * 资源库
 *
 * @author MrZ 2019-05-21 17:09:05
 */
public interface MtErrorMessageRepository extends BaseRepository<MtErrorMessage>, AopProxy<MtErrorMessageRepository> {

    /**
     * 获取消息: 先查询redis，如果匹配不到，查询MySql
     * 
     * @param code
     * @param module
     * @param args
     * @return String
     */
    String getErrorMessageWithModule(Long tenantId, String code, String module, String... args);

    /**
     * messageLimitMessageCodeQuery根据消息内容获取消息代码
     *
     * @param module
     * @param message
     * @return List<String>
     */
    List<String> messageLimitMessageCodeQuery(Long tenantId, String module, String message);

    /**
     * messageCodeLimitMessageGet根据消息代码获取消息内容
     *
     * @param module
     * @param messageCode
     * @return String
     */
    String messageCodeLimitMessageGet(Long tenantId, String module, String messageCode);


    void initDataToRedis();

    String messageBasicPropertyUpdate(Long tenantId, MtErrorMessage dto);

    void removeMessage(Long tenantId, List<MtErrorMessageVO2> list);

}
