package me.iscle.quack.manifest

import me.iscle.quack.resources.XmlResourceParser
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.InputStream

// https://developer.android.com/guide/topics/manifest/manifest-intro
// https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/tools/aapt2/

class AndroidManifestParser(
    inputStream: InputStream,
) : XmlResourceParser {
    private val parser = XmlResourceParser.fromInputStream(inputStream)

    fun getAsAndroidManifest(): AndroidManifest? {
        val document = getAsDocument()

        val manifestElement = document.getElementsByTagName("manifest").item(0)
        if (manifestElement == null) {
            println("Manifest element not found")
            return null
        }

        val packageName = manifestElement.attributes.getNamedItem("package")?.nodeValue
        if (packageName == null) {
            println("Package name not found")
            return null
        }

        val versionCode = manifestElement.attributes.getNamedItem("android:versionCode")?.nodeValue
        if (versionCode == null) {
            println("Version code not found")
            return null
        }

        val versionName = manifestElement.attributes.getNamedItem("android:versionName")?.nodeValue
        if (versionName == null) {
            println("Version name not found")
            return null
        }

        val permissions = mutableListOf<String>()
        val permissionElements = document.getElementsByTagName("uses-permission")
        for (i in 0 until permissionElements.length) {
            val permissionElement = permissionElements.item(i)
            val permission = permissionElement.attributes.getNamedItem("android:name")?.nodeValue
            if (permission != null) {
                permissions.add(permission)
            } else {
                println("Invalid permission element at index $i")
            }
        }

        val applicationElement = document.getElementsByTagName("application").item(0) as Element?
        if (applicationElement == null) {
            println("Application element not found")
            return null
        }

        val applicationSubclass = getApplicationSubclass(packageName, applicationElement)

        val activities = mutableListOf<ManifestActivity>()
        val activityElements = applicationElement.getElementsByTagName("activity")
        for (i in 0 until activityElements.length) {
            val activityElement = activityElements.item(i) as Element
            val activityName = activityElement.attributes.getNamedItem("android:name")?.nodeValue
            if (activityName != null) {
                val permission = activityElement.attributes.getNamedItem("android:permission")?.nodeValue
                val intentFilters = getElementIntentFilters(activityElement)

                activities.add(
                    ManifestActivity(
                        name = activityName,
                        permission = permission,
                        intentFilters = intentFilters,
                    )
                )
            } else {
                println("Invalid activity element at index $i")
            }
        }

        val services = mutableListOf<ManifestService>()
        val serviceElements = applicationElement.getElementsByTagName("service")
        for (i in 0 until serviceElements.length) {
            val serviceElement = serviceElements.item(i) as Element
            val serviceName = serviceElement.attributes.getNamedItem("android:name")?.nodeValue
            if (serviceName != null) {
                val permission = serviceElement.attributes.getNamedItem("android:permission")?.nodeValue
                val intentFilters = getElementIntentFilters(serviceElement)

                services.add(
                    ManifestService(
                        name = serviceName,
                        permission = permission,
                        intentFilters = intentFilters,
                    )
                )
            } else {
                println("Invalid service element at index $i")
            }
        }

        val receivers = mutableListOf<ManifestReceiver>()
        val receiverElements = applicationElement.getElementsByTagName("receiver")
        for (i in 0 until receiverElements.length) {
            val receiverElement = receiverElements.item(i) as Element
            val receiverName = receiverElement.attributes.getNamedItem("android:name")?.nodeValue
            if (receiverName != null) {
                val permission = receiverElement.attributes.getNamedItem("android:permission")?.nodeValue
                val intentFilters = getElementIntentFilters(receiverElement)

                receivers.add(
                    ManifestReceiver(
                        name = receiverName,
                        permission = permission,
                        intentFilters = intentFilters,
                    )
                )
            } else {
                println("Invalid receiver element at index $i")
            }
        }

        return AndroidManifest(
            packageName = packageName,
            versionCode = versionCode,
            versionName = versionName,
            permissions = permissions,
            applicationSubclass = applicationSubclass,
            activities = activities,
            services = services,
            receivers = receivers,
        )
    }

    private fun getApplicationSubclass(packageName: String, application: Node): String? {
        val subclass = application.attributes.getNamedItem("android:name")?.nodeValue ?: return null
        return if (subclass.startsWith(".")) {
            packageName + subclass
        } else {
            subclass
        }
    }

    private fun getElementIntentFilters(element: Element): List<ManifestIntentFilter> {
        val intentFilters = mutableListOf<ManifestIntentFilter>()
        val intentFilterElements = element.getElementsByTagName("intent-filter")
        for (i in 0 until intentFilterElements.length) {
            val intentFilterElement = intentFilterElements.item(i) as Element
            val actions = mutableListOf<String>()
            val actionElements = intentFilterElement.getElementsByTagName("action")
            for (j in 0 until actionElements.length) {
                val actionElement = actionElements.item(j) as Element
                val actionName = actionElement.attributes.getNamedItem("android:name")?.nodeValue
                if (actionName != null) {
                    actions.add(actionName)
                } else {
                    println("Invalid action element at index $j")
                }
            }

            val categories = mutableListOf<String>()
            val categoryElements = intentFilterElement.getElementsByTagName("category")
            for (j in 0 until categoryElements.length) {
                val categoryElement = categoryElements.item(j) as Element
                val categoryName = categoryElement.attributes.getNamedItem("android:name")?.nodeValue
                if (categoryName != null) {
                    categories.add(categoryName)
                } else {
                    println("Invalid category element at index $j")
                }
            }

            intentFilters.add(
                ManifestIntentFilter(
                actions = actions,
                categories = categories,
            )
            )
        }

        return intentFilters
    }

    override fun parse() {
        parser.parse()
    }

    override fun getAsString(prettyPrint: Boolean): String {
        return parser.getAsString(prettyPrint)
    }

    override fun getAsDocument(): Document {
        return parser.getAsDocument()
    }
}