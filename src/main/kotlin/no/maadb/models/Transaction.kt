package no.maadb.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Transaction(val id: Long, val flowIn: Double, val flowOut: Double, val balance: Double)

object Transactions : Table() {
    val id = long("id").autoIncrement()
    val flowIn = double("flow_in")
    val flowOut = double("flow_out")
    val balance = double("balance")
    override val primaryKey = PrimaryKey(id)
}