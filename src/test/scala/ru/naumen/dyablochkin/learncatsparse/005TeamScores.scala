package ru.naumen.dyablochkin.learncatsparse

import cats.implicits._
import cats.parse.Parser
import cats.parse.Parser.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import cats.parse.Parser.{Error => ParserError}
import cats.parse.Rfc5234._
import cats.parse.Parser.{char => pchar} // потому что char есть ещё в Rfc5234 и там он про другое

final case class Score(left: Int, right: Int)

class Ex_TeamScores extends AnyFunSuite with Matchers {

  val parser: Parser[Score] = {

    // маленький парсер для чтения только чисел в строке
    val multiDigit = digit.rep.string.map(_.toInt)

    // этот парсер парсит только дефис между счётом команд и пробелы вокруг (sp)
    val dash = pchar('-').surroundedBy(sp)

    // комбинируем парсеры в большой парсер
    /* <* product left используется для игнора правой стороны (дефис не нужен в Score, но прочитать его где-то нужно,так
     * как читаем строку слева-направо) */
    // Если бы не было скобок - оператор <* игнорил бы всю правую часть
    (
      (multiDigit <* dash) ~ multiDigit
    ).map(x => Score(x._1, x._2))
  }

  test("Парсер должен парсить счёт команд") {

    val res = Score(123, 456)
    parser.parse("123 - 456") should be(("", res).asRight[ParserError])

  }
}
