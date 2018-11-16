/*
 * gcore-spark is the reference implementation of the G-CORE graph query
 * language by the Linked Data Benchmark Council (LDBC) - ldbcouncil.org
 *
 * The copyrights of the source code in this file belong to:
 * - Universidad de Talca (2018)
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

package spark.examples

import algebra.expressions.Label
import org.apache.spark.sql.{DataFrame, SparkSession}
import schema.EntitySchema.LabelRestrictionMap
import schema.{SchemaMap, Table}
import spark.SparkGraph

case class CompanyGraph(spark: SparkSession) extends SparkGraph {



  val mit = Company2(id = 105, name = "MIT")
  val cwi = Company2(id = 106, name = "CWI")
  val acme = Company2(id = 107, name = "Acme")
  val hal = Company2(id = 108, name = "HAL")


  import spark.implicits._

  override var graphName: String = "company_graph"

  override def vertexData: Seq[Table[DataFrame]] =
    Seq(
      Table(Label("Company"), Seq(mit, cwi, acme, hal).toDF())
    )

  override def edgeData: Seq[Table[DataFrame]] = Seq.empty

  override def pathData: Seq[Table[DataFrame]] = Seq.empty

  override def edgeRestrictions: LabelRestrictionMap =
    SchemaMap(Map(
    ))

  override def storedPathRestrictions: LabelRestrictionMap = SchemaMap.empty
}

sealed case class Company2(id: Int, name: String)
