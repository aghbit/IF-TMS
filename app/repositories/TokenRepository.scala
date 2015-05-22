package repositories

import controllers.security.Token
/**
 * Created by Szymek Seget on 08.05.15.
 */
class TokenRepository extends Repository[Token]{

  override val collectionName: String = "Sessions"
  override val clazz: Class[Token] = classOf[Token]
}
