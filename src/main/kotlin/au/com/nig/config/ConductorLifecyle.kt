package au.com.nig.config

import com.netflix.conductor.client.task.WorkflowTaskCoordinator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.SmartLifecycle
import org.springframework.context.annotation.Configuration
import java.util.concurrent.atomic.AtomicBoolean

@Configuration
class ConductorLifecyle(var taskCoordinator: WorkflowTaskCoordinator): SmartLifecycle {
    private val logger: Logger = LoggerFactory.getLogger(ConductorLifecyle::class.java)

    var isRunning: AtomicBoolean = AtomicBoolean(false)
    override fun start() {
        logger.info("Starting taskCoord")
        taskCoordinator.init()
    }

    override fun stop() {
        taskCoordinator.shutdown()
    }

    override fun isRunning(): Boolean {
        return isRunning.get()
    }
}
