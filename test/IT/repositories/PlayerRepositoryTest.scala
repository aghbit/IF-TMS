package IT.repositories

import com.mongodb.BasicDBObject
import models.player.players.{DefaultPlayerImpl, Captain}
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.mock.MockitoSugar
import repositories.PlayerRepository

/**
 * Created by Szymek Seget on 16.06.15.
 */
class PlayerRepositoryTest extends FunSuite with MockitoSugar with BeforeAndAfter {

  var playerRepository: PlayerRepository = _

  before {
    playerRepository = new PlayerRepository
  }

  after {
    playerRepository.dropCollection()
  }
  test("Simple CRD with Captain") {

    //given
    val name = "Szymek"
    val surname = "Seget"
    val phone = "784588999"
    val mail = "test@test.com"
    val captain = Captain(name, surname, phone, mail)

    //when
    playerRepository.insert(captain)

    //then
    val playerFromRepo = playerRepository.findOne(new BasicDBObject("_id", captain._id))
    playerFromRepo match {
      case Some(cap:Captain) => assert(cap.name.equals("Szymek"), "Something went wrong!")
      case _ => throw new Exception("It's not captain or repo doesn't work.")
    }

    //when
    playerRepository.remove(captain)
    playerRepository.findOne(new BasicDBObject("_id", captain._id)) match {
      case Some(i) => throw new Exception("Remove doesn't work!")
      case None =>
    }
  }
  test("Simple CRD with DefaultPlayerImpl") {

    //given
    val name = "Szymek"
    val surname = "Seget"
    val player = DefaultPlayerImpl(name, surname)

    //when
    playerRepository.insert(player)

    //then
    val playerFromRepo = playerRepository.findOne(new BasicDBObject("_id", player._id))
    playerFromRepo match {
      case Some(pla:DefaultPlayerImpl) => assert(pla.name.equals("Szymek"), "Something went wrong!")
      case _ => throw new Exception("It's not captain or repo doesn't work.")
    }
  }
}
