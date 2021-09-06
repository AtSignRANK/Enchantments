package io.github.rank.plugin

import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.lang.NumberFormatException

class EnchantPlugin: JavaPlugin() {
    override fun onEnable() {
        server.pluginManager.run {
            registerEvents(EnchantListener(), this@EnchantPlugin)
            registerEvents(EnergyListener(), this@EnchantPlugin)
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (command.name) {
            "enchant" -> {
                if (sender !is Player) return false

                if (args.isNotEmpty() && args[0] == "enchantments:energy") {
                    if (!sender.inventory.itemInMainHand.isAllowedEnchantEnergy()) {
                        sender.sendMessage(Component.text("${ChatColor.RED}이 아이템에 이 인첸트를 할 수 없습니다!"))
                        return false
                    }
                    var psionicsLevel = 1
                    if (args.size > 1) {
                        if (args.size == 2) {
                            try {
                                psionicsLevel = args[1].toInt()
                            } catch (exception: NumberFormatException) {
                                exception.printStackTrace()
                            }
                        } else {
                            sender.sendMessage(Component.text("${ChatColor.RED}인수가 너무 많습니다!"))
                            return false
                        }
                    }
                    sender.inventory.itemInMainHand.psionicsLevel = psionicsLevel
                    sender.inventory.itemInMainHand.addUnsafeEnchantment(Enchantment.LUCK, 1)
                }
            }
        }
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {
        val commandList = mutableListOf<String>()

        if (command.name == "enchant") {
            when (args.size) {
                1 -> {
                    commandList.add("enchantments:energy")

                    return commandList
                }
            }
        }
        return null
    }
}