package no.maadb.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Receipt(val id: Long, val transactionId: Long, val flowIn: Double, val flowOut: Double, val details: String)

object Receipts : Table() {
    val id = long("id").autoIncrement()
    val transactionId = reference("transaction_id", Transactions.id)
    val flowIn = double("flow_in")
    val flowOut = double("flow_out")
    val balance = double("balance")
    val details = varchar("details", 4096)
    override val primaryKey = PrimaryKey(id)
}
