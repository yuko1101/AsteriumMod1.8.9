package io.github.yuko1101.asterium.utils.minecraft

import io.github.yuko1101.asterium.utils.minecraft.WorldUtils.getDistance
import io.github.yuko1101.asterium.utils.minecraft.RenderUtils.draw3DLine
import net.minecraft.util.BlockPos
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.Vec3
import java.awt.Color
import java.lang.Exception
import java.util.*

class AStarPathFinder(var timeout: Long, var goThoughBlocks: Boolean) {
    // A* pathfinding basically works like this
    // Step 1: Get list of nodes with lowest f value
    // Step 2: if you have more than one node then select the one with the lowest distanceToEnd
    // Step 3: if you still have more pick one of the remaining ones
    // Step 4: check nodes around the selected node
    // Step 5: repeat until you reach the end
    // Node
    class Node(var pos: BlockPos, var distanceToStart: Double, var distanceToEnd: Double, var previousNode: Node?) {
        var hasChecked = false
        val fvalue: Double
            get() = distanceToStart + distanceToEnd
    }

    var path: ArrayList<BlockPos> = arrayListOf()
    fun createPath(start: BlockPos, end: BlockPos, distanceApart: Double): List<BlockPos> {
        var blockPos = start
        var endPos = end
        if (Minecraft.getMinecraft().theWorld.getBlockState(blockPos).block != Blocks.air && !goThoughBlocks) {
//			NotificationManager.getNotificationManager().createNotification("Pathfinder", "Start point is inside of block, selecting point next to it", true, 1000, Type.WARNING, Color.RED);
            for (x in -2..1) for (y in -2..1) for (z in -2..1) if (Minecraft.getMinecraft().theWorld.getBlockState(
                    blockPos.add(
                        x,
                        y,
                        z
                    )
                ).block == Blocks.air
            ) {
                blockPos = blockPos.add(x, y, z)
                break
            }
            if (Minecraft.getMinecraft().theWorld.getBlockState(blockPos).block != Blocks.air) {
//				NotificationManager.getNotificationManager().createNotification("Pathfinder", "Failed to find air point next to the original start point", true, 1000, Type.WARNING, Color.RED);
                return emptyList()
            }
        }
        if (Minecraft.getMinecraft().theWorld.getBlockState(endPos).block != Blocks.air && !goThoughBlocks) {
//			NotificationManager.getNotificationManager().createNotification("Pathfinder", "End point is inside of block, selecting point next to it", true, 1000, Type.WARNING, Color.RED);
            for (x in -2..1) for (y in -2..1) for (z in -2..1) if (Minecraft.getMinecraft().theWorld.getBlockState(
                    endPos.add(
                        x,
                        y,
                        z
                    )
                ).block == Blocks.air
            ) {
                endPos = endPos.add(x, y, z)
                break
            }
            if (Minecraft.getMinecraft().theWorld.getBlockState(endPos).block != Blocks.air) {
//				NotificationManager.getNotificationManager().createNotification("Pathfinder", "Failed to find air point next to the original end point", true, 1000, Type.WARNING, Color.RED);
                return emptyList()
            }
        }
        var flipAfter = false
        if (getBlockCountAroundPos(blockPos) < getBlockCountAroundPos(endPos)) {
            val temp = blockPos
            blockPos = endPos
            endPos = temp
            flipAfter = true
        }
        path.clear()
        var nodes = arrayListOf<Node>()
        nodes.add(Node(blockPos, 0.0, getDistance(blockPos, endPos), null))

        // Prevents the program from freezing
        val antiFreeze = System.currentTimeMillis() + timeout
        while (System.currentTimeMillis() < antiFreeze) {

            // Prevents it from getting stuck if there is no path the to end
            var breakHere = true
            for (n in nodes) {
                if (!n.hasChecked) {
                    breakHere = false
                }
            }
            if (breakHere) break

            // Finds nodes that are best to check
            var nodeToCheck: Node? = null
            val temp = arrayListOf<Node>()
            for (n in nodes) {
                if (temp.isEmpty() || n.fvalue < temp[0].fvalue && !n.hasChecked) {
                    if (!n.hasChecked) {
                        temp.clear()
                        temp.add(n)
                    }
                } else if (temp.isNotEmpty() && temp[0].fvalue == n.fvalue && !n.hasChecked) {
                    if (!n.hasChecked) {
                        temp.add(n)
                    }
                }
            }
            for (n in temp) {
                if (nodeToCheck == null || n.distanceToEnd < nodeToCheck.distanceToEnd) {
                    nodeToCheck = n
                }
            }

            // If it reached the end then return
            if (nodeToCheck!!.pos == endPos) {
                path.clear()
                var backtrack = nodeToCheck
                path.add(backtrack.pos)
                try {
                    while (backtrack!!.previousNode.also { backtrack = it } != null) {
                        path.add(backtrack!!.pos)
                        //						backtrack = backtrack.previousNode;
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (flipAfter) path.reverse()
                spacePath(distanceApart)
                return path
            }

            // Debug lines
//			path.add(nodeToCheck.pos);

            // Recreates arraylist with added values
            nodeToCheck.hasChecked = true
            nodes = reCreateNodeArrayList(nodeToCheck, endPos, nodes)
        }
        return emptyList()
    }

    fun createPath(start: BlockPos, end: BlockPos) {
        createPath(start, end, 1.0)
    }

    private fun spacePath(spacing: Double) {
        var spacing = spacing
        if (path.isEmpty()) return
        var newPath = arrayListOf<BlockPos>()
        val lastPos = path[0]
        newPath.add(lastPos)
        if (spacing > 1) {
            for (pos in path) {
                if (pos != lastPos && getDistance(pos, lastPos) >= spacing || path.indexOf(pos) == path.size) {
                    newPath.add(pos)
                }
            }
        } else if (spacing < 1) {
            if (spacing == 0.0) {
                spacing = 0.05
                //				spacing = 9.0E-4D;
            }
            for (pos in path) {
                if (pos != lastPos && getDistance(pos, lastPos) >= spacing || path.indexOf(pos) == path.size) {
                    var x = 0.0
                    while (x < (lastPos.x - pos.x) / spacing) {
                        var y = 0.0
                        while (y < (lastPos.y - pos.y) / spacing) {
                            var z = 0.0
                            while (z < (lastPos.z - pos.z) / spacing) {
                                newPath.add(BlockPos(pos.z + x, pos.y + y, pos.z + z))
                                z += spacing
                            }
                            y += spacing
                        }
                        x += spacing
                    }
                    newPath.add(pos)
                }
            }
        } else {
            newPath = path
        }
        path = newPath
    }

    private fun getBlockCountAroundPos(pos: BlockPos): Int {
        var blockCount = 0
        for (x in -10..9) for (y in -10..9) for (z in -10..9) if (Minecraft.getMinecraft().theWorld.getBlockState(
                pos.add(
                    x,
                    y,
                    z
                )
            ).block != Blocks.air
        ) blockCount++
        return blockCount
    }

    private fun reCreateNodeArrayList(
        nodeToCheck: Node?,
        end: BlockPos,
        existingNodes: ArrayList<Node>
    ): ArrayList<Node> {
        nodeToCheck!!.hasChecked = true

        // Creates new arraylist
        val nodes = ArrayList<Node>()
        // Adds existing nodes
        nodes.addAll(existingNodes)

        // Checks area around the node
        for (face in EnumFacing.VALUES) {
            // Creates new node
            val newNode = Node(
                nodeToCheck.pos.offset(face),
                nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.offset(face), end),
                nodeToCheck
            )

            // Checks to see if it should actually add the node
            var add = true
            for (n in nodes) {
                if (n.pos == newNode.pos) {
//					n.previousNode = nodeToCheck;
                    add = false
                }
            }
            if (add && !goThoughBlocks) {
                try {
                    if (Minecraft.getMinecraft().theWorld.getBlockState(newNode.pos).block != Blocks.air) add = false
                } catch (e: Exception) {
                    // TODO: handle exception
                }
            }

            // Adds node if it fits the criteria
            if (add) {
                nodes.add(newNode)
            }
        }
        val cornersToCheck = ArrayList<Node>()
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(1, 0, 1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(1, 0, 1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(1, 0, -1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(1, 0, -1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(-1, 0, -1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(-1, 0, -1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(-1, 0, 1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(-1, 0, 1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(1, 1, 1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(1, 1, 1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(1, 1, -1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(1, 1, -1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(-1, 1, -1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(-1, 1, -1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(-1, 1, 1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(-1, 1, 1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(1, -1, 1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(1, -1, 1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(1, -1, -1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(1, -1, -1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(-1, -1, -1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(-1, -1, -1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(-1, -1, 1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(-1, -1, 1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(1, -1, 0), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(1, -1, 0), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(-1, -1, 0), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(-1, -1, 0), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(0, -1, -1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(0, -1, -1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(0, -1, 1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(0, -1, 1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(1, 1, 0), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(1, 1, 0), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(-1, 1, 0), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(-1, 1, 0), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(0, 1, -1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(0, 1, -1), end), nodeToCheck
            )
        )
        cornersToCheck.add(
            Node(
                nodeToCheck.pos.add(0, 1, 1), nodeToCheck.distanceToStart + 1,
                getDistance(nodeToCheck.pos.add(0, 1, 1), end), nodeToCheck
            )
        )
        for (newNode in cornersToCheck) {
            // Checks to see if it should actually add the node
            var add = true
            for (n in nodes) {
                if (n.pos == newNode.pos) {
//								n.previousNode = nodeToCheck;
                    add = false
                }
            }
            if (add && !goThoughBlocks) {
                try {
                    if (Minecraft.getMinecraft().theWorld.getBlockState(newNode.pos).block != Blocks.air) add = false
                } catch (e: Exception) {
                    // TODO: handle exception
                }
            }

            // Adds node if it fits the criteria
            if (add) {
                nodes.add(newNode)
            }
        }

        // Return the new arraylist
        return nodes
    }

    fun renderPath(partialTicks: Float, width: Int, color: Color) {
        try {
            val trailList = arrayListOf<Vec3>()
            for (pos in path) {
                trailList.add(Vec3(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5))
            }
            var lastLoc: Vec3? = null
            for (loc in trailList) {
                lastLoc = if (lastLoc == null) {
                    loc
                } else {
                    if (Minecraft.getMinecraft().thePlayer.getDistance(loc.xCoord, loc.yCoord, loc.zCoord) > 100) {
                    } else {
                        draw3DLine(
                            Vec3(lastLoc.xCoord, lastLoc.yCoord, lastLoc.zCoord),
                            Vec3(loc.xCoord, loc.yCoord, loc.zCoord),
                            width,
                            color,
                            partialTicks
                        )
                    }
                    loc
                }
            }
        } catch (e: Exception) {
            // TODO: handle exception
        }
    }
}