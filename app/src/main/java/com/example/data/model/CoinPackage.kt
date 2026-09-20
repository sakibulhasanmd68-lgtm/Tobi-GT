package com.example.data.model

data class CoinPackage(
    val productId: String,
    val priceDollars: Double,
    val diamondAmount: Long,
    val formattedPrice: String,
    val badge: String? = null,
    val isPopular: Boolean = false,
    val isBestValue: Boolean = false
) {
    // Backward compatibility for existing references
    val coinAmount: Long get() = diamondAmount
}

typealias DiamondPackage = CoinPackage

object CoinPackages {
    val ALL: List<CoinPackage> = listOf(
        CoinPackage("diamonds_51", 0.51, 51L, "$0.51", badge = "MICRO DROP"),
        CoinPackage("diamonds_101", 1.01, 101L, "$1.01", badge = "STARTER"),
        CoinPackage("diamonds_201", 2.01, 201L, "$2.01", badge = "POPULAR", isPopular = true),
        CoinPackage("diamonds_301", 3.01, 301L, "$3.01"),
        CoinPackage("diamonds_401", 4.01, 401L, "$4.01"),
        CoinPackage("diamonds_501", 5.01, 501L, "$5.01", badge = "PILOT'S PICK"),
        CoinPackage("diamonds_601", 6.01, 601L, "$6.01"),
        CoinPackage("diamonds_701", 7.01, 701L, "$7.01"),
        CoinPackage("diamonds_801", 8.01, 801L, "$8.01"),
        CoinPackage("diamonds_901", 9.01, 901L, "$9.01"),
        CoinPackage("diamonds_1001", 10.01, 1001L, "$10.01", badge = "WAR CHEST"),
        CoinPackage("diamonds_1501", 15.01, 1501L, "$15.01"),
        CoinPackage("diamonds_2001", 20.01, 2001L, "$20.01"),
        CoinPackage("diamonds_2501", 25.01, 2501L, "$25.01", badge = "SQUADRON"),
        CoinPackage("diamonds_3001", 30.01, 3001L, "$30.01"),
        CoinPackage("diamonds_4001", 40.01, 4001L, "$40.01"),
        CoinPackage("diamonds_5001", 50.01, 5001L, "$50.01", badge = "HIGH ROLLER"),
        CoinPackage("diamonds_9901", 99.01, 9901L, "$99.01", badge = "ARMADA"),
        CoinPackage("diamonds_50001", 500.01, 50001L, "$500.01", badge = "FLEET VAULT"),
        CoinPackage("diamonds_100001", 1000.01, 100001L, "$1,000.01", badge = "SUPREME FLEET", isBestValue = true),
        CoinPackage("diamonds_100", 1.00, 100L, "$1.00", badge = "STARTER"),
        CoinPackage("diamonds_200", 2.00, 200L, "$2.00", badge = "POPULAR", isPopular = true)
    )

    fun findById(productId: String): CoinPackage? {
        val exact = ALL.find { it.productId == productId }
        if (exact != null) return exact

        val stripped = productId
            .replace("coins_", "diamonds_")
            .replace("tobi_coins_", "diamonds_")

        val strippedMatch = ALL.find { it.productId == stripped }
        if (strippedMatch != null) return strippedMatch

        // Backward compatibility for legacy aliases
        val normalized = stripped
            .replace("diamonds_50", "diamonds_51")
            .replace("diamonds_300", "diamonds_301")
            .replace("diamonds_400", "diamonds_401")
            .replace("diamonds_500", "diamonds_501")
            .replace("diamonds_600", "diamonds_601")
            .replace("diamonds_700", "diamonds_701")
            .replace("diamonds_800", "diamonds_801")
            .replace("diamonds_900", "diamonds_901")
            .replace("diamonds_1000", "diamonds_1001")
            .replace("diamonds_1500", "diamonds_1501")
            .replace("diamonds_2000", "diamonds_2001")
            .replace("diamonds_2500", "diamonds_2501")
            .replace("diamonds_3000", "diamonds_3001")
            .replace("diamonds_4000", "diamonds_4001")
            .replace("diamonds_5000", "diamonds_5001")
            .replace("diamonds_10000", "diamonds_9901")
            .replace("diamonds_50000", "diamonds_50001")
            .replace("diamonds_100000", "diamonds_100001")

        return ALL.find { it.productId == normalized }
    }
}

