package com.ssblur.creeper_crush.network

import com.ssblur.creeper_crush.CreeperCrush
import com.ssblur.creeper_crush.data.Dialogue
import com.ssblur.unfocused.network.NetworkManager
import net.minecraft.resources.Identifier

object CreeperCrushS2C {
  data class DialoguePacket(val id: Identifier, val dialogue: Dialogue.DialogueEntry)
  val syncDate = NetworkManager.registerS2C(
    CreeperCrush.location("sync_dialogue"),
    DialoguePacket::class) { (id, dialogue) ->
    Dialogue.entries[id] = dialogue
  }

  fun init() {}
}