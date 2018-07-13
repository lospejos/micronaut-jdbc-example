package notes.x

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.spring.tx.annotation.Transactional
import javax.sql.DataSource

@Controller("/transactional")
open class TransactionalController(
        private val dataSource: DataSource
) {
    init {
        dataSource.connection.use {
            it.createStatement().use {
                it.execute("CREATE TABLE foo2 (id INTEGER);")
            }
            it.createStatement().use {
                it.execute("INSERT INTO foo2 (id) VALUES (0);")
            }

            it.commit()
        }
    }

    @Get("/")
    @Transactional
    open fun index(): String {
        return dataSource.connection.use {
            it.createStatement().use {
                it.execute("UPDATE foo2 SET id = id + 1")
            }

            it.createStatement().use {
                val resultSet = it.executeQuery("SELECT id FROM foo2;")
                val count = resultSet.use {
                    it.next()
                    it.getInt("id")
                }
                count.toString()
            }
        }
    }
}
