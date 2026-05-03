package com.ssblur.creeper_crush.screen

import com.ssblur.creeper_crush.data.Dialogue
import com.ssblur.creeper_crush.menu.DialogueMenu
import com.ssblur.creeper_crush.network.CreeperCrushC2S
import com.ssblur.unfocused.helper.LocalizedMarkdownReader
import com.ssblur.unfocused.screen.UnfocusedScreen
import com.ssblur.unfocused.screen.widget.ButtonWidget
import com.ssblur.unfocused.screen.widget.MarkdownWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.*

class DialogueScreen(menu: DialogueMenu, inventory: Inventory, component: Component):
  UnfocusedScreen<DialogueMenu>(menu, inventory, component, 480, 200) {
  var dialogue: Identifier? = null
  private var lastDialogue: Identifier? = null
  private var data: Dialogue.DialogueEntry? = null

  override fun init() {
    leftPos = this.width - imageWidth
    topPos = this.height - imageHeight
    dialogue = menu.location
    build()
  }

  override fun rebuildWidgets() {
    super.rebuildWidgets()
    build()
  }

  override fun containerTick() {
    dialogue = menu.location
    if(lastDialogue != dialogue) rebuildWidgets()
    super.containerTick()
  }

  override fun extractRenderState(guiGraphics: GuiGraphicsExtractor, i: Int, j: Int, f: Float) {
    val uuid = menu.uuid
    Minecraft.getInstance().level?.getEntity(UUID.fromString(uuid))?.let { entity ->
      val state = Minecraft.getInstance().entityRenderDispatcher.extractEntity(entity, f)
      val rot = Quaternionf().rotateX(Math.PI.toFloat())
      val x = imageWidth - 200
      guiGraphics.entity(
        state,
        100.0f,
        Vector3f(0.0f, state.boundingBoxHeight / 2.0f, 0.0f),
        rot,
        null,
        x,
        0,
        x + 300,
        300
      )
    }
    super.extractRenderState(guiGraphics, i, j, f)
  }

  fun build() {
    clearWidgets()
    data = Dialogue.entries[dialogue]
    lastDialogue = dialogue
    if(data == null) return
    val data = data!!

    var y = topPos + (imageHeight - 160)
    // dialogue box
    add(MarkdownWidget(
      leftPos + 10,
      y,
      imageWidth - 20,
      90,
      LocalizedMarkdownReader.read(data.dialogue!!)
    )).setColor(230, 255, 230)
    y += 80
    // dialogue options
    data.options?.forEach {
      y += 24
      add(ButtonWidget(
        leftPos + 20,
        y,
        width / 2 - 40,
        22,
        Component.translatable(it.key)
      ) {
        CreeperCrushC2S.pickDialogue(CreeperCrushC2S.PickDialogue(it.key))
      })
    }

    if(data.ends_dialogue == true) {
      y += 24
      add(ButtonWidget(
        leftPos + 20,
        y,
        width / 2 - 40,
        22,
        Component.translatable("dialogue.creeper_crush.close")
      ) {
        CreeperCrushC2S.pickDialogue(CreeperCrushC2S.PickDialogue(""))
      })
    }

    // entity emotion overlay
  }
}