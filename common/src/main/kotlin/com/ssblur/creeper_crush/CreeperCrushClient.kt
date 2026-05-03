package com.ssblur.creeper_crush

import com.ssblur.creeper_crush.screen.DialogueScreen
import com.ssblur.unfocused.event.client.ClientScreenRegistrationEvent.registerScreen

object CreeperCrushClient {
  fun init() {
    CreeperCrush.DIALOGUE_MENU.then {
      CreeperCrush.registerScreen(it, ::DialogueScreen)
    }
  }
}