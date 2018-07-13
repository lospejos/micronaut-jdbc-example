package notes

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import org.slf4j.LoggerFactory
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import javax.sql.DataSource

@Factory
class DataSourceTxManagerFactory(
        private val dataSource: DataSource
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun transactionManager(): DataSourceTransactionManager {
        logger.info("Creating DataSourceTransactionManager")

        return DataSourceTransactionManager(dataSource)
    }
}