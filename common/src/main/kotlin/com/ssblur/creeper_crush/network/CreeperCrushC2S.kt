package com.ssblur.creeper_crush.network

import com.ssblur.creeper_crush.CreeperCrush
import com.ssblur.creeper_crush.data.Dialogue
import com.ssblur.creeper_crush.data.PlayerDateCondition
import com.ssblur.creeper_crush.menu.DialogueMenu
import com.ssblur.unfocused.menu.SimpleMenuProvider
import com.ssblur.unfocused.network.NetworkManager
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.LivingEntity
import kotlin.collections.get
import kotlin.jvm.optionals.getOrNull

object CreeperCrushC2S {
  data class PickDialogue(val choice: String)
  val pickDialogue = NetworkManager.registerC2S(
    CreeperCrush.location("client_pick_dialogue"),
    PickDialogue::class
  ) { (choice), player ->
    val menu = player.containerMenu
    if(menu is DialogueMenu) {
      val data = Dialogue.entries[menu.location]
      if(data?.ends_dialogue == true) {
        player.closeContainer()
      }
      val option = data?.options?.get(choice)?.let { CreeperCrush.location(it) }
      if(option != null) {
        val pick = Dialogue.entries[option]!!
        menu.location = pick.dialogue
        val condition = PlayerDateCondition.computeIfAbsent(player)
        pick.change_condition?.forEach {
          condition?.conditions[menu.uuid]?.set(it.key, it.value)
          condition?.setDirty()
        }
      }
    }
  }

  data class OpenDialogue(val id: Int)
  val openDialogue = NetworkManager.registerC2S(
    CreeperCrush.location("client_open_dialogue"),
    OpenDialogue::class
  ) { (id), player ->
    val entity = player.level().getEntity(id)
    Dialogue.entries.entries.filter {
      it.value.condition?.all { c ->
        PlayerDateCondition.computeIfAbsent(player)!!.getCondition(entity as LivingEntity, c.key) == c.value
      } ?: true
    }.filter{
      BuiltInRegistries.ENTITY_TYPE.get(it.value.entity!!).getOrNull()?.value() == entity?.type
    }.randomOrNull()?.let {
      player.openMenu(SimpleMenuProvider { i, inventory, _ ->
        val menu = DialogueMenu(i, inventory, entity as LivingEntity)
        menu.location = it.key
        menu.uuid = entity.stringUUID
        menu
      })
      val condition = PlayerDateCondition.computeIfAbsent(player)
      it.value.change_condition?.forEach {
        condition?.conditions[entity!!.stringUUID]?.set(it.key, it.value)
        condition?.setDirty()
      }
    }
  }

  fun init() {}
}