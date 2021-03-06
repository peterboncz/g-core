/*
 * gcore-spark is the reference implementation of the G-CORE graph query
 * language by the Linked Data Benchmark Council (LDBC) - ldbcouncil.org
 *
 * The copyrights of the source code in this file belong to:
 * - CWI (www.cwi.nl), 2017-2018
 *
 * This software is released in open source under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package compiler

import algebra.AlgebraRewriter
import algebra.operators.{Create, Drop, Query, View}
import algebra.trees.{AlgebraContext, AlgebraTreeNode}
import parser.SpoofaxParser
import parser.trees.ParseContext
import spark.sql.SqlRunner

/** Defines the compilation pipeline of a G-CORE query. */
case class GcoreCompiler(context: CompileContext) extends Compiler {

  val parser: ParseStage = SpoofaxParser(ParseContext(context.catalog))
  val rewriter: RewriteStage = AlgebraRewriter(AlgebraContext(context.catalog))
  val target: RunTargetCodeStage = SqlRunner(context)

  override def compile(query: String): Unit =
  {
    var parsed: AlgebraTreeNode  = parser(query)

    parsed match {
    case create : Create =>
      var qCreate = parsed.asInstanceOf[Create]
      if (qCreate.exist)
        println("The graph "+ qCreate.getGraphName+ " already exists")
      else
      {
        var rewrited: AlgebraTreeNode  = rewriter(parsed)
        target(rewrited)
      }
    case view : View =>
      var qCreate = parsed.asInstanceOf[View]
      if (qCreate.exist)
        println("The graph "+ qCreate.getGraphName+ " already exists")
      else
      {
        var rewrited: AlgebraTreeNode  = rewriter(parsed)
        target(rewrited)
      }
    case query: Query =>
      var rewrited: AlgebraTreeNode  = rewriter(parsed)
      target(rewrited)
    case drop: Drop =>
      if(!drop.exist)
        println("The graph "+ drop.getGraphName+ " not exists")
      else
      {
        var rewrited: AlgebraTreeNode  = rewriter(parsed)
        target(rewrited)
      }

  }


  }




}
