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
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.parser.SqlParserPos;

/**
 * Helper function for creating DDL-related objects.
 */
public class SqlDdlNodesExtended {

  private SqlDdlNodesExtended() {}

  public static SqlKeyConstraint primaryKeyAutoincrement(SqlParserPos pos, SqlIdentifier name,
      SqlNodeList columnList) {
    return new SqlPrimaryKeyAutoincrement(pos, name, columnList);
  }

  /** Creates a CREATE TABLE. */
  public static DdlCreateTable createTable(SqlParserPos pos, boolean replace,
      boolean ifNotExists, SqlIdentifier name, SqlNodeList columnList,
      SqlNode query) {
    return new DdlCreateTable(pos, replace, ifNotExists, name, columnList,
        query);
  }

}
