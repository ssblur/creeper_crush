package com.ssblur.creeper_crush.helper

import com.ssblur.creeper_crush.CreeperCrush
import com.ssblur.creeper_crush.data.Dialogue
import com.ssblur.creeper_crush.network.CreeperCrushC2S
import com.ssblur.unfocused.extension.ItemStackExtension.matches
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player

object ReplaceInteractHelper {
  fun replaceInteract(player: Player, entity: Entity): Boolean {
    if(!(player.getItemInHand(InteractionHand.MAIN_HAND) matches CreeperCrush.LETTER.get()
          || player.getItemInHand(InteractionHand.OFF_HAND) matches CreeperCrush.LETTER.get()))
      return false
    if(Dialogue.noneMatch(entity)) return false

    CreeperCrushC2S.openDialogue(CreeperCrushC2S.OpenDialogue(entity.id))

    return true
  }
}