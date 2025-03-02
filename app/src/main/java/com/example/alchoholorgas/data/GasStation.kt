package com.example.alchoholorgas.data

import org.json.JSONObject

data class GasStation(
    val name: String,
    val gasPrice: Double,
    val alcoholPrice: Double,
    val address: String
) {
    constructor(json: JSONObject): this(
        json.optString("name", "") ?: "",
        json.optDouble("gasPrice", 0.0) ?: 0.0,
        json.optDouble("alcoholPrice", 0.0) ?: 0.0,
        json.optString("address", "") ?: ""
    )

    fun toJSONObject(): JSONObject {
        return JSONObject().apply {
            put("name", this@GasStation.name)
            put("gasPrice", this@GasStation.gasPrice)
            put("alcoholPrice", this@GasStation.alcoholPrice)
            put("address", this@GasStation.address)
        }
    }
}