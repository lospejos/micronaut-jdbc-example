package notes.x

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.sql.Connection
import javax.sql.DataSource

@Controller("/hello")
class HelloController(
        private val dataSource: DataSource
) {
    init {
        inTransaction(dataSource) {
            it.createStatement().execute("CREATE TABLE foo (id INTEGER);")
            it.createStatement().execute("INSERT INTO foo (id) VALUES (0);")
        }
    }

    @Get("/")
    fun index(): String = inTransaction(dataSource) {
        it.createStatement().execute("UPDATE foo SET id = id + 1")
        val resultSet = it.createStatement().executeQuery("SELECT id FROM foo;")
        resultSet.next()

        val count = resultSet.getInt("id")

        count.toString()
    }
}

fun <T> inTransaction(dataSource: DataSource, block: (Connection) -> T): T {
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