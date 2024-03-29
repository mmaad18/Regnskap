package no.maadb.dao

import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import no.maadb.models.ImageUris
import no.maadb.models.Receipts
import no.maadb.models.Transactions
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {

        val appConfig = HoconApplicationConfig(ConfigFactory.load())
        val driverClassName = appConfig.property("db.driverClassName").getString()
        val jdbcURL = appConfig.property("db.jdbcUrl").getString()
        val username = appConfig.property("db.username").getString()
        val password = appConfig.property("db.password").getString()

        val database = Database.connect(jdbcURL, driverClassName, username, password)

        transaction(database) {
            SchemaUtils.create(Transactions)
            SchemaUtils.create(Receipts)
            SchemaUtils.create(ImageUris)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}