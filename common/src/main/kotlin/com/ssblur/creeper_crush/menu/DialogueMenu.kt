package com.ssblur.creeper_crush.menu

import com.ssblur.creeper_crush.CreeperCrush
import com.ssblur.unfocused.Unfocused
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class DialogueMenu(i: Int, val inventory: Inventory? = null, val entity: Entity?):
  AbstractContainerMenu(CreeperCrush.DIALOGUE_MENU.get(), i) {
  var location: Identifier?
    get() = slot.item[DataComponents.ITEM_NAME]?.string?.let { Unfocused.location(it) }
    set(value) {
      val item = ItemStack(Items.STICK)
      item[DataComponents.ITEM_NAME] = value?.let { Component.literal(it.toString()) }
      slot.set(item)
    }
  private val container = SimpleContainer(2)
  private val slot: Slot = addSlot(Slot(container, 0, Integer.MAX_VALUE - 40, Integer.MAX_VALUE - 40))

  var uuid: String?
    get() = slote.item[DataComponents.ITEM_NAME]?.string
    set(value) {
      val item = ItemStack(Items.STICK)
      item[DataComponents.ITEM_NAME] = value?.let { Component.literal(it) }
      slote.set(item)
    }
  private val slote: Slot = addSlot(Slot(container, 1, Integer.MAX_VALUE - 40, Integer.MAX_VALUE - 40))

  init {
    uuid = entity?.uuid?.toString()
  }

  override fun quickMoveStack(
    player: Player,
    slotIndex: Int
  ): ItemStack = ItemStack.EMPTY

  override fun stillValid(player: Player): Boolean = if(entity == null) true else player.distanceTo(entity) < 16
}