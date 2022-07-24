package no.maadb.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Transaction(val id: Long)

object Transactions : Table() {
    val id = long("id").autoIncrement()
    val flowIn = double("flow_in")
    val flowOut = double("flow_out")
    val balance = double("balance")
}