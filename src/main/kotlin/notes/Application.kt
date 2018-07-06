package notes

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("notes")
                .mainClass(Application.javaClass)
                .start()
    }
}