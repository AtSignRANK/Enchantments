package io.github.rank.plugin

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class EnergyListener: Listener {
    private fun Double.calculate(energyLevel: Int): Double {
        return this.times(1 + 0.2 * energyLevel)
    }

    @EventHandler
    fun onPlayerAttackAtEntity(event: EntityDamageByEntityEvent) {
        val player = event.damager
        if (player !is Player) return

        var sum = 0
        if (player.inventory.helmet != null) sum += player.inventory.helmet?.psionicsLevel!!
        if (player.inventory.chestplate != null) sum += player.inventory.chestplate?.psionicsLevel!!
        if (player.inventory.leggings != null) sum += player.inventory.leggings?.psionicsLevel!!
        if (player.inventory.boots != null) sum += player.inventory.boots?.psionicsLevel!!
        event.damage = event.damage.calculate(sum)
    }
}