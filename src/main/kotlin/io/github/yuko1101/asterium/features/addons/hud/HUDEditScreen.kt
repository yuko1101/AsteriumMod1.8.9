package io.github.yuko1101.asterium.features.addons.hud

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.addons.hud.position.AbsoluteScreenPosition
import io.github.yuko1101.asterium.features.addons.hud.position.RelativeScreenPosition
import io.github.yuko1101.asterium.features.addons.hud.position.ScreenPosition
import io.github.yuko1101.asterium.utils.minecraft.DrawUtils
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Keyboard

class HUDEditScreen : GuiScreen() {

    val features: List<DraggableFeature>
        get() = Asterium.hudManager.features.filterIsInstance<DraggableFeature>()

    var dragging: DraggableFeature? = null
    var selected: DraggableFeature? = null
    var resizing: DraggableFeature? = null

    override fun initGui() {
        for (feature in features) {
            println("${feature.name}-${feature.addon.getAddonMetaData().uuid}: ${feature.position.x}, ${feature.position.y} scale: ${feature.scale}, visible: ${feature.visible}")
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        for (feature in features) {
            val isSelected = selected == feature

            val borderColor = if (isSelected) 0xFFDAA520 else 0xFFFFFFFF

            GlStateManager.pushMatrix()
            GlStateManager.translate(feature.position.x, feature.position.y, 0f)
            GlStateManager.scale(feature.scale.toDouble(), feature.scale.toDouble(), 1.0)
            feature.renderDummy()
            DrawUtils.drawHollowRect(0F, 0F, feature.width, feature.height, 0.5F, borderColor)
            GlStateManager.popMatrix()
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        when (keyCode) {
            Keyboard.KEY_ESCAPE -> {
                if (selected != null) {
                    selected = null
                    return
                }
                mc.displayGuiScreen(null)
                onGuiClosed()
            }
        }
    }

    private var preMouseX: Int? = null
    private var preMouseY: Int? = null
    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        val feature = getFeatureWithPos(mouseX.toFloat(), mouseY.toFloat())
        selected = feature
        dragging = feature

        preMouseX = mouseX
        preMouseY = mouseY
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        if (dragging != null) {
            val target = dragging!!

            val movedX = target.position.x + (mouseX - preMouseX!!).toFloat()
            val movedY = target.position.y + (mouseY - preMouseY!!).toFloat()

            val newPosition: ScreenPosition = if (target.position is RelativeScreenPosition) {
                val res = Asterium.scaledResolution
                RelativeScreenPosition((movedX.toDouble() / res.scaledWidth), movedY.toDouble() / res.scaledHeight)
            } else {
                AbsoluteScreenPosition(movedX, movedY)
            }

            target.position = newPosition
            target.position = target.adjustBounds()
        }
        preMouseX = mouseX
        preMouseY = mouseY
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        dragging = null
    }

    override fun onGuiClosed() {
        Asterium.hudManager.hudConfig.save()
    }

    override fun doesGuiPauseGame(): Boolean {
        return true
    }

    private fun getFeatureWithPos(x: Float, y: Float): DraggableFeature? {
        val matched = features.filter { isFeatureAtPos(it, x, y) }
        return matched.minByOrNull { feature -> feature.width * feature.scale * feature.height * feature.scale }
    }

    private fun isFeatureAtPos(feature: DraggableFeature, x: Float, y: Float): Boolean {
        val sx = feature.position.x
        val sy = feature.position.y
        val ex = feature.position.x + feature.scale * feature.width
        val ey = feature.position.y + feature.scale * feature.height
        return (x in sx..ex && y in sy..ey)
    }

}