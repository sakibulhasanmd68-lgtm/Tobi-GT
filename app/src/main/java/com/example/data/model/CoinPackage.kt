package com.example.data.model

data class CoinPackage(
    val productId: String,
    val priceDollars: Double,
    val coinAmount: Long,
    val formattedPrice: String,
    val badge: String? = null,
    val isPopular: Boolean = false,
    val isBestValue: Boolean = false
)

object CoinPackages {
    val ALL: List<CoinPackage> = listOf(
        CoinPackage("coins_101", 1.01, 101L, "$1.01", badge = "STARTER"),
        CoinPackage("coins_201", 2.01, 201L, "$2.01"),
        CoinPackage("coins_301", 3.01, 301L, "$3.01"),
        CoinPackage("coins_401", 4.01, 401L, "$4.01"),
        CoinPackage("coins_501", 5.01, 501L, "$5.01", badge = "PILOT'S PICK"),
        CoinPackage("coins_601", 6.01, 601L, "$6.01"),
        CoinPackage("coins_701", 7.01, 701L, "$7.01"),
        CoinPackage("coins_801", 8.01, 801L, "$8.01"),
        CoinPackage("coins_901", 9.01, 901L, "$9.01"),
        CoinPackage("coins_1001", 10.01, 1001L, "$10.01", badge = "POPULAR", isPopular = true),
        CoinPackage("coins_1501", 15.01, 1501L, "$15.01"),
        CoinPackage("coins_2001", 20.01, 2001L, "$20.01"),
        CoinPackage("coins_2501", 25.01, 2501L, "$25.01", badge = "SQUADRON PACK"),
        CoinPackage("coins_3001", 30.01, 3001L, "$30.01"),
        CoinPackage("coins_4001", 40.01, 4001L, "$40.01"),
        CoinPackage("coins_5001", 50.01, 5001L, "$50.01", badge = "WAR CHEST"),
        CoinPackage("coins_9901", 99.01, 9901L, "$99.01", badge = "HIGH ROLLER"),
        CoinPackage("coins_50001", 500.01, 50001L, "$500.01", badge = "ARMADA"),
        CoinPackage("coins_100001", 1000.01, 100001L, "$1,000.01", badge = "ULTIMATE FLEET", isBestValue = true)
    )

    fun findById(productId: String): CoinPackage? = ALL.find { it.productId == productId }
}
