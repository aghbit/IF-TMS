package models.team.teams.volleyball.volleyballs

import models.team.Team

/**
 * Created by Szymek Seget on 07.03.15.
 */
trait TeamObject {
  def apply(name:String):Team
}
