package com.ssblur.creeper_crush.network

import com.ssblur.creeper_crush.CreeperCrush
import com.ssblur.creeper_crush.data.Dialogue
import com.ssblur.creeper_crush.data.Dialogue.runCommands
import com.ssblur.creeper_crush.data.PlayerDateCondition
import com.ssblur.creeper_crush.menu.DialogueMenu
import com.ssblur.unfocused.menu.SimpleMenuProvider
import com.ssblur.unfocused.network.NetworkManager

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
    Dialogue.randomEntry(player, entity)?.let {
      player.openMenu(SimpleMenuProvider { i, inventory, _ ->
        val menu = DialogueMenu(i, inventory, entity)
        menu.location = it.key
        menu.uuid = entity?.stringUUID
        menu
      })
      val condition = PlayerDateCondition.computeIfAbsent(player)
      it.value.change_condition?.forEach {
        condition?.conditions[entity!!.stringUUID]?.set(it.key, it.value)
        condition?.setDirty()
      }
      it.value.runCommands(player, entity)
    }
  }

  fun init() {}
}