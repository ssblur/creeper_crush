package com.ssblur.creeper_crush

import com.ssblur.creeper_crush.data.Dialogue
import com.ssblur.creeper_crush.menu.DialogueMenu
import com.ssblur.creeper_crush.network.CreeperCrushC2S
import com.ssblur.creeper_crush.network.CreeperCrushS2C
import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.event.common.PlayerJoinedEvent
import net.minecraft.client.Minecraft
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object CreeperCrush : ModInitializer("creeper_crush") {
    const val MODID = "creeper_crush"
    val LOGGER: Logger = LoggerFactory.getLogger(id)

    val LETTER = registerItem("love_letter") {
      object: Item(
        it.stacksTo(1)
      ) {
        override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
          if(level.isClientSide) {
            Minecraft.getInstance().hitResult?.let {
              if(it.type == HitResult.Type.ENTITY) {
                CreeperCrushC2S.openDialogue(CreeperCrushC2S.OpenDialogue((it as EntityHitResult).entity.id))
              }
            }
          }
          return super.use(level, player, hand)
        }
      }
    }

    val DIALOGUE_MENU = registerMenu("book") {
      MenuType({ id, inventory ->
        DialogueMenu(id, inventory, null)
      }, FeatureFlagSet.of())
    }

    fun init() {
      LOGGER.info("Creeper Crush loaded...")
      Dialogue.init()
      CreeperCrushC2S.init()
      CreeperCrushS2C.init()

      PlayerJoinedEvent.register {
        Dialogue.entries.forEach { (key, value) ->
          CreeperCrushS2C.syncDate(CreeperCrushS2C.DialoguePacket(key, value), listOf(it))
        }
      }
    }

    fun clientInit() {
      LOGGER.info("Creeper Crush loaded clientside...")
      CreeperCrushClient.init()
    }
}