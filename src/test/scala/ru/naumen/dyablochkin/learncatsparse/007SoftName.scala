package ru.naumen.dyablochkin.learncatsparse

import cats.implicits._
import cats.parse.Parser
import cats.parse.Parser.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import cats.parse.Parser.{Error => ParserError}
import cats.parse.Rfc5234._
import cats.parse.Parser.{char => pchar} // потому что char есть ещё в Rfc5234 и там он про другое

final case class UserName(value: String) extends AnyVal

class Ex_SoftName extends AnyFunSuite with Matchers {

  val parser: Parser[UserName] = {
    val name = alpha.rep.string

    // оператор soft похож на backtrack. Разница в том, что backtrack работает через orElse, а soft работает с product
    // но лучше не использовать backtract и soft, такие парсеры получаются проще

    /* soft говорит о том, что если после product произошла ошибка - нужно отматать символы и попробовать следующий
     * парсер через product */
    // rep0 говорит о том, что точка может и отсутствовать
    // получается, что вся левая часть парсера ничего не потребит в случае с примером "schmidth"
    {
      (name.soft ~ pchar('.')).rep0.with1 ~ name
    }.string.map(UserName.apply)

  }

  test("Парсер должен парсить имя пользователя с оператором soft") {

    val res1 = UserName("jess.day.one")

    parser.parse("jess.day.one") should be(("", res1).asRight[ParserError])

    val res2 = UserName("schmidt")

    parser.parse("schmidt") should be(("", res2).asRight[ParserError])

  }
}
