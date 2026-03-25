
import io.opentelemetry.api.OpenTelemetry
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.then
import org.http4k.filter.OpenTelemetryTracing
import org.http4k.filter.ServerFilters
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.koin.core.context.startKoin

fun main() {
    val koin = startKoin { modules(appModule) }
    val repo: TodoRepo by koin.koin.inject()
    val otel: OpenTelemetry  by koin.koin.inject()

    val api = contract {
        renderer   = OpenApi3(ApiInfo("Todo API", "1.0"))
        descriptionPath = "/openapi.json"   // Swagger UI can point here
        routes     += TodoRoutes.routes(repo)
    }



    val app = ServerFilters.CatchLensFailure()  // returns 400 on bad input
        .then(api)

    val tracedApp = ServerFilters.OpenTelemetryTracing(otel)         // latency histogram
        .then(app)

    tracedApp.asServer(Undertow(9000)).start()
    println("Listening on :9000 — OpenAPI spec at http://localhost:9000/openapi.json")
}