package io.github.yuko1101.asterium.features.hud

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.hud.position.AbsoluteScreenPosition
import io.github.yuko1101.asterium.features.hud.position.RelativeScreenPosition
import io.github.yuko1101.asterium.features.hud.position.ScreenArea
import io.github.yuko1101.asterium.features.hud.position.ScreenPosition
import io.github.yuko1101.asterium.utils.minecraft.DrawUtils
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Keyboard

class HUDEditScreen(val features: List<DraggableFeature>) : GuiScreen() {

    var selectingStart: AbsoluteScreenPosition? = null
    val selected = arrayListOf<DraggableFeature>()
    var dragging = false
    var resizing: DraggableFeature? = null

    override fun initGui() {
        for (feature in features) {
            Asterium.logger.info("${feature.name}-${feature.addon.addonMetaData.uuid}: ${feature.position.x}, ${feature.position.y} scale: ${feature.scale}, visible: ${feature.visible}")
        }
    }

    private var preMousePos: AbsoluteScreenPosition? = null
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val res = Asterium.scaledResolution
        GlStateManager.enableAlpha()
        GlStateManager.enableBlend()
        // draw background
        DrawUtils.drawRect(0F, 0F, res.scaledWidth.toFloat(), res.scaledHeight.toFloat(), 0x80000000)
//        GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ZERO)
        val selectingArea = selectingStart?.let { ScreenArea(it, AbsoluteScreenPosition(mouseX.toFloat(), mouseY.toFloat())) }
        if (selectingArea != null) {
            DrawUtils.drawRect(selectingArea.start.x, selectingArea.start.y, selectingArea.end.x, selectingArea.end.y, 0x80DAA520)
            DrawUtils.drawHollowRect(selectingArea.start.x, selectingArea.start.y, selectingArea.end.x, selectingArea.end.y, 1.5F, 0xFFDAA520)
        }

        for (feature in features) {
            // detect selecting
            if (selectingArea != null) {
                if (isFeatureInArea(feature, selectingArea)) {
                    if (!selected.contains(feature)) selected.add(feature)
                } else {
                    selected.remove(feature)
                }
            }

            val isSelected = selected.contains(feature)

            val borderColor = if (isSelected) 0xFFDAA520 else 0xFFFFFFFF

            if (isSelected && dragging) {
                // this if statement may be needless because dragging is always null before calling onMouseClicked.
                if (preMousePos != null) {
                    moveFeature(feature, mouseX.toFloat() - preMousePos!!.x, mouseY.toFloat() - preMousePos!!.y)
                }
            }
            GlStateManager.pushMatrix()
            GlStateManager.translate(feature.position.x, feature.position.y, 0f)
            GlStateManager.scale(feature.scale.toDouble(), feature.scale.toDouble(), 1.0)
            feature.renderDummy()
            DrawUtils.drawHollowRect(0F, 0F, feature.width, feature.height, 0.5F, borderColor)
            GlStateManager.popMatrix()
        }
        if (dragging) preMousePos = AbsoluteScreenPosition(mouseX.toFloat(), mouseY.toFloat())
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        when (keyCode) {
            Keyboard.KEY_ESCAPE -> {
                if (selected.isNotEmpty()) {
                    selected.clear()
                    return
                }
                mc.displayGuiScreen(null)
                onGuiClosed()
            }
        }
    }

    private var lastClickedPos: AbsoluteScreenPosition? = null
    private var lastClickedTime: Long = 0
    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        val feature = getFeatureWithPos(mouseX.toFloat(), mouseY.toFloat())
        if (feature != null) {
            dragging = true
            if (!selected.contains(feature)) {
                if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && !Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) selected.clear()
                selected.add(feature)
            } else {
                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                    selected.remove(feature)
                } else {
                    // もともと選択されていたHUDのうちの1つをクリックしたとき、Ctrlが押されていなかったら
                    lastClickedPos = AbsoluteScreenPosition(mouseX.toFloat(), mouseY.toFloat())
                    lastClickedTime = System.currentTimeMillis()
                }
            }
        } else {
            selected.clear()
            selectingStart = AbsoluteScreenPosition(mouseX.toFloat(), mouseY.toFloat())
        }

        preMousePos = AbsoluteScreenPosition(mouseX.toFloat(), mouseY.toFloat())
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        dragging = false
        selectingStart = null

        // 最後にクリックした地点とほとんど変わっていなく、時間があまり経っていない場合は、シングルクリックをしたとみなし、選択中のHUDをクリックしたHUDに変更する
        if (lastClickedPos != null) {
            if (lastClickedPos!!.x == mouseX.toFloat() && lastClickedPos!!.y == mouseY.toFloat() && System.currentTimeMillis() - lastClickedTime < 500) {
                lastClickedTime = 0
                val feature = getFeatureWithPos(mouseX.toFloat(), mouseY.toFloat())
                if (feature != null) {
                    selected.clear()
                    selected.add(feature)
                }
            }
        }
    }

    override fun onGuiClosed() {
        for (feature in features) {
            feature.save()
        }
    }

    override fun doesGuiPauseGame(): Boolean {
        return true
    }

    private fun getFeatureWithPos(x: Float, y: Float): DraggableFeature? {
        val matched = features.filter { isFeatureAtPos(it, x, y) }
        return matched.minByOrNull { feature -> feature.width * feature.scale * feature.height * feature.scale }
    }

    private fun isFeatureAtPos(feature: DraggableFeature, x: Float, y: Float): Boolean {
        val ex = feature.position.x + feature.scale * feature.width
        val ey = feature.position.y + feature.scale * feature.height
        return ScreenArea(feature.position, AbsoluteScreenPosition(ex, ey)).containsPos(AbsoluteScreenPosition(x, y))
    }

    private fun isFeatureInArea(feature: DraggableFeature, area: ScreenArea): Boolean {
        val ex = feature.position.x + feature.scale * feature.width
        val ey = feature.position.y + feature.scale * feature.height
        return ScreenArea(feature.position, AbsoluteScreenPosition(ex, ey)).overlapsArea(area)
    }

    private fun moveFeature(feature: DraggableFeature, offsetX: Float, offsetY: Float) {

        val movedX = feature.position.x + offsetX
        val movedY = feature.position.y + offsetY

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