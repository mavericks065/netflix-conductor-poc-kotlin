package au.com.nig.workers

import com.netflix.conductor.client.worker.Worker
import com.netflix.conductor.common.metadata.tasks.Task
import com.netflix.conductor.common.metadata.tasks.TaskResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class Process1: Worker {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(FailingProcess1::class.java)
    }
    override fun getTaskDefName(): String {
        return "Process1"
    }

    override fun execute(task: Task): TaskResult {
        val taskResult = TaskResult(task)
        logger.info("##### RUNNING Process1 #####")
        logger.info(task.inputData.toString())
        taskResult.addOutputData("stuff", "STUFF Process1")

        taskResult.status = TaskResult.Status.COMPLETED
        return taskResult
    }
}
