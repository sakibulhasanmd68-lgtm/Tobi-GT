package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.CoinPackages
import com.example.data.model.PlayPointsCoupons
import org.junit.Assert.assertEquals
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
  fun `verify exactly 19 coin packages and mathematical formula`() {
    assertEquals(19, CoinPackages.ALL.size)
    for (pkg in CoinPackages.ALL) {
      val expectedCoins = (pkg.priceDollars.toInt() * 100L) + 1L
      assertEquals(expectedCoins, pkg.coinAmount)
    }
  }

  @Test
  fun `verify exactly 12 play points coupon tiers`() {
    assertEquals(12, PlayPointsCoupons.ALL.size)
    val expectedValues = listOf(1, 2, 3, 4, 5, 6, 10, 15, 20, 25, 30, 40)
    val actualValues = PlayPointsCoupons.ALL.map { it.discountDollars }
    assertEquals(expectedValues, actualValues)
  }
}
