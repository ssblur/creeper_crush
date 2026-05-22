package com.ssblur.creeper_crush.data

import com.ssblur.creeper_crush.CreeperCrush
import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.commands.CommandSource
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.permissions.PermissionSet
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import kotlin.jvm.optionals.getOrNull

object Dialogue {
  data class SpriteLocation(
    val location: Identifier,
    val x: Int,
    val y: Int,
    val w: Int,
    val h: Int
  )

  data class DialogueEntry(
    val entity: Identifier?,
    val condition: Map<String, Boolean>?,
    val dialogue: Identifier?,
    val options: Map<String, String>?,
    val emotion: String?,
    val textbox: String?,
    val change_condition: Map<String, Boolean>?,
    val ends_dialogue: Boolean?,
    val sprites: List<SpriteLocation>?,
    val commands: List<String>?,

    val requires: String?, // required mod
  )

  val entries = CreeperCrush.registerSimpleDataLoader("creeper_crush/dialogue", DialogueEntry::class)

  fun validEntries(player: Player, entity: Entity?): Map<Identifier, DialogueEntry> {
    if(entity == null) return mapOf()
    return entries.filter { (_, v) ->
      v.requires == null || Unfocused.isModLoaded(v.requires)
    }.filter {
      it.value.condition?.all { c ->
        PlayerDateCondition.computeIfAbsent(player)!!.getCondition(entity, c.key) == c.value
      } ?: true
    }.filter{
      BuiltInRegistries.ENTITY_TYPE.get(it.value.entity!!).getOrNull()?.value() == entity.type
    }
  }

  fun randomEntry(player: Player, entity: Entity?) = validEntries(player, entity).entries.randomOrNull()

  fun init() {}

  fun DialogueEntry.runCommands(player: Player, entity: Entity?) {
    println("Running ${commands?.size ?: 0} commands for Dialogue")
    println(entity)
    if(entity == null) return
    val entitySelector = getTargetSelector(entity)
    val substitutions = mapOf(
      $$"$date_uuid" to entity.stringUUID,
      $$"$date_name" to entity.name.string.ifEmpty { "Someone Special" },
      $$"$player_name" to player.name.string.ifEmpty { "My Beloved" },
      "@date" to entitySelector
    )

    player.level().server?.let {
      val commands = it.commands
      this.commands?.forEach { command ->
        var run = command
        substitutions.forEach { (key, value) ->
          run = run.replace(key, value)
        }

        val s = commands.dispatcher.parse(
          run,
          (player as ServerPlayer).createCommandSourceStack()
            .withPermission(PermissionSet.ALL_PERMISSIONS)
        )
        s.exceptions.forEach {
          CreeperCrush.LOGGER.warn(it.value.message)
        }
        commands.performCommand(s, run)
      }
    }
  }

  private fun getTargetSelector(entity: Entity): String {
    val uuid = entity.uuid
    val l = uuid.leastSignificantBits
    val m = uuid.mostSignificantBits
    return String.format(
      "@e[nbt={UUID:[I;%d,%d,%d,%d]}]",
      (m shr 32).toInt(),
      m.toInt(),
      (l shr 32).toInt(),
      l.toInt()
    )
  }
}