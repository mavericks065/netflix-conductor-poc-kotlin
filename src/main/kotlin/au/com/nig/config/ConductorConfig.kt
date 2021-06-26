package au.com.nig.config

import com.netflix.conductor.client.http.MetadataClient
import com.netflix.conductor.client.http.TaskClient
import com.netflix.conductor.client.task.WorkflowTaskCoordinator
import com.netflix.conductor.client.worker.Worker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConductorConfig(@Value("\${conductor.server.url}") private val conductorUrl: String) {

    private val logger: Logger = LoggerFactory.getLogger(ConductorConfig::class.java)

    @Bean
    fun taskClient(): TaskClient {
        val taskClient = TaskClient()
        logger.info("Conductor server url $conductorUrl")
        taskClient.setRootURI(conductorUrl)
        return taskClient
    }

    @Bean
    fun metadataClient(): MetadataClient {
        val metadataClient = MetadataClient()
        metadataClient.setRootURI(conductorUrl)
        return metadataClient
    }

    @Bean
    fun workflowTaskCoordinator(workers: Collection<Worker>): WorkflowTaskCoordinator {
        return WorkflowTaskCoordinator.Builder()
            .withWorkers(workers).withThreadCount(3).withTaskClient(taskClient()).build()
    }
}
