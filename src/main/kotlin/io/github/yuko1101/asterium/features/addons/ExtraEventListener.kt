package io.github.yuko1101.asterium.features.addons

import net.minecraftforge.client.event.*
import net.minecraftforge.client.event.sound.SoundEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

interface ExtraEventListener {

    // Chat

    fun onClientChatReceiveEvent(event: ClientChatReceivedEvent) {}

    // Tick

    fun onTickEvent(event: TickEvent) {}
    fun onTickEventRenderTickEvent(event: TickEvent.RenderTickEvent) {}
    fun onTickEventPlayerTickEvent(event: TickEvent.PlayerTickEvent) {}
    fun onTickEventClientTickEvent(event: TickEvent.ClientTickEvent) {}

    // Render

    fun onRenderWorldLastEvent(event: RenderWorldLastEvent) {}
    fun onRenderWorldEvent(event: RenderWorldEvent) {}

    fun onRenderLivingEvent(event: RenderLivingEvent<*>) {}
    fun onRenderLivingEventPre(event: RenderLivingEvent.Pre<*>) {}
    fun onRenderLivingEventPost(event: RenderLivingEvent.Post<*>) {}
    fun onRenderLivingEventSpecials(event: RenderLivingEvent.Specials<*>) {}

    fun onRenderGameOverlayEvent(event: RenderGameOverlayEvent) {}
    fun onRenderGameOverlayEventPre(event: RenderGameOverlayEvent.Pre) {}
    fun onRenderGameOverlayEventPost(event: RenderGameOverlayEvent.Post) {}
    fun onRenderGameOverlayEventText(event: RenderGameOverlayEvent.Text) {}
    fun onRenderGameOverlayEventChat(event: RenderGameOverlayEvent.Chat) {}


    // GUI

    fun onGuiScreenEventDrawScreenEvent(event: GuiScreenEvent.DrawScreenEvent) {}
    fun onGuiScreenEventDrawScreenEventPre(event: GuiScreenEvent.DrawScreenEvent.Pre) {}
    fun onGuiScreenEventDrawScreenEventPost(event: GuiScreenEvent.DrawScreenEvent.Post) {}

    fun onGuiScreenEventInitGuiEvent(event: GuiScreenEvent.InitGuiEvent) {}
    fun onGuiScreenEventInitGuiEventPre(event: GuiScreenEvent.InitGuiEvent.Pre) {}
    fun onGuiScreenEventInitGuiEventPost(event: GuiScreenEvent.InitGuiEvent.Post) {}

    fun onGuiScreenEventKeyboardInputEvent(event: GuiScreenEvent.KeyboardInputEvent) {}
    fun onGuiScreenEventKeyboardInputEventPre(event: GuiScreenEvent.KeyboardInputEvent.Pre) {}
    fun onGuiScreenEventKeyboardInputEventPost(event: GuiScreenEvent.KeyboardInputEvent.Post) {}

    fun onGuiScreenEventMouseInputEvent(event: GuiScreenEvent.MouseInputEvent) {}
    fun onGuiScreenEventMouseInputEventPre(event: GuiScreenEvent.MouseInputEvent.Pre) {}
    fun onGuiScreenEventMouseInputEventPost(event: GuiScreenEvent.MouseInputEvent.Post) {}

    fun onGuiOpenEvent(event: GuiOpenEvent) {}



    // Player

    fun onAttackEntityEvent(event: AttackEntityEvent) {}
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {}



    // Controls

    fun onInputEventKeyInputEvent(event: InputEvent.KeyInputEvent) {}
    fun onInputEventMouseInputEvent(event: InputEvent.MouseInputEvent) {}


    // World


    fun onWorldEvent(event: WorldEvent) {}
    fun onWorldEventLoad(event: WorldEvent.Load) {}
    fun onWorldEventSave(event: WorldEvent.Save) {}
    fun onWorldEventUnload(event: WorldEvent.Unload) {}
    fun onWorldEventCreateSpawnPosition(event: WorldEvent.CreateSpawnPosition) {}
    fun onWorldEventPotentialSpawns(event: WorldEvent.PotentialSpawns) {}


//    // Sound
//
//    fun onSoundEvent(event: SoundEvent) {}
//    fun onSoundEventSoundSourceEvent(event: SoundEvent.SoundSourceEvent) {}




}