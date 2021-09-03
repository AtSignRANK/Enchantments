package io.github.rank.plugin

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

object EnergyEnchantments {
    val energyTag =
        Component.text().content("에너지").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY).build()

    internal val enchantabilities: Map<Material, Int> = EnumMap<Material, Int>(Material::class.java).apply {
        val armors = Material.values().filter { item ->
            item.isItem && item.equipmentSlot.let { slot ->
                slot != EquipmentSlot.HAND && slot != EquipmentSlot.OFF_HAND
            }
        }

        fun putAllArmors(name: String, value: Int) {
            armors.filter { it.name.startsWith(name) }.forEach {
                this[it] = value
            }
        }

        putAllArmors("LEATHER", 15)
        putAllArmors("CHAINMAIL", 12)
        putAllArmors("IRON", 9)
        putAllArmors("GOLDEN", 25)
        putAllArmors("DIAMOND", 10)
        putAllArmors("TURTLE", 9)
        putAllArmors("NETHERITE", 15)
    }
}

fun ItemStack.isAllowedEnchantEnergy(): Boolean {
    val armors = Material.values().filter { item ->
        item.isItem && item.equipmentSlot.let { slot ->
            slot != EquipmentSlot.HAND && slot != EquipmentSlot.OFF_HAND
        }
    }

    val allowedList = mutableListOf<Material>()
    fun filter(name: String) {
        armors.filter { it.name.startsWith(name) }.forEach {
            allowedList.add(it)
        }
    }

    return allowedList.contains(this.type)
}

var ItemStack.psionicsLevel
    get() = itemMeta.transcendenceLevel
    set(value) {
        itemMeta = itemMeta.apply {
            transcendenceLevel = value
        }
    }

var ItemMeta.transcendenceLevel
    get() = lore()?.find { lore ->
        lore is TextComponent && lore.content() == EnergyEnchantments.energyTag.content()
    }?.let { tag ->
        tag.children().firstOrNull()?.color()?.value()
    } ?: 0
    set(value) {
        val lore = lore() ?: ArrayList()
        lore.removeIf { it is TextComponent && it.content() == EnergyEnchantments.energyTag.content() }

        if (value > 0) {
            lore.add(
                0, EnergyEnchantments.energyTag.children(
                    listOf(
                        Component.space().color(TextColor.color(value)),
                        Component.text().content(value.toRomanNumerals()).color(NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, false).build()
                    )
                )
            )
        }

        lore(lore)
    }

private fun Int.toRomanNumerals() = when (this) {
    1 -> "I"
    2 -> "II"
    3 -> "III"
    4 -> "IV"
    5 -> "V"
    6 -> "VI"
    7 -> "VII"
    8 -> "VIII"
    9 -> "IX"
    10 -> "X"
    else -> toString()
}

private fun ItemStack.isSimilarLore(other: ItemStack): Boolean {
    val meta = itemMeta
    val otherMeta = other.itemMeta

    return type == other.type && data == other.data && meta.displayName() == itemMeta.displayName() && meta.lore() == otherMeta.lore()
}

val Material.enchantability: Int
    get() = EnergyEnchantments.enchantabilities[this] ?: 0