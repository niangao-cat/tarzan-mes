package tarzan.method.domain.vo;

import java.util.List;

import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.entity.MtRouterOperationComponent;

public class MtRouterOperationVO {

    private MtRouterOperation routerOperation;
    private List<MtRouterOperationComponent> routerOperationComponents;

    public MtRouterOperation getRouterOperation() {
        return routerOperation;
    }

    public void setRouterOperation(MtRouterOperation routerOperation) {
        this.routerOperation = routerOperation;
    }

    public List<MtRouterOperationComponent> getRouterOperationComponents() {
        return routerOperationComponents;
    }

    public void setRouterOperationComponents(List<MtRouterOperationComponent> routerOperationComponents) {
        this.routerOperationComponents = routerOperationComponents;
    }

}
