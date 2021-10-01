package ru.naumen.dyablochkin.learncatsparse

import cats.implicits._
import cats.parse.Parser
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import cats.parse.Parser.{Error => ParserError}

// тут содержатся всякие штуки типа "пробел", "алфавит" и подобные
import cats.parse.Rfc5234._

final case class LetterAndNumber(value: String) extends AnyVal

class Ex_LetterAndNumber extends AnyFunSuite with Matchers {

  /** ~ это сложение (AND) string берёт результат и превращает в string, иначе это был бы tuple
    *
    * без String: (alpha ~ digit).map((t: (Char, Char)) => t)
    */
  val parser: Parser[LetterAndNumber] = (alpha ~ digit).string.map(r => LetterAndNumber(r))

  test("Парсер должен парсить буквы и цифры") {

    parser.parse("B2") should be(("", LetterAndNumber("B2")).asRight[ParserError])
    // порядок важен
    parser.parse("2B").isInstanceOf[Left[ParserError, LetterAndNumber]] should be(true)

  }
}
