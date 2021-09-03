package io.github.rank.plugin

import io.github.monun.kommand.kommand
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class EnchantPlugin: JavaPlugin() {
    override fun onEnable() {
        server.pluginManager.run {
            registerEvents(EnchantListener(), this@EnchantPlugin)
            registerEvents(EnergyListener(), this@EnchantPlugin)
        }
    }
}