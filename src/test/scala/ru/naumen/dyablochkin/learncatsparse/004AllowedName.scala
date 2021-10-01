package ru.naumen.dyablochkin.learncatsparse

import cats.implicits._
import cats.parse.Parser
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import cats.parse.Parser.{Error => ParserError}

// тут содержатся всякие штуки типа "пробел", "алфавит" и подобные
import cats.parse.Rfc5234._
import cats.data.NonEmptyList

final case class Name(value: String) extends AnyVal

class Ex_AllowedName extends AnyFunSuite with Matchers {

  val allowedChars         = List(Parser.char('\''), alpha, sp)
  // имя обязательно начинается с буквы, а потом только пробелы и апострофы
  val parser: Parser[Name] = (alpha ~ Parser.oneOf(allowedChars).rep).string.map(Name.apply)

  test("Парсер должен парсить только разрешённые символы") {

    val res = Name("Sam O'Brien")

    parser.parse("Sam O'Brien") should be(("", res).asRight[ParserError])

    // остаток строки - первый элемент тапла
    parser.parse("Sam O'Brien123") should be(("123", res).asRight[ParserError])
  }
}
