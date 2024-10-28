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
package org.apache.calcite.sql.ddl;

import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Autoincrement column constraint.
 */
public class SqlPrimaryKeyAutoincrement extends SqlKeyConstraint {

  public final SqlNodeList columnList;

  /**
   * Creates a SqlKeyConstraint.
   *
   * @param pos position
   * @param name name
   * @param columnList column list
   */
  SqlPrimaryKeyAutoincrement(SqlParserPos pos, @Nullable SqlIdentifier name,
      SqlNodeList columnList) {
    super(pos, name, columnList);
    this.columnList = columnList;
  }

  @Override public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
    writer.keyword("PRIMARY KEY");
    columnList.unparse(writer, 1, 1);
    writer.keyword("AUTOINCREMENT");
  }

  @Override public SqlOperator getOperator() {
    return SqlKeyConstraintsExtended.PRIMARY_AUTOINCREMENT;
  }
}
