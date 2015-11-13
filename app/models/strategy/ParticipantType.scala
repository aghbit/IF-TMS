package models.strategy

/**
 * Created by Szymek Seget on 24.09.15.
 */
object ParticipantType extends Enumeration{
  type ParticipantType = Value
  val TEAM = Value("team")
  val PLAYER = Value("player")
}
