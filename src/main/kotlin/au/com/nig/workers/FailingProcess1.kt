package au.com.nig.workers

import com.netflix.conductor.client.worker.Worker
import com.netflix.conductor.common.metadata.tasks.Task
import com.netflix.conductor.common.metadata.tasks.TaskResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FailingProcess1 : Worker {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(FailingProcess1::class.java)
    }
    override fun getTaskDefName(): String {
        return "FailingProcess1"
    }

    override fun execute(task: Task): TaskResult {
        val taskResult = TaskResult(task)
        logger.info("##### RUNNING FailingProcess1 #####")
        logger.info(task.inputData.toString())
        if (task.retryCount >= 3) {
            taskResult.status = TaskResult.Status.COMPLETED
            taskResult.addOutputData("anotherKeyFailingProcess1", "outputdata FailingProcess1")
            return taskResult
        } else {
            taskResult.status = TaskResult.Status.FAILED
            val runtimeException = RuntimeException("FailingProcess1 is failing miserably")
            taskResult.addOutputData("error", runtimeException)
            return taskResult
        }
    }
}
