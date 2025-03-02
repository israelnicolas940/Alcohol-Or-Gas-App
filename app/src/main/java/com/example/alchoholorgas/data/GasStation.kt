package com.example.alchoholorgas.data

import org.json.JSONObject

data class GasStation(
    var id: Int,
    var name: String,
    var gasPrice: Double,
    var alcoholPrice: Double,
    var address: String
) {
    constructor(json: JSONObject): this(
        json.optInt("id", 0) ?: 0,
        json.optString("name", "") ?: "",
        json.optDouble("gasPrice", 0.0) ?: 0.0,
        json.optDouble("alcoholPrice", 0.0) ?: 0.0,
        json.optString("address", "") ?: ""
    )

    fun toJSONObject(): JSONObject {
        return JSONObject().apply {
            put("id", this@GasStation.id)
            put("name", this@GasStation.name)
            put("gasPrice", this@GasStation.gasPrice)
            put("alcoholPrice", this@GasStation.alcoholPrice)
            put("address", this@GasStation.address)
        }
    }
}