package controllers.security

import org.scalatest.{BeforeAndAfter, FunSuite}
import reactivemongo.bson.BSONObjectID

/**
 * Created by Szymek.
 */
class TokenImplTest extends FunSuite with BeforeAndAfter {

  var tokenX:TokenImpl = _
  var tokenY:TokenImpl = _
  var tokenZ:TokenImpl = _
  var tokenNotX:TokenImpl = _


  before {
    tokenX = TokenImpl(BSONObjectID.generate)
    tokenY = TokenImpl(tokenX.token)
    tokenZ = TokenImpl(tokenX.token)
    tokenNotX = TokenImpl(BSONObjectID.generate)
  }

  test("Simple test"){

    //given
    val id = BSONObjectID.generate
    val token = TokenImpl(id)

    //when
    val returnedID = token.getUserID

    //then
    assert(returnedID===id, "Simple test 1")
  }

  test("Equals: to self"){

    //given

    //when
    val result = tokenX.equals(tokenX)

    //then
    assert(result, "Equals to self")
  }

  test("Equals: wrong type must return false"){

    //given
    val wrongType = new Integer(4)

    //when
    val result = tokenX.equals(wrongType)

    //then
    assert(!result, "Passing incompatible object type to equals must return false")

  }

  test("Equals: equals of null should return false") {

    //given

    //when
    val result = tokenX.equals(null)

    //then
    assert(!result, "Passing null to equals must return false")
  }

  test("Equals: Reflexive and Symmetric tests"){

    //given

    //when
    val reflexiveResult = tokenX.equals(tokenY)
    val symmetricResult = tokenY.equals(tokenX)

    //then
    assert(reflexiveResult, "Reflexive test should return true")
    assert(symmetricResult, "Symmetric test should return true")
  }
  test("Equals: Transitive test"){

    //given

    //when
    val XYResult = tokenX.equals(tokenY)
    val YZResult = tokenY.equals(tokenZ)
    val XZResult = tokenX.equals(tokenZ)

    //then
    assert(XYResult, "Transitive test x,y should return true")
    assert(YZResult, "Transitive test y,z should return true")
    assert(XZResult, "Transitive test x,z should return true")
  }

  test("Equals: Repeated calls to equals consistently return the same result"){

    //given

    //when
    val resultTrue1 = tokenX.equals(tokenY)
    val resultTrue2 = tokenX.equals(tokenY)
    val resultFalse1 = tokenX.equals(tokenNotX)
    val resultFalse2 = tokenX.equals(tokenNotX)

    //then
    assert(resultTrue1, "Consistent test x,y should return true")
    assert(resultTrue2, "Consistent test x,y should return true")
    assert(!resultFalse1, "Consistent test x,not x should return false")
    assert(!resultFalse2, "Consistent test x,not x should return false")
  }

  test("Hashcode: Repeated calls to hashcode should return the same result") {

    //given

    //when
    val hashcode1 = tokenX.hashCode()
    val hashcode2 = tokenX.hashCode()

    //then
    assert(hashcode1 === hashcode2, "Hashcode should return the same result")

  }

  test("Hashcode: Equal objects should return the same hashes") {

    //given

    //when
    val hashX = tokenX.hashCode()
    val hashY = tokenY.hashCode()

    //then
    assert(hashX === hashY, "Hashes should be the same")
  }

  test("Hashcode: Not equal objects should return different hashes") {

    //given

    //when
    val hash1 = tokenX.hashCode()
    val hash2 = tokenNotX.hashCode()

    //then
    assert(!(hash1 == hash2), "Hashes should be different")
  }
}