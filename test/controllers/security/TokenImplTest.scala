package controllers.security

import org.scalatest.FunSuite
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
class TokenImplTest extends FunSuite {

  test("Simple test"){

    //given
    val id = BSONObjectID.generate
    val token = new TokenImpl(id)

    //when
    val returnedID = token.getUserID

    //then
    assert(returnedID===id, "Simple test 1")
  }

  test("equals test") {

    //given
    val id1 = BSONObjectID.generate
    val id2 = BSONObjectID.generate
    val token1 = new TokenImpl(id1)
    val token2 = new TokenImpl(id2)

    //when
    val equalsResult1 = token1.equals(token1)
    val equalsResult2 = token1.equals(token2)
    val equalsResult3 = token2.eq(token1)

    //then
    assert(equalsResult1, "equals test 1")
    assert(!equalsResult2, "equals test 2")
    assert(!equalsResult3, "equals test 3")

  }

}
