/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.calcite.postgres;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.ddl.SqlColumnDeclaration;
import org.apache.calcite.sql.ddl.SqlColumnDeclarationAutoincrement;
import org.apache.calcite.sql.ddl.SqlPrimaryKeyAutoincrement;
import org.apache.calcite.sql.dialect.PostgresqlSqlDialect;

public class PostgresqlDdlDialect extends PostgresqlSqlDialect {

  public static final PostgresqlDdlDialect DEFAULT = new PostgresqlDdlDialect(DEFAULT_CONTEXT);

  public PostgresqlDdlDialect(Context context) {
    super(context);
  }

  @Override
  public void unparseCall(SqlWriter writer, SqlCall call, int leftPrec, int rightPrec) {
//    System.out.println("POSTGRESQL DIALECT: " + call + ", kind=" + call.getKind());
    switch (call.getKind()) {
    case COLUMN_DECL:
      unparseColumnDecl(writer, (SqlColumnDeclaration) call, leftPrec, rightPrec);
      break;
    case PRIMARY_KEY:
      unparsePrimaryKey(writer, call, leftPrec, rightPrec);
      break;
    default:
      callSuperUnparseCall(writer, call, leftPrec, rightPrec);
      break;
    }
  }

  private void unparseColumnDecl(SqlWriter writer, SqlColumnDeclaration colDecl, int leftPrec, int rightPrec) {
    if (colDecl instanceof SqlColumnDeclarationAutoincrement) {
      colDecl.name.unparse(writer, 0, 0);
      final String serialTypeName;
      switch (colDecl.dataType.getTypeName().getSimple()) {
      case "SMALLINT": serialTypeName = "SMALLSERIAL"; break;
      case "INT":
      case "INTEGER": serialTypeName = "SERIAL"; break;
      case "BIGINT": serialTypeName = "BIGSERIAL"; break;
      default: throw new UnsupportedOperationException("Unsupported type for autoincrement column: " + colDecl.dataType.getTypeName());
      }
      writer.keyword(serialTypeName);
      if (Boolean.FALSE.equals(colDecl.dataType.getNullable())) {
        writer.keyword("NOT NULL");
      }
      SqlNode expression = colDecl.expression;
      if (expression != null) {
        throw new UnsupportedOperationException("Expression is not allowed for autoincrement column.");
      }
    } else {
      callSuperUnparseCall(writer, colDecl, leftPrec, rightPrec);
    }
  }

  private void unparsePrimaryKey(SqlWriter writer, SqlCall primaryKey, int leftPrec, int rightPrec) {
    // Override default PRIMARY KEY AUTOINCREMENT - don't write AUTOINCREMENT,
    // because in PostgreSQL it's replaced by SERIAL column type
    if (primaryKey instanceof SqlPrimaryKeyAutoincrement) {
      writer.keyword("PRIMARY KEY");
      ((SqlPrimaryKeyAutoincrement)primaryKey).columnList.unparse(writer, 1, 1);
    } else {
      callSuperUnparseCall(writer, primaryKey, leftPrec, rightPrec);
    }

  }

  private void callSuperUnparseCall(SqlWriter writer, SqlCall call, int leftPrec, int rightPrec) {
    try {
      super.unparseCall(writer, call, leftPrec, rightPrec);
    } catch (UnsupportedOperationException e) {
      call.unparse(writer, leftPrec, rightPrec);
    }
  }
}

