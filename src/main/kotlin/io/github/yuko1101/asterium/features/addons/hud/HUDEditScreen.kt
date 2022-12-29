package io.github.yuko1101.asterium.features.addons.hud

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.universal.UKeyboard
import gg.essential.universal.UMatrixStack
import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.addons.hud.position.AbsoluteScreenPosition
import io.github.yuko1101.asterium.features.addons.hud.position.RelativeScreenPosition
import io.github.yuko1101.asterium.features.addons.hud.position.ScreenPosition
import io.github.yuko1101.asterium.utils.minecraft.DrawUtils
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Keyboard

class HUDEditScreen : WindowScreen(ElementaVersion.V2) {

    val features: List<DraggableFeature>
        get() = Asterium.hudManager.features.filterIsInstance<DraggableFeature>()

    var dragging: DraggableFeature? = null
    var selected: DraggableFeature? = null
    var resizing: DraggableFeature? = null

    override fun initScreen(width: Int, height: Int) {
        for (feature in features) {
            println("${feature.name}-${feature.addon.getAddonMetaData().uuid}: ${feature.position.x}, ${feature.position.y} scale: ${feature.scale}, visible: ${feature.visible}")
        }
    }

    private var preMouseX: Double? = null
    private var preMouseY: Double? = null
    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        for (feature in features) {
            val isSelected = selected == feature

            val borderColor = if (isSelected) 0xFFDAA520 else 0xFFFFFFFF

            if (dragging == feature) {
                // this if statement may be needless because dragging is always null before calling onMouseClicked.
                if (preMouseX != null && preMouseY != null) {
                    moveFeature(feature, mouseX - preMouseX!!, mouseY - preMouseY!!)
                }

                preMouseX = mouseX.toDouble()
                preMouseY = mouseY.toDouble()
            }
            GlStateManager.pushMatrix()
            GlStateManager.translate(feature.position.x, feature.position.y, 0f)
            GlStateManager.scale(feature.scale.toDouble(), feature.scale.toDouble(), 1.0)
            feature.renderDummy()
            DrawUtils.drawHollowRect(0F, 0F, feature.width, feature.height, 0.5F, borderColor)
            GlStateManager.popMatrix()
        }
    }

    override fun onKeyPressed(keyCode: Int, typedChar: Char, modifiers: UKeyboard.Modifiers?) {
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

    override fun onMouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        val feature = getFeatureWithPos(mouseX.toFloat(), mouseY.toFloat())
        selected = feature
        dragging = feature

        preMouseX = mouseX
        preMouseY = mouseY
    }

    override fun onMouseReleased(mouseX: Double, mouseY: Double, state: Int) {
        dragging = null
    }

    override fun onScreenClose() {
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

    private fun moveFeature(feature: DraggableFeature, offsetX: Double, offsetY: Double) {

        val movedX = feature.position.x + offsetX.toFloat()
        val movedY = feature.position.y + offsetY.toFloat()

        val newPosition: ScreenPosition = if (feature.position is RelativeScreenPosition) {
            val res = Asterium.scaledResolution
            RelativeScreenPosition((movedX.toDouble() / res.scaledWidth), movedY.toDouble() / res.scaledHeight)
        } else {
            AbsoluteScreenPosition(movedX, movedY)
        }

        feature.position = newPosition
        feature.position = feature.adjustBounds()
    }

}