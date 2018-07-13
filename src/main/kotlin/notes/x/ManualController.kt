package notes.x

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.sql.Connection
import javax.sql.DataSource

@Controller("/manual")
class ManualController(
        private val dataSource: DataSource
) {
    init {
        inTransaction(dataSource) {
            it.createStatement().use {
                it.execute("CREATE TABLE foo (id INTEGER);")
            }
            it.createStatement().use {
                it.execute("INSERT INTO foo (id) VALUES (0);")
            }
        }
    }

    @Get("/")
    fun index(): String = inTransaction(dataSource) {
        it.createStatement().use {
            it.execute("UPDATE foo SET id = id + 1")
        }

        it.createStatement().use {
            val resultSet = it.executeQuery("SELECT id FROM foo;")
            val count = resultSet.use {
                it.next()
                it.getInt("id")
            }
            count.toString()
        }
    }
}

private fun <T> inTransaction(dataSource: DataSource, block: (Connection) -> T): T {
    dataSource.connection.use {
        try {
            val result = block(it)
            it.commit()
            return result
        } catch (e: Exception) {
            it.rollback()
            throw e
        }
    }
}