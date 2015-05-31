package controllers.security

import java.util

import org.mockito.Mockito
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.bson.types.ObjectId

/**
 * Created by Szymek.
 */
class TokensKeeper$Test extends FunSuite with MockitoSugar with BeforeAndAfter{

  val underTest = TokensKeeper
  var tokens:List[Token] = _

  before{
    tokens = List(
      TokenImpl(ObjectId.get()),
      TokenImpl(ObjectId.get()),
      TokenImpl(ObjectId.get())
    )

  }

  after {
    underTest.removeAllTokens()
  }

  test("ContainsToken: Empty TokenKeeper") {

    //given

    //when
    val containsResult = underTest.containsToken(tokens.head)

    //then
    assert(!containsResult, "ContainsToken: test 1")
  }

  test("ContainsToken: One token") {

    //given
    underTest.addToken(tokens.head)

    //when
    val containsResult = underTest.containsToken(tokens.head)

    //then
    assert(containsResult, "ContainsToken: test 1")
  }

  test("ContainsToken: More tokens") {

    //given
    tokens.foreach(token => underTest.addToken(token))

    //when
    val containsResults = tokens.map(token => underTest.containsToken(token))

    //then
    containsResults.foreach(result => assert(result, "ContainsToken: test 1"))
  }

  test("RemoveToken: One token") {

    //given
    underTest.addToken(tokens.head)

    //when
    val containsResultBeforeRemove = underTest.containsToken(tokens.head)
    underTest.removeTokenForUser(tokens.head.getUserID)
    val containsResultAfterRemove = underTest.containsToken(tokens.head)

    //then
    assert(containsResultBeforeRemove, "RemoveToken: test 1")
    assert(!containsResultAfterRemove, "RemoveToken: test 2")
  }

  test("AddToken: Remove duplicates token for single user"){

    //given
    underTest.addToken(tokens.head)
    underTest.addToken(tokens.head)

    //when
    val tokensNumber = underTest.getTokensNumber

    //then
    assert(tokensNumber == 1, "AddToken: test 1")
  }
}
