package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.entity.ItfDtpCollectIface;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 器件测试台数据采集接口表Mapper
 *
 * @author wenzhang.yu@hand-china.com 2020-08-25 19:00:41
 */
public interface ItfDtpCollectIfaceMapper extends BaseMapper<ItfDtpCollectIface> {


    void insertIface(@Param("domains")List<ItfDtpCollectIface> domains);
}
