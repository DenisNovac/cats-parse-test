package ru.naumen.dyablochkin.learncatsparse

import cats.implicits._
import cats.parse.Parser
import cats.parse.Parser.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import cats.parse.Parser.{Error => ParserError}
import cats.parse.Rfc5234._
import cats.parse.Parser.{char => pchar} // потому что char есть ещё в Rfc5234 и там он про другое

final case class CarType(make: String, model: Option[String])

class Ex_CarType extends AnyFunSuite with Matchers {

  val parser: Parser[CarType] = {
    val make = alpha.rep.string

    // ? возвращает None вместо ошибки если такого инпута в строке нет
    val model = alpha.rep.string.?

    // пробел через ? потому что его может и не быть в случае, когда вписана только марка
    ((make <* sp.?) ~ model).map(x => CarType(x._1, x._2))

  }

  test("Парсер должен парсить марку авто") {

    val res1 = CarType("ford", None)

    parser.parse("ford") should be(("", res1).asRight[ParserError])

    val res2 = CarType("ford", Some("focus"))

    parser.parse("ford focus") should be(("", res2).asRight[ParserError])

  }
}
