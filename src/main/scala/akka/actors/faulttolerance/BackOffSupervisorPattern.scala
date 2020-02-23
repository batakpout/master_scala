package akka.actors.faulttolerance

/**
 * Pain: the repeated restarts of actors
 *    -> restarting immediately might be useless
 *    -> everyone attempting at the same time might kill the resources again
 * Create backoff supervision for exponential delays between attempts
 */
object BackoffSupervisorPattern extends App {


}
