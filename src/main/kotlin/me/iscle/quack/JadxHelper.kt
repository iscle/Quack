package me.iscle.quack

import jadx.api.JadxArgs
import jadx.api.JadxDecompiler
import jadx.core.dex.info.MethodInfo
import jadx.core.dex.instructions.InvokeNode
import jadx.core.dex.nodes.MethodNode
import jadx.core.dex.visitors.AbstractVisitor
import jadx.core.dex.visitors.IDexTreeVisitor
import jadx.core.dex.visitors.ProcessInstructionsVisitor
import jadx.plugins.tools.JadxExternalPluginsLoader
import java.io.File

class JadxHelper(
    file: File,
) {
    val jadx = JadxDecompiler(JadxArgs().apply {
        setInputFile(file)
        outDir = File("jadx-output")
        pluginLoader = JadxExternalPluginsLoader()
    })

    val methodCalls = mutableMapOf<MethodNode, MutableList<MethodInfo>>()

    fun load() {
        jadx.load()

        addCustomPassAfter(ProcessInstructionsVisitor::class.java, object : AbstractVisitor() {
            override fun visit(mth: MethodNode) {
                val calls = mutableListOf<MethodInfo>()
                mth.instructions?.forEach {
                    if (it is InvokeNode) {
                        calls.add(it.callMth)
                    }
                }
                methodCalls.put(mth, calls)
            }
        })
    }

    fun decompile() {
        jadx.save()

        methodCalls.forEach { (mth, calls) ->
            println("${mth.methodInfo.fullName} calls:")
            calls.forEach {
                println("  ${it.declClass.fullName}.${it.name}")
            }
        }
    }

    fun findMethodCalls(fullMethodName: String): List<MethodNode> {
        return methodCalls.filter { (_, calls) ->
            calls.any { it.fullName == fullMethodName }
        }.keys.toList()
    }

    private fun addCustomPassAfter(clazz: Class<*>, visitor: IDexTreeVisitor) {
        val passes = jadx.root.passes
        for (i in 0 until passes.size) {
            if (passes[i].javaClass == clazz) {
                passes.add(i + 1, visitor)
                return
            }
        }
    }
}