    import com.uchuhimo.konf.Config
import org.example.buildOpenTelemetry
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val appModule = module {
    single {
        Config { addSpec(DatabaseConfigSpec) }.from.env()
    }
    single {
        val config = get<Config>()

        Database.connect(
            url      = config[DatabaseConfigSpec.url],
            driver   = config[DatabaseConfigSpec.driver],
            user     = config[DatabaseConfigSpec.user],
            password = config[DatabaseConfigSpec.password]
        )
    }
    single { TodoRepo(get()) }
    single { buildOpenTelemetry() }
}