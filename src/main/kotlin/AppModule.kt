import org.example.buildOpenTelemetry
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val appModule = module {
    single {
        Database.connect(
            url      = "jdbc:h2:mem:todos;DB_CLOSE_DELAY=-1",
            driver   = "org.h2.Driver",
            user     = "sa",
            password = ""
        )
    }
    single { TodoRepo(get()) }
    single { buildOpenTelemetry() }
}