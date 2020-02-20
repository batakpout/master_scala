/**
 * Parent must decide upon their children's failure
 * When an actor fails,
 * it suspends its children
 * sends a special message to its parent
 * Parent decide to Restart, Resume, or Stop child or Escalate to its parent and fail itself.
 */