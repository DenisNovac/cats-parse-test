package ru.naumen.dyablochkin.learncatsparse

import cats.parse.{Numbers, Parser => P, Parser0}
import org.typelevel.jawn.ast._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import io.circe

object JsonExample {
  private[this] val whitespace: P[Unit]   = P.charIn(" \t\r\n").void
  private[this] val whitespaces0: P[Unit] = whitespace.rep.void

  private val parser: P[JValue] = P.recursive[JValue] { recurse =>
    val pnull   = P.string("null").as(JNull)
    val bool    = P.string("true").as(JBool.True).orElse(P.string("false").as(JBool.False))
    val justStr = JsonStringUtil.escapedString('"')
    val str     = justStr.map(JString(_))
    val num     = Numbers.jsonNumber.map(JNum(_))

    val listSep: P[Unit] =
      P.char(',').surroundedBy(whitespaces0).void

    def rep[A](pa: P[A]): Parser0[List[A]] =
      pa.repSep0(listSep).surroundedBy(whitespaces0)

    val list = rep(recurse).with1
      .between(P.char('['), P.char(']'))
      .map(vs => JArray.fromSeq(vs))

    val kv: P[(String, JValue)] =
      justStr ~ (P.char(':').surroundedBy(whitespaces0) *> recurse)

    val obj = rep(kv).with1
      .between(P.char('{'), P.char('}'))
      .map(vs => JObject.fromSeq(vs))

    P.oneOf(str :: num :: list :: obj :: bool :: pnull :: Nil)
  }

  // any whitespace followed by json followed by whitespace followed by end
  def parserFile: P[JValue] = whitespaces0.with1 *> parser <* (whitespaces0 ~ P.end)

}

class JsonExample extends AnyFunSuite with Matchers {
  import JsonExample._

  test("Парсер должен парсить json") {

    val json      = """
    {
      "some_flag": true,
      "some_number": 1,
      "some_thing": null
    }
    """
    val circeJson = circe.parser.parse(json).toOption.get
    println(circeJson)

    val r = parserFile.parse(circeJson.spaces2)
    println(r)

  }
}
