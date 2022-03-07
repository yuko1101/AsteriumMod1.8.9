package io.github.yuko1101.asterium.features.addons

import io.github.yuko1101.asterium.Asterium
import net.minecraft.client.Minecraft
import net.minecraft.entity.passive.EntityBat
import net.minecraftforge.client.event.*
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object EventEmitter {
    private val mc = Minecraft.getMinecraft()

    // Chat

    @SubscribeEvent
    fun onClientChatReceiveEvent(event: ClientChatReceivedEvent) {
        getListeners().forEach {listener -> listener.onClientChatReceiveEvent(event) }
    }


    // Tick

    @SubscribeEvent
    fun onTickEvent(event: TickEvent) {
        getListeners().forEach {listener -> listener.onTickEvent(event) }
    }
    @SubscribeEvent
    fun onTickEventRenderTickEvent(event: TickEvent.RenderTickEvent) {
        getListeners().forEach {listener -> listener.onTickEventRenderTickEvent(event) }
    }
    @SubscribeEvent
    fun onTickEventPlayerTickEvent(event: TickEvent.PlayerTickEvent) {
        getListeners().forEach {listener -> listener.onTickEventPlayerTickEvent(event) }
    }


    // Render

    @SubscribeEvent
    fun onRenderWorldLastEvent(event: RenderWorldLastEvent) {
        getListeners().forEach {listener -> listener.onRenderWorldLastEvent(event) }
    }
    @SubscribeEvent
    fun onRenderWorldEvent(event: RenderWorldEvent) {
        getListeners().forEach {listener -> listener.onRenderWorldEvent(event) }
    }

    @SubscribeEvent
    fun onRenderLivingEvent(event: RenderLivingEvent<*>) {
        getListeners().forEach {listener -> listener.onRenderLivingEvent(event) }
    }
    @SubscribeEvent
    fun onRenderLivingEventPre(event: RenderLivingEvent.Pre<*>) {
        getListeners().forEach {listener -> listener.onRenderLivingEventPre(event) }
    }
    @SubscribeEvent
    fun onRenderLivingEventPost(event: RenderLivingEvent.Post<*>) {
        getListeners().forEach {listener -> listener.onRenderLivingEventPost(event) }
    }
    @SubscribeEvent
    fun onRenderLivingEventSpecials(event: RenderLivingEvent.Specials<*>) {
        getListeners().forEach {listener -> listener.onRenderLivingEventSpecials(event) }
    }

    @SubscribeEvent
    fun onRenderGameOverlayEvent(event: RenderGameOverlayEvent) {
        getListeners().forEach {listener -> listener.onRenderGameOverlayEvent(event) }
    }
    @SubscribeEvent
    fun onRenderGameOverlayEventPre(event: RenderGameOverlayEvent.Pre) {
        getListeners().forEach {listener -> listener.onRenderGameOverlayEventPre(event) }
    }
    @SubscribeEvent
    fun onRenderGameOverlayEventPost(event: RenderGameOverlayEvent.Post) {
        getListeners().forEach {listener -> listener.onRenderGameOverlayEventPost(event) }
    }
    @SubscribeEvent
    fun onRenderGameOverlayEventText(event: RenderGameOverlayEvent.Text) {
        getListeners().forEach {listener -> listener.onRenderGameOverlayEventText(event) }
    }
    @SubscribeEvent
    fun onRenderGameOverlayEventChat(event: RenderGameOverlayEvent.Chat) {
        getListeners().forEach {listener -> listener.onRenderGameOverlayEventChat(event) }
    }


    // GUI

    @SubscribeEvent
    fun onGuiScreenEventDrawScreenEvent(event: GuiScreenEvent.DrawScreenEvent) {
        getListeners().forEach {listener -> listener.onGuiScreenEventDrawScreenEvent(event) }
    }
    @SubscribeEvent
    fun onGuiScreenEventDrawScreenEventPre(event: GuiScreenEvent.DrawScreenEvent.Pre) {
        getListeners().forEach {listener -> listener.onGuiScreenEventDrawScreenEventPre(event) }
    }
    @SubscribeEvent
    fun onGuiScreenEventDrawScreenEventPost(event: GuiScreenEvent.DrawScreenEvent.Post) {
        getListeners().forEach {listener -> listener.onGuiScreenEventDrawScreenEventPost(event) }
    }


    @SubscribeEvent
    fun onGuiScreenEventInitGuiEvent(event: GuiScreenEvent.InitGuiEvent) {
        getListeners().forEach {listener -> listener.onGuiScreenEventInitGuiEvent(event) }
    }
    @SubscribeEvent
    fun onGuiScreenEventInitGuiEventPre(event: GuiScreenEvent.InitGuiEvent.Pre) {
        getListeners().forEach {listener -> listener.onGuiScreenEventInitGuiEventPre(event) }
    }
    @SubscribeEvent
    fun onGuiScreenEventInitGuiEventPost(event: GuiScreenEvent.InitGuiEvent.Post) {
        getListeners().forEach {listener -> listener.onGuiScreenEventInitGuiEventPost(event) }
    }


    @SubscribeEvent
    fun onGuiScreenEventKeyboardInputEvent(event: GuiScreenEvent.KeyboardInputEvent) {
        getListeners().forEach {listener -> listener.onGuiScreenEventKeyboardInputEvent(event) }
    }
    @SubscribeEvent
    fun onGuiScreenEventKeyboardInputEventPre(event: GuiScreenEvent.KeyboardInputEvent.Pre) {
        getListeners().forEach {listener -> listener.onGuiScreenEventKeyboardInputEventPre(event) }
    }
    @SubscribeEvent
    fun onGuiScreenEventKeyboardInputEventPost(event: GuiScreenEvent.KeyboardInputEvent.Post) {
        getListeners().forEach {listener -> listener.onGuiScreenEventKeyboardInputEventPost(event) }
    }

    @SubscribeEvent
    fun onGuiScreenEventMouseInputEvent(event: GuiScreenEvent.MouseInputEvent) {
        getListeners().forEach {listener -> listener.onGuiScreenEventMouseInputEvent(event) }
    }
    @SubscribeEvent
    fun onGuiScreenEventMouseInputEventPre(event: GuiScreenEvent.MouseInputEvent.Pre) {
        getListeners().forEach {listener -> listener.onGuiScreenEventMouseInputEventPre(event) }
    }
    @SubscribeEvent
    fun onGuiScreenEventMouseInputEventPost(event: GuiScreenEvent.MouseInputEvent.Post) {
        getListeners().forEach {listener -> listener.onGuiScreenEventMouseInputEventPost(event) }
    }


    @SubscribeEvent
    fun onGuiOpenEvent(event: GuiOpenEvent) {
        getListeners().forEach {listener -> listener.onGuiOpenEvent(event) }
    }


    // Player


    @SubscribeEvent
    fun onAttackEntityEvent(event: AttackEntityEvent) {
        getListeners().forEach {listener -> listener.onAttackEntityEvent(event) }
    }
    @SubscribeEvent
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        getListeners().forEach {listener -> listener.onPlayerInteractEvent(event) }
    }



    // Controls


    @SubscribeEvent
    fun onInputEventKeyInputEvent(event: InputEvent.KeyInputEvent) {
        getListeners().forEach {listener -> listener.onInputEventKeyInputEvent(event) }
    }
    @SubscribeEvent
    fun onInputEventMouseInputEvent(event: InputEvent.MouseInputEvent) {
        getListeners().forEach {listener -> listener.onInputEventMouseInputEvent(event) }
    }


    private fun getListeners(): List<ExtraEventListener> {
        val listeners = arrayListOf<ExtraEventListener>()
        AddonManager.getAddonMetaDataList().forEach { addonMetaData -> listeners.addAll(addonMetaData.eventListeners) }
        return listeners
    }

}