package repositories

import models.player.Player

/**
 * Created by Szymek Seget on 07.03.15.
 */
class PlayerRepository extends Repository[Player] {

  override val collectionName: String = "Players"
  override val clazz: Class[Player] = classOf[Player]

}
