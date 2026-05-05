package com.ssblur.creeper_crush.data

import com.ssblur.creeper_crush.CreeperCrush
import com.ssblur.unfocused.serialization.KClassCodec
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.world.level.saveddata.SavedDataType

class PlayerDateCondition(data: Map<String, Map<String, Boolean>>?): SavedData() {
  var conditions: MutableMap<String, MutableMap<String, Boolean>>
  init {
    conditions = data?.mapValues { (key, value) -> value.toMutableMap() }?.toMutableMap() ?: mutableMapOf()
  }

  fun getCondition(entity: Entity, condition: String): Boolean {
    val id = entity.uuid.toString()
    conditions[id] = conditions[id] ?: mutableMapOf()
    return conditions[id]!![condition] ?: false
  }

  companion object {
    val CODEC = KClassCodec.codec(PlayerDateCondition::class)

    fun computeIfAbsent(player: Player): PlayerDateCondition? {
      val level = player.level()
      val server = level.server
      val id = player.uuid
      return server?.dataStorage?.computeIfAbsent(
        SavedDataType(
          CreeperCrush.location("player/$id"),
          { PlayerDateCondition(mapOf()) },
          CODEC,
          DataFixTypes.PLAYER
        )
      )
    }
  }
}