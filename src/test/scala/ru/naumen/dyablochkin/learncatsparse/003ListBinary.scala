package ru.naumen.dyablochkin.learncatsparse

import cats.implicits._
import cats.parse.Parser
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import cats.parse.Parser.{Error => ParserError}

// тут содержатся всякие штуки типа "пробел", "алфавит" и подобные
import cats.parse.Rfc5234._
import cats.data.NonEmptyList

final case class BinaryList(value: NonEmptyList[Binary]) extends AnyVal

class Ex_ListBinary extends AnyFunSuite with Matchers {

  val parserSingle: Parser[Binary] = Parser.char('0').as(Binary.Zero).orElse(Parser.char('1').as(Binary.One))

  // rep даёт NonEmptyList на выходе, поэтому можно маппить напрямую
  // ещё есть rep0 для возможно пустого листа
  val parserRepl = parserSingle.rep.map(BinaryList.apply)

  test("Парсер должен парсить ряд бинарных символов") {

    val res = BinaryList(List(Binary.Zero, Binary.One, Binary.One, Binary.Zero).toNel.get)

    parserRepl.parse("0110") should be(("", res).asRight[ParserError])

  }
}
