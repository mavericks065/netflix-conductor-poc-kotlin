package au.com.nig.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.conductor.client.exceptions.ConductorClientException
import com.netflix.conductor.client.http.MetadataClient
import com.netflix.conductor.common.metadata.tasks.TaskDef
import com.netflix.conductor.common.metadata.workflow.WorkflowDef
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

@Component
class ConductorMetadataRunner(
    private val metadataClient: MetadataClient,
    private val objectMapper: ObjectMapper,
    @Value(value = "classpath:defs/tasks/*.json") private val taskResources: Array<Resource>,
    @Value(value = "classpath:defs/workflows/*.json") private val workflowResources: Array<Resource>
): ApplicationRunner {

    private val logger: Logger = LoggerFactory.getLogger(ConductorMetadataRunner::class.java)

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments?) {
        for (taskResource in taskResources) {
            logger.info("#### Loading the tasks ####")
            logger.info(taskResource.filename)

            val rootNode = objectMapper.readTree(taskResource.inputStream)
            require(!(rootNode == null || !rootNode.isContainerNode)) { "Expected JSON array or object" }
            if (rootNode.isArray) {
                val iterator = rootNode.elements()
                while (iterator.hasNext()) {
                    val elementNode = iterator.next()
                    val taskDef = objectMapper.convertValue(elementNode, TaskDef::class.java)
                    upsertTaskDef(taskDef)
                }
            } else if (rootNode.isObject) {
                val taskDef = objectMapper.readValue(taskResource.inputStream, TaskDef::class.java)
                upsertTaskDef(taskDef)
            }
        }
        for (workflowResource in workflowResources) {
            logger.info("#### Loading the workflows ####")
            logger.info(workflowResource.filename)
            val workflowDef = objectMapper.readValue(
                workflowResource.inputStream,
                WorkflowDef::class.java
            )
            upsertWorkflowDef(workflowDef)
        }
    }

    private fun upsertTaskDef(taskDef: TaskDef) {
        try {
            metadataClient.updateTaskDef(taskDef)
        } catch (e: ConductorClientException) {
            metadataClient.registerTaskDefs(listOf(taskDef))
        }
    }

    private fun upsertWorkflowDef(workflowDef: WorkflowDef) {
        try {
            metadataClient.updateWorkflowDefs(listOf(workflowDef))
        } catch (e: ConductorClientException) {
            metadataClient.registerWorkflowDef(workflowDef)
        }
    }
}
