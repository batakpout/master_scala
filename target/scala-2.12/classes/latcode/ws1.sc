
trait DomainError
case class ValidationError(actionCode: String, errorMessage: String) extends DomainError

sealed trait CardStatusDomain {
  val validationErrorCode: String

  def validationError(newStatus: CardStatusDomain): ValidationError =
    ValidationError(validationErrorCode, s"Can't change the card status from $this to $newStatus")
}
sealed trait OldTopicStatus extends CardStatusDomain
case object LOCKED extends OldTopicStatus {
  override val validationErrorCode: String = "STATUS_CHANGE_ON_LOCKED_CARD"
}

case object INACTIVE extends CardStatusDomain {
  override val validationErrorCode: String = "STATUS_CHANGE_ON_INACTIVE_CARD"
}
LOCKED.validationError(INACTIVE)

val r = new scala.util.Random()
r.nextInt(50)
