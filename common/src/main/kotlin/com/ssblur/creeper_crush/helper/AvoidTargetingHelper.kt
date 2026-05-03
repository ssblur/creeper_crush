package com.ssblur.creeper_crush.helper

import com.ssblur.creeper_crush.CreeperCrush
import com.ssblur.creeper_crush.data.PlayerDateCondition
import com.ssblur.unfocused.extension.ItemStackExtension.matches
import net.minecraft.world.entity.Entity

object AvoidTargetingHelper {
  fun shouldAvoidAttacking(entity: Entity): Boolean {
    entity.level().players().forEach {
      // if there's a nearby player with a love letter
      if(it.distanceTo(entity) < 16 && it.mainHandItem matches CreeperCrush.LETTER.get()) {
        return true
      }

      // if anyone has started dating this creeper
      if(PlayerDateCondition.computeIfAbsent(it)?.conditions[entity.stringUUID] != null) {
        return true
      }
    }
    return false
  }
}