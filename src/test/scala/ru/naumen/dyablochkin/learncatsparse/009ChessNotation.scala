package ru.naumen.dyablochkin.learncatsparse

import cats.implicits._
import cats.parse.Parser
import cats.parse.Parser.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import cats.parse.Parser.{Error => ParserError}
import cats.parse.Rfc5234._
import cats.parse.Parser.{char => pchar} // потому что char есть ещё в Rfc5234 и там он про другое

// Пример записей ходов в шахматах:

/**   1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Ba4 Nf6 5. O-O Be7 6. Re1 b5 7. Bb3 d6 8. c3 O-O 9. h3 Nb8 10. d4 Nbd7 11. c4 c6
  *      12. cxb5 axb5 13. Nc3 Bb7 14. Bg5 b4 15. Nb1 h6 16. Bh4 c5 17. dxe5 Nxe4 18. Bxe7 Qxe7 19. exd6 Qf6 20. Nbd2
  *      Nxd6 21. Nc4 Nxc4 22. Bxc4 Nb6 23. Ne5 Rae8 24. Bxf7+ Rxf7 25. Nxf7 Rxe1+ 26. Qxe1 Kxf7 27. Qe3 Qg5 28. Qxg5
  *      hxg5 29. b3 Ke6 30. a3 Kd6 31. axb4 cxb4 32. Ra5 Nd5 33. f3 Bc8 34. Kf2 Bf5 35. Ra7 g6 36. Ra6+ Kc5 37. Ke1 Nf4
  *      38. g3 Nxh3 39. Kd2 Kb5 40. Rd6 Kc5 41. Ra6 Nf2 42. g4 Bd3 43. Re6 1/2-1/2
  */

class Ex_ChessNotation extends AnyFunSuite with Matchers {

  val parser: Parser[CarType] = {
    val make = alpha.rep.string

    // ? возвращает None вместо ошибки если такого инпута в строке нет
    val model = alpha.rep.string.?

    // пробел через ? потому что его может и не быть в случае, когда вписана только марка
    ((make <* sp.?) ~ model).map(x => CarType(x._1, x._2))

  }

  test("Парсер должен парсить шахматные ходы") {

    val res1 = CarType("ford", None)

    parser.parse("ford") should be(("", res1).asRight[ParserError])

    val res2 = CarType("ford", Some("focus"))

    parser.parse("ford focus") should be(("", res2).asRight[ParserError])

  }
}
