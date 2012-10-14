package org.elasticmq.rest.sqs

trait BatchRequestsModule {
  this: SQSLimitsModule =>

  def batchParametersMap(prefix: String, parameters: Map[String, String]): List[Map[String, String]] = {
    val subParameters = ParametersUtil.subParametersMaps(prefix, parameters)

    val uniqueIds = subParameters.map(_.get(Constants.IdSubParameter)).toSet
    if (uniqueIds.size != subParameters.size) {
      throw new SQSException("AWS.SimpleQueueService.BatchEntryIdsNotDistinct")
    }

    ifStrictLimits(uniqueIds.size > 10) {
      "AWS.SimpleQueueService.TooManyEntriesInBatchRequest"
    }

    subParameters
  }
}