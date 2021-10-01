package ru.naumen.dyablochkin.learncatsparse

import cats.implicits._
import cats.parse.Parser
import cats.parse.Parser.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import cats.parse.Parser.{Error => ParserError}

sealed abstract class Binary extends Product with Serializable

object Binary {
  case object Zero extends Binary

  case object One extends Binary
}

class Ex_Binary extends AnyFunSuite with Matchers {

  // "1" -> Binary.One
  val parser: Parser[Binary] = 
    char('0').as(Binary.Zero).orElse(char('1').as(Binary.One))

  test("Парсер должен парсить 1 и 0") {

    println(parser.parse("2"))

    // val signature: Either[cats.parse.Parser.Error, (String, Binary)] = parser.parse("1")

    parser.parse("1") should be(("", Binary.One).asRight[ParserError])
    parser.parse("0") should be(("", Binary.Zero).asRight[ParserError])

  }
}
