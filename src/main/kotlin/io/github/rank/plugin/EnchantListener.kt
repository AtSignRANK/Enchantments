package io.github.rank.plugin

import com.destroystokyo.paper.event.inventory.PrepareResultEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.inventory.InventoryType
import kotlin.math.roundToInt
import kotlin.random.Random.Default.nextFloat
import kotlin.random.Random.Default.nextInt

class EnchantListener: Listener {
    @EventHandler(ignoreCancelled = true)
    fun onEnchantItem(event: EnchantItemEvent) {
        val item = event.item
        val enchantAbility = item.type.enchantability

        if (enchantAbility <= 0) return
        if (nextInt(3) != 0) return // 1/3 확률로 인챈트 실패

        val randEnchantAbility = 1 + nextInt(enchantAbility / 4 + 1) + nextInt(enchantAbility / 4 + 1)
        val k = event.expLevelCost + randEnchantAbility
        val randBonusPercent = 1.0F + (nextFloat() + nextFloat() - 1.0F) * 0.15F
        var finalLevel = (k * randBonusPercent).roundToInt()

        if (finalLevel < 1.0F) finalLevel = 1

        item.psionicsLevel = when(finalLevel) {
            in 1 until 12 -> 1
            in 12 until 23 -> 2
            in 23 until 34 -> 3
            in 34 until 45 -> 4
            else -> 0
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPrepareResult(event: PrepareResultEvent) {
        val inv = event.inventory
        val invType = event.inventory.type

        @Suppress("USELESS_ELVIS")
        if (invType == InventoryType.GRINDSTONE) {
            event.result?.apply {
                psionicsLevel = 0
            }
        } else if (invType == InventoryType.ANVIL) {
            event.result?.apply {
                val contents = inv.storageContents
                val first = contents[0] ?: return@apply
                val second = contents[1] ?: return@apply
                var level = first.psionicsLevel.coerceAtLeast(second.psionicsLevel)

                if (level > 0) {
                    if (first == second)
                        level++

                    psionicsLevel = 5.coerceAtMost(level)
                }
            }
        }
    }
}