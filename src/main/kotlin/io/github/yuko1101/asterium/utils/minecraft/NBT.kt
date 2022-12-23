package io.github.yuko1101.asterium.utils.minecraft

import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.JsonToNBT
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraftforge.common.util.Constants

object NBT {
    fun ItemStack.getLore(): ArrayList<String> {
        if (!this.hasTagCompound()) return arrayListOf()
        val nbt = this.tagCompound
        if (!nbt.hasKey("display") || !nbt.getCompoundTag("display").hasKey("Lore")) return arrayListOf()
        val display = nbt.getCompoundTag("display")
        val lore: ArrayList<String> = arrayListOf()
        for (n in 0 until display.getTagList("Lore", Constants.NBT.TAG_STRING).tagCount()) {
            lore.add(display.getTagList("Lore", Constants.NBT.TAG_STRING)[n].toString().replace("^\"(.*)\"$".toRegex(), "$1").replace("\\\"", "\"").replace("""\\""", """\"""))
        }
        return lore
    }

    fun createItem(itemStack: ItemStack, name: String, lore: List<String>?, shiny: Boolean = false): ItemStack {
        if (!itemStack.hasTagCompound()) itemStack.tagCompound = NBTTagCompound()
        val nbt = itemStack.tagCompound
        nbt.setTag("display", NBTTagCompound())
        nbt.getCompoundTag("display").setString("Name", name)
        if (lore != null) {
            nbt.getCompoundTag("display").setTag("Lore", NBTTagList())
            for (line in lore.indices) {
                nbt.getCompoundTag("display").getTagList("Lore", Constants.NBT.TAG_STRING).appendTag(NBTTagString(lore[line]))
            }
        }
        //itemStack.tagCompound = nbt
        return itemStack
    }
    fun createItem(nbt: String): ItemStack {
        val itemStack = ItemStack(Blocks.air)
        itemStack.readFromNBT(JsonToNBT.getTagFromJson(nbt))
        //itemStack.tagCompound = nbt
        return itemStack
    }

    fun NBTTagCompound.hasPath(path: List<String>): Boolean {
        var nbtTag = this
        for (key in 0..(path.size - 2)) {
            if (!nbtTag.hasKey(path[key])) return false
            nbtTag = nbtTag.getCompoundTag(path[key])
        }
        return nbtTag.hasKey(path[path.size - 1])
    }
    fun NBTTagCompound.getPath(path: List<String>, default: String): String {
        if (!hasPath(path)) return default
        var nbtTag = this
        for (key in 0..(path.size - 2)) {
            nbtTag = nbtTag.getCompoundTag(path[key])
        }
        return nbtTag.getString(path[path.size - 1])
    }
    fun NBTTagCompound.getPath(path: List<String>, default: Int): Int {
        if (!hasPath(path)) return default
        var nbtTag = this
        for (key in 0..(path.size - 2)) {
            nbtTag = nbtTag.getCompoundTag(path[key])
        }
        return nbtTag.getInteger(path[path.size - 1])
    }
    fun NBTTagCompound.setPath(path: List<String>, string: String) {

        if (!hasPath(path)) return
        var nbtTag = this
        for (key in 0..(path.size - 2)) {
            nbtTag = nbtTag.getCompoundTag(path[key])
        }
        nbtTag.setString(path[path.size - 1], string)
    }

    fun ItemStack.createNBTPath(path: List<String>): ItemStack {
        val item = this
        if (!item.hasTagCompound()) item.tagCompound = NBTTagCompound()
        var nbt = item.tagCompound
        for (key in path) {
            nbt.setTag(key, NBTTagCompound())
            nbt = nbt.getCompoundTag(key)
        }
        return item
    }

}