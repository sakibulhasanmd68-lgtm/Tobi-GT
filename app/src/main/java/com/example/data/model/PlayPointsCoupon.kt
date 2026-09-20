package com.example.data.model

data class PlayPointsCoupon(
    val id: String,
    val pointsCost: Int,
    val discountDollars: Int,
    val title: String,
    val description: String,
    val availabilityStatus: String = "Configured in Google Play Console",
    val eligiblePackages: String = "Eligible for Tobi GT Coin purchases"
)

object PlayPointsCoupons {
    val ALL: List<PlayPointsCoupon> = listOf(
        PlayPointsCoupon(
            id = "pp_coupon_1",
            pointsCost = 1,
            discountDollars = 1,
            title = "$1 OFF Tobi GT Coins",
            description = "Exchange 1 Google Play Point for $1 off any eligible Tobi GT digital coin package."
        ),
        PlayPointsCoupon(
            id = "pp_coupon_2",
            pointsCost = 2,
            discountDollars = 2,
            title = "$2 OFF Tobi GT Coins",
            description = "Exchange 2 Google Play Points for $2 off any eligible Tobi GT digital coin package."
        ),
        PlayPointsCoupon(
            id = "pp_coupon_3",
            pointsCost = 3,
            discountDollars = 3,
            title = "$3 OFF Tobi GT Coins",
            description = "Exchange 3 Google Play Points for $3 off any eligible Tobi GT digital coin package."
        ),
        PlayPointsCoupon(
            id = "pp_coupon_4",
            pointsCost = 4,
            discountDollars = 4,
            title = "$4 OFF Tobi GT Coins",
            description = "Exchange 4 Google Play Points for $4 off any eligible Tobi GT digital coin package."
        ),
        PlayPointsCoupon(
            id = "pp_coupon_5",
            pointsCost = 5,
            discountDollars = 5,
            title = "$5 OFF Tobi GT Coins",
            description = "Exchange 5 Google Play Points for $5 off any eligible Tobi GT digital coin package."
        ),
        PlayPointsCoupon(
            id = "pp_coupon_6",
            pointsCost = 6,
            discountDollars = 6,
            title = "$6 OFF Tobi GT Coins",
            description = "Exchange 6 Google Play Points for $6 off any eligible Tobi GT digital coin package."
        ),
        PlayPointsCoupon(
            id = "pp_coupon_10",
            pointsCost = 10,
            discountDollars = 10,
            title = "$10 OFF Tobi GT Coins",
            description = "Exchange 10 Google Play Points for $10 off any eligible Tobi GT digital coin package."
        ),
        PlayPointsCoupon(
            id = "pp_coupon_15",
            pointsCost = 15,
            discountDollars = 15,
            title = "$15 OFF Tobi GT Coins",
            description = "Exchange 15 Google Play Points for $15 off any eligible Tobi GT digital coin package."
        ),
        PlayPointsCoupon(
            id = "pp_coupon_20",
            pointsCost = 20,
            discountDollars = 20,
            title = "$20 OFF Tobi GT Coins",
            description = "Exchange 20 Google Play Points for $20 off any eligible Tobi GT digital coin package."
        ),
        PlayPointsCoupon(
            id = "pp_coupon_25",
            pointsCost = 25,
            discountDollars = 25,
            title = "$25 OFF Tobi GT Coins",
            description = "Exchange 25 Google Play Points for $25 off any eligible Tobi GT digital coin package."
        ),
        PlayPointsCoupon(
            id = "pp_coupon_30",
            pointsCost = 30,
            discountDollars = 30,
            title = "$30 OFF Tobi GT Coins",
            description = "Exchange 30 Google Play Points for $30 off any eligible Tobi GT digital coin package."
        ),
        PlayPointsCoupon(
            id = "pp_coupon_40",
            pointsCost = 40,
            discountDollars = 40,
            title = "$40 OFF Tobi GT Coins",
            description = "Exchange 40 Google Play Points for $40 off any eligible Tobi GT digital coin package."
        )
    )
}
