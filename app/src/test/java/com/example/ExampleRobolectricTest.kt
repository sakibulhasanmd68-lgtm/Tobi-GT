package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.CoinPackages
import com.example.data.model.PlayPointsCoupons
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Tobi GT", appName)
  }

  @Test
  fun `verify diamond packages and mathematical formula`() {
    assertEquals(22, CoinPackages.ALL.size)

    val expectedTiers = listOf(
        Pair(0.51, 51L),
        Pair(1.01, 101L),
        Pair(2.01, 201L),
        Pair(3.01, 301L),
        Pair(4.01, 401L),
        Pair(5.01, 501L),
        Pair(6.01, 601L),
        Pair(7.01, 701L),
        Pair(8.01, 801L),
        Pair(9.01, 901L),
        Pair(10.01, 1001L),
        Pair(15.01, 1501L),
        Pair(20.01, 2001L),
        Pair(25.01, 2501L),
        Pair(30.01, 3001L),
        Pair(40.01, 4001L),
        Pair(50.01, 5001L),
        Pair(99.01, 9901L),
        Pair(500.01, 50001L),
        Pair(1000.01, 100001L),
        Pair(1.00, 100L),
        Pair(2.00, 200L)
    )

    for (i in expectedTiers.indices) {
        val (expectedPrice, expectedDiamonds) = expectedTiers[i]
        val pkg = CoinPackages.ALL[i]
        assertEquals("Mismatch at package $i price", expectedPrice, pkg.priceDollars, 0.001)
        assertEquals("Mismatch at package $i diamonds", expectedDiamonds, pkg.diamondAmount)
    }

    // Explicitly verify separate $0.51, $1.00, $1.01, $2.00, $2.01
    val pkg51 = CoinPackages.findById("diamonds_51")
    assertNotNull(pkg51)
    assertEquals(51L, pkg51!!.diamondAmount)
    assertEquals(0.51, pkg51.priceDollars, 0.001)

    val pkg100 = CoinPackages.findById("diamonds_100")
    assertNotNull(pkg100)
    assertEquals(100L, pkg100!!.diamondAmount)
    assertEquals(1.00, pkg100.priceDollars, 0.001)

    val pkg101 = CoinPackages.findById("diamonds_101")
    assertNotNull(pkg101)
    assertEquals(101L, pkg101!!.diamondAmount)
    assertEquals(1.01, pkg101.priceDollars, 0.001)

    val pkg200 = CoinPackages.findById("diamonds_200")
    assertNotNull(pkg200)
    assertEquals(200L, pkg200!!.diamondAmount)
    assertEquals(2.00, pkg200.priceDollars, 0.001)

    val pkg201 = CoinPackages.findById("diamonds_201")
    assertNotNull(pkg201)
    assertEquals(201L, pkg201!!.diamondAmount)
    assertEquals(2.01, pkg201.priceDollars, 0.001)
  }

  @Test
  fun `verify exactly 12 play points coupon tiers`() {
    assertEquals(12, PlayPointsCoupons.ALL.size)
    val expectedValues = listOf(1, 2, 3, 4, 5, 6, 10, 15, 20, 25, 30, 40)
    val actualValues = PlayPointsCoupons.ALL.map { it.discountDollars }
    assertEquals(expectedValues, actualValues)
  }
}
