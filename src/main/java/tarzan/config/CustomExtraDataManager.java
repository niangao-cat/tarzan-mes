package tarzan.config;

import io.choerodon.core.swagger.ChoerodonRouteData;
import io.choerodon.swagger.annotation.ChoerodonExtraData;
import io.choerodon.swagger.swagger.extra.ExtraData;
import io.choerodon.swagger.swagger.extra.ExtraDataManager;

/**
 * @program: tarzan-parent
 * @description: 注册服务
 * @author: Mr.Zxl
 * @create: 2019-04-11 14:18
 **/
@ChoerodonExtraData
public class CustomExtraDataManager implements ExtraDataManager {
    @Override
    public ExtraData getData() {
        ChoerodonRouteData routeData = new ChoerodonRouteData();
        routeData.setName("mes");
        routeData.setPath("/mes/**");
        routeData.setServiceId("tarzan-mes");
        routeData.setPackages("tarzan,io.tarzan.common,com.ruike");
        extraData.put(ExtraData.ZUUL_ROUTE_DATA, routeData);
        return extraData;
    }
}
