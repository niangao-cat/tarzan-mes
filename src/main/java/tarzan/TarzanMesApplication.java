package tarzan;

import org.hzero.core.jackson.annotation.EnableObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import io.choerodon.mybatis.code.DbType;
import io.choerodon.mybatis.domain.Config;
import io.choerodon.mybatis.helper.MapperHelper;
import io.choerodon.resource.annoation.EnableChoerodonResourceServer;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.infra.repository.impl.MtCustomMysqlRepositoryImpl;
import io.tarzan.common.infra.repository.impl.MtCustomOracleRepositoryImpl;

import java.util.TimeZone;

/**
 * @author MrZ
 */
@SpringBootApplication
@ComponentScan({"io.tarzan.common", "tarzan", "io.tarzan", "com.ruike"})
@EnableFeignClients({"org.hzero", "io.choerodon", "tarzan", "io.tarzan", "com.ruike"})
@EnableDiscoveryClient
@EnableChoerodonResourceServer
@EnableObjectMapper
public class TarzanMesApplication {

    @Autowired
    private MapperHelper mapperHelper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public MtCustomDbRepository mtCustomDbRepository() {
        Config config = mapperHelper.getConfig();
        DbType dbType = config.getDbType();
        String dbTypeValue = dbType.getValue();
        if (dbTypeValue.equals("oracle")) {
            MtCustomOracleRepositoryImpl mtCustomOracleRepository = new MtCustomOracleRepositoryImpl();
            mtCustomOracleRepository.setJdbcTemplate(jdbcTemplate);
            return mtCustomOracleRepository;
        } else {
            MtCustomMysqlRepositoryImpl mtCustomMysqlRepository = new MtCustomMysqlRepositoryImpl();
            mtCustomMysqlRepository.setJdbcTemplate(jdbcTemplate);
            return mtCustomMysqlRepository;
        }
    }

    @Bean
    public StartupRunner startupRunner() {
        return new StartupRunner();
    }

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(TarzanMesApplication.class, args);
    }

}
