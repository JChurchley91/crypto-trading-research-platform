import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.slf4j.LoggerFactory
import azure.KeyVaultClient
import azure.DatabaseFactory
import io.ktor.http.HttpStatusCode
import models.ApiResponse
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils
import kotlin.text.insert
import kotlin.text.set
import kotlin.toString


val logger: org.slf4j.Logger = LoggerFactory.getLogger("news-fetcher")

suspend fun main() {
    logger.info("Initializing database connection")
    DatabaseFactory.init()
    logger.info("Retrieving secret from Azure Key Vault")
    val testSecret: String = KeyVaultClient.getSecret("test-secret")
    logger.info("Sending request to ktor.io")
    val client = HttpClient(CIO)
    var httpResponse: HttpResponse = client.get("https://ktor.io/")
    var httpResponseStatus: String = httpResponse.status.toString()
    client.close()
    logger.info("Received response with status: ${httpResponse.status}")
    println(httpResponseStatus)
    println(testSecret)
    transaction {
        SchemaUtils.create(ApiResponse)
        ApiResponse.insert {
            it[status] = httpResponseStatus.toString()
            it[response] = httpResponse.toString()
        }
    }
}