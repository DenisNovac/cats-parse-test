package ru.naumen.dyablochkin.learncatsparse

import cats.implicits._
import cats.parse.Parser
import cats.parse.Parser.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import cats.parse.Parser.{Error => ParserError}
import cats.parse.Rfc5234._
import cats.parse.Parser.{char => pchar} // потому что char есть ещё в Rfc5234 и там он про другое

sealed abstract class MyTuple extends Product with Serializable

object MyTuple {
  final case class Two(one: String, two: String)                  extends MyTuple
  final case class Three(one: String, two: String, three: String) extends MyTuple
}

class Ex_OneTwoThree extends AnyFunSuite with Matchers {

  val parser: Parser[MyTuple] = {
    val comma = pchar(',')
    val item  = Parser.oneOf(List(alpha, digit)).rep.string
    val two   = ((item <* comma) ~ item).map((x: (String, String)) => MyTuple.Two(x._1, x._2))
    val three = (item ~ item.surroundedBy(comma) ~ item).map { case ((a, b), c) => MyTuple.Three(a, b, c) }

    /* многие библиотеки используют backtrack по дефолту, а cats-parse требует его ручного указания, так как так больше
     * перфоманс */
    // он отматывает полученные текущим парсером символы обратно и позволяет направить их в другой парсер
    // нужно сначала пробовать three а потом two, иначе всё будет подходить в two
    three.backtrack.orElse(two)
  }

  test("Парсер должен парсить строку с переменным количеством строк") {

    val res1 = MyTuple.Three("one", "2", "three")

    parser.parse("one,2,three") should be(("", res1).asRight[ParserError])

    val res2 = MyTuple.Two("one", "2")

    parser.parse("one,2") should be(("", res2).asRight[ParserError])

  }
}
