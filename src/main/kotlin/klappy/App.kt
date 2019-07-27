/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package klappy

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
//import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.*

import org.lwjgl.system.MemoryUtil.NULL
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

interface Logging

inline fun <reified T: Logging> T.logger(): Logger = getLogger(T::class.java)

class App: Logging {
    private val width = 1280
    private val height = 720
    private var window = NULL

    private val keys = BooleanArray(65536)

    private var running = false

    fun init() {
        // Initialize GLFW
        assert(glfwInit()) { log("Error initializing GLFW window") }
        // Set window properties
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE)
        // Create window
        window = glfwCreateWindow(width, height, "Klappy", NULL, NULL)
        assert(window != NULL) {
            glfwTerminate()
            println("Error creating GLFW window")
        }
        // Get video mode of the main monitor
        val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())!! // Throw exception if null
        // Calculate center
        val centerX = (videoMode.width() - width) / 2
        val centerY = (videoMode.height() - height) / 2
        // Move GLFW window to center
        glfwSetWindowPos(window, centerX, centerY)
        // Catch key events and store them on keys
        glfwSetKeyCallback(window) { _: Long, key: Int, _: Int, action: Int, _: Int ->
            keys[key] = action != GLFW_RELEASE
        }

        // Bring the window context to this thread
        glfwMakeContextCurrent(window)
        GL.createCapabilities()

        glEnable(GL_DEPTH_TEST)
        glActiveTexture(GL_TEXTURE1)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        log("OpenGL: ${glGetString(GL_VERSION)}")
    }

    fun run() {
        init();
        // Game loop
        running = true
        var lastTime = System.nanoTime()
        var delta = 0.0
        val ns = 1_000_000_000.0 / 60.0
        var timer = System.currentTimeMillis()
        var updates = 0
        var frames = 0
        while (running) {
            val now = System.nanoTime()
            delta += (now - lastTime) / ns
            lastTime = now
            if (delta >= 1.0) {
                glfwPollEvents()
                updates++
                delta--
            }
            frames++
            if (System.currentTimeMillis() - timer > 1_000) {
                timer += 1_000
                log("$updates ups, $frames fps")
                updates = 0
                frames = 0
            }
            if (glfwWindowShouldClose(window)) running = false
        }
        glfwDestroyWindow(window)
        glfwTerminate()
    }

    fun log(s: String) {
        logger().info(s)
    }
}

fun main(args: Array<String>) = runBlocking {
    val game = GlobalScope.launch {
        App().run()
    }
    game.join()
}
