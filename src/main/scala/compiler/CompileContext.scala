package compiler

import common.compiler.Context
import schema.GraphDb

case class CompileContext(graphDb: GraphDb[_]) extends Context
