package com.ssblur.creeper_crush.data

import com.ssblur.creeper_crush.CreeperCrush
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.resources.Identifier

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
  )

  val entries = CreeperCrush.registerSimpleDataLoader("creeper_crush/dialogue", DialogueEntry::class)

  fun init() {}
}